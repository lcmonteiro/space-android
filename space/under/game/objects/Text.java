/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import space.under.vectors2D.Vector;

/**
 *
 * @author root
 */
public class Text extends Drawable {

	static public class Properties {
		/**
		 */
		public Properties() {
		}
		/**
		 */
		public Properties SetSize(int s) {
			size = s;
			return this;
		}
		/**
		 */
		public Properties SetPosition(float x, float y) {
			pos = new Vector(x, y);
			return this;
		}
		/**
		 */
		public Properties SetColor(int c) {
			color = c;
			return this;
		}
		/**
		 */
		public Properties SetAlpha(int a) {
			alpha = a;
			return this;
		}
		/**
		 * default values
		 */
		protected int alpha = 255;
		protected int size = 10;
		protected Vector pos;
		protected int color = Color.parseColor("#EEEEEE");
	}
	/**
	 *
	 */
	private String __text;
	/**
	 */
	private final Paint __paint;
	private final Vector __pos;

	/**
	 */
	public Text(String text, Properties p) {
		/**
		 */
		__text = text;
		/**
		 */
		__pos = p.pos;
		/**
		 */
		__paint = new Paint();
		__paint.setColor(p.color);
		__paint.setAlpha(p.alpha);
		__paint.setTextSize(p.size);
		__paint.setTextAlign(Paint.Align.CENTER);
	}

	public void setText(String text) {
		__text = text;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawText(__text, __pos.getX(), __pos.getY(), __paint);
	}

	@Override
	public void setAlpha(int alpha) {
		__paint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		__paint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
}
