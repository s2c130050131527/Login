package raspi.alphabetagamma.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shreyngd on 2/4/17.
 */
public class SetTimeTask extends AsyncTask<String,Void,String> {
    private Context ctx;
    private ProgressDialog progressDialog;
    public SetTimeTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(String s) {
//        progressDialog.dismiss();
        super.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {

//        progressDialog=new ProgressDialog(ctx);
//        progressDialog.setMessage("Loading....");
//        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder response=new StringBuilder();
        try {
            Log.d("Set TimeTAsk", "doInBackground: "+params[0]);
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = in.readLine()) != null) {

                response.append(line);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Resp", "doInBackground: " +response.toString());
        return response.toString();

    }
}