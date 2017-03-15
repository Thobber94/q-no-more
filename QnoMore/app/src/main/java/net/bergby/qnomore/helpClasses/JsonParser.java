package net.bergby.qnomore.helpClasses;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by thomas on 15-Mar-17.
 */


public class JsonParser extends ArrayList<String>
{
    private static Context mContext;
    private ArrayList<String> restaurantNames = new ArrayList<>();
    private boolean warm;
    private boolean cold;
    private boolean food;
    private boolean drink;


    public JsonParser(Context context, String fileNameJson, boolean warm, boolean cold, boolean food, boolean drink) throws JSONException
    {
        String fileName = fileNameJson;
        mContext = context;

        // Sets which buttons have been pressed
        this.warm = warm;
        this.cold = cold;
        this.food = food;
        this.drink = drink;

        // Calls the method to start the process
        getJsonValue(fileName);
    }


    private void getJsonValue(String jsonFile) throws JSONException
    {

        JSONArray jsonArray;

        jsonArray = new JSONArray(readFile(jsonFile));

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

            String restaurant_names = mJsonObject.optString("restaurant_name");

            if (food && sellsFood != 0)
            {
                if (warm && sellsWarm != 0)
                {
                    restaurantNames.add(restaurant_names);
                }
                else if (cold && sellsCold != 0)
                {
                    restaurantNames.add(restaurant_names);
                }
            }
            else if (drink && sellsDrinks != 0)
            {
                if (warm && sellsWarm != 0)
                {
                    restaurantNames.add(restaurant_names);
                }
                else if (cold && sellsCold != 0)
                {
                    restaurantNames.add(restaurant_names);
                }
            }
            else
            {
                Log.e("Error", "No options valid");
            }
        }
    }


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

    public ArrayList<String> getRestaurantNames()
    {
        return restaurantNames;
    }
}
