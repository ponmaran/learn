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
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), "");
//        String message = new String();
        if (bridgeNum.equals(""))
        	{
//	            message = "Your input \"" + message + "\" is saved";
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "No Data");
            startActivity(intent);
        	}
        else
        	{
//			message = bridgeNum;
//          TextView textView = new TextView(this);
            EditText editText = (EditText) findViewById(R.id.edit_message);
//          editText.setTextSize(15);
            editText.setText(bridgeNum, TextView.BufferType.NORMAL);
//            setContentView(R.layout.activity_main);
        	}; 
//        EditText editText = (EditText) findViewById(R.id.edit_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
/*        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
*/
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.bridgeNum), message);
        editor.commit();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, "Your message \"" + message + "\" is saved");
        startActivity(intent);

	}
}
