/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.inputII;

/**
 * imports
 */
import space.base.AppI;
import space.base.Data;
import space.layerI.LinkI;
import space.layerI.InputI;
import space.under.filters.DecayAngle;
import space.under.filters.Line;

/**
 *
 * @author monteiro
 */
public class PointerSlave extends InputI {

	/**
	 *
	 */
	public static class Definitions {

		public static final float TRANSFORM_X = 0.11f;
		public static final float TRANSFORM_Y = 0.13f;
		public static final float FILTER_X = 0.25f;
		public static final float FILTER_Y = 0.25f;
		public static final int DIFF_MAX = 1000;
		public static final int DIFF_MIN = 10;
	}

	/**
	 *
	 */
	private final DecayAngle __fx;
	private final DecayAngle __fy;
	/**
	 *
	 */
	private Line __lx;
	private Line __ly;
	/**
	 *
	 */
	private Screen __screen;
	/**
	 *
	 */
	private Float __last_x = null;
	private Float __last_y = null;

	/**
	 *
	 * @param app
	 */
	public PointerSlave(AppI app) {
		super(app);
		/**
		 *
		 */
		__fx = new DecayAngle(Definitions.FILTER_X);
		__fy = new DecayAngle(Definitions.FILTER_Y);
		/**
		 *
		 */
		__lx = new Line(new float[]{1.0f, 1.0f});
		__ly = new Line(new float[]{1.0f, 1.0f});
	}

	/**
	 *
	 * @param retries
	 * @return
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
	 *
	 * @param data
	 */
	@Override
	public void onInit(Data data) {
		if (data instanceof Screen) {
			/**
			 * screen
			 */
			__screen = (Screen) data;
			/**
			 *
			 */
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
			/**
			 *
			 */
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
		//__process_sync(data);
		/**
		 *
		 */
		if (data instanceof Position) {
			/**
			 * update position
			 */
			data = __process((Position) data);
			/**
			 *
			 */
		} else if (data instanceof Command) {
			Command tmp = (Command) data;
			/**
			 * reset values
			 */
			switch (tmp.getBTN()) {
				case BRESET:
					__reset();
					break;
				case BRIGHT:
					break;
				case BLEFT:
					break;
			}
		} else if (data instanceof LinkI.Status) {
			LinkI.Status tmp = (LinkI.Status) data;
			/**
			 *
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
		 *
		 */
		super.onRun(data);
	}

	/**
	 *
	 */
	private void __reset() {
		__last_x = null;
		__last_y = null;
		/**
		 *
		 */
		__src_ref = null;
		__loc_ref = null;
		/**
		 *
		 */
		__fx.reset();
		__fy.reset();
	}

	/**
	 * process
	 *
	 * @param pos
	 * @return
	 */
	private Position __process(Position pos) {
		/**
		 *
		 */
		__fx.update(pos.getX());
		__fy.update(pos.getY());
		/**
		 *
		 */
		Position ret = null;
		/**
		 *
		 */
		if (__last_x != null && __last_y != null) {
			/**
			 *
			 */
			ret = new Position(
				-__lx.process(__fx.get() - __last_x), -__ly.process(__fy.get() - __last_y)
			);
		}
		__last_x = __fx.get();
		__last_y = __fy.get();
		/**
		 *
		 */
		return ret;
	}
	/**
	 *
	 */
	private Long __src_ref = null;
	private Long __loc_ref = null;

	private void __process_sync(Data d) {
		/**
		 * update times
		 */
		long loc_time = System.currentTimeMillis();
		long src_time = d.getTime();
		/**
		 * update local difference
		 */
		long loc_diff = 0;
		if (__loc_ref != null) {
			loc_diff = loc_time - (long) __loc_ref;
		}
		/**
		 * update source difference
		 */
		long src_diff = 0;
		if (__src_ref != null) {
			src_diff = src_time - (long) __src_ref;
		}
		/**
		 * sleep the difference between source and local
		 */
		long diff = src_diff - loc_diff;
		if (diff > Definitions.DIFF_MIN && diff < Definitions.DIFF_MAX) {
			try {
				logger(LogType.INFO, "diff=" + diff);
				sleep(diff, 0);
			} catch (InterruptedException ex) {
				return;
			}
		}
		__loc_ref = loc_time;
		__src_ref = src_time;
	}

}
