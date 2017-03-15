package net.bergby.qnomore.helpClasses;

import android.content.Context;
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
    private int warm;
    private int cold;
    private int food;
    private int drink;


    public JsonParser(Context context, String fileNameJson, int warm, int cold, int food, int drink) throws JSONException
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

            JSONObject menu = (JSONObject) mJsonObject.get("seller");

            int menuItems = menu.optInt("food");

            if (menuItems == 1 && food == 1)
            {
                String restaurant_names = mJsonObject.optString("restaurant_name");
                restaurantNames.add(restaurant_names);
            }
            else if (menuItems == 0 && food == 0)
            {
                System.out.println("Does not sell food");
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
