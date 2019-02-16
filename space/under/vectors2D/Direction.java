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
public class Direction extends Vector {

	/**
	 * constructor
	 */
	public Direction(float x, float y) {
		super(normalize(new float[]{x, y}));
	}

	public Direction(float[] p) {
		super(normalize(p));
	}

	public Direction(Vector v) {
		super(normalize(v._p));
	}

	public Direction() {
		_p[Ases.X] = 1.0f;
		_p[Ases.Y] = 0.0f;
	}
	/**
	 * update direction
	 * @param x
	 * @param y 
	 */
	public Direction update(Direction d){
		_p[Ases.X] = d._p[Ases.X];
		_p[Ases.Y] = d._p[Ases.Y];
		return this;
	}
	/**
	 * update direction
	 * @param x
	 * @param y 
	 */
	public Direction update(float x, float y){
		normalize(x, y);
		return this;
	}
	
	/**
	 * product dot vector
	 */
	public float dot_poduct(Movement v) {
		return (_p[Ases.X] * v.getX() + _p[Ases.Y] * v.getY());
	}

	/**
	 * Direction length
	 * @return length
	 */
	@Override
	public float length() {
		return (float) 1.0;
	}
}
