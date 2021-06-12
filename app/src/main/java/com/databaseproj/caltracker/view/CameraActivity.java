package com.databaseproj.caltracker.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.databaseproj.caltracker.R;
import com.github.clans.fab.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    FloatingActionButton fab;
    CameraView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        fab = findViewById(R.id.fab);

        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);
        camera.setFacing(Facing.BACK);

        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NotNull final PictureResult result) {
                // Picture was taken!
                // If planning to show a Bitmap, we will take care of
                // EXIF rotation and background threading for you...
                /*result.toBitmap(new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        Toast.makeText(CameraActivity.this, "Picture taken",
                                Toast.LENGTH_SHORT).show();

                    }
                });*/

                File fileDirectory = CameraActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(fileDirectory, "image.bmp");

                result.toFile(file, new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {
                        Toast.makeText(CameraActivity.this, "Save pic succeeded",
                                Toast.LENGTH_SHORT).show();
                    }
                });


                finish();


                // If planning to save a file on a background thread,
                // just use toFile. Ensure you have permissions.

                // Access the raw data if needed.
                //byte[] data = result.getData();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture();
            }
        });
    }

}