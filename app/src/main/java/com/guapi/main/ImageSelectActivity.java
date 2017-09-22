package com.guapi.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guapi.MainActivity;
import com.guapi.R;
import com.guapi.main.base.BaseActivity;
import com.guapi.tool.Utils;
import com.guapi.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Johnny on 2016/7/21.
 */
public class ImageSelectActivity extends BaseActivity {
    private final int MAX_NUM = 9;
    private final int OPEN_CAMERA = 1;//相机
    private final int REQUEST_CLIP_IMAGE = 3;//裁剪
    private String mOutputPath;
    private Uri imageUri;
    private String imageName;
    private String savePath;
    private ImageLoader imageLoader;
    private ArrayList<String> imageUrls, selectedUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    private Button button1;
    private ImageView iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_grid);
        initData();
        initGallery();
    }

    private void initData() {
        selectedUrls = getIntent().getStringArrayListExtra("pices");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        button1 = (Button) findViewById(R.id.button1);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        mOutputPath = new File(getExternalCacheDir(), "chosen.jpg").getPath();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChoosePhotosClick(view);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.imageUrls = new ArrayList<String>();
        imageUrls.add("head");
        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = this
                .getApplicationContext()
                .getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                        null, null, orderBy + " DESC");
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            Log.i("imageUrl", imageUrls.get(i));
        }
    }

    private void initGallery() {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.stub_image)
                .showImageForEmptyUri(R.drawable.image_for_empty_url)
                .cacheInMemory().cacheOnDisc().build();
        imageAdapter = new ImageAdapter(this, imageUrls);
        imageAdapter.matchList(selectedUrls);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    takePhoto();
                }
            }
        });
    }

    private void takePhoto() {
        if (Build.VERSION.SDK_INT < 24) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageName = getNowTime() + ".jpg";
            File dir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir = Environment.getExternalStorageDirectory();
            } else {
                dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
            File file = new File(dir, imageName);
            imageUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, OPEN_CAMERA);
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String imageName = getNowTime() + ".jpg";
            File file = new File(savePath, imageName);
            imageUri = FileProvider.getUriForFile(ImageSelectActivity.this, "com.guapi.fileProvider", file);//这里进行替换uri的获得方式
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
            startActivityForResult(intent, OPEN_CAMERA);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    public void btnChoosePhotosClick(View v) {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
//        showToast("Total photos selected: " + selectedItems.size(), true);
        Log.d(MainActivity.class.getSimpleName(), "Selected Items: "
                + selectedItems.toString());
        Intent intent = new Intent();
        intent.putExtra("images", selectedItems);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Description GridView Adapter
     */
    public class ImageAdapter extends BaseAdapter {
        private float mImageHeight;
        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;
        ArrayList<String> mSelectList;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mImageHeight = (Utils.getScreenWidth(context) - Utils.dip2px(context, 60)) / 3;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<String>();
            mSelectList = new ArrayList<String>();
            this.mList = imageList;
        }

        //        public ArrayList<String> getCheckedItems() {
//            ArrayList<String> mTempArry = new ArrayList<String>();
//            for (int i = 0; i < mList.size(); i++) {
//                if (mSparseBooleanArray.get(i)) {
//                    mTempArry.add(mList.get(i));
//                }
//            }
//            return mTempArry;
//        }
        public ArrayList<String> getCheckedItems() {
            return mSelectList;
        }


        public void matchList(ArrayList<String> list) {
            if (list != null && list.size() != 0) {
                mSelectList.addAll(list);
                if (imageUrls != null && imageUrls.size() != 0) {
                    for (int i = 0; i < imageUrls.size(); i++) {
                        String image = imageUrls.get(i);
                        for (int j = 0; j < list.size(); j++) {
                            if (image.equals(list.get(j))) {
                                mSparseBooleanArray.put(i, true);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String image = imageUrls.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_multiphoto_item,
                        null);
            }
            CheckBox mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkBox1);
            RelativeLayout rl_head = (RelativeLayout) convertView
                    .findViewById(R.id.rl_head);
            final ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            imageView.getLayoutParams().width = (int) mImageHeight;
            imageView.getLayoutParams().height = (int) mImageHeight;
            if (image.equals("head")) {
                mCheckBox.setVisibility(View.GONE);
                imageView.setVisibility(View.INVISIBLE);
                rl_head.setVisibility(View.VISIBLE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);
                rl_head.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageLoader.displayImage("file://" + imageUrls.get(position),
                        imageView, ImageLoaderUtils.getDisplayImageOptions(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                Animation anim = AnimationUtils.loadAnimation(
                                        context, R.anim.fade_in);
                                imageView.setAnimation(anim);
                                anim.start();
                            }
                        });
                mCheckBox.setTag(position);
                mCheckBox.setChecked(mSparseBooleanArray.get(position));
                mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            }
            return convertView;
        }

        OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                int size = mSelectList.size();
                if (size >= MAX_NUM) {
                    showToast("最多选9张图片!", true);
                    return;
                }
                // TODO Auto-generated method stub
                mSparseBooleanArray.put((Integer) buttonView.getTag(),
                        isChecked);
                int position = (Integer) buttonView.getTag();
                String name = mList.get(position);
                if (isChecked) {
                    if (!mSelectList.contains(name)) {
                        mSelectList.add(name);
                    }
                } else {
                    if (mSelectList.contains(name)) {
                        mSelectList.remove(name);
                    }
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CAMERA && resultCode == RESULT_OK) {    //打开相机
            if (imageUri != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 24) {
                    path = Utils.getPath(this, imageUri);
                } else {
                    path = imageUri.toString();
                }
                if (Build.VERSION.SDK_INT >= 24 && path.contains("com.atgc.cotton.fileProvider") &&
                        path.contains("/IMProject/")) {
                    String[] arr = path.split("/IMProject/");
                    if (arr != null && arr.length > 1) {
                        path = savePath + arr[1];
                    }
                }
                if (!TextUtils.isEmpty(path)) {
                    ClipImageActivity.prepare()
                            .aspectX(2).aspectY(2)//裁剪框横向及纵向上的比例
                            .inputPath(path).outputPath(mOutputPath)//要裁剪的图片地址及裁剪后保存的地址
                            .startForResult(this, REQUEST_CLIP_IMAGE);
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CLIP_IMAGE) {
            String path = ClipImageActivity.ClipOptions.createFromBundle(data).getOutputPath();
            if (!TextUtils.isEmpty(path)) {
                ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
                selectedItems.add(path);
                Intent intent = new Intent();
                intent.putExtra("images", selectedItems);
                setResult(RESULT_OK, intent);
                finish();
            }
            return;
        }
    }
}
