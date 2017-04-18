package com.caccommonmodule.util;

import android.util.Log;

/**
 * Created by laiis.li on 2016/12/20.
 */

public class LogUtil {

    private Configuration mConf;

    private static volatile LogUtil sLogUtils;

    private LogUtil() {

    }

    public static void initialLogUtil(Configuration conf) {
        if (sLogUtils == null) {
            synchronized (LogUtil.class) {
                if (sLogUtils == null) {
                    sLogUtils = new LogUtil();
                }
            }
        }

        sLogUtils.setConfiguration(conf);
    }

    public static void log(Class<?> cls, LogType logType, String msg) {
        Configuration conf = sLogUtils.getConfiguration();
        if (!conf.isDebugMode()) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" ---> [ ");
        sb.append(logType.toString());
        sb.append(" ] : ");
        sb.append(msg);
        String log = sb.toString();

        switch (logType) {
            case INFO:
                Log.i(cls.getName(), log);
                break;
            case DEBUG:
                Log.d(cls.getName(), log);
                break;
            case WARNING:
                Log.w(cls.getName(), log);
                break;
            case VERBOSE:
                Log.v(cls.getName(), log);
                break;
            case ERROR:
                Log.e(cls.getName(), log);
                break;
        }
    }

    public static void logDebug(Class<?> cls, String msg) {
        log(cls, LogType.DEBUG, msg);
    }

    public static void logError(Class<?> cls, String msg) {
        log(cls, LogType.ERROR, msg);
    }

    public static void logError(Class<?> cls, Throwable throwable) {
        if (throwable == null) {
            log(cls, LogType.ERROR, " error message: there is a exception.");
            return;
        }

        log(cls, LogType.ERROR, " error message: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void setConfiguration(Configuration conf) {
        this.mConf = conf;
    }

    public Configuration getConfiguration() {
        return mConf;
    }

    public enum LogType {
        DEBUG("DEBUG"), ERROR("ERROR"), VERBOSE("VERBOSE"), INFO("INFO"), WARNING("WARNING");

        private String type;

        LogType(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }
}