package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.Post;

public class FullPostImage extends AppCompatActivity
{
    PhotoView full_post_image;

    public static final String EXTRA_POST_KEY = "post_key";
    String mPostKey;

    DatabaseReference mDatabase;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post_image);

        full_post_image = findViewById(R.id.full_post_image);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getpostImage(mPostKey);
    }

    public void getpostImage(String key)
    {
        mDatabase.child("allposts").child(key).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        Post post = dataSnapshot.getValue(Post.class);

                        url = post.postimageurl;

                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.blog)
                                .into(full_post_image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();
                    }
                });
    }
}
