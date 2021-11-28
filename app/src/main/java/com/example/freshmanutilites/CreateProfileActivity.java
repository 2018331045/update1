package com.example.freshmanutilites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.StringNode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileActivity extends AppCompatActivity {

    private Button create_profile_save_profile_btn;
    EditText create_profile_name_et , create_profile_department_et , create_profile_batch_et , create_profile_mobile_no_et , create_profile_blood_group_et , create_profile_fb_profile_link_et;
    ImageView create_profile_image_iv;
    ProgressBar create_profile_progress_pb;
    Uri imageUri;
    UploadTask uploadTask;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    StorageReference storageReference;
    private static final int PICK_IMAGE = 1;
    AllUserInfo allUserInfo;
    String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        allUserInfo = new AllUserInfo();
        create_profile_image_iv = findViewById(R.id.create_profile_image);
        create_profile_name_et = findViewById(R.id.crete_profile_name_et);
        create_profile_department_et = findViewById(R.id.crete_profile_department_et);
        create_profile_batch_et = findViewById(R.id.crete_profile_batch_et);
        create_profile_mobile_no_et = findViewById(R.id.crete_profile_mobile_no_et);
        create_profile_blood_group_et = findViewById(R.id.crete_profile_blood_group_et);
        create_profile_fb_profile_link_et = findViewById(R.id.crete_profile_facebook_link_et);
        create_profile_save_profile_btn = findViewById(R.id.create_profile_save_button);
        create_profile_progress_pb = findViewById(R.id.create_profile_progress_bar);

        // get current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // get current user uid
        currentUserId = currentUser.getUid();
        // fire store for current user(offline - use hashmap )
        documentReference = firebaseFirestore.collection("User").document(currentUserId);
        // Store Photo in firebase storage reference  (Profile Images Storage)
        storageReference = FirebaseStorage.getInstance().getReference("Profile Images");
        // keep data in firebase realtime database with the name - "All User Info"
        // database - realtime
        databaseReference = firebaseDatabase.getReference("All Users Info");

        create_profile_save_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        // for opening image selection facility in firebase . . .
        create_profile_image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

    }

    // set the image in proper - imageView place in the activity layout
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data!=null || data.getData()!=null){
                imageUri = data.getData();
                // Picasso used for image
                Picasso.get().load(imageUri).fit().centerCrop().into(create_profile_image_iv);
               // Toast.makeText(CreateProfileActivity.this, "IMAGE SHOULD LOAD .. .", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e ){
            Toast.makeText(CreateProfileActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
        }
    }

    // we need to keep the image uri at the database for that we need the image uri
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void uploadData() {
            String name = create_profile_name_et.getText().toString();
            String dept = create_profile_department_et.getText().toString();
            String batch = create_profile_batch_et.getText().toString();
            String mobNo = create_profile_mobile_no_et.getText().toString();
            String bloodGrp = create_profile_blood_group_et.getText().toString();
            String link = create_profile_name_et.getText().toString();

            if(!(name.isEmpty()) && !(dept.isEmpty()) && !(batch.isEmpty()) && !(mobNo.isEmpty()) && !(bloodGrp.isEmpty()) && !(link.isEmpty()) ){
                create_profile_progress_pb.setVisibility(View.VISIBLE);
               // Toast.makeText(CreateProfileActivity.this, "Here", Toast.LENGTH_SHORT).show();
                // storage reference to keep the photo in the firebase storage
                final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
                uploadTask = reference.putFile(imageUri);
                // follow the documentation or copy it for keep data in the storage
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        Toast.makeText(CreateProfileActivity.this, "Here I am", Toast.LENGTH_SHORT).show();
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){
                            // get the image uri we keep in the firebase storage
                            Uri downloadUri = task.getResult();
                            // map - hashmap needed to keeep the string and files
                            Map<String,String> profile = new HashMap<>();

                            profile.put("name",name);
                            profile.put("Dept",dept);
                            profile.put("Batch",batch);
                            profile.put("Mobile No",mobNo);
                            profile.put("Blood Group",bloodGrp);
                            profile.put("User Id",currentUserId);
                            profile.put("Facebook Profile Link",link);
                            profile.put("Image URL",downloadUri.toString());
                            profile.put("privacy","public");
                            // set the info to the model class for further real time database process
                            allUserInfo.setName(name);
                            allUserInfo.setDept(dept);
                            allUserInfo.setBatch(batch);
                            allUserInfo.setMobNo(mobNo);

                            allUserInfo.setBloodGrp(bloodGrp);
                            allUserInfo.setUrl(downloadUri.toString());
                            // keep the references in the realtime data base
                            // remember -- database - real time
                            databaseReference.child(currentUserId).setValue(allUserInfo);
                            // document -- firestore - offline database
                            documentReference.set(profile)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            create_profile_progress_pb.setVisibility(View.INVISIBLE);
                                            Toast.makeText(CreateProfileActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                        // handler for ui purposes to keep the user expectation
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(CreateProfileActivity.this, Fragment1.class);
                                                    startActivity(intent);
                                                }
                                            },2000);
                                        }
                                    });
                        }
                    }
                });
            } else {
                Toast.makeText(CreateProfileActivity.this, "Please Fill All The Values Correctly!", Toast.LENGTH_SHORT).show();
            }
    }
}