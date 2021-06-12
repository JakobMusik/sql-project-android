package com.databaseproj.caltracker.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;

import com.databaseproj.caltracker.R;
import com.otaliastudios.cameraview.controls.Facing;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class LicenseActivity extends AppCompatActivity {

    CameraView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        camera = findViewById(R.id.license_camera);
        camera.setLifecycleOwner(this);
        camera.setFacing(Facing.FRONT);
        Button button = findViewById(R.id.buttontry);

        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NotNull final PictureResult result) {

                File fileDirectory = LicenseActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(fileDirectory, "agreement.bmp");

                result.toFile(file, new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {
                        Toast.makeText(LicenseActivity.this, "em", Toast.LENGTH_LONG).show();
                    }
                });

                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                camera.takePicture();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}