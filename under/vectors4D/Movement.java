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
public class Movement extends Position {

	/**
	 * constructor
	 */
	public Movement(float x, float y, float z, long dt) {
		super(x, y, z, dt);
	}

	public Movement(Direction dir, float ds, long dt) {
		_p = dir.getData();
		_s = ds;
		_t = dt;
	}
}
