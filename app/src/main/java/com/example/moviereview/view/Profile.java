package com.example.moviereview.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.moviereview.R;

public class Profile extends AppCompatActivity {

    private LinearLayout explorerLayout;
    private EditText nameET, mobileET, emailET;
    private Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        explorerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, HomepageActivity.class));
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();


            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String mobile = snapshot.child("mobile").getValue(String.class);
                        nameET.setText(name);
                        mobileET.setText(mobile);
                        emailET.setText(email);

                        nameET.setFocusable(false);
                        nameET.setClickable(false);

                        mobileET.setFocusable(false);
                        mobileET.setClickable(false);

                        emailET.setFocusable(false);
                        emailET.setClickable(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firebase Database", "Database error: " + error.getMessage());
                }
            });

        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Signout successful", Toast.LENGTH_LONG).show();
                signOut();
            }
        });

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Profile.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initView() {

        explorerLayout = findViewById(R.id.explorer);
        nameET = findViewById(R.id.nameET);
        mobileET = findViewById(R.id.mobileET);
        emailET = findViewById(R.id.emailET);
        signout = findViewById(R.id.signout);
    }
}