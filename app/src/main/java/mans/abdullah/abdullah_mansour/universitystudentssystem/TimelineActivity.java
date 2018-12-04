package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.Post;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.UserData;
import mans.abdullah.abdullah_mansour.universitystudentssystem.PostsFragments.*;

public class TimelineActivity extends AppCompatActivity
{
    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;

    int PICK_IMAGE_REQUEST = 80;

    Uri photoPath;

    CircleImageView my_image;
    ImageView post_photo;

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String selected_userimageURL,selected_username;
    String selected_postimaeURL = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        viewPager = findViewById(R.id.posts_viewpager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            private final Fragment[] mFragments = new Fragment[]
                    {
                            new RecentPostsFragment(),
                            new MyPostsFragment(),
                    };
            private final String[] mFragmentNames = new String[]
                    {
                            "RECENT POSTS",
                            "MY POSTS",
                    };

            @Override
            public Fragment getItem(int position)
            {
                return mFragments[position];
            }
            @Override
            public int getCount()
            {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position)
            {
                return mFragmentNames[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        findViewById(R.id.new_post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showCustomDialog();
            }
        });
    }

    private void showCustomDialog()
    {
        final Dialog dialog = new Dialog(TimelineActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.newpost_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button post = (Button) dialog.findViewById(R.id.post_btn);
        final Button back = (Button) dialog.findViewById(R.id.back_btn);

        final EditText post_filed = (EditText) dialog.findViewById(R.id.post_field);

        my_image = dialog.findViewById(R.id.your_image);

        post_photo = dialog.findViewById(R.id.post_image);

        final LinearLayout add_post_photo = dialog.findViewById(R.id.add_post_photo_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        final String userId = user.getUid();

        mDatabase.child("allstudents").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        UserData userData = dataSnapshot.getValue(UserData.class);

                        selected_userimageURL = userData.imageURL;
                        selected_username = userData.getName();

                        Picasso.get()
                                .load(selected_userimageURL)
                                .placeholder(R.drawable.adduser)
                                .error(R.drawable.adduser)
                                .into(my_image);

                                /*progressDialog.dismiss();
                                snackbar.dismiss();*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();
                    }
                });

        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String post_content = post_filed.getText().toString();

                if (post_content.length() == 0 && selected_postimaeURL.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Share an article, photo or idea ...", Toast.LENGTH_SHORT).show();
                } else
                    {
                        uploadImage(selected_username, selected_userimageURL, post_content);

                        dialog.dismiss();
                    }
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        add_post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                chooseImage();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            photoPath = data.getData();
            // Get a reference to store file at chat_photos/<FILENAME>

            post_photo.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(photoPath)
                    .placeholder(R.drawable.user3)
                    .into(post_photo);
        }
    }

    private void writeNewUser(String username, String userimageurl, String postcontenttext , String postimageurl)
    {
        FirebaseUser user = mAuth.getCurrentUser();

        Post post = new Post(user.getUid(), username, userimageurl, postcontenttext,postimageurl);

        String key = databaseReference.child("allposts").push().getKey();

        databaseReference.child("allposts").child(key).setValue(post);
        databaseReference.child("usersposts").child(user.getUid()).child(key).setValue(post);
    }

    private void uploadImage(final String username,final String userimageURL, final String postcontent)
    {
        progressDialog = new ProgressDialog(TimelineActivity.this);
        progressDialog.setMessage("Posting ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (photoPath != null)
        {
            UploadTask uploadTask;

            final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

            uploadTask = ref.putFile(photoPath);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();

                    //Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    selected_postimaeURL = downloadUri.toString();

                    writeNewUser(username, userimageURL, postcontent, selected_postimaeURL);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    // Handle unsuccessful uploads
                    Toast.makeText(getApplicationContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            {
                selected_postimaeURL = "";
                writeNewUser(username, userimageURL, postcontent, selected_postimaeURL);

                progressDialog.dismiss();
            }
    }
}
