package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;

public class CallBridgeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call_bridge);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        String dialedNumber = getIntent().getDataString();
        System.out.println("*************");
        System.out.println("Actual dialed number from URI " + dialedNumber + " Lenght: " + String.valueOf(dialedNumber.length()));
        dialedNumber = dialedNumber.replaceAll("%2B", "+");
        dialedNumber = PhoneNumberUtils.extractNetworkPortion(dialedNumber);
        System.out.println("After replace " + dialedNumber + " Lenght: " + String.valueOf(dialedNumber.length()));
        if (dialedNumber.substring(0,4).matches("tel:"))
        {
        	dialedNumber = dialedNumber.substring(4);
        };
        System.out.println("Actual dialed number is " + dialedNumber);

        System.out.println("(0,2) \"" + dialedNumber.substring(0, 2) + "\"");
//        dialedNumber = PhoneNumberUtils.formatNumber(dialedNumber);
//        System.out.println("Formatted dialed number is " + dialedNumber);
        
        String numSeq = new String();
        
        if (PhoneNumberUtils.isGlobalPhoneNumber(dialedNumber)){
            System.out.println("Global number" + dialedNumber);
        	if (dialedNumber.substring(0, 2).equals("+1")){
        		System.out.println(dialedNumber + " is US Number");
        		numSeq = "tel:"+ dialedNumber;
        	}
        	else if ( dialedNumber.substring(0,3).equals("011")){
        		System.out.println(dialedNumber + " is non US with 011");
        		numSeq = nonUsNumSeqBuild(dialedNumber);
        	}
        	else if (dialedNumber.substring(0,1).equals("+")){
        		System.out.println(dialedNumber + " is non US with +");
        		dialedNumber = dialedNumber.substring(1);
        		numSeq = nonUsNumSeqBuild(dialedNumber);
        	}
        	else {
        		System.out.println(dialedNumber + " is US number w/o country code");
        		numSeq = "tel:" + dialedNumber;
        	};
/*        }
        else{
            System.out.println("Not a global number" + dialedNumber);
        	if 
        	else{
        		System.out.println(dialedNumber + " is US number without \'+\'");
        		numSeq = "tel:"+ dialedNumber;
        	};*/
        };
        System.out.println("Number Sequence: " + numSeq);

//        Toast.makeText(getApplicationContext(), "\"" + dialedNumber + "\"", Toast.LENGTH_LONG).show();
        
        Uri number = Uri.parse(numSeq);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        startActivity(callIntent);
    }

    private String nonUsNumSeqBuild(String dialedNum){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        String bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        String delayTime = sharedPref.getString(getString(R.string.delayTime), getString(R.string.delay_default_value));

        int delayTimeNum = Integer.parseInt(delayTime);
       
        String pauses = new String();
        for(int i=0;i<delayTimeNum;i+=2)
        {
        	pauses = pauses + ",";
        }

        return "tel:" + bridgeNum + pauses + dialedNum;
    };
    
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
