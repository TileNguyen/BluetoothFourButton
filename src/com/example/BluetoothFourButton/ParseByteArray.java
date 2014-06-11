package com.example.BluetoothFourButton;

/**
 * Created with IntelliJ IDEA.
 * User: CP9
 * Date: 6/8/14
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParseByteArray {

public static void parseByte(String str){

        if(str.charAt(0) == 'o'){
            if(str.charAt(1) != 'k'){
                int i = Integer.parseInt(str.substring(1));
                if(MyActivity.flag){
                    if(MyActivity.test){
                        getOnDevice(i);
                    }
                    MyActivity.flag = false;
                }else{
                    getOnDevice(i);
                }
            }else{
                MyActivity.n = 1;
            }

        }
        if(str.charAt(0) == 'e' && str.charAt(1) == 'r'){
            MyActivity.n = 2;

        }
//			else if((bytes[0] == 'e') && (bytes[1] == 'r')){
//			MyActivity.n = 1;
//			Log.d("ER", MyActivity.n+"");
//		}
        else if(str.charAt(0) == 'f'){
            int i = Integer.parseInt(str.substring(1));
            if(MyActivity.flag){
                if(!MyActivity.test){
                    getOffDevice(i);
                }
                MyActivity.flag = false;
            }else{
                getOffDevice(i);
            }

        }
//		else if((bytes[0] == 'o') && (bytes[1] == 'k')){
//			MyActivity.n = 2;
//			Log.d("OK", MyActivity.n+"");
//		}
        else if(str.charAt(0) == 'a'){
//			String s = new String(b);
            for(int n = 1; n <=4 ;n++){
//				int i = Integer.parseInt(str.substring(n));
//				str3 +=n +" "+ s.substring(n) + "  ";
                getStatusDevice(str.charAt(n), n);
            }
        }

    }



    private static void getOnDevice(int i){
//		Log.d("GET", i + " ");
        switch(i){
            case 0:
                MyActivity.tgbt_1.setChecked(true);
                break;
            case 1:
                MyActivity.tgbt_2.setChecked(true);
                break;
            case 2:
                MyActivity.tgbt_3.setChecked(true);
                break;
            case 3:
                MyActivity.tgbt_4.setChecked(true);
                break;
        }
    }

    private static void getOffDevice(int i){
        switch(i){
            case 0:
                MyActivity.tgbt_1.setChecked(false);
                break;
            case 1:
                MyActivity.tgbt_2.setChecked(false);
                break;
            case 2:
                MyActivity.tgbt_3.setChecked(false);
                break;
            case 3:
                MyActivity.tgbt_4.setChecked(false);
                break;
        }
    }
    private static void getStatusDevice(char b, int i){
        switch(i){
            case 1:
                if(b == 'o'){
                    MyActivity.tgbt_1.setChecked(true);
                }else if(b == 'f'){
                    MyActivity.tgbt_1.setChecked(false);
                }
                break;
            case 2:
                if(b == 'o'){
                    MyActivity.tgbt_2.setChecked(true);
                }else if(b == 'f'){
                    MyActivity.tgbt_2.setChecked(false);
                }
                break;
            case 3:
                if(b == 'o'){
                    MyActivity.tgbt_3.setChecked(true);
                }else if(b == 'f'){
                    MyActivity.tgbt_3.setChecked(false);
                }
                break;
            case 4:
                if(b == 'o'){
                    MyActivity.tgbt_4.setChecked(true);
                }else if(b == 'f'){
                    MyActivity.tgbt_4.setChecked(false);
            }
                break;
        }
    }


}
