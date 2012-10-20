package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get shared preferences
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        String bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        String delayTime = sharedPref.getString(getString(R.string.delayTime), getString(R.string.delay_default_value));

        if (bridgeNum.equals(""))
        	{
/*            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "No Data");
            startActivity(intent);
*/        	}
        else
        	{
            EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
            editText1.setText(bridgeNum, TextView.BufferType.NORMAL);
            EditText editText2 = (EditText) findViewById(R.id.edit_delay);
            editText2.setText(delayTime, TextView.BufferType.NORMAL);

        	}; 

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void editDone(View view) {

        EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
        String bridgeNum = editText1.getText().toString();

        EditText editText2 = (EditText) findViewById(R.id.edit_delay);
        String delayTime = editText2.getText().toString();        

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.bridgeNum), bridgeNum);
        editor.putString(getString(R.string.delayTime), delayTime);
        editor.commit();
        
/*        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Bridge Number \"" + bridgeNum + "\" and " + "Dealy \"" + delayTime + "\" are saved.");
        startActivity(intent);
*/
	}
    
    /** Called when the user clicks the Reset button */
    public void pressReset(View view) {

        EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
        editText1.setText(R.string.bridge_default_value, TextView.BufferType.NORMAL);

        EditText editText2 = (EditText) findViewById(R.id.edit_delay);
        editText2.setText(R.string.delay_default_value, TextView.BufferType.NORMAL);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        editor.putString(getString(R.string.delayTime), getString(R.string.delay_default_value));
        editor.commit();
        
	}

    /** Called when the user clicks the Call button */
    public void pressCall(View view) {
//        String numSeq = "tel:" + bridgeNum + pauses + dialedNumber;
//        Uri number = Uri.parse(numSeq);
/*        Uri number = Uri.parse("tel:9803338444");
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        startActivity(callIntent);
*/
    	String phNum = "+19803338444,,p,w2355";
    	System.out.println( phNum + "Network portion" + PhoneNumberUtils.extractNetworkPortion(phNum));
//    	phNum = "9803338444";
    	System.out.println( phNum + "Post dial portion" + PhoneNumberUtils.extractPostDialPortion(phNum));
//    	phNum = "+19803338444,,p,w2355";
    	System.out.println( phNum + " Formated " +PhoneNumberUtils.formatNumber(phNum));
    	phNum = "011 91 98488 3244";
    	System.out.println( phNum + " Network Portion " + PhoneNumberUtils.extractNetworkPortion(phNum) );
//    	phNum = "+1-984-883-244";
    	System.out.println( phNum + (PhoneNumberUtils.isGlobalPhoneNumber(phNum)?" is Global":" is Local"));
	}
}