package net.bergby.qnomore.helpClasses;

import android.os.AsyncTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by thomas on 15-Mar-17.
 */


public class JsonParserPostPurchase
{

    private String userId;
    private String date;
    private String confirmationCode;
    private double sum;
    private String items;
    private String serverUrl;
    private int response;
    private onResponseCodeRecieved mCallback;
    private String restaurant;

    public interface onResponseCodeRecieved
    {
        void onResponseCodeRecievedMethod(int code);
    }

    public JsonParserPostPurchase(String userId, String url, String date, String confirmationCode, double sum, String items, String restaurant, onResponseCodeRecieved mCallback) throws JSONException, IOException, ExecutionException, InterruptedException
    {
        this.userId = userId;
        this.confirmationCode = confirmationCode;
        this.date = date;
        this.sum = sum;
        this.items = items;
        this.serverUrl = url;
        this.mCallback = mCallback;
        this.restaurant = restaurant;
        // Calls the method to start the process
        new jsonAsync().execute();
    }

    private class jsonAsync extends AsyncTask<Void, Void, Void>
    {
        String createJsonString() throws JsonProcessingException
        {

            String jsonStringMethod;

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.createObjectNode();

            ((ObjectNode) rootNode).put("user_id", userId);
            ((ObjectNode) rootNode).put("restaurant_name", restaurant);
            ((ObjectNode) rootNode).put("purchase_date", date);
            ((ObjectNode) rootNode).put("confirmation_code", confirmationCode);
            ((ObjectNode) rootNode).put("purchase_sum", sum);
            ((ObjectNode) rootNode).put("purchase_items", items);

            jsonStringMethod = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            return jsonStringMethod;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                String jsonString = createJsonString();
                JSONObject object = new JSONObject(jsonString);

                URL url = new URL(serverUrl);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(object.toString());
                wr.flush();
                wr.close();
                response = httpURLConnection.getResponseCode();
                httpURLConnection.disconnect();

            } catch (JSONException | IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            mCallback.onResponseCodeRecievedMethod(response);
        }
    }

}
