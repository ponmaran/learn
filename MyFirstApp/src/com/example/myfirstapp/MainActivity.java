package com.example.myfirstapp;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

//	public final static String com.example.myfirstapp.ATTR_NUM, com.example.myfirstapp.ATTR_DEL;
	public static AttributeSet attr_num = null, attr_delay = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get shared preferences
        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.base_layout);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        String a = sharedPref.getString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        String b = sharedPref.getString(getString(R.string.delayTime), getString(R.string.delay_default_value));
        String[] bridgeNum = a.split(":");
        String[] delayTime = b.split(":",bridgeNum.length);

        int i=0;
        RelativeLayout fieldParent = (RelativeLayout) findViewById(R.id.field_parent);

        for(;i<bridgeNum.length;i++){
        	System.out.println("Num \"" + bridgeNum[i] +"\" Del \"" + delayTime[i] + "\"");
        }

        Resources r = getResources();
        XmlPullParser parser = r.getXml(R.layout.attr_num);

        int state = 0;
        do {
        	System.out.println("ding");
            try {
                state = parser.next();
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }       
            if (state == XmlPullParser.START_TAG) {
                if (parser.getName().equals("EditText")) {
                    attr_num = Xml.asAttributeSet(parser);
                    break;
                }
            }
        } while(state != XmlPullParser.END_DOCUMENT);

        state = 0;
        parser = r.getXml(R.layout.attr_del);
        do {
        	System.out.println("dong");
            try {
                state = parser.next();
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }       
            if (state == XmlPullParser.START_TAG) {
                if (parser.getName().equals("EditText")) {
                    attr_delay = Xml.asAttributeSet(parser);
                    break;
                }
            }
        } while(state != XmlPullParser.END_DOCUMENT);
        
        i=0;
        do{
        	LinearLayout ll = new LinearLayout(this);
        	ll.setId(10 + i);
        	ll.setOrientation(LinearLayout.HORIZONTAL);
        	if (i == 0){
            	fieldParent.addView(ll);
        	}
        	else{
        		RelativeLayout.LayoutParams fieldSetLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        		fieldSetLP.addRule(RelativeLayout.BELOW, 10 + (i - 1));
        		fieldParent.addView(ll, fieldSetLP);
        	}
        	
            EditText numberBox = new EditText(this, attr_num);
            numberBox.setId(100 + i);
            
            numberBox.setText(bridgeNum[i], TextView.BufferType.NORMAL);
            
            EditText delayBox = new EditText(this, attr_delay);
            delayBox.setId(200 + i);
            
            delayBox.setText(delayTime[i], TextView.BufferType.NORMAL);

            ll.addView(numberBox);
            ll.addView(delayBox);
            
            i++;
        }while(i<bridgeNum.length && !bridgeNum[i].equals(""));
        setContentView(baseLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void editDone(View view) {

    	String bridgeNum = new String(),delayTime = new String();
    	for(int i=0;findViewById(100 + i) != null ; i++){
            EditText numBox = (EditText) findViewById(100 + i);
            EditText delBox = (EditText) findViewById(200 + i);
            String number = numBox.getText().toString();
            if (number.equals("")){
            	continue;
            }
            else{
            	if (bridgeNum.equals("")){
            		bridgeNum = number;
            		delayTime = delBox.getText().toString();
            	}
            	else{
            		bridgeNum = bridgeNum + ":" + number;
            		delayTime = delayTime + ":" + delBox.getText().toString();
            	}
            }
    	}
    	System.out.println("Number " + bridgeNum);
    	System.out.println("Delay " + delayTime);
    	
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.bridgeNum), bridgeNum);
        editor.putString(getString(R.string.delayTime), delayTime);
        editor.commit();
        
    	Toast.makeText(getApplicationContext(), "Information saved successfully!", Toast.LENGTH_LONG).show();
	}
    
    /** Called when the user clicks the Reset button */
    public void pressReset(View view) {

    	RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.base_layout);
    	RelativeLayout fieldParent = (RelativeLayout) findViewById(R.id.field_parent);
    	fieldParent.removeAllViews();

    	LinearLayout ll = new LinearLayout(this);
    	ll.setId(10);
    	ll.setOrientation(LinearLayout.HORIZONTAL);

		RelativeLayout.LayoutParams fieldSetLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		fieldSetLP.addRule(RelativeLayout.BELOW, 10 - 1);
		fieldParent.addView(ll, fieldSetLP);

		EditText numBox = new EditText(this, attr_num);
    	numBox.setId(100);
    	numBox.setText(R.string.bridge_default_value, TextView.BufferType.NORMAL);
    	
    	ll.addView(numBox);

        EditText delBox = new EditText(this, attr_delay);
        delBox.setId(200);
        delBox.setText(R.string.delay_default_value, TextView.BufferType.NORMAL);

        ll.addView(delBox);
        
        setContentView(baseLayout);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        editor.putString(getString(R.string.delayTime), getString(R.string.delay_default_value));
        editor.commit();
    	Toast.makeText(getApplicationContext(), "Reset Successful!", Toast.LENGTH_LONG).show();
	}

    /** Called when the user clicks the Call button */
    public void pressAdd(View view) {

    	RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.base_layout);
    	RelativeLayout fieldParent = (RelativeLayout) findViewById(R.id.field_parent);
    	
    	int i;
    	for(i=0;findViewById(100 + i) != null ; i++){
//    		System.out.println("Box Count");
    	};

    	LinearLayout ll = new LinearLayout(this);
    	ll.setId(10 + i);
    	ll.setOrientation(LinearLayout.HORIZONTAL);

		RelativeLayout.LayoutParams fieldSetLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		fieldSetLP.addRule(RelativeLayout.BELOW, 10 + (i - 1));
		fieldParent.addView(ll, fieldSetLP);
    	
    	EditText numBox = new EditText(this, attr_num);
    	numBox.setId(100 + i);
    	numBox.setText(R.string.bridge_default_value, TextView.BufferType.NORMAL);
    	ll.addView(numBox);

        EditText delBox = new EditText(this, attr_delay);
        delBox.setId(200 + i);
        delBox.setText(R.string.delay_default_value, TextView.BufferType.NORMAL);

        ll.addView(delBox);
        
        setContentView(baseLayout);    	
	}
}