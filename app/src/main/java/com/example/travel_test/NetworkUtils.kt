package com.example.travel_test

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog

object NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun showNetworkErrorDialog(activity: Activity) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("網路連接錯誤")
        alertDialogBuilder.setMessage("請確認您的設備已連接至網路。")
        alertDialogBuilder.setPositiveButton("確定") { dialog, _ ->
            dialog.dismiss()
            activity.finish() // 結束活動
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}