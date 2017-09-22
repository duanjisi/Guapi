package com.zijing.sharesdk;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ewuapp.framework.common.utils.SdCardUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description: 图片剪切
 */
public class BitmapUtil {

    public static Bitmap decodeBitmapResourse(Context context, int resourceId) {
        Resources resource = context.getResources();
        return BitmapFactory.decodeResource(resource, resourceId);
    }

    /**
     * 保存图片，默认图片名为：时间.png
     */
    public static boolean saveImage(Bitmap bitmap,String path) {
        String suffixName = ".png";
        File paintpad = new File(SdCardUtil.DEFAULT_PHOTO_PATH);
        try {
            if (!paintpad.exists()) {
                paintpad.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    new FileOutputStream(SdCardUtil.DEFAULT_PHOTO_PATH + path + suffixName));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * bitmap转为byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    /**
     * 根据路径从sd卡读取位图
     *
     * @param filePath
     * @return
     */
    public static Bitmap decodeBitmapSd(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            FileDescriptor fileDescriptor = inputStream.getFD();
            bitmap = decodeBitmap(fileDescriptor);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("FileNotFound", filePath + "not found");
        } catch (IOException e) {
            Log.e("IOException", filePath + "read error");
        }
        return bitmap;
    }

    /**
     * 图片读取
     *
     * @param desc
     * @return
     */
    public static Bitmap decodeBitmap(FileDescriptor desc) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        try {
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bmp = BitmapFactory.decodeFileDescriptor(desc, null, opts);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
