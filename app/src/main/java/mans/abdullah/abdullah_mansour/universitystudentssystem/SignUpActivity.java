package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText email,pass,user_name,phone_num,code;
    FloatingActionButton sign_up_btn2;
    String user_email,user_password,user_name2,user_phone,phone_code;
    Button send_code_btn;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        email = (TextInputEditText) findViewById(R.id.email_field);
        pass = (TextInputEditText) findViewById(R.id.password_field);
        user_name = (TextInputEditText) findViewById(R.id.name_field);
        phone_num = (TextInputEditText) findViewById(R.id.phone_field);
        code = (TextInputEditText) findViewById(R.id.code_field);

        sign_up_btn2 = (FloatingActionButton) findViewById(R.id.sign_up_btn2);
        send_code_btn = (Button) findViewById(R.id.send_code_btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign up");

        OnVerificationStateChanged();

        send_code_btn.setOnClickListener(
                new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                user_phone = phone_num.getText().toString().trim();

                if (user_phone.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter phone number", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        progressDialog = new ProgressDialog(SignUpActivity.this);
                        progressDialog.setMessage("Sending Code, Please Wait ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        send_code_btn.setEnabled(false);
                        send_code_btn.setBackground(getDrawable(R.drawable.circle1));
                        send_code_btn.setText("re-send code");
                        startPhoneNumberVerification(user_phone);
                    }
            }
        });

        sign_up_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = email.getText().toString().trim();
                user_password = pass.getText().toString().trim();
                user_name2 = user_name.getText().toString().trim();
                user_phone = phone_num.getText().toString().trim();
                phone_code = code.getText().toString().trim();

                //UpdateUI();

                if (user_email.length() == 0
                        | user_password.length() == 0
                        | user_name2.length() == 0
                        | user_phone.length() == 0
                        | phone_code.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter a valid data", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        if
                                (
                                user_name2.contains(".")
                                | user_name2.contains("(")
                                | user_name2.contains(")")
                                | user_name2.contains("*")
                                | user_name2.contains("[")
                                | user_name2.contains("]")
                                )
                        {
                            Toast.makeText(getApplicationContext(), "please enter valid name", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                progressDialog = new ProgressDialog(SignUpActivity.this);
                                progressDialog.setMessage("Verifying Phone Number ...");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();
                                progressDialog.setCancelable(false);

                                signInWithPhoneAuthCredential(SignIn(phone_code));
                            }
                    }
            }
        });

    }

    public void startPhoneNumberVerification(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + phoneNumber,                // Phone number to verify
                60,                              // Timeout duration
                TimeUnit.SECONDS,        // Unit of timeout
                this,                // Activity (for callback binding)
                mCallbacks);                 // OnVerificationStateChangedCallbacks
    }

    public void OnVerificationStateChanged ()
    {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                progressDialog.dismiss();
                send_code_btn.setEnabled(true);
                send_code_btn.setBackground(getDrawable(R.drawable.circle2));
                Toast.makeText(getApplicationContext(), "code can\'t send to : " + user_phone, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressDialog.dismiss();
                send_code_btn.setEnabled(true);
                send_code_btn.setBackground(getDrawable(R.drawable.circle2));
                Toast.makeText(getApplicationContext(), "code sent to : " + user_phone, Toast.LENGTH_SHORT).show();
                codeSent = s;
            }
        };
    }

    public PhoneAuthCredential SignIn (String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

        return credential;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            //Toast.makeText(getApplicationContext(), "Sign In Success", Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.INVISIBLE);

                            FirebaseUser user = task.getResult().getUser();
                            UpdateUI();
                            //SignUp(user_email,user_password,user_name2,user_phone,selected_department,selected_year,selected_section);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "wrong verification code", Toast.LENGTH_SHORT).show();
                                //progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void UpdateUI()
    {
        Intent n = new Intent(getApplicationContext(), FinishProfile.class);

        n.putExtra("name", user_name2);
        n.putExtra("email", user_email);
        n.putExtra("pass", user_password);
        n.putExtra("phone", user_phone);
        n.putExtra("code", phone_code);

        startActivity(n,
                ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this).toBundle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
