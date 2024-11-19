package com.example.asteroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Utilities;

public class Login extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText tietEmail, tietPassword;
    private FirebaseAuth fbAuth;

    //If the user logged in before, it goes to the MainActivity.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = fbAuth.getCurrentUser();
        if(fbAuth.getCurrentUser() != null && firebaseUser.isEmailVerified()){
            showMessage("Welcome to Asteroids");
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            //finish(); // Close login activity
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView img = toolbar.findViewById(R.id.imgToolbar);
        TextView tvRegStep = toolbar.findViewById(R.id.tvRegStep);

        TextInputLayout tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        TextInputEditText tietEmail = (TextInputEditText) findViewById(R.id.tietEmail);
        TextInputEditText tietPassword = (TextInputEditText) findViewById(R.id.tietPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView tvRegister = (TextView) findViewById(R.id.tvRegister);

        fbAuth = FirebaseAuth.getInstance();

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


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = tietEmail.getText().toString();
                String pwd = tietPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    tietEmail.requestFocus();
                    showMessage("Missing field");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tietEmail.requestFocus();
                    tietEmail.setError("Insert a valid email address");
                } else if (TextUtils.isEmpty(pwd)) {
                    tietPassword.requestFocus();
                    showMessage("Missing field");
                } else { loginUser(email, pwd); }

            }
        });


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Redirecting...");

                Intent intent = new Intent(Login.this, Register1.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String email, String pwd) {

        fbAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser fbUser = fbAuth.getCurrentUser();

                    if (fbUser.isEmailVerified()) {

                        showMessage("Welcome to Asteroids ");


                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        fbAuth.signOut();
                        showEmailDialog();
                    }

                }
                else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        showMessage("User does not exist");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        showMessage("Invalid credentials");
                    } catch (Exception e) {
                        Log.e("Register1", e.getMessage());
                        showMessage("" + e.getMessage());
                    }
                }
            }
        });
    }

    private void showEmailDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Email not verified")
                .setMessage("You cannot access the app without verifying the email first")
                .setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        //this flag will open the email app in a different window
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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