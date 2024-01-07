package com.example.madtest1;//package com.example.madtest1;


import static android.content.ContentValues.TAG;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForumDesign extends Fragment {

    Dialog popAddPost, addComm;
    TextView addDisc;
    ImageView addImg;
    Button btnAddImg, btnPost;
    RecyclerView rvForum;
    EditText editDesc, editTitle, editComment;
    TextView textComment;
    Button btnAddComment;
    private static final int PReqCode = 2;
    private static final int REQUESTCODE = 2;
    private Uri pickedImgUri = null;
    ArrayList<Comment> commentList;
    ArrayList<Post> postArrayList;
    ForumAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum_design, container, false);
        FirebaseApp.initializeApp(requireActivity());

        initViews(view);
        initRecyclerView(view);
        initDialogs();
        fetchForumData();

        return view;
    }

    private void initViews(View view) {
        addDisc = view.findViewById(R.id.btnStartDisc);
        addDisc.setOnClickListener(v -> popAddPost.show());
    }

    private void initRecyclerView(View view) {
        rvForum = view.findViewById(R.id.rvForum);
        rvForum.setHasFixedSize(true);
        rvForum.setLayoutManager(new LinearLayoutManager(getContext()));
        postArrayList = new ArrayList<>();
        adapter = new ForumAdapter(getContext(), postArrayList, Glide.with(this));
        rvForum.setAdapter(adapter);
        adapter.setOnCommentButtonClickListener(this::addComment);
    }

    private void initDialogs() {
        popAddPost = new Dialog(requireActivity());
        initPostPopup();
        addComm = new Dialog(requireActivity());
        initCommentPopup();
    }

    private void initPostPopup() {
        popAddPost.setContentView(R.layout.popup_forum);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;

        btnAddImg = popAddPost.findViewById(R.id.btnAddImg);
        btnPost = popAddPost.findViewById(R.id.btnPost);
        addImg = popAddPost.findViewById(R.id.imgView_add);
        editDesc = popAddPost.findViewById(R.id.editTxtDesc);
        editTitle = popAddPost.findViewById(R.id.editTxtTitle);

        btnAddImg.setOnClickListener(v -> checkAndRequestForPermission());
        btnPost.setOnClickListener(v -> handlePostSubmission());
    }

    private void initCommentPopup() {
        addComm.setContentView(R.layout.popup_goal);
        addComm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addComm.getWindow().getAttributes().gravity = Gravity.CENTER;

        textComment = addComm.findViewById(R.id.txtAddGoal);
        btnAddComment = addComm.findViewById(R.id.btnGoal);
        editComment = addComm.findViewById(R.id.editGoal);
        textComment.setText("Add Comment");
        editComment.setHint("Enter comment here");
    }

    private void addComment(int position) {
        addComm.show();
        btnAddComment.setOnClickListener(view -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore userDb = FirebaseFirestore.getInstance();
            DocumentReference userRef = userDb.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userName = task.getResult().get("name").toString();
                    String text = editComment.getText().toString();
                    Comment comment = new Comment(text, userName);
                    postArrayList.get(position).setCommentList(comment);
                    addCommentToFirestore(postArrayList.get(position).getPostId(), comment);
                    addComm.dismiss();
                }
            });
        });
    }

    private void fetchForumData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference forumCollection = db.collection("forum").document("posts").collection("user_forum");
        forumCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String postId = document.getId();
                    String userID = document.getString("userID");
                    String title = document.getString("title");
                    String desc = document.getString("description");
                    String profPic = document.getString("userPhoto");
                    String postPic = document.getString("picture");
                    fetchCommentsForPost(postId, userID, title, desc, profPic, postPic);
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private void fetchCommentsForPost(String postId, String userID, String title, String desc, String profPic, String postPic) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentCollection = db.collection("forum")
                .document("posts")
                .collection("user_forum")
                .document(postId)
                .collection("comments");

        commentCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Comment> comments = new ArrayList<>();
                Post getPost = new Post(postId, title, desc, postPic, userID, profPic);
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String commentVal = document.getString("comment");
                    String userIdVal = document.getString("userID");
                    Comment comment = new Comment(commentVal, userIdVal);
                    comments.add(comment);
                    if (!comments.isEmpty()){
                        getPost.setCommentList(comment);
                    }
                }
                postArrayList.add(getPost);
                adapter.updateData(postArrayList);
            } else {
                Log.w(TAG, "Error getting comments.", task.getException());
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE && data != null) {
            pickedImgUri = data.getData();
            addImg.setImageURI(pickedImgUri);
        }
    }

    private void handlePostSubmission() {
        if (!editTitle.getText().toString().isEmpty() && !editDesc.getText().toString().isEmpty()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore userDb = FirebaseFirestore.getInstance();
            DocumentReference userRef = userDb.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userName = task.getResult().get("name").toString();
                    saveForumToFirestore(userName);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please fill in all details and add an image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveForumToFirestore(String userName) {
        String imageDownloadLink = (pickedImgUri != null) ? pickedImgUri.toString() : "";
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();

        Map<String, Object> postData = new HashMap<>();
        postData.put("userID", userName);
        postData.put("title", title);
        postData.put("description", desc);
        postData.put("picture", imageDownloadLink);
        postData.put("userPhoto", "profile");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postsCollection = db.collection("forum").document("posts").collection("user_forum");
        postsCollection.add(postData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Post added successfully.", Toast.LENGTH_SHORT).show();
                    Post post = new Post(documentReference.getId(), title, desc, imageDownloadLink, userName, "profile");
                    postArrayList.add(post);
                    adapter.updateData(postArrayList);
                    popAddPost.dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error adding post: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void addCommentToFirestore(String postId, Comment comment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentsCollection = db.collection("forum")
                .document("posts")
                .collection("user_forum")
                .document(postId)
                .collection("comments");

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("comment", comment.getCommentText());
        commentData.put("userID", comment.getUserId());

        commentsCollection.add(commentData)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "Comment added to Firestore"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding comment to Firestore", e));
    }
}

//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestManager;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ForumDesign extends Fragment {
//
//    private Dialog popAddPost;
//    private Dialog addComm;
//    private TextView addDisc;
//    private ImageView addImg;
//    private Button btnAddImg;
//    private Button btnPost;
//    private RecyclerView rvForum;
//    private EditText editDesc;
//    private EditText editTitle;
//    private TextView textComment;
//    private Button btnAddComment;
//    private EditText editComment;
//    private static final int PReqCode = 2;
//    private static final int REQUESTCODE = 2;
//    private Uri pickedImgUri = null;
//    private ArrayList<Comment> commentList;
//    private ArrayList<String> comm;
//    private ArrayList<Post> postArrayList;
//    private ForumAdapter adapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.forum_design, container, false);
//
//        FirebaseApp.initializeApp(requireContext());
//
//        commentList = new ArrayList<>();
//        addComm = new Dialog(requireContext());
//        postArrayList = new ArrayList<Post>();
//        postArrayList.clear();
//        getForum();
//        rvForum = view.findViewById(R.id.rvForum);
//        RequestManager glide = Glide.with(this);
//        adapter = new ForumAdapter(requireContext(), postArrayList, glide);
//        setRVForum(view);
//        popAddPost = new Dialog(requireContext());
//        inipopup(view);
//        setUpPopImageClick();
//
//        addDisc = view.findViewById(R.id.btnStartDisc);
//        addDisc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popAddPost.show();
//            }
//        });
//
//        return view;
//    }
//
//    // Display discussions in recycler view
//    private void setRVForum(View view) {
//        rvForum.setHasFixedSize(true);
//        rvForum.setLayoutManager(new LinearLayoutManager(requireContext()));
//        rvForum.setAdapter(adapter);
//        adapter.setOnCommentButtonClickListener(new ForumAdapter.OnCommentButtonClickListener() {
//            @Override
//            public void onCommentButtonClick(int position) {
//                addComment(position);
//            }
//        });
//    }
//
//    // Other methods (addComment, getForum, setUpPopImageClick, etc.) go here...
//
//    private void inipopup(View view) {
//        popAddPost.setContentView(R.layout.popup_forum);
//        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;
//
//        btnAddImg = popAddPost.findViewById(R.id.btnAddImg);
//        btnPost = popAddPost.findViewById(R.id.btnPost);
//        addImg = popAddPost.findViewById(R.id.imgView_add);
//        editDesc = popAddPost.findViewById(R.id.editTxtDesc);
//        editTitle = popAddPost.findViewById(R.id.editTxtTitle);
//
//        // Add post button click listener
//        btnPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!editTitle.getText().toString().isEmpty() && !editDesc.getText().toString().isEmpty()) {
//                    // Get the user's UID from Firebase Authentication
//                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                    FirebaseFirestore userDb = FirebaseFirestore.getInstance();
//                    DocumentReference userRef = userDb.collection("users").document(userId);
//
//                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            String userName = task.getResult().get("name").toString();
//                            saveForumToFirestore(userName);
//                        }
//                    });
//                    setRVForum(view);
//                } else {
//                    showMessage("Please fill in all details and add an image.");
//                }
//            }
//        });
//    }
//
//    private void addComment(int position) {
//        addComm.setContentView(R.layout.popup_goal);
//        addComm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        addComm.getWindow().getAttributes().gravity = Gravity.CENTER;
//        addComm.show();
//
//        textComment = addComm.findViewById(R.id.txtAddGoal);
//        btnAddComment = addComm.findViewById(R.id.btnGoal);
//        editComment = addComm.findViewById(R.id.editGoal);
//        textComment.setText("Add Comment");
//        editComment.setHint("Enter comment here");
//
//        // Add comment button click listener
//        btnAddComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(requireContext(), "Comment added", Toast.LENGTH_SHORT).show();
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                FirebaseFirestore userDb = FirebaseFirestore.getInstance();
//                DocumentReference userRef = userDb.collection("users").document(userId);
//
//                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        String userName = task.getResult().get("name").toString();
//                        String text = editComment.getText().toString();
//                        Comment comment = new Comment(text, userName);
//                        postArrayList.get(position).setCommentList(comment);
//
//                        addCommentToFirestore(postArrayList.get(position).getPostId(), comment);
//                        addComm.dismiss();
//                    }
//                });
//            }
//        });
//    }
//
//    private void saveForumToFirestore(String userName) {
//        String imageDownloadLink;
//        if (pickedImgUri != null) {
//            imageDownloadLink = pickedImgUri.toString();
//        } else {
//            imageDownloadLink = "";
//        }
//
//        String title = editTitle.getText().toString().trim();
//        String desc = editDesc.getText().toString().trim();
//
//        Map<String, Object> postData = new HashMap<>();
//        postData.put("userID", userName);
//        postData.put("title", title);
//        postData.put("description", desc);
//        postData.put("picture", imageDownloadLink);
//        postData.put("userPhoto", "profile");
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference postsCollection = db.collection("forum").document("posts").collection("user_forum");
//
//        postsCollection.add(postData)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        String userId = documentReference.getId();
//                        Toast.makeText(requireContext(), "Post added successfully.", Toast.LENGTH_SHORT).show();
//                        Post post = new Post(userId, title, desc, imageDownloadLink, userName, "profile");
//                        postArrayList.add(post);
//                        adapter.updateData(postArrayList);
//                        popAddPost.dismiss();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(requireContext(), "Error adding post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    // Other methods (getForum, setUpPopImageClick, etc.) go here...
//
//    private void showMessage(String message) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
//    }
//}