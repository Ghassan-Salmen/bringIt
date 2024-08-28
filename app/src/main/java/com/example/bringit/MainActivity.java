package com.example.bringit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bringit.Prevalent.Prevalent;
import com.example.bringit.model.GroupModel;
import com.example.bringit.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button joinGroupBtn;
    Button newGroupBtn;
    ProgressBar progressBar;
    TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinGroupBtn = (Button) findViewById(R.id.join_group_btn);
        newGroupBtn = (Button) findViewById(R.id.new_group_btn);
        progressText = (TextView) findViewById(R.id.progressText);
        progressBar = findViewById(R.id.progressBar);

        Paper.init(this);



        String groupIdString = Paper.book().read("groupId");
        if(groupIdString!=null && !groupIdString.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);

            FirebaseUtils.getGroupsRef().child(groupIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Prevalent.currentGroup = snapshot.getValue(GroupModel.class);
                    Intent intent = new Intent(MainActivity.this,AllOrdersActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

        }



        joinGroupBtn.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this,JoinGroupActivity.class);
            startActivity(intent);

        });

        newGroupBtn.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this,NewGroupActivity.class);
            startActivity(intent);

        });
    }
}