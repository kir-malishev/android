package study.android.magicball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

	private static final double SHAKE_THRESHOLD_GRAVITY = 2.0;
	private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
	private long mShakeTimestamp;
	private OnShakeListener mListener;

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void setOnShakeListener(OnShakeListener listener) {
		this.mListener = listener;
	}

	public interface OnShakeListener {
		void onShake();
	}

	public ShakeDetector(Context context) {
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);  
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		float gX = x / SensorManager.GRAVITY_EARTH;
		float gY = y / SensorManager.GRAVITY_EARTH;
		float gZ = z / SensorManager.GRAVITY_EARTH;

		double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

		if (gForce > SHAKE_THRESHOLD_GRAVITY) {
			final long now = System.currentTimeMillis();

			if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS > now) {
				return;
			}
			mShakeTimestamp = now;
			if (mListener != null)
			{
			    mListener.onShake();
			}    

		}
	}
}
