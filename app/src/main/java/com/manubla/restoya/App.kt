package com.manubla.restoya

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.manubla.restoya.inject.*
import com.manubla.restoya.presentation.view.splash.SplashActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.lang.ref.WeakReference

class App : Application() {
//    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(networkModule, storageModule,
                restaurantsModule, locationModule, viewModelsModule))
        }

        // Initializing LocalDate backport
        AndroidThreeTen.init(this)

        // Initializing Stetho inspector
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)

        registerActivityLifecycleCallbacks(Lifecycle())
    }


    inner class Lifecycle : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
            activity?.let {
                currentActivity = WeakReference(it)
            }
        }

        override fun onActivityStarted(activity: Activity?) {
            activity?.let {
                currentActivity = WeakReference(it)
            }
        }

        override fun onActivityDestroyed(activity: Activity?) {
            if (activity == currentActivity.get()) {
                currentActivity.clear()
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }
    }

    companion object {
        var currentActivity = WeakReference<Activity>(null)

        fun reloadSplash() {
            currentActivity.get()?.let {
                val intent = Intent(it, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                it.startActivity(intent)
                it.finish()
            }
        }
    }
}
