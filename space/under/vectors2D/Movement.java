/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.vectors2D;

/**
 *
 * @author root
 */
public class Movement extends Position {

	/**
	 * constructor
	 */
	public Movement() {
		super(0, 0);
	}

	public Movement(float x, float y) {
		super(x, y);
	}

	public Movement(Direction dir, float vel) {
		this(dir, vel, 1);
	}

	public Movement(Direction dir, float ds, long dt) {
		_p = dir.getData();
		_s = ds;
	}

	/**
	 */
	public void decay(float factor) {
		_s *= (1.0f - factor);
	}
	/**
	 *
	 */
	public void rise(float factor) {
		_s /= (1.0f - factor);
	}
	/**
	 * update
	 */
	public Movement update(float s) {
		/**/
		_s = s;
		/**/
		return this;
	}
		
	@Override
	public Movement update(Movement m) {
		/**/
		super.update((Position) m);
		/**/
		return this;
	}

	/**
	 */
	@Override
	public Movement update(float x, float y) {
		/**/
		super.update(x, y);
		/**/
		return this;
	}

	/**
	 */
	public Movement update(Direction d, float s) {
		_s = s;
		/**/
		super.update(d);
		/**/
		return this;
	}

	/**
	 * add
	 */
	public Movement add(Movement m) {
		/**/
		super.update(m);
		/**/
		return this;
	}

	/**
	 * product
	 */
	public Movement product(float k) {
		/**/
		_s *= k;
		/**/
		return this;
	}
	
	/**
	 * invert
	 */
	@Override
	public Movement invert() {
		_p[Ases.X] *= -1;
		_p[Ases.Y] *= -1;
		return this;
	}

	public Movement invert_x() {
		_p[Ases.X] *= -1;
		return this;
	}

	public Movement invert_y() {
		_p[Ases.Y] *= -1;
		return this;
	}
}
