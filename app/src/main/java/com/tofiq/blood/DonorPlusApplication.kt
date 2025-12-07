package com.tofiq.blood

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.tofiq.blood.service.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DonorPlusApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase Crashlytics
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        
        // Create notification channel for FCM
        NotificationHelper.createNotificationChannel(this)
        
        // Subscribe to default topic (optional - remove if not needed)
        // FirebaseMessaging.getInstance().subscribeToTopic("all")
    }
}