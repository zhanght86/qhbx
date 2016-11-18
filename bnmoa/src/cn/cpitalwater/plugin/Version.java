package cn.cpitalwater.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class Version extends CordovaPlugin {
	
    public final String ACTION_GET_VERSION_CODE = "GetVersionCode";
    public final String ACTION_GET_VERSION_NAME = "GetVersionName";  
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		boolean result = false;
        PackageManager packageManager = this.cordova.getActivity().getPackageManager();
        if(action.equals(ACTION_GET_VERSION_CODE)) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), 0);
                result = true;
                callbackContext.success(packageInfo.versionCode);
            }
            catch (NameNotFoundException nnfe) {
                result = false;
                callbackContext.success(nnfe.getMessage());
            }
        }
        else if(action.equals(ACTION_GET_VERSION_NAME)) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), 0);
                result = true;
                callbackContext.success(packageInfo.versionName);
            }
            catch (NameNotFoundException nnfe) {
                result = false;
                callbackContext.success(nnfe.getMessage());
            }
        
        }
        
        return result;
	}

	 

}
