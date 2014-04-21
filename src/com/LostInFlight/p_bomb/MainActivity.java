package com.LostInFlight.p_bomb;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class MainActivity extends Activity {

	private NumberPicker numPicker;
	private String destination, message;
	private int numToSend = 0; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        numPicker = (NumberPicker) findViewById(R.id.numberPicker);
        String[] values = {"1", "5", "10", "20", "50", "100", "500"};
        numPicker.setMinValue(1);
        numPicker.setMaxValue(values.length);
        numPicker.setDisplayedValues(values);
        
        registerForBroadcast("SMS_SENT");
        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText messageBox = (EditText) findViewById(R.id.contentBox);
				EditText destinationBox = (EditText) findViewById(R.id.recipientBox);
				destination = destinationBox.getText().toString();
		        message = messageBox.getText().toString();
		        
		        int[] values = {0, 1, 5, 10, 20, 50, 100, 500};
		        numToSend += values[numPicker.getValue()];
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void sendSMS() {
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(destination, null, message, sentPI, null);        
    }

	private void registerForBroadcast(String SENT) {
		registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
            	if (getResultCode() == Activity.RESULT_OK) {
            		Log.d("SMS_Status", "SMS Sent");
            		if (numToSend-- > 0){
            			SystemClock.sleep(2000);
            			sendSMS();
            		}
            	} else {
            		Log.d("SMS_Status", "SMS Failed to Send");
            		sendSMS();
            	}
            }
        }, new IntentFilter(SENT));
	}
    
}
