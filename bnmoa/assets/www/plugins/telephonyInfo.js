
var TelephonyInfo = function() {
};
TelephonyInfo.prototype.getIMEI = function(successCallback, failureCallback) {
    return cordova.exec(successCallback, failureCallback, 'TelephonyInfo', 'getTelephonyInfo', []);
};
if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.telephonyInfo) {
    window.plugins.telephonyInfo = new TelephonyInfo();
}
