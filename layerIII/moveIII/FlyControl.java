/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerIII.moveIII;

/**
 * imports
 */
import space.base.AppI;
import space.base.Data;
import space.base.Module;
import space.layerII.motorII.MotorArduino;
import space.layerII.touchII.TouchControl.PowerDir;

/**
 *
 * @author monteiro
 */
public class FlyControl extends Module {

	/**
	 *
	 */
	private final float[] __power_motor;
	/**
	 *
	 */
	private PowerDir __last_power;

	/**
	 *
	 */
	public FlyControl(AppI app) {
		super(app, 20);
		/**
		 */
		__power_motor = new float[4];

		/**
		 */
		__last_power = null;
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof PowerDir) {
			PowerDir power = (PowerDir) data;
			/**
			 *
			 */
			logger(
				LogType.INFO,
				"x= " + power.getX()
				+ " y= " + power.getY()
				+ " z= " + power.getZ()
			);
			/**
			 * control
			 */
			float[] power_motor = Feedforward(power);
			/**
			 *
			 */
			__last_power = power;
			/**
			 *
			 */
			logger(
				LogType.INFO,
				"p1= " + power_motor[0]
				+ " p2= " + power_motor[1]
				+ " p3= " + power_motor[2]
				+ " p4= " + power_motor[3]
			);
			/**
			 * send
			 */
			send(new MotorArduino.Power(
				power_motor[0],
				power_motor[1],
				power_motor[2],
				power_motor[3]
			));
		}
	}

	/**
	 *
	 */
	float[] Feedforward(PowerDir power) {
		/**
		 * process Z
		 */
		if (__last_power != null && !power.isEmpty()) {
			for (int i = 0; i < __power_motor.length; i++) {
				__power_motor[i] += power.getZ() - __last_power.getZ();
			}
		}
		/**
		 * normalization
		 */
		Normalization(__power_motor);
		/**
		 * change variable
		 */
		float[] power_motor = __power_motor.clone();
		/**
		 * process X
		 */
		for (int i = 0, m = 0, s = (power_motor.length >> 1); i < power_motor.length; i++, m++) {

			if (m >= power_motor.length) {
				m = 0;
			}
			if (i < s) {
				power_motor[m] += power.getX() * 0.5;
			} else {
				power_motor[m] -= power.getX() * 0.5;
			}
		}
		/**
		 * process Y
		 */
		for (int i = 0, m = 1, s = (power_motor.length >> 1); i < power_motor.length; i++, m++) {

			if (m >= power_motor.length) {
				m = 0;
			}
			if (i < s) {
				power_motor[m] += power.getY() * 0.5;
			} else {
				power_motor[m] -= power.getY() * 0.5;
			}
		}
		/**
		 * normalization
		 */
		Normalization(power_motor);
		/**
		 *
		 */
		return power_motor;
	}

	/**
	 *
	 */
	void Normalization(float[] power_motor) {
		float min = 0;
		/**
		 */
		for (Float p : power_motor) {
			if (p < min) {
				min = p;
			}
		}
		if (min < 0.0) {
			/**
			 */
			for (int i = 0; i < power_motor.length; i++) {
				power_motor[i] -= min;
			}
		}
		/**
		 */
		float max = 0;
		/**
		 */
		for (Float p : power_motor) {
			if (p > max) {
				max = p;
			}
		}
		if (max > 100.0) {
			float factor = 100 / max;
			/**
			 *
			 */
			for (int i = 0; i < power_motor.length; i++) {
				power_motor[i] *= factor;
			}
		}
	}
}
