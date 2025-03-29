package com.example.moviedb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchViewController extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private ListViewController movieAdapter;
    private List<Movie> movieList;
    private Button viewFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);
        viewFav = findViewById(R.id.viewFav);


        movieList = new ArrayList<>();
        movieAdapter = new ListViewController(movieList, new ListViewController.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Intent intent = new Intent(SearchViewController.this, MovieDetailsViewController.class);
                intent.putExtra("MOVIE", movie);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);
        viewFav.setOnClickListener(v -> viewFavorites());


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchMovies(query);
                }
            }
        });
    }

    private void searchMovies(String query) {
        ApiUtility.searchMoviesByName(query, new ApiUtility.OnMoviesFetchedListener() {
            @Override
            public void onMoviesFetched(final List<Movie> movies) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movies != null) {
                            movieList.clear();
                            movieList.addAll(movies);
                            movieAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onError(final String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("SearchViewController", errorMessage);
                    }
                });
            }
        });
    }

    private void viewFavorites() {
        Intent intent = new Intent(SearchViewController.this, FavoriteMoviesController.class);
        startActivity(intent);
    }
}
