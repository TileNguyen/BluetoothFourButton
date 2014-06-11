package com.example.BluetoothFourButton;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.ConnectBluetooth.ConnectBluetoothFourButton;
import com.example.ConnectBluetooth.DeviceListActivity;
import com.example.Databases.DeviceName;
import com.example.Databases.MyDatabases;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity implements CompoundButton.OnCheckedChangeListener {


    // 23/05/2014 TILE
    public static int n = 0;
    /**
     * Kiểm tra gửi đi là on hay off.
     */
    public static boolean test = true;
    /**
     * Kiểm tra có phải đã gửi và đang chờ không.
     */
    public static boolean flag = true;
    String note = "";
    byte[] readByte;

    public static String passWord;
    public static String passWord_New;
    public static String MY_PREFERENCES = "myPR";




    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Name of the connected device
    private static String mConnectedDeviceName = null;

    // DataBase
    MyDatabases databases;



    private static TextView mTitle, mTitle1;

    /*
     * ------------------------------------------------------------------------
     */
    private BluetoothAdapter mBluetoothAdapter = null;
    public static ConnectBluetoothFourButton mConnectBluetoothFourButton = null;

	/*
	 * -------------------------------------------------------------------------
	 */



    /* Update day 08/06/2014 */
    TextView tv_1, tv_2, tv_3, tv_4;
    public static ToggleButton tgbt_1, tgbt_2, tgbt_3, tgbt_4;
    CheckBox cb_dv_1, cb_dv_2, cb_dv_3, cb_dv_4;

    List<DeviceName> lsdvName = new ArrayList<DeviceName>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Custom actionBar
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.custom_title);
        }
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle1 = (TextView) findViewById(R.id.title_right_text );


        /* Check bluetooth available */
        checkBluetooth();

        // Create database.
        databases = new MyDatabases(this);



        // Initialized
        init();


        // Get passWord
        passWord = loadPreferences();

        // show name of device.
        getName();

    }




    /**
     * Get password when user open app.
     * @return
     */
    private String loadPreferences() {

        SharedPreferences sp = getSharedPreferences(MY_PREFERENCES,
                Context.MODE_PRIVATE);

        String str = "";
        if (sp.contains("passW")) {
            str = sp.getString("passW", "");

        } else {
            // pass mặc định.
            SharedPreferences.Editor ed = sp.edit();
            str = "00000000";
            ed.putString("passW", str);
            ed.commit();

        }

        return str;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(cb_dv_1.getVisibility() == View.VISIBLE || cb_dv_2.getVisibility() == View.VISIBLE ||
                cb_dv_3.getVisibility() == View.VISIBLE || cb_dv_4.getVisibility() == View.VISIBLE){

            cb_dv_1.setVisibility(View.INVISIBLE);
            cb_dv_2.setVisibility(View.INVISIBLE);
            cb_dv_3.setVisibility(View.INVISIBLE);
            cb_dv_4.setVisibility(View.INVISIBLE);

            cb_dv_1.setChecked(false);
            cb_dv_2.setChecked(false);
            cb_dv_3.setChecked(false);
            cb_dv_4.setChecked(false);

        }


    }

    /**
     * Get and set name
     */
    private void getName(){

        lsdvName = databases.getAllName();

        if(lsdvName.size() > 0){
            for(int i = 0; i < lsdvName.size(); i++){
                switch (i){
                    case 0:
                        tv_1.setText(lsdvName.get(i).get_name().toUpperCase());
                        break;
                    case 1:
                        tv_2.setText(lsdvName.get(i).get_name().toUpperCase());
                        break;
                    case 2:
                        tv_3.setText(lsdvName.get(i).get_name().toUpperCase());
                        break;
                    case 3:
                        tv_4.setText(lsdvName.get(i).get_name().toUpperCase());
                        break;
                }
            }
        }else{
            for(int i = 0; i < 4; i++){
                switch (i){
                    case 0:
                        DeviceName dv1 = new DeviceName();
                        dv1.set_name(tv_1.getText().toString());
                        databases.installFourButton(dv1);
                        break;
                    case 1:
                        DeviceName dv2 = new DeviceName();
                        dv2.set_name(tv_2.getText().toString());
                        databases.installFourButton(dv2);
                        break;
                    case 2:
                        DeviceName dv3 = new DeviceName();
                        dv3.set_name(tv_3.getText().toString());
                        databases.installFourButton(dv3);
                        break;
                    case 3:
                        DeviceName dv4 = new DeviceName();
                        dv4.set_name(tv_4.getText().toString());
                        databases.installFourButton(dv4);
                        break;
                }
            }
        }



    }


    /**
     * Method check bluetooth available of device.
     */
    private void checkBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        /* Check bluetooth available */

        if(mBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "" +
                    "", Toast.LENGTH_SHORT).show();

            finish(); // Close app.

            return;
        }

    }


    /**
     * Initialized.
     */
     private void init(){
         tgbt_1 = (ToggleButton)findViewById(R.id.tgbt_1);
         tgbt_2 = (ToggleButton)findViewById(R.id.tgbt_2);
         tgbt_3 = (ToggleButton)findViewById(R.id.tgbt_3);
         tgbt_4 = (ToggleButton)findViewById(R.id.tgbt_4);

         tv_1 = (TextView)findViewById(R.id.tv_1);
         tv_2 = (TextView)findViewById(R.id.tv_2);
         tv_3 = (TextView)findViewById(R.id.tv_3);
         tv_4 = (TextView)findViewById(R.id.tv_4);

         cb_dv_1 = (CheckBox)findViewById(R.id.cb_device_1);
         cb_dv_2 = (CheckBox)findViewById(R.id.cb_device_2);
         cb_dv_3 = (CheckBox)findViewById(R.id.cb_device_3);
         cb_dv_4 = (CheckBox)findViewById(R.id.cb_device_4);


         cb_dv_1.setOnCheckedChangeListener(this);
         cb_dv_2.setOnCheckedChangeListener(this);
         cb_dv_3.setOnCheckedChangeListener(this);
         cb_dv_4.setOnCheckedChangeListener(this);

         tgbt_1.setOnCheckedChangeListener(this);
         tgbt_2.setOnCheckedChangeListener(this);
         tgbt_3.setOnCheckedChangeListener(this);
         tgbt_4.setOnCheckedChangeListener(this);



     }


    /**
     * Create new variable handler
     *  get data from bluetooth service.
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.

            switch(msg.what){

                case MESSAGE_READ:
                    readByte = (byte[]) msg.obj;

                    if (readByte.length > 0) {

                        // new MyAsynTask().execute();

                        String str = "";
                        try {
                            str = new String(readByte, "US-ASCII");
                            // Log.d("DATA", str+"");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        ParseByteArray.parseByte(str);

                        /* Hiển thị Toast khi change password. */
                        if(str.equals("ok")){
                            savePreferences(passWord_New);
                        }
                        else if(str.equals("er")){
                            Toast.makeText(getBaseContext()," error ", Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;
                case MESSAGE_DEVICE_NAME:
				/* Get Device Name */
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connect to " + mConnectedDeviceName, Toast.LENGTH_LONG)
                            .show();

                    mTitle1.setText("Connect to " + mConnectedDeviceName);

                    // getActionBar().setTitle("Connect to " +
                    // mConnectedDeviceName);
                    sendMessages("a");
                    Log.d("TITLE", mConnectedDeviceName + "");

                    break;
                case MESSAGE_TOAST:
				/* Make Error */
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    mTitle1.setText("Not connect");
                    Log.d("TITLE", MESSAGE_TOAST + "");
                    break;

            }
        }
    };


    /* ------------------------ ON ACTIVITY LYCICLE --------------------- */

    @Override
    protected void onRestart() {
        super.onRestart();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Toast.makeText(getApplication(),"A",1).show();

        // Show name
        getName();

        cb_dv_1.setVisibility(View.INVISIBLE);
        cb_dv_2.setVisibility(View.INVISIBLE);
        cb_dv_3.setVisibility(View.INVISIBLE);
        cb_dv_4.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        /* Stop thread if it running */
        if(mConnectBluetoothFourButton != null){
            mConnectBluetoothFourButton.stop();
        }
    }

    @Override
    protected synchronized void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.

        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }else{
            if(mConnectBluetoothFourButton == null)
                setupConnect(); // Initialize connect.
        }

        if (mConnectBluetoothFourButton != null) {

            if (mConnectBluetoothFourButton.getState() != mConnectBluetoothFourButton.STATE_CONNECTED) {
                mTitle1.setText("Not connect");
            } else {
                mTitle1.setText("Connect to " + mConnectedDeviceName);
            }
        }
    }  // End onStart().





    /* --------------------------- END LYCICLE ------------------------------  */

    private void setupConnect() {
		/* Create new Service connect. */
        mConnectBluetoothFourButton = new ConnectBluetoothFourButton(this,
                handler);
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        switch (arg0) {
            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (arg1 == Activity.RESULT_OK) {
                    try {
                        // Get the device MAC address
                        String address = arg2.getExtras().getString(
                                DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        // Get the BLuetoothDevice object
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mConnectBluetoothFourButton.connect(device);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Try connect again.....", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("ERROR CONNECT", e.getMessage()+"");
                    }
                }
                break;
        }

    }

    /**
     * Sends a message. Sends byte[] to Device in the house.
     *
     * @param message
     *            A string of text to send.
     */
    public static void sendMessages(String message) {
        // Check that we're actually connected before trying anything
        if (mConnectBluetoothFourButton.getState() != ConnectBluetoothFourButton.STATE_CONNECTED) {
//            Toast.makeText(ge, "not connect device...",
//                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() >= 0) {
            byte[] send = message.getBytes();
            mConnectBluetoothFourButton.write(send);
        }
        Log.d("SEND", message);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // menu.add("Connect to "+mConnectedDeviceName);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.scan:
			/* Scan and connect device */
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.action_settings:
                Intent serverIntent1 = new Intent(this, ChangePassActivity.class);
                startActivity(serverIntent1);

//                diagLog();
                return true;

            case R.id.edit_name:

                // Hiển thị dialog cho phép edit name của button.
//                diagLogEditName();


                // show all checkbox in layout
                cb_dv_1.setVisibility(View.VISIBLE);
                cb_dv_2.setVisibility(View.VISIBLE);
                cb_dv_3.setVisibility(View.VISIBLE);
                cb_dv_4.setVisibility(View.VISIBLE);

                return true;
        }
        return false;
    }


    /**
     * Method check clicked on Toggle Button.
     //* @param view
     */
