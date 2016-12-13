package com.example.jihadshams.movies_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();


        if (null == savedInstanceState) {

           DetailActivityFragment mDetailsFragment =  ((DetailActivityFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.container));    //fab is the id of activity_main ?!! changed
        }
    }


//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//                    startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
@Override
public boolean onOptionsItemSelected(MenuItem item) {

   int id = item.getItemId();
    return super.onOptionsItemSelected(item);
}
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_detail, menu);
//        return super.onCreateOptionsMenu(menu);
//
//    }

}
