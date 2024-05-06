package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.VideolayoutBinding;

import java.util.ArrayList;

public  class adapterclass extends RecyclerView.Adapter<adapterclass.ViewHolder> {
    private final Context context;
    private ArrayList<dataclass> videos;
    private final OnVideoPreparedListener videoPreparedListener;

    public adapterclass(Context context, ArrayList<dataclass> videos, OnVideoPreparedListener videoPreparedListener) {
        this.context = context;
        this.videos = videos;
        this.videoPreparedListener = videoPreparedListener;

    }



    @NonNull
    @Override
    public adapterclass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideolayoutBinding view = VideolayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(view, context, videoPreparedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterclass.ViewHolder holder, int position) {
        dataclass model = videos.get(position);
        holder.setVideoPath(model.getVideo());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final VideolayoutBinding binding;
        private final   Context context;
        private final OnVideoPreparedListener videoPreparedListener;
        public ExoPlayer exoPlayer;
        private MediaSource mediaSource;

        public ViewHolder(VideolayoutBinding binding, Context context, OnVideoPreparedListener videoPreparedListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            this.videoPreparedListener = videoPreparedListener;
        }
        @OptIn(markerClass = UnstableApi.class) public void setVideoPath(String video) {
            exoPlayer = new ExoPlayer.Builder(context).build();
            exoPlayer.addListener(new Player.Listener() {

                @Override
                public void onPlayerError(PlaybackException error) {
                    Player.Listener.super.onPlayerError(error);
                    Toast.makeText(context, "Can't play the video", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPlaybackStateChanged(@Player.State int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        binding.pgbar.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_READY) {
                        binding.pgbar.setVisibility(View.GONE);
                    }
                }
            });
            binding.playerview.setPlayer(exoPlayer);
            exoPlayer.seekTo(0);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
            //CacheDataSource.Factory cacheDataSource = CacheUtil.getCacheDataSourceFactory();
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(video));
            exoPlayer.setMediaSource(mediaSource);
            exoPlayer.prepare();


            if (getAbsoluteAdapterPosition() == 0) {
                exoPlayer.setPlayWhenReady(true);
                exoPlayer.play();
            }
            videoPreparedListener.onVideoPrepared(new Item(exoPlayer, getAbsoluteAdapterPosition()));

        }


    }
    public interface OnVideoPreparedListener {
        void onVideoPrepared(Item item);
    }

}