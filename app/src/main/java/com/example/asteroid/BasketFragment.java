package com.example.asteroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BasketFragment extends Fragment {

    private Button btnContinueShopping, btnCheckout;
    private TextView tvTotal;
    private Double total;

    public BasketFragment() {
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
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rec_view_basket = (RecyclerView) view.findViewById(R.id.rec_view_basket);
        btnContinueShopping = view.findViewById(R.id.btnContinueShopping);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        tvTotal = view.findViewById(R.id.tvTotal);

        total = 0.00;

        ArrayList<UserBasket> basketArray = new ArrayList<>();

        BasketAdapter adapter = new BasketAdapter(getContext(), basketArray, new BasketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserBasket product) {

            }
        });

        rec_view_basket.setAdapter(adapter);
        rec_view_basket.setLayoutManager(new LinearLayoutManager(getContext()));

        //retrieving userID to access its values in database
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        String userID = fbAuth.getUid();
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance()
                .getReference("Users").child(userID)
                        .child("Basket");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserBasket userBasket = dataSnapshot.getValue(UserBasket.class);
                    basketArray.add(userBasket);

                    Double finalPrice = Double.parseDouble(userBasket.getFinalPrice());
                    total+= finalPrice;
                    tvTotal.setText("£ " + total);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                fragmentTransaction.commit();
            }
        });

    }
}