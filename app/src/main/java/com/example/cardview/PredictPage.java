package com.example.cardview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cardview.ml.TfModelReal;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PredictPage extends AppCompatActivity{
    ImageView imageView;
    private Bitmap img;

    View view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict_page);
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
        imageView = (ImageView) findViewById(R.id.imgVGallery);
        view2 = findViewById(R.id.included2);

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressButton progressButton = new ProgressButton(PredictPage.this, view);
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            imageView.setImageURI(data.getData());
            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void showBottomSheetDialog()
    {
        img = Bitmap.createScaledBitmap(img, 78, 104,true);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        MaterialTextView text = bottomSheetDialog.findViewById(R.id.textView1);
        try {
            TfModelReal model = TfModelReal.newInstance(getApplicationContext());
            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(img);
            ByteBuffer byteBuffer = tensorImage.getBuffer();

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 104, 78, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            TfModelReal.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Releases model resources if no longer used.
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