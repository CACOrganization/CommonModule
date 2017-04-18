package com.caccommonmodule.util;
/**
 * Created by laiis.li on 2016/12/20.
 */

public class Configuration {

    private LogUtil.LogType mLogType;
    private boolean mDebugMode;

    private Configuration() {

    }

    private void setType(LogUtil.LogType logType) {
        this.mLogType = logType;
    }

    public void setDebugMode(boolean debug) {
        this.mDebugMode = debug;
    }

    public LogUtil.LogType getType() {
        return mLogType;
    }

    public boolean isDebugMode() {
        return mDebugMode;
    }

    public static class Builder {

        Configuration _Conf;

        public Builder() {
            _Conf = new Configuration();
        }

        public Builder setType(LogUtil.LogType logType) {
            _Conf.setType(logType);
            return this;
        }

        public Builder setDebugMode(boolean isDebug) {
            _Conf.setDebugMode(isDebug);
            return this;
        }

        public Configuration build() {
            return _Conf;
        }
    }
}