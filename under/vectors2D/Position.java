/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.vectors2D;
/**
 * Position
 * @author Luis Monteiro
 */
public class Position extends Direction {
	/**
	 * space
	 */
	protected float _s;
	/**
	 * constructor
	 */
	public Position(float x, float y) {
		set(x, y);
	}
	public Position(float[] p, long t) {
		this(p[Ases.X], p[Ases.Y]);
	}
	public Position(Vector v, long t) {
		this(v._p, t);
	}
	public Position() {
		this(0, 0);
	}
	/**
	 * Set Values
	 */
	private void set(float x, float y) {
		/**
		 * set space distance
		 */
		_s = length(x, y);
		/**
		 * set direction
		 */
		if (_s == 0) {
			_p[Ases.X] = 1;
			_p[Ases.Y] = 0;
		} else {
			_p[Ases.X] = x/_s;
			_p[Ases.Y] = y/_s;
		}
	}
	/**
	 * 
	 */
	@Override
	public Position sum(float x, float y) {
		set(
			getX() + x,
			getY() + y
		);
		return this;
	}
	/**
	 * update position
	 */
	public Position update(Movement m) {
		set(
			getX() + m.getX(),
			getY() + m.getY()
		);
		return this;
	}
	public Position update(Position p) {
		_s = p._s;
		_p[Ases.X] = p._p[Ases.X];
		_p[Ases.Y] = p._p[Ases.Y];
		return this;
	}
	@Override
	public Position update(float x, float y) {
		set(x, y);
		return this;
	}
	/**
	 * get space
	 */
	public float space() {
		return _s;
	}
	/**
	 * get x
	 */
	@Override
	public float getX() {
		return _s * _p[Ases.X];
	}
	/**
	 * get y
	 */
	@Override
	public float getY() {
		return _s * _p[Ases.Y];
	}
}
