package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

public class CallBridgeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call_bridge);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        String dialedNumber = getIntent().getDataString();

//        Toast.makeText(getApplicationContext(), dialedNumber , Toast.LENGTH_SHORT).show();        
        
        if (dialedNumber.substring(0,4).matches("tel:"))
        {
        	dialedNumber = dialedNumber.substring(4);
        };

//        Toast.makeText(getApplicationContext(), "\"" + dialedNumber + "\"", Toast.LENGTH_LONG).show();
        
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        String bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        String delayTime = sharedPref.getString(getString(R.string.delayTime), getString(R.string.delay_default_value));

        int delayTimeNum = Integer.parseInt(delayTime);
       
        String pauses = new String();
        for(int i=0;i<delayTimeNum;i+=2)
        {
        	pauses = pauses + ",";
        }

        String numSeq = "tel:" + bridgeNum + pauses + dialedNumber;
        System.out.println(numSeq);
        Uri number = Uri.parse(numSeq);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        startActivity(callIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_call_bridge, menu);
        return true;
    }

    
/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
