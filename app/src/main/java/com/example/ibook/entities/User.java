package com.example.ibook.entities;

import android.app.Notification;
import android.os.SystemClock;
import android.util.Log;

import com.example.ibook.activities.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a model/entity class for the user
 * has getters for the attributes and is passed to the firestore cloud as a object
 */
public class User implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private ArrayList<Book> bookList;
    private ArrayList<String> notificationList; //holds all the notifications for the user
    private String userID;

//
//
//    BookRequest
//

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }
//    Sender;
//    Receiver;
//    Book;
//
//    for the current user, I will check in the book request collection.
//    for everyplace where currentUserID matches the requestsender  docoument in bookRequest document
//
//    if book.status is requested,
//    then i gather, then i display in requested toggle
//
//            the owner accepts it, delete all the documents where the bookID matches, except for the document where the senderID is the one i accepted of
//
//            if book.status is accepted
//    then I put them in accepeted booklist

    /**
     * no argument constructor for the firebase cloud
     */
    public User() {


    }// empty constructor

    /**
     * This constructor is called when a new user signs up
     *
     * @param userName
     * @param password
     * @param email
     * @param phoneNumber
     */
    public User(String userName, String password, String email, String phoneNumber, String userID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bookList = new ArrayList<Book>();
        this.notificationList = new ArrayList<String>();
        this.userID = userID;
    }// constructor


    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This method returns the users booklist
     *
     * @return arraylist of books of the user
     */

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public ArrayList<String> getNotificationList() {
        return notificationList;
    }

    public void addToNotificationList(String message) {
        notificationList.add(message);
    }

    public void addToBookList(Book book) {
        bookList.add(book);
    }


}// users class
