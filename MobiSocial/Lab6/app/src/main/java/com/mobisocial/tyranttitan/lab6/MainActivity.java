package com.mobisocial.tyranttitan.lab6;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


public class MainActivity extends AppCompatActivity {
    private final int RC_PICTURE_TAKEN = 1111;
    private final int RC_PERMISSIONS = 2222;
    private int numberOfFace = 5;
    private FaceDetector detector;
    boolean started = false;
    float myEyesDistance;
    int numberOfFaceDetected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Bundle extras = getIntent().getExtras();
            boolean fromNewActivity = Boolean.parseBoolean(extras.getString("fromNewActivity"));
            if (fromNewActivity) {
                started = false;
            }
        } catch (Exception e)
        {

        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, RC_PERMISSIONS);
            finish();
        }
        detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    public void setStated ()
    {
        started = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICTURE_TAKEN && resultCode == RESULT_OK) {
// the newly taken photo is now stored in a Bitmap object
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
// handle the facial recognition etc. here
            FaceDetector detector = new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    .setProminentFaceOnly(true).build();
            Detector<Face> safeDetector = new SafeFaceDetector(detector);
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Face> faces = safeDetector.detect(frame);
            Log.d("Titan", "faces detected: " + faces.size());
            if (faces.size() != 1) {
                Toast.makeText(this, "Only one face is allow", Toast.LENGTH_SHORT).show();
            }
            else {
                Face face = faces.valueAt(0);
                if (face.getIsSmilingProbability()>0.7) {
                    Intent newIntent = new Intent(this, NewActivity.class);
                    Log.d("Titan","GOOD");
                    Toast.makeText(this, "Good!", Toast.LENGTH_SHORT).show();
                    detector.release();
                    bitmap.recycle();
                    started = true;
                    startActivity(newIntent);

                }
                else {
                    Toast.makeText(this, "Keep smiling, my friend!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        if (requestCode == RC_PERMISSIONS && resultCode == RESULT_OK) {
// restart the activity if you arrive here from the permission dialog
            Intent reboot = new Intent(this, MainActivity.class);
            startActivity(reboot);
        }
    }



    public void ScanFace(View view) {
        if (!started) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, RC_PICTURE_TAKEN);
        }
    }
}

