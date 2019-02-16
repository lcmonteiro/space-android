/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.base;

import android.os.SystemClock;

/**
 *
 * @author monteiro
 */
public abstract class Data extends Object implements java.io.Serializable {

	/**
	 */
	final protected long _t;
	/**
	 */
	public Data() {
		_t = SystemClock.elapsedRealtime();
	}

	public Data(long t) {
		_t = t;
	}
	/**
	 */
	public long getTime() {
		return _t;
	}
}
