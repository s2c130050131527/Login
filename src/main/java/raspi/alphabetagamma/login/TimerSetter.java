package raspi.alphabetagamma.login;


import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimerSetter extends AppCompatActivity implements View.OnClickListener {

    int mYear, mMonth, mDay, mHour, mMinute;
    Button starttimer,endtimer,savetimer,cleartimer;
    EditText editstart1,editend1,editstart2,editend2;
    String finalstart="";
    String finalend="";
    String id="";
    private Context ctx;
    String ipAddress="";
    String URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_setter);
        starttimer= (Button) findViewById(R.id.starttimer);
        endtimer= (Button) findViewById(R.id.endtimer);
        savetimer= (Button) findViewById(R.id.savetimer);
        cleartimer= (Button) findViewById(R.id.cleartimer);
        editstart1= (EditText) findViewById(R.id.editstart1);
        editend1= (EditText) findViewById(R.id.editend1);
        editstart2= (EditText) findViewById(R.id.editstart2);
        editend2= (EditText) findViewById(R.id.editend2);

        starttimer.setOnClickListener(this);
        endtimer.setOnClickListener(this);
        cleartimer.setOnClickListener(this);
        savetimer.setOnClickListener(this);

        editstart1.requestFocus();

        editstart1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editstart1.getText().toString().length()==1){
                    editstart2.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editstart2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editstart2.getText().toString().length()==1){
                    editend1.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editend1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editend1.getText().toString().length()==1){
                    editend2.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editend2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editend2.getText().toString().length()==1){
                    savetimer.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Bundle b= getIntent().getExtras();
        id=b.getString("id");
        ipAddress=b.getString("ip");
        ctx=this;
        URL="http://"+ipAddress+"/smarthome/timerset.php";

    }
    public void onClick(View v) {

        if (v == starttimer) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            editstart1.setText(String.format("%02d", hourOfDay));
                            editstart2.setText(String.format("%02d", minute));

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }
        if (v == endtimer) {


            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            editend1.setText(String.format("%02d", hourOfDay));
                            editend2.setText(String.format("%02d", minute));

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v==cleartimer){

            editstart1.setText("");
            editend1.setText("");

            editstart2.setText("");
            editend2.setText("");
        }

        if(v==savetimer){
            String str1=editstart1.getText().toString();
            String str2=editstart2.getText().toString();
            String str3=editend1.getText().toString();
            String str4=editend2.getText().toString();
            if(!(str1.equals(""))&& !(str2.equals(""))){
            finalstart=str1+str2;
            }
            if(!(str3.equals("")) && !(equals(""))){
            finalend=str3+str4;
            }
            Toast.makeText(this,"Timer set sucessfully",Toast.LENGTH_SHORT).show();
            editstart1.setText("");
            editend1.setText("");
            editstart2.setText("");
            editend2.setText("");
            Log.d(finalstart, "onClick: "+finalend);
            URL+="?id="+id+"&start="+finalstart+"&end="+finalend;
            SetTimeTask setTimeTask =new SetTimeTask(ctx);
            setTimeTask.execute(URL);
            Log.d("JSON",URL);
            finish();
        }

    }
}
