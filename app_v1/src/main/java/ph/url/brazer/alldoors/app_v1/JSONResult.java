package ph.url.brazer.alldoors.app_v1;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by brazer on 8.3.14.
 */
public class JSONResult {

    public static class Parameter {

        public String name, value;
        private static String url = "";

        public static void setUrl(String str) {
            url = str;
        }

    }

    private Parameter params[];
    private String result = "";
    private ArrayList<NameValuePair> nameValuePairs;
    private JSONArray arrayResult;

    public JSONResult(Parameter params[]) {
        nameValuePairs = new ArrayList<NameValuePair>();
        arrayResult = new JSONArray();
        this.params = params;
    }

    public String getStringResult() {
        return result;
    }
    public JSONArray getArrayResult() { return arrayResult; }

    public InputStream fetchData() {
        Log.i("JSONResult.fetchData()", "Start");
        buildParams();
        InputStream is = getHttpResponse();
        result = convert(is);
        Log.i("Result", result);
        parseToArrayJSON();
        return is;
    }

    private void buildParams() {
            for (int i=0; i<params.length; i++)
                nameValuePairs.add(new BasicNameValuePair(params[i].name, params[i].value));
    }

    private InputStream getHttpResponse() {
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Parameter.url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return is;
    }

    private String convert(InputStream is) {
        String res = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            res = sb.toString();
        } catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        return res;
    }

    private void parseToArrayJSON() {
        try {
            arrayResult = new JSONArray(result);
        }
        catch(JSONException e) {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
    }

    public ArrayList<String> getList(String name) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            arrayResult = new JSONArray(result);
            for(int i=0; i<arrayResult.length(); i++) {
                JSONObject json_data = arrayResult.getJSONObject(i);
                list.add(json_data.getString(name));
            }
        }
        catch(JSONException e) {
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
        return list;
    }

}
