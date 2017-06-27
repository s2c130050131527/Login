package raspi.alphabetagamma.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class SeeTemp extends AppCompatActivity implements GetTempTask.TempListener {

    private GetTempTask.TempListener tempListener=this;
    TextView temperature;
    String ipAddress="";
    Runnable r;
    Thread t;
    ScheduledExecutorService s;
    private String roomId="1";
    private Handler handler;
    private TimerTask timerTask;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_temp);
        temperature= (TextView) findViewById(R.id.temperature);
        Bundle bundle=getIntent().getExtras();
        ipAddress=bundle.getString("ipAddress");
        roomId=bundle.getString("roomid");
        ScheduledExecutorService s;

        new GetTempTask(tempListener).execute("http://"+ipAddress+"/smarthome/temp.php?room_id="+roomId);

        r=new Runnable() {
            @Override
            public void run() {
                Log.d("runnable", "run: "+roomId);
                new GetTempTask(tempListener).execute("http://"+ipAddress+"/smarthome/temp.php?room_id="+roomId);
            }
        };
//        t = new Thread() {
//                public void run() {
//                    while (true) {
//                        try {
//                            if (getApplicationContext() == null) {
//                                return;
//                            }
//                            runOnUiThread(r);
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            };
//            t.start();

//        s=Executors.newScheduledThreadPool(1);
//        s.scheduleWithFixedDelay(r,5,5,TimeUnit.SECONDS);

//        handler = new Handler().isLoggable()
        timerTask= new TimerTask() {
            @Override
            public void run() {
                new GetTempTask(tempListener).execute("http://"+ipAddress+"/smarthome/temp.php?room_id="+roomId);
            }
        };

        timer=new Timer();
        timer.schedule(timerTask,0,5000);

    }

    @Override
    public void ontempload(String temp) {

        temperature.setText(temp);


    }

    @Override
    public void ontemperror() {
        timer.cancel();
        temperature.setText("No Sensor Found ");
//        Toast.makeText(this,"Temp Error",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();

    }
}
