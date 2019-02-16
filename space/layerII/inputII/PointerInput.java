/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.inputII;

/**
 * imports
 */
import space.layerI.InputI;
import space.base.AppI;
import space.base.Data;
import space.layerI.LinkI;
import space.under.filters.Line;
import space.under.filters.VariationPointer;

/**
 * @author monteiro
 */
public class PointerInput extends InputI {
	/**
	 */
	public static class Definitions {

		public static final float TRANSFORM_X = 0.30f;
		public static final float TRANSFORM_Y = 0.30f;
	}
	/**
	 *
	 */
	private Line __lx;
	private Line __ly;
	/**
	 */
	private Screen __screen;
	/**
	 */
	final private VariationPointer __var;

	/**
	 */
	public PointerInput(AppI app) {
		super(app);
		/**/
		__lx = new Line(new float[]{1.0f, 1.0f});
		__ly = new Line(new float[]{1.0f, 1.0f});
		/**/
		__var = new VariationPointer();
	}

	/**
	 */
	@Override
	protected Integer play(Integer retries) {
		/**
		 * check initialization
		 */
		if (__screen == null) {
			return 1;
		}
		return 0;
	}
	/**
	 */
	@Override
	public void onInit(Data data) {
		if (data instanceof Screen) {
			/**
			 * screen
			 */
			__screen = (Screen) data;
			/**/
			__lx = new Line(
				new float[]{Definitions.TRANSFORM_X, Math.min(__screen.getX(), __screen.getY())}
			);
			__ly = new Line(
				new float[]{Definitions.TRANSFORM_Y, Math.min(__screen.getX(), __screen.getY())}
			);
		}
		super.onInit(data);
	}

	@Override
	public void onPause(Data data) {
		if (data instanceof Screen) {
			/**
			 * screen
			 */
			__screen = (Screen) data;
			/**/
			__lx = new Line(
				new float[]{Definitions.TRANSFORM_X, Math.min(__screen.getX(), __screen.getY())}
			);
			__ly = new Line(
				new float[]{Definitions.TRANSFORM_Y, Math.min(__screen.getX(), __screen.getY())}
			);
		}
		super.onPause(data);
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	@Override
	public void onRun(Data data) {
		/**
		 *
		 */
		if (data instanceof Position) {
			Position tmp = (Position) data;
			/**
			 * update position
			 */
			__var.update(tmp.getX(), tmp.getY());
			/**
			 */
			tmp.update(
				-__lx.process(__var.get_x()), -__ly.process(__var.get_y())
			);
			/**
			 */
		} else if (data instanceof Command) {
			Command tmp = (Command) data;
			/**
			 * reset values
			 */
			switch (tmp.getBTN()) {
				case BRESET:
					__var.reset();
					break;
				default:
					break;
			}
		} else if (data instanceof LinkI.Status) {
			LinkI.Status tmp = (LinkI.Status) data;
			/**
			 */
			switch (tmp.getStatus()) {

				case LinkI.Status.CONNECT: {
					send(new LogEntry("Conneted to " + tmp.getAddress()), 1);
					break;
				}
				case LinkI.Status.DISCONNECT: {
					send(new LogEntry("Searching...  ", 1), 1);
					break;
				}
			}
		}
		/**
		 */
		super.onRun(data);
	}
}
