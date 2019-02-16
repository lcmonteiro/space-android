/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.vectors;
/**
 * Position
 * @author Luis Monteiro
 */
public class Position extends Vector {
	/**
	 * constructor
	 */
	public Position(float x, float y) {
		super(x, y);
	}
	public Position() {
		this(0, 0);
	}
	/**
	 * update position
	 */
	public Position update(Movement m) {
		sum(m._x, m._y);
		return this;
	}
	public Position update(Position p) {
		update(p._x, p._y);
		return this;
	}
	/**
	 * get space
	 */
	public float space() {
		return length();
	}
}
