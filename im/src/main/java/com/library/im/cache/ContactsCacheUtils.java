package com.library.im.cache;

import android.content.Context;
import android.os.AsyncTask;

import com.library.im.db.UserDao;
import com.library.im.domain.EaseUser;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 目的：编写一套缓存框架，缓存服务器数据到本地，获取数据先从内存读取，内存中不存在时去服务端获取，
 * 为了减轻服务端压力，需要考虑是否可以合并请求。
 * <p>
 * 通用性考虑，保存的数据采用本地冗余5个字段参数，用户自定义
 * <p>
 * <p>
 * Created by longbh on 16/5/25.
 */
public class ContactsCacheUtils {

    public static long expireTime = 2 * 24 * 60 * 60 * 1000;
    private static ContactsCacheUtils cache;

    private DbCache dbCache;   //内存缓存
    private UserDao daoHelper;
    //指定数据的所有者
    private String owner;
    private Process process;
    private Boolean pause = false;

    public static final Executor DUAL_THREAD_EXECUTOR = Executors
            .newFixedThreadPool(2);
    private final Object mPauseWorkLock = new Object();

    public static ContactsCacheUtils getInstance(Context context) {
        if (cache == null) {
            cache = new ContactsCacheUtils(context);
        }
        return cache;
    }

    public ContactsCacheUtils(Context context) {
        dbCache = new DbCache();
        daoHelper = new UserDao(context);
    }

    /**
     * 设置缓存所有这，也可以
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
        rebuildData();
    }

    private void rebuildData() {
        dbCache.get().evictAll();
        daoHelper.loadCache(dbCache.get());
    }

    public void addCache(String key, EaseUser entity) {
        dbCache.put(key, entity);
    }

    public void loadText(String key, RecycleTextView textView) {
        if (key == null) {
            return;
        }
        EaseUser entity = dbCache.get(key);
        if (entity == null) {
            entity = daoHelper.get(key);
            if (entity == null || entity.getNick() == null) {
                //启动服务器请求
                final EntityWorkerTask task = new EntityWorkerTask(key, textView);
                if (textView instanceof RecycleTextView) {
                    ((RecycleTextView) textView).setTast(task);
                    task.executeOnExecutor(DUAL_THREAD_EXECUTOR);
                } else {
                    textView.loadAvatar(entity.getNick(), entity.getAvatar());
                }
            } else {
                textView.loadAvatar(entity.getNick(), entity.getAvatar());
            }
        } else {
            textView.loadAvatar(entity.getNick(), entity.getAvatar());
        }
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    public class EntityWorkerTask extends AsyncTask<Void, Void, EaseUser> {
        private Object mData;
        private final WeakReference<RecycleTextView> imageViewReference;

        public EntityWorkerTask(Object data, RecycleTextView imageView) {
            mData = data;
            imageViewReference = new WeakReference<RecycleTextView>(imageView);
            if (imageView != null) {
                imageView.reset();
            }
        }

        /**
         * Background processing.
         */
        @Override
        protected EaseUser doInBackground(Void... params) {
            //BEGIN_INCLUDE(load_bitmap_in_background)

            final String dataString = String.valueOf(mData);
            EaseUser entity = null;
            // Wait here if work is paused and the task is not cancelled
            synchronized (mPauseWorkLock) {
                while (pause && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }


            // If the bitmap was not found in the cache and this task has not been cancelled by
            // another thread and the ImageView that was originally bound to this task is still
            // bound back to this task and our "exit early" flag is not set, then call the main
            // process method (as implemented by a subclass)
            if (entity == null && !isCancelled() && getAttachedTextView() != null && process != null) {
                entity = process.process(dataString);
            }

            // If the bitmap was processed and the image cache is available, then add the processed
            // bitmap to the cache for future use. Note we don't check if the task was cancelled
            // here, if it was, and the thread is still running, we may as well add the processed
            // bitmap to our cache as it might be used again in the future
            if (entity != null) {
                //添加到缓存
                dbCache.put(dataString, entity);
                daoHelper.saveContact(entity);
            }
            return entity;
            //END_INCLUDE(load_bitmap_in_background)
        }

        /**
         * Once the image is processed, associates it to the imageView
         */
        @Override
        protected void onPostExecute(EaseUser value) {
            //BEGIN_INCLUDE(complete_background_work)
            // if cancel was called on this task or the "exit early" flag is set then we're done
            if (isCancelled()) {
                value = null;
            }

            final RecycleTextView textView = getAttachedTextView();
            if (value != null && textView != null) {
                textView.loadAvatar(value.getNick(), value.getAvatar());
            } else {
                if (textView != null) {
                    textView.reset();
                }
            }
            //END_INCLUDE(complete_background_work)
        }

        @Override
        protected void onCancelled(EaseUser value) {
            super.onCancelled(value);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the ImageView's task still
         * points to this task as well. Returns null otherwise.
         */
        private RecycleTextView getAttachedTextView() {
            final RecycleTextView imageView = imageViewReference.get();
            final EntityWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    private static EntityWorkerTask getBitmapWorkerTask(RecycleTextView textView) {
        if (textView != null) {
            if (textView instanceof RecycleTextView) {
                final RecycleTextView asyncDrawable = (RecycleTextView) textView;
                return asyncDrawable.get();
            }
        }
        return null;
    }

    public void setProcess(Process process) {
        this.process = process;
    }


}
