package org.tvapp.utils;

import android.util.Log;

import org.tvapp.base.Constants;


public class LogUtils {

    private static final String mTag = Constants.TAG;

    public static final int LEVEL_OFF = 0;

    public static final int LEVEL_ALL = 7;

    public static final int LEVEL_VERBOSE = 1;

    public static final int LEVEL_DEBUG = 2;

    public static final int LEVEL_INFO = 3;

    public static final int LEVEL_WARN = 4;

    public static final int LEVEL_ERROR = 5;

    public static final int LEVEL_SYSTEM = 6;

    private static int mDebuggable = LEVEL_ALL;

    public static void setDebuggable(int debuggable) {
        mDebuggable = debuggable;
    }


    public static void v(String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(mTag, msg);
        }
    }

    public static void d(String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(mTag, msg);
        }
    }

    public static void i(String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            LogUtils.i(mTag, msg);
        }
    }

    public static void w(String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, msg);
        }
    }

    public static void w(Throwable tr) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, "", tr);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {
            Log.w(mTag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, msg);
        }
    }

    public static void s(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            System.out.println(msg);
        }
    }

    public static void e(Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            Log.e(mTag, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(tag, msg);
        }
    }

}
