/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 * imports
 */
import android.content.Context;
import space.base.Module;
import space.base.Data;
import space.base.AppI;
/**
 *
 */
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author monteiro
 */
public class MoveI extends Module implements SensorEventListener {

	/**
	 * Data Types
	 */
	public static class MoveData extends Data {

		protected float[] _p;

		public MoveData(long t, float x, float y, float z) {
			super(t);
			/**
			 *
			 */
			_p = new float[]{x, y, z};
		}

		/**
		 *
		 * @return
		 */
		public float getX() {
			return _p[0];
		}

		public float getY() {
			return _p[1];
		}

		public float getZ() {
			return _p[2];
		}

		/**
		 *
		 * @param val
		 */
		public float[] getPoint() {
			return _p;
		}
	}

	public static class Accelerometer extends MoveData {

		public Accelerometer(Long t, float x, float y, float z) {
			super(t, x, y, z);
		}
	}

	public static class Gyroscope extends MoveData {

		public Gyroscope(Long t, float x, float y, float z) {
			super(t, x, y, z);
		}
	}

	public static class Direction extends MoveData {

		public Direction(Long t, float x, float y, float z) {
			super(t, x, y, z);
		}
	}

	public static class Gravity extends MoveData {

		public Gravity(Long t, float x, float y, float z) {
			super(t, x, y, z);
		}
	}

	public static class Magnetic extends MoveData {

		public Magnetic(Long t, float x, float y, float z) {
			super(t, x, y, z);
		}
	}
	/**
	 * Definitions
	 */
	static final long MIN_DELAY = 10000000;
	/**
	 * declare system interfaces
	 */
	final protected SensorManager _sensorM;

	final protected List<Sensor> _sensors;

	final protected int[] _sensorID;

	final protected int _delay;

