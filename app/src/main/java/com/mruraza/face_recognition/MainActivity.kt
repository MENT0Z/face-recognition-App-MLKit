package com.mruraza.face_recognition

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn_to_detect_face)
        btn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager)!=null){
                startActivityForResult(intent,1)
            }else{
                 Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT ).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            val allextras = data?.extras
            val bitmap = allextras?.get("data") as? Bitmap
            if (bitmap != null) {
                detectmyface(bitmap)
            }
        }
    }

    fun detectmyface(bitmap: Bitmap){
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(highAccuracyOpts)
        val image = InputImage.fromBitmap(bitmap,0)
        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                var resultext = " "
                var i = 1
                for (face in faces) {
                    resultext =
                        "Face Number = $i" + "\n Smile = ${face.smilingProbability?.times(100)}%" +
                                "\n Left Eye Open = ${face.leftEyeOpenProbability?.times(100)}%" +
                                "\n Right Eye Open = ${face.rightEyeOpenProbability?.times(100)}%\n"
                    i++
                }
                if(faces.isEmpty()){

                    Toast.makeText(this,"stg wrong",Toast.LENGTH_LONG).show()
                }else{

                    Toast.makeText(this,resultext,Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"its not working fine",Toast.LENGTH_SHORT).show()
            }
    }
}