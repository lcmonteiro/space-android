/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;
/**
 */
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
/**
 */
import space.under.game.vectors.Movement;
import space.under.game.vectors.Position;
import space.under.game.vectors.Vector;
/**
 * Object
 * @author Luis Monteiro
 */
public class Object extends ShapeDrawable {

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
		public Properties SetPosition(float x, float y) {
			pos = new Position(x, y);
			return this;
		}

		public Properties SetVelocity(float x, float y) {
			vel = new Movement(x, y);
			return this;
		}

		public Properties SetSize(int x, int y) {
			size = new Vector(x, y);
			return this;
		}

		public Properties SetDecay(float d) {
			decay = d;
			return this;
		}
		/**
		 * default values
		 */
		protected Position pos = new Position();
		protected Movement vel = new Movement();
		protected Vector size = new Vector();
		protected float decay;
	}
	/**
	 *
	 */
	protected final Position __pos;
	protected final Movement __vel;
	protected final Vector __size;
	protected final Rect __region;
	protected final float __decay;

	/**
	 *
	 */
	public Object(Shape shape, Properties p) {
		super(shape);
		/**
		 * set properties
		 */
		__pos = p.pos;
		__vel = p.vel;
		__size = p.size;
		__decay = p.decay;
		/**
		 * update region
		 */
		__region = new Rect(
			(int) __pos.getX(),
			(int) __pos.getY(),
			(int) (__pos.getX() + __size.getX()),
			(int) (__pos.getY() + __size.getY())
		);
		
	}
	/**
	 * update position
	 */
	public float updatePosition() {
		/**
		 * update velocity
		 */
		__vel.decay(__decay);
		/**
		 * update position
		 */
		__pos.update(__vel);
		/**
		 * update region
		 */
		__region.set(
			(int) __pos.getX(),
			(int) __pos.getY(),
			(int) (__pos.getX() + __size.getX()),
			(int) (__pos.getY() + __size.getY())
		);
		return __vel.speed();
	}

	public void updatePosition(float x, float y) {
		/**
		 * update velocity
		 */
		__vel.update(0);
		/**
		 * update position
		 */
		__pos.update(x, y);
		/**
		 * update region
		 */
		__region.set(
			(int) x,
			(int) y,
			(int) (__pos.getX() + __size.getX()),
			(int) (__pos.getY() + __size.getY())
		);
	}
	
	public void updatePosition(Position pos) {
		/**
		 * update velocity
		 */
		__vel.update(0);
		/**
		 * update position
		 */
		__pos.update(pos);
		/**
		 * update region
		 */
		__region.set(
			(int) __pos.getX(),
			(int) __pos.getY(),
			(int) (__pos.getX() + __size.getX()),
			(int) (__pos.getY() + __size.getY())
		);
	}
	
	public void updatePosition(Movement vel) {
		/**
		 * update position
		 */
		__pos.update(vel);
		/**
		 * update region
		 */
		__region.set(
			(int) __pos.getX(),
			(int) __pos.getY(),
			(int) (__pos.getX() + __size.getX()),
			(int) (__pos.getY() + __size.getY())
		);
	}

	/**
	 * update velocity
	 */
	public void updateVelocity(float x, float y) {
		__vel.update(x, y);
	}
	
	public void updateVelocity(Movement move) {
		__vel.update(move);
	}
	
	public void updateVelocity(float decay) {
		__vel.product(decay);
	}
	
	/**
	 *
	 */
	public void updateDirection(Vector dir) {
		__vel.product(dir);
	}
	/**
	 *
	 */
	@Override
	public void draw(Canvas canvas) {
		setBounds(getRegion());
		/**
		 */
		super.draw(canvas);
	}

	/**
	 * get region
	 */
	public Rect getRegion() {
		return __region;
	}

	/**
	 * get position
	 */
	public Position getPos() {
		return __pos;
	}
	public float getPosX() {
		return __pos.getX();
	}
	public float getPosY() {
		return __pos.getY();
	}

	/**
	 * get velocity
	 */
	public Movement getVel() {
		return __vel;
	}

	/**
	 * dry update
	 */
	public Rect dryUpdate(long time) {
		Movement vel = new Movement(__vel.getX(), __vel.getY());
		Position pos = new Position(__pos.getX(), __pos.getY());
		/**
		 */
		if (time < 0) {
			vel.invert();
			/**
			 * update position
			 */
			for (int i = 0; i < -time; ++i) {
				vel.rise(__decay);
				/**/
				pos.update(vel);
			}
		} else {
			/**
			 * update position
			 */
			for (int i = 0; i < time; ++i) {
				vel.decay(__decay);
				/**/
				pos.update(vel);
			}
		}
		return new Rect(
			(int) pos.getX(),
			(int) pos.getY(),
			(int) (pos.getX() + __size.getX()),
			(int) (pos.getY() + __size.getY())
		);
	}

	/**
	 * Dry Update
	 */
	public void dryUpdate(long time, Position pos, Movement vel, Rect reg) {
		vel.update(__vel);
		pos.update(__pos);
		/**
		 */
		if (time < 0) {
			vel.invert();
			/**
			 * update position
			 */
			for (int i = 0; i < -time; ++i) {
				vel.rise(0.01f);
				/**/
				pos.update(vel);
			}
		} else {
			/**
			 * update position
			 */
			for (int i = 0; i < time; ++i) {
				vel.decay(__decay);
				/**/
				pos.update(vel);
			}
		}
		reg.set(
			(int) pos.getX(),
			(int) pos.getY(),
			(int) (pos.getX() + __size.getX()),
			(int) (pos.getY() + __size.getY())
		);
	}
}
