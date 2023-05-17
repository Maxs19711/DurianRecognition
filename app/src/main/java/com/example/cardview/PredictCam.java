package com.example.cardview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.cardview.ml.TfModelReal;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PredictCam extends AppCompatActivity {

    private ImageView imageView;
    View view;
    Bitmap photo;
    private String currentphotopath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict_cam);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(imageFileName, ".jpg", storageDirectory);
            currentphotopath = imageFile.getAbsolutePath();
            Uri imageuri = FileProvider.getUriForFile(PredictCam.this,
                    "com.example.cardview.fileprovider", imageFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
            startActivityForResult(intent, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        view = findViewById(R.id.included);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ProgressButton progressButton = new ProgressButton(PredictCam.this, view);
                    progressButton.buttonActivated();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                       public void run() {
                           progressButton.buttonFinished();
                           showBottomSheetDialog();
                      }
                     }, 3000);
                }
                catch (Exception e)
                {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Error: "+ e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            photo = BitmapFactory.decodeFile(currentphotopath);
            imageView = (ImageView) findViewById(R.id.imgVCamera);
            imageView.setImageBitmap(photo);
        }
    }

    private void showBottomSheetDialog()
    {
        photo = Bitmap.createScaledBitmap(photo, 78, 104,true);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        MaterialTextView text = bottomSheetDialog.findViewById(R.id.textView1);
        try {
            TfModelReal model = TfModelReal.newInstance(getApplicationContext());
            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(photo);
            ByteBuffer byteBuffer = tensorImage.getBuffer();

            //Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 104, 78, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            //Runs model inference and gets result.
            TfModelReal.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            //Releases model resources if no longer used.
            model.close();
            float Bmerah = outputFeature0.getFloatArray()[0];
            float ioi = outputFeature0.getFloatArray()[1];
            float king = outputFeature0.getFloatArray()[2];
            if(Bmerah>0 && Bmerah>ioi && Bmerah>king)
                text.setText("This image most likely belongs to durian bunga merah");
            else if(ioi>0 || ioi>1 && ioi>Bmerah && ioi>king)
                text.setText("This image most likely belongs to durian IOI");
            else if(king>0 || king>2 && king>Bmerah && king>ioi)
                text.setText("This image most likely belongs to durian musang king");
            else
                text.setText("Unrecognizable");
            bottomSheetDialog.show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error: "+ e, Toast.LENGTH_LONG).show();
        }
    }
}