package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ForumActivity extends AppCompatActivity {

    Dialog popAddPost;
    TextView addDisc;
    ImageView addImg;
    Button btnAddImg;
    Button btnPost;
    EditText editDesc;
    EditText editTitle;
    private static final int PReqCode = 2;
    private static final int REQUESTCODE = 2;
    private Uri pickedImgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        popAddPost = new Dialog(this);
        inipopup();
        setUpPopImageClick();

        addDisc = (TextView) findViewById(R.id.btnStartDisc);
        addDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
            }
        });

    }

    private void setUpPopImageClick() {
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when button clicked, open gallery
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(ForumActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ForumActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(ForumActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(ForumActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGalery();
    }

    private void openGalery(){
        //open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    //when user picked an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data!= null){
            //user picked image, need to save reference to a Uri variable
            pickedImgUri = data.getData();
            addImg.setImageURI(pickedImgUri);
        }
    }

    private void inipopup() {
        popAddPost.setContentView(R.layout.popup_forum);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;

        btnAddImg = popAddPost.findViewById(R.id.btnAddImg);
        btnPost = popAddPost.findViewById(R.id.btnPost);
        addImg = popAddPost.findViewById(R.id.imgView_add);
        editDesc = popAddPost.findViewById(R.id.editTxtDesc);
        editTitle = popAddPost.findViewById(R.id.editTxtTitle);

        //add post

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test all input fields (title and description) and post image

                if(!editTitle.getText().toString().isEmpty()
                    && !editDesc.getText().toString().isEmpty()
                    && pickedImgUri != null){

                    //no empty or null value
                    //TODO create post object and add to firebase
                    //need to upload post image
                    //access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("forum_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    //create post object
                                    Post post = new Post(editTitle.getText().toString(),
                                            editDesc.getText().toString(),
                                            imageDownloadLink,
                                            "UserID", "drawable/profile.png");

                                    //add post to firebase
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //fail upload picture

                                    showMessage(e.getMessage());
                                }
                            });
                        }
                    });

                }
                else{
                    showMessage("Please fill in all details and add an image.");
                }
            }
        });

    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Forum").push();

        //get post unique id and update post key
        String key = myRef.getKey();
        post.setPostKey(key);

        //add post data to firebase
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post added successfully");
                popAddPost.dismiss();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(ForumActivity.this,message,Toast.LENGTH_LONG).show();
    }
}