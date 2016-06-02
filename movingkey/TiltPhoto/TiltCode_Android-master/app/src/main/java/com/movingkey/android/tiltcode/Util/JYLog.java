package Util;

import android.util.Log;

/**
 * Created by Gyul on 2016-05-24.
 */
public class JYLog {

    public static String TAG = Util.JYLog.class.getSimpleName();

    public static final int MODE_DEBUG = 2;

    public static void D(String msg, Throwable currentThrowable) {
        if (Util.BaseApplication.getDebugMode() == MODE_DEBUG)
            Log.d(TAG, ">>> " + msg + getFileNameAndLineNumber(currentThrowable));
        currentThrowable = null;
    }

    public static void E(String msg, Throwable currentThrowable) {
        if (Util.BaseApplication.getDebugMode() == MODE_DEBUG)
            Log.e(TAG, ">>> " + msg + getFileNameAndLineNumber(currentThrowable));
        currentThrowable = null;
    }

    private static String getFileNameAndLineNumber(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        String fileName = throwable.getStackTrace()[0].getFileName();
        String className = throwable.getStackTrace()[0].getClassName();
        String methodName = throwable.getStackTrace()[0].getMethodName();
        int lineNumber = throwable.getStackTrace()[0].getLineNumber();
        // return "at " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")";
        throwable = null;
        return "> at " + methodName + "(" + fileName + ":" + lineNumber + ")";
    }
}
