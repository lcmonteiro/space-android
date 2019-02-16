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
public class Vector {

	/**
	 * meta data
	 */
	public static class Ases {

		public static final int X = 0;
		public static final int Y = 1;
	}
	/**
	 * data container
	 */
	protected float[] _p;
	/*
	 * constructors
	 */

	public Vector(float x, float y) {
		/*
		 */
		_p = new float[]{x, y};
	}

	public Vector(float[] p) {
		/*
		 */
		_p = p;
	}

	public Vector() {
		/*
		 */
		set(0, 0);
	}

	/**
	 * Set Values
	 */
	private void set(float x, float y) {
		/*
		 */
		_p = new float[]{x, y};
	}

	/**
	 * Get values
	 *
	 * @return
	 */
	public float getX() {
		return _p[Ases.X];
	}

	public float getY() {
		return _p[Ases.Y];
	}

	/**
	 *
	 */
	public float[] getData() {
		return _p;
	}

	/**
	 * vector length
	 */
	public float length() {
		return length(_p[Ases.X], _p[Ases.Y]);
	}

	static public float length(float[] p) {
		return (float) Math.sqrt(p[Ases.X] * p[Ases.X] + p[Ases.Y] * p[Ases.Y]);
	}

	static public float length(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * update vector
	 */
	private Vector update(float x, float y) {
		set(x,y);
		return this;
	}
	/**
	 * sum vector
	 */
	public Vector sum(float x, float y) {
		_p[Ases.X] += x;
		_p[Ases.Y] += y;
		return this;
	}

	public Vector sum(Vector v) {
		_p[Ases.X] += v._p[Ases.X];
		_p[Ases.Y] += v._p[Ases.Y];
		return this;
	}

	/**
	 * product vector
	 */
	public Vector product(Vector v) {
		_p[Ases.X] *= v._p[Ases.X];
		_p[Ases.Y] *= v._p[Ases.Y];
		return this;
	}

	/**
	 * product dot vector
	 */
	public float dot_poduct(Vector v) {
		return (_p[Ases.X] * v._p[Ases.X] + _p[Ases.Y] * v._p[Ases.Y]);
	}

	public float dot_poduct(float x, float y) {
		return (_p[Ases.X] * x + _p[Ases.Y] * y);
	}

	/**
	 * product cross vector
	 */
	public float cross_product(Vector v) {
		return (_p[Ases.X] * v._p[Ases.Y] - _p[Ases.Y] * v._p[Ases.X]);
	}

	/**
	 * normalize
	 */
	public Vector normalize(float x, float y) {
		float len = length(x, y);
		/**
		 */
		_p[Ases.X] = x / len;
		_p[Ases.Y] = y / len;
		/**
		 */
		return this;
	}

	static protected float[] normalize(float[] p) {
		float len = length(p);
		/*
		 */
		p[Ases.X] /= len;
		p[Ases.Y] /= len;
		/*
		 */
		return p;
	}

	/**
	 * orthogonal
	 */
	public Vector orthogonal() {
		_p[Ases.X] = -_p[Ases.Y];
		_p[Ases.Y] = _p[Ases.X];
		return this;
	}

	/**
	 * invert
	 */
	public Vector invert() {
		_p[Ases.X] = -_p[Ases.X];
		_p[Ases.Y] = -_p[Ases.Y];
		return this;
	}
}
