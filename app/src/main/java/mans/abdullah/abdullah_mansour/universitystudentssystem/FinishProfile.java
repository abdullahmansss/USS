package mans.abdullah.abdullah_mansour.universitystudentssystem;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinishProfile extends AppCompatActivity {

    String selected_department,selected_year,selected_section;
    TextInputEditText selected_address;
    FloatingActionButton finish;
    CircleImageView pp;
    String name,email,pass,phone,code,address2;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    int REQUEST_CAMERA = 3;
    int SELECT_FILE = 2;
    int PICK_IMAGE_REQUEST = 71;

    Uri photoPath;

    String selected_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_profile);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        Spinner dep = (Spinner) findViewById(R.id.department_spinner);
        Spinner year = (Spinner) findViewById(R.id.year_spinner);
        Spinner section = (Spinner) findViewById(R.id.sections_spinner);

        selected_address = (TextInputEditText) findViewById(R.id.address_field);
        finish = (FloatingActionButton) findViewById(R.id.finish_btn);
        pp = (CircleImageView) findViewById(R.id.finish_profile_image);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");

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

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                address2 = selected_address.getText().toString().trim();

                if (address2.length() == 0 | selected_department.length() == 0 | selected_year.length() == 0 | selected_section.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter a valid data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog = new ProgressDialog(FinishProfile.this);
                    progressDialog.setMessage("Creating Account ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    SignUp(email,pass,name,phone, address2,selected_URL, selected_department, selected_year,selected_section);
                }
            }
        });
    }


    public void SignUp(final String email, final String password, final String name, final String phone, final String address, final String imageURL, final String dep, final String year, final String section)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            writeNewUser(user.getUid(), email,name,phone,address, imageURL ,dep,year,section);

                            /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("Jane Q. User")
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();*/

                            UpdateUI();
                            progressDialog.dismiss();
                            //progressBar.setVisibility(View.GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "invalid email",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void writeNewUser(String userId, String email, String name, String phone , String address, String imageURL ,  String dep, String year, String section)
    {
        UserData user = new UserData(userId, name, email, phone,address, imageURL, dep, year, section);

        databaseReference.child("allstudents").child(userId).setValue(user);
        databaseReference.child("sectionstudents").child(dep).child(year).child(section).child(name).setValue(user);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void UpdateUI()
    {
        Intent n = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(n,
                ActivityOptions.makeSceneTransitionAnimation(FinishProfile.this).toBundle());
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

            Picasso.get()
                    .load(photoPath)
                    .placeholder(R.drawable.user3)
                    .into(pp);

            uploadImage();
        }
    }

    private void uploadImage() {
        if (photoPath != null) {
            progressDialog = new ProgressDialog(FinishProfile.this);
            progressDialog.setMessage("Uploading Photo ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

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

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                        selected_URL = downloadUri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    // Handle unsuccessful uploads
                    Toast.makeText(getApplicationContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
