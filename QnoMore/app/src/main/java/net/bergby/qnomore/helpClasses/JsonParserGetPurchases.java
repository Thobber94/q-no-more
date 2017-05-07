package net.bergby.qnomore.helpClasses;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by thomas on 15-Mar-17.
 */


public class JsonParserGetPurchases
{

    private Double total_sum = 0.0;
    private ArrayList<HashMap<String, String>> purchaseList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> purchaseListHistory = new ArrayList<>();

    public JsonParserGetPurchases(String url, String thisUserID) throws JSONException, IOException, ExecutionException, InterruptedException, ParseException
    {
        // Calls the method to start the process
        getJsonValue(url, thisUserID);
    }


    private void getJsonValue(String url, String thisUserID) throws JSONException, IOException, ExecutionException, InterruptedException, ParseException
    {

        JSONArray jsonArrayForward;
        String json = String.valueOf(new jsonAsync().execute(url).get());
        jsonArrayForward = new JSONArray(json);

        JSONArray jsonArray = new JSONArray();
        for (int i = jsonArrayForward.length() - 1; i >= 0; i--)
        {
            jsonArray.put(jsonArrayForward.get(i));
        }

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject mJsonObject = jsonArray.getJSONObject(i);

            String user_id = (String) mJsonObject.get("user_id");
            if (user_id.hashCode() == thisUserID.hashCode())
            {
                HashMap<String, String> tempPurchaseObject = new HashMap<>();

                Boolean isSold = (Boolean) mJsonObject.get("is_sold");
                String restaurant_name = (String) mJsonObject.get("restaurant_name");
                String purchase_date_unformatted = (String) mJsonObject.get("purchase_date");
                String confirmationCode = (String) mJsonObject.get("confirmation_code");
                String purchase_sum = (String) mJsonObject.get("purchase_sum");
                total_sum = total_sum + Double.parseDouble(purchase_sum);
                String purchase_items = (String) mJsonObject.get("purchase_items");

                tempPurchaseObject.put("is_sold", String.valueOf(isSold));
                tempPurchaseObject.put("restaurant_name", restaurant_name);
                tempPurchaseObject.put("purchase_date", purchase_date_unformatted);
                tempPurchaseObject.put("confirmation_code", confirmationCode);
                tempPurchaseObject.put("purchase_sum", purchase_sum);
                tempPurchaseObject.put("total_sum", String.valueOf(total_sum));
                tempPurchaseObject.put("purchase_items", purchase_items);

                if (!isSold)
                {
                    purchaseList.add(tempPurchaseObject);
                }
                else
                {
                    purchaseListHistory.add(tempPurchaseObject);
                }
            }
        }
    }

    // Duplicate code from the other jsonParser
    @SuppressWarnings("Duplicates")
    private class jsonAsync extends AsyncTask<String, String, String>
    {

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

    public Double getTotal_sum()
    {
        return total_sum;
    }

    public ArrayList<HashMap<String, String>> getPurchaseListHistory()
    {
        return purchaseListHistory;
    }

    public ArrayList<HashMap<String, String>> getPurchaseList()
    {
        return purchaseList;
    }
}
