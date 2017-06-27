package raspi.alphabetagamma.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class SetIPActivity extends AppCompatActivity {

    private Button setIp;
    private EditText getIp=null;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_ip);
        setIp = (Button) findViewById(R.id.button2);
        getIp = (EditText) findViewById(R.id.editText4);
        prefManager = new PrefManager(this);

//        if (!prefManager.isFirstTimeLaunch()) {
//             launchHomeScreen();
//        }
        setIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(getIp.getText().toString());
                        outputStreamWriter.close();
                        prefManager.setFirstTimeLaunch(false);
                        launchHomeScreen();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        });
    }
        private void launchHomeScreen() {

            startActivity(new Intent(SetIPActivity.this, MainActivity.class));
            finish();
        }
    }

