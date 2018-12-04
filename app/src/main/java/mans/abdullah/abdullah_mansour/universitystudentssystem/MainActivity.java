package mans.abdullah.abdullah_mansour.universitystudentssystem;

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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {
    /*ViewPager viewPager;
    TabLayout indicator;
    List<Integer> image;*/
    LinearLayout my_profile, timeline, dep;
    CircleImageView pp;

    ProgressDialog progressDialog;
    Snackbar snackbaroff;
    Snackbar snackbaron;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_profile = (LinearLayout) findViewById(R.id.my_profile_btn);
        timeline = (LinearLayout) findViewById(R.id.timeline_btn);
        dep = (LinearLayout) findViewById(R.id.dep_btn);
        pp = findViewById(R.id.profile_image_main);

        BroadcastReceiver br = new MyBroadcast();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);
        this.registerReceiver(br, filter);

        snackbaroff = Snackbar.make(findViewById(R.id.my_main_layout), "No internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbaron = Snackbar.make(findViewById(R.id.my_main_layout), "Internet connected", Snackbar.LENGTH_SHORT);
        progressDialog = new ProgressDialog(MainActivity.this);

        /*if (isConnected)
        {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

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

                            String url = userData.imageURL;

                            Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.adduser)
                                    .into(pp);

                            //progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {
                            Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                        }
                    });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }*/

        dep.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), DepartmentsActivity.class);
                startActivity(n,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        timeline.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(n);
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                /*FirebaseAuth.getInstance().signOut();
                Intent n = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(n);*/
                Intent n = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(n,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        /*viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        image = new ArrayList<>();
        image.add(R.drawable.u1);
        image.add(R.drawable.u2);
        image.add(R.drawable.u3);

        viewPager.setAdapter(new SliderAdapter(this, image));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);*/
    }

    @Override
    public void onBackPressed()
    {

    }

    public class MyBroadcast extends BroadcastReceiver
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected)
            {
                /*progressDialog.setMessage("Please Wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);*/

                snackbaroff.dismiss();

                snackbaron.getView().setBackground(getResources().getDrawable(R.drawable.snackbaron));
                snackbaron.show();

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

                                String url = userData.imageURL;

                                Picasso.get()
                                        .load(url)
                                        .placeholder(R.drawable.adduser)
                                        .into(pp);

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
            } else
            {
                //Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                snackbaroff.setAction("Retry", new View.OnClickListener()
                        {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view)
                    {
                        Intent n = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(n,
                                ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                    }
                });
                snackbaroff.setActionTextColor(getResources().getColor(android.R.color.white));
                snackbaroff.getView().setBackground(getResources().getDrawable(R.drawable.snackbaroff));
                snackbaroff.show();
                snackbaron.dismiss();
                progressDialog.dismiss();
            }
        }
    }

    /*private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < image.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }*/
}
