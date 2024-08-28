package com.example.bringit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.bringit.Prevalent.Prevalent;
import com.example.bringit.model.GroupModel;
import com.example.bringit.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;

public class NewGroupActivity extends AppCompatActivity {

    EditText groupNameEt;

    Button nextBtn;

    DatabaseReference groupsRef;
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        groupNameEt = (EditText) findViewById(R.id.group_name_edit_text);

        nextBtn = (Button) findViewById(R.id.next_btn);
        groupsRef = FirebaseUtils.getGroupsRef();

        nextBtn.setOnClickListener((v->{

            groupName = groupNameEt.getText().toString();



            if(groupName.isEmpty() || groupName.length() < 3){
                groupNameEt.setError("group name length should be at least 3 chars");
                return;
            }


            String groupId = groupsRef.push().getKey();
            GroupModel group = new GroupModel(groupId, groupName);
            Prevalent.currentGroup = group;
            groupsRef.child(groupId).setValue(group);

            Intent intent = new Intent(NewGroupActivity.this,AddingGroupMembersActivity.class);



            startActivity(intent);

        }));

    }
}