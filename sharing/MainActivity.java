package com.example.foodcarboncalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    ImageView BTNdashboard;
    CardView cardShopping, cardCarbon;

    private RelativeLayout relativeLayout;
    private ImageButton shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.bottom_calendar) {
                startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                return true;
            } else if (item.getItemId() == R.id.bottom_comment) {
                // Handle DestCommunity
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            }
            return false;
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);


        BTNdashboard = findViewById(R.id.dashboardImage);
            BTNdashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.example.foodcarboncalculator.MainActivity.this, DashboardDesign.class);
                    startActivity(intent);
                }
            });

        cardShopping = findViewById(R.id.button_discover1);
        cardShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });

        cardCarbon = findViewById(R.id.button_discover2);
        /*cardCarbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CarbonTipsActivity.class);
                startActivity(intent);
            }
        });*/
        // Find your HorizontalScrollView by its ID
        LinearLayout linearLayout = findViewById(R.id.insightLayout);

        // Set OnClickListener to the HorizontalScrollView
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_insights.class);
                startActivity(intent);
            }
        });

        relativeLayout = findViewById(R.id.relativeLayout);
        shareButton = findViewById(R.id.shareButton);

        // Change the image resource
        ImageView mainImageView = findViewById(R.id.image);
        mainImageView.setImageResource(R.drawable.new_image);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a bitmap of the layout
                Bitmap bitmap = createBitmapFromView(relativeLayout);

                // Share the generated image
                if (bitmap != null) {
                    shareImage(bitmap);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to generate image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void shareImage(Bitmap bitmap) {
        // Share the image
        Uri imageUri = getImageUri(bitmap);
        if (imageUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share image via"));
        } else {
            Toast.makeText(this, "Failed to get image Uri", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        File imageFile = new File(getCacheDir(), "shared_image.png");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return FileProvider.getUriForFile(this, "com.example.foodcarboncalculator", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
