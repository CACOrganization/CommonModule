package com.caccommonmodule.contentprovider;

/**
 * Created by ac on 2017/2/3.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class NotePad {
    //ContentProvider的uri
    public static final String AUTHORITY = "com.xh.google.provider.NotePad";

    private NotePad(){}

    //定义基本字段   实现BaseColumns 这个接口里边已经定义了"_id"字段所以这里不用定义了
    public static final class Notes implements BaseColumns {
        private Notes(){}

        //Uri.parse 方法根据指定字符串创建一个 Uri 对象
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

        //新的MIME类型-多个
        public static final String  CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

        //新的MIME类型-单个
        public static final String  CONTENT_ITME_TYPE  = "vnd.android.cursor.item/vnd.google.note";

        public static final String  DEFAULT_SORT_ORDER = "modified DESC";

        //字段
        public static final String  TITLE              = "title";
        public static final String  NOTE               = "note";
        public static final String  CREATEDDATE        = "created";
        public static final String  MODIFIEDDATE       = "modified";

        //登入資訊
        public static final String  ACCOUNT       = "account";
        public static final String  PASSWORD       = "password";
        public static final String  LOGINDATA       = "logindata";
    }
}