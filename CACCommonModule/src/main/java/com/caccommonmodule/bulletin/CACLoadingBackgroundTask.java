package com.caccommonmodule.bulletin;

/**
 * Created by ac on 2017/1/24.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.caccommonmodule.CACApplication;
import com.caccommonmodule.db.PublishDBHelper;
import com.caccommonmodule.util.DialogHelper;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CACLoadingBackgroundTask extends
        AsyncTask<Map<String, String>, String, Boolean> {

    protected Context context;
    private boolean isStopSync = false;
    private boolean isShowPublish = false;

    private String publishTitle = null;
    private String backAndShowAlertDuringNotice = null;
    private String buttonName1 = null;
    private String buttonName2 = null;
    private String buttonLink1 = null;
    private String buttonLink2 = null;
    private String PACKAGE_NAME;
    private String url = "";

    public static boolean DEBUG = true;
    protected DialogHelper mDialogHelper;

    private OnBulletinCallBack mOnBulletinCallBack;

    public CACLoadingBackgroundTask(Context context) {
        this.context = context;
        mDialogHelper = new DialogHelper(context, getMainApp(context).isDialogCancelable());
        PACKAGE_NAME = context.getApplicationContext().getPackageName();
    }

    public CACLoadingBackgroundTask(Context context, String url) {
        this.context = context;
        this.url = url;
        mDialogHelper = new DialogHelper(context, getMainApp(context).isDialogCancelable());
        PACKAGE_NAME = context.getApplicationContext().getPackageName();
    }

    private CACApplication getMainApp(Context context){
        return (CACApplication)context.getApplicationContext();
    }

//    public CACLoadingBackgroundTask(Context context, String url, OnBulletinCallBack mOnBulletinCallBack) {
//        this(context, url);
//        this.mOnBulletinCallBack = mOnBulletinCallBack;
//    }

    public CACLoadingBackgroundTask addListener(OnBulletinCallBack mOnBulletinCallBack) {
        this.mOnBulletinCallBack = mOnBulletinCallBack;
        return this;
    }

    protected Boolean doInBackground(Map<String, String>... query) {
        try {
            System.out.println("update publish start");
            /*------------------ 由公告平台取得公告，並確認是否繼續同步 START --------------------*/
            /*--------------- (1)同步公告版本 START ---------------*/
            try {
                isStopSync = updatePublish(url);
            } catch (Exception exception) {
                System.out.println("exception: " + exception.toString());
                exception.printStackTrace();
                if(mOnBulletinCallBack != null)
                    mOnBulletinCallBack.doPublishFail();
            }
            /*--------------- (1)同步公告版本 END ---------------*/
            /*------------------ 由公告平台取得公告，並確認是否繼續同步 END --------------------*/

            System.out.println("update publish end");
            if(!isStopSync)
                if(mOnBulletinCallBack != null)
                    mOnBulletinCallBack.afterPublishDoAnotherThing();

        } catch (Exception e) {

        } finally {}

        return false;
    }

    @Override
    protected void onPostExecute(Boolean isTaskSuccess) {
        if(mOnBulletinCallBack != null)
            mOnBulletinCallBack.showMaintaining(isStopSync);

        // 有公告則show dialog
        if (backAndShowAlertDuringNotice != null) {
            if (buttonName2 != null && buttonName2.length() > 0) {
//                    new DialogHelper(context).showAlertDialog(
//                            publishTitle, backAndShowAlertDuringNotice,
//                            buttonName1,
//                            createButtonClickListener(buttonLink1),
//                            buttonName2,
//                            createButtonClickListener(buttonLink2), false);
                mDialogHelper.dismissAlerDialog();
                mDialogHelper.showAlertDialog(
                        publishTitle, backAndShowAlertDuringNotice,
                        buttonName1,
                        createButtonClickListener(buttonLink1),
                        buttonName2,
                        createButtonClickListener(buttonLink2), isStopSync);
            } else {
//                    new DialogHelper(context).showAlertDialog(
//                            publishTitle, backAndShowAlertDuringNotice,
//                            buttonName1,
//                            createButtonClickListener(buttonLink1), null, null,
//                            false);
                mDialogHelper.dismissAlerDialog();
                mDialogHelper.showAlertDialog(
                        publishTitle, backAndShowAlertDuringNotice,
                        buttonName1,
                        createButtonClickListener(buttonLink1), null, null,
                        isStopSync);
            }

            publishTitle = null;
            backAndShowAlertDuringNotice = null;
            buttonName1 = null;
            buttonName2 = null;
            buttonLink1 = null;
            buttonLink2 = null;
        } else {
            if(mOnBulletinCallBack != null)
                mOnBulletinCallBack.finishActivity();
        }
    }

    private boolean updatePublish(String url) throws Exception {
        String jsonString = doGet(url);
        System.out.println("------publish: " + url + "\n");
        System.out.println("------jsonString: " + jsonString);

        JsonParser jsonParser = new JsonParser();
        JsonObject jo_notice = (JsonObject) jsonParser.parse(jsonString);

        int status = Integer.valueOf(jo_notice.get("status").getAsString());

        String buttonLink1 = "";
        try{
            buttonLink1 = jo_notice.get("button_link1").getAsString();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(mOnBulletinCallBack != null)
            mOnBulletinCallBack.customBullinParser(jo_notice);

        // status 0:無公告 1:一般公告 2:此版本不維護公告
        // show_Type 0:每天啟動一次 1:每次啟動 2:只發佈一次
        if (status == 0) {
            // 無公告情況下，將原本DB的公告資料　update成不可能發生的時間
            try {
                PublishDBHelper.getInstance(context).update("update notice set start = \"201001010000\" , end = \"201001010000\"; ");
//                DBHelper dbHelper = new DBHelper(context, dbName);
//                dbHelper.open();
//                dbHelper.update("update notice set start = \"201001010000\" , end = \"201001010000\"; ");
//                dbHelper.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                // 最壞情況Exception也不要影響運作ＸＤ
            }
            return false;
        } else if (status == 2) {
            buttonLink1 = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME + "&feature=search_result";
        }


        Cursor cursor = PublishDBHelper.getInstance(context).select("select show_type , next_show_time , version from notice ");
//        DBHelper dbHelper = new DBHelper(context, dbName);
//        dbHelper.open();
//        Cursor cursor = dbHelper
//                .select("select show_type , next_show_time , version from notice ");
        cursor.moveToNext();
        String showType = cursor.getString(0);
        long next_show_time = cursor.getLong(1);
        String bulletinVersion = cursor.getString(2);
        cursor.close();

        // 更新公告
        String content = jo_notice.get("content").getAsString();
        String jShowType = jo_notice.get("show_type").getAsString();
        String jBulletinVersion = jo_notice.get("bulletin_version")
                .getAsString();
        if (content != null && content.length() > 0) {
//            content = "</font color='#FFFFFF'>" + content + "</font>";
        }
        long currentLong = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmm")
                .format(new Date()));
        long notice_start = Long.parseLong(jo_notice.get("start_time")
                .getAsString());
        long notice_end = Long.parseLong(jo_notice.get("end_time")
                .getAsString());

        String f0Str = "";
        if (currentLong >= notice_start && currentLong <= notice_end) {
            if (jo_notice.get("f0") != null
                    && jo_notice.get("f0").getAsString().length() > 0) {
                if (jo_notice.get("f0").getAsString().equals("1")) {
                    f0Str += " , f0 = '1' ";
                } else {
                    f0Str += " , f0 = '0' ";
                    isStopSync = true;
                }
            }
        }

        /**
         *
         */
        String updateString = "";
        if (!isStopSync) {
            updateString = "update notice set version='"
                    + jBulletinVersion + "', message=\"" + content
                    + "\", start='" + jo_notice.get("start_time").getAsString()
                    + "', end='" + jo_notice.get("end_time").getAsString()
                    + "', button_name1='"
                    + jo_notice.get("button_name1").getAsString()
                    + "' , button_link1 ='" + buttonLink1
                    + "' , button_name2='"
                    + jo_notice.get("button_name2").getAsString()
                    + "' , button_link2 ='"
                    + jo_notice.get("button_link2").getAsString()
                    + "' , title ='" + jo_notice.get("title").getAsString()
                    + "' , show_type='" + jShowType + "'";
        } else {
            updateString = "update notice set version='"
                    + jBulletinVersion + "', message=\"" + content
                    + "\", start='" + jo_notice.get("start_time").getAsString()
                    + "', end='" + jo_notice.get("end_time").getAsString()
                    + "', button_name1='"
                    + jo_notice.get("button_name1").getAsString()
                    + "' , button_link1 ='" + buttonLink1
                    + "' , button_name2='"
                    + jo_notice.get("button_name2").getAsString()
                    + "' , button_link2 ='"
                    + jo_notice.get("button_link2").getAsString()
                    + "' , title ='" + jo_notice.get("title").getAsString()
                    + "' , show_type='" + jShowType + "'";
        }
        if (showType.equals("0") && !jShowType.equals("0")) {
            // 如果原本的showType = 0每天啟動一次 改為其他狀態(1:每次啟動 2:只發佈一次)
            // 把原本的show_date改為20110101
            updateString += " , next_show_time = 201101010000 ";
        }

        updateString += f0Str;

        PublishDBHelper.getInstance(context).update(updateString);

        //客製化
        try{
            if(mOnBulletinCallBack != null)
                PublishDBHelper.getInstance(context).update(mOnBulletinCallBack.customUpdateToDB(jo_notice, isStopSync));
        }catch (Exception e){}

//        dbHelper.update(updateString);
//        dbHelper.close();

        if (currentLong >= notice_start && currentLong <= notice_end) {
            // 表示為公告期間
            // System.out.println("showType: "+jShowType);
            switch (Integer.parseInt(jShowType)) {
                case 0: // 0:每天啟動一次
                    if (currentLong > next_show_time
                            && jBulletinVersion != bulletinVersion) {
                        isShowPublish = true;
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, 1);
                        String updateNextShowTime = new SimpleDateFormat("yyyyMMdd")
                                .format(c.getTime()) + "0000";

                        PublishDBHelper.getInstance(context).update(" update notice set next_show_time = "
                                + updateNextShowTime);

//                        dbHelper.open();
//                        dbHelper.update(" update notice set next_show_time = "
//                                + updateNextShowTime);
//                        dbHelper.close();
                    }
                    break;
                case 1: // 1:每次啟動
                    isShowPublish = true;
                    break;
                case 2: // 2:只發佈一次
                    if (!jBulletinVersion.equals(bulletinVersion)) {
                        isShowPublish = true;
                    }
                    break;
            }
        }

        if (isShowPublish) {
            this.backAndShowAlertDuringNotice = content;
            this.publishTitle = jo_notice.get("title").getAsString();
            this.buttonName1 = jo_notice.get("button_name1").getAsString();
            this.buttonLink1 = buttonLink1;
            this.buttonName2 = jo_notice.get("button_name2").getAsString();
            this.buttonLink2 = jo_notice.get("button_link2").getAsString();

//            return true;// 直接返回,下面的就不用進行了
        } else {
            this.backAndShowAlertDuringNotice = null;
            this.publishTitle = null;
            this.buttonName1 = null;
            this.buttonLink1 = null;
            this.buttonName2 = null;
            this.buttonLink2 = null;
        }

        PublishDBHelper.getInstance(context).close();
        return isStopSync;
    }

    public static String doGet(String url) throws Exception {
        String HTTP_CHARSET = "utf-8";
        String responsetStr = "";
        HttpURLConnection huc = null;
        BufferedReader br = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
//            huc.setRequestProperty("User-Agent", BaseProxy.userAgent);
            br = new BufferedReader(new InputStreamReader(huc.getInputStream(),
                    HTTP_CHARSET));
            String NL = System.getProperty("line.separator");
            StringBuffer sb = new StringBuffer();

            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp + NL);
            }

            responsetStr = sb.toString();
            if (DEBUG) {
                System.out.println(responsetStr);
            }
        } finally {
            if (br != null)
                br.close();
            if (huc != null)
                huc.disconnect();
        }
        return responsetStr;
    }

    private DialogInterface.OnClickListener createButtonClickListener(
            final String link) {
        if (link != null && link.length() > 0) {
            return new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (!isStopSync) {
                        if(mOnBulletinCallBack != null)
                            mOnBulletinCallBack.finishActivity();
                    }
                    if (link.indexOf("play.google.com") != -1) {
                        String url = "";
                        if (Build.VERSION.SDK_INT >= 8) {
                            url = "market://details?id=" + PACKAGE_NAME;
                        } else {
                            url = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME + "&feature=search_result";
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(intent);
                    } else {
                        // 另開網頁
                        if(mOnBulletinCallBack != null)
                            mOnBulletinCallBack.openWebView(link);
                        else{
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url));
                            startActivity(intent);
                        }
                    }

                    if(mOnBulletinCallBack != null)
                        mOnBulletinCallBack.onDialogButtonClick();
                }
            };
        } else {
            return new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (!isStopSync) {
                        if(mOnBulletinCallBack != null)
                            mOnBulletinCallBack.finishActivity();
                    }
                }
            };
        }
    }

    private void startActivity(Intent intent){
        context.startActivity(intent);
    }

    public interface OnBulletinCallBack{

        void showMaintaining(boolean isMaintaining);

        void customBullinParser(JsonObject jo_notice);

        void afterPublishDoAnotherThing() throws Exception;

        void openWebView(String link);

        void onDialogButtonClick();

        void finishActivity();

        String customUpdateToDB(JsonObject jo_notice, boolean isStopSync);

        void doPublishFail();
    }
}
