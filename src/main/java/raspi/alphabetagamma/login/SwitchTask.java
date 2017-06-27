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

public class SwitchTask extends AsyncTask <String,Void,Void>{

    private ProgressDialog progressDialog;
    Context ctx;

    public SwitchTask(Context ctx) {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(ctx,ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d("SwitchTask",switching(params[0]));

        return null;
    }
    private String switching(String param) {
        StringBuilder response=new StringBuilder();
        try {
            URL url = new URL(param);
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
        return response.toString();
    }
}
//some changes