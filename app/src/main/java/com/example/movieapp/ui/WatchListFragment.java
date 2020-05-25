package com.example.movieapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.movieapp.Database.Movie;
import com.example.movieapp.Database.MovieDB;
import com.example.movieapp.MovieViewActivity;
import com.example.movieapp.R;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class WatchListFragment extends Fragment {

    LinearLayout watch_container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_watchlist, container, false);
        watch_container = root.findViewById(R.id.watchlist_container);
        new GetFromDB().execute();

        return root;
    }

    class GetFromDB extends AsyncTask<Void,Void, List<Movie>>{
        @Override
        protected List<Movie> doInBackground(Void... voids) {
           return  MovieDB.getInstance(getContext()).dao().getAll();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            for(final Movie movie: movies){
                View v = vi.inflate(R.layout.watchlist_row,null);
                TextView name_tv = v.findViewById(R.id.watch_name);
                TextView release_tv = v.findViewById(R.id.release_time_tv);
                TextView add_tv = v.findViewById(R.id.add_time_tv);
                name_tv.setText(movie.getName());
                release_tv.setText(movie.getRelease());
                add_tv.setText(movie.getAddTime());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {new AlertDialog.Builder(getContext())
                            .setTitle(movie.getName())
                            .setMessage("Please select your option")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Viewed", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent it = new Intent(getActivity(), MovieViewActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("user",getActivity().getIntent().getExtras().getParcelable("user"));
                                    it.putExtra("id",movie.getId());
                                    it.putExtras(bundle);
                                    startActivity(it);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteInstance(v).execute(movie);
                                }
                            })
                            .show();
                    }
                });
                watch_container.addView(v);
            }

        }
    }

    class DeleteInstance extends AsyncTask<Movie,Void,Boolean>{
        private View v;
        public DeleteInstance(View v) {
            this.v = v;
        }

        @Override
        protected Boolean doInBackground(Movie... movies) {
            try {
                MovieDB.getInstance(getContext()).dao().delete(movies);
                return true;
            }
          catch (Exception e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) watch_container.removeView(v);
        }
    }



}