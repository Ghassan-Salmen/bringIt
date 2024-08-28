package com.example.bringit.utils;
import com.example.bringit.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static DatabaseReference getRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getGroupsRef() {
        return getRootRef().child("groups");
    }

    public static DatabaseReference getMembersRef(String groupId) {

        return getRootRef().child("groups").child(groupId).child("members");
    }

    public static DatabaseReference getOrdersRef(String groupId) {

        return getGroupsRef().child(groupId).child("orders");
    }




}
