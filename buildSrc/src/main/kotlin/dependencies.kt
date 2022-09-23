object Versions {
    const val MIN_SDK = 24
    const val TARGET_SDK = 31
    const val COMPILE_SDK = 31
    const val BUILD_TOOLS = "30.0.3"

    const val PLAY_SERVICE_AUTH = "18.1.0"
    const val SERVICE = "4.3.10"
    const val AUTO_SERVICE = "1.0-rc7"
    const val GRADLE = "7.0.2"
    const val SONARQUEBE_GRADLE = "2.6.1"

    const val KOTLIN_POET = "1.6.0"
    const val KOTLIN = "1.6.21"
    const val CORE_VERSION = "1.8.0"
    const val COROUTINES = "1.6.0"

    const val APPCOMPAT = "1.3.0"
    const val SUPPORT = "1.0.0"
    const val MATERIAL = "1.4.0"
    const val SWIPE_REFRESH_LAYOUT = "1.1.0"
    const val CONSTRAINT_LAYOUT = "2.1.2"
    const val PERCENT_LAYOUT = "1.0.0"
    const val ANDROID_BROWSER = "1.0.0"
    const val EMOJI = "1.1.0"

    const val LIFE_CYCLE = "2.4.1"
    const val NAVIGATION = "2.3.5"
    const val PAGING = "3.1.1"
    const val DATASTORE = "1.0.0"


    const val KOIN = "3.2.0"
    const val ANKO = "0.10.5"

    const val GLIDE = "4.13.0"
    const val WEBP_DECODER = "2.0.4.12.0"
    const val ANDROID_SVG = "1.4"
    const val DISCRETE_SCROLLVIEW = "1.4.9"
    const val CIRCLE_IMAGEVIEW = "3.0.0"
    const val LOTTIE = "3.1.0"
    const val ROUNDED_IMAGEVIEW = "2.3.0"
    const val YUVTOMAT = "1.1.0"

    const val RX_JAVA = "2.2.21" //TODO: RxJava2 is end of life -> change to use RxJava3
    const val RX_ANDROID = "2.1.1"

    const val COMMON_CODEC = "1.10"
    const val OKIO = "1.17.4"
    const val RETROFIT = "2.9.0"
    const val RETROFIT_ADAPTER = "1.0.0" //TODO: Retrofit RxJava adapter deprecated (retrofit2-rxjava2-adapter) -> Follow this url to migrate to built-in RxJava2 Adapter: https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2
    const val GSON = "2.9.0"
    const val ROOM = "2.4.2"

    const val SIMPLE_XML = "2.7.1"
    const val JODA_TIME  = "2.10.13"
    const val EXIF_INTERFACE = "1.1.0"
    const val COMMON_TEXT = "1.7"

    //FOR TEST
    const val ANDROID_TEST = "1.1.0"
    const val ANDROID_SUPPORT_TEST = "1.1.0"
    const val ANDROID_TEST_ESPRESSO = "3.1.0"
    const val ANDROID_SUPPORT_TEST_ESPRESSO = "3.0.2"
    const val MOCKITO = "4.6.1"
    const val MOCKITO_KOTLIN = "4.0.0"
    const val POWERMOCK_TEST = "2.0.0-RC.4"
    const val POWERMOCK = "1.6.6"
    const val MOCKWEB_SERVER = "3.6.0"
    const val ORCHESTRATOR = "1.3.0"
    const val UIAUTOMATOR = "2.2.0"
    const val LINKEDIN_DEXMAKER = "2.21.0"
    const val HAMCREST = "1.3"
    const val JUNIT = "4.12"
    const val ARCH_CORE_TESTING = "2.1.0"
}

