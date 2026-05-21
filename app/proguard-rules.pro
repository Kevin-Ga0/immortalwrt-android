# ImmortalWrt Manager ProGuard Rules

# Keep Moshi generated adapters
-keep class com.immortalwrt.manager.** {
    <init>(...);
}

# Keep data classes used with Moshi
-keep @com.squareup.moshi.JsonClass class *
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
