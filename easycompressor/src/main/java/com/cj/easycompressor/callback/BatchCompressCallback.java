package com.cj.easycompressor.callback;

import java.io.File;
import java.util.List;

/**
 * Created by mayikang on 2018/9/18.
 */

//批量压缩结果接口
public interface BatchCompressCallback extends IBaseCallback{

    void onSuccess(List<File> files);

    void onFailed(Throwable throwable);

}