object Libs {

    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val SONARQUEBE_GRADLE_PLUGIN  = "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${Versions.SONARQUEBE_GRADLE}"

    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Versions.SERVICE}"
    const val GOOGLE_PLAY_SERVICE = "com.google.android.gms:play-services-auth:${Versions.PLAY_SERVICE_AUTH}"
    const val GOOGLE_AUTO_SERVICE = "com.google.auto.service:auto-service:${Versions.AUTO_SERVICE}"

    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val KOTLIN_JDK_7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
    const val KOTLIN_COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val KOTLIN_COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val CORE = "androidx.core:core:${Versions.CORE_VERSION}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_VERSION}"
    const val KOTLIN_POET = "com.squareup:kotlinpoet:${Versions.KOTLIN_POET}"

    const val ANDROID_SUPPORT = "androidx.legacy:legacy-support-v4:${Versions.SUPPORT}"
    const val ANDROID_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val SWIPE_REFRESH_LAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE_REFRESH_LAYOUT}"
    const val ANDROID_CARDVIEW = "androidx.cardview:cardview:${Versions.SUPPORT}"
    const val ANDROID_RECYCLE_VIEW = "androidx.recyclerview:recyclerview:${Versions.SUPPORT}"
    const val ANDROID_ANNOTATION = "androidx.annotation:annotation:${Versions.SUPPORT}"
    const val ANDROID_CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val ANDROID_PERCENT_LAYOUT = "androidx.percentlayout:percentlayout:${Versions.PERCENT_LAYOUT}"
    const val ANDROID_LIFECYCLE_VIEWMODEL  = "androidx.lifecycle:lifecycle-viewmodel:${Versions.LIFE_CYCLE}"
    const val ANDROID_LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Versions.LIFE_CYCLE}"
    const val ANDROID_BROWSER = "androidx.browser:browser:${Versions.ANDROID_BROWSER}"
    const val ANDROID_EMOJI = "androidx.emoji:emoji:${Versions.EMOJI}"
    const val ANDROID_EMOJI_BUNDLED = "androidx.emoji:emoji-bundled:${Versions.EMOJI}"

    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment:${Versions.NAVIGATION}"
    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui:${Versions.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_DYNAMIC = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.NAVIGATION}"

    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFE_CYCLE}"
    const val LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFE_CYCLE}"
    const val LIFECYCLE_LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFE_CYCLE}"
    const val PAGING_RUNTIME_KTX = "androidx.paging:paging-runtime-ktx:${Versions.PAGING}"

    const val KOIN_CORE = "io.insert-koin:koin-core:${Versions.KOIN}"
  //  const val KOIN_EXT = "io.insert-koin:koin-core-ext:${Versions.KOIN}"
    const val KOIN_ANDROID = "io.insert-koin:koin-android:${Versions.KOIN}"
   // const val KOIN_SCOPE = "io.insert-koin:koin-androidx-scope:${Versions.KOIN}"
   // const val KOIN_VIEWMODEL = "io.insert-koin:koin-androidx-viewmodel:${Versions.KOIN}"
    const val KOIN_JAVA = "io.insert-koin:koin-android-compat:${Versions.KOIN}"
    const val DATASTORE = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"
    const val ANKO = "org.jetbrains.anko:anko:${Versions.ANKO}"
    const val ANKO_COMMONS = "org.jetbrains.anko:anko-commons:${Versions.ANKO}"

    const val WINDOW_MANAGER = "androidx.window:window:1.0.0-alpha01"

    const val GOOGLE_GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val RXJAVA = "io.reactivex.rxjava2:rxjava:${Versions.RX_JAVA}"
    const val RX_ANDROID = "io.reactivex.rxjava2:rxandroid:${Versions.RX_ANDROID}"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_ADAPTER = "com.squareup.retrofit2:adapter-rxjava2:${Versions.RETROFIT}"
    const val RETROFIT_GSON_CONVERTER = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.9.1"

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
    const val GLIDE_WEB_DECODER = "com.zlc.glide:webpdecoder:${Versions.WEBP_DECODER}"
    const val CIRCLE_IMAGEVIEW = "de.hdodenhof:circleimageview:${Versions.CIRCLE_IMAGEVIEW}"
    const val CAVEROCK_SVG = "com.caverock:androidsvg:${Versions.ANDROID_SVG}"
    const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"

    const val ROUNDED_IMAGEVIEW = "com.makeramen:roundedimageview:${Versions.ROUNDED_IMAGEVIEW}"
    const val QUICKBIRDSSTUDIO_YUVTOMAT = "com.quickbirdstudios:yuvtomat:${Versions.YUVTOMAT}"
    const val DISCRETE_SCROLLVIEW = "com.yarolegovich:discrete-scrollview:${Versions.DISCRETE_SCROLLVIEW}"
    const val COMMON_CODEC = "commons-codec:commons-codec:${Versions.COMMON_CODEC}"
    const val OKIO = "com.squareup.okio:okio:${Versions.OKIO}"
    const val COMMON_TEXT = "org.apache.commons:commons-text:${Versions.COMMON_TEXT}"

    const val JODA_TIME = "joda-time:joda-time:${Versions.JODA_TIME}"
    const val EXIF_INTERFACE  = "androidx.exifinterface:exifinterface:${Versions.EXIF_INTERFACE}"

    //FOR TEST
    const val JUNIT = "junit:junit:${Versions.JUNIT}"

    const val ANDROID_TEST_JUNIT = "androidx.test.ext:junit:${Versions.ANDROID_TEST}"
    const val ANDROID_TEST_CORE = "androidx.test:core:${Versions.ANDROID_TEST}"
    const val ANDROID_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROID_TEST}"
    const val ANDROID_TEST_RULES = "androidx.test:rules:${Versions.ANDROID_TEST}"
    const val ANDROID_SUPPORT_TEST_RUNNER = "com.android.support.test:runner:${Versions.ANDROID_SUPPORT_TEST}"

    const val ANDROID_TEST_ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ANDROID_TEST_ESPRESSO}"
    const val ANDROID_TEST_ESPRESSO_INTENTS = "androidx.test.espresso:espresso-intents:${Versions.ANDROID_TEST_ESPRESSO}"
    const val ANDROID_TEST_ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:${Versions.ANDROID_TEST_ESPRESSO}"
    const val ANDROID_SUPPORT_TEST_ESPRESSO_CORE = "com.android.support.test.espresso:espresso-core:${Versions.ANDROID_SUPPORT_TEST_ESPRESSO}"

    const val MOCKITO_TEST_CORE = "org.mockito:mockito-core:${Versions.MOCKITO}"
    const val MOCKITO_TEST_INLINE = "org.mockito:mockito-inline:${Versions.MOCKITO}"
    const val MOCKITO_TEST_KOTLIN = "org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"

    const val POWERMOCK_TEST = "org.powermock:powermock:${Versions.POWERMOCK}"
    const val POWERMOCK_TEST_API = "org.powermock:powermock-api:${Versions.POWERMOCK}"
    const val POWERMOCK_TEST_CORE = "org.powermock:powermock-core:${Versions.POWERMOCK_TEST}"
    const val POWERMOCK_TEST_MOCKITO = "org.powermock:powermock-api-mockito2:${Versions.POWERMOCK_TEST}"
    const val POWERMOCK_TEST_JUNIT = "org.powermock:powermock-module-junit4:${Versions.POWERMOCK_TEST}"

    const val KOIN_TEST = "io.insert-koin:koin-test:${Versions.KOIN}"
    const val ORCHESTRATOR_TEST = "androidx.test:orchestrator:${Versions.ORCHESTRATOR}"
    const val HAMCREST_TEST_LIBRARY = "org.hamcrest:hamcrest-library:${Versions.HAMCREST}"
    const val UIAUTOMATOR_TEST = "androidx.test.uiautomator:uiautomator:${Versions.UIAUTOMATOR}"
    const val LINKEDIN_DEXMAKER_TEST = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.LINKEDIN_DEXMAKER}"
    const val MOCK_WEBSERVER_TEST = "com.squareup.okhttp3:mockwebserver:${Versions.MOCKWEB_SERVER}"
    const val ANDROID_ARCH_TEST = "androidx.arch.core:core-testing:${Versions.ARCH_CORE_TESTING}"

}
