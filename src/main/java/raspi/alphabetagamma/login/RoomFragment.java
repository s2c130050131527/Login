package raspi.alphabetagamma.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment implements Listener {


    private ListView mListView;
    private String URL;
    private List<HashMap<String, String>> mAndroidMapList ;

    private static final String KEY_NAME = "name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ID="ID";


    private Listener listener=this;
    private Context ctx=getContext();

    Button seeTemp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String ipAddress="";

    private TimerTask timerTask;
    private Timer timer;


    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String param1, String param2,String ipAddress) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,ipAddress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            ipAddress=getArguments().getString(ARG_PARAM3);

        }

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View devices= inflater.inflate(R.layout.fragment_room, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mParam2);

        mAndroidMapList = new ArrayList<>();
        seeTemp= (Button) devices.findViewById(R.id.seetemp);
        mListView = (ListView) devices.findViewById(R.id.list_view);
        Button refresh= (Button) devices.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadJSONTask task = new LoadJSONTask(ctx,listener);
                task.execute(URL);
            }
        });

        URL=buildURL();
        LoadJSONTask task = new LoadJSONTask(ctx,listener);
        task.execute(URL);
        loadListView();
//        timerTask=new TimerTask() {
//            @Override
//            public void run() {
//                new LoadJSONTask(ctx,listener).execute(URL);
//            }
//        };
//        timer=new Timer();
//        timer.schedule(timerTask,0,10000);


//        new Thread(){
//            public void run(){
//                while (true){
//                    try{
//                        if(getActivity()==null){
//                            return;
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateList();
//                            }
//                        });
//
//                     Thread.sleep(5000);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }.start();

        seeTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("ipAddress",ipAddress);
                bundle.putString("roomid",mParam1);
                Intent intent=new Intent(getActivity(),SeeTemp.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });


        // Inflate the layout for this fragment
        return devices;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private String buildURL() {
        String URIBuilder="http://"+ipAddress+"/smarthome/result.php?roomid="+mParam1;
        return URIBuilder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
//        timer.cancel();
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLoaded(List<DeviceStats> deviceStatsList) {
        mAndroidMapList.clear();
        for (DeviceStats deviceStats: deviceStatsList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_ID,deviceStats.getId());
            map.put(KEY_NAME, deviceStats.getName());
            map.put(KEY_STATUS, deviceStats.getStatus());


            mAndroidMapList.add(map);
        }

        loadListView();
    }

    private void loadListView() {
        ListAdapter adapter = new Myadapter(getActivity(), mAndroidMapList, R.layout.list_item,
                new String[] { KEY_NAME, KEY_STATUS },
                new int[] {R.id.name, R.id.status },ipAddress,RoomFragment.this);
        mListView.setAdapter(adapter);
    }


    public void updateList() {
        mAndroidMapList.clear();
//        new GetTempTask(tempListener).execute("http://"+ipAddress+"/gettemp.php");
        new LoadJSONTask(ctx, listener).execute(URL);

    }
    @Override
    public void onError() {

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void onFragmentInteraction(Uri uri);
    }
}
