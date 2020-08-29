package com.games.sbr.livingdots;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

public class PlayerMode {
    SharedPreferences sharedPreferences;
    Context context;
    int players;
    int highscore;

    public String getColour() {
        colour=sharedPreferences.getString("HighColor","");
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
        sharedPreferences.edit().putString("HighColor",this.colour).commit();
    }

    String colour;
    public int getHighscore() {
        highscore=sharedPreferences.getInt("High",0);
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
        sharedPreferences.edit().putInt("High",this.highscore).commit();
    }


    public int getPlayers() {
        players=sharedPreferences.getInt("Player",1);
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
        sharedPreferences.edit().putInt("Player",this.players).commit();
    }
    public PlayerMode(Context context) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("player_mode",Context.MODE_PRIVATE);
    }

}
