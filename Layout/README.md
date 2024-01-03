i updated the bottom navigation view in activity_main.xml and MainActivity java

for every activity that contains bottom navigation view, please add this code

.java: 

public class [ActivityName] extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Bottom Navigation View
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setSelectedItemId(R.id.DestHome);
    bottomNavigationView.setOnItemSelectedListener(item -> {
        if (item.getItemId() == R.id.DestHome) {
            // Handle DestHome
            return true;
        } else if (item.getItemId() == R.id.DestCalender) {
            startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.DestCommunity) {
            // Handle DestCommunity
            return true;
        } else if (item.getItemId() == R.id.DestProfile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
            return true;
        }
        return false;
    });

}

activity.xml:
*the BNV and FAB have to be under coordinatorlayout*
<androidx.coordinatorlayout.widget.CoordinatorLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

<ImageView
    android:id="@+id/imageView3"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_marginTop="60dp"
    android:src="@drawable/background" />

<com.google.android.material.bottomnavigation.BottomNavigationView
    android:id="@+id/bottomNavigationView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    app:itemIconTint="@drawable/item_selector"
    app:itemRippleColor="@android:color/transparent"
    android:background="@drawable/transparent_background"
    android:backgroundTint="@color/colorPrimary"
    app:labelVisibilityMode="unlabeled"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:menu="@menu/menu_bottom">

</com.google.android.material.bottomnavigation.BottomNavigationView>

<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/floatingActionButton"
    android:layout_width="60dp"
    android:layout_height="60dp"
    android:backgroundTint="@color/colorPrimaryDark"
    android:contentDescription="calculator"
    android:scaleType="center"
    android:src="@drawable/calculator"
    app:fabCustomSize="60dp"
    app:layout_anchor="@+id/bottomNavigationView"
    app:layout_anchorGravity="center"
    app:maxImageSize="24dp"
    app:shapeAppearanceOverlay="@style/ShapeAppearanceOverlay.MyApp.FloatingActionButton"
    app:tint="@color/white" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
</androidx.drawerlayout.widget.DrawerLayout>
