package com.example.movietrailer.activity;

import android.os.Bundle;

import com.example.movietrailer.R;
import com.example.movietrailer.adapter.MainAdapter;
import com.example.movietrailer.model.MovieModel;
import com.example.movietrailer.retrofit.Constant;
import com.example.movietrailer.retrofit.MovieService;
import com.example.movietrailer.retrofit.RetrofitInstance;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietrailer.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MainAdapter adapter;
    private List<MovieModel.Results> movieList = new ArrayList<>();
    private ProgressBar progressBar;
    private String movieCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setupView();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(movieCategory == null){
            movieCategory = Constant.POPULAR;
        }
        getMovie();
    }

    private void setupRecyclerView() {
        adapter = new MainAdapter(movieList, this, new MainAdapter.AdapterListener() {
            @Override
            public void onClick() {
                showMessage(Constant.MOVIE_TITLE);
            }
        });
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupView() {
        recyclerView = findViewById(R.id.list_movie);
        progressBar = findViewById(R.id.progress_loading);
    }

    private void getMovie() {
        showLoading(true);
        Call<MovieModel> call = null;

        switch (movieCategory) {
            case Constant.POPULAR:
                call = service.getPopularMovie(
                        Constant.API_KEY,
                        Constant.LANGUAGE,
                        "1"
                );
                break;
            case Constant.NOW_PLAYING:
                call = service.getNowPlaying(Constant.API_KEY,
                        Constant.LANGUAGE,
                        "1"
                );
                break;
        }
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                showLoading(false);

                if (response.isSuccessful()){
                    MovieModel movieModel = response.body();
                    showMovie(movieModel);
                }else{
                    Log.d(TAG,response.toString());
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                showLoading(false);
                Log.d(TAG,t.toString());
            }
        });
    }

    private void showLoading(Boolean loading) {
        if(loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showMovie(MovieModel movie) {
        movieList = movie.getResults();
        adapter.setData(movieList);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            getSupportActionBar().setTitle("Popular");
            movieCategory = Constant.POPULAR;
            getMovie();
            return true;
        } else if (id == R.id.action_now_playing) {
            getSupportActionBar().setTitle("Now Playing");
            movieCategory = Constant.NOW_PLAYING;
            getMovie();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}