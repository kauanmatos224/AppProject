package com.example.securityaplication;


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.concurrent.TimeUnit



@RequiresApi(Build.VERSION_CODES.O)
fun Context.scheduleNotification(isLockScreen: Boolean) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val timeInMillis = System.currentTimeMillis() + LdifiMilis
    //////////////////////////////////////////////^FALAR EM MILISEEGUNDOS PARA O TEMPO DE ATIVAÇÃO DA NOTIFICAÇÃO


    with(alarmManager) {
        setExact(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver(isLockScreen))
    }

}

private fun Context.getReceiver(isLockScreen: Boolean): PendingIntent {
    // for demo purposes no request code and no flags
    return PendingIntent.getBroadcast(
        this,
        0,
        NotificationReceiver.build(this, isLockScreen),
        0
    )
}
@RequiresApi(Build.VERSION_CODES.O)
var LdifiMilis: Long = Lembrete.difMilis



