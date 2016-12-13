package com.example.jihadshams.movies_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

public class DetailActivityFragment extends Fragment {

    String Name, Date, Overview, PosterPath,TrailerJsonStr,Vote;
    Boolean Adults;
    ArrayList<Object> MovieData = new ArrayList<>();
    ArrayList<String> TrailerNames = new ArrayList<>();
    ArrayAdapter<String> TrailerAdapter;
    ListView TrailerListView;
    ImageView star;

    int MenuItemId;

    int Id, pos;

    View rootView;
    Intent intent;
    String MovieJsonString;
    JSONObject BigJsonObject;
    JSONObject BlockIntoArray;

    String FvName, FvDate, FvOverview, FvPoster, FvId, Fvcheck;
    String Dates, Overviews, poster_path, Ids, Names, check,str;
    int countOfPosters;
    int result;


    private final String LOG_TAG = "sso";


    public DetailActivityFragment() {
    }

    @Override //Bundle for two pane (tablet)
    public void setArguments(Bundle args) {
        super.setArguments(args);
            Log.v(LOG_TAG, "Detail 1");
        MenuItemId = getArguments().getInt("menuu");
        MovieJsonString = getArguments().getString("string");  //Da meen
        pos = getArguments().getInt("pos",-1);
        Dates = getArguments().getString("Dates");
        Names = getArguments().getString("Names");
        poster_path = getArguments().getString("PosterPath");
        Ids = getArguments().getString("Ids");
        Overviews = getArguments().getString("Overviews");
    }

//        Log.v(LOG_TAG,MenuItemId+" : "+ Ids +" :"  +Names +" : "+ pos);
//
//            extras.putInt("menuu",MenuItemId);
//            extras.putInt("pos",position);
//            extras.putString("Names", Name);
//            extras.putString("Dates", Date);
//            extras.putString("Overviews", Overview);
//            extras.putString("Ids",Id);
//            extras.putString("PosterPath",PosterPath);
//            extras.putString("string",MovieJsonStr);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "HI :)) ");

        //to make the screen rotate
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        intent = getActivity().getIntent();
        final ImageView imageView;
        ImageView imageView1;

