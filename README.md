Screenshot library
======
![demo](https://raw.githubusercontent.com/c2hw/screenshot/master/img/25D23F2F-17B6-42FC-9C72-979AAE4C5C12.png "demo")

Step 1
-----
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2
-----
	dependencies {
	        implementation 'com.github.c2hw:screenshot:1.0'
	}


Step 3
-----
    <activity android:name="com.c2hw577.screenshot.ScreenshotActivity" android:theme="@android:style/Theme.Translucent"/>

Step 4
-----
    ScreenshotUtils.getInstance(applicationContext).screenshot(object : OnScreenshotListener {

        override fun onSuccess(bitmap: Bitmap) {
            ...
        }

        override fun onError(e: Exception) {
            ...
        }
    })

Api 29 Step
-----
        <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

        <service
            android:name="com.c2hw577.screenshot.ScreenshotService"
            android:enabled="true"
            android:foregroundServiceType="mediaProjection" />