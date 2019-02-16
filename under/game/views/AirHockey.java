/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import space.under.filters.VariationPointer;
import space.under.game.models.Field;
import space.under.game.objects.Ball;
import space.under.game.objects.Drive;
import space.under.game.objects.Edge;
import space.under.game.objects.Info;
import space.under.game.objects.Score;
import space.under.game.workers.Collision;
import space.under.game.workers.Controller;
import space.under.game.vectors.Movement;
import space.under.game.vectors.Position;
/**
 * AirHockey
 * @author Luis Monteiro
 */
public class AirHockey extends GameView {

	/**
	 * controller
	 */
	private Controller __ctrl_1;
	private Controller __ctrl_2;
	/**
	 * objects
	 */
	private Ball __ball;
	private Drive __drive_1;
	private Drive __drive_2;
	private Field __field;
	private Edge __space_1;
	private Edge __space_2;
	private Edge __goal_1;
	private Edge __goal_2;
	private Score __score_1;
	private Score __score_2;
	private Info __info;
	/**
	 * space
	 */
	private Rect __space;
	/**
	 * workers
	 */
	private Collision __collision;
	/**
	 * positions
	 */
	private Position __drive_pos_1;
	private Position __drive_pos_2;
	private Position __ball_pos_1;
	private Position __ball_pos_2;
	/**
	 * status
	 */
	private int __last_touch = 1;

	/**
	 */
	public AirHockey(Context context) {
		super(context, 30);
	}

	@Override
	protected void onCreate(Rect region) {
		super.onCreate(region);
		/**
		 * spaces
		 */
		__space = new Rect(
			region.left - GetRealSize(0.15f), region.top, region.right + GetRealSize(0.15f), region.bottom
		);
		/**
		 * positions
		 */
		__drive_pos_1 = new Position(region.left + GetRealSize(0.06f), region.centerY());
		__drive_pos_2 = new Position(region.right - GetRealSize(0.06f), region.centerY());
		__ball_pos_1 = new Position(region.left + GetRealSize(0.3f), region.centerY());
		__ball_pos_2 = new Position(region.right - GetRealSize(0.3f), region.centerY());
		/**
		 */
		__ctrl_1 = new Controller(region, 1.0f);
		__ctrl_2 = new Controller(region, 1.0f);
		/**
		 */
		__ball = new Ball(
			new Position(region.centerX(), region.centerY()),
			new Movement(((float) Math.random() * 20) - 10, ((float) Math.random() * 20) - 10),
			new Ball.Properties()
			.SetSize(GetRealSize(region, 0.06f))
			.SetColor(Color.parseColor("#B0B0B0"))
			.SetDecay(0.02f)
		);
		/**
		 * drive
		 */
		__drive_1 = new Drive(
			__drive_pos_1, new Movement(), new Drive.Properties()
			.SetSize(GetRealSize(region, 0.10f))
			.SetColor(Color.parseColor("#B07020"))
		);
		__drive_2 = new Drive(
			__drive_pos_2, new Movement(), new Drive.Properties()
			.SetSize(GetRealSize(region, 0.10f))
			.SetColor(Color.parseColor("#2070B0"))
		);
		/**
		 */
		__field = new Field(new Field.Properties()
			.SetGoal(GetRealSize(region, 0.26f))
			.SetStart(-GetRealSize(region, 0.5f), -GetRealSize(region, 0.5f))
			.SetSize(region.right + GetRealSize(region, 1.0f), region.bottom + GetRealSize(region, 1.0f))
			.SetColor(Color.parseColor("#808880"))
			.SetEdge(GetRealSize(region, 0.53f))
		);
		/**
		 * space
		 */
		__space_1 = new Edge(new Position(region.left, region.top), new Edge.Properties()
			.SetSize(region.centerX(), region.bottom)
			.SetColor(Color.parseColor("#202010"))
		);
		__space_2 = new Edge(new Position(region.centerX(), region.top), new Edge.Properties()
			.SetSize(region.centerX(), region.bottom)
			.SetColor(Color.parseColor("#102020"))
		);
		/**
		 * goal
		 */
		__goal_1 = new Edge(new Position(-GetRealSize(region, 0.28f), GetRealSize(region, 0.25f)), new Edge.Properties()
			.SetSize(GetRealSize(region, 0.2f), GetRealSize(region, 0.5f))
			.SetColor(Color.parseColor("#202020"))
		);
		__goal_2 = new Edge(new Position(region.right + GetRealSize(region, 0.08f), GetRealSize(region, 0.25f)), new Edge.Properties()
			.SetSize(GetRealSize(region, 0.2f), GetRealSize(region, 0.5f))
			.SetColor(Color.parseColor("#202020"))
		);

		/**
		 * score
		 */
		__score_1 = new Score(new Score.Properties()
			.SetColor(Color.parseColor("#B07020"))
			.SetPosition(region.centerX() - GetRealSize(0.1f), GetRealSize(0.15f))
			.SetSize(GetRealSize(0.1f))
		);
		__score_2 = new Score(new Score.Properties()
			.SetColor(Color.parseColor("#2070B0"))
			.SetPosition(region.centerX() + GetRealSize(0.1f), GetRealSize(0.15f))
			.SetSize(GetRealSize(0.1f))
		);
		/**
		 * info
		 */
		__info = new Info(new Info.Properties()
			.SetColor(Color.parseColor("#FFFFFF"))
			.SetPosition(region.centerX(), region.centerY())
			.SetSize(GetRealSize(0.3f))
		);
		/**
		 */
		__collision = new Collision(0.8f, 1.0f);
		/**
		 *
		 */
		__ref = 0;
	}

