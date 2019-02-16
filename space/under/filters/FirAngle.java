/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.filters;

/**
 *
 * @author root
 */
public class FirAngle {

	/**
	 *
	 */
	private final static float __2PI = 6.283185307f;
	private final static float __PI = 3.141592654f;
	/**
	 */
	private static final int INIT = -2;
	/**
	 *
	 */
	private final float __b[];
	/**
	 *
	 */
	private final float __a[];
	/**
	 *
	 */
	private int __i = INIT;
	/**
	 */
	private float __v = 0;

	/**
	 *
	 * @param factor
	 */
	public FirAngle(float factors[], float init[]) {
		/**
		 * update
		 */
		__b = factors;
		/**
		 */
		__a = init;
		/**
		 */
		__i = 0;
		/**
		 */
		__v = value();
	}

	public FirAngle(float factors[]) {
		/**
		 * update
		 */
		__b = factors;
		/**
		 */
		__a = new float[__b.length];
	}

	/**
	 *
	 */
	public void update(float v) {
		/**
		 * first time
		 */
		if (__i == INIT) {
			for (int i = 0; i < __a.length; ++i) {
				__a[i] = v;
			}
			__i = 0;
		} else {
			/**
			 * decrement index
			 */
			__i--;
			/**
			 * check index
			 */
			if (__i < 0) {
				__i = __b.length - 1;
			}
			/**
			 * update
			 */
			__a[__i] = scope(v);
		}
		__v = value();
	}

	/**
	 * get current value
	 *
	 * @return
	 */
	public float get() {
		return __v;
	}

	/**
	 *
	 */
	public void reset() {
		__i = INIT;
	}

	/**
	 *
	 */
	public boolean ready() {
		return __i != INIT;
	}

	/**
	 *
	 */
	private float value() {
		float result = 0.0f;
		/**
		 */
		int i_b = 0;
		for (int i_v = __i; i_v < __b.length; ++i_v, ++i_b) {
			result += (__a[i_v] * __b[i_b]);
		}
		for (int i_v = 0; i_v < __i; ++i_v, ++i_b) {
			result += (__a[i_v] * __b[i_b]);
		}
		/**
		 */
		return result;
	}

	private float scope(float v) {
		float diff = v - (float) __v;
		/**
		 * process value
		 */
		if (diff < -__PI) {
			return v + __2PI;
		}
		if (diff > __PI) {
			return v - __2PI;
		}
		return v;
	}

	private float center(float v) {
		/**
		 * process value
		 */
		if (v < -__2PI) {
			return v + __2PI;
		}
		if (v > __2PI) {
			return v - __2PI;
		}
		return v;
	}
}
