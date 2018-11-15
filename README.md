# EasyCompressor
Android图片压缩库
  
  
      #featur
      1、支持单张图片压缩
      2、支持多张图片批量压缩
      3、结合Rxjava2实现异步压缩，保证压缩顺序，结果回调到主线程
      
      #useage
      1、在Application中初始化
         EasyCompressor.init(getApplicationContext(),new CompressOptions(1024*1024*3,true,75));
         
      2、直接使用
      
         //a.默认的压缩配置（压缩到1.5M以下，压缩后质量不低于65%）
         EasyCompressor.getInstance().compress("file://xxxxx.png", new CompressCallback() {
            @Override
            public void onSuccess(File compressedFile) {
                
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
           
          //批量压缩
          EasyCompressor.getInstance().batchCompress(files, new BatchCompressCallback() {
            @Override
            public void onSuccess(List<File> files) {
                
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });


        
         //b.自定义压缩配置
         EasyCompressor.with(new CompressOptions(1024*1024*10,false,50)).
                compress("file://xxxxx.jpg", new CompressCallback() {
                    @Override
                    public void onSuccess(File compressedFile) {
                        
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });

