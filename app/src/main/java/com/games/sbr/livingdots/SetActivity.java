package com.games.sbr.livingdots;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;

public class SetActivity extends Activity {

    int dwidth,dheight,rx,ry;
    Bitmap bitmap,bt;
    Canvas canvas;
    Paint paint;
    ImageView imageView;
    PlayerMode playerMode;
    RadioButton r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_set);
        imageView=(ImageView)findViewById(R.id.imageView7);
        r1=(RadioButton) findViewById(R.id.radioSingle);
        r2=(RadioButton) findViewById(R.id.radioDouble);
        dwidth=this.getWindowManager().getDefaultDisplay().getWidth();
        dheight=this.getWindowManager().getDefaultDisplay().getHeight();
        bitmap=Bitmap.createBitmap(dwidth,dheight,Bitmap.Config.RGB_565);
        imageView.setImageBitmap(bitmap);
        canvas=new Canvas(bitmap);
        paint=new Paint();
        fun();
        playerMode=new PlayerMode(this);
        if(playerMode.getPlayers()==1)
            r1.setChecked(true);
        else
            r2.setChecked(true);
    }

    private void fun() {
        paint.setColor(Color.rgb(0,0,0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0f,0f,(float)dwidth,(float)dheight,paint);
        paint.setColor(Color.rgb(255,255,255));
        for (Star tem: MenuActivity.star) {
            canvas.drawCircle((float)tem.x,(float)tem.y,1.5f,paint);
        }
    }

    public void play1(View view) {
        playerMode.setPlayers(1);
    }

    public void play2(View view) {
        playerMode.setPlayers(2);
    }
}
