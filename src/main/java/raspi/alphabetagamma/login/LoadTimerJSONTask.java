package raspi.alphabetagamma.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Process;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;

/**
 * Created by shreyngd on 7/4/17.
 */

class LoadTimerJSONTask extends AsyncTask<String,Void,TimerResponse>{

    Context ctx;
    TimerResponseListener timerResponseListener;
    private ProgressDialog progressDialog;

    public LoadTimerJSONTask(Context ctx, TimerResponseListener timerResponseListener) {
        this.ctx=ctx;
        this.timerResponseListener=timerResponseListener;
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(ctx,ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected TimerResponse doInBackground(String... params) {
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
        Gson gson = new Gson();
        return gson.fromJson(response.toString(), TimerResponse.class);

    }

    @Override
    protected void onPostExecute(TimerResponse timerResponse) {

        if(timerResponse!=null){
            timerResponseListener.onTimersLoad(timerResponse.getTimerList());
        }
        else{
            timerResponseListener.onTimerError();
        }
        progressDialog.dismiss();
    }
}
