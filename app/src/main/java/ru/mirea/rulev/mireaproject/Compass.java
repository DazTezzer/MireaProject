package ru.mirea.rulev.mireaproject;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.hardware.Sensor;

import ru.mirea.rulev.mireaproject.databinding.FragmentSensorBinding;

public class Compass extends Fragment implements SensorEventListener{

    private ImageView HeaderImage;
    private float RotateDegree = 0f;
    private SensorManager mSensorManager;
    TextView CompOrient;
    private FragmentSensorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSensorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HeaderImage = binding.CompassView;
        CompOrient = binding.Header;
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        float degree = Math.round(event.values[0]);
        CompOrient.setText("Отклонение от севера: " + Float.toString(degree) + " градусов");

        RotateAnimation rotateAnimation = new RotateAnimation(
                RotateDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        rotateAnimation.setDuration(200);

        rotateAnimation.setFillAfter(true);

        HeaderImage.startAnimation(rotateAnimation);
        RotateDegree = -degree;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}