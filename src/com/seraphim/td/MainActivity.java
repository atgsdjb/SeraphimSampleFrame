package com.seraphim.td;


import com.seraphim.td.upnp.UpnpActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
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
        mDialog = new Dialog(this);
        Window window = mDialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDialog.setContentView(R.layout.layout_upnp_service_op_dialog);
        Button dialogButtonClose = (Button) mDialog.findViewById(R.id.clos_sulf);
        Button dialogButtonShwoALL = (Button) mDialog.findViewById(R.id.show_all_action);
        dialogButtonClose.setOnClickListener(mDialogListener);
        dialogButtonShwoALL.setOnClickListener(mDialogListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    private class DialogListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch(id){
			case R.id.show_all_action:
				break;
			case R.id.clos_sulf:
				mDialog.dismiss();
				break;
			default:
				break;
			}
		}
    	
    }
    private DialogListener mDialogListener = new DialogListener();
    public void click(View view){
    	int id = view.getId();
    	switch(id){
    	case R.id.upnp:
    		Intent intent = new Intent(this,UpnpActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.temp:
    		mDialog.show();
    		break;
    	default :
    		break;
    	}
    }
}
