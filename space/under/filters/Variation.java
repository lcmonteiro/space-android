/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.filters;

/**
 * Variation
 * @author Luis Monteiro
 */
public class Variation {
	/**
	 */
	private float __curr_v;
	private long __curr_t;

	/**
	 *
	 * @param factor
	 */
	public Variation() {
		/**
		 * update
		 */
		__curr_v = 0;
		__curr_t = 0;
	}

	/**
	 */
	public float get(float v) {
		float diff = v - __curr_v;
		/**
		 * update
		 */
		__curr_v = v;
		/**
		 */
		__curr_t += 1;
		/**
		 * return
		 */
		return __curr_t > 1 ? diff : 0.0f;
	}
	/**
	 * reset
	 */
	public void reset(){
		__curr_v = 0;
		__curr_t = 0;
	}
}
