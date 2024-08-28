package com.example.bringit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.bringit.Prevalent.Prevalent;
import com.example.bringit.model.GroupModel;
import com.example.bringit.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import io.paperdb.Paper;

public class JoinGroupActivity extends AppCompatActivity {

    Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        Paper.init(this);

        scanBtn = (Button) findViewById(R.id.qr_scan_btn);

        scanBtn.setOnClickListener(view -> {

            scanCode();
        });


    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result -> {

        if(result.getContents() != null){

            Log.e("tag","notscanning");
            FirebaseUtils.getGroupsRef().child(result.getContents()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Prevalent.currentGroup = snapshot.getValue(GroupModel.class);

                    Paper.book().write("groupId", Prevalent.currentGroup.getGroupId());
                    Intent intent = new Intent(JoinGroupActivity.this,AllOrdersActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(getApplicationContext(), "this group is not valid", Toast.LENGTH_SHORT).show();

                }
            });

        }
    });
}