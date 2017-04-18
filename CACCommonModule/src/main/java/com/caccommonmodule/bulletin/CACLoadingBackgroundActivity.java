//package com.caccommonmodule.bulletin;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//
//import com.caccommonmodule.db.PublishDBHelper;
//import com.caccommonmodule.util.DialogHelper;
//import com.google.myjson.JsonObject;
//import com.google.myjson.JsonParser;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created by ac on 2017/1/20.
// */
//
//public abstract class CACLoadingBackgroundActivity extends CACBulletinActivity {
//
//    // private static final int HIDE_LOADING_DIALOG_WAIT_TIME = 3; // 秒
//    protected Context context;
//    private boolean isStopSync = false;
//    private boolean isShowPublish = false;
//    private boolean isClick = false;
//
//    private String publishTitle = null;
//    private String backAndShowAlertDuringNotice = null;
//    private String buttonName1 = null;
//    private String buttonName2 = null;
//    private String buttonLink1 = null;
//    private String buttonLink2 = null;
//    private String PACKAGE_NAME;
//
//    public static boolean DEBUG = true;
//
//    private DialogHelper mDialogHelper;
//
//    /**
//     * 每個產品客製化的json 對其做處理
//     * @param jo_notice
//     */
//    protected abstract void customBullinParser(JsonObject jo_notice);
//
//    /**
//     * 公告後要做的事情 may call Api or doAnotherThing
//     * @throws Exception
//     */
//    protected abstract void afterPublishDoAnotherThing() throws Exception;
//
//    /**
//     * 維修或封站期間的呈現
//     * @param isMaintaining
//     */
//    protected abstract void showMaintaining(boolean isMaintaining);
//
//    /**
//     * 設定要讀取公告的url
//     * @return
//     */
//    protected abstract String setPublishUrl();
//
//    /**
//     * 如果公告要有顯示按鈕需要開啟連結
//     * @param url
//     */
//    protected abstract void openWebView(String url);
//
//    /**
//     * 設定layout resource
//     * @return
//     */
//    protected abstract int getLayoutResId();
//    /**
//     * 初始化view
//     */
//    protected abstract void findView();
//    /**
//     * 設定view功能
//     */
//    protected abstract void setView();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = this;
//        mDialogHelper = new DialogHelper(context);
//        PACKAGE_NAME = getApplicationContext().getPackageName();
//        setContentView(getLayoutResId());
//        findView();
//        setView();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isStopSync = false;
//        if(!isClick)
//            new DoBackgroundTask().execute();
//        isClick = false;
//    }
//
//    @Override
//    public void onBackPressed() {
//        // do nothing to lock back button
//    }
//
//    private class DoBackgroundTask extends
//            AsyncTask<Map<String, String>, String, Boolean> {
//
//        boolean isStopSync = false;
//        protected Boolean doInBackground(Map<String, String>... query) {
//            try {
//                System.out.println("update publish start");
//            /*------------------ 由公告平台取得公告，並確認是否繼續同步 START --------------------*/
//            /*--------------- (1)同步公告版本 START ---------------*/
//                try {
//                    isStopSync = updatePublish(setPublishUrl());
//                } catch (Exception exception) {
//                    System.out.println("exception: " + exception.toString());
//                    exception.printStackTrace();
//                }
//            /*--------------- (1)同步公告版本 END ---------------*/
//            /*------------------ 由公告平台取得公告，並確認是否繼續同步 END --------------------*/
//
//                System.out.println("update publish end");
//                if(!isStopSync)
//                    afterPublishDoAnotherThing();
//
//            } catch (Exception e) {
//
//            } finally {}
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean isTaskSuccess) {
//            showMaintaining(isStopSync);
//
//            // 有公告則show dialog
//            if (backAndShowAlertDuringNotice != null) {
//                if (buttonName2 != null && buttonName2.length() > 0) {
////                    new DialogHelper(context).showAlertDialog(
////                            publishTitle, backAndShowAlertDuringNotice,
////                            buttonName1,
////                            createButtonClickListener(buttonLink1),
////                            buttonName2,
////                            createButtonClickListener(buttonLink2), false);
//                    mDialogHelper.dismissAlerDialog();
//                    mDialogHelper.showAlertDialog(
//                            publishTitle, backAndShowAlertDuringNotice,
//                            buttonName1,
//                            createButtonClickListener(buttonLink1),
//                            buttonName2,
//                            createButtonClickListener(buttonLink2), isStopSync);
//                } else {
////                    new DialogHelper(context).showAlertDialog(
////                            publishTitle, backAndShowAlertDuringNotice,
////                            buttonName1,
////                            createButtonClickListener(buttonLink1), null, null,
////                            false);
//                    mDialogHelper.dismissAlerDialog();
//                    mDialogHelper.showAlertDialog(
//                            publishTitle, backAndShowAlertDuringNotice,
//                            buttonName1,
//                            createButtonClickListener(buttonLink1), null, null,
//                            isStopSync);
//                }
//
//                publishTitle = null;
//                backAndShowAlertDuringNotice = null;
//                buttonName1 = null;
//                buttonName2 = null;
//                buttonLink1 = null;
//                buttonLink2 = null;
//            } else {
//                finishActivity();
//            }
//        }
//    }
//
//    private boolean updatePublish(String url) throws Exception {
////            String productId = MainApp.getInstance().productId;
////            String versionName = MainApp.getInstance().versionName;
////
////            String publishUrl = "/publish/" + productId + "/Android_" + productId + "_" + versionName
////                    + ".txt";
////
////            String publishURL = BaseProxy.MOBILE_PROTOCOL + BaseProxy.MOBILE_SERVER
////                    + publishUrl + "?" + QueryKey.APP_VERSION
////                    + "=" + MainApp.getInstance().versionName;
//
//        String jsonString = doGet(url);
//        System.out.println("------publish: " + url + "\n");
//        System.out.println("------jsonString: " + jsonString);
//
//        JsonParser jsonParser = new JsonParser();
//        JsonObject jo_notice = (JsonObject) jsonParser.parse(jsonString);
//
//        int status = Integer.valueOf(jo_notice.get("status").getAsString());
//
//        String buttonLink1 = "";
//        try{
//            buttonLink1 = jo_notice.get("button_link1").getAsString();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        customBullinParser(jo_notice);
//
//        // status 0:無公告 1:一般公告 2:此版本不維護公告
//        // show_Type 0:每天啟動一次 1:每次啟動 2:只發佈一次
//        if (status == 0) {
//            // 無公告情況下，將原本DB的公告資料　update成不可能發生的時間
//            try {
//                PublishDBHelper.getInstance(context).update("update notice set start = \"201001010000\" , end = \"201001010000\"; ");
////                DBHelper dbHelper = new DBHelper(context, dbName);
////                dbHelper.open();
////                dbHelper.update("update notice set start = \"201001010000\" , end = \"201001010000\"; ");
////                dbHelper.close();
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//                // 最壞情況Exception也不要影響運作ＸＤ
//            }
//            return false;
//        } else if (status == 2) {
//            buttonLink1 = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME + "&feature=search_result";
//        }
//
//
//        Cursor cursor = PublishDBHelper.getInstance(context).select("select show_type , next_show_time , version from notice ");
////        DBHelper dbHelper = new DBHelper(context, dbName);
////        dbHelper.open();
////        Cursor cursor = dbHelper
////                .select("select show_type , next_show_time , version from notice ");
//        cursor.moveToNext();
//        String showType = cursor.getString(0);
//        long next_show_time = cursor.getLong(1);
//        String bulletinVersion = cursor.getString(2);
//        cursor.close();
//
//        // 更新公告
//        String content = jo_notice.get("content").getAsString();
//        String jShowType = jo_notice.get("show_type").getAsString();
//        String jBulletinVersion = jo_notice.get("bulletin_version")
//                .getAsString();
//        if (content != null && content.length() > 0) {
////            content = "</font color='#FFFFFF'>" + content + "</font>";
//        }
//        long currentLong = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmm")
//                .format(new Date()));
//        long notice_start = Long.parseLong(jo_notice.get("start_time")
//                .getAsString());
//        long notice_end = Long.parseLong(jo_notice.get("end_time")
//                .getAsString());
//
//        String f0Str = "";
//        if (currentLong >= notice_start && currentLong <= notice_end) {
//            if (jo_notice.get("f0") != null
//                    && jo_notice.get("f0").getAsString().length() > 0) {
//                if (jo_notice.get("f0").getAsString().equals("1")) {
//                    f0Str += " , f0 = '1' ";
//                } else {
//                    f0Str += " , f0 = '0' ";
//                    isStopSync = true;
//                }
//            }
//        }
//
//        /**
//         *
//         */
//        String updateString = "";
//        if (!isStopSync) {
//            updateString = "update notice set version='"
//                    + jBulletinVersion + "', message=\"" + content
//                    + "\", start='" + jo_notice.get("start_time").getAsString()
//                    + "', end='" + jo_notice.get("end_time").getAsString()
//                    + "', button_name1='"
//                    + jo_notice.get("button_name1").getAsString()
//                    + "' , button_link1 ='" + buttonLink1
//                    + "' , button_name2='"
//                    + jo_notice.get("button_name2").getAsString()
//                    + "' , button_link2 ='"
//                    + jo_notice.get("button_link2").getAsString()
//                    + "' , title ='" + jo_notice.get("title").getAsString()
//                    + "' , show_type='" + jShowType + "'";
//        } else {
//            updateString = "update notice set version='"
//                    + jBulletinVersion + "', message=\"" + content
//                    + "\", start='" + jo_notice.get("start_time").getAsString()
//                    + "', end='" + jo_notice.get("end_time").getAsString()
//                    + "', button_name1='"
//                    + jo_notice.get("button_name1").getAsString()
//                    + "' , button_link1 ='" + buttonLink1
//                    + "' , button_name2='"
//                    + jo_notice.get("button_name2").getAsString()
//                    + "' , button_link2 ='"
//                    + jo_notice.get("button_link2").getAsString()
//                    + "' , title ='" + jo_notice.get("title").getAsString()
//                    + "' , show_type='" + jShowType + "'";
//        }
//        if (showType.equals("0") && !jShowType.equals("0")) {
//            // 如果原本的showType = 0每天啟動一次 改為其他狀態(1:每次啟動 2:只發佈一次)
//            // 把原本的show_date改為20110101
//            updateString += " , next_show_time = 201101010000 ";
//        }
//
//        updateString += f0Str;
//
//        PublishDBHelper.getInstance(context).update(updateString);
//
////        dbHelper.update(updateString);
////        dbHelper.close();
//
//        if (currentLong >= notice_start && currentLong <= notice_end) {
//            // 表示為公告期間
//            // System.out.println("showType: "+jShowType);
//            switch (Integer.parseInt(jShowType)) {
//                case 0: // 0:每天啟動一次
//                    if (currentLong > next_show_time
//                            && jBulletinVersion != bulletinVersion) {
//                        isShowPublish = true;
//                        Calendar c = Calendar.getInstance();
//                        c.add(Calendar.DATE, 1);
//                        String updateNextShowTime = new SimpleDateFormat("yyyyMMdd")
//                                .format(c.getTime()) + "0000";
//
//                        PublishDBHelper.getInstance(context).update(" update notice set next_show_time = "
//                                + updateNextShowTime);
//
////                        dbHelper.open();
////                        dbHelper.update(" update notice set next_show_time = "
////                                + updateNextShowTime);
////                        dbHelper.close();
//                    }
//                    break;
//                case 1: // 1:每次啟動
//                    isShowPublish = true;
//                    break;
//                case 2: // 2:只發佈一次
//                    if (!jBulletinVersion.equals(bulletinVersion)) {
//                        isShowPublish = true;
//                    }
//                    break;
//            }
//        }
//
//        if (isShowPublish) {
//            this.backAndShowAlertDuringNotice = content;
//            this.publishTitle = jo_notice.get("title").getAsString();
//            this.buttonName1 = jo_notice.get("button_name1").getAsString();
//            this.buttonLink1 = buttonLink1;
//            this.buttonName2 = jo_notice.get("button_name2").getAsString();
//            this.buttonLink2 = jo_notice.get("button_link2").getAsString();
//
////            return true;// 直接返回,下面的就不用進行了
//        } else {
//            this.backAndShowAlertDuringNotice = null;
//            this.publishTitle = null;
//            this.buttonName1 = null;
//            this.buttonLink1 = null;
//            this.buttonName2 = null;
//            this.buttonLink2 = null;
//        }
//
//        PublishDBHelper.getInstance(context).close();
//        return isStopSync;
//    }
//
//    public static String doGet(String url) throws Exception {
//        String HTTP_CHARSET = "utf-8";
//        String responsetStr = "";
//        HttpURLConnection huc = null;
//        BufferedReader br = null;
//        try {
//            huc = (HttpURLConnection) new URL(url).openConnection();
////            huc.setRequestProperty("User-Agent", BaseProxy.userAgent);
//            br = new BufferedReader(new InputStreamReader(huc.getInputStream(),
//                    HTTP_CHARSET));
//            String NL = System.getProperty("line.separator");
//            StringBuffer sb = new StringBuffer();
//
//            String temp;
//            while ((temp = br.readLine()) != null) {
//                sb.append(temp + NL);
//            }
//
//            responsetStr = sb.toString();
//            if (DEBUG) {
//                System.out.println(responsetStr);
//            }
//        } finally {
//            if (br != null)
//                br.close();
//            if (huc != null)
//                huc.disconnect();
//        }
//        return responsetStr;
//    }
//
//    private DialogInterface.OnClickListener createButtonClickListener(
//            final String link) {
//        if (link != null && link.length() > 0) {
//            return new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    isClick = true;
//
//                    if (!isStopSync) {
//                        finishActivity();
//                    }
//                    if (link.indexOf("play.google.com") != -1) {
//                        String url = "";
//                        if (Build.VERSION.SDK_INT >= 8) {
//                            url = "market://details?id=" + PACKAGE_NAME;
//                        } else {
//                            url = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME + "&feature=search_result";
//                        }
//                        Intent intent = new Intent(Intent.ACTION_VIEW,
//                                Uri.parse(url));
//                        startActivity(intent);
//                    } else {
//                        // 另開網頁
//                        openWebView(link);
//                    }
//                }
//            };
//        } else {
//            return new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    if (!isStopSync) {
//                        finishActivity();
//                    }
//                }
//            };
//        }
//
//    }
//
//    private void finishActivity(){
//        setResult(RESULT_OK);
//        //close self
//        finish();
//    }
//
//    protected Class<?> setLoadingBulletinActivity(){
//        return getClass();
//    }
//
//    protected void doAnotherThingForBackground2Foreground(){
//
//    }
//
//    protected void loadingBulletinSuceess(){
//
//    }
//
//    protected void networkConnectionFail(){
//
//    }
//}


package com.caccommonmodule.bulletin;

import android.os.Bundle;

import com.google.myjson.JsonObject;

/**
 * Created by ac on 2017/1/20.
 */

public abstract class CACLoadingBackgroundActivity extends CACBulletinActivity {

    private boolean isClick = false;

    /**
     * 每個產品客製化的json 對其做處理
     * @param jo_notice
     */
    protected abstract void customBullinParser(JsonObject jo_notice);

    /**
     * 公告後要做的事情 may call Api or doAnotherThing
     * @throws Exception
     */
    protected abstract void afterPublishDoAnotherThing() throws Exception;

    /**
     * 維修或封站期間的呈現
     * @param isMaintaining
     */
    protected abstract void showMaintaining(boolean isMaintaining);

    /**
     * 設定要讀取公告的url
     * @return url
     */
    protected abstract String setPublishUrl();

    /**
     * 如果公告要有顯示按鈕需要開啟連結
     * @param url
     */
    protected abstract void openWebView(String url);

    /**
     * 設定layout resource
     * @return layout resource
     */
    protected abstract int getLayoutResId();
    /**
     * 初始化view
     */
    protected abstract void findView();
    /**
     * 設定view功能
     */
    protected abstract void setView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        findView();
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isClick)
            new CACLoadingBackgroundTask(context, setPublishUrl()).addListener(mOnBulletinCallBack).execute();
        isClick = false;
    }

    private CACLoadingBackgroundTask.OnBulletinCallBack mOnBulletinCallBack = new CACLoadingBackgroundTask.OnBulletinCallBack() {
        @Override
        public void showMaintaining(boolean isMaintaining) {
            CACLoadingBackgroundActivity.this.showMaintaining(isMaintaining);
        }

        @Override
        public void customBullinParser(JsonObject jo_notice) {
            CACLoadingBackgroundActivity.this.customBullinParser(jo_notice);
        }

        @Override
        public void afterPublishDoAnotherThing() throws Exception {
            CACLoadingBackgroundActivity.this.afterPublishDoAnotherThing();
        }

        @Override
        public void openWebView(String link) {
            CACLoadingBackgroundActivity.this.openWebView(link);
        }

        @Override
        public void onDialogButtonClick() {
            isClick = true;
        }

        @Override
        public void finishActivity() {
            setResult(RESULT_OK);
            //close self
            finish();
        }

        @Override
        public String customUpdateToDB(JsonObject jo_notice, boolean isStopSync) {
            return null;
        }

        @Override
        public void doPublishFail() {

        }
    };

    @Override
    public void onBackPressed() {
        // do nothing to lock back button
    }

    protected Class<?> setLoadingBulletinActivity(){
        return getClass();
    }

    protected void doAnotherThingForBackground2Foreground(){}

    protected void loadingBulletinSuceess(){}

    protected void networkConnectionStatus(boolean isFail){}
}
