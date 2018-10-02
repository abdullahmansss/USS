package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity {
    RadioGroup group;
    RadioButton y1,y2,y3,y4;
    MaterialEditText email,pass,user_name,phone_num;
    Button back_btn,sign_up_btn2;
    String user_email,user_password,user_name2,user_phone;
    String selected_department,selected_year,selected_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*group = (RadioGroup) findViewById(R.id.group);
        y1 = (RadioButton) findViewById(R.id.year1);
        y2 = (RadioButton) findViewById(R.id.year2);
        y3 = (RadioButton) findViewById(R.id.year3);
        y4 = (RadioButton) findViewById(R.id.year4);*/

        email = (MaterialEditText) findViewById(R.id.email_field);
        pass = (MaterialEditText) findViewById(R.id.password_field);
        user_name = (MaterialEditText) findViewById(R.id.name_field);
        phone_num = (MaterialEditText) findViewById(R.id.phone_field);

        back_btn = (Button) findViewById(R.id.back_btn);
        sign_up_btn2 = (Button) findViewById(R.id.sign_up_btn2);

        Spinner dep = (Spinner) findViewById(R.id.department_spinner);
        Spinner year = (Spinner) findViewById(R.id.year_spinner);
        Spinner section = (Spinner) findViewById(R.id.sections_spinner);

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

        sign_up_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = email.getText().toString();
                user_password = pass.getText().toString();
                user_name2 = user_name.getText().toString();
                user_phone = phone_num.getText().toString();

                if (user_email.length() == 0 | user_password.length() == 0 | user_name2.length() == 0 | user_phone.length() == 0
                        | selected_department.length() == 0 | selected_year.length() == 0 | selected_section.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter a valid data", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        Toast.makeText(getApplicationContext(), "signed up success", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
               SignUpActivity.super.onBackPressed();
            }
        });

        //onRadioButtonClicked();
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
