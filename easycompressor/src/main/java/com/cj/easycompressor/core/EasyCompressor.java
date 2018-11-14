package com.cj.easycompressor.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.cj.easycompressor.callback.BatchCompressCallback;
import com.cj.easycompressor.callback.CompressCallback;
import com.cj.easycompressor.config.CompressOptions;
import com.cj.easycompressor.config.ImageOption;
import com.cj.easycompressor.util.CompressUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mayikang on 2018/9/12.
 */

public class EasyCompressor implements IEasyCompressor {
    public static String TAG = "EasyCompressor";
    private static Context mContext;
    private static CompressOptions mOptions;

    private EasyCompressor() {
    }

    /**
     * EasyCompressor的初始化方法，只调用一次即可
     * @param context
     * @param options
     */
    public static void init(@NonNull Context context, @Nullable CompressOptions options) {

        //保证只需要初始化一次
        if(mContext==null){
            mContext = context;
        }


        if(mOptions==null){
            mOptions=options;
        }

    }

    /**
     * EasyCompressor调用入口
     * @param options
     * @return
     */
    public static EasyCompressor with(@Nullable CompressOptions options){
        mOptions=options;//不同使用的地方可能有不同的CompressOptions
        return Holder.instance;
    }

    private static class Holder {
        private static final EasyCompressor instance = new EasyCompressor();
    }

    public static EasyCompressor getInstance() {
        if (mContext == null) {
            try {
                throw new Exception("---请先初始化EasyCompressor---");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Holder.instance;
    }

    public Context getContext() {
        if(mContext==null){
            try {
                throw new Exception("请先初始化EasyCompressor！！！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mContext;
    }

    public CompressOptions getOptions() {
        if (mOptions == null) {
            return new CompressOptions();
        }
        return mOptions;
    }


    @Override
    public void compress(@NonNull final String filePath,@NonNull final CompressCallback callback) {

        Observable.just(filePath).map(new Function<String, File>() {
            @Override
            public File apply(String s) throws Exception {

                String lowerPath = filePath.toLowerCase();
                //图片后缀名
                String suffix = ImageOption.IMG_PNG_SUFFIX;

                if (lowerPath.endsWith(ImageOption.IMG_JPG_SUFFIX)) {
                    suffix = ImageOption.IMG_JPG_SUFFIX;
                } else if (lowerPath.endsWith(ImageOption.IMG_JPEG_SUFFIX)) {
                    suffix = ImageOption.IMG_JPEG_SUFFIX;
                }
                return CompressUtil.create().invokeCompress(filePath, suffix);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                if (file != null) {
                    Log.e(TAG,"--压缩完成--");
                    callback.onSuccess(file);
                } else {
                    callback.onFailed(new Throwable("--压缩后 file == null ！！！--"));
                }
            }
        });
    }

    @Override
    public void batchCompress(@NonNull final List<String> filePaths,@NonNull final BatchCompressCallback callback) {
        final List<File>files=new ArrayList<>();
        final int[] index = {0};
        Observable.fromIterable(filePaths).concatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                return Observable.just(s);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                compress(s, new CompressCallback() {
                    @Override
                    public void onSuccess(File compressedFile) {
                        index[0]++;
                        files.add(compressedFile);
                        if(index[0]==filePaths.size()){
                            callback.onSuccess(files);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        callback.onFailed(throwable);
                    }
                });
            }
        });
    }


}
