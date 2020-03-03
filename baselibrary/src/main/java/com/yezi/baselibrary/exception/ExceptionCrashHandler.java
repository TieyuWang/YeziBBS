package com.yezi.baselibrary.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author : GW00175635
 * @date : 2020/3/3 10:51
 * desc   : 异常收集，保存到内存卡 需要读取和储存权限
 * version: 1.0
 */
public class ExceptionCrashHandler implements  Thread.UncaughtExceptionHandler {

    private static volatile ExceptionCrashHandler mInstance;

    private final String TAG = "ExceptionCrashHandler";

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private String mDirPath;


    private ExceptionCrashHandler() {

    }

    public static ExceptionCrashHandler getInstance(){
        if(mInstance == null){
            synchronized(ExceptionCrashHandler.class){
                if (mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        mContext = context;
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        mDirPath = getDirPath();
    }

    public HashMap<String,String> getCrashLogFile(){

        File[] files = new File(mDirPath).listFiles();

        if( files!=null ) {
            HashMap<String,String> map = new HashMap<>(files.length);
            for (File file : files) {
          //      Log.d(TAG, "getCrashLogFile: fileName = " + file.getName() + " path = " + file.getAbsolutePath());
                map.put(file.getName(), file.getAbsolutePath());
            }
            return map;
        }
        Log.d(TAG, "getCrashLogFile: no files");
        return null;
    }

    private String getDirPath() {
        File file = mContext.getExternalFilesDir("crash");
        if (file != null) {
            return file.getAbsolutePath();
        }

        File dir = new File(mContext.getFilesDir()+File.separator+"crashLog");
        if(!dir.exists()){
            dir.mkdir();
        }
        Log.d(TAG, "getDirPath: "+dir);
        return dir.getAbsolutePath();
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        saveExceptionFile(thread,throwable);

        if(mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        }
    }

    private void saveExceptionFile(Thread thread, Throwable throwable) {

        String fileName = "Crash_Log_"+getAssignTime("yyyy_MM_dd_HH_mm_ss");

        StringBuilder stringBuilder = new StringBuilder();

        String environmentInfo = obtainEnvironmentInfo(fileName);
        String exceptionInfo = obtainExceptionInfo(throwable);
        stringBuilder.append(environmentInfo);
        stringBuilder.append(exceptionInfo);

       // Log.d(TAG, "saveExceptionFile: "+stringBuilder);

        File logFile = new File(mDirPath+File.separator+fileName);
        Log.d(TAG, "saveExceptionFile: "+logFile);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(logFile);
            fileOutputStream.write(stringBuilder.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String obtainEnvironmentInfo(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n=====================\n");
        stringBuilder.append(fileName);
        stringBuilder.append("\n=====================\n");
        appendApplicationInfo(stringBuilder);
        appendPermissionInfo(stringBuilder);
        appendDeviceInfo(stringBuilder);
        return stringBuilder.toString();
    }

    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }


    /**
     * 返回当前日期根据格式
     **/
    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr, Locale.CHINESE);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    /**
     * 添加当前应用的基本信息
     * @param sb 文本
     */
    private void appendApplicationInfo(StringBuilder sb) {
        sb.append("\n============ApplicationInfo=============\n");
        try {
            PackageManager packageManager  = mContext.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
            sb.append("PACKAGE_NAME = ");
            sb.append(info.packageName);
            sb.append("\nVERSION_NAME = ");
            sb.append(info.versionName);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                sb.append("\nVERSION_CODE = ");
                sb.append(info.versionCode);
            }else{
                sb.append("\nLONG_VERSION_CODE = ");
                sb.append(info.getLongVersionCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("get application info failed");
        }
        sb.append("\n=========================================\n\n");
    }

    /**
     * 添加当前获取权限状态
     * @param sb 文本
     */
    private void appendPermissionInfo(StringBuilder sb) {
        //TODO: 添加当前获取权限状态
    }


    /**
     * 添加当前设备信息
     * @param sb 文本
     */
    private void appendDeviceInfo(StringBuilder sb) {
        sb.append("\nDEVICE_DETAILS_INFO:\n");
        Field[] fields = Build.class.getDeclaredFields();
        try {
        for(Field field : fields){
            field.setAccessible(true);
            sb.append(field.getName());
            sb.append(" = ");
            sb.append(field.get(null));
            sb.append("\n");
        }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("get device info failed");
        }
        sb.append("=========================================\n\n");
    }
}
