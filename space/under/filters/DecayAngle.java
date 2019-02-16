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
public class DecayAngle {

	/**
	 *
	 */
	private final static float __2PI = 6.283185307f;
	private final static float __PI = 3.141592654f;
	private final static float __1PI2 = 1.570796327f;
	/**
	 *
	 */
	private final float __a;
	private final float __b;
	/**
	 *
	 */
	private Float __v;

	/**
	 *
	 * @param factor
	 */
	public DecayAngle(float factor, float init) {
		/**
		 * update
		 */
		__a = 1.0f - factor;
		__b = factor;
		__v = init;
	}

	public DecayAngle(float factor) {
		/**
		 * update
		 */
		__a = 1.0f - factor;
		__b = factor;
		__v = null;
	}

	/**
	 *
	 */
	public void update(float v) {
		if (__v == null) {
			/**
			 */
			__v = v;
		} else {
			/**
			 */
			__v = center(__a * __v + __b * scope(v));
		}
	}

	/**
	 *
	 */
	public void reset() {
		__v = null;
	}
	
	/**
	 *
	 */
	public boolean ready() {
		return __v != null;
	}
	
	/**
	 *
	 */
	public Float get() {
		return __v;
	}

	/**
	 *
	 */
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
