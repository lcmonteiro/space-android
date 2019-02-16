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
public class Line {

	/**
	 *
	 */
	private final float __b;
	private final float __o_x;
	private final float __o_y;
	private final float __min;
	private final float __max;

	/**
	 *
	 */
	public Line(float origin[], float reference[], float limits[]) {
		/**
		 * update
		 */
		__b = reference[1] / reference[0];
		__o_x = origin[0];
		__o_y = origin[1];
		__min = limits[0];
		__max = limits[1];
	}

	public Line(float reference[], float limits[]) {
		/**
		 * update
		 */
		this(new float[]{0.0f, 0.0f}, reference, limits);
	}

	public Line(float reference[]) {
		/**
		 * update
		 */
		this(new float[]{0.0f, 0.0f}, reference, new float[]{-1000000000000.0f, 1000000000000.0f});
	}

	/**
	 */
	public float process(float v) {
		float r;
		/**
		 *
		 */
		if (v >= __o_x) {
			r = (__b * (v - __o_x)) + __o_y;
		} else {
			r = __o_y - (__b * (__o_x - v));
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
