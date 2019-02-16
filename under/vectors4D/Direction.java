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
public class Direction extends Vector {

	/**
	 * constructor
	 */
	public Direction(float x, float y, float z) {
		super(normalize(x, y, z));
	}

	public Direction(float[] p) {
		super(normalize(p[Ases.X], p[Ases.Y], p[Ases.Z]));
	}

	public Direction(Vector v) {
		super(normalize(v._p));
	}

	protected Direction() {
	}

	/**
	 * Direction length
	 *
	 * @return length
	 */
	@Override
	public float length() {
		return (float) 1.0;
	}
}
