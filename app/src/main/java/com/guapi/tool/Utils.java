package com.guapi.tool;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Johnny on 2017-09-06.
 */
public class Utils {
    private static int ScreenHeight;
    private static int ScreenWidth;

    public static float getScreenPx(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ScreenHeight = dm.heightPixels;
        float density = dm.density;
        return ScreenHeight * density;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ScreenHeight = dm.heightPixels;
        return ScreenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ScreenWidth = dm.widthPixels;
        return ScreenWidth;
    }

    /**
     * 转换dip为px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 转换px为dip
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    public static Bitmap changeRoation(Context context, Bitmap bmp, int orientations, int edgeLength) {
//        int edgeLength = length - Utils.dip2px(context, 10);
        Matrix matrixs = new Matrix();
        if (orientations > 325 || orientations <= 45) {
            Log.v("time", "Surface.ROTATION_0;" + orientations);
            matrixs.setRotate(90);
        } else if (orientations > 45 && orientations <= 135) {
            Log.v("time", " Surface.ROTATION_270" + orientations);
            matrixs.setRotate(180);
        } else if (orientations > 135 && orientations < 225) {
            Log.v("time", "Surface.ROTATION_180;" + orientations);
            matrixs.setRotate(270);
        } else {
            Log.v("time", "Surface.ROTATION_90" + orientations);
            matrixs.setRotate(0);
        }
//        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrixs, true);
        bmp = Bitmap.createBitmap(bmp, 0, 0, edgeLength, edgeLength, matrixs, true);
        return bmp;
    }

    public static Bitmap changRoation(Context context, Bitmap bmp, int orientations) {
//        int bW = 0;
//        int bH = 0;
//        int width = getScreenWidth(context);
//        int height = getScreenHeight(context);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
//        if (w < width) {
//            bW = w;
//        } else {
//            bW = width;
//        }
//        if (h < height) {
//            bH = h;
//        } else {
//            bH = height;
//        }

        Matrix matrixs = new Matrix();
        if (orientations > 325 || orientations <= 45) {
            Log.v("time", "Surface.ROTATION_0;" + orientations);
            matrixs.setRotate(90);
        } else if (orientations > 45 && orientations <= 135) {
            Log.v("time", " Surface.ROTATION_270" + orientations);
            matrixs.setRotate(180);
        } else if (orientations > 135 && orientations < 225) {
            Log.v("time", "Surface.ROTATION_180;" + orientations);
            matrixs.setRotate(270);
        } else {
            Log.v("time", "Surface.ROTATION_90" + orientations);
            matrixs.setRotate(0);
        }
        bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrixs, true);
//        bmp = Bitmap.createBitmap(bmp, 0, 0, edgeLength, edgeLength, matrixs, true);
        return bmp;
    }
//    /**
    //     * 按正方形裁切图片
    //     */
//    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled, int edgeLength) {
//        if (bitmap == null) {
//            return null;
//        }
//        int w = bitmap.getWidth(); // 得到图片的宽，高
//        int h = bitmap.getHeight();
//
//        int longerEdge = (int) (edgeLength * Math.max(w, h) / Math.min(w, h));
//        int scaledWidth = w > h ? longerEdge : edgeLength;
//        int scaledHeight = w > h ? edgeLength : longerEdge;
//
//
//        //从图中截取正中间的正方形部分。
//        int xTopLeft = (w - edgeLength) / 2;
//        int yTopLeft = (h - edgeLength) / 2;
//
////        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
////       Log.i("info", "========wh:" + wh);
////        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
////        int retY = w > h ? 0 : (h - w) / 2;
//
//        Bitmap bmp = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, edgeLength, edgeLength, null,
//                false);
//        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
//                && !bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }
//        // 下面这句是关键
//        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
//        // false);
//    }

//    /**
//     * 按正方形裁切图片
//     */
//    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {
//
//        if (bitmap == null) {
//            return null;
//        }
//
//        int w = bitmap.getWidth(); // 得到图片的宽，高
//        int h = bitmap.getHeight();
//
//        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
//
//        Log.i("info", "========wh:" + wh);
//        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
//        int retY = w > h ? 0 : (h - w) / 2;
//
//        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, 300, 300, null,
//                false);
//        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
//                && !bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }
//
//        // 下面这句是关键
//        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
//        // false);
//    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Context context, Bitmap bitmap, boolean isRecycled, int edgeLength) {
//        int edgeLength = length - Utils.dip2px(context, 10);
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

//        int longerEdge = (int) (edgeLength * Math.max(w, h) / Math.min(w, h));
//        int scaledWidth = w > h ? longerEdge : edgeLength;
//        int scaledHeight = w > h ? edgeLength : longerEdge;

