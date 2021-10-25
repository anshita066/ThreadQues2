package com.example.threadques2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*
import java.util.concurrent.Executors

class JobSched : JobService() {

    val rightNow: Calendar = Calendar.getInstance()
    val currenthour: Int = rightNow.get(Calendar.HOUR)
    val currentmin: Int = rightNow.get(Calendar.MINUTE)

    private val TAG = "MyJobScheduler"


    //On stop job Cancel JOb
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job Stopped")
        Toast.makeText(applicationContext, "Job Stopped", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        //createNotificationChannel1()
//        for (i in 1..10) {


        Executors.newSingleThreadExecutor().execute {
            //Work to be done when the job starts
            doWork()

        }


        //Job Finish and rescheduling job
        jobFinished(params, true)
        Toast.makeText(applicationContext, "Job Finished", Toast.LENGTH_SHORT).show()
        return true

    }

    private fun doWork() {

        //Taking system minutes and checking if the current minute is even or odd
        if (currentmin % 2 == 0) {

            //Even,calling notification channel 1
            createNotificationChannel1()

        } else {
            //Odd,calling notification channel 2
            createNotificationChannel2()
        }

    }

    //Notification Channel 1
    private fun createNotificationChannel1() {
        val CHANNEL_ID = "channelID"
        val CHANNEL_NAME = "channel1"
        val NOTIFICATION_ID = 1
        //Creating notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            //Creating Notification Channel
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        //Creating pending intent
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        //Creating Notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification-Channel-1")
            .setContentText("My notification 1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(NOTIFICATION_ID, notification)

    }


    //Notification Channel 2

    private fun createNotificationChannel2() {
        val CHANNEL_ID = "channelID2"
        val CHANNEL_NAME = "channel2"
        val NOTIFICATION_ID = 2


        //Creating notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        //Creating pending intent
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        //Creating Notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification-Channel-2")
            .setContentText("My notification 2")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(NOTIFICATION_ID, notification)

    }
}