package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText email,pass,user_name,phone_num,code;
    FloatingActionButton sign_up_btn2;
    String user_email,user_password,user_name2,user_phone,phone_code;
    String selected_department,selected_year,selected_section;
    Button send_code_btn;
    ProgressBar progressBar;

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
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress_bar);

        progressBar.setVisibility(View.GONE);

        Spinner dep = (Spinner) findViewById(R.id.department_spinner);
        Spinner year = (Spinner) findViewById(R.id.year_spinner);
        Spinner section = (Spinner) findViewById(R.id.sections_spinner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign up");

        OnVerificationStateChanged();
        send_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_phone = phone_num.getText().toString().trim();

                if (user_phone.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter phone number", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        new CountDownTimer(4000, 1000) {

                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            public void onTick(long millisUntilFinished) {
                                //here you can have your logic to set text to edittext
                                progressBar.setVisibility(View.VISIBLE);
                                send_code_btn.setEnabled(false);
                                send_code_btn.setBackground(getDrawable(R.drawable.circle1));
                            }

                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            public void onFinish() {
                                progressBar.setVisibility(View.GONE);
                                send_code_btn.setEnabled(true);
                                send_code_btn.setBackground(getDrawable(R.drawable.circle2));
                            }

                        }.start();
                        send_code_btn.setText("re-send code");
                        startPhoneNumberVerification(user_phone);
                    }
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.department, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dep.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        year.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.section, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        section.setAdapter(adapter3);

        dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selected_department = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selected_year = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selected_section = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                if (user_email.length() == 0 | user_password.length() == 0 | user_name2.length() == 0 | user_phone.length() == 0 | phone_code.length() == 0
                        | selected_department.length() == 0 | selected_year.length() == 0 | selected_section.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter a valid data", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        signInWithPhoneAuthCredential(SignIn(phone_code));
                        //Toast.makeText(getApplicationContext(), "signed up success", Toast.LENGTH_SHORT).show();
                        SignUp(user_email,user_password,user_name2,user_phone,selected_department,selected_year,selected_section);
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
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
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
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void SignUp(final String email, final String password, final String name, final String phone, final String dep, final String year, final String section)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            writeNewUser(email,name,phone,dep,year,section);
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "You already have an account",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String email, String name, String phone, String dep, String year, String section)
    {
        UserData user = new UserData(email, name, phone, dep, year, section);

        mDatabase.child("students").child(dep).child(year).child(section).child(email).setValue(user);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void UpdateUI()
    {
        Intent n = new Intent(getApplicationContext(), MainActivity.class);
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

    /*public void onRadioButtonClicked()
    {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup gp,int ID)
            {
                switch(ID)
                {
                    case R.id.year1:
                        y1.setTextColor(getResources().getColor(R.color.textradio2));
                        y2.setTextColor(getResources().getColor(R.color.textradio));
                        y3.setTextColor(getResources().getColor(R.color.textradio));
                        y4.setTextColor(getResources().getColor(R.color.textradio));
                        break;
                    case R.id.year2:
                        y1.setTextColor(getResources().getColor(R.color.textradio));
                        y2.setTextColor(getResources().getColor(R.color.textradio2));
                        y3.setTextColor(getResources().getColor(R.color.textradio));
                        y4.setTextColor(getResources().getColor(R.color.textradio));
                        break;
                    case R.id.year3:
                        y1.setTextColor(getResources().getColor(R.color.textradio));
                        y2.setTextColor(getResources().getColor(R.color.textradio));
                        y3.setTextColor(getResources().getColor(R.color.textradio2));
                        y4.setTextColor(getResources().getColor(R.color.textradio));
                        break;
                    case R.id.year4:
                        y1.setTextColor(getResources().getColor(R.color.textradio));
                        y2.setTextColor(getResources().getColor(R.color.textradio));
                        y3.setTextColor(getResources().getColor(R.color.textradio));
                        y4.setTextColor(getResources().getColor(R.color.textradio2));
                        break;
                }
            }
        });
    }*/
}
