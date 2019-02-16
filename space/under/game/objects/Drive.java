/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;
/**
 */
import android.graphics.Color;
import space.under.game.vectors.Movement;
import space.under.game.vectors.Position;
/**
 * Drive
 * @author Luis Monteiro
 */
public class Drive extends Ball {
	/**
	 *
	 */
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
		public Properties SetColor(int c) {
			color = c;
			return this;
		}
		/**
		 */
		public Properties SetDecay(float d) {
			decay = d;
			return this;
		}
		/**
		 * default values
		 */
		protected int size = 10;
		protected float decay = 0.1f;
		protected int color = Color.parseColor("#EEEEEE");
	}

	/**
	 *
	 */
	public Drive(Position pos, Movement vel, Properties p) {
		super(pos, vel, new Ball.Properties()
			.SetSize(p.size)
			.SetDecay(p.decay)
			.SetColor(p.color)
		);
	}
}
