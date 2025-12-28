# Add project specific ProGuard rules here.

# Keep data classes for serialization
-keep class com.negativespace.puzzler.domain.model.** { *; }
-keep class com.negativespace.puzzler.data.local.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.negativespace.puzzler.**$$serializer { *; }
-keepclassmembers class com.negativespace.puzzler.** {
    *** Companion;
}
-keepclasseswithmembers class com.negativespace.puzzler.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Compose
-dontwarn androidx.compose.**

# Hilt
-dontwarn dagger.hilt.**
