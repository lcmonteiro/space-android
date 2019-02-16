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
public class Decay {

	/**
	 *
	 */
	private final float __a;
	private final float __b;
	/**
	 *
	 */
	private float __v;

	/**
	 *
	 * @param factor
	 */
	public Decay(float factor, float init) {
		/**
		 * update
		 */
		__a = 1.0f - factor;
		__b = factor;
		__v = init;
	}

	public Decay(float factor) {
		/**
		 * update
		 */
		__a = 1.0f - factor;
		__b = factor;
		__v = 0.0f;
	}

	/**
	 *
	 */
	public void update(float v) {
		__v = __a * __v + __b * v;
	}
	/**
	 *
	 */
	public void set(float v) {
		__v = v;
	}
	/**
	 * get current value
	 * @return 
	 */
	public float get() {
		return __v;
	}

}
