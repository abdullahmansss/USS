package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView username,email,phone,dep,year,sec,address;
    CircleImageView profileimage;
    FloatingActionButton more_btn,logout_btn,edituser_btn;
    String em,na,po,ad,url,de,ye,se;
    ProgressDialog progressDialog;
    boolean rotate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.user_name_txt);
        email = (TextView) findViewById(R.id.email_txt);
        phone = (TextView) findViewById(R.id.phone_txt);
        dep = (TextView) findViewById(R.id.dep_txt);
        year = (TextView) findViewById(R.id.year_txt);
        sec = (TextView) findViewById(R.id.sec_txt);
        address = (TextView) findViewById(R.id.address_txt);
        profileimage = (CircleImageView) findViewById(R.id.profile_image);
        more_btn = (FloatingActionButton) findViewById(R.id.more_btn);
        logout_btn = (FloatingActionButton) findViewById(R.id.logout_btn);
        edituser_btn = (FloatingActionButton) findViewById(R.id.edit_user_btn);
        initShowOut(logout_btn);
        initShowOut(edituser_btn);

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        final String userId = user.getUid();

        mDatabase.child("allstudents").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        UserData userData = dataSnapshot.getValue(UserData.class);

                        em = userData.email;
                        na = userData.name;
                        po = userData.phone;
                        ad = userData.address;
                        url = userData.imageURL;
                        de = userData.depart;
                        ye = userData.year;
                        se = userData.section;

                        username.setText(na);
                        email.setText(em);
                        phone.setText(po);
                        address.setText(ad);
                        dep.setText(de);
                        year.setText(ye);
                        sec.setText(se);

                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.user3)
                                .into(profileimage);

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rotate = rotateFab(v, !rotate);
                if (rotate) {
                    showIn(logout_btn);
                    showIn(edituser_btn);
                } else {
                    showOut(logout_btn);
                    showOut(edituser_btn);
                }
            }
        });

        /*loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        e = loginPreferences.getString("email", "");
        n = loginPreferences.getString("name", "");
        p = loginPreferences.getString("phone", "");
        d = loginPreferences.getString("dep", "");
        y = loginPreferences.getString("year", "");
        s = loginPreferences.getString("sec", "");*/

        /*for (UserInfo profile : user.getProviderData())
        {
            // Id of the provider (ex: google.com)
            String providerId = profile.getProviderId();

            // UID specific to the provider
            String uid = profile.getUid();

            // Name, email address, and profile photo Url
            String name = profile.getDisplayName();
            String email = profile.getEmail();
            Uri photoUrl = profile.getPhotoUrl();
        };

        String name = user.getDisplayName();
        String email1 = user.getEmail();
        String pho = user.getPhoneNumber();*/

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent n = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(n);
            }
        });

        edituser_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "edit user button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void initShowOut(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }

    public static void showOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 90f : 0f);
        return rotate;
    }
}
