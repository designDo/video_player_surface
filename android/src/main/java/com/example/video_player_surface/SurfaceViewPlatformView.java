package com.example.video_player_surface;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

import io.flutter.Log;
import io.flutter.plugin.platform.PlatformView;

// create SurfaceViewPlatformView
class SurfaceViewPlatformView implements PlatformView {
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private boolean firstAttach = true;
    private boolean attachToWindow = true;
    private Context context;

    SurfaceViewPlatformView(Context context) {
        this.context = context;
        this.surfaceView = new SurfaceView(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            addOnAttachStateChangeListener();
        }
        //simple delay play the video
        startPlay(1000);
    }

    private void addOnAttachStateChangeListener() {
        surfaceView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                attachToWindow = true;
                if (!firstAttach) {
                    startPlay(0);
                }
                firstAttach = false;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                attachToWindow = false;
                surfaceView = null;
                mediaPlayer.release();
            }
        });
    }

    private void startPlay(int delay) {
        surfaceView.postDelayed(() -> {
            // 创建MediaPlayer并设置数据源
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceView.getHolder());

            AssetFileDescriptor assetFileDescriptor = null;
            try {
                // 获取MP4文件的AssetFileDescriptor
                assetFileDescriptor = surfaceView.getContext().getAssets().openFd("big_buck_bunny.mp4");
                // 设置MediaPlayer的数据源为AssetFileDescriptor
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                // 准备MediaPlayer
                mediaPlayer.prepare();
                // 开始播放
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 释放AssetFileDescriptor资源
                if (assetFileDescriptor != null) {
                    try {
                        assetFileDescriptor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, delay);
    }

    @Override
    public View getView() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (!attachToWindow && surfaceView == null) {
                surfaceView = new SurfaceView(context);
                Log.e("AAA", "Create new SurfaceView");
                addOnAttachStateChangeListener();
            }
        }
        return surfaceView;
    }

    @Override
    public void dispose() {
        // 清理资源
        mediaPlayer.release();
    }
}