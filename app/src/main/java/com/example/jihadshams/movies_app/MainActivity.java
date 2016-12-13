package com.example.jihadshams.movies_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.GridView;

import static com.example.jihadshams.movies_app.R.menu.menu_main;


public class MainActivity extends AppCompatActivity implements NameListener {
    public final String LOG_TAG = "sso";
    boolean mTwoPane;
    int id;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
         Log.v(LOG_TAG, "Helllllllllllllllllo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //activity_main
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
          MainActivityFragment moviesFragment =  ((MainActivityFragment)getSupportFragmentManager()
               .findFragmentById(R.id.flPanel_One));    //fab is the id of activity_main
        FrameLayout flPanel2 = (FrameLayout) findViewById(R.id.flPanel_Two);
        if (null == flPanel2) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }
        Log.v(LOG_TAG,mTwoPane+" mpane");
            //mTwoPane=true;
//        By crash
        if (null == savedInstanceState) {
            MainActivityFragment MovFragment = new MainActivityFragment();
            MovFragment.setNameListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.flPanel_One, MovFragment).commit();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // new MainActivityFragment().updateMovieList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void setTabJsonAndPosition (int MenuItemId, int position, String Name, String Date , String Overview, String Id,String PosterPath, String MovieJsonStr,boolean isFav)
    {
        // single pane
        //if (!mTwoPane)
        if (!mTwoPane)
        {
            Log.v("LOG_TAG", "Intent");
            Log.v("Test","_____________fav clicked Main Activity Name :"+Name);
            Intent intent = new Intent(this, DetailActivity.class).putExtra("pos", position)
                    .putExtra("string", MovieJsonStr).putExtra("Names", Name).putExtra("Dates", Date)
                    .putExtra("Overviews", Overview).putExtra("PosterPath", PosterPath).putExtra("Ids", Id)
                    .putExtra("menuu",MenuItemId);
            intent.putExtra("isFav",isFav);
            startActivity(intent);
        }
        else // double pane : Tablet UI
        {
                Log.v(LOG_TAG,"Tablet");
            DetailActivityFragment DetailsTab= new DetailActivityFragment();
            Bundle extras =new Bundle();

            extras.putInt("menuu",MenuItemId);
            extras.putInt("pos",position);
            extras.putString("Names", Name);
            extras.putString("Dates", Date);
            extras.putString("Overviews", Overview);
            extras.putString("Ids",Id);
            extras.putString("PosterPath",PosterPath);
            extras.putString("string",MovieJsonStr);

            DetailsTab.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.flPanel_Two, DetailsTab).commit();



        }
    }


}
