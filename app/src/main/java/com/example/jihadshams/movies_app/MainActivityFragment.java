package com.example.jihadshams.movies_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment {

    String check;
    String poster;
    String Id;
    ImageAdapter imageAdapter;
    GridView gridView;
    boolean isFav= false;
    Uri buildUri;
    public final String LOG_TAG = "sso";
    String MovieJsonStr = null;
    ArrayList<String> arr = new ArrayList<>();

    String n, FvId, v;
    String PosterItem, IdItem, DateItem, NameItem, OverviewItem, CheckItem;
    HashSet<String> hashSet = new HashSet<>();
    String FvName, FvDate, FvOverview, FvPoster, FvCheck;
    ArrayList<String> FvPosterlist = new ArrayList<>();
    ArrayList<String> FvIdList = new ArrayList<>();
    ArrayList<String> FvCheckList = new ArrayList<>();
    ArrayList<String> FvDateList = new ArrayList<>();
    ArrayList<String> FvNameList = new ArrayList<>();
    ArrayList<String> FvOverviewList = new ArrayList<>();
    int countOfPosters ,MenuItemId;

    NameListener nameListener = null;


   Intent intent;
    ArrayList<String> urls = new ArrayList<String>();
    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    final String SIZE = "w185";
    final String RESULTS = "results";
    final String PATH = "poster_path";
    final String TITLE = "original_title";
    final String OVERVIEW = "overview";
    final String VOTE_AVERAGE = "vote_average";
    final String RELEASE_DATE = "release_date";
    final String ID="id";
    String path ,PosterPath,Date;
    String title ;
    String overview ;
    String voteAverage ;
    String releaseDate;
    String id;
    String url ;
    String Ids;
    int position;
   // MovieTask movieTask = new MovieTask();

 // private NameListener nameListener = null;
    private NameListener mNameListener;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }
