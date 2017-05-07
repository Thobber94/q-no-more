package net.bergby.qnomore.helpClasses;

import android.os.AsyncTask;
import android.util.Log;
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
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by thomas on 15-Mar-17.
 */


public class JsonParserGetMenus
{
    private ArrayList<String> restaurantNames = new ArrayList<>();
    private boolean warm;
    private boolean cold;
    private boolean food;
    private boolean drink;
    private String longitude;
    private String altitude;


    public JsonParserGetMenus(String url, boolean warm, boolean cold, boolean food, boolean drink) throws JSONException, IOException, ExecutionException, InterruptedException
    {

        // Sets which buttons have been pressed
        this.warm = warm;
        this.cold = cold;
        this.food = food;
        this.drink = drink;

        // Calls the method to start the process
        getJsonValue(url);
    }


    private void getJsonValue(String url) throws JSONException, IOException, ExecutionException, InterruptedException
    {

        JSONArray jsonArray;
        String json = String.valueOf(new jsonAsync().execute(url).get());
        jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject mJsonObject = jsonArray.getJSONObject(i);

            JSONObject seller = (JSONObject) mJsonObject.get("seller");

            int sellsFood;
            int sellsDrinks;
            int sellsWarm;
            int sellsCold;

            sellsDrinks = seller.optInt("drinks");
            sellsFood = seller.optInt("food");
            sellsWarm = seller.optInt("warm");
            sellsCold = seller.optInt("cold");

            String restaurant_names = null;

            if (food && sellsFood != 0)
            {
                if ((warm && sellsWarm != 0) || cold && sellsCold != 0)
                {
                    restaurant_names = mJsonObject.toString();
                }
            }
            else if (drink && sellsDrinks != 0)
            {
                if ((warm && sellsWarm != 0) || (cold && sellsCold != 0))
                {
                    restaurant_names = mJsonObject.toString();
                }
            }

            if (restaurant_names != null)
            {
                restaurantNames.add(restaurant_names);
            }
            else
            {
                Log.e("Error", "No options valid");
            }
        }
    }

    // ### OLD! FOR READING LOCAL FILE ### //

    /*
    // Reads file, and returns the JSON as string, sends it to getJsonValue method
    private static String readFile(String fileName)
    {
        String json;

        try
        {
            InputStream inputStream = mContext.getResources().getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return json;
    }
    */

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

    public ArrayList<String> getRestaurantNames()
    {
        return restaurantNames;
    }
}
