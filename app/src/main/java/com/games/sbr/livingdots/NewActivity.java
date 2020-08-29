package com.games.sbr.livingdots;

import android.app.AlertDialog;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NewActivity extends BaseGameActivity {
    //andengine requires
    public static int blue,red;
    TextView t1,t2;
    private Camera mCamera;
    public Scene scene;
    private BitmapTextureAtlas mTexGrid,mTexCoin;
    private TextureRegion gridReg,coinReg;
    //My Game
    public static int dwidth, dheight;
    boolean isRunning=false;
    int player,howPlayer;
    public static int checkingCount;
    Grid grid[][]=new Grid[5][6];
    ArrayList<Dot> dots;
    ArrayList<Grid> availGrid1,availGrid2,availGrid3;
    AlertDialog alertDialog;
    PlayerMode playerMode;

    @Override
    public Engine onLoadEngine() {
        dwidth = this.getWindowManager().getDefaultDisplay().getWidth();
        dheight = this.getWindowManager().getDefaultDisplay().getHeight();
        this.mCamera=new Camera(0,0,dwidth,dheight);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return new Engine(new EngineOptions(true, EngineOptions.ScreenOrientation.PORTRAIT,new RatioResolutionPolicy(dwidth,dheight),this.mCamera));
    }

    @Override
    public void onLoadResources() {
        availGrid1=new ArrayList<>();
        availGrid2=new ArrayList<>();
        availGrid3=new ArrayList<>();
        dots=new ArrayList<>();
        playerMode=new PlayerMode(this);
        howPlayer=playerMode.getPlayers();
        checkingCount=0;
        blue=red=0;
        mTexGrid=new BitmapTextureAtlas(64,128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTexCoin=new BitmapTextureAtlas(32,32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        gridReg=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexGrid,this,"grid2.png",0,0);
        coinReg=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexCoin,this,"dot2.png",0,0);
        this.mEngine.getTextureManager().loadTexture(this.mTexGrid);
        this.mEngine.getTextureManager().loadTexture(this.mTexCoin);

        player=0;
        for(int j=0;j<6;j++) {
            for (int i = 0; i < 5; i++) {
                grid[i][j]=new Grid((int)(dwidth/8.0)+(i*(int)(dwidth/6.4)),(int)(dheight/8.0)+(j*(int)(dheight/8.0)),this);
                if((i>0&&i<4)||(j>0&&j<5)) {
                    grid[i][j].CAPACITY++;
                }
                if(i>0&&j>0&&i<4&&j<5) {
                    grid[i][j].CAPACITY++;
                }
            }
        }
    }

    @Override
    public Scene onLoadScene() {
        scene=new Scene();
        this.mEngine.registerUpdateHandler(new FPSLogger());
        scene.setBackground(new ColorBackground(0,0,0));
        for (Star tem: MenuActivity.star) {
            final Rectangle rectangle=new Rectangle((float) tem.x,(float)tem.y,2f,2f);
            rectangle.setColor(255,255,255);
            scene.attachChild(rectangle);
        }
        for(int j=0;j<6;j++) {
            for (int i = 0; i < 5; i++) {
                Sprite sprite=new Sprite(grid[i][j].X,grid[i][j].Y,gridReg);
                sprite.setScale((float)(1.25),(float) (1.25));
                sprite.setAlpha(0.5f);
                scene.attachChild(sprite);
            }
        }
        for(int j=1;j<5;j++) {
            for (int i = 1; i < 4; i++) {
                grid[i][j].t1=grid[i][j-1];
                grid[i][j].t2=grid[i+1][j];
                grid[i][j].t3=grid[i-1][j];
                grid[i][j].t4=grid[i][j+1];
            }
        }
        for (int i = 1; i < 4; i++) {
            grid[i][0].t1=grid[i-1][0];
            grid[i][0].t2=grid[i+1][0];
            grid[i][0].t3=grid[i][1];
        }
        for (int i = 1; i < 4; i++) {
            grid[i][5].t1=grid[i][4];
            grid[i][5].t2=grid[i+1][5];
            grid[i][5].t3=grid[i-1][5];
        }
        for(int j=1;j<5;j++) {
            grid[0][j].t1=grid[0][j-1];
            grid[0][j].t2=grid[1][j];
            grid[0][j].t3=grid[0][j+1];
        }
        for(int j=1;j<5;j++) {
            grid[4][j].t1=grid[3][j];
            grid[4][j].t2=grid[4][j-1];
            grid[4][j].t3=grid[4][j+1];
        }
        grid[0][0].t1=grid[0][1];
        grid[0][0].t2=grid[1][0];
        grid[4][0].t1=grid[3][0];
        grid[4][0].t2=grid[4][1];
        grid[0][5].t1=grid[0][4];
        grid[0][5].t2=grid[1][5];
        grid[4][5].t1=grid[3][5];
        grid[4][5].t2=grid[4][4];
        //System.out.println("W:"+dwidth+"H:"+dheight);
        //System.out.println("w:"+gridReg.getWidth()+"h:"+gridReg.getHeight());
        return scene;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View view= LayoutInflater.from(this).inflate(R.layout.my_layout,viewGroup,false);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
        //super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if(howPlayer==1)
                singlePlayer(event);
            else if(howPlayer==2)
                dualPlayer(event);
        }
        return true;
    }

    public boolean checking() {
        if(checkForOver()) {
            //System.out.println(checkForOver());
            AlertDialog.Builder builder=new AlertDialog.Builder(NewActivity.this);
            ViewGroup viewGroup=findViewById(android.R.id.content);
            View view= LayoutInflater.from(NewActivity.this).inflate(R.layout.game_over,viewGroup,false);
            t1=(TextView)view.findViewById(R.id.tBlue);
            t2=(TextView)view.findViewById(R.id.tRed);
            if(t1!=null&&t2!=null) {
                t1.append(Integer.toString(blue));
                t2.append(Integer.toString(red));
                if(blue>red)
                    playerMode.setColour("blue");
                else
                    playerMode.setColour("red");
                if((blue+red)>=playerMode.getHighscore())
                    playerMode.setHighscore(blue+red);
            }
            else {
                System.out.println("errrrr");
            }
            builder.setView(view);
            alertDialog=builder.create();
            alertDialog.show();
            return true;
        }
        return false;
    }
    public void singlePlayer(MotionEvent motionEvent) {
        int tX, tY;
        if (!isRunning) {
            if (motionEvent.getX() >= grid[0][0].X && motionEvent.getX() <= (grid[4][5].X + grid[4][5].W) && motionEvent.getY() >= grid[0][0].Y && motionEvent.getY() <= (grid[4][5].Y + grid[4][5].H)) {
                tX = (int) (motionEvent.getX() - (int) (dwidth / 9.6)) / grid[0][0].W;
                tY = (int) (motionEvent.getY() - (int) (dheight / 8.0)) / grid[0][0].H;
                if (grid[tX][tY].AvailBlue) {
                    grid[tX][tY].calPosition(grid[tX][tY].Dots + 1);
                    Dot temp = new Dot(grid[tX][tY].InX, grid[tX][tY].InY, coinReg);
                    temp.setPosition(grid[tX][tY].InX, grid[tX][tY].InY);
                    temp.setColor(0, 0, 1);
                    temp.setScale(0.75f);
                    dots.add(temp);
                    grid[tX][tY].addDots(Color.rgb(0, 0, 255), temp);
                    scene.attachChild(grid[tX][tY].dn);
                    grid[tX][tY].dn = null;
                    checkingCount++;
                    player = 1;
                    while (checkingCount > 0) {
                        run();
                        checkingCount--;
                        try {
                            Thread.sleep(250);
                        } catch (Exception er) {

                        }
                    }
                    if(checking())
                        return;
                    Random r = new Random();
                    for (int j = 0; j < 6; j++) {
                        for (int i = 0; i < 5; i++) {
                            if (grid[i][j].AvailRed && grid[i][j].CAPACITY == 1) {
                                availGrid1.add(grid[i][j]);
                            } else if (grid[i][j].AvailRed && grid[i][j].CAPACITY == 2) {
                                availGrid2.add(grid[i][j]);
                            } else if (grid[i][j].AvailRed && grid[i][j].CAPACITY == 3) {
                                availGrid3.add(grid[i][j]);
                            }
                        }
                    }
                    if (!availGrid1.isEmpty()) {
                        Grid gTemp = availGrid1.get(0 + r.nextInt(availGrid1.size()));
                        gTemp.calPosition(gTemp.Dots + 1);
                        Dot temp1 = new Dot(gTemp.InX, gTemp.InY, coinReg);
                        temp1.setColor(1, 0, 0);
                        temp1.setScale(0.75f);
                        dots.add(temp1);
                        gTemp.addDots(Color.rgb(255, 0, 0), temp1);
                        scene.attachChild(gTemp.dn);
                        gTemp.dn = null;
                        checkingCount++;
                        player = 0;
                        availGrid1.clear();
                    } else if (!availGrid2.isEmpty()) {
                        Grid gTemp = availGrid2.get(0 + r.nextInt(availGrid2.size()));
                        gTemp.calPosition(gTemp.Dots + 1);
                        Dot temp1 = new Dot(gTemp.InX, gTemp.InY, coinReg);
                        temp1.setColor(1, 0, 0);
                        temp1.setScale(0.75f);
                        dots.add(temp1);
                        gTemp.addDots(Color.rgb(255, 0, 0), temp1);
                        scene.attachChild(gTemp.dn);
                        gTemp.dn = null;
                        checkingCount++;
                        player = 0;
                        availGrid2.clear();
                    } else if (!availGrid3.isEmpty()) {
                        Grid gTemp = availGrid3.get(0 + r.nextInt(availGrid3.size()));
                        gTemp.calPosition(gTemp.Dots + 1);
                        Dot temp1 = new Dot(gTemp.InX, gTemp.InY, coinReg);
                        temp1.setColor(1, 0, 0);
                        temp1.setScale(0.75f);
                        dots.add(temp1);
                        gTemp.addDots(Color.rgb(255, 0, 0), temp1);
                        scene.attachChild(gTemp.dn);
                        gTemp.dn = null;
                        checkingCount++;
                        player = 0;
                        availGrid3.clear();
                    }
                    while (checkingCount > 0) {
                        run();
                        checkingCount--;
                        try {
                            Thread.sleep(250);
                        } catch (Exception er) {

                        }
                    }
                    if(checking())
                        return;
                }
                else if (!grid[tX][tY].AvailBlue) {
                    Toast.makeText(this, "Red color already assigned", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean checkForOver() {
        int tB,tR;
        tB=tR=0;
        for(Dot dto:dots) {
            if(dto.colour==0) {
                tB++;
            }
            else if(dto.colour==1) {
                tR++;
            }
        }
        blue=tB;
        red=tR;
        if((blue==0||blue==1)&&red==0) {
            return false;
        }
        else if(blue==0||red==0) {
            System.out.println("b"+blue+"r"+red);
            return true;
        }
        return false;
    }
    public void dualPlayer(MotionEvent motionEvent) {
        int tX,tY;
        if(!isRunning) {
            if (motionEvent.getX() >= grid[0][0].X && motionEvent.getX() <= (grid[4][5].X+grid[4][5].W) && motionEvent.getY() >= grid[0][0].Y && motionEvent.getY() <= (grid[4][5].Y+grid[4][5].H)) {
                tX = (int) (motionEvent.getX() - (int) (dwidth / 9.6)) / grid[0][0].W;
                tY = (int) (motionEvent.getY() - (int) (dheight / 8.0)) / grid[0][0].H;
                if(player==0&&grid[tX][tY].AvailBlue) {
                    grid[tX][tY].calPosition(grid[tX][tY].Dots+1);
                    Dot temp=new Dot(grid[tX][tY].InX,grid[tX][tY].InY,coinReg);
                    temp.setPosition(grid[tX][tY].InX,grid[tX][tY].InY);
                    temp.setColor(0,0,1);
                    temp.setScale(0.75f);
                    dots.add(temp);
                    grid[tX][tY].addDots(Color.rgb(0,0,255),temp);
                    scene.attachChild(grid[tX][tY].dn);
                    grid[tX][tY].dn=null;
                    checkingCount++;
                    player=1;
                }
                else if(player==1&&grid[tX][tY].AvailRed){
                    grid[tX][tY].calPosition(grid[tX][tY].Dots+1);
                    Dot temp=new Dot(grid[tX][tY].InX,grid[tX][tY].InY,coinReg);
                    temp.setColor(1,0,0);
                    temp.setScale(0.75f);
                    dots.add(temp);
                    grid[tX][tY].addDots(Color.rgb(255,0,0),temp);
                    scene.attachChild(grid[tX][tY].dn);
                    grid[tX][tY].dn=null;
                    checkingCount++;
                    player=0;
                }
                else if(!grid[tX][tY].AvailBlue) {
                    Toast.makeText(this,"Red color already assigned",Toast.LENGTH_SHORT).show();
                }
                else if(!grid[tX][tY].AvailRed) {
                    Toast.makeText(this,"Blue color already assigned",Toast.LENGTH_SHORT).show();
                }
            }
        }
        while(checkingCount>0) {
            run();
            checkingCount--;
            try {
                Thread.sleep(250);
            }
            catch (Exception er) {

            }
        }
        if(checking())
            return;
    }
    public void run() {
        //boolean UP,DOWN,RIGHT,LEFT;
        isRunning=true;
        for(int j=0;j<6;j++) {
            for (int i = 0; i < 5; i++) {
                if(grid[i][j].isFull()) {
                    //System.out.println("Fill");
                    try {
                        grid[i][j].move(1);
                    }
                    catch (Exception ex) {
                        System.out.println(ex+"d1");
                    }
                    try {
                        grid[i][j].move(2);
                    }
                    catch (Exception ex) {
                        System.out.println(ex+"d2");
                    }
                    try {
                        grid[i][j].move(3);
                    }
                    catch (Exception ex) {
                        System.out.println(ex+"d3");
                    }
                    try {
                        grid[i][j].move(4);
                    }
                    catch (Exception ex) {
                        System.out.println(ex+"d4");
                    }
                    checkingCount=+4;
                    grid[i][j].clearDots();
                }
            }
        }
        isRunning=false;
    }
    @Override
    public void onLoadComplete() {

    }

    public void exitTheGame(View view) {
        super.onBackPressed();
    }

    public void kuchhNahi(View view) {
        alertDialog.cancel();
    }
}
