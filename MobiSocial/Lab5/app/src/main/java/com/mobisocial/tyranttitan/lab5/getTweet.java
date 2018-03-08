package com.mobisocial.tyranttitan.lab5;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class getTweet extends AsyncTask<String, Void, List<String>> {
    private static String TWITTER_CONSUMER_KEY = "hACZ4OthKHSuGV8Zim0XLo722";
    private static String TWITTER_CONSUMER_SECRET_KEY = "T22etWNgGceN4JJJcblYNyXPdqHYRZlx1KwairvQO0KaRJUQ26";
    private static String TWITTER_ACCESS_TOKEN = "967537631797137410-69ExIWEJvm0Ap03adcVph9UTCdWWCcn";
    private static String TWITTER_ACCESS_TOKEN_SECRET = "epOVNBmVnsXVB2c00x77y0lWowoLi1UvGtWhQoKii9x69";
    private String user = "";
    private MainActivity activity;
    private List<String> str = new ArrayList<String>();
    private ListView listView;

    public getTweet(String inputUser, MainActivity mainActivity) {
        super();
        this.activity = mainActivity;
        if (!inputUser.isEmpty()) {
            user = inputUser;
            Log.d("Titan", "Titan USER: " + user);
        }
    }


    protected List<String> doInBackground(String... strings) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET_KEY);
        builder.setOAuthAccessToken(TWITTER_ACCESS_TOKEN);
        builder.setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
        Configuration configuration = builder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();

        List<twitter4j.Status> statuses = new ArrayList<twitter4j.Status>();
        try {
            statuses = twitter.getUserTimeline(user);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        //Log.d("Titan", "Titan getTweet: " + String.valueOf(statuses.size()));

        for (twitter4j.Status status : statuses) {
            str.add("@"+String.valueOf(status.getId())+ " - " + status.getText());

            //Log.d("TITAN", "In GetTweet + " + status.toString());

        }
        return str;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        //Log.d("TITAN", "In GetTweet Total mess " + String.valueOf(str.size()));
        activity.setList(str);
        listView = (ListView) activity.findViewById(R.id.listview);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, str);

        listView.setAdapter(adapter);
    }
}
