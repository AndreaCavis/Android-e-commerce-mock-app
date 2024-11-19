package com.example.asteroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register1 extends AppCompatActivity {

    ImageView imgUserPropic;

    private TextInputLayout tilUsername, tilEmail, tilPassword, tilConfirmPassword, tilMobile;
    private TextInputEditText tietUsername, tietEmail, tietPassword, tietConfirmPassword, tietMobile;
    private String username, email, confirmEmail, password, confirmPassword, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvRegStep = toolbar.findViewById(R.id.tvRegStep);
        ImageView imgToolbar = toolbar.findViewById(R.id.imgToolbar);

        imgToolbar.setImageResource(R.drawable.register_pink_toolbar);
        tvRegStep.setVisibility(View.VISIBLE);

        Button btnContinue = (Button) findViewById(R.id.btnContinue);
        ImageView imgUserPropic = (ImageView) findViewById(R.id.imgUserPropic);

        TextInputLayout tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        TextInputLayout tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        TextInputLayout tilConfirmPassword = (TextInputLayout) findViewById(R.id.tilConfirmPassword);
        TextInputLayout tilMobile = (TextInputLayout) findViewById(R.id.tilMobile);

        TextInputEditText tietUsername = (TextInputEditText) findViewById(R.id.tietUsername);
        TextInputEditText tietEmail = (TextInputEditText) findViewById(R.id.tietEmail);
        TextInputEditText tietPassword = (TextInputEditText) findViewById(R.id.tietPassword);
        TextInputEditText tietConfirmPassword = (TextInputEditText) findViewById(R.id.tietConfirmPassword);
        TextInputEditText tietMobile = (TextInputEditText) findViewById(R.id.tietMobile);


        tietPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tietPassword.length() > 0 && tietPassword.length() < 8) {
                    tilPassword.setError("Enter a minimum of 8 characters");
                    tilPassword.setErrorEnabled(true);
                } else tilPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        tietConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tietConfirmPassword.length() > 0 && tietConfirmPassword.length() < 8) {
                    tilConfirmPassword.setError("Enter a minimum of 8 characters");
                    tilConfirmPassword.setErrorEnabled(true);
                } else tilConfirmPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = tietUsername.getText().toString();
                email = tietEmail.getText().toString().trim();
                password = tietPassword.getText().toString();
                confirmPassword = tietConfirmPassword.getText().toString();
                mobile = tietMobile.getText().toString();

                if (username.isEmpty()) { completeForm(tietUsername);}
                else if (email.isEmpty()) { completeForm(tietEmail); }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tietEmail.requestFocus();
                    tietEmail.setError("Insert a valid email address");}
                else if (password.isEmpty()) { completeForm(tietPassword); }
                else if (password.length() < 8) { tietPassword.requestFocus(); }
                else if (confirmPassword.isEmpty()) { completeForm(tietConfirmPassword); }
                else if (!password.equals(confirmPassword)) {
                    tietConfirmPassword.requestFocus();
                    tietConfirmPassword.setError("The password does not match");
                }
                else if (mobile.isEmpty()) { completeForm(tietMobile); }
                else if (mobile.length() != 10) {
                    tietMobile.requestFocus();
                    tietMobile.setError("Insert a 10 digit mobile number");}
                else {
                    registerUser(username, email, password, mobile); }
            }
        });
    }

    private void registerUser(String username, String email, String password, String mobile) {

        FirebaseAuth fbAuth = FirebaseAuth.getInstance();

        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register1.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser fbUser = fbAuth.getCurrentUser();
                    String userID = fbAuth.getUid();

                    UserDetails userDetails = new UserDetails(email, mobile, username);

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                    userRef.child(userID).child("Profile").setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(Register1.this, Register2.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                   try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        showMessage("Password too weak");
                    } catch (FirebaseAuthUserCollisionException e) {
                        showMessage("Email already in use");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        showMessage(e.getMessage());
                    } catch (Exception e) {
                        Log.e("Register1", e.getMessage());
                        showMessage("" + e.getMessage());
                    }
                }
            }
        });
    }


    public void completeForm(TextInputEditText field) {
        field.requestFocus();
        field.setError("Required field");
        showMessage("Mandatory field");
    }


    private void showMessage (String value) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = (TextView) layout.findViewById(R.id.toastMessage);
        text.setText(value);
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}