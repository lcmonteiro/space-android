/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.vectors4D;

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
		public static final int Z = 2;
		public static final int W = 3;
	}
	/**
	 * data container
	 */
	protected float[] _p;
	/*
	 * constructors
	 */

	public Vector(float x, float y, float z) {
		/*
		 */
		_p = new float[]{x, y, z};
	}

	public Vector(float[] p) {
		/*
		 */
		_p = p;
	}

	protected Vector() {
		/*
		 */
		_p = null;
	}

	/**
	 * Set Values
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	protected void set(float x, float y, float z) {
		/*
		 */
		_p = new float[]{x, y, z};
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

	public float getZ() {
		return _p[Ases.Z];
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
		return length(_p[Ases.X], _p[Ases.Y], _p[Ases.Z]);
	}

	static public float length(float[] p) {
		return (float) Math.sqrt(p[Ases.X] * p[Ases.X] + p[Ases.Y] * p[Ases.Y] + p[Ases.Z] * p[Ases.Z]);
	}

	static public float length(float x, float y, float z) {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * sum vector
	 */
	public Vector sum(Vector v) {
		return new Vector(_p[Ases.X] + v._p[Ases.X], _p[Ases.Y] + v._p[Ases.Y], _p[Ases.Z] + v._p[Ases.Z]);
	}
	
	public Vector self_sum(Vector v) {
		_p[Ases.X] += v._p[Ases.X];
		_p[Ases.Y] += v._p[Ases.Y];
		_p[Ases.Z] += v._p[Ases.Z];
		return this;
	}

	/**
	 * product vector
	 */
	public Vector product(float v) {
		return new Vector(_p[Ases.X] * v, _p[Ases.Y] * v, _p[Ases.Z] * v);
	}
	
	public Vector self_product(float v) {
		_p[Ases.X] *= v;
		_p[Ases.Y] *= v;
		_p[Ases.Z] *= v;
		return this;
	}

	/**
	 * product dot vector
	 */
	public float dot_poduct(Vector v) {
		return (_p[Ases.X] * v._p[Ases.X] + _p[Ases.Y] * v._p[Ases.Y] + _p[Ases.Z] * v._p[Ases.Z]);
	}

	/**
	 * product cross vector
	 */
	public Vector cross_product(Vector v) {
		return new Vector(
			_p[Ases.Y] * v._p[Ases.Z] - _p[Ases.Z] * v._p[Ases.Y],
			_p[Ases.Z] * v._p[Ases.X] - _p[Ases.X] * v._p[Ases.Z],
			_p[Ases.X] * v._p[Ases.Y] - _p[Ases.Y] * v._p[Ases.X]
		);
	}

	public Vector cross_product_inv(Vector v) {
		return new Vector(
			_p[Ases.Z] * v._p[Ases.Y] - _p[Ases.Y] * v._p[Ases.Z],
			_p[Ases.X] * v._p[Ases.Z] - _p[Ases.Z] * v._p[Ases.X],
			_p[Ases.Y] * v._p[Ases.X] - _p[Ases.X] * v._p[Ases.Y]
		);
	}

	/**
	 * normalize
	 */
	public Vector normalize() {
		/*
		 */
		return new Vector(normalize(_p[Ases.X], _p[Ases.Y], _p[Ases.Z]));
	}
	
	public Vector self_normalize() {
		normalize(_p);
		/**/
		return this;
	}

	static public float[] normalize(float x, float y, float z) {
		float len = length(x, y, z);
		/*
		 */
		return new float[]{x / len, y / len, z / len};
	}

	static public float[] normalize(float[] p) {
		float len = length(p);
		/*
		 */
		p[Ases.X] /= len;
		p[Ases.Y] /= len;
		p[Ases.Z] /= len;
		/*
		 */
		return p;
	}

	/**
	 * Quaternions and spatial rotation
	 */
	protected Vector rotate(Vector v) {
		/**
		 */
		return rotate(v, v.length());
	}
	/**
	 */
	protected Vector rotate(Vector v, float angle) {
		/**/
		return new Vector(rotate(v._p, angle));
	}
	/**
	 */
	protected Vector self_rotate(Vector v, float angle) {
		_p = rotate(v._p, angle);
		/**/
		return this;
	}
	/**
	 */
	protected float[] rotate(float[] v, float angle) {
		angle /= 2;
		/**
		 * to quarternions
		 */
		float[] p = new float[]{_p[Ases.X], _p[Ases.Y], _p[Ases.Z], 0};
		/**
		 *
		 */
		float s = (float) Math.sin((float) angle);
		float c = (float) Math.cos((float) angle);
		/**
		 *
		 */
		float[] q = new float[]{v[Ases.X] * s, v[Ases.Y] * s, v[Ases.Z] * s, c};
		/**
		 *
		 */
		return __q_rotation(p, q);
	}

	/**
	 * rotation function base on quarternions
	 */
	private float[] __q_invert(float[] r) {
		/**/
		return new float[]{-r[Ases.X], -r[Ases.Y], -r[Ases.Z], r[Ases.W]};
	}

	protected float[] __q_rotation(float[] p, float[] q) {
		/**/
		return __q_product(__q_product(q, p), __q_invert(q));
	}

	private float[] __q_product(float[] r, float[] q) {
		/**/
		return new float[]{
			(r[Ases.W] * q[Ases.X] + r[Ases.X] * q[Ases.W] + r[Ases.Y] * q[Ases.Z] - r[Ases.Z] * q[Ases.Y]),
			(r[Ases.W] * q[Ases.Y] - r[Ases.X] * q[Ases.Z] + r[Ases.Y] * q[Ases.W] + r[Ases.Z] * q[Ases.X]),
			(r[Ases.W] * q[Ases.Z] + r[Ases.X] * q[Ases.Y] - r[Ases.Y] * q[Ases.X] + r[Ases.Z] * q[Ases.W]),
			(r[Ases.W] * q[Ases.W] - r[Ases.X] * q[Ases.X] - r[Ases.Y] * q[Ases.Y] - r[Ases.Z] * q[Ases.Z])
		};
	}

}
