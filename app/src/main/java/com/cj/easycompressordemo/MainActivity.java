package com.cj.easycompressordemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cj.easycompressor.callback.BatchCompressCallback;
import com.cj.easycompressor.callback.CompressCallback;
import com.cj.easycompressor.config.CompressOptions;
import com.cj.easycompressor.core.EasyCompressor;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader()).build());

        findViewById(R.id.single).setOnClickListener(this);
        findViewById(R.id.multi).setOnClickListener(this);
        String[] per = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, per, 1);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //压缩一张图片
            case R.id.single:
                Album.album(this) // Image and video mix options.
                        .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
                        .columnCount(3) // The number of columns in the page list.
                        .selectCount(1)  // Choose up to a few images.
                        .camera(true) // Whether the camera appears in the Item.
                        .cameraVideoQuality(1) // Video quality, [0, 1].
                        .cameraVideoLimitDuration(Long.MAX_VALUE) // The longest duration of the video is in milliseconds.
                        .cameraVideoLimitBytes((Long.MAX_VALUE)) // Maximum size of the video, in bytes.
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {

                                if (result != null) {
                                    List<String> paths = new ArrayList<>();
                                    for (AlbumFile file : result) {
                                        paths.add(file.getPath());
                                    }
                                    EasyCompressor.getInstance(null).compress(paths.get(0), new CompressCallback() {
                                        @Override
                                        public void onSuccess(File compressedFile) {
                                            Log.e("gg", "压缩成功：" + compressedFile.getAbsolutePath());
                                        }

                                        @Override
                                        public void onFailed(Throwable throwable) {
                                            if (throwable != null) {
                                                Log.e("gg", throwable.toString());
                                            }
                                        }
                                    });
                                }
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                                // The user canceled the operation.
                            }
                        })
                        .start();
                break;

            //压缩多张
            case R.id.multi:
                Album.album(this) // Image and video mix options.
                        .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
                        .columnCount(3) // The number of columns in the page list.
                        .selectCount(9)  // Choose up to a few images.
                        .camera(true) // Whether the camera appears in the Item.
                        .cameraVideoQuality(1) // Video quality, [0, 1].
                        .cameraVideoLimitDuration(Long.MAX_VALUE) // The longest duration of the video is in milliseconds.
                        .cameraVideoLimitBytes((Long.MAX_VALUE)) // Maximum size of the video, in bytes.
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {

                                if (result != null) {
                                    List<String> paths = new ArrayList<>();
                                    for (AlbumFile file : result) {
                                        paths.add(file.getPath());
                                    }
                                    EasyCompressor.getInstance(null).batchCompress(paths, new BatchCompressCallback() {

                                        @Override
                                        public void onSuccess(List<File> files) {
                                            for (int i = 0; i < files.size(); i++) {
                                                Log.e("gg", "压缩成功：" + files.get(i).getAbsolutePath());
                                            }
                                        }

                                        @Override
                                        public void onFailed(Throwable throwable) {
                                            Log.e("gg", "压缩失败" + throwable.toString());
                                        }
                                    });
                                }
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                                // The user canceled the operation.
                            }
                        })
                        .start();
                break;
        }
    }
}
