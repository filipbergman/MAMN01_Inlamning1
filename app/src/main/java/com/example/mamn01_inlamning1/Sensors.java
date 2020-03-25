package com.example.mamn01_inlamning1;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Sensors extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView;
    private ImageView image;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private static final float ALPHA = 0.2f; // if ALPHA = 1 OR 0, no filter applies.
    private float[] sensorVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorVals = lowPass(event.values.clone(), sensorVals);

            TextView textView0 = findViewById(R.id.textView3);
            textView0.setText("X: " + String.valueOf(sensorVals[0]));
            TextView textView1 = findViewById(R.id.textView4);
            textView1.setText("Y: " + String.valueOf(sensorVals[1]));
            TextView textView2 = findViewById(R.id.textView5);
            textView2.setText("Z: " + String.valueOf(sensorVals[2]));
        }
    }

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + (ALPHA * (input[i] - output[i]));
        }
        return output;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
