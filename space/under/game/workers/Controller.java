/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.workers;
/**
 */
import android.graphics.Rect;
/**
 */
import space.under.game.vectors.Movement;
/**
 * Controller
 * @author Luis Monteiro
 */
public class Controller extends Movement {

	private final float __max;
	/**
	 */
	public Controller(Rect region, float coef) {
		super();
		/**
		 */
		__max = Math.min(region.height(), region.width()) * coef;
	}
	/**
	 *
	 */
	@Override
	public Movement update(float x, float y) {
		return (Movement) super.update(process(x), process(y));
	}
	/**
	 *
	 */
	private float process(float val) {
		if (val < 0) {
			return (__max / (1 - val)) - __max;
		}
		return __max - (__max/ (val + 1));
	}
}
