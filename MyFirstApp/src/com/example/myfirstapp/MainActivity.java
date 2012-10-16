package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        String bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), "");
        String delayTime = sharedPref.getString(getString(R.string.delayTime), "0");

        if (bridgeNum.equals(""))
        	{
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "No Data");
            startActivity(intent);
        	}
        else
        	{
//          TextView textView = new TextView(this);
            EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
//          editText.setTextSize(15);
            editText1.setText(bridgeNum, TextView.BufferType.NORMAL);
            EditText editText2 = (EditText) findViewById(R.id.edit_delay);
            editText2.setText(delayTime, TextView.BufferType.NORMAL);

        	}; 
//        EditText editText = (EditText) findViewById(R.id.edit_bridge_num);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void savePrefs(String key, String value){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
       }

    /** Called when the user clicks the Send button */
    public void editDone(View view) {
/*        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_bridge_num);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
*/
        EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
        String bridgeNum = editText1.getText().toString();
        savePrefs(getString(R.string.bridgeNum), bridgeNum);
        
        EditText editText2 = (EditText) findViewById(R.id.edit_delay);
        String delayTime = editText2.getText().toString();        
        savePrefs(getString(R.string.delayTime), delayTime);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), "");
        delayTime = sharedPref.getString(getString(R.string.delayTime), "0");
        
        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_bridge_num);
//        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, "Bridge Number \"" + bridgeNum + "\" and " + "Dealy \"" + delayTime + "\" are saved.");
        startActivity(intent);

	}
}
