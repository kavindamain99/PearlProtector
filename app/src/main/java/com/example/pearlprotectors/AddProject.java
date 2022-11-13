package com.example.pearlprotectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pearlprotectors.Models.Projects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddProject extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button button;
    ImageView imageView;
    DatabaseReference ref;
    String Storage_Path = "All_Image_Uploads/";
    Uri FilePathUri;
    StorageReference storageReference;
    int Image_Request_Code = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        ref = FirebaseDatabase.getInstance().getReference("ProjectDetails");
        storageReference = FirebaseStorage.getInstance().getReference();

        editText1 = (EditText)findViewById(R.id.ptitle);
        editText2 = (EditText)findViewById(R.id.pvenue);
        editText3 = (EditText)findViewById(R.id.pdate);
        editText4 = (EditText)findViewById(R.id.pdescription);
        button = (Button)findViewById(R.id.addbtn);
        imageView = (ImageView)findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadImageFileToFirebaseStorage();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                imageView.setImageBitmap(bitmap);


            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                    new OnCompleteListener<Uri>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String fileLink = task.getResult().toString();
                                            //next work with URL

                                            System.out.println(fileLink);

                                            final String title = editText1.getText().toString();
                                            final String venue = editText2.getText().toString();
                                            final String date = editText3.getText().toString();
                                            final String description = editText4.getText().toString();

                                            if (title.isEmpty()) {
                                                editText1.setError("Project title is required");
                                            }else if (venue.isEmpty()) {
                                                editText2.setError("Project venue is required");
                                            }else if (date.isEmpty()) {
                                                editText3.setError("Project date is required");
                                            }else if (description.isEmpty()) {
                                                editText4.setError("Project Description is required");
                                            }else {

                                                // Getting image upload ID.
                                                String key = ref.push().getKey();

                                                System.out.println();

                                                @SuppressWarnings("VisibleForTests")
                                                Projects projects = new Projects(key, fileLink, title, venue, date, description);

                                                // Adding image upload id s child element into databaseReference.
                                                ref.child(key).setValue(projects);

                                                // Showing toast message after done uploading.
                                                Toast.makeText(getApplicationContext(), "Data Uploaded Successfully ", Toast.LENGTH_LONG).show();

                                            }

                                        }
                                    });


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Showing exception erro message.
                            Toast.makeText(AddProject.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
        else {

            Toast.makeText(AddProject.this, "Failed", Toast.LENGTH_LONG).show();

        }
    }
}