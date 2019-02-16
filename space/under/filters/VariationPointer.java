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
public class VariationPointer {

	/**
	 */
	private final Variation __x;
	private final Variation __y;
	/**
	 */
	private float __x_var = 0;
	private float __y_var = 0;

	/**
	 */
	public VariationPointer() {
		/**
		 * update
		 */
		__x = new Variation();
		__y = new Variation();
	}

	public void update(float x, float y) {
		float factor = (float) Math.cos(y);
		/**/
		__x_var = __x.get(x) * factor;
		__y_var = __y.get(y) * Math.abs(factor);
	}

	/**
	 */
	public float get_x() {
		return __x_var;
	}

	public float get_y() {
		return __y_var;
	}

	/**
	 * reset
	 */
	public void reset() {
		__x.reset();
		__y.reset();
	}
}
