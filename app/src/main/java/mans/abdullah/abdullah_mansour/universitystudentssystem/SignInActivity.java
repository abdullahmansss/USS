package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    TextInputEditText email,pass;
    FloatingActionButton sign_in_btn;
    TextView forgot;
    CheckBox remember;
    String email_txt,pass_txt;
    Context context = SignInActivity.this;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sign_in_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign in");

        mAuth = FirebaseAuth.getInstance();

        email = (TextInputEditText) findViewById(R.id.email_sign_in_field);
        pass = (TextInputEditText) findViewById(R.id.password_sign_in_field);

        forgot = (TextView) findViewById(R.id.forgot_password_txt);
        remember = (CheckBox) findViewById(R.id.remember_me_checkbox);

        sign_in_btn = (FloatingActionButton) findViewById(R.id.sign_in_in_btn);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin)
        {
            email.setText(loginPreferences.getString("username", ""));
            pass.setText(loginPreferences.getString("password", ""));
            remember.setChecked(true);
        }

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_txt = email.getText().toString().trim();
                pass_txt = pass.getText().toString().trim();

                ConnectivityManager cm =
                        (ConnectivityManager)SignInActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected)
                {
                    if (email_txt.length() == 0 | pass_txt.length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "please enter a valid data", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(), "signed in success", Toast.LENGTH_SHORT).show();
                        progressDialog = new ProgressDialog(SignInActivity.this);
                        progressDialog.setMessage("Signing In ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        SignIn(email_txt,pass_txt);

                        if (remember.isChecked())
                        {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", email_txt);
                            loginPrefsEditor.putString("password", pass_txt);
                            loginPrefsEditor.apply();
                        } else {
                            loginPrefsEditor.clear();
                            loginPrefsEditor.apply();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                email_txt = email.getText().toString().trim();

                if (email_txt.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter email first", Toast.LENGTH_SHORT).show();
                } else
                    {
                        progressDialog = new ProgressDialog(SignInActivity.this);
                        progressDialog.setMessage("Sending Password Reset to Email ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        sentpasswordresetemail(email_txt);
                    }
            }
        });
    }

    public void sentpasswordresetemail(final String email)
    {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "password reset email sent to : " + email, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void UpdateUI()
    {
        Intent n = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(n,
                ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this).toBundle());
    }

    public void SignIn (final String email,final String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI();
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Wrong email or password.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() { }
}
