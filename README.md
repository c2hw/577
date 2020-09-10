#Screenshot library
![demo](http://www.baidu.com/img/bdlogo.gif "demo")

##Step 1
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

##Step 2
	dependencies {
	        implementation 'com.github.c2hw:screenshot:1.0'
	}


##Step 3
    <activity android:name="com.c2hw577.screenshot.ScreenshotActivity" android:theme="@android:style/Theme.Translucent"/>

##Step 4
    ScreenshotUtils.getInstance(applicationContext).screenshot(object : OnScreenshotListener {

        override fun onSuccess(bitmap: Bitmap) {
            ...
        }

        override fun onError(e: Exception) {
            ...
        }
    })