package raspi.alphabetagamma.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class LoadRoomJSONTask extends AsyncTask<String, Void, RoomResponse> {

    private final static String TAG = "LoadRoomJSONTask";

    private Context ctx;
    private RespListener respListener;




    public interface RespListener {

        void onLoaded(List<Room> roomList);
        void loadHomeFragment();
        void onError();
    }


    public LoadRoomJSONTask(Context ctx,RespListener respListener) {
        this.ctx = ctx;
        this.respListener=respListener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
         }

    @Override
    protected RoomResponse doInBackground(String... params) {
        Log.d(TAG, "doInBackground: ");
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
        return gson.fromJson(response.toString(), RoomResponse.class);

    }

    @Override
    protected void onPostExecute(RoomResponse roomResponse) {
        if (roomResponse != null) {
            respListener.onLoaded(roomResponse.getRoomList());
            respListener.loadHomeFragment();

        } else {
            respListener.onError();
        }

    }
}
