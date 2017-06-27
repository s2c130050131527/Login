package raspi.alphabetagamma.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by shreyngd on 1/4/17.
 */

public class Myadapter extends SimpleAdapter {
    private Context ctx;
    private String ipAddress;
    private RoomFragment fragment;

    public Myadapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, String ipAddress, RoomFragment roomFragment) {
        super(context, data, resource, from, to);
        ctx = context;
        this.ipAddress = ipAddress;
        fragment = roomFragment;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ctx,"Jaadu",Toast.LENGTH_SHORT).show();
            }
        });

        Button onOff = (Button) v.findViewById(R.id.onoff);


        ImageButton setTimer = (ImageButton) v.findViewById(R.id.settime);


        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onoff = "/smarthome/switch.php?id=";
                String id = "";
                try {
                    id = ((Map) getItem(position)).get("ID").toString();
                } catch (Exception e) {
                    Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
                }
                String URL = "http://" + ipAddress + onoff + id;
                SwitchTask switchTask = new SwitchTask(ctx);
                switchTask.execute(URL);
//                Toast.makeText(ctx,URL,Toast.LENGTH_SHORT).show();
                fragment.updateList();
                //Android is Amazing

            }

        });

        setTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", ((Map) getItem(position)).get("ID").toString());
                b.putString("ip", ipAddress);
                Intent i = new Intent(ctx, TimerSetter.class);
                i.putExtras(b);
                fragment.startActivity(i);

//                Toast.makeText(ctx,"Ban rha hai",Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }

}
