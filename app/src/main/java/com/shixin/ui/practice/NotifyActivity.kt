package com.shixin.ui.practice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shixin.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit

class NotifyActivity : AppCompatActivity() {


    private var notificationManager: NotificationManagerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        notificationManager = NotificationManagerCompat.from(this)
        var i = 0
        findViewById<Button>(R.id.btn).setOnClickListener {
            i++
            notifyAndProgress(i)
        }
    }

    private fun notifyAndProgress(id: Int) {
        val builder = NotificationCompat.Builder(this, "111")
        createNotificationChannel()
        builder.setContentTitle("File upload$id")
            .setContentText("upload in progress")
            .setSmallIcon(R.mipmap.ic_launcher)
        var i = 0
        val subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .takeUntil { aLong -> aLong == 20L }
            .subscribe {
                i += 5
                println("========$i")
                builder.setProgress(100, i, false)
                notificationManager?.notify(id, builder.build())

                if (i == 100) {
                    builder.setContentText("upload completed")
                    builder.setProgress(0, 0, false)
                    notificationManager?.notify(id, builder.build())
                   // close(subscribe)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun close(subscribe: Disposable?) {
        if (!subscribe?.isDisposed!!) {
            subscribe.dispose()
        }
    }


    fun notify1() {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, NotifyActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        createNotificationChannel()
        val notificationManager = NotificationManagerCompat.from(this)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build())

    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("111", "name", importance).apply {
                description = "descriptionText"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}