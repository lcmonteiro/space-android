/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.vectors;
/**
 * Movement
 * @author Luis Monteiro
 */
public class Movement extends Vector {
	/**
	 * constructor
	 */
	public Movement() {
		super(0, 0);
	}
	public Movement(Direction dir, float vel) {
		super(dir._x * vel, dir._y * vel);
	}
	public Movement(float x, float y) {
		super(x, y);
	}
	/**
	 */
	public void decay(float factor) {
		_x *= (1.0f - factor);
		_y *= (1.0f - factor);
	}
	/**
	 */
	public void rise(float factor) {
		_x /= (1.0f - factor);
		_y /= (1.0f - factor);
	}
	/**
	 * update
	 */
	public Movement update(Direction d, float s) {
		/**/
		super.update(d._x * s, d._y * s);
		/**/
		return this;
	}
	public Movement update(Movement m) {
		/**/
		super.update(m._x, m._y);
		/**/
		return this;
	}
	public Movement update(float s) {
		/**/
		product(s / length());
		/**/
		return this;
	}
	/**
	 * get speed
	 */
	public float speed() {
		return length();
	}
}