//    private void checkClick(View view){
//        switch(view.getId()){
//            case R.id.tgbt_1:
//                if (tgbt_1.isChecked()) {
//                    // device01.setChecked(false);
//                    sendMessages("o0" + passWord + "#");
//                    test = true;
//                    flag = true;
//                } else {
//                    // device01.setChecked(true);
//                    sendMessages("f0" + passWord + "#");
//                    test = false;
//                    flag = true;
//                }
//                break;
//            case R.id.tgbt_2:
//                if (tgbt_2.isChecked()) {
//                    sendMessages("o1" + passWord + "#");
//                    test = true;
//                    flag = true;
//                } else {
//                    sendMessages("f1" + passWord + "#");
//                    test = false;
//                    flag = true;
//                }
//                break;
//            case R.id.tgbt_3:
//                if (tgbt_3.isChecked()) {
//                    sendMessages("o2" + passWord + "#");
//                    test = true;
//                    flag = true;
//                } else {
//                    sendMessages("f2" + passWord + "#");
//                    test = false;
//                    flag = true;
//                }
//                break;
//            case R.id.tgbt_4:
//                if (tgbt_4.isChecked()) {
//                    sendMessages("o3" + passWord + "#");
//                    test = true;
//                    flag = true;
//                } else {
//                    sendMessages("f3" + passWord + "#");
//                    test = false;
//                    flag = true;
//                }
//                break;
//        }
//    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch(compoundButton.getId()){
            case R.id.cb_device_1:
                if(b){
                /* get one DeviceName in database */
                DeviceName dv1 = databases.getOneItem(1);
                 /* Update database. */
                dialogShow(dv1.get_name(),1);
                }
                break;
            case R.id.cb_device_2:
                if(b){
                    DeviceName dv2 = databases.getOneItem(2);
                    dialogShow(dv2.get_name(),2);
                }
                break;
            case R.id.cb_device_3:
                if(b){
                    DeviceName dv3 = databases.getOneItem(3);
                    dialogShow(dv3.get_name(),3);
                }
                break;
            case R.id.cb_device_4:
                if(b){
                    DeviceName dv4 = databases.getOneItem(4);
                    dialogShow(dv4.get_name(),4);
                }
                break;
            case R.id.tgbt_1:
                if (tgbt_1.isChecked()) {
                    // device01.setChecked(false);
                    sendMessages("o0" + passWord + "#");
                    test = true;
                    flag = true;
                } else {
                    // device01.setChecked(true);
                    sendMessages("f0" + passWord + "#");
                    test = false;
                    flag = true;
                }
                break;
            case R.id.tgbt_2:
                if (tgbt_2.isChecked()) {
                    sendMessages("o1" + passWord + "#");
                    test = true;
                    flag = true;
                } else {
                    sendMessages("f1" + passWord + "#");
                    test = false;
                    flag = true;
                }
                break;
            case R.id.tgbt_3:
                if (tgbt_3.isChecked()) {
                    sendMessages("o2" + passWord + "#");
                    test = true;
                    flag = true;
                } else {
                    sendMessages("f2" + passWord + "#");
                    test = false;
                    flag = true;
                }
                break;
            case R.id.tgbt_4:
                if (tgbt_4.isChecked()) {
                    sendMessages("o3" + passWord + "#");
                    test = true;
                    flag = true;
                } else {
                    sendMessages("f3" + passWord + "#");
                    test = false;
                    flag = true;
                }
                break;
        }

    }

    private void dialogShow(final String name, final int i){
        final Dialog dialog = new Dialog(this,R.style.myBackgroundStyleDialog);
        /* Dialog no title bar. */
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Change name device....");
        dialog.setContentView(R.layout.edit_name_device);

        setFinishOnTouchOutside(false);


        final EditText edit_name_device = (EditText)dialog.findViewById(R.id.edit_name_device);
        final Button btn_edit = (Button)dialog.findViewById(R.id.btn_edit_name_device);

        /* setText for EditText name of device. */
        edit_name_device.setText(name);
        switch (i){
            case 1:
                edit_name_device.setText(tv_1.getText());
                break;
            case 2:
                edit_name_device.setText(tv_2.getText());
                break;
            case 3:
                edit_name_device.setText(tv_3.getText());
                break;
            case 4:
                edit_name_device.setText(tv_4.getText());
                break;
        }
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Save data in Database. */
                String str_edit = edit_name_device.getText().toString();
                /* Check info user input */
                if(str_edit != ""){
                    DeviceName dv = new DeviceName();
                    dv.set_id(i);
                    dv.set_name(str_edit);
                    databases.updateFourButton(dv);
                    // show name device.
                    getName();

                    dialog.dismiss();


                }

            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // hide checkbox
                cb_dv_1.setVisibility(View.INVISIBLE);
                cb_dv_2.setVisibility(View.INVISIBLE);
                cb_dv_3.setVisibility(View.INVISIBLE);
                cb_dv_4.setVisibility(View.INVISIBLE);

                // set all checkbox unchecked.
                cb_dv_1.setChecked(false);
                cb_dv_2.setChecked(false);
                cb_dv_3.setChecked(false);
                cb_dv_4.setChecked(false);
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

         //dialog.show();

    }

    public boolean savePreferences(String str) {
        SharedPreferences sp = getSharedPreferences(
                MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("passW", str);
        if (ed.commit()) {
            passWord = str;
            ed.commit();
            return true;
        }
        Log.d("PASS CHANGE", str);
        return false;
    }



}
