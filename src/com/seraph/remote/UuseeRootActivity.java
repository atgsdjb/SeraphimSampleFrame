package com.seraph.remote;

import com.seraph.remote.airplay.UuseeAirPlayMDns;
import com.seraph.remote.client.UuseeClient;
import com.seraph.remote.upnp.UuseeUpnpActivity;
import com.seraphim.td.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class UuseeRootActivity extends Activity {

	@SuppressWarnings("unused")
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
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
    /**
     * 
     * @param view
     */
    public void click(View view){
    	int id = view.getId();
    	Intent intent = null;
    	switch(id){
    	case R.id.upnp:
    		intent = new Intent(this,UuseeUpnpActivity.class);
    		startActivity(intent);
    		break;
    	
    	case R.id.temp:
    		intent = new Intent(this,UuseeAirPlayMDns.class);
    		startActivity(intent);
    		break;
    	case R.id.server_client:
    		intent = new Intent(this,UuseeClient.class);
    		startActivity(intent);
    	default :
    		break;
    	}
    	
    }
	private static final int menuPlay = 1;
	private static final int menuPush=2;
	private static final int menuStop=3;
	private static final int menuSeek=4;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, menuPlay, menuPlay, "PLAY");
		menu.add(0, menuPush, menuPush, "PUSH");
		menu.add(0, menuPlay, menuStop, "STOP");
		menu.add(0, menuPlay, menuSeek, "SEEK");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}
}
