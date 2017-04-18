package com.caccommonmodule;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

import com.caccommonmodule.util.Configuration;
import com.caccommonmodule.util.CreateUdidUtil;
import com.caccommonmodule.util.LogUtil;
import com.caccommonmodule.util.SharedPrefUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class CACApplication extends Application {
//    public Object o;
    private float density;
    private String DEVICE_KEY = "DEVICE_ID";

    private SharedPrefUtil mSharedPrefUtil;
    private boolean isDialogCancelable;
    private boolean isDialogOnTouchable;
    private String objectSharepreferenceKey = "hashObject";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        density = getResources().getDisplayMetrics().density;
        LogUtil.initialLogUtil(new Configuration.Builder()
                .setType(LogUtil.LogType.DEBUG)
                .setDebugMode(true)
                .build());

        getSharedPrefUtil().put(objectSharepreferenceKey, 0);
        initDevice();
        checkDB();
        setSetting();
        isDialogCancelable = setDialogCancelable();
        isDialogOnTouchable = isDialogOnTouchable();
    }

    /**
     * 裝置資訊初始化
     */
    protected abstract void initDevice();

    /**
     * 檢查db是否替代或者是否存在
     */
    protected abstract void checkDB();

    /**
     * 如果有登入資訊存在db則,將資訊取回
     */
    protected abstract void setSetting();

    /**
     * 取得db內部版本以做後續比對
     * @param dbDestPath
     * @param dbName
     * @return
     * @throws Exception
     */
    protected abstract String getDBVersionCallBack(String dbDestPath, String dbName) throws Exception;

    /**
     * 將舊的資料更新至新db
     * @param dbDestPath
     * @param dbName
     * @param dbVersion
     */
    protected abstract void updateDBCallBack(String dbDestPath, String dbName, String dbVersion);

    /**
     * 回傳資訊至錯誤信箱
     * @param e
     */
    protected abstract void sendErrorEmail(final Throwable e);

    public int dpToPix(float dp) {
        return (int) (dp * density + 0.5f);
    }

    protected void copyDB(String destPath, String dbName) throws IOException,
            FileNotFoundException {
        InputStream inputStream = getBaseContext().getAssets().open(dbName);
        OutputStream outputStream = new FileOutputStream(destPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();

        File DbF = new File(destPath);
        LogUtil.logError(getClass(), "copyDB : " + dbName + " isExists " + DbF.exists());
        DbF = null;
    }

    /**
     *
     * @return
     */
    protected String getVersionName() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(
                    getClass().getPackage().getName(),
                    PackageManager.GET_META_DATA).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * getDevice custom uuid
     * @return
     */
    protected String getDeviceId() {
        // device_id = Installation.id(this);
        String device_id = getSharedPrefUtil().getStr(DEVICE_KEY);

        if (device_id == null || device_id.equals("")) {
            device_id = CreateUdidUtil.getUdId(this);
            getSharedPrefUtil().put(DEVICE_KEY, device_id);
        }

        return device_id;
    }

    /**
     * 檢查權限是否開啟
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        // 如果使用者的 Android 版本低於 6.0 ，直接回傳 True (在安裝時已授權)
        boolean hasPermission = false;

        int api_version = Build.VERSION.SDK_INT;    //API版本
        String android_version = Build.VERSION.RELEASE;    //Android版本
        if(api_version < Build.VERSION_CODES.M && !android_version.matches("(6)\\..+")) return true;

        int targetSdkVersion = getTargetSDKVersion();

        if (targetSdkVersion >= Build.VERSION_CODES.M) {
            // targetSdkVersion >= Android M, we can
            // use Context#checkSelfPermission
            for(String permission : permissions){
                hasPermission = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if(!hasPermission)
                    break;
            }
        } else {
            // targetSdkVersion < Android M, we have to use PermissionChecker
            for(String permission : permissions){
                hasPermission = PermissionChecker.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
                if(!hasPermission)
                    break;
            }
        }

        return hasPermission;
    }

    private int getTargetSDKVersion(){
        int targetSdkVersion = 14;
        try {
            final PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    /**
     * sharepreference Util
     * key "104group"
     * @return SharedPrefUtil
     */
    public SharedPrefUtil getSharedPrefUtil(){
        mSharedPrefUtil = SharedPrefUtil.getInstance(this);
        return mSharedPrefUtil;
    }

    /**
     * sharepreference Util change key
     * @param ShredPrefKeyName
     * @return
     */
    public SharedPrefUtil newSharedPrefUtil(String ShredPrefKeyName){
        mSharedPrefUtil = SharedPrefUtil.newInstance(this, ShredPrefKeyName);
        return mSharedPrefUtil;
    }

    private void createDBFolder(String destPath) {
        File destF = new File(destPath);
        if (!destF.exists()) {
            //20160114
            destF.setReadable(true, true);
            destF.setWritable(true, true);
            boolean isCreated = destF.mkdir();
            LogUtil.logError(getClass(), "checkDB createDBFolder no exists : isCreated = " + isCreated);
        }else {
            LogUtil.logError(getClass(), "checkDB createDBFolder exists");
        }
    }

    private String judgeDB(String dbDestPath, String DB_NAME, String DB_VERSION, boolean isDataData, boolean isUpdate) {
        String CHECK_DB_NAME = DB_NAME;
        try {
            File dbF = new File(dbDestPath);
            if (!dbF.exists()) {
                // db不存在,直接copy過去即可
                LogUtil.logError(getClass(), "judgeDB : " + DB_NAME + " db no exists");
                copyDB(dbDestPath, DB_NAME);
                LogUtil.logError(getClass(), "judgeDB : " + DB_NAME + " db no exists end");
            } else {
                // db已存在,此時需先判斷版本,再決定是否覆蓋過去
                LogUtil.logError(getClass(), "judgeDB db exists");
                try{
                    String currentDBVersion = getDBVersionCallBack(dbDestPath, DB_NAME);
                    if (!currentDBVersion.equals(DB_VERSION)) {
                        if(!isUpdate){
                            // 版本不同,決定先砍檔再copy過去
                            LogUtil.logError(getClass(), "judgeDB : " + DB_NAME + " db exists but version not equal : copyDB");
                            // 1.砍檔
                            dbF.delete();
                            // 2.copy
                            copyDB(dbDestPath, DB_NAME);
                            LogUtil.logError(getClass(), "judgeDB : " + DB_NAME + " db exists but version not equal : copyDB end");
                        }else {
                            //用更新的方式
                            updateDBCallBack(dbDestPath, DB_NAME, DB_VERSION);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    LogUtil.logError(getClass(), "judgeDB : " + DB_NAME + " db exists but has some problem : copyDB");

                    //找不到欄位
                    if(!isUpdate){
                        // 1.砍檔
                        dbF.delete();
                        // 2.copy
                        copyDB(dbDestPath, DB_NAME);
                    }else{
                        //用更新的方式
                        updateDBCallBack(dbDestPath, DB_NAME, DB_VERSION);
                    }

                    LogUtil.logError(getClass(), "judgeDB : " + DB_NAME + " db exists but has some problem : copyDB end");
                }

            }
        } catch (final Exception e) {
            e.printStackTrace();

            LogUtil.logError(getClass(), "judgeDB has some problem : checkDBNoStoragePermission");
            if (isDataData) {
                CHECK_DB_NAME = checkDBNoStoragePermission(DB_NAME, DB_VERSION, isUpdate);
                LogUtil.logError(getClass(), "judgeDB has some problem : checkDBNoStoragePermission end");
            } else
                sendErrorEmail(e);
        }
        return CHECK_DB_NAME;
    }

    /**
     * 檢查databases目錄下是否有my104db,若無則從assets目錄複製過去
     * @param DB_NAME
     * @param DB_VERSION
     * @return
     */
    protected String checkDB(String DB_NAME, String DB_VERSION) {
        // step1 檢查databases目錄是否存在, 若無則建立查
        String destPath = "/data/data/" + getPackageName() + "/databases";
        createDBFolder(destPath);

        // step2 檢查databases目錄下是否有db, 若無則copy過去
        String dbDestPath = "/data/data/" + getPackageName()
                + "/databases/" + DB_NAME;

        return judgeDB(dbDestPath, DB_NAME, DB_VERSION, true, false);
    }

    /**
     * 檢查databases目錄下是否有my104db,若無則從assets目錄複製過去
     * @param DB_NAME
     * @param DB_VERSION
     * @param isUpdate
     * @return
     */
    protected String checkDB(String DB_NAME, String DB_VERSION, boolean isUpdate) {
        // step1 檢查databases目錄是否存在, 若無則建立查
        String destPath = "/data/data/" + getPackageName() + "/databases";
        createDBFolder(destPath);

        // step2 檢查databases目錄下是否有db, 若無則copy過去
        String dbDestPath = "/data/data/" + getPackageName()
                + "/databases/" + DB_NAME;

        return judgeDB(dbDestPath, DB_NAME, DB_VERSION, true, isUpdate);
    }

    /**
     * 檢查databases目錄下是否有my104db,若無則從assets目錄複製過去
     */
    private String checkDBNoStoragePermission(String DB_NAME, String DB_VERSION, boolean isUpdate) {
//        String DB_NAME = DB_NAME_;

        // step1 檢查databases目錄是否存在, 若無則建立查
        String destPath = getExternalFilesDir(null).toString() + "/databases";
        createDBFolder(destPath);

        // step2 檢查databases目錄下是否有db, 若無則copy過去
        String dbDestPath = destPath + "/" + DB_NAME;
//        File dbF = new File(dbDestPath);
//        DB_NAME = dbDestPath;

        judgeDB(dbDestPath, DB_NAME, DB_VERSION, false, isUpdate);

        return dbDestPath;
//                judgeDB(dbDestPath, DB_NAME, DB_VERSION, false, isUpdate);
    }

    public abstract boolean setDialogCancelable();

    public boolean isDialogCancelable(){
        return isDialogCancelable;
    }

    public abstract boolean setDialogOnTouchable();

    public boolean isDialogOnTouchable(){
        return isDialogCancelable;
    }
}