//    void setNameListener (NameListener nameListener){
//        this.mNameListener=nameListener;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int id =item.getItemId();
        if(id==R.id.Popular)
        {
           // Log.e(LOG_TAG,"POOOOOOOOOP");
            new MovieTask().execute("popular");
            return true;
        }
        else if(id==R.id.TopRated)
        {
            //Log.e(LOG_TAG,"TOOOOP RATED");
            new MovieTask().execute("toprated");
            return true;

        }
        else if (id==R.id.Favourites)
        {
            FvName = sh.getString("Names : ", " ");
            FvDate = sh.getString("Dates : ", " ");
            FvOverview = sh.getString("Overviews : ", " ");
            FvPoster = sh.getString("Poster_path : ", " ");
            FvId = sh.getString("Ids : ", " ");
            countOfPosters = sh.getInt("countOfPosters : ", 0); //Leh 0
            //Log.v(LOG_TAG, "COP  "+countOfPosters);


            isFav =true;
            // Get all FV data and assing it to AddToFavorite method
            AddToFavorite(MenuItemId, FvName, FvDate, FvOverview, FvPoster, FvId, countOfPosters,isFav);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void AddToFavorite(int Menu ,String FvNam ,final String FvDat,String FvPoste ,final String FvOvervie , final String FvI,int countOfPosters,final boolean isFavV )
    {

        FvPosterlist = new ArrayList<>();
        FvIdList = new ArrayList<>();
        FvCheckList = new ArrayList<>();
        FvDateList = new ArrayList<>();
        FvNameList = new ArrayList<>();
        FvOverviewList = new ArrayList<>();

        for (String r : FvId.split("--", countOfPosters)) {
            v = r.replace("--", "");
            hashSet.add(v);
        }
        FvIdList.addAll(hashSet);
        Log.v(LOG_TAG, "List of Ids : " + FvIdList);
        hashSet.clear();


        for (String s : FvPoster.split("--", countOfPosters)) {

            n = s.replace("--", "");
            // to remove spaces
            n=n.trim();
            hashSet.add(n);
        }
        FvPosterlist.addAll(hashSet);
        //Log.v(LOG_TAG, "List of FvPoster : " + FvPosterlist);
        hashSet.clear();


        for (String r : FvName.split("--", countOfPosters)) {
            v = r.replace("--", "");
            hashSet.add(v);
        }
        FvNameList.addAll(hashSet);
        Log.v(LOG_TAG, "List of names : " + FvNameList);
        hashSet.clear();

        for (String r : FvDate.split("--", countOfPosters)) {
            v = r.replace("--", "");
            hashSet.add(v);
        }
        FvDateList.addAll(hashSet);
        // Log.v(LOG_TAG, "List of Dates : " + FvDateList);
        hashSet.clear();

        for (String r : FvOverview.split("&&&&", countOfPosters)) {
            v = r.replace("&&&&", "");
            hashSet.add(v);
        }
        FvOverviewList.addAll(hashSet);
        //Log.v(LOG_TAG, "List of FvOverew : " + FvOverviewList);
        hashSet.clear();

        ;

        // To show Fv films
        imageAdapter =new ImageAdapter(getActivity(),FvPosterlist);
        // Passing posters to Image Adapter
        gridView.setAdapter(null);
        gridView.setAdapter(imageAdapter);
        //here :::

        //showing Fv Details
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Log.v("Test","_______________________ fav clicked Name: "+ FvNameList.get(position));
                Log.v (LOG_TAG,"ON ITEM "+id);
                Log.v (LOG_TAG,"ON Pos "+position);

                DateItem = FvDateList.get(position);
                //  Log.v(LOG_TAG, "Date into fava " + DateItem);
                IdItem = FvIdList.get(position);
                //  Log.v(LOG_TAG, "Date into fava " + IdItem);

                PosterItem = FvPosterlist.get(position);
                Log.v(LOG_TAG, " Data into fava : Poster  Item " + PosterItem);
                NameItem = FvNameList.get(position);
                Log.v(LOG_TAG, "Data into fava  : Name " + NameItem);
                OverviewItem = FvOverviewList.get(position);
                //There is aproblem here !
                Log.v(LOG_TAG, "Date into fava " + OverviewItem);

                Log.v(LOG_TAG, ":((())))");

                Log.v(LOG_TAG, DateItem + "Data  " + PosterItem + " " + NameItem + " " + OverviewItem);
                //int MenuItemId,int postion,String tiltle,String Date,String overview,String Id,String poster, String MovieJsonString
                //sha3'aaal
                // elmoshkla  fe  namelistner
                nameListener.setTabJsonAndPosition(MenuItemId, position, NameItem, DateItem, OverviewItem, IdItem, PosterItem, MovieJsonStr,isFavV);
            }
        });
    }

    ////to start rotation
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        nameListener = (NameListener) activity;

    }


    @Override
    public void onStart() {
        super.onStart();

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        //Log.v(LOG_TAG, "Eroooooor in getting details  1 !! ");  //:)
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Get a reference to the gridview and attach this adapter to it.
        gridView = (GridView) rootView.findViewById(R.id.GridView_Posters);   //id of gridview(image view)
        MovieTask movieTask = new MovieTask();
        movieTask.execute("popular");
//        gridView.setAdapter(imageAdapter);

        // to make on item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
         public void onItemClick(AdapterView<?> adapterView, View view,  int  position, long Id) {
                //int MenuItemId,int postion,String tiltle,String Date,String overview,String Id,String poster, String MovieJsonString
                nameListener.setTabJsonAndPosition(MenuItemId, position, title, Date, overview, Ids, PosterPath, MovieJsonStr,isFav);
                //public void setTabJsonAndPosition(int MenuItemId, int position, String Name, String Date , String Overview, String Id,String PosterPath, String MovieJsonStr)

//                //((MainActivity) (getActivity())).setTabJsonAndPosition(PosTab, MovieStringTab);
                Log.v(LOG_TAG, MovieJsonStr);
                Log.v(LOG_TAG, "  POSITION "+position);
//         intent = new Intent(getActivity(), DetailActivity.class).putExtra("pos", position).putExtra("string", MovieJsonStr);
//         //                   .putExtra("Title",title).putExtra("ID",Id).putExtra("Vote Average",voteAverage).putExtra("Release Date",releaseDate).putExtra("Url",url).putExtra("urls",urls);
//
//               startActivity(intent);
//
            }

        });
         return rootView;

}
    public void setNameListener(NameListener nameListener)
    {
        this.nameListener = nameListener;
    }

    class MovieTask extends AsyncTask<String, Void, ArrayList> {

        //Detail detail;

        private ArrayList<String> getMoviePath(String moviesData)
                throws JSONException {

            JSONObject moviesJson = new JSONObject(moviesData);
            JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);
            urls.clear();
            arr.clear();
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieData = moviesArray.getJSONObject(i);
                path = movieData.getString(PATH);
                title = movieData.getString(TITLE);
                overview = movieData.getString(OVERVIEW);
                voteAverage = movieData.getString(VOTE_AVERAGE);
                releaseDate = movieData.getString(RELEASE_DATE);
                id = movieData.getString(ID);
                url = POSTER_BASE_URL.concat(SIZE).concat(path);
                arr.add(url);
                //detail = new Detail(title, overview, voteAverage, releaseDate, url, id);
                //Log.v(LOG_TAG ,url);
               // urls.add(detail);
            }
            return arr;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            //Log.v(LOG_TAG, "error ");
            if (arr != null) {
                // Log.v(LOG_TAG, "error ");
                imageAdapter = new ImageAdapter(getActivity(), arr);
                gridView.setAdapter(imageAdapter);

            } else {
                Log.v(LOG_TAG, "ON POST  ");
            }
        }

        @Override
        protected ArrayList doInBackground(String... params) {    //params selection

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/";   ///form the url to get JSON string
                final String MOVPopular_BASE_URL = "3/movie/popular?";
                final String MOVMostLikely_BASE_URL = "3/movie/top_rated?";
                final String API_KEY_PARAM = "api_key";
                //https://api.themoviedb.org/3/movie/550?api_key=4e0f950b54ed44326d0c24afd5359479

                // Log.v(LOG_TAG,params[0]);
                // params[0] = "popular";   // to default

                // choose popular or top rated
                if (params[0].equals("popular")) {
                    buildUri = Uri.parse(MOVIE_BASE_URL + MOVPopular_BASE_URL).buildUpon()
                            //.appendPath(MOVPopular_BASE_URL)
                            .appendQueryParameter(API_KEY_PARAM, BuildConfig.OPEN_MovieApp_API_KEY)
                            .build();
                } else if (params[0].equals("toprated")) {
                    buildUri = Uri.parse(MOVIE_BASE_URL + MOVMostLikely_BASE_URL).buildUpon()
                            //.appendPath(MOVPopular_BASE_URL)
                            .appendQueryParameter(API_KEY_PARAM, BuildConfig.OPEN_MovieApp_API_KEY)
                            .build();
                }

                //url and checking internet connection
                URL url = new URL(buildUri.toString());
                //Log.v(LOG_TAG, "Built URI " + buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");


                urlConnection.connect();

                /////////Read every thing in that Url into buffer
                InputStream inputStream = urlConnection.getInputStream();  //get input stream in a String
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                /////////Put all the data of the Url int from the inputStream into reader then into buffer to make the debugging easier.
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                MovieJsonStr = buffer.toString();  //string of lines
                // Log.v(LOG_TAG, "MOVIE Json String  " + MovieJsonStr);

            } catch (IOException e) {

                Log.e(LOG_TAG, "Movie Json String Not found", e);
                return null;
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {

                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error connection", e);
                    }
                }
            }
            try {
                // Log.e(LOG_TAG,MovieJsonStr);
                return getMoviePath(MovieJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;


        }
    }
//        Detail Movie=new Detail();
//        public void GetCurrentMovieData(ArrayList a) {
//        for (int i=0;i<a.size();i++)
//        {
//            if (Movie.getId()==Id)
//            {
//
//            }
//        }
//        }

//        public ArrayList<Object> getPoster_PathDataFromJson(String MovieJsonStr)
//                throws JSONException{
//           // Log.v(LOG_TAG,"Get poster");
//            BigJsonObject = new JSONObject(MovieJsonStr);
//            JSONArray jsonArray = BigJsonObject.getJSONArray("results");
//          BlockIntoArray = BigJsonObject.getJSONArray("results").getJSONObject(pos); // I think msh by3ml 7aga
//            for (int i=0;i<jsonArray.length();i++) {
//
//                BlockIntoArray = BigJsonObject.getJSONArray("results").getJSONObject(pos+i);   //Json object
//                String a=("http://image.tmdb.org/t/p/w185" + BlockIntoArray.get("poster_path"));
//                arrayList.add(a);
//                //arrayList.add("http://image.tmdb.org/t/p/w185" + BlockIntoArray.get("poster_path"));
//              // Log.v(LOG_TAG,"error "+ a);
//            }
//
//            return arrayList;
//
//        }

}
