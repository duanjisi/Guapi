package com.guapi.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

/**
 * Description : 通知栏通知音
 */
public class NotificationSoundUtil {

    private static NotificationSoundUtil ourInstance;
    private Context context;
    private Uri systemDefaultRingtoneUri;
    private MediaPlayer mMediaPlayer = null;

    public static NotificationSoundUtil get() {
        return ourInstance;
    }

    private NotificationSoundUtil(Context context) {
        this.context = context.getApplicationContext();
        // 获取系统默认铃声的Uri
        this.systemDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
    }

    public static void init(Context context) {
        if (ourInstance == null) {
            ourInstance = new NotificationSoundUtil(context);
        }
    }

    /**
     * 播放系统通知音
     */
    public void startAlarm() {
        play(systemDefaultRingtoneUri);
    }

    /**
     * 播放音效
     *
     * @param uri
     */
    private void play(Uri uri) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = MediaPlayer.create(context, uri);
        mMediaPlayer.setLooping(false);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }
}
