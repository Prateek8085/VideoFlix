package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    adapterclass adapter;
    private ArrayList<dataclass> videos;
    private ArrayList<Item> items;
    String url = "https://fatema.takatakind.com/app_api/index.php?p=showAllVideos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        videos = new ArrayList<>();
        items = new ArrayList<>();
        getjsondata();
        adapter = new adapterclass(this, videos, new adapterclass.OnVideoPreparedListener() {
            @Override
            public void onVideoPrepared(Item item) {
                items.add(item);
            }
        });
        binding.viewpager2.setAdapter(adapter);
        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int previousIndex = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).player1.isPlaying()) {
                        previousIndex = i;
                        break;
                    }
                }
                if (previousIndex != -1) {
                    ExoPlayer player = items.get(previousIndex).player1;
                    player.pause();
                    player.setPlayWhenReady(false);
                }
                int newIndex = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).position == position) {
                        newIndex = i;
                        break;
                    }
                }
                if (newIndex != -1) {
                    ExoPlayer player = items.get(newIndex).player1;
                    player.setPlayWhenReady(true);
                    player.play();
                }

            }

        });

    }

    @Override
    public void onPause() {
        super.onPause();

        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).position == binding.viewpager2.getCurrentItem()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            ExoPlayer player = items.get(index).player1;
            player.pause();
            player.setPlayWhenReady(false);
        }


    }
    @Override
    public void onResume() {
        super.onResume();

        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).position == binding.viewpager2.getCurrentItem()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            ExoPlayer player = items.get(index).player1;
            player.setPlayWhenReady(true);
            player.play();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!items.isEmpty()) {
            for (Item item: items) {
                ExoPlayer player = item.player1;
                player.stop();
                player.clearMediaItems();
            }
        }
    }
    public void getjsondata() {
        StringRequest request = new StringRequest(Request.Method.GET, url, this::onResponse, error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("msg");
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(10);
                String video = jsonObject1.getString("video");
                Log.d(TAG, "onResponse: "+ video);
                dataclass dc = new dataclass(video);
                videos.add(dc);

                videos.add(
                        new dataclass(jsonObject1.getString("video"))
                );


            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