	/**
	 * update movement
	 */
	private long __ref = 0;
	/**
	 */
	final private VariationPointer __var_1 = new VariationPointer();
	final private VariationPointer __var_2 = new VariationPointer();

	/**
	 */
	public synchronized void SetControl(String name, long id) {
		if (__ref <= id) {
			__ref = id;
		}
	}

	public synchronized void ResetControl(long id) {
		if (__ref <= id) {
			__var_1.reset();
			/**/
			__ref = id;
		} else {
			__var_2.reset();
		}
	}

	public synchronized void SetMovement(long id, float x, float y) {
		if (__ref <= id) {
			__var_1.update(x, y);
			__ctrl_1.update(-__var_1.get_x(), -__var_1.get_y());
			/**/
			__ref = id;
		} else {
			__var_2.update(x, y);
			__ctrl_2.update(-__var_2.get_x(), -__var_2.get_y());
		}
	}

	/**
	 */
	@Override
	public boolean isFocused() {
		return super.isFocused();
	}

	/**
	 *
	 */
	@Override
	public void setAlpha(float alpha) {
		__ball.setAlpha((int) alpha);
		__drive_1.setAlpha((int) alpha);
		__drive_2.setAlpha((int) alpha);
		__field.setAlpha((int) alpha);
		__space_1.setAlpha((int) alpha);
		__space_2.setAlpha((int) alpha);
		__score_1.setAlpha((int) alpha);
		__score_2.setAlpha((int) alpha);
		super.setAlpha(alpha);
	}

	@Override
	protected float onUpdate() {
		float movement = 0;
		/**
		 * consume velocity
		 */
		synchronized (this) {
			if (__ctrl_1.speed()!= 0) {
				__drive_1.updateVelocity(__ctrl_1);
				__ctrl_1.update(0);
			}
			if (__ctrl_2.speed() != 0) {
				__drive_2.updateVelocity(__ctrl_2);
				__ctrl_2.update(0);
			}
		}
		/**
		 * positions
		 */
		movement += __ball.updatePosition();
		movement += __drive_1.updatePosition();
		movement += __drive_2.updatePosition();
		/**
		 * collisions
		 */
		if (__collision.update(__drive_1, __ball)) {
			__last_touch = 1;
		}
		if (__collision.update(__drive_2, __ball)) {
			__last_touch = 2;
		}
		/**
		 */
		__collision.update(__field, __ball);
		__collision.update(__field, __drive_1);
		__collision.update(__field, __drive_2);
		__collision.update(__goal_1, __drive_1);
		/**
		 * goals
		 */
		if (__collision.update(__goal_1, __ball)) {
			__score_2.setGoal();
			__drive_1.updatePosition(__drive_pos_1);
			__drive_2.updatePosition(__drive_pos_2);
			__ball.updatePosition(__ball_pos_1);
			__info.setInfo("GOAL", 50);
		}
		if (__collision.update(__goal_2, __ball)) {
			__score_1.setGoal();
			__drive_1.updatePosition(__drive_pos_1);
			__drive_2.updatePosition(__drive_pos_2);
			__ball.updatePosition(__ball_pos_2);
			__info.setInfo("GOAL", 50);
		}
		/**
		 */
		__collision.update(__goal_2, __drive_2);
		__collision.update(__space_2, __drive_1);
		__collision.update(__space_1, __drive_2);
		/**
		 *
		 */
		if (!Rect.intersects(__drive_1.getRegion(), __space)) {
			__drive_1.updatePosition(__drive_pos_1);
			__info.setInfo("FAULT", 50);
		}
		if (!Rect.intersects(__drive_2.getRegion(), __space)) {
			__drive_2.updatePosition(__drive_pos_2);
			__info.setInfo("FAULT", 50);
		}
		if (!Rect.intersects(__ball.getRegion(), __space)) {
			if (__last_touch == 1) {
				__ball.updatePosition(__ball_pos_2);
				__info.setInfo("OUT", 50);
			} else {
				__ball.updatePosition(__ball_pos_1);
				__info.setInfo("OUT", 50);
			}
		}
		return movement;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);
		/**
		 * clear
		 */
		canvas.drawColor(0, Mode.CLEAR);
		/**
		 */
		__space_1.draw(canvas);
		__space_2.draw(canvas);
		__field.draw(canvas);
		__goal_1.draw(canvas);
		__goal_2.draw(canvas);
		__ball.draw(canvas);
		__drive_1.draw(canvas);
		__drive_2.draw(canvas);
		__score_1.draw(canvas);
		__score_2.draw(canvas);
		__info.draw(canvas);
	}

	/**
	 *
	 */
	void initPosition() {
		__ball.getPos().update(GONE, GONE);
	}
}
