package com.example.foodcarboncalculator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class share_activity extends AppCompatActivity {

    ImageView imageshare;
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        imageshare = findViewById(R.id.image);
        shareButton = findViewById(R.id.shareButton);

        // Retrieve the Bitmap data from the intent
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");

        if (byteArray != null) {
            // Convert byte array back to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            // Set the received Bitmap to the ImageView for display
            imageshare.setImageBitmap(bitmap);
        } else {
            // Handle the case where Bitmap data is not received
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
        }
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the drawable from ImageView
                Drawable drawable = imageshare.getDrawable();

                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    // Share the image
                    shareImage(bitmap);
                } else {
                    // Handle other drawable types or conditions
                    Toast.makeText(share_activity.this, "Image not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void shareImage(Bitmap bitmap) {
        Uri uri = getImageUri(bitmap);

        if (uri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
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
