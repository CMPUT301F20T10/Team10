package com.example.ibook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibook.BookListAdapter;
import com.example.ibook.R;
import com.example.ibook.activities.AddMyBookActivity;
import com.example.ibook.activities.ViewBookActivity;
import com.example.ibook.entities.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BookListFragment extends Fragment {

    //Private variables
    private ListView bookListView;
    private BookListAdapter adapter;
    private ArrayList<Book> datalist;
    private Button btn_addBook;
    private FirebaseFirestore db;
    private String userID;
    private String userName;
    private FirebaseAuth uAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booklist, container, false);
        // Set up the view
        bookListView = root.findViewById(R.id.bookList);
        btn_addBook = root.findViewById(R.id.button_add);
        datalist = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        uAuth = FirebaseAuth.getInstance();

        adapter = new BookListAdapter(datalist, getActivity());
        bookListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //default username = "yzhang24@gmail.com";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // try to get the userID
            userID = user.getUid();
            //Toast.makeText(getContext(), userID, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "invalid user", Toast.LENGTH_SHORT).show();
            return root;
        }


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // todo: change email key word to username
                                String matchID = document.getId();
                                //Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
                                if (matchID.equals(userID)) {
                                    //Toast.makeText(getContext(), "match", Toast.LENGTH_SHORT).show();\

                                    Map<String, Object> convertMap;
                                    ArrayList<Book> hashList = (ArrayList<Book>) document.get("BookList");
                                    if (document.getData().containsKey("BookList")) {
                                        //Toast.makeText(getContext(), "exist", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Toast.makeText(getContext(), "not exist", Toast.LENGTH_SHORT).show();
                                        Map<String, Object> data = new HashMap();
                                        ArrayList<Book> bookList = new ArrayList<>();
                                        data = document.getData();
                                        data.put("BookList", bookList);
                                        db.collection("users")
                                                .document(userID).set(data);
                                        return;
                                    }


                                    for (int i = 0; i < hashList.size(); i += 1) {
                                        convertMap = (Map<String, Object>) hashList.get(i);

                                        datalist.add(new Book(
                                                String.valueOf(convertMap.get("title")),
                                                String.valueOf(convertMap.get("author")),
                                                String.valueOf(convertMap.get("date")),
                                                (String.valueOf(convertMap.get("description"))),
                                                from_string_to_enum(String.valueOf(convertMap.get("status"))),
                                                String.valueOf(convertMap.get("isbn"))
                                        ));
                                    }
                                    if (datalist == null) {
                                        datalist = new ArrayList<>();
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "got an error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        // view book on the list
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ViewBookActivity.class);
                intent.putExtra("USER_ID", userID);
                intent.putExtra("BOOK_NUMBER", position);
                startActivityForResult(intent, 0);
            }
        });

        // add book button
        btn_addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMyBookActivity.class);
                intent.putExtra("USER_ID", userID);
                startActivityForResult(intent, 0);
            }
        });


        return root;
    }

    @Override // if add/edit/delete books, update changes
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) { // if data changed, update
            Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
            DocumentReference docRef = db.collection("users").document(userID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> convertMap;

                            ArrayList<Book> hashList = (ArrayList<Book>) document.get("BookList");
                            datalist = new ArrayList<>();
                            for (int i = 0; i < hashList.size(); i += 1) {
                                convertMap = (Map<String, Object>) hashList.get(i);


                                datalist.add(new Book(
                                        String.valueOf(convertMap.get("title")),
                                        String.valueOf(convertMap.get("author")),
                                        String.valueOf(convertMap.get("date")),
                                        (String.valueOf(convertMap.get("description"))),
                                        from_string_to_enum(String.valueOf(convertMap.get("status"))),
                                        String.valueOf(convertMap.get("isbn"))
                                ));

                            }

                            if (datalist == null) {
                                datalist = new ArrayList<>();
                            } else {
                                adapter = new BookListAdapter(datalist, getActivity());
                                bookListView.setAdapter(adapter);
                            }

                        } else {
                            Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "got an error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }


    public Book.Status from_string_to_enum(String input) {
        if (input.equals("Available"))
            return Book.Status.Available;

        if (input.equals("Available"))
            return Book.Status.Available;

        if (input.equals("Available"))
            return Book.Status.Available;

        if (input.equals("Available"))
            return Book.Status.Available;
        // todo: change later
        return Book.Status.Available;
    }
}