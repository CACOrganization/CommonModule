package com.caccommonmodule.util;

/**
 * Created by ac on 2017/1/24.
 */

import android.widget.Toast;
import android.content.Context;
import android.view.View;


public class ToastUtil {

    public static class Builder {

        private Toast _Toast;

//        public Builder() {
//            this(MainApp.getInstance());
//        }

        public Builder(Context context) {
            this._Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }

        public Builder setText(String msg) {
            _Toast.setText(msg);
            return this;
        }

        public Builder setView(View view) {
            _Toast.setView(view);
            return this;
        }

        public Builder setDuration(int duration) {
            _Toast.setDuration(duration);
            return this;
        }

        public Builder setMargin(float horizontal, float vertical) {
            _Toast.setMargin(horizontal, vertical);
            return this;
        }

        public Builder setGravity(int gravity, int xOffset, int yOffset) {
            _Toast.setGravity(gravity, xOffset, yOffset);
            return this;
        }

        public Toast build() {
            return _Toast;
        }

    }
}
