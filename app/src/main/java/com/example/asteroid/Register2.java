package com.example.asteroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class Register2 extends AppCompatActivity {
    private TextInputLayout tilFullName, tilAddressLine, tilFlatNumber, tilPostcode, tilCity;
    private TextInputEditText tietFullName, tietAddressLine, tietFlatNumber, tietPostcode, tietCity;
    private String fullName, addressLine, flatNumber, postcode, city;
    private String cardName, cardNumber, expMM, expYY, cvv;
    private String username, userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        CardView cvPayment = (CardView) findViewById(R.id.cvPayment);
        cvPayment.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvRegStep = toolbar.findViewById(R.id.tvRegStep);
        ImageView imgToolbar = toolbar.findViewById(R.id.imgToolbar);

        imgToolbar.setImageResource(R.drawable.register_pink_toolbar);
        tvRegStep.setText("2/2");
        tvRegStep.setVisibility(View.VISIBLE);


        TextView tvAddPayment = (TextView) findViewById(R.id.tvAddPayment);
        TextView tvAddCard = (TextView) findViewById(R.id.tvAddCard);
        TextView tvCancel = (TextView) findViewById(R.id.tvCancel);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);

        TextInputLayout tilFullName = (TextInputLayout) findViewById(R.id.tilFullName);
        TextInputLayout tilAddressLine = (TextInputLayout) findViewById(R.id.tilAddressLine);
        TextInputLayout tilFlatNumber = (TextInputLayout) findViewById(R.id.tilFlatNumber);
        TextInputLayout tilPostcode = (TextInputLayout) findViewById(R.id.tilPostcode);
        TextInputLayout tilCity = (TextInputLayout) findViewById(R.id.tilCity);

        TextInputEditText tietFullName = (TextInputEditText) findViewById(R.id.tietFullName);
        TextInputEditText tietAddressLine = (TextInputEditText) findViewById(R.id.tietAddressLine);
        TextInputEditText tietFlatNumber = (TextInputEditText) findViewById(R.id.tietFlatNumber);
        TextInputEditText tietPostcode = (TextInputEditText) findViewById(R.id.tietPostcode);
        TextInputEditText tietCity = (TextInputEditText) findViewById(R.id.tietCity);

        //this userID is the database unique identifier for the current user
        userID = getIntent().getExtras().getString("userID");

        CardForm cardForm = (CardForm) findViewById(R.id.card_form);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED);

        cardForm.setup(Register2.this);

        EditText etCardName = (EditText) cardForm.findViewById(com.braintreepayments.cardform.R.id.bt_card_form_cardholder_name);
        EditText etCardNumber = (EditText) cardForm.findViewById(com.braintreepayments.cardform.R.id.bt_card_form_card_number);
        EditText etExpDate = (EditText) cardForm.findViewById(com.braintreepayments.cardform.R.id.bt_card_form_expiration);
        EditText etCvv = (EditText) cardForm.findViewById(com.braintreepayments.cardform.R.id.bt_card_form_cvv);


        tvAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { cvPayment.setVisibility(View.VISIBLE); } });


        tvAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardForm.validate();

                cardName = cardForm.getCardholderName();
                cardNumber = cardForm.getCardNumber();
                expMM = cardForm.getExpirationMonth();
                expYY = cardForm.getExpirationYear();
                cvv = cardForm.getCvv();

                if (cardName.isEmpty()) { completeCardForm(etCardName); }
                else if (cardNumber.isEmpty()) { completeCardForm(etCardNumber); }
                else if (expMM.isEmpty()) { completeCardForm(etExpDate); }
                else if (expYY.isEmpty()) { completeCardForm(etExpDate); }
                else if (cvv.isEmpty()) { completeCardForm(etCvv); }
                else if (cardForm.isValid()) {
                    registerUserCard(cardName, cardNumber, expMM, expYY, cvv);
                    showMessage("Card added successfully");
                    cvPayment.setVisibility(View.GONE);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCardName.setText("");
                etCardNumber.setText("");
                etExpDate.setText("");
                etCvv.setText("");
                cvPayment.setVisibility(View.GONE);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = tietFullName.getText().toString();
                addressLine = tietAddressLine.getText().toString();
                flatNumber = tietFlatNumber.getText().toString();
                postcode = tietPostcode.getText().toString().toUpperCase();
                city = tietCity.getText().toString();

                if (fullName.isEmpty()) { completeForm(tietFullName); }
                else if (addressLine.isEmpty()) { completeForm(tietAddressLine); }
                else if (flatNumber.isEmpty()) { tietFlatNumber.setText("none"); }
                else if (postcode.isEmpty()) { completeForm(tietPostcode); }
                else if (city.isEmpty()) { completeForm(tietCity); }
                else {
                    registerUserAddress(fullName, addressLine, flatNumber, postcode, city);
                }
            }
        });

    }

    private void registerUserCard(String cardName, String cardNumber, String expMM, String expYY, String cvv) {

        FirebaseAuth fbAuth2 = FirebaseAuth.getInstance();
        FirebaseUser fbUser2 = fbAuth2.getCurrentUser();

        UserCardDetails userCardDetails = new UserCardDetails (cardName, cardNumber, expMM, expYY, cvv);

        DatabaseReference userCardRef = FirebaseDatabase.getInstance().getReference();
        String cardID = userCardRef.push().getKey(); //Generates unique ID for each card

        userCardRef.child("Users").child(userID).child("Card details").child(cardID)
                .setValue(userCardDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showMessage("New card added");
            }
        });
    }

    private void registerUserAddress(String fullName, String addressLine, String flatNumber, String postcode, String city) {

        FirebaseAuth fbAuth2 = FirebaseAuth.getInstance();
        FirebaseUser fbUser2 = fbAuth2.getCurrentUser();

        UserAddress userAddress = new UserAddress(fullName, addressLine, flatNumber, postcode, city);

        DatabaseReference userAddressRef = FirebaseDatabase.getInstance().getReference();
        String addressID = userAddressRef.push().getKey(); // unique address ID generator

        userAddressRef.child("Users").child(userID).child("Address").child(addressID)
                .setValue(userAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showMessage("Account registered. Sending verification email");
                fbUser2.sendEmailVerification();

                Intent intent = new Intent(Register2.this, Login.class);
                startActivity(intent);
            }
        });
    }


    public void completeForm(TextInputEditText field) {
        field.setError("Required field");
        field.requestFocus();
        showMessage("Mandatory field");
    }

    public void completeCardForm(EditText field) {
        field.setError("Required field");
        field.requestFocus();
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