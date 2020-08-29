package com.games.sbr.livingdots;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FlashActivity extends Activity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_flash);
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                Intent intent=new Intent(FlashActivity.this,MenuActivity.class);
                FlashActivity.this.startActivity(intent);
                FlashActivity.this.finish();
            }
        },3000);
    }
}

