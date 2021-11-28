package com.example.freshmanutilites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VarsityActivity extends AppCompatActivity implements View.OnClickListener{


    FloatingActionButton fp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;
    //StorageReference ref2;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuserId = user.getUid();


        setContentView(R.layout.activity_varsity);

        iv = findViewById(R.id.varsity_image);
        fp= findViewById(R.id.floatingActionButton2);
        ref = db.collection("User").document(currentuserId);

        fp.setOnClickListener(this);
        iv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.floatingActionButton2)
        {
            Intent intent = new Intent(getApplicationContext(), Questions.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onStart() {

        super.onStart();

        ref.get().addOnCompleteListener((task) -> {
            if(task.getResult().exists())
            {
                String url = task.getResult().getString(("Image URL"));

                System.out.println("url is "+url);


                Picasso.get().load(url).fit().into(iv);


            }
            else

            {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
    }
}