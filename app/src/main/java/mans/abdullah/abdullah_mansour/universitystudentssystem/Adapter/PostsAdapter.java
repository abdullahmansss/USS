package mans.abdullah.abdullah_mansour.universitystudentssystem.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Adapter.PostsAdapter.PostsViewHolder;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.Post;
import mans.abdullah.abdullah_mansour.universitystudentssystem.R;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.Post.*;

public class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder>
{
    Context context;
    List <Post> postList;
    onButtonClick onButtonClick;

    public PostsAdapter(Context context, List<Post> postList)
    {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);

        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position)
    {
        Post post = postList.get(position);

        holder.user_post_name.setText(post.getUsername());
        holder.text_post_content.setText(post.getPostcontent());

        Picasso.get()
                .load(post.getUserimageurl())
                .placeholder(R.drawable.adduser)
                .error(R.drawable.adduser)
                .into(holder.user_post_image);

        Picasso.get()
                .load(post.getPostimageurl())
                .placeholder(R.drawable.adduser)
                .error(R.drawable.adduser)
                .into(holder.image_post_content);
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView user_post_image;
        TextView user_post_name,text_post_content;
        ImageView image_post_content;
        Button like_post,comment_post;

        public PostsViewHolder(View itemView)
        {
            super(itemView);

            user_post_image = itemView.findViewById(R.id.user_post_image);
            user_post_name = itemView.findViewById(R.id.user_post_name);
            text_post_content = itemView.findViewById(R.id.text_post_content);
            image_post_content = itemView.findViewById(R.id.image_post_content);
            like_post = itemView.findViewById(R.id.like_post_btn);
            comment_post = itemView.findViewById(R.id.comment_post_btn);

            like_post.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (onButtonClick != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            onButtonClick.onLike(position);
                        }
                    }
                }
            });

            comment_post.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (onButtonClick != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            onButtonClick.onComment(position);
                        }
                    }
                }
            });
        }
    }

    public interface onButtonClick
    {
        void onLike(int position);
        void onComment(int position);
    }
    public void setOnButtonClick (onButtonClick onButtonClick)
    {
        this.onButtonClick = onButtonClick;
    }
}
