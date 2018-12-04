package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.UserData;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.UserData.*;

public class ProfileActivity extends AppCompatActivity {
    TextView username,email,phone,dep,year,sec,address;
    CircleImageView profileimage;
    FloatingActionButton more_btn,logout_btn,edituser_btn,ref_btn;
    String em,na,po,ad,url,de,ye,se;
    ProgressDialog progressDialog;
    boolean rotate = false;

    Snackbar snackbaroff;
    Snackbar snackbaron;

    @SuppressLint("CutPasteId")
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
        ref_btn = (FloatingActionButton) findViewById(R.id.refresh_btn);
        initShowOut(logout_btn);
        initShowOut(edituser_btn);
        initShowOut(ref_btn);

        BroadcastReceiver br = new MyBroadcast();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);
        this.registerReceiver(br, filter);

        snackbaroff = Snackbar.make(findViewById(R.id.profile_layout), "No internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbaron = Snackbar.make(findViewById(R.id.profile_layout), "Internet connected", Snackbar.LENGTH_SHORT);
        progressDialog = new ProgressDialog(ProfileActivity.this);

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rotate = rotateFab(v, !rotate);
                if (rotate) {
                    showIn(logout_btn);
                    showIn(edituser_btn);
                    showIn(ref_btn);
                } else {
                    showOut(logout_btn);
                    showOut(edituser_btn);
                    showOut(ref_btn);
                }
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ConnectivityManager cm =
                        (ConnectivityManager)ProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent n = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(n);
                }
                else
                    {
                        Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
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

        ref_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)ProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected)
                {
                    Intent n = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(n,
                            ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this).toBundle());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class MyBroadcast extends BroadcastReceiver
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ConnectivityManager cm =
                    (ConnectivityManager)ProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected)
            {
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setMessage("Please Wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                snackbaroff.dismiss();

                snackbaron.getView().setBackground(getResources().getDrawable(R.drawable.snackbaron));
                snackbaron.show();

                more_btn.setEnabled(true);

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
            }
            else
            {
                //Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();

                snackbaroff.setAction("Retry", new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view)
                    {
                        Intent n = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(n,
                                ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this).toBundle());
                    }
                });

                snackbaroff.setActionTextColor(getResources().getColor(android.R.color.white));
                snackbaroff.getView().setBackground(getResources().getDrawable(R.drawable.snackbaroff));
                snackbaroff.show();

                snackbaron.dismiss();

                more_btn.setEnabled(false);
                progressDialog.dismiss();
            }
        }
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        Intent n = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(n,
                ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this).toBundle());
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.refresh:
                super.onCreate(savedInstanceState);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