        if (intent != null && intent.hasExtra("string") && intent.hasExtra("Ids")) //check on intent.hasExtra("str") !!
        {
            Log.v(LOG_TAG, "Intent");
            pos = intent.getIntExtra("pos", -1);
            MovieJsonString = intent.getStringExtra("string");
            MenuItemId=intent.getIntExtra("menuu", 0);
            Names=intent.getStringExtra("Names");
            poster_path=intent.getStringExtra("PosterPath");
            Ids=intent.getStringExtra("Ids");
            Overviews=intent.getStringExtra("Overviews");

    }
        if (MenuItemId == 0 || MenuItemId == 2131493012 || MenuItemId == 2131493011) {  // Popular or toprated
                    Log.v(LOG_TAG,"MENU ITEM ID  " );

            try {
                Name = (String) getMoviePathFromJSOnString(pos, MovieJsonString).get(0);
                PosterPath = (String) getMoviePathFromJSOnString(pos, MovieJsonString).get(1);
                Overview = (String) getMoviePathFromJSOnString(pos, MovieJsonString).get(2);
                Date = (String) getMoviePathFromJSOnString(pos, MovieJsonString).get(3);
                Adults = (boolean) getMoviePathFromJSOnString(pos, MovieJsonString).get(4);
                Vote =  getMoviePathFromJSOnString(pos, MovieJsonString).get(5)+"";
                Id = (int) getMoviePathFromJSOnString(pos, MovieJsonString).get(6);
//                if (intent != null && intent.hasExtra("string") && intent.hasExtra("Ids")) //check on intent.hasExtra("str") !!
//                {
//                    Log.v(LOG_TAG, "Intent");
//                    pos = intent.getIntExtra("pos", -1);
//                    MovieJsonString = intent.getStringExtra("string");
//                    MenuItemId=intent.getIntExtra("menuu", 0);
//                    Names=intent.getStringExtra("Names");
//                    poster_path=intent.getStringExtra("PosterPath");
//                    Ids=intent.getStringExtra("Ids");
//                    Overviews=intent.getStringExtra("Overviews");
                if (intent != null){
                    if(intent.getBooleanExtra("isFav",false)){

                        Name= intent.getStringExtra("Names");
                        PosterPath = intent.getStringExtra("PosterPath");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v(LOG_TAG, Name);
            Log.v(LOG_TAG, Date);
            Log.v(LOG_TAG, PosterPath);

            try {
                // Log.v(LOG_TAG, "Hello into Details11 " + check + " " + date + " " + id + " " + poster + " " + title + " " + Overviews);

                ((TextView) rootView.findViewById(R.id.title)).setText(Name);
                imageView1 = ((ImageView) rootView.findViewById(R.id.image_Poster));
                Picasso.with(getActivity()).load(PosterPath).into(imageView1);
                ((TextView) rootView.findViewById(R.id.overView)).setText(Overview);
                ((TextView) rootView.findViewById(R.id.date)).setText("Release date :" + Date);
                ((TextView) rootView.findViewById(R.id.adults)).setText("Adults :" + Adults);
                ((TextView) rootView.findViewById(R.id.vote)).setText("Average rate :" + Vote);
                star = (ImageView) rootView.findViewById(R.id.fvimag);

                new geTrailer().execute(Id);
                new getReview(getActivity(), rootView).execute(Id);
                 TrailerAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_trailer, R.id.TrailerList, TrailerNames);

            } catch (Exception e) {
                Log.v(LOG_TAG, "Error in executing ", e);
                e.printStackTrace();
            }
            //To mark fav. films
            star.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    check = "true";
                    // Check if it's like or unlike
                    star.setImageResource(R.drawable.favourite1);  // Mark as yellow
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPref.edit();

                    //sharedPref.edit().clear().commit();
                    countOfPosters = sharedPref.getInt("countOfPosters : ", 0);
                   // Log.v(LOG_TAG,"FIRST count "+countOfPosters);
                    countOfPosters++;
                    editor.putInt("countOfPosters : ", countOfPosters);
                   // Log.v(LOG_TAG, "FIRST count " + countOfPosters);
                    editor.remove("countOfPosters : ").apply();;

                    FvId = sharedPref.getString("Ids : ", " ");
                    FvId += Id + "--";
                    editor.putString("Ids : ", FvId);
                    Log.v(LOG_TAG, "List of editor Ids " + FvId);

                    FvOverview = sharedPref.getString("Overviews : ", " ");
                    FvOverview += Overview + "&&&&";
                    editor.putString("Overviews : ", FvOverview);
                    Log.v(LOG_TAG, "List of editor Overviews " + FvOverview);

                    FvDate = sharedPref.getString("Dates : ", " ");
                    FvDate += Date + "--";
                    editor.putString("Dates : ", FvDate);
                    Log.v(LOG_TAG, "List of editor Dates " + FvDate);

                    Fvcheck = sharedPref.getString("check : ", "");
                    Fvcheck += check + "--";       //check
                    editor.putString("check : ", Fvcheck);
                    Log.v(LOG_TAG, "List of editor Check " + Fvcheck);

                    FvName = sharedPref.getString("Names : ", " ");
                    FvName += Name + "--";
                    editor.putString("Names : ", FvName);
                    Log.v(LOG_TAG, "List of editor name " + FvName);

                    FvPoster = sharedPref.getString("Poster_path : ", " ");
                    FvPoster += PosterPath + "--";
                    editor.putString("Poster_path : ", FvPoster);
                    Log.v(LOG_TAG, "List of editor Posters   :)))))))");

                    Log.v(LOG_TAG, "hello : film is get fav  " + countOfPosters + "\n hh  Name "+ FvName);
                    editor.commit();

                }
            });
        }
        else {    ///if favourite
            Log.v(LOG_TAG, "Craaaaaaaaaaaaaaaaaaaash");
            ((TextView) rootView.findViewById(R.id.title)).setText(Name);
            Log.v(LOG_TAG,Name);


            imageView = ((ImageView) rootView.findViewById(R.id.image_Poster));
            Picasso.with(getActivity()).load(PosterPath).into(imageView);

            ((TextView) rootView.findViewById(R.id.overView)).setText(Overview);
            ((TextView) rootView.findViewById(R.id.date)).setText("Release date :" + Date);

            int t = Integer.parseInt(Ids);
            Log.v(LOG_TAG, "Hello into Parsing t " + t);

            new geTrailer().execute(t);
           new getReview(getActivity(), rootView).execute(t);
            ((TextView) rootView.findViewById(R.id.title)).setText(Name);

            imageView1 = ((ImageView) rootView.findViewById(R.id.image_Poster));
            Picasso.with(getActivity()).load(PosterPath).into(imageView1);

            ((TextView) rootView.findViewById(R.id.overView)).setText(Overview);
            ((TextView) rootView.findViewById(R.id.date)).setText("Release date :" + Date);
            ((TextView) rootView.findViewById(R.id.adults)).setText("Adults :" + Adults);
            ((TextView) rootView.findViewById(R.id.vote)).setText("Average rate :" + Vote);
            star = (ImageView) rootView.findViewById(R.id.fvimag);

            new geTrailer().execute(Id);
            new getReview(getActivity(), rootView).execute(Id);


        }

        return rootView;
    }

    public ArrayList<Object> getMoviePathFromJSOnString(int posi, String MovieJsonString)
            throws JSONException {

        BigJsonObject = new JSONObject(MovieJsonString);
        BlockIntoArray = BigJsonObject.getJSONArray("results").getJSONObject(pos); ///get the block that contains poster paths,titles,....
        MovieData.add(BlockIntoArray.get("title"));
        MovieData.add("http://image.tmdb.org/t/p/w185" + BlockIntoArray.get("poster_path"));
        MovieData.add(BlockIntoArray.get("overview"));
        MovieData.add(BlockIntoArray.get("release_date"));
        MovieData.add(BlockIntoArray.get("adult"));
        MovieData.add(BlockIntoArray.get("vote_average"));
        MovieData.add(BlockIntoArray.get("id"));
        //HHERE


        return MovieData;
    }

    public class geTrailer extends AsyncTask<Integer, Void, ArrayList<String>> {
        InputStream inputStream;
        URL url;
        @Override
        protected ArrayList<String> doInBackground(Integer... params) {


            if (params.length == 0) {
                Log.v(LOG_TAG, "doInBackground is Null");
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

//http://api.themoviedb.org/3/movie/209112/videos?api_key=67c3e9a8357661792e5956106c3de3f6
                Uri buildUri;
                final String MOVIE_BASE_URL = "http://api.themoviedb.org//3/movie/";

                buildUri = Uri.parse(MOVIE_BASE_URL + params[0] + "/videos?api_key=4e0f950b54ed44326d0c24afd5359479").buildUpon().build();

                 url = new URL(buildUri.toString());
                // URL String
                Log.v(LOG_TAG, "Built URI of trailer " + url);

                //Internet Connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Get Data from link
                inputStream = urlConnection.getInputStream();
                 StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }


                //JSON String
                TrailerJsonStr = buffer.toString();
                  Log.v(LOG_TAG, "Trailer Json String  " + TrailerJsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {

                Log.e(LOG_TAG, "Trailer Json String Not found", e);
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
                return getTrailer_DataFromJson(TrailerJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "back to doInBackGround !");
                // e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(final ArrayList ArrayTrailer) {

            if (ArrayTrailer != null) {
                // Log.v(LOG_TAG, "Into onPost");
                try {
                    TrailerNames = getTrailerName(TrailerJsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TrailerAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_trailer, R.id.TrailerList, TrailerNames);//here
                TrailerListView = (ListView) rootView.findViewById(R.id.TrailerListView);
                TrailerListView.setAdapter(TrailerAdapter);
                //setListViewHeightBasedOnChildren(TrailerListView);
                TrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(ArrayTrailer.get(position).toString()));
                        startActivity(intent);
                    }
                });
            }
        }

        public ArrayList<String> getTrailerName(String TrailerJsonStr) throws JSONException {

            JSONObject BigJsonObject2 = new JSONObject(TrailerJsonStr);
            JSONObject BlockIntoArray;

            ArrayList<String> ArrayName = new ArrayList<>();

            result = BigJsonObject2.getJSONArray("results").length();
            Log.v(LOG_TAG, "res : " + result);
            try {
                for (int i = 0; i < result; i++) {
                    BlockIntoArray = BigJsonObject2.getJSONArray("results").getJSONObject(i);
                    ArrayName.add((String) BlockIntoArray.get("name"));
                }
                // Log.v(LOG_TAG, "Trailers : " + ArrayName);
            } catch (Exception e) {
                Log.e(LOG_TAG, "error in getting trailer", e);
                e.printStackTrace();

            }
            return ArrayName;

        }

        public ArrayList<String> getTrailer_DataFromJson(String TrailerJsonStr) throws JSONException {

            JSONObject BigJsonObject2 = new JSONObject(TrailerJsonStr);
            JSONObject BlockIntoArray;

            ArrayList<String> ArrayName = new ArrayList<>();
            ArrayList<String> ArrayTrailer = new ArrayList<>();
            ArrayList<String> ArrayKeys = new ArrayList<>();

            int result = BigJsonObject2.getJSONArray("results").length();
            // Log.v(LOG_TAG, "res : " + result);
            try {
                for (int i = 0; i < result; i++) {
                    BlockIntoArray = BigJsonObject2.getJSONArray("results").getJSONObject(i);

                    ArrayName.add((String) BlockIntoArray.get("name"));
                    ArrayKeys.add((String) BlockIntoArray.get("key"));
                    ArrayTrailer.add("https://www.youtube.com/watch?v=" + BlockIntoArray.get("key"));
                   // Log.v(LOG_TAG," khgkfn"+ArrayTrailer.get(1));
                }
                //  Log.v(LOG_TAG, "Trailers : " + ArrayName);
            } catch (Exception e) {
                Log.e(LOG_TAG, "error in getting trailer", e);
                e.printStackTrace();

            }
            return ArrayTrailer;


        }
    }


    }
