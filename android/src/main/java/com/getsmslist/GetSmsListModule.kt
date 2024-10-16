package com.getsmslist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener


private const val REQUEST_ENABLE_BT = 1
private const val REQUEST_READ_SMS = 2
private const val EVENT_PERMISSION = "permission"
private const val PERM_DENIED = 2
private const val PERM_ALLOWED = 3
private const val PERMISSION = Manifest.permission.READ_SMS
private val SMS = Uri.parse("content://sms")
private val TAG = GetSmsListModule::class.java.simpleName

class GetSmsListModule(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), PermissionListener, ActivityEventListener {
  init {
    reactContext.addActivityEventListener(this)
  }

  @SuppressLint("Recycle")
  @ReactMethod
  fun readSMS(options: ReadableMap, promise: Promise) {
    if (!hasReadSMSPermissions()) {
      requestReadSMSPermission()
      return
    }

    try {
      val sortOrderBy: String? = if (options.hasKey("orderBy")) options.getString("orderBy") else "date"
      val startBillingDate: String? = if (options.hasKey("minDate")) options.getString("minDate") else null
      val endBillingDate: String? = if (options.hasKey("maxDate")) options.getString("maxDate") else null
      var selection: String? = null
      var selectionArgs: Array<String>? = null
      if (startBillingDate != null && endBillingDate != null) {
        selection = "date > ? AND date < ?"
        selectionArgs = arrayOf(
          startBillingDate,
          endBillingDate
        )
      } else if (startBillingDate != null) {
        selection = "date > ?"
        selectionArgs = arrayOf(
          startBillingDate,
        )
      } else if (endBillingDate != null) {
        selection = "date < ?"
        selectionArgs = arrayOf(
          endBillingDate
        )
      }
      val cursor = reactContext.contentResolver.query(
        SMS, arrayOf("_id", "address", "date", "body"),
        selection,
        selectionArgs,
        sortOrderBy
      )

      val array: WritableArray = WritableNativeArray()
      while (cursor !== null && cursor.moveToNext()) {
        val wm = Arguments.createMap()
        wm.putString("id", cursor.getString(0))
        wm.putString("address", cursor.getString(1))
        wm.putString("date", cursor.getString(2))
        wm.putString("body", cursor.getString(3))
        array.pushMap(wm)
      }
      promise.resolve(array)
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  private fun hasReadSMSPermissions(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      reactContext.checkSelfPermission(PERMISSION) ==
        PackageManager.PERMISSION_GRANTED
    } else true
  }

  private fun requestReadSMSPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (currentActivity != null && currentActivity is PermissionAwareActivity) {
        (currentActivity as PermissionAwareActivity).requestPermissions(
          arrayOf(PERMISSION),
          REQUEST_READ_SMS,
          this
        )
      } else {
        Log.e(
          TAG,
          "requestReadSMSPermission: Activity is null or doesn't implement PermissionAwareActivity"
        )
      }
    }
  }

  override fun getName(): String {
    return NAME
  }

  override fun onRequestPermissionsResult(requestCode:Int, permissions: Array<out String>?, grantResults: IntArray?): Boolean {
    Log.d(TAG,
      "onRequestPermissionsResult, requestCode : $requestCode, permissions : $permissions, grantResults : $grantResults"
    )
    when (requestCode) {
      REQUEST_READ_SMS -> {
        val params = Arguments.createMap()
        // If request is cancelled, the result arrays are empty.
        val status =
          grantResults?.size!! > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
        params.putInt("type", REQUEST_READ_SMS)
        params.putInt("status", if (status) PERM_ALLOWED else PERM_DENIED)
        sendEvent(EVENT_PERMISSION, params)
        return status
      }
    }
    return false
  }

  private fun sendEvent(eventName: String, params: WritableMap?) {
    reactContext.getJSModule(RCTDeviceEventEmitter::class.java).emit(eventName, params)
  }

  override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
    if (activity != null) {
      Log.d(TAG, "onActivityResult, requestCode : $requestCode, resultCode : $resultCode")

      if (requestCode == REQUEST_ENABLE_BT) {
        val params = Arguments.createMap()
        params.putInt("type", REQUEST_ENABLE_BT)
        params.putInt("status", if (resultCode == Activity.RESULT_OK) PERM_ALLOWED else PERM_DENIED)
        sendEvent(EVENT_PERMISSION, params)
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {

  }

  companion object {
    const val NAME = "GetSmsList"
  }
}
