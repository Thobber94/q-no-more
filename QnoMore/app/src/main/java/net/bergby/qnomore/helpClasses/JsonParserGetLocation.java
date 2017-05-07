package net.bergby.qnomore.helpClasses;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by thomas on 08-May-17.
 */
public class JsonParserGetLocation
{
    private ArrayList<HashMap<String, String>> locationData = new ArrayList<>();

    public JsonParserGetLocation(String url) throws ExecutionException, InterruptedException, JSONException
    {

        getJsonValue(url);
    }

    private void getJsonValue(String url) throws ExecutionException, InterruptedException, JSONException
    {
        JSONArray jsonArray;
        String json = String.valueOf(new jsonAsync().execute(url).get());
        jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++)
        {
            HashMap<String, String> locationDataTemp = new HashMap<>();
            JSONObject mJsonObject = jsonArray.getJSONObject(i);
            JSONObject location = (JSONObject) mJsonObject.get("location");

            String restaurant = mJsonObject.getString("restaurant_name");
            String latitude = location.optString("latitude");
            String longitude = location.optString("longitude");
            
            locationDataTemp.put("restaurant_name", restaurant);
            locationDataTemp.put("latitude", latitude);
            locationDataTemp.put("longitude", longitude);

            locationData.add(locationDataTemp);
            
        }
    }

    private class jsonAsync extends AsyncTask<String, String, String>
    {
        @SuppressWarnings("Duplicates")
        @Override
        protected String doInBackground(String... urls)
        {
            URL url1 = null;
            try
            {
                url1 = new URL(urls[0]);
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            HttpURLConnection request = null;
            try
            {
                assert url1 != null;
                request = (HttpURLConnection) url1.openConnection();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                assert request != null;
                request.connect();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            String response = null;
            try
            {
                Scanner s = new Scanner(new InputStreamReader(((InputStream) request.getContent()))).useDelimiter("\\A");
                response = s.hasNext() ? s.next() : "";

            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return response;
        }
    }

    public ArrayList<HashMap<String, String>> getLocationData()
    {
        return locationData;
    }
}
