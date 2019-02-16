/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.vectors;
/**
 * Vector
 * @author Luis Monteiro
 */
public class Vector {
	/**
	 * data container
	 */
	protected float _x;
	protected float _y;
	/*
	 * constructors
	 */
	public Vector(float x, float y) {
		_x = x;
		_y = y;
	}
	public Vector() {
		_x = 0;
		_y = 0;
	}
	/**
	 * Get values
	 */
	public float getX() {
		return _x;
	}
	public float getY() {
		return _y;
	}
	/**
	 * vector length
	 */
	public float length() {
		return length(_x, _y);
	}
	static public float length(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}
	/**
	 * update vector
	 */
	public Vector update(float x, float y) {
		_x = x;
		_y = y;
		return this;
	}
	/**
	 * sum vector
	 */
	public Vector sum(float x, float y) {
		_x += x;
		_y += y;
		return this;
	}
	public Vector sum(Vector v) {
		_x += v._x;
		_y += v._y;
		return this;
	}
	/**
	 * product vector
	 */
	public Vector product(Vector v) {
		_x *= v._x;
		_y *= v._y;
		return this;
	}
	public Vector product(float k) {
		/**/
		_x *= k;
		_y *= k;
		/**/
		return this;
	}
	/**
	 * product dot vector
	 */
	public float dot_poduct(Vector v) {
		return (_x * v._x + _y * v._y);
	}
	public float dot_poduct(float x, float y) {
		return (_x * x + _y * y);
	}
	/**
	 * product cross vector
	 */
	public float cross_product(Vector v) {
		return (_x * v._y - _y * v._x);
	}
	/**
	 * orthogonal
	 */
	public Vector orthogonal() {
		float x = _x;
		/**/
		_x = -_y;
		_y = x;
		/**/
		return this;
	}
	/**
	 * invert
	 */
	public Vector invert() {
		_x = -_x;
		_y = -_y;
		return this;
	}
	/**
	 * invert
	 */
	public Vector invertX() {
		_x = -_x;
		return this;
	}
	public Vector invertY() {
		_y = -_y;
		return this;
	}
}
