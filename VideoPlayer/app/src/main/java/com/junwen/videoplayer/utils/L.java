
package com.junwen.videoplayer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述 :日志类
 * 作者 :卜俊文
 * 时间 :2016/2/28 19:12
 */

@SuppressLint("SimpleDateFormat")
public class L {
    private static final String TAG = L.class.getSimpleName();

    private L() {/* 禁止实例化 */
    }

    public static void v() {
        log(Log.VERBOSE, null, null);
    }

    public static void v(String msg) {
        log(Log.VERBOSE, msg, null);
    }

    public static void v(Throwable throwable) {
        log(Log.VERBOSE, null, throwable);
    }

    public static void v(String msg, Throwable throwable) {
        log(Log.VERBOSE, msg, throwable);
    }

    public static void i() {
        log(Log.INFO, null, null);
    }

    public static void i(String msg) {
        log(Log.INFO, msg, null);
    }

    public static void i(Throwable throwable) {
        log(Log.INFO, null, throwable);
    }

    public static void i(String msg, Throwable throwable) {
        log(Log.INFO, msg, throwable);
    }

    public static void d() {
        log(Log.DEBUG, null, null);
    }

    public static void d(String msg) {
        log(Log.DEBUG, msg, null);
    }

    public static void d(Throwable throwable) {
        log(Log.DEBUG, null, throwable);
    }

    public static void d(String msg, Throwable throwable) {
        log(Log.DEBUG, msg, throwable);
    }

    public static void w() {
        log(Log.WARN, null, null);
    }

    public static void w(String msg) {
        log(Log.WARN, msg, null);
    }

    public static void w(Throwable throwable) {
        log(Log.WARN, null, throwable);
    }

    public static void w(String msg, Throwable throwable) {
        log(Log.WARN, msg, throwable);
    }

    public static void e() {
        log(Log.ERROR, null, null);
    }

    public static void e(String msg) {
        log(Log.ERROR, msg, null);
    }

    public static void e(Throwable throwable) {
        log(Log.ERROR, null, throwable);
    }

    public static void e(String msg, Throwable throwable) {
        log(Log.ERROR, msg, throwable);
    }

    private static final String LOG_FILE_EXTENSION = ".log";

    private static boolean isToLogcat = true;

    private static boolean isToFile = false;

    /**
     * log文件路径
     */
    private static String filePath = "";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat DATE_TIME_FORMAT =
            new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss] ");

    private static final String DATE_TIME_PLACEHOLDER = "                      ";

    private static void log(int logLevel, String msg, Throwable throwable) {

        if (isLogcatEnabled() || isLogToFileEnabled()) {

            StackTraceElement element = Thread.currentThread().getStackTrace()[4];
            String tag = "info";
            String codeLocation = getCodeLocation(element);

            /** 根据内容处理打印排版 */
            if (msg == null && throwable == null) {
                msg = tag + " => " + codeLocation;
            } else if (msg == null && throwable != null) {
                msg = "";
            } else if (msg != null && throwable == null) {
                if (TextUtils.isEmpty(msg)) {
                    msg = "\"\"";
                }
                msg = msg + " => " + codeLocation;
            } else if (msg != null && throwable != null) {
                // Nothing
            }

            if (isLogcatEnabled()) {
                switch (logLevel) {
                    case Log.VERBOSE:
                        Log.v(tag, msg, throwable);
                        // com.readystatesoftware.notificationlog.Log.v(tag, msg, throwable);
                        break;

                    case Log.INFO:
                        Log.i(tag, msg, throwable);
                        // com.readystatesoftware.notificationlog.Log.i(tag, msg, throwable);
                        break;

                    case Log.DEBUG:
                        Log.d(tag, msg, throwable);
                        // com.readystatesoftware.notificationlog.Log.d(tag, msg, throwable);
                        break;

                    case Log.WARN:
                        Log.w(tag, msg, throwable);
                        // com.readystatesoftware.notificationlog.Log.w(tag, msg, throwable);
                        break;

                    case Log.ERROR:
                        Log.e(tag, msg, throwable);
                        // com.readystatesoftware.notificationlog.Log.e(tag, msg, throwable);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * @return 格式类似于: TestLogActivity.onClick
     */
    private static String getTag(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName().substring(
                stackTraceElement.getClassName().lastIndexOf(".") + 1) + "."
                + stackTraceElement.getMethodName() + "()";
    }

    /**
     * @return 格式类似于: at me.fantouch.demo.TestLogActivity$2.onClick(TestLogActivity.java:47) <br>
     * 这样的格式可以实现eclipse双击转跳到源码相应位置
     */
    private static String getCodeLocation(StackTraceElement stackTraceElement) {
        return "at "
                + stackTraceElement.getClassName()
                + "."
                + stackTraceElement.getMethodName()
                + "("
                + stackTraceElement.getFileName()
                + ":"
                + stackTraceElement.getLineNumber()
                + ")";
    }


    /**
     * 设置是否把日志输出到Logcat,建议在Application里面设置
     */
    public static void setLogcatEnable(boolean isEnable) {
        setLogcatEnable(isEnable);
    }

    public static boolean isLogcatEnabled() {
        return isToLogcat;
    }

    /**
     * 设置是否保存日志到文件,文件所在目录示例/data/data/packageName/file/xx.log
     *
     * @param enable 缺省false
     */
    public static void setLogToFileEnable(boolean enable, Context ctx) {
        setLogToFileEnable(enable, ctx, null);
    }

    /**
     * 设置是否保存日志到文件,并指定文件路径
     *
     * @param enable 缺省false
     */
    public static void setLogToFileEnable(boolean enable, Context ctx, String path) {
        if (enable) {
            if (!isToFile) {
                isToFile = true;
                if (!TextUtils.isEmpty(path)) {
                    filePath = path;
                } else {
                    filePath = ctx.getFilesDir().getAbsolutePath();
                }
                Log.v(TAG, "Save Log To File Enabled");
            } else {
                Log.w(TAG, "Save To File Already Enabled");
            }
        } else {
            if (isToFile) {
                isToFile = false;
                Log.v(TAG, "Save Log To File Disabled");
            } else {
                Log.w(TAG, "Save To File Already Disabled");
            }
        }
    }

    public static boolean isLogToFileEnabled() {
        return isToFile;
    }



    // TODO 异步操作
    private static void writeFile(String time, String tag, String msg) {
        File file = null;
        try {
            file = getLogFile();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
            reader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(msg.getBytes())));

            boolean isFirstLoop = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLoop) {
                    isFirstLoop = false;
                    writer.append(time);
                    writer.append(tag);
                    writer.append("\n");
                }

                if (!TextUtils.isEmpty(line)) {
//                    writer.append(DATE_TIME_PLACEHOLDER);
                }

                writer.append(line);

                if (!TextUtils.isEmpty(line)) {
                    writer.append("\n");
                }

            }
            writer.append("\r\n");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private static File getLogFile() throws IOException {
        File file = new File(filePath,
                DATE_FORMAT.format(new Date(System.currentTimeMillis())) + LOG_FILE_EXTENSION);
        return creatFileIfNotExists(file);
    }

    private static File getCfgFile() throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "log.cfg");
        return creatFileIfNotExists(file);
    }

    /**
     * @return null if IOException
     */
    private static File creatFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            new File(filePath).mkdirs();
            file.createNewFile();
        }
        return file;
    }
}
