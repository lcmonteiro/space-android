/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;
/**
 */
import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;
import space.under.game.vectors.Movement;
import space.under.game.vectors.Position;
/**
 * Ball
 * @author Luis Monteiro
 */
public class Ball extends Object {
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
		protected float decay = 0.01f;
		protected int color = Color.parseColor("#EEEEEE");
	}
	/**
	 *
	 */
	public Ball(Position pos, Movement vel, Properties p) {
		super(new OvalShape(), new Object.Properties()
			.SetPosition(pos.getX() - p.size / 2, pos.getY() - p.size / 2)
			.SetVelocity(vel.getX(), vel.getY())
			.SetSize(p.size, p.size)
			.SetDecay(p.decay)
		);
		/**
		 */
		super.getPaint().setColor(p.color);
		/**/
	}
	/**
	 * object size
	 */
	public float getSize() {
		return __size.getX();
	}
	/**
	 * object color
	 */
	public int getColor() {
		return getPaint().getColor();
	}
	/**
	 * updatePosition
	 * @param pos
	 */
	@Override
	public void updatePosition(Position pos) {
		super.updatePosition(
			(Position)__pos.update(pos).sum(-(__size.getX() / 2), -(__size.getY() / 2))
		);
	}
}
