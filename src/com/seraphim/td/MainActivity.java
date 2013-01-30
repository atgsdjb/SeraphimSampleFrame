package com.seraphim.td;



import com.seraphim.td.remote.SeraphimRootActivity;
import com.seraphim.td.remote.upnp.SeraphimUpnpActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	static private final String TAG="com.seraphim.td"; 
	private Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
    	case R.id.go_to_remote:
    		Intent intent = new Intent(this,SeraphimRootActivity.class);
    		startActivity(intent);
    		break;
    	default :
    		break;
    	}
    }
}
