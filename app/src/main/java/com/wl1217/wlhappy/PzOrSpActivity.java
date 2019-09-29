package com.wl1217.wlhappy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKitView;
import com.jpegkit.Jpeg;
import com.jpegkit.JpegImageView;

public class PzOrSpActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private JpegImageView imageView;
    public static Intent getInstance(Context context) {
        return new Intent(context, PzOrSpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pz_or_sp);

        cameraKitView = findViewById(R.id.camera);
        imageView = findViewById(R.id.imageView);

        findViewById(R.id.photoBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("wg", "onClick: -------------");
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView view, final byte[] photo) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Jpeg jpeg = new Jpeg(photo);
                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setJpeg(jpeg);
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
