package raspi.alphabetagamma.login;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by shreyngd on 7/4/17.
 */

class GridViewAdapter extends SimpleAdapter {
    Context ctx;
    String ipAddress="";
    String URL="";

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public GridViewAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,String ipAddress) {


        super(context, data, resource, from, to);
        ctx = context;
        this.ipAddress = ipAddress;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= super.getView(position, convertView, parent);
        ImageButton delButton;
        delButton= (ImageButton) v.findViewById(R.id.timerdeletebutton);
        String starttime=((Map) getItem(position)).get("starttime").toString();
        String endtime=((Map) getItem(position)).get("endtime").toString();

        URL="http://"+ipAddress+"/smarthome/timerdelete.php?id="+((Map)getItem(position)).get("ID").toString()+"&type=";
        if(starttime.equals("")||endtime.equals("")){
            URL+="2";
        }
        else {
            URL+="1";
        }




        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SwitchTask task =new SwitchTask(ctx);
                Log.d("Timer", "onClick: "+URL);
                task.execute(URL);
                ((CheckTimer)ctx).updateGrid();

            }
        });
        return v;
    }
}
