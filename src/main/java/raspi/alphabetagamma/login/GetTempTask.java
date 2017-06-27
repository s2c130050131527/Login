package raspi.alphabetagamma.login;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shreyngd on 5/4/17.
 */

public class GetTempTask extends AsyncTask<String, Void, String> {

    public TempListener tempListener;

    public GetTempTask(TempListener tempListener) {
        this.tempListener = tempListener;
    }

    public interface TempListener {
        void ontempload(String temp);

        void ontemperror();
    }


    @Override
    protected String doInBackground(String... params) {
        Log.d("gettemp", "temptaskrecieved");
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;


            while ((line = in.readLine()) != null) {

                response.append(line);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        if(s==null){
            tempListener.ontemperror();
        }
        else if (!(s.equals(""))) {
            tempListener.ontempload(s);
        }
        else {
            tempListener.ontemperror();
        }
    }

}
