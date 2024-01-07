package com.example.madtest1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

//for adding forum posts
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder>{

    View view;
    Context context;
    ArrayList<Post> arrayList = new ArrayList<>();
    RequestManager glide;
    Dialog addComm;
    TextView textComment;
    Button btnAddComment;
    EditText editComment;
    private OnCommentButtonClickListener listener;
    private boolean isLiked = false;

    public ForumAdapter(Context context, ArrayList<Post> arrayList, RequestManager glide) {
        this.context = context;
        this.arrayList = arrayList;
        this.glide = glide;
        addComm = new Dialog(context);
    }

    public void updateData(ArrayList<Post> newData) {
        this.arrayList = newData;
        notifyDataSetChanged();
    }

    public interface OnCommentButtonClickListener {
        void onCommentButtonClick(int position);
    }

    private OnCommentButtonClickListener commentButtonClickListener;

    public void setOnCommentButtonClickListener(OnCommentButtonClickListener listener) {
        this.commentButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_feed,parent,false);
        return new ViewHolder(view,commentButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Post post = arrayList.get(position);
        holder.username.setText(post.getUserID());
        holder.title.setText(post.getTitle());
        holder.desc.setText(post.getDescription());
        if(post.getPicture()==null){
            holder.postPic.setVisibility(View.GONE);
        }else{
            holder.postPic.setVisibility(View.VISIBLE);
            glide.load(post.getPicture()).into(holder.postPic);
        }
        // Display comments using nested RecyclerView
        //if(!arrayList.get(position).getCommentList().isEmpty()){
        ArrayList<Comment> comments = arrayList.get(position).getCommentList();
        CommentAdapter commentAdapter = new CommentAdapter(context, comments, glide);
        holder.rvComments.setAdapter(commentAdapter);
        //}
    }

    private String addComment() {
        addComm.setContentView(R.layout.popup_goal);
        addComm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addComm.getWindow().getAttributes().gravity = Gravity.CENTER;

        textComment = addComm.findViewById(R.id.txtAddGoal);
        btnAddComment = addComm.findViewById(R.id.btnGoal);
        editComment = addComm.findViewById(R.id.editGoal);
        textComment.setText("Add Comment");
        editComment.setHint("Enter comment here");
        String text = editComment.toString();

        //add comment
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComm.dismiss();
            }
        });
        return text;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView title;
        TextView desc;
        ImageView profilePic;
        ImageView postPic;
        RecyclerView rvComments;
        TextView btnComment;
        ImageView like;

        public ViewHolder(@NonNull View itemView, OnCommentButtonClickListener listener) {
            super(itemView);
            profilePic = (ImageView)itemView.findViewById(R.id.imgView_profile);
            postPic = (ImageView) itemView.findViewById(R.id.imgView_ForumPic);
            username = (TextView) itemView.findViewById(R.id.TxtUsername);
            title = (TextView) itemView.findViewById(R.id.TxtTopic);
            desc = (TextView) itemView.findViewById(R.id.TxtDesc);
            rvComments = (RecyclerView) itemView.findViewById(R.id.feedRecyclerView);
            btnComment = (TextView)itemView.findViewById(R.id.BtnComments);
            like = (ImageView)itemView.findViewById(R.id.imgStar);

            //handle comment
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onCommentButtonClick(getAdapterPosition());
                    }
                }
            });

            //change image when like
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isLiked = !isLiked;

                    // Load the appropriate image based on the like state
                    int imageResource = isLiked ? R.drawable.filled_heart : R.drawable.unfilled_heart;
                    glide.load(imageResource).into(like);
                }
            });

            // Set a LinearLayoutManager for the nested RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            rvComments.setLayoutManager(layoutManager);
        }
    }

}