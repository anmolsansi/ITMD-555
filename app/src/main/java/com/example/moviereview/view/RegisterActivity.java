package com.example.moviereview.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moviereview.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviereview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextMobile, editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });


    }

    private void register(View view) {
        String name = editTextName.getText().toString();
        String mobile = editTextMobile.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                        String userId = user.getUid();
                        User newUser = new User(name, mobile, email);
                        usersRef.child(userId).setValue(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Firebase Database", "Data successfully saved to Firebase Realtime Database");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {

                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Firebase Database", "Failed to save data to Firebase Realtime Database: " + e.getMessage());
                                    }
                                });

                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        Intent mainIntent = new Intent(this, MainActivity.class);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void viewLoginClicked(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
