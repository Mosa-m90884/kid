package com.example.kiddo.Controller;

import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import android.content.res.AssetFileDescriptor;
import android.content.Context;

public class Predictor {
    private Interpreter tflite;

    public Predictor(Context context, String modelFilename) throws IOException {
        tflite = new Interpreter(loadModelFile(context, modelFilename));
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFilename) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public boolean predict(float age, float points) {
        float[][] inputData = {{age, points}};
        float[][] outputData = new float[1][1];
        tflite.run(inputData, outputData);
        return outputData[0][0] > 0.5;
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
        }
    }
}