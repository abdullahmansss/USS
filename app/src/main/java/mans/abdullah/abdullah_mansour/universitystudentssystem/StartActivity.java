package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    LinearLayout sign_up_btn,sign_in_btn,contact_us_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sign_up_btn = (LinearLayout) findViewById(R.id.sign_up_btn);
        sign_in_btn = (LinearLayout) findViewById(R.id.sign_in_btn);
        contact_us_btn = (LinearLayout) findViewById(R.id.contact_us_btn);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(n,
                        ActivityOptions.makeSceneTransitionAnimation(StartActivity.this).toBundle());
            }
        });

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(n,
                        ActivityOptions.makeSceneTransitionAnimation(StartActivity.this).toBundle());
            }
        });

        contact_us_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(n,
                        ActivityOptions.makeSceneTransitionAnimation(StartActivity.this).toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}