package com.superc.lib.constants;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by superchen on 2017/6/21.
 * <p>
 * 配置文件类
 */
public class Constants {

    public static class FileConstants {

        public static String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "S-MVP" + File.separator;

        public static String CACHE_DIR = ROOT_DIR + "Cache" + File.separator;
        public static String CACHE_File_DIR = CACHE_DIR + "File" + File.separator;
        public static String CACHE_IMAGE_DIR = CACHE_DIR + "Image" + File.separator;
        public static String CACHE_AUDIO_DIR = CACHE_DIR + "Audio" + File.separator;
        public static String CACHE_MESSAGE_DIR = CACHE_DIR + "Message" + File.separator;
        public static String CACHE_MEDIA_DIR = CACHE_DIR + "Media" + File.separator;
        public static String RESOURCE_DIRECTORY = ROOT_DIR + "Directory" + File.separator;
    }

    public static class HttpConstants {

        /**
         * 服务器地址
         */
        public static String HOST_URL = "http://116.62.19.252:81";

        public static final int DEFAULT_RETRY_COUNT = 3; // 默认重试次数
        public static final long DEFAULT_TIMEOUT_CONNECT = 20; // 默认的超时时间
        public static final long DEFAULT_TIMEOUT_WRITE = 60; // 默认写入时间
        public static final long DEFAULT_TIMEOUT_READ = 30; // 默认读取时间
        public static final int DEFAULT_RETRY_DELAY = 500; // 默认重试延时
        private static final int DEFAULT_RETRY_INCREASEDELAY = 0;   // 默认重试叠加时间
        public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS; // 默认计算时间单位
        public static final long DEFAULT_CACHE_MAX_SIZE = 1024 * 1024 * 50; // 默认缓存大小
    }
}
