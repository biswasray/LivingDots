package com.games.sbr.livingdots;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MenuActivity extends Activity {

    boolean doubleTab=false;
    int dwidth,dheight,rx,ry;
    Bitmap bitmap,bt;
    Canvas canvas;
    Paint paint;
    ImageView imageView;
    AlertDialog alertDialog;
    public static ArrayList<Star> star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_menu);
        star=new ArrayList<>();
        imageView=(ImageView)findViewById(R.id.imageView2);
        dwidth=this.getWindowManager().getDefaultDisplay().getWidth();
        dheight=this.getWindowManager().getDefaultDisplay().getHeight();
        bitmap=Bitmap.createBitmap(dwidth,dheight,Bitmap.Config.RGB_565);
        imageView.setImageBitmap(bitmap);
        canvas=new Canvas(bitmap);
        paint=new Paint();
        for(int i=0;i<700;i++) {
            rx=(int)(Math.random()*dwidth);
            ry=(int)(Math.random()*dheight);
            star.add(new Star(rx,ry));
        }
        fun();
    }

    private void fun() {
        paint.setColor(Color.rgb(0,0,0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0f,0f,(float)dwidth,(float)dheight,paint);
        paint.setColor(Color.rgb(255,255,255));
        for (Star tem: star) {
            canvas.drawCircle((float)tem.x,(float)tem.y,1.5f,paint);
        }
    }

    @Override
    public void onBackPressed() {
        if(doubleTab) {
            exitApp();
        }
        else {
            doubleTab=true;
            Toast.makeText(this,"Press Back again for Exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTab=false;
                }
            },1000);
        }

    }

    public void changeToNew(View view) {
        Intent intent=new Intent(this,NewActivity.class);
        startActivity(intent);
    }
    public void exitApp() {
        finish();
        Process.killProcess(Process.myPid());
    }

    public void changeToSet(View view) {
        Intent intent=new Intent(this,SetActivity.class);
        startActivity(intent);
    }

    public void showHighScore(View view1) {
        AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View view= LayoutInflater.from(MenuActivity.this).inflate(R.layout.high_score,viewGroup,false);
        TextView t1=(TextView)view.findViewById(R.id.tHigh);
        PlayerMode pl=new PlayerMode(this);
        if(pl.getColour()=="blue") {
            t1.setText("Blue:"+Integer.toString(pl.getHighscore()));
            t1.setTextColor(getResources().getColor(R.color.Blue));
        }
        else if(pl.getColour()=="red") {
            t1.setText("Red:"+Integer.toString(pl.getHighscore()));
            t1.setTextColor(getResources().getColor(R.color.Red));
        }
        else {
            t1.setText(Integer.toString(pl.getHighscore()));
            //t1.setTextColor(Color.rgb(1,1,1));
        }
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void closeHigh(View view) {
        alertDialog.cancel();
    }
}
