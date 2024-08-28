package com.example.bringit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.bringit.Prevalent.Prevalent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GroupShareActivity extends AppCompatActivity {

    String groupId;
    ImageView qrImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_share);

        qrImageView = (ImageView) findViewById(R.id.qr_code_image);
        groupId = Prevalent.currentGroup.getGroupId();
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix matrix = writer.encode(groupId, BarcodeFormat.QR_CODE,800,800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
}