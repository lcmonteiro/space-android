/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.motorII;

import space.base.AppI;
import space.base.Data;
import space.layerI.SerialI;

/**
 *
 * @author root
 */
public class MotorArduino extends SerialI {

	/*
	 * Data Types
	 */
	public static class Power extends Data {

		protected byte[] _b;

		public Power(float nw, float ne, float se, float sw) {
			super();
			/**
			 *
			 */
			_b = new byte[]{
				(byte) (nw * 2.56), (byte) (ne * 2.56), (byte) (se * 2.56), (byte) (sw * 2.56)
			};
		}

		/**
		 *
		 * @param val
		 */
		public byte[] getData() {
			return _b;
		}

		/**
		 *
		 */
		@Override
		public String toString() {
			return "" + _b[0] + " " + _b[1] + " " + _b[2] + " " + _b[3];
		}
	}

	/**
	 *
	 */
	public static class Energy extends Data {

		protected Float _e;

		public Energy(byte high, byte low) {
			super();
			/**
			 *
			 */
			_e = (float) (int) ((int) (high << 8) | (int) (low));
		}

		/**
		 *
		 * @param val
		 */
		public Float getData() {
			return _e;
		}
	}

	/**
	 *
	 * @param log
	 */
	public MotorArduino(AppI app) {
		super(app, 9600);
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onInit(Data data) {
		if (data instanceof Power) {
			Power p = ((Power) data);
			/**
			 */
			logger(
				LogType.INFO, "m= " + p.toString()
			);
		}
	}
	/**
	 *
	 * @param data
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof Power) {
			Power p = ((Power) data);
			/**
			 */
			logger(
				LogType.INFO, "m= " + p.toString()
			);
			sendData(p.getData());
		}
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onNewData(byte[] data) {
		logger(
			LogType.INFO, "new data"
		);
	}

}
