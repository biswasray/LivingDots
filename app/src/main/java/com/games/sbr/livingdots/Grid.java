package com.games.sbr.livingdots;


import android.graphics.Color;


public class Grid {
    public int CAPACITY,X,Y,InX,InY;
    public int W,H,Dots;
    public boolean AvailBlue,AvailRed;
    public int GridColor,Demand;
    public  Dot d[]=new Dot[8];
    public Dot dn;
    public Grid t1,t2,t3,t4;
    public static int GI;
    public NewActivity newActivity;
    public Grid(int a,int b,NewActivity temp) {
        Demand=0;
        newActivity=temp;
        AvailBlue=AvailRed=true;
        Dots=0;
        CAPACITY=1;
        X=a;
        Y=b;
        W=(int)(NewActivity.dwidth/6.4);
        H=(int)(NewActivity.dheight/8.0);
        InX=X-(W/20);
        InY=Y-(H/20);
    }
    public void clearDots() {
        boolean tempb=true;
        Dots=0;
        //AvailBlue=AvailRed=true;
        InX=X-(W/20);
        InY=Y-(H/40);
        //d1=d2=d3=d4=null;
        for (int i=0;i<=CAPACITY;i++) {
            d[i]=null;
        }
        //d[0]=d[1]=d[2]=d[3]=null;
        for(int j=CAPACITY+1;j<8;j++) {
            if(d[j]!=null) {
                //System.out.println("d1"+j);
                d[j%(CAPACITY+1)]=d[j];
                d[j]=null;
                Dots++;
                tempb=false;
            }
        }
        for(int i=0;i<8;i++) {
            if(d[i]!=null) {
                //System.out.println("d2"+i+"X"+d[i].getX()+"Y"+d[i].getY());
                calPosition(i+1);
                d[i].move2(InX,InY,200);
            }
        }
        if(tempb) {
            AvailBlue = AvailRed = true;

        }
        //System.out.println("clean");
    }
    public void calPosition(int r) {
        if ((r-1)%(CAPACITY+1)==0) {
            InX=X-(W/20);
            InY=Y-(H/40);
        }
        else if((r-1)%(CAPACITY+1)==1) {
            InX=X+(W/2)-(W/10);
            InY=Y-(H/40);
        }
        else if((r-1)%(CAPACITY+1)==2) {
            InX=X-(W/20);
            InY=Y+(H/2)-(H/40);
        }
        else if((r-1)%(CAPACITY+1)==3) {
            InX=X+(W/2)-(W/10);
            InY=Y+(H/2)-(H/40);
        }
    }
    public void addDots(int temp,Dot t) {
        Dots++;
        GridColor=temp;
        for(int i=0;i<8;i++) {
            if(d[i]==null) {
                d[i]=t;
                break;
            }
        }
        if(temp==Color.rgb(0,0,255)) {
            AvailBlue=true;
            AvailRed=false;
            for(int i=0;i<4;i++) {
                if(d[i]!=null) {
                    d[i].setColor(0, 0, 1);
                }
            }
        }
        else {
            AvailBlue=false;
            AvailRed=true;
            for(int i=0;i<4;i++) {
                if(d[i]!=null) {
                    d[i].setColor(1, 0, 0);
                }
            }
        }
        dn=t;
        NewActivity.checkingCount++;
    }
    public boolean isFull() {
        if (CAPACITY<Dots) {
            return true;
        }
        return false;
    }
    public void move(int i) {
        Grid target = null;
        if(i==1) {
            target=t1;
            dn = d[0];
        }
        else if(i==2) {
            target=t2;
            dn = d[1];
        }
        else if(i==3) {
            target=t3;
            dn = d[2];
        }
        else if(i==4) {
            target=t4;
            dn = d[3];
        }
        if(dn!=null&&target!=null&&(!dn.active)) {
            target.Demand++;
            target.calPosition(target.Dots+target.Demand);
            dn.move(target.InX,target.InY,400,target,GridColor);
        }
        //NewActivity.checkingCount++;
        dn=null;
        /*if(i==1)
            dn=d[0]=null;
        else if(i==2)
            dn=d[1]=null;
        else if(i==3)
            dn=d[2]=null;
        else if(i==4)
            dn=d[3]=null;*/
    }
}
