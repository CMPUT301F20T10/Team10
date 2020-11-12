package com.example.ibook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ibook.R;
import com.example.ibook.entities.Book;
import com.example.ibook.entities.User;
import com.example.ibook.fragment.ScanFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 *
 */
public class AddBookActivity extends AppCompatActivity implements ScanFragment.OnFragmentInteractionListener {
    private User user;
    private Book book;
    private EditText bookNameEditText;
    private EditText authorEditText;
    private EditText dateEditText;
    private EditText isbnEditText;
    private Button cancelButton;
    private Button completeButton;
    private Button scanButton;
    private ImageView imageView;
    private FirebaseFirestore db;
    private String userID;
    private ArrayList<Book> books;
    private final int REQ_CAMERA_IMAGE = 1;
    private final int REQ_GALLERY_IMAGE = 2;
    String bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the top bar and make it full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_add_or_edit_book_screen);
        bookNameEditText = findViewById(R.id.titleEditor);
        authorEditText = findViewById(R.id.authorEditor);
        dateEditText = findViewById(R.id.dateEditor);
        isbnEditText = findViewById(R.id.isbnEditor);

        cancelButton = findViewById(R.id.cancelButton);
        completeButton = findViewById(R.id.completeButton);
        scanButton = findViewById(R.id.scanButton);
        imageView = findViewById(R.id.imageView);

        db = FirebaseFirestore.getInstance();
        books = new ArrayList<>();

        Intent intent = getIntent();
       // userID = MainActivity.database.getCurrentUserUID();

        // go back when clicking cancel
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // complete editing
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bookName = bookNameEditText.getText().toString();
                final String authorName = authorEditText.getText().toString();
                final String date = dateEditText.getText().toString();
                final String isbn = isbnEditText.getText().toString();

                // check full information
                if (bookName.length() > 0
                        && authorName.length() > 0
                        && date.length() > 0
                        && isbn.length() > 0) {

                    DocumentReference docRef = MainActivity.database.getUserDocumentReference();
                                    // NEW BOOK OBJECT


                                    Book newBook = new Book(bookName,authorName,date,isbn, MainActivity.user);


                                    // add new book object to 'books' collection in database
                                    MainActivity.database.getDb().collection("Books").add(newBook).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // storing the firebase generated unique id in bookID
                                            bookId = documentReference.getId();
                                            Toast.makeText(getBaseContext(), "Add book successfully!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    //adding bookid to the newbook object
                                    newBook.setBookID(bookId);
                                    //adding the new book to the user objects booklist
                                    MainActivity.user.addBookToOwnedBooksList(newBook);

                                    //updating the current user information in the database
                    Toast.makeText(getBaseContext(), "Coming here okay!", Toast.LENGTH_LONG).show();
                                    MainActivity.database.getUserDocumentReference().set(MainActivity.user);
                                    Toast.makeText(getBaseContext(), "Add book successfully!", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                }//if
                                 else {
                                 Toast.makeText(getBaseContext(), "Please input full information", Toast.LENGTH_SHORT).show();
                                 }

                        }// onclick
                    });//onClickListener




        // choose a image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        // scan ISBN
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanFragment().show(getSupportFragmentManager(), "Scan ISBN");
            }
        });
    }
    // let user choose to use camera or gallery
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Upload Image")
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // second parameter : request code

                        Toast.makeText(getBaseContext(), "There is a bug with camera, please use gallery", Toast.LENGTH_SHORT).show();
                        // TODO: there is a bug when using camera, maybe because of MediaStore library
                        // startActivityForResult(intent, REQ_CAMERA_IMAGE);

                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(intent, REQ_GALLERY_IMAGE);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAMERA_IMAGE) {
            // result of camera
            if (resultCode == RESULT_OK) {
                // get image data
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                onSuccessChangePhoto(bitmap);
            }

        } else if (requestCode == REQ_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                // get image data
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(bitmap);
                    onSuccessChangePhoto(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imageView.setImageURI(selectedImage);
            }
        }
    }
    private void onSuccessChangePhoto(Bitmap bitmap) {
        //Intent intent = new Intent();
        //intent.putExtra("PHOTO_CHANGE", bitmap);
        //setResult(1, intent);
        // Comment: so far, we can only let user upload photo, but can't store it
        //      Thus, unfortunately, it won't be passed back to the previous activity
        // TODO: figure out how to scale image, compress it and store it to database
    }

    @Override
    public void onOkPressed(String ISBN) {
        isbnEditText.setText(ISBN);
    }
}