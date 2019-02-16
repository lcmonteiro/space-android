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
public class Position extends Direction {

	/**
	 * time in milliseconds
	 */
	protected long _t;
	/**
	 * space
	 */
	protected float _s;

	/**
	 * constructor
	 */
	public Position(float x, float y, float z, long t) {
		/**
		 * set space distance
		 */
		_s = length(x, y, z);
		/**
		 * set direction
		 */
		set(x / _s, y / _s, z / _s);
		/**
		 * set time distance
		 */
		_t = t;
	}

	public Position(Vector v, long t) {
		this(v._p[Ases.X], v._p[Ases.Y], v._p[Ases.Z], t);
	}

	public Position(float[] p, long t) {
		this(p[Ases.X], p[Ases.Y], p[Ases.Z], t);
	}

	public Position(Position p) {
		/**
		 * set direction
		 */
		set(p.getX(),p.getY(),p.getZ());
		/**
		 * set position
		 */
		_s = p.space();
		/**
		 * set time
		 */
		_t = p.time();
	}
	
	protected Position() {
	}

	/**
	 * rotate
	 */
	public Position rotate(Movement m) {
		return new Position(
			/**
			 * time in milliseconds
			 */
			rotate((Vector) m, (m.space() * m.time()) / 1000), _t + m.time()
		);
	}
	/** 
	 */
	public Position rotate_inv(Movement m) {
		return new Position(
			/**
			 * time in milliseconds
			 */
			rotate((Vector) m, -(m.space() * m.time()) / 1000), _t + m.time()
		);
	}
	
	public Position self_rotate_inv(Movement m) {
		/**
		 * update position
		 */
		_p = rotate(m._p, -(m.space() * m.time()) / 1000);
		/**
		 * update time
		 */
		_t += m.time();
		/**
		 */
		return this;
	}

	/**
	 *
	 */
	public Position move(Movement m, long t) {
//		return new Position(
//			sum((Vector) m, (m.space() * m.time()) / 2), _t + m.time()
//		);
		return null;
	}

	/**
	 *
	 * @return
	 */
	public long time() {
		return _t;
	}
	
	public Position self_time(long time) {
		_t = time;
		/**/
		return this;
	}

	/**
	 *
	 * @return
	 */
	public float space() {
		return _s;
	}
}
