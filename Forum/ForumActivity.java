package com.example.foodcarboncalculator;

import static android.content.ContentValues.TAG;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForumActivity extends AppCompatActivity {

    Dialog popAddPost;
    Dialog addComm;
    TextView addDisc;
    ImageView addImg;
    Button btnAddImg;
    Button btnPost;
    RecyclerView rvForum;
    EditText editDesc;
    EditText editTitle;
    TextView textComment;
    Button btnAddComment;
    EditText editComment;
    private static final int PReqCode = 2;
    private static final int REQUESTCODE = 2;
    private Uri pickedImgUri = null;
    ArrayList<Comment> commentList;
    ArrayList<String> comm;
    ArrayList<Post> postArrayList;
    ForumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        FirebaseApp.initializeApp(this);

        commentList = new ArrayList<>();
        addComm = new Dialog(this);
        postArrayList = new ArrayList<Post>();
        postArrayList.clear();
        getForum();
        rvForum = findViewById(R.id.rvForum);
        RequestManager glide = Glide.with(this);
        adapter = new ForumAdapter(this,postArrayList, glide);
        setRVForum();
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

    //display discussions in recycler view
    private void setRVForum() {
        rvForum.setHasFixedSize(true);
        rvForum.setLayoutManager(new LinearLayoutManager(this));
        rvForum.setAdapter(adapter);
        adapter.setOnCommentButtonClickListener(new ForumAdapter.OnCommentButtonClickListener() {
            @Override
            public void onCommentButtonClick(int position) {
                addComment(position);
            }
        });
    }

    private void addComment(int position) {
        addComm.setContentView(R.layout.popup_goal);
        addComm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addComm.getWindow().getAttributes().gravity = Gravity.CENTER;
        addComm.show();

        textComment = addComm.findViewById(R.id.txtAddGoal);
        btnAddComment = addComm.findViewById(R.id.btnGoal);
        editComment = addComm.findViewById(R.id.editGoal);
        textComment.setText("Add Comment");
        editComment.setHint("Enter comment here");

        //add comment
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ForumActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                // Get the user's UID from Firebase Authentication
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Get a reference to the Firestore collection "users"
                FirebaseFirestore userDb = FirebaseFirestore.getInstance();
                DocumentReference userRef = userDb.collection("users").document(userId);

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String userName = task.getResult().get("name").toString();
                        String text = editComment.getText().toString();
                        Comment comment = new Comment(text, userName);
                        postArrayList.get(position).setCommentList(comment);

                        //Update firestore with new comment
                        addCommentToFirestore(postArrayList.get(position).getPostId(), comment);
                        addComm.dismiss();
                    }
                });

            }
        });
    }

    //to retrieve forum data from database
    private void getForum() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the forum
        CollectionReference forumCollection = db.collection("forum").document("posts").collection("user_forum");

        // Query to get all documents in the forum
        forumCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Iterate through the documents and convert them to Goal objects
                    // Inside the loop where you're constructing Post objects
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String postId = document.getId(); // Get the post ID
                        // Other fields
                        String userID = document.getString("userID");
                        String title = document.getString("title");
                        String desc = document.getString("description");
                        String profPic = document.getString("userPhoto");
                        String postPic = document.getString("picture");

                        // Fetch comments for this post
                        fetchCommentsForPost(postId, userID, title, desc, profPic, postPic);
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
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
                //no empty or null value
                //TODO create post object and add to firebase
                //need to upload post image
                //access firebase storage
                if(!editTitle.getText().toString().isEmpty()
                    && !editDesc.getText().toString().isEmpty()){
                    // Get the user's UID from Firebase Authentication
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Get a reference to the Firestore collection "users"
                    FirebaseFirestore userDb = FirebaseFirestore.getInstance();
                    DocumentReference userRef = userDb.collection("users").document(userId);

                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            String userName = task.getResult().get("name").toString();
                            saveForumToFirestore(userName);
                        }
                    });
                    setRVForum();

                   /* String imageDownloadLink = pickedImgUri.toString();
                    //create post object
                    String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    Post post = new Post(editTitle.getText().toString(),
                            editDesc.getText().toString(),
                            imageDownloadLink,
                            userName, "drawable/profile.png", commentList); //change later to get userId and profile pic if have

                    //add post to firebase
                    addPost(post);*/

                    /*
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
                                            "UserID", "drawable/profile.png"); //change later to get userId and profile pic if have

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
                    });*/

                }
                else{
                    showMessage("Please fill in all details and add an image.");
                }
            }
        });

    }

    //not used
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
        myRef.setValue(post).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error adding post: " + e.getMessage());
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(ForumActivity.this,message,Toast.LENGTH_LONG).show();
    }



    private void saveForumToFirestore(String userName) {
        /*String userName;
        // Get the user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the Firestore collection "users"
        FirebaseFirestore userDb = FirebaseFirestore.getInstance();
        DocumentReference userRef = userDb.collection("users").document(userId);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String userName = task.getResult().get("name").toString();
            }
        });*/

        // Get post image link
        String imageDownloadLink;
        if(pickedImgUri != null){
            imageDownloadLink = pickedImgUri.toString();
        }else{
            imageDownloadLink = "";
        }

        //create post object
        //String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();

        // Create a Map to represent the post data
        Map<String, Object> postData = new HashMap<>();
        postData.put("userID", userName);
        postData.put("title", title);
        postData.put("description", desc);
        postData.put("picture", imageDownloadLink);
        postData.put("userPhoto", "profile");

        // Get a reference to the Firestore collection "posts" under the "forum" collection
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postsCollection = db.collection("forum").document("posts").collection("user_forum");

        // Add a new document (post) to the "posts" collection
        postsCollection.add(postData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Successfully added post
                        String userId = documentReference.getId();
                        Toast.makeText(ForumActivity.this, "Post added successfully.", Toast.LENGTH_SHORT).show();
                        Post post = new Post(userId, title,desc,
                                imageDownloadLink,
                                userName, "profile");
                        postArrayList.add(post);
                        adapter.updateData(postArrayList);
                        popAddPost.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add post
                        Toast.makeText(ForumActivity.this, "Error adding post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void fetchCommentsForPost(String postId, String userID, String title, String desc, String profPic, String postPic) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference commentCollection = db.collection("forum")
                .document("posts")
                .collection("user_forum")
                .document(postId) // Use the post ID to reference the comments collection
                .collection("comments");

        commentCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Comment> comments = new ArrayList<>();
                    Comment comment = null;
                    // Create the Post object with comments
                    Post getPost = new Post(postId, title, desc, postPic, userID, profPic);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String commentVal = document.getString("comment");
                        String userIdVal = document.getString("userID");
                        comment = new Comment(commentVal, userIdVal);
                        comments.add(comment);
                        if (!comments.isEmpty()){
                            getPost.setCommentList(comment);
                        }
                    }

                    postArrayList.add(getPost);

                    // Update the RecyclerView adapter with the retrieved goals
                    adapter.updateData(postArrayList);
                } else {
                    Log.w(TAG, "Error getting comments.", task.getException());
                }
            }
        });
    }

    private void addCommentToFirestore(String postId, Comment comment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Reference to the comments collection under the specific post
        CollectionReference commentsCollection = db.collection("forum")
                .document("posts")
                .collection("user_forum")
                .document(postId)
                .collection("comments");

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("comment", comment.getCommentText());
        commentData.put("userID", comment.getUserId());

        // Add the comment to Firestore
        commentsCollection.add(commentData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Comment added to Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding comment to Firestore", e);
                    }
                });
    }


}