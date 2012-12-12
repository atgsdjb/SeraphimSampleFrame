package com.seraphim.td;


import com.seraphim.td.upnp.UpnpActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	static private final String TAG="com.seraphim.td"; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,getPackageName());
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public void click(View view){
    	int id = view.getId();
    	switch(id){
    	case R.id.upnp:
    		Intent intent = new Intent(this,UpnpActivity.class);
    		startActivity(intent);
    		break;
    	default :
    		break;
    	}
    }
}
