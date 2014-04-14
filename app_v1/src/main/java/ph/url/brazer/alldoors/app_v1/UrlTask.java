package ph.url.brazer.alldoors.app_v1;

import android.os.AsyncTask;

import java.util.ArrayList;

import ph.url.brazer.alldoors.app_v1.JSONResult.Parameter;

/**
 * Created by brazer on 23.03.2014.
 */
public class UrlTask extends AsyncTask<Parameter, Void, String> {

    private JSONResult result;

    @Override
    protected String doInBackground(Parameter... params) {
        result = new JSONResult(params);
        result.fetchData();
        return result.getStringResult();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equals("")) {
            try {
                throw new ResultException();
            } catch (ResultException e) {
                e.printStackTrace();
            }
        }
    }

    class ResultException extends Exception {

        public ResultException() {
            super("Нулевой результат");
        }

    }

    public ArrayList<String> getList(String name) {
        return result.getList(name);
    }

    public ArrayList<Item> getListItems(String name) {
        ArrayList<Item> list = new ArrayList<Item>();
        ArrayList<String> strList = getList(name);
        for (String s : strList)
            list.add(new Item(s));
        return list;
    }

}
