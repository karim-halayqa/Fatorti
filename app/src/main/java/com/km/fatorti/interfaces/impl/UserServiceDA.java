package com.km.fatorti.interfaces.impl;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.km.fatorti.interfaces.UserService;
import com.km.fatorti.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * used to store user information
 *
 * @author Aws Ayyash
 */

public class UserServiceDA implements UserService, Serializable {

    private List<User> userList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String collectionName = "User";

    public UserServiceDA() {

    }

    @Override
    public User findUser(String userName) {

        getAll();
        for (User user : userList) {
            if (user.getUserName().equals(userName)) {
                return user;
            }

        }
        return null; // this means, my userName (USER) does not exist -Not registered-
    }

    @Override
    public void addUser(User newUser) {
        userList.add(newUser);

        db.collection(collectionName).add(newUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();

                db.collection(collectionName).document(id).update("documentId", id);
                newUser.setDocumentId(id);
            }
        });
    }

    @Override
    public void addAll(List<User> users) {
        for (User user : userList) {


            db.collection(collectionName).add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String id = documentReference.getId();

                    db.collection(collectionName).document(id).update("documentId", id);
                    user.setDocumentId(id);
                }
            });
        }
    }

    @Override
    public List<User> getAll() {
        db.collection(collectionName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            userList.clear();
                            userList.addAll(queryDocumentSnapshots.toObjects(User.class));

                            //Log.d(TAG, "onSuccess: " + d);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("gettingAllUsers", e.getMessage());
                    }
                });

        return userList;
    }
}
