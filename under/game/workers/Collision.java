/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.workers;
/**
 */
import android.graphics.Rect;
import space.under.game.models.Field;
import space.under.game.objects.Ball;
import space.under.game.objects.Drive;
import space.under.game.objects.Edge;
import space.under.game.vectors.Direction;
import space.under.game.vectors.Movement;
/**
 * Collision
 * @author Luis Monteiro
 */
public class Collision {
	/**
	 *
	 */
	final float __drive_efficiency;
	final float __edge_efficiency;
	final float __ball_efficiency;
	/**
	 */
	public Collision(float drive_efficiency, float edge_efficiency, float ball_efficiency) {
		__drive_efficiency = drive_efficiency;
		/**/
		__edge_efficiency = edge_efficiency;
		/**/
		__ball_efficiency = ball_efficiency;
	}
	/**
	 */
	public Collision(float drive_efficiency, float edge_efficiency) {
		this(drive_efficiency, edge_efficiency, 1.0f);
	}
	/**
	 * auxiliaries
	 */
	protected final Movement _vel_d = new Movement();
	protected final Movement _vel_c = new Movement();
	protected final Movement _vel_1 = new Movement();
	protected final Movement _vel_2 = new Movement();
	protected final Direction _dir_c = new Direction();
	
	protected final space.under.vectors2D.Movement _vel_d_ = new space.under.vectors2D.Movement();
	protected final space.under.vectors2D.Movement _vel_c_ = new space.under.vectors2D.Movement();
	protected final space.under.vectors2D.Movement _vel_1_ = new space.under.vectors2D.Movement();
	protected final space.under.vectors2D.Movement _vel_2_ = new space.under.vectors2D.Movement();
	protected final space.under.vectors2D.Direction _dir_c_ = new space.under.vectors2D.Direction();
	/**
	 * collision between edge and ball
	 */
	public boolean update(Edge obj1, Ball obj2) {
		/**
		 * intersect verification
		 */
		if (!Rect.intersects(obj1.getRegion(), obj2.getRegion())) {
			/* not touch */
			return false;
		}
		/**
		 * intersect confirmation
		 */
		Rect inter_reg = intersect(obj1.getRegion(), obj2.getRegion());
		/**/
		if (inter_reg.width() * inter_reg.height() <= obj2.getSize()) {
			/*not touch*/
			return false;
		}
		/**
		 * update positions
		 */
		float time = intersectTime(obj1.getRegion(), obj2.getRegion(), obj2.getVel());
		/**/
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(time));
		/**
		 * collision direction
		 */
		inter_reg = intersect(obj1.getRegion(), obj2.getRegion());
		/**/
		_dir_c.update(
			(inter_reg.centerX() - obj2.getRegion().centerX()),
			(inter_reg.centerY() - obj2.getRegion().centerY())
		);
		/**
		 * collision update
		 */
		if (_vel_c.update(_dir_c, _dir_c.dot_poduct(obj2.getVel())).speed()> 0) {
			/**
			 * update velocity
			 */
			obj2.getVel().sum(_vel_c.product(-2));

		} else {
			/**
			 * update positions
			 */
			obj2.updatePosition(_vel_c.update(_dir_c, _dir_c.dot_poduct(_vel_2.update(
				inter_reg.width(), inter_reg.height()
			))));
		}
		/**
		 * update positions
		 */
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(-time));
		/**
		 * update velocity
		 */
		obj2.updateVelocity(__edge_efficiency);
		/**
		 */
		return true;
	}
	/**
	 * collision between edge and Drive
	 */
	public boolean update(Edge obj1, Drive obj2) {
		/**
		 * intersect verification
		 */
		if (!Rect.intersects(obj1.getRegion(), obj2.getRegion())) {
			/* not touch */
			return false;
		}
		/**
		 * intersect confirmation
		 */
		Rect inter_reg = intersect(obj1.getRegion(), obj2.getRegion());
		/**/
		if (inter_reg.width() * inter_reg.height() <= obj2.getSize()) {
			/*not touch*/
			return false;
		}
		/**
		 * update positions
		 */
		float time = intersectTime(obj1.getRegion(), obj2.getRegion(), obj2.getVel());
		/**/
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(time));
		/**
		 * collision direction
		 */
		inter_reg = intersect(obj1.getRegion(), obj2.getRegion());
		/**/
		_dir_c.update(
			(inter_reg.centerX() - obj2.getRegion().centerX()),
			(inter_reg.centerY() - obj2.getRegion().centerY())
		);
		/**
		 * collision update
		 */
		if (_vel_c.update(_dir_c, _dir_c.dot_poduct(obj2.getVel())).speed()> 0) {
			/**
			 * update velocity
			 */
			obj2.getVel().sum(_vel_c.product(-1));

		} else {
			/**
			 * update positions
			 */
			obj2.updatePosition(_vel_c.update(_dir_c, _dir_c.dot_poduct(_vel_2.update(
				inter_reg.width(), inter_reg.height()
			))));
		}
		/**
		 * update positions
		 */
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(-time));
		/**
		 * update velocity
		 */
		obj2.updateVelocity(__edge_efficiency);
		/**
		 */
		return true;
	}
	/**
	 * Collision between two balls
	 *
	 * @param ball1
	 * @param ball2
	 * @return
	 */
	public boolean update(Ball obj1, Ball obj2) {
		/**
		 */
		Rect obj2_reg = obj2.getRegion();
		Rect obj1_reg = obj1.getRegion();
		/**
		 */
		if (!Rect.intersects(obj2_reg, obj1_reg)) {
			/* not touch */
			return false;
		}
		/**
		 */
		int d = (obj2_reg.width() + obj1_reg.width()) / 2;
		int x = (obj2_reg.centerX() - obj1_reg.centerX());
		int y = (obj2_reg.centerY() - obj1_reg.centerY());
		/**
		 * intersect verification
		 */
		if ((x * x + y * y) >= (d * d)) {
			/* not touch */
			return false;
		}
		/**
		 * update positions
		 */
		float time = intersectTime(obj1.getRegion(), obj2.getRegion(), obj1.getVel(), obj2.getVel(), d);
		/**
		 */
		obj1.updatePosition((Movement)_vel_1.update(obj1.getVel()).product(time));
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(time));
		/**
		 * collision direction
		 */
		_dir_c.update(
			(obj1_reg.centerX() - obj2_reg.centerX()),
			(obj1_reg.centerY() - obj2_reg.centerY())
		);
		/**
		 * collision update
		 */
		_vel_d.update(obj1.getVel()).product(-1).sum(obj2.getVel());
		/**/
		if (_vel_c.update(_dir_c, _dir_c.dot_poduct(_vel_d)).speed()> 0) {
			/**/
			obj1.getVel().sum(_vel_c);
			/**/
			obj2.getVel().sum(_vel_c.product(-1));

		} else {
			/**
			 * update positions
			 */
			Rect inter_reg = intersect(obj1.getRegion(), obj2.getRegion());
			/**/
			obj2.updatePosition(_vel_c.update(_dir_c, _dir_c.dot_poduct(_vel_2.update(
				inter_reg.width(), inter_reg.height()
			))));
		}
		/**
		 * update positions
		 */
		obj1.updatePosition((Movement)_vel_1.update(obj1.getVel()).product(-time));
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(-time));
		/**
		 * update velocity
		 */
		obj1.updateVelocity(__ball_efficiency);
		obj2.updateVelocity(__ball_efficiency);
		/**
		 */
		return true;
	}
	/**
	 * Collision between drive and ball
	 *
	 * @param ball1
	 * @param ball2
	 * @return
	 */
	public boolean update(Drive obj1, Ball obj2) {
		/**
		 */
		Rect obj2_reg = obj2.getRegion();
		Rect obj1_reg = obj1.getRegion();
		/**
		 */
		if (!Rect.intersects(obj2_reg, obj1_reg)) {
			/* not touch */
			return false;
		}
		/**
		 */
		int d = (obj2_reg.width() + obj1_reg.width()) / 2;
		int x = (obj2_reg.centerX() - obj1_reg.centerX());
		int y = (obj2_reg.centerY() - obj1_reg.centerY());
		/**
		 * intersect verification
		 */
		if ((x * x + y * y) >= (d * d)) {
			/* not touch */
			return false;
		}
		/**
		 * update positions
		 */
		float time = intersectTime(obj1.getRegion(), obj2.getRegion(), obj1.getVel(), obj2.getVel(), d);
		/**
		 */
		obj1.updatePosition((Movement)_vel_1.update(obj1.getVel()).product(time));
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(time));
		/**
		 * collision direction
		 */
		_dir_c.update(
			(obj1_reg.centerX() - obj2_reg.centerX()),
			(obj1_reg.centerY() - obj2_reg.centerY())
		);
		/**
		 * collision update
		 */
		_vel_d.update(obj1.getVel()).product(-1).sum(obj2.getVel());
		/**/
		if (_vel_c.update(_dir_c, _dir_c.dot_poduct(_vel_d)).speed()> 0) {
			/**
			 * update velocity
			 */
			obj2.getVel().sum(_vel_c.product(-2));
		} else {
			/**
			 * update positions
			 */
			Rect inter_reg = intersect(obj1.getRegion(), obj2.getRegion());
			/**/
			obj2.updatePosition(_vel_c.update(_dir_c, _dir_c.dot_poduct(_vel_2.update(
				inter_reg.width(), inter_reg.height()
			))));
		}
		/**
		 * update positions
		 */
		obj1.updatePosition((Movement)_vel_1.update(obj1.getVel()).product(-time));
		obj2.updatePosition((Movement)_vel_2.update(obj2.getVel()).product(-time));
		/**
		 * update velocity
		 */
		obj2.updateVelocity(__drive_efficiency);
		/**
		 */
		return true;
	}

	/**
	 * collision between field and ball
	 *
	 * @param field
	 * @param ball
	 */
	public boolean update(Field field, Ball ball) {
		boolean flag = false;
		for (Edge e : field.getEdges()) {
			flag |= update(e, ball);
		}
		return flag;
	}

	/**
	 * collision between field and drive
	 *
	 * @param field
	 * @param ball
	 */
	public boolean update(Field field, Drive ball) {
		boolean flag = false;
		for (Edge e : field.getEdges()) {
			flag |= update(e, ball);
		}
		return flag;
	}

	/**
	 *
	 */
	private float intersectTime(Rect p1, Rect p2, Movement v1, Movement v2, float d) {
		/**
		 * auxiliaries
		 */
		float vdx = v2.getX() - v1.getX();
		float vdy = v2.getY() - v1.getY();
		float pdx = p2.centerX() - p1.centerX();
		float pdy = p2.centerY() - p1.centerY();
		/**
		 * aX² + bX + c
		 */
		float a = ((vdx * vdx) + (vdy * vdy));
		float b = ((pdx * vdx) + (pdy * vdy)) * (2);
		float c = ((pdx * pdx) + (pdy * pdy)) - (d * d);
		/**
		 * Quadratic formula
		 */
		float t = (-b - (float) Math.sqrt((b * b) - (a * c * 4))) / (2 * a);
		/**
		 */
		return t;
	}

	/**
	 *
	 */
	private float intersectTime(Rect r, Rect p, Movement v) {
		/**
		 * auxiliaries
		 */
		float times[] = {
			(r.left - p.right) / v.getX(),
			(r.right - p.left) / v.getX(),
			(r.top - p.bottom) / v.getY(),
			(r.bottom - p.top) / v.getY()
		};
		/**
		 *
		 */
		float time = -2;
		for (float t : times) {
			if (t < 0 && t > time) {
				time = t;
			}
		}
		/**
		 */
		return time;
	}
	/**
	 *
	 */
	private final Rect _reg_intersect = new Rect();
	private final Rect _reg_null = new Rect();

	private Rect intersect(Rect r1, Rect r2) {
		_reg_intersect.set(r1.left - 1, r1.top - 1, r1.right + 1, r1.bottom + 1);
		return _reg_intersect.intersect(r2) ? _reg_intersect : _reg_null;
	}
}
