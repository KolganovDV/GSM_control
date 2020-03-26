package com.example.gsm_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_READ_SMS = 1;
    private static final int REQUEST_CODE_PERMISSION_SEND_SMS = 2;
    private static final int REQUEST_CODE_PERMISSION_RECEIVE_SMS = 3;

    String tv = null;
    private BroadcastReceiver sent      = null;
    private BroadcastReceiver delivered = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = findViewById(R.id.toolbar);*/
        //setSupportActionBar(toolbar);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    REQUEST_CODE_PERMISSION_SEND_SMS);
        }
        permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS},
                    REQUEST_CODE_PERMISSION_RECEIVE_SMS);
        }
        permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},
                    REQUEST_CODE_PERMISSION_READ_SMS);
        }

        //Регистрация широковещательного приемника: Отправка
        IntentFilter in_sent = new IntentFilter(SENT);
        sent = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                tv = "";
                /*tv.append(intent.getStringExtra("PARTS")+": ");
                tv.append(intent.getStringExtra("MSG")+": ");*/
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        tv = "\nSMS отправлено\n";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    default:
                        tv = "\nSMS не отправлено\n";
                        break;
                    /*case SmsManager.RESULT_ERROR_NO_SERVICE:
                        tv = "Нет сети\n";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        tv = "Null PDU\n";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        tv = "Нет связи\n";
                        break;*/
                }
                Toast.makeText(context, tv, Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "asd", Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(sent, in_sent);

        //Регистрация широковещательного приемника: Доставка
        IntentFilter in_delivered = new IntentFilter(DELIVERED);
        delivered = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                /*tv.append(intent.getStringExtra("PARTS")+": ");
                tv.append(intent.getStringExtra("MSG")+": ");*/
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        tv = "\nSMS доставлено\n";
                        Toast.makeText(context, tv, Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        tv = "SMS не доставлено\n";
                        break;
                }

            }
        };
        registerReceiver(delivered, in_delivered);

        NumberPicker numberPicker = findViewById(R.id.numberPicker1);
        numberPicker.setMinValue(18);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(24);
    }

    public void ClickButton1(View v){
        SendSMS("Button1");
    }

    String SENT      = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    private void SendSMS(String SMS_text) {
        //SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        String destinationAddress = "89124419584";//pref.getString("master",null);

        if (destinationAddress.length()<11) {
            Toast.makeText(this, "Неверный номер телефона", Toast.LENGTH_LONG).show();
            return;
        }
        // Find the sms_message view.

        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        Intent sentIntent = new Intent(SENT);
        PendingIntent pi_sent = PendingIntent.getBroadcast(this, 0, sentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Intent deliveredIntent = new Intent(DELIVERED);
        PendingIntent pi_delivered = PendingIntent.getBroadcast(this, 0, deliveredIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(destinationAddress, null, SMS_text,  pi_sent, pi_delivered);
    }
}