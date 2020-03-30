package com.example.mamn01_inlamning1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private static final float ALPHA = 0.3f; // if ALPHA = 1 OR 0, no filter applies.
    private float[] sensorVals;
    TextView textView0;
    TextView textView1;
    TextView textView2;
    TextView textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        textView0 = findViewById(R.id.textView3);
        textView1 = findViewById(R.id.textView4);
        textView2 = findViewById(R.id.textView5);
        textView3 = findViewById(R.id.textView6);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorVals = lowPass(event.values.clone(), sensorVals);

            textView0.setText("X: " + String.valueOf(sensorVals[0]));
            textView1.setText("Y: " + String.valueOf(sensorVals[1]));
            textView2.setText("Z: " + String.valueOf(sensorVals[2]));
        }


        if(Math.abs(sensorVals[0]) > 9.0 && Math.abs(sensorVals[1]) < 2 && Math.abs(sensorVals[2]) < 2) {
            textView3.setText("Mobilen ligger på sidan");
        }
        else if(Math.abs(sensorVals[1]) > 9.0 && Math.abs(sensorVals[0]) < 2 && Math.abs(sensorVals[2]) < 2) {
            textView3.setText("Mobilen står på högkant");
        }
        else if(Math.abs(sensorVals[2]) > 9.0 && Math.abs(sensorVals[0]) < 2 && Math.abs(sensorVals[1]) < 2) {
            textView3.setText("Mobilen ligger ner");
        } else {
            textView3.setText("");
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
