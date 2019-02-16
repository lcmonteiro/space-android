/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;
/**
 */
import android.graphics.Color;
import android.graphics.drawable.shapes.RectShape;
/**
 */
import space.under.game.vectors.Position;
/**
 * Edge
 * @author Luis Monteiro
 */
public class Edge extends Object {

	static public class Properties {

		/**
		 */
		public Properties() {
		}

		/**
		 */
		public Properties SetSize(int x, int y) {
			size_x = x;
			size_y = y;
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
		protected int size_x = 0;
		protected int size_y = 0;
		protected int color = Color.parseColor("#EEEEEE");
	}

	/**
	 */
	public Edge(Position pos, Properties p) {
		super(new RectShape(), new Object.Properties()
			.SetPosition(pos.getX(), pos.getY())
			.SetSize(p.size_x, p.size_y)
		);
		/**
		 * set properties
		 */
		getPaint().setColor(p.color);
		getPaint().setAlpha(p.alpha);
	}
}
