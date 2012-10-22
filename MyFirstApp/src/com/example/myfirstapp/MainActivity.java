package com.example.myfirstapp;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

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
        String[] delayTime = b.split(":");

        int i=0;
        RelativeLayout fieldParent = (RelativeLayout) findViewById(R.id.field_parent);

        for(;i<bridgeNum.length;i++){
        	System.out.println("Num \"" + bridgeNum[i] +"\" Del \"" + delayTime[i] + "\"");
        }

        Resources r = getResources();
        XmlPullParser parser = r.getXml(R.layout.attr_num);
        AttributeSet attr_num = null, attr_delay = null;

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
        	System.out.println("inside do-while: i1 = " + String.valueOf(i));
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

        	System.out.println("inside do-while: i2 = " + String.valueOf(i));
        	
            EditText numberBox = new EditText(this, attr_num);
            numberBox.setId(100 + i);
            
            numberBox.setText(bridgeNum[i], TextView.BufferType.NORMAL);

            System.out.println("inside do-while: i3 = " + String.valueOf(i));
            
            EditText delayBox = new EditText(this, attr_delay);
            delayBox.setId(200 + i);
            
            delayBox.setText(delayTime[i], TextView.BufferType.NORMAL);
            
            System.out.println("inside do-while: i4 = " + String.valueOf(i));
            
            ll.addView(numberBox);
            ll.addView(delayBox);
            
            i++;
            System.out.println("inside do-while: i5 = " + String.valueOf(i));
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

//        EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
        EditText editText1 = (EditText) findViewById(100);
        String bridgeNum = editText1.getText().toString();

//        EditText editText2 = (EditText) findViewById(R.id.edit_delay);
        EditText editText2 = (EditText) findViewById(200);
        String delayTime = editText2.getText().toString();        

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.bridgeNum), bridgeNum);
        editor.putString(getString(R.string.delayTime), delayTime);
        editor.commit();
        
	}
    
    /** Called when the user clicks the Reset button */
    public void pressReset(View view) {

//        EditText editText1 = (EditText) findViewById(R.id.edit_bridge_num);
        EditText editText1 = (EditText) findViewById(100);
        editText1.setText(R.string.bridge_default_value, TextView.BufferType.NORMAL);

//        EditText editText2 = (EditText) findViewById(R.id.edit_delay);
        EditText editText2 = (EditText) findViewById(200);
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