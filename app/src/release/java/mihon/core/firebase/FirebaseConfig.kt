package mihon.core.firebase

import android.content.Context

// bchan: release builds ship without Firebase (same no-op as the foss build type)
object FirebaseConfig {
    fun init(context: Context) = Unit

    fun setAnalyticsEnabled(enabled: Boolean) = Unit

    fun setCrashlyticsEnabled(enabled: Boolean) = Unit
}
