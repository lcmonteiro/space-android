/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;

import android.graphics.Canvas;

/**
 *
 * @author root
 */
public class Info extends Text {
	private long __time = 0;
	/**
	 */
	public Info(Properties p) {
		super("0", p);
	}
	/**
	 */
	public void setInfo(String txt, long time) {
		setText(txt);
		/**
		 * draw timeout
		 */
		__time = time;
	}
	public void clear() {
		setInfo("", 0);
	}
	/**
	 * 
	 * @param canvas 
	 */
	@Override
	public void draw(Canvas canvas) {
		if (__time > 0) {
			super.draw(canvas);
			__time--;
		}
	}
}
