package com.yc.myegl3demo01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnLayout:
                intent = new Intent(this, Activity_Layout.class);
                break;
            case R.id.btnLine:
                intent = new Intent(this, Activity_Line.class);
                break;
            case R.id.btnEtc2:
                intent = new Intent(this, Activity_Etc2.class);
                break;
            case R.id.btnBlend:
                intent = new Intent(this, Activity_Blend.class);
                break;
            default:
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

}
