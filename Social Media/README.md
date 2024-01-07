- add these two into android manifest

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

-----------------------------------------------------------
```xml
<provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.example.foodcarboncalculator"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/paths" />
        </provider>
```
- add a drawable, new_image
- add a new XML, paths
- i only edited the code from natasha's DashboardDesign
