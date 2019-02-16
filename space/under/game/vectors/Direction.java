/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.vectors;
/**
 * Direction
 * @author Luis Monteiro
 */
public class Direction extends Vector {
	/**
	 * power
	 */
	protected float _pow;
	/**
	 * constructor
	 */
	public Direction(float x, float y) {
		normalize(x, y);
	}
	public Direction(Vector v) {
		normalize(v._x, v._y);
	}
	public Direction() {
		_pow = 0;
		_x = 0;
		_y = 0;
	}
	/**
	 * update direction
	 * @param x
	 * @param y 
	 */
	public Direction update(Direction d){
		super.update(d._x, d._y);
		return this;
	}
	/**
	 * update direction
	 * @param x
	 * @param y 
	 */
	@Override
	public Direction update(float x, float y){
		normalize(x, y);
		return this;
	}
	/**
	 * Direction length
	 * @return length
	 */
	@Override
	public float length() {
		return _pow;
	}
	/**
	 * normalize
	 */
	private void normalize(float x, float y) {
		_pow = length(x, y);
		try {
			_x = x / _pow;
			_y = y / _pow;
		} catch (Exception e) {
			_x = 0.0f;
			_y = 0.0f;
		}
	}
}
