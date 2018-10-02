package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    MaterialEditText email,pass;
    Button back_btn,sign_in_btn;
    TextView forgot;
    CheckBox remember;
    String email_txt,pass_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = (MaterialEditText) findViewById(R.id.email_sign_in_field);
        pass = (MaterialEditText) findViewById(R.id.password_sign_in_field);

        forgot = (TextView) findViewById(R.id.forgot_password_txt);
        remember = (CheckBox) findViewById(R.id.remember_me_checkbox);

        back_btn = (Button) findViewById(R.id.back_sign_in_btn);
        sign_in_btn = (Button) findViewById(R.id.sign_in_in_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity.super.onBackPressed();
            }
        });

    }



    @Override
    public void onBackPressed() {

    }
}
