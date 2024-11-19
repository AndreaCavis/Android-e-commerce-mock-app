package com.example.asteroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class ProductFragment extends Fragment {

    private TextView tvPriceProduct, tvAmount, tvFlavour, tvSuggestedUse, tvMinus,
            tvQuantity, tvPlus;
    private Button btnAddBasket;
    private List<String> imageAccessTokens;
    private int quantity;
    private String finalPrice, productName, productQuantity, productPrice, productImg1;


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPagerProduct = (ViewPager2) view.findViewById(R.id.viewPagerProduct);


        tvPriceProduct = view.findViewById(R.id.tvPriceProduct);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvFlavour = view.findViewById(R.id.tvFlavour);
        tvSuggestedUse = view.findViewById(R.id.tvSuggestedUse);
        tvMinus = view.findViewById(R.id.tvMinus);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvPlus = view.findViewById(R.id.tvPlus);
        btnAddBasket = view.findViewById(R.id.btnAddBasket);

        if (getArguments() != null) {
            //retrieve bundle data from Recycler View Home
            ProductsModel product = (ProductsModel) getArguments().getParcelable("productInfo");

            if (product != null) {
                imageAccessTokens = new ArrayList<>();

                if (product.getImg1() != null && !product.getImg1().isEmpty()) {
                    imageAccessTokens.add(product.getImg1());
                }
                if (product.getImg2() != null && !product.getImg2().isEmpty()) {
                    imageAccessTokens.add(product.getImg2());
                }
            }

            tvPriceProduct.setText("£ " + product.getPrice());
            tvAmount.setText(product.getDimension());
            tvFlavour.setText(product.getFlavour());
            tvSuggestedUse.setText(product.getSuggested_use());

            ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), imageAccessTokens);
            viewPagerProduct.setAdapter(adapter);

            CircleIndicator3 indicator = (CircleIndicator3) view.findViewById(R.id.vpCircleIndicator3);
            indicator.setViewPager(viewPagerProduct);


            quantity = Integer.parseInt(tvQuantity.getText().toString());

            tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (quantity < 2) {
                        tvMinus.setEnabled(false);
                        tvPlus.setEnabled(true);
                        tvPlus.setTextColor(getResources().getColor(R.color.pink));
                        tvMinus.setTextColor(getResources().getColor(R.color.pink100));

                    } else {
                        tvMinus.setEnabled(true);
                        tvPlus.setTextColor(getResources().getColor(R.color.pink));
                        tvMinus.setTextColor(getResources().getColor(R.color.pink));
                        quantity--;
                    }

                    tvQuantity.setText("" + quantity);
                }
            });


            tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (quantity > Integer.parseInt(product.getQuantity())-1) {
                        tvPlus.setEnabled(false);
                        tvMinus.setEnabled(true);
                        tvMinus.setTextColor(getResources().getColor(R.color.pink));
                        tvPlus.setTextColor(getResources().getColor(R.color.pink100));
                    } else {
                        tvPlus.setEnabled(true);
                        tvMinus.setTextColor(getResources().getColor(R.color.pink));
                        tvPlus.setTextColor(getResources().getColor(R.color.pink));
                        quantity++;
                    }

                    tvQuantity.setText("" + quantity);
                }
            });


            btnAddBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productImg1 = product.getImg1();
                    productName = product.getName();
                    productPrice = product.getPrice();
                    productQuantity = tvQuantity.getText().toString();


                    String finalPrice = String.valueOf(Double.parseDouble(productPrice) * Double.parseDouble(productQuantity));


                    createUserBasket(finalPrice, productImg1, productName, productPrice, productQuantity);
                }
            });


        }
    }

    private void createUserBasket(String finalPrice,String productImg1, String productName, String productPrice, String productQuantity) {

        FirebaseAuth fbAuthBasket = FirebaseAuth.getInstance();
        FirebaseUser fbUserBasket = fbAuthBasket.getCurrentUser();
        String userID = fbAuthBasket.getUid();

        DatabaseReference userBasketReference = FirebaseDatabase.getInstance().getReference();

        //Generates a new Unique ID string for any product added
        String productID = userBasketReference.push().getKey();

        UserBasket userBasket = new UserBasket(finalPrice, productImg1, productName, productPrice, productQuantity);

        userBasketReference.child("Users").child(userID).child("Basket").child(productID)
                        .setValue(userBasket).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Item added successfully!");

                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updateNotification(Integer.parseInt(productQuantity));
                    }
                });
    }


    private void showMessage (String value) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = (TextView) layout.findViewById(R.id.toastMessage);
        text.setText(value);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}