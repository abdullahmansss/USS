package mans.abdullah.abdullah_mansour.universitystudentssystem.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.Post;
import mans.abdullah.abdullah_mansour.universitystudentssystem.R;

public class PostViewHolder extends RecyclerView.ViewHolder
{
    public CircleImageView user_post_image;
    public TextView user_post_name,text_post_content;
    public ImageView image_post_content;
    public Button comment_post;
    public Button like_post;

    public PostViewHolder(View itemView)
    {
        super(itemView);

        user_post_image = itemView.findViewById(R.id.user_post_image);
        user_post_name = itemView.findViewById(R.id.user_post_name);
        text_post_content = itemView.findViewById(R.id.text_post_content);
        image_post_content = itemView.findViewById(R.id.image_post_content);
        like_post = itemView.findViewById(R.id.like_post_btn);
        comment_post = itemView.findViewById(R.id.comment_post_btn);
    }

    public void bindToPost(Post post, View.OnClickListener likeClickListener, View.OnClickListener commentClickListner, View.OnClickListener imageClickListner)
    {
        user_post_name.setText(post.getUsername());

        if (post.getPostcontent().length() == 0)
        {
            text_post_content.setVisibility(View.GONE);
        } else
        {
            text_post_content.setText(post.getPostcontent());
        }

        Picasso.get()
                .load(post.getUserimageurl())
                .placeholder(R.drawable.adduser)
                .error(R.drawable.adduser)
                .into(user_post_image);

        if (post.getPostimageurl().length() == 0)
        {
            image_post_content.setVisibility(View.GONE);
        } else
            {
                Picasso.get()
                        .load(post.getPostimageurl())
                        .placeholder(R.drawable.blog)
                        .error(R.drawable.blog)
                        .into(image_post_content);
            }

            like_post.setOnClickListener(likeClickListener);
            comment_post.setOnClickListener(commentClickListner);
            image_post_content.setOnClickListener(imageClickListner);
    }

}