        //从图中截取正中间的正方形部分。
        int xTopLeft = (w - edgeLength) / 2;
        int yTopLeft = (h - edgeLength) / 2;
//        String str = "xTopLeft:" + xTopLeft + "yTopLeft:" + yTopLeft + "\n" + "edgeLength:" + edgeLength;
//        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        Bitmap bmp = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, edgeLength, edgeLength, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        // 下面这句是关键
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
        // false);
    }

    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap
     * @param num1   长边的比例
     * @param num2   短边的比例
     * @return
     */

    public static Bitmap ImageCrop(Bitmap bitmap, int num1, int num2,
                                   boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h) {
            if (h > w * num2 / num1) {
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else {
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else {
            if (w > h * num2 / num1) {
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else {
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }


    /*
       *获取拍照之后的尺寸
       */
    public static Camera.Size getPictureSize(Context context, List<Camera.Size> sizes) {

        int screenWidth = getScreenWidth(context);
        int index = -1;

        for (int i = 0; i < sizes.size(); i++) {
            Log.i("info", "=============width:" + sizes.get(i).width + "\n" + "sizes.get(i).height" + sizes.get(i).height);
            if (Math.abs(screenWidth - sizes.get(i).width) == 0) {
                index = i;
                break;
            }
        }
        // 当未找到与手机分辨率相等的数值,取列表中间的分辨率
        if (index == -1) {
            index = sizes.size() / 2;
        }
        return sizes.get(index);
    }

    /**
     * 设置 camera 视大小
     */
    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.2;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            Log.d("dd", "Checking size " + size.width + "w " + size.height + "h");
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the
        // requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static int getZoomRank(double distance) {
        int rank = 0;
        if (distance < 20) {
            rank = 19;
        } else if (distance >= 20 && distance < 25) {
            rank = 18;
        } else if (distance >= 25 && distance < 50) {
            rank = 17;
        } else if (distance >= 50 && distance < 100) {
            rank = 16;
        } else if (distance >= 100 && distance < 200) {
            rank = 15;
        } else if (distance >= 200 && distance < 500) {
            rank = 14;
        } else if (distance >= 500 && distance < 1000) {
            rank = 13;
        } else if (distance >= 1000 && distance < 2000) {
            rank = 12;
        } else if (distance >= 2000 && distance < 5000) {
            rank = 11;
        } else if (distance >= 5000 && distance < 10000) {
            rank = 10;
        } else if (distance >= 10000 && distance < 20000) {
            rank = 9;
        } else if (distance >= 20000 && distance < 30000) {
            rank = 8;
        } else if (distance >= 30000 && distance < 50000) {
            rank = 7;
        } else if (distance >= 50000 && distance < 100000) {
            rank = 6;
        } else if (distance >= 100000 && distance < 200000) {
            rank = 5;
        } else if (distance >= 200000 && distance < 500000) {
            rank = 4;
        } else if (distance >= 500000 && distance < 1000000) {
            rank = 3;
        } else if (distance >= 1000000) {
            rank = 3;
        }
        return rank;
    }


    public static void saveVideoFile(Context context, String path, long paramLong) {
        File paramFile = new File(path);
        if (!paramFile.exists()) {
            return;
        }
        String fileName = paramFile.getName();
        String fileType = fileName.substring(fileName.indexOf("."), fileName.length());
        Log.i("info", "====================fileName:" + fileName + "\n" + "fileType:" + fileType);

        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/" + fileType);
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static String getDateTimeStr(long time) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(time));
        return date1;// 2012-10-03 23:41:31
    }

    public static String getDate(String time) {
        long a = Long.parseLong(time);
        long b = (long) (a * 1000.0);
        return getDateTimeStr(b);
    }

    public static String getTimeStr(String time) {
        String str = "";
        long[] times = getDistanceTimes(time, getDate("" + (System.currentTimeMillis() / 1000)));
        if (times[0] < 1) {
            if (times[1] < 1) {
                if (times[2] < 1) {
                    if (times[3] < 1) {
                        if (times[4] < 1) {
                            if (times[5] < 1) {
                                str = times[5] + "秒前";
                            } else {
                                str = "刚才";
                            }
                        } else {
                            str = times[4] + "分钟前";
                        }
                    } else {
                        str = times[3] + "小时前";
                    }
                } else {
                    str = times[2] + "天前";
                }
            } else {
                str = times[1] + "月前";
            }
        } else {
            str = times[0] + "年前";
        }
        return str;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{年，月，天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long year = 0;
        long month = 0;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            year = diff / (365 * 30 * 24 * 60 * 60 * 1000);
            month = diff / (30 * 24 * 60 * 60 * 1000);
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {year, month, day, hour, min, sec};
        return times;
    }
}
