package raspi.alphabetagamma.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadRoomJSONTask.RespListener, RoomFragment.OnFragmentInteractionListener {


    private final static String TAG = "MainActivity";

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nv;
    Menu menu;


    private List<HashMap<String, String>> RoomList;
    private static final String KEY_NAME = "name";
    //    private static final String KEY_STATUS = "status";
    private static final String KEY_ID = "ID";
    private LoadRoomJSONTask.RespListener listener;
    SwitchCompat smartswitch;


    private Context ctx = this;
    SubMenu subMenu;
    private String ipAddress;


    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            //filling variables
            toolbar = (Toolbar) findViewById(R.id.include);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.draweropen, R.string.drawerclose);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            nv = (NavigationView) findViewById(R.id.nv);
            menu = nv.getMenu();
            subMenu = menu.addSubMenu("Rooms");
            RoomList = new ArrayList<>();
            fragmentManager = getSupportFragmentManager();
            ipAddress = getIpAddress();
            //  *fill variable complete

            LoadRoomJSONTask loadRoomJSONTask = new LoadRoomJSONTask(this, this);
            loadRoomJSONTask.execute("http://" + ipAddress + "/smarthome/room.php");

       smartswitch = (SwitchCompat) menu.getItem(2).getActionView();
        smartswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SwitchTask task=new SwitchTask(ctx);
                    task.execute("http://"+ipAddress+"/smarthome/smarton.php");
                }
                else{
                    SwitchTask task=new SwitchTask(ctx);
                    task.execute("http://"+ipAddress+"/smarthome/smartoff.php");
                }
            }
        });
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
//                if(id==0) {
                    for (HashMap<String, String> room : RoomList) {
                        if (room.get(KEY_NAME).equals(item.getTitle())) {
                            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                            RoomFragment roomFragment = new RoomFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("param1", room.get(KEY_ID));
                            bundle.putString("param2", room.get(KEY_NAME));
                            bundle.putString("param3", ipAddress);
                            roomFragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.containerview, roomFragment);
                            fragmentTransaction.commit();
                            drawerLayout.closeDrawers();

                        }
                    }
//                }
//                else
//                    {
                        Log.d(TAG, "onNavigationItemSelected: " +id);
                switch (id) {
                    case R.id.nav_setting:
                        startActivity(new Intent(MainActivity.this, SetIPActivity.class));
                        return true;
                    case R.id.checktimer:
                        Bundle b=new Bundle();
                        b.putString("ipAddress",ipAddress);
                        Intent i =new Intent(ctx,CheckTimer.class);
                        i.putExtras(b);
                        startActivity(i);
                        return true;
//                    case R.id.nav_about:
//                        Toast.makeText(ctx, item.getTitle(), Toast.LENGTH_LONG).show();
//                        drawerLayout.closeDrawers();
//                        return true;
//                    case R.id.help:
//                        Toast.makeText(ctx, item.getTitle(), Toast.LENGTH_LONG).show();
//                        drawerLayout.closeDrawers();
//                        return true;
                    default:
                        Log.d(TAG, "Room Selected");
//                        Toast.makeText(ctx,, Toast.LENGTH_LONG).show();
                        return true;
                        }


//                    }

            }

        });

    }
    private String getIpAddress() {

        try {
            InputStream inputStream = openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                return stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public void onLoaded(List<Room> roomList) {
        for (Room room : roomList) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, room.getId());
            map.put(KEY_NAME, room.getName());
            subMenu.add(room.getName());
            RoomList.add(map);
        }
    }

    @Override
    public void loadHomeFragment() {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        RoomFragment roomFragment = new RoomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", RoomList.get(0).get(KEY_ID));
        bundle.putString("param2", RoomList.get(0).get(KEY_NAME));
        bundle.putString("param3", ipAddress);
        roomFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.containerview, roomFragment).commit();
    }


    @Override
    public void onError() {
        Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            showExitConfirmDialog();
        }
    }

    public void showExitConfirmDialog(){ // just show an dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit?"); // set title
        dialog.setMessage("Are you sure to exit?"); // set message
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity(); // when click OK button, finish current activity!
                    }
                });
        dialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show(); // just show a Toast, do nothing else
                    }
                });
        dialog.create().show();
    }
    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