	/**
	 *
	 */
	public MoveI(AppI app, int delay, int[] sensorID) {
		super(app, 200);
		/**
		 * get sensor manager
		 */
		_sensorM = (SensorManager) app.getContext().getSystemService(Context.SENSOR_SERVICE);
		/**
		 *
		 */
		_sensors = new ArrayList<Sensor>();
		/**
		 */
		_sensorID = sensorID;
		/**
		 */
		_delay = delay;
		/**
		 */
		for (int id : sensorID) {
			Sensor defaultSensor = _sensorM.getDefaultSensor(id);
			if (defaultSensor == null) {
				logger(LogType.WARNING, "sensor id not found (id=" + id + ")");
			}
			if (!_sensors.add(defaultSensor)) {
				logger(LogType.WARNING, "sensor id not added (id=" + id + ")");
			}
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer play(Integer retries) {
		/**
		 *
		 */
		int i = 0;
		for (Sensor s : _sensors) {
			if (!_sensorM.registerListener(this, s, _delay)) {
				logger(LogType.WARNING, "Register Listener Fail");
				/**
				 */
				send(new LogEntry(LogEntry.Type.WARNING, __to_name[_sensorID[i]] + " Sensor"), 1);
				/**
				 * unregister
				 */
				_sensorM.unregisterListener(this);
				/**
				 * try again
				 */
				return (retries + 1) * 1000;
			}
			i++;
		}
		/**
		 *
		 */
		return super.play(retries);
	}

	@Override
	protected Integer pause(Integer retries) {
		/**
		 *
		 */
		_sensorM.unregisterListener(this);
		/**
		 *
		 */
		return super.pause(retries);
	}

	/**
	 *
	 * @param sensor
	 * @param accuracy
	 */
	@Override
	public final void onAccuracyChanged(Sensor s, int accuracy) {
		Log.d("game", "a =" + accuracy + " s" + s.getName());
		logger(LogType.INFO,
			"Accuracy Changed (accur=" + accuracy + " vendor=" + s.getVendor() + " version=" + s.getVersion() + ")"
		);
	}

	/**
	 * sensor data source
	 */
//	float __gyro_bias[] = {0.0f, -0.005f, -0.023f};
//	float __gyro_k[] = {1.0f, 1.0f, 1.0f};
	/**
	 */
	private long __gyr_time_ref = 0;
	private long __acc_time_ref = 0;
	private long __mag_time_ref = 0;
	private long __ori_time_ref = 0;
	private long __gra_time_ref = 0;

	/**
	 */
	@Override
	public final synchronized void onSensorChanged(SensorEvent event) {
		/**
		 *
		 */
		switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER: {
//				logger(LogType.INFO, "acc t=" + (long) (event.timestamp * 0.000001) + " t1=" + SystemClock.elapsedRealtime() + " t2=" + System.currentTimeMillis() + " t3=" + SystemClock.uptimeMillis());
//				logger(LogType.INFO, "acc " + event.values[0]);
				if (__acc_time_ref < event.timestamp) {
					send(new Accelerometer(
						(long) (event.timestamp * 0.000001),
						event.values[0],
						event.values[1],
						event.values[2]
					));
					__acc_time_ref = event.timestamp + MIN_DELAY;
				}
				break;
			}
			case Sensor.TYPE_GYROSCOPE: {
//				logger(LogType.INFO, "gyr t=" + (long) (event.timestamp * 0.000001) + " t1=" + SystemClock.elapsedRealtime() + " t2=" + System.currentTimeMillis() + " t3=" + SystemClock.uptimeMillis());
//				logger(LogType.INFO, "gyro " + event.values[0]);
				if (__gyr_time_ref < event.timestamp) {
					send(new Gyroscope(
						(long) (event.timestamp * 0.000001),
//						(event.values[0] - __gyro_bias[0]) * __gyro_k[0],
//						(event.values[1] - __gyro_bias[1]) * __gyro_k[1],
//						(event.values[2] - __gyro_bias[2]) * __gyro_k[2]
						event.values[0],
						event.values[1],
						event.values[2]
					));
					__gyr_time_ref = event.timestamp + MIN_DELAY;
				}
				break;
			}
			case Sensor.TYPE_ORIENTATION: {
				if (__ori_time_ref < event.timestamp) {
					send(new Direction(
						(long) (event.timestamp * 0.000001),
						event.values[0],
						event.values[1],
						event.values[2]
					));
					__ori_time_ref = event.timestamp + MIN_DELAY;
				}
				break;
			}
			case Sensor.TYPE_GRAVITY: {
				if (__gra_time_ref < event.timestamp) {
					send(new Gravity(
						(long) (event.timestamp * 0.000001),
						event.values[0],
						event.values[1],
						event.values[2]
					));
					__gra_time_ref = event.timestamp + MIN_DELAY;
				}
				break;
			}
			case Sensor.TYPE_LINEAR_ACCELERATION: {

				if (__acc_time_ref < event.timestamp) {
					send(new Accelerometer(
						(long) (event.timestamp * 0.000001),
						event.values[0],
						event.values[1],
						event.values[2]
					));
					__acc_time_ref = event.timestamp + MIN_DELAY;
				}
				break;
			}
			case Sensor.TYPE_MAGNETIC_FIELD: {
				if (__mag_time_ref < event.timestamp) {
					send(new Magnetic(
						(long) (event.timestamp * 0.000001),
						event.values[0],
						event.values[1],
						event.values[2]
					));
					__mag_time_ref = event.timestamp + MIN_DELAY;
				}
				break;
			}
		}
	}
	/**
	 * convert id to string
	 */
	String[] __to_name = {
		"Unknown",
		"Accelerometer",
		"Magnetic",
		"Orientation",
		"Gyroscope",
		"Light",
		"Pressure",
		"Temperature",
		"Proximity",
		"Gravity",
		"Unknown",
		"Unknown",
		"Unknown",
		"Unknown",
		"Unknown",
		"Unknown",
		"Unknown"
	};
}
