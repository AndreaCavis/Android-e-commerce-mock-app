package com.example.asteroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.braintreepayments.cardform.view.CardForm;
import com.example.asteroid.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // we'll see abt this later
//        showMessage("Welcome to Asteroids " + username);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView img = toolbar.findViewById(R.id.imgToolbar);
        replaceFragment(new HomeFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                img.setImageResource(R.drawable.asteroid_pink_toolbar);
                replaceFragment(new HomeFragment());
                return true;
            }
            else if (item.getItemId() == R.id.account) {
                img.setImageResource(R.drawable.account_pink_toolbar);
                replaceFragment(new AccountFragment());
                return true;
            }
            else if (item.getItemId() == R.id.basket) {
                img.setImageResource(R.drawable.basket_pink_toolbar);
                replaceFragment(new BasketFragment());
                return true;
            }
            return true;
        });
    }


    public void replaceFragment (Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }


    public void updateNotification(int quantity) {
        int notificationValue = Integer.parseInt(binding.tvNotification.getText().toString());
        notificationValue += quantity;
        binding.tvNotification.setText("" + notificationValue);
        binding.cvNotification.setVisibility(View.VISIBLE);
        binding.tvNotification.setVisibility(View.VISIBLE);
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