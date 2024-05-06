package com.example.myapplication;

import androidx.media3.exoplayer.ExoPlayer;

public class Item {
    public ExoPlayer player1;
    public int position;


    public Item(ExoPlayer player, int absoluteAdapterPosition) {
        this.player1 = player;
        this.position = absoluteAdapterPosition;
    }
}

