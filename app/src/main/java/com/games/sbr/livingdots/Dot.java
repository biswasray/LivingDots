package com.games.sbr.livingdots;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Dot extends Sprite {
    public int index,colour;
    public boolean active;
    public Dot(float pX, float pY, TextureRegion pTextureRegion) {
        super(pX, pY, pTextureRegion);
        index=Grid.GI++;
        active=false;
    }
    public void move(float a, float b, float t, final Grid target, final int gC) {
        if(a!=this.getX()||b!=this.getY()) {
            final float tX=a;
            final float tY=b;
            final float sX=((a-this.getX())/t)*50;
            final float sY=((b-this.getY())/t)*50;
            //System.out.println("in move");
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    active=true;
                    float pvX,pvY;
                    //System.out.println("MX"+Dot.this.getX()+"MY"+Dot.this.getY());
                    while (tX!=Dot.this.getX()||tY!=Dot.this.getY()) {
                        //System.out.println("move");
                        //System.out.println("TX"+tX+"TY"+tY);
                        try{
                            Thread.sleep(50);
                        }
                        catch (Exception er) {
                            System.out.println(er+"te");
                        }
                        pvX=Dot.this.getX();
                        pvY=Dot.this.getY();
                        Dot.this.setPosition(Dot.this.getX()+sX,Dot.this.getY()+sY);
                        if(((tX-Dot.this.getX())*(tX-pvX)<=0)&&((tY-Dot.this.getY())*(tY-pvY)<=0))
                            break;
                    }
                    Dot.this.setPosition(tX,tY);
                    active=false;
                    target.addDots(gC,Dot.this);
                    target.Demand--;
                }
            });
            thread.start();
        }
    }

    @Override
    public void setColor(float pRed, float pGreen, float pBlue) {
        super.setColor(pRed, pGreen, pBlue);
        if(pBlue==1)
            colour=0;
        else if(pRed==1)
            colour=1;
    }

    public void move2(float a, float b, float t) {
        if(a!=this.getX()||b!=this.getY()) {
            final float tX=a;
            final float tY=b;
            final float sX=((a-this.getX())/t)*50;
            final float sY=((b-this.getY())/t)*50;
            //System.out.println("in move");
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    active=true;
                    float pvX,pvY;
                    //System.out.println("MX"+Dot.this.getX()+"MY"+Dot.this.getY());
                    while (tX!=Dot.this.getX()||tY!=Dot.this.getY()) {
                        //System.out.println("move");
                        //System.out.println("TX"+tX+"TY"+tY);
                        try{
                            Thread.sleep(50);
                        }
                        catch (Exception er) {
                            System.out.println(er+"te");
                        }
                        pvX=Dot.this.getX();
                        pvY=Dot.this.getY();
                        Dot.this.setPosition(Dot.this.getX()+sX,Dot.this.getY()+sY);
                        if(((tX-Dot.this.getX())*(tX-pvX)<=0)&&((tY-Dot.this.getY())*(tY-pvY)<=0))
                            break;
                    }
                    Dot.this.setPosition(tX,tY);
                    active=false;
                }
            });
            thread.start();
        }
    }
}
