gradle dependencies:
    
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")

--------------------------------------------------------------------------------------------
res/value/style:

  <resources>
      
      <style name="DialogStyle" parent="Theme.Design.Light.BottomSheetDialog">
      
            <item name="android:windowIsFloating">false</item>
          
              <item name="android:statusBarColor">@android:color/transparent</item>
          
              <item name="android:windowSoftInputMode">adjustResize</item>
          
      </style>

  </resources>