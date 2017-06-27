package raspi.alphabetagamma.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckTimer extends AppCompatActivity  implements TimerResponseListener{

    private List<HashMap<String, String>> TimerList;
    private static final String KEY_TID = "ID";
    private static final String KEY_DNAME = "devicename";
    private static final String KEY_RNAME="roomname";
    private static final String KEY_STIME = "starttime";
    private static final String KEY_ETIME="endtime";


    GridView gv;

    private String ipAddress;


    LoadTimerJSONTask loadTimerJSONTask ;
    
    TextView timerresp;

    TimerResponseListener timerResponseListener=this;

    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_timer);
        gv= (GridView) findViewById(R.id.timergridview);
//        timerresp = (TextView) findViewById(R.id.timerresp);
        ipAddress=getIntent().getExtras().getString("ipAddress");
        TimerList=new ArrayList<>();
        loadTimerJSONTask=new LoadTimerJSONTask(ctx,timerResponseListener);
        loadTimerJSONTask.execute("http://"+ipAddress+"/smarthome/checktimer.php");



    }


    @Override
    public void onTimersLoad(List<Timers> timersList) {
        TimerList.clear();
        for (Timers timer:timersList){
            HashMap<String,String> map=new HashMap<>();
            map.put(KEY_TID,timer.getTimerid());
            map.put(KEY_DNAME,timer.getDevicename());
            map.put(KEY_STIME,timer.getStarttime());
            map.put(KEY_ETIME,timer.getEndtime());
            map.put(KEY_RNAME,timer.getRoomname());
            
            TimerList.add(map);
        }
        loadgrid();
    }

   public void updateGrid()
    {
        loadTimerJSONTask=new LoadTimerJSONTask(ctx,timerResponseListener);
        loadTimerJSONTask.execute("http://"+ipAddress+"/smarthome/checktimer.php");
    }

    private void loadgrid() {

        GridViewAdapter adapter = new GridViewAdapter(ctx, TimerList, R.layout.timerlayout,
                new String[] { KEY_DNAME, KEY_RNAME,KEY_STIME,KEY_ETIME },
                new int[] {R.id.devicename,R.id.roomname, R.id.starttime,R.id.endtime },ipAddress);
        gv.setAdapter(adapter);
    }

    @Override
    public void onTimerError() {
        Toast.makeText(this,"Error reading timer", Toast.LENGTH_SHORT).show();

    }
}
