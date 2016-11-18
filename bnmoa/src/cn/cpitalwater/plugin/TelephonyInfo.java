package cn.cpitalwater.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class TelephonyInfo extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals("getTelephonyInfo")) {
//			TelephonyManager telephonyManager = (TelephonyManager) this.cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            JSONObject r = new JSONObject();
//            r.put("IMEI", telephonyManager.getDeviceId());
            
            WifiManager wifi = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);  
            WifiInfo info = wifi.getConnectionInfo();  
            r.put("MAC", info.getMacAddress());
            callbackContext.success(r);
            
//            System.out.println("IMEI: "+ telephonyManager.getDeviceId());
            System.out.println("MAC: "+ info.getMacAddress());
        }
        else {
            return false;
        }
        return true;
	}
}
