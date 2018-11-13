package com.cj.easycompressordemo;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

/**
 * Created by mayikang on 2018/9/20.
 */

public class MediaLoader implements AlbumLoader{


        @Override
        public void load(ImageView imageView, AlbumFile albumFile) {
            load(imageView, albumFile.getPath());
        }

        @Override
        public void load(ImageView imageView, String url) {

                Glide.with(imageView.getContext())
                        .load(url)
                        .into(imageView);
        }

}
