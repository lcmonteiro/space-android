/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.filters;
/**
 * @author root
 */
public class Curve {

	/**
	 *
	 */
	private final float __b;
	private final float __o_x;
	private final float __o_y;
	private final float __n;
	private final float __min;
	private final float __max;

	/**
	 *
	 */
	public Curve(float order, float origin[], float reference[], float limits[]) {
		/**
		 * update
		 */
		__b = reference[1] / (float) Math.pow(reference[0], order);
		__o_x = origin[0];
		__o_y = origin[1];
		__n = order;
		__min = limits[0];
		__max = limits[1];
	}

	public Curve(float order, float reference[], float limits[]) {
		/**
		 * update
		 */
		this(order, new float[]{0.0f, 0.0f}, reference, limits);
	}

	public Curve(float order, float reference[]) {
		/**
		 * update
		 */
		this(order, new float[]{0.0f, 0.0f}, reference, new float[]{-1000000000000.0f, 1000000000000.0f});
	}

	/**
	 *
	 */
	public float process(float v) {
		float r;
		/**
		 *
		 */
		if (v >= __o_x) {
			r = (__b * (float) Math.pow((v - __o_x), __n)) + __o_y;
		} else {
			r = __o_y - (__b * (float) Math.pow((__o_x - v), __n));
		}
		/**
		 * limits
		 */
		if (r < __min) {
			return __min;
		}
		if (r > __max) {
			return __max;
		}
		return r;
	}
}
