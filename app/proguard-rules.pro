# Add project specific ProGuard rules here.
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-dontwarn kotlin.**
-keep class kotlin.** { *; }
