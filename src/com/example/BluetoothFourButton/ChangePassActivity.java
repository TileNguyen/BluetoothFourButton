package com.example.BluetoothFourButton;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ConnectBluetooth.ConnectBluetoothFourButton;

import java.io.UnsupportedEncodingException;
import java.util.logging.LogRecord;

/**
 * Created with IntelliJ IDEA.
 * User: CP9
 * Date: 6/5/14
 * Time: 11:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangePassActivity extends Activity {


     String note;
     private EditText ed_old_pass, ed_new_pass, ed_new_pass_again;


    String _old, _new, _newAgain;

    Button btn_reset;
    public TextView txt_thong_bao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepass_layout);

        // Initialized
        init();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _old = ed_old_pass.getText().toString();
                _new = ed_new_pass.getText().toString();
                _newAgain = ed_new_pass_again.getText().toString();
                // strPass = _new;
                // Kiểm tra kết nối bluetooth.
                if (MyActivity.mConnectBluetoothFourButton.getState() == ConnectBluetoothFourButton.STATE_CONNECTED) {
                    // Kiểm tra password.
                    if (checkPass(_old, _new, _newAgain)) {

                        MyActivity.passWord_New = _new;
                        // Send password new.
						MyActivity.sendMessages("p" + MyActivity.passWord
								+ _new + "#");
                            finish();
                    }
                    // }
                    else {
                        Toast.makeText(getApplicationContext(), note,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Not connect Device...", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void init(){
        ed_old_pass = (EditText)findViewById(R.id.ed_old_pass);
        ed_new_pass = (EditText)findViewById(R.id.ed_new_pass);
        ed_new_pass_again = (EditText)findViewById(R.id.ed_new_pass_again);

        btn_reset = (Button)findViewById(R.id.btn_edit_name);
    }


    /**
     * Method get event when user click button change pass.
     */
    private void onClickButton(){

    }

    /**
     * Kiểm tra các thông tin password vừa nhập.
     *
     * @param _old
     * @param _new
     * @param _newAgain
     * @return
     */
    private boolean checkPass(String _old, String _new, String _newAgain) {

        if ((_old.length() != 8) || (_new.length() != 8)
                || (_newAgain.length() != 8)) {
            note = "Không đủ 8 ký tự...";
            return false;
        } else if (!new String(_new).equals(_newAgain)) {
            note = "password không giống nhau...";
            Log.d("PASS SO SANH", _new + " " + _newAgain);
            return false;
        } else if (!new String(_old).equals(MyActivity.passWord)) {
            note = "Sai password cũ...";
            Log.d("PASS SO SANH", MyActivity.passWord + " " + _old);
            return false;
        }

        return true;
    }



//    public boolean savePreferences(String str) {
//        SharedPreferences sp = getSharedPreferences(
//                MyActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed = sp.edit();
//        ed.putString("passW", str);
//        if (ed.commit()) {
//            MyActivity.passWord = str;
//            ed.commit();
//            return true;
//        }
//        Log.d("PASS CHANGE", str);
//        return false;
//    }



//    Handler handler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch(msg.what){
//                case MyActivity.MESSAGE_READ:
//                    byte[] readByte = (byte[]) msg.obj;
//
//                    if (readByte.length > 0) {
//
//                        // new MyAsynTask().execute();
//
//                        String str = "";
//                        try {
//                            str = new String(readByte, "US-ASCII");
//                            // Log.d("DATA", str+"");
//                        } catch (UnsupportedEncodingException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                        ParseByteArray.parseByte(str);
//
////                        Toast.makeText(getApplication(),str,1).show();
//                    }
//                    break;
//            }
//        }
//    };


}
