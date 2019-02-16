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
public class Fir {

	private static final int INIT = -2;
	/**
	 *
	 */
	private final float __b[];
	/**
	 *
	 */
	private final float __v[];
	/**
	 *
	 */
	private int __i = INIT;

	/**
	 *
	 * @param factor
	 */
	public Fir(float factors[], float init[]) {
		/**
		 * update
		 */
		__b = factors;
		/**
		 */
		__v = init;
		/**
		 */
		__i = 0;
	}

	public Fir(float factors[]) {
		/**
		 * update
		 */
		__b = factors;
		/**
		 */
		__v = new float[__b.length];
	}

	/**
	 *
	 */
	public void update(float v) {
		/**
		 * first time
		 */
		if (__i == INIT) {
			for (int i = 0; i < __v.length; ++i) {
				__v[i] = v;
			}
		}
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
		__v[__i] = v;
	}

	/**
	 * get current value
	 *
	 * @return
	 */
	public float get() {
		float result = 0.0f;
		/**
		 */
		int i_b = 0;
		for (int i_v = __i; i_v < __b.length; ++i_v, ++i_b) {
			result += (__v[i_v] * __b[i_b]);
		}
		for (int i_v = 0; i_v < __i; ++i_v, ++i_b) {
			result += (__v[i_v] * __b[i_b]);
		}
		return result;
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

}
