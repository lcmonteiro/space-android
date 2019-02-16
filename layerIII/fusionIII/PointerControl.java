	 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerIII.fusionIII;

/**
 * imports
 */
import android.os.SystemClock;
/**
 *
 */
import space.base.App;
import space.base.AppI;
import space.base.Data;
import space.layerI.LinkI;
import space.layerI.MoveI;
import space.layerI.TouchI.Touch;
import space.layerI.InputI.Command;
import space.layerI.InputI.Position;
import space.layerI.TouchI.Key;
import space.layerII.fusionII.Fusion;
import space.layerII.fusionII.Fusion.DirectionY;
import space.under.filters.Decay;
import space.under.input.UserInput;
/**
 * @author monteiro
 */
public class PointerControl extends Fusion {
	/**
	 *
	 */
	public static class Definitions {

		public static final float MIN_POW = 0.07f;
		//
		public static final float DECAY_POW = 0.2f;
		// pinter id filter
		public static final int POINTER_ID = 0;
		// touch tolerance
		public static final int TOUCH_TOL = 2;
		// touch resolution
		public static final int TOUCH_MOVE_X = 9;
		public static final int TOUCH_MOVE_Y = 7;
		// max move pointer time
		public static final int MAX_TIME = 100000;
		//
		public static final int MIN_TIME = 100;
		//
		public static final int WAIT_TIME = 10000;
		//
		public static final int MIN_TOUCH_TIMEOUT = 50;
		//
		public static final int TOUCH_TIMEOUT = 300;
		//
		public static final int LONG_TOUCH_TIMEOUT = 1000;
		//
		public static final float DECAY_SCROLL = 0.02f;

	}
	/**
	 *
	 */
	private enum State {

		KEY, POINTER
	}
	/**
	 *
	 */
	private static class Tap {

		public Tap() {

			zone = Zone.NULL;
			state = State.UP;
			t_down = 0;
			t_move = 0;
			t_up = 0;
			p_down = new Point(0, 0);
			p_move = new Point(0, 0);
			p_up = new Point(0, 0);
			p_tol = new Point(Definitions.TOUCH_TOL, Definitions.TOUCH_TOL);
			p_resolusion = new Point(
				Definitions.TOUCH_MOVE_X, Definitions.TOUCH_MOVE_Y
			);
		}

		/**
		 * @param x
		 * @param y
		 */
		public void SetScreen(int x, int y, float dx, float dy) {
			screen_x = new int[]{
				(int) ((float) x * 0.01),
				(int) ((float) x * 0.25),
				(int) ((float) x * 0.75),
				(int) ((float) x * 0.98)
			};
			screen_y = new int[]{
				(int) ((float) y * 0.25),
				(int) ((float) y * 0.75)
			};
			p_tol = new Point(
				(int) ((float) Definitions.TOUCH_TOL * dx),
				(int) ((float) Definitions.TOUCH_TOL * dy)
			);
			p_resolusion = new Point(
				(int) ((float) Definitions.TOUCH_MOVE_X * dx),
				(int) ((float) Definitions.TOUCH_MOVE_Y * dy)
			);
		}
		/**
		 * @param p
		 * @return
		 */
		public Zone GetZone(Point p) {
			/**
			 *
			 */
			if (p.y > screen_y[0]) {
				if (p.y > screen_y[1]) {
					return Zone.NULL;
				}
			} else {
				return Zone.NULL;
			}
			if (p.x > screen_x[0]) {
				if (p.x > screen_x[1]) {
					if (p.x > screen_x[2]) {
						if (p.x > screen_x[3]) {
							return Zone.NULL;
						}
						return Zone.RIGHT;
					}
					return Zone.SELECT;
				}
				return Zone.LEFT;
			}
			return Zone.NULL;
		}

		/**
		 *
		 */
		public boolean Moved(int x, int y) {
			return (Math.abs(p_down.x - x) > p_tol.x) || (Math.abs(p_down.y - y) > p_tol.y);
		}

		/**
		 *
		 */
		public int MovedX(int x) {
			return (x - p_move.x) / p_resolusion.x;
		}

		public int MovedY(int y) {
			return (y - p_move.y) / p_resolusion.y;
		}

		public void MoveX(int n) {
			p_move.x += (n * p_resolusion.x);
		}

		public void MoveY(int n) {
			p_move.y += (n * p_resolusion.y);
		}
		/**
		 *
		 */
		public boolean isShort(long time, int x, int y) {
			if (((time - t_down) > Definitions.TOUCH_TIMEOUT)) {
				return false;
			}
			if (((time - t_down) < Definitions.MIN_TIME)) {
				return false;
			}
			return !Moved(x, y);
		}
		/**
		 *
		 */
		public boolean isLong(long time, int x, int y) {
			if (((time - t_down) < Definitions.LONG_TOUCH_TIMEOUT)) {
				return false;
			}
			return !Moved(x, y);
		}
		/**
		 */
		public class Point {

			public Point(int x, int y) {
				this.x = x;
				this.y = y;
			}
			public int x;
			public int y;
		}
		/**
		 */
		public enum Zone {

			NULL, RIGHT, SELECT, LEFT
		}
		/**
		 */
		public enum State {

			DOWN, MOVE, PRESS, UP
		}
		/**
		 */
		public Zone zone;
		public State state;
		public long t_down;
		public long t_move;
		public long t_up;
		public Point p_down;
		public Point p_move;
		public Point p_up;
		public Point p_tol;
		public Point p_resolusion;
		public int screen_x[];
		public int screen_y[];
	}
	/**
	 *
	 */
	private static class Scroll {

		public Scroll() {
			t = 0;
			x_pos = 0.0f;
			y_pos = 0.0f;
			x_move_pos = 0.0f;
			y_move_pos = 0.0f;
			x_pow = new Decay(0.5f);
			y_pow = new Decay(0.5f);
			x_resolution = Definitions.TOUCH_MOVE_X;
			y_resolution = Definitions.TOUCH_MOVE_Y;
		}
		/**
		 */
		public void SetScreen(int x, int y, float dx, float dy) {
			x_resolution = (int) ((float) Definitions.TOUCH_MOVE_X * dx);
			y_resolution = (int) ((float) Definitions.TOUCH_MOVE_Y * dy);
		}
		/**
		 */
		public void Down(int x, int y) {
			x_pow = new Decay(0.5f);
			y_pow = new Decay(0.5f);
			x_move_pos = x_pos = x;
			y_move_pos = y_pos = y;
		}
		/**
		 */
		public void Update(int x, int y) {
			x_pow.update(x - x_pos);
			y_pow.update(y - y_pos);
			x_move_pos = x_pos = x;
			y_move_pos = y_pos = y;
		}
		/**
		 */
		public void Up() {
			if (Math.abs(x_pow.get()) > Math.abs(y_pow.get())) {
				x_pow = new Decay(Definitions.DECAY_SCROLL, x_pow.get());
			} else {
				y_pow = new Decay(Definitions.DECAY_SCROLL, y_pow.get());
			}
			x_move_pos = x_pos = 0.0f;
			y_move_pos = y_pos = 0.0f;
		}
		/**
		 */
		public int MovedX() {
			x_pos += (x_pow.get());
			return (int) ((x_pos - x_move_pos) / x_resolution);
		}
		
		public int MovedY() {
			y_pos += (y_pow.get());
			return (int) ((y_pos - y_move_pos) / y_resolution);
		}

		public void MoveX(int n) {
			x_move_pos += (n * x_resolution);
		}

		public void MoveY(int n) {
			y_move_pos += (n * y_resolution);
		}

		/**
		 */
		public void Break() {
			if (Math.abs(x_pow.get()) < (x_resolution >> 4)) {
				x_pow.set(0);
			} else {
				x_pow.update(0);
			}
			if (Math.abs(y_pow.get()) < (y_resolution >> 4)) {
				y_pow.set(0);
			} else {
				y_pow.update(0);
			}
		}
		/**
		 */
		public long t;
		public float x_pos;
		public float y_pos;
		public float x_move_pos;
		public float y_move_pos;
		public Decay x_pow;
		public Decay y_pow;
		public int x_resolution;
		public int y_resolution;
	}
	/**
	 *
	 */
	private static class Pointer {

		public Pointer() {
			zone = Zone.STOP;
			__start = 0;
			__stop = 0;
		}

		/**
		 *
		 */
		public void Set(long start, long stop) {
			this.__start = start;
			this.__stop = stop;
		}

		/**
		 * Get Zone
		 *
		 * @param time
		 * @return
		 */
		public Zone GetZone(long time) {
			if (time > __start) {
				if (time > __stop) {
					return Zone.STOP;
				}
				return Zone.MOVE;
			}
			if (time > __stop) {
				return Zone.WAITING;
			}
			if (__start > __stop) {
				return Zone.MOVE;
			}
			return Zone.WAITING;
		}

		enum Zone {

			WAITING, MOVE, STOP
		}

		public Zone zone;
		/**
		 */
		private long __start;
		private long __stop;
	}
	/**
	 *
	 */
	final private Tap __tap;

	final private Scroll __scroll;

	final private Pointer __pointer;
	/**
	 *
	 */
	final private Decay __pow;
	/**
	 *
	 */
	private State __state;
	/**
	 *
	 */
	public PointerControl(AppI app) {
		super(
			app,
			new int[]{Fusion.Source.ACC},
			new int[]{Fusion.Source.ACC, Fusion.Source.ROT},
			new int[]{Fusion.Output.Y, Fusion.Output.P},
			1
		);
		/**/
		__tap = new Tap();

		__scroll = new Scroll();

		__pointer = new Pointer();
		/**/
		__pow = new Decay(Definitions.DECAY_POW);
		/**/
		__state = State.KEY;
	}

	/**
	 * play
	 *
	 * @param retries
	 * @return
	 */
	@Override
	protected Integer play(Integer retries) {
		super._reset();
		/**
		 * check initialization
		 */
		if (__tap.screen_y == null || __tap.screen_x == null) {
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
		if (data instanceof App.Screen) {
			App.Screen tmp = (App.Screen) data;
			/**
			 */
			__tap.SetScreen(tmp.getX(), tmp.getY(), tmp.getDX(), tmp.getDY());
			/**/
			__scroll.SetScreen(tmp.getX(), tmp.getY(), tmp.getDX(), tmp.getDY());
			/**/
		}
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof Touch) {
			Touch tmp = (Touch) data;
			/**
			 */
			if (tmp.getID() == Definitions.POINTER_ID) {
				/**
				 */
				switch (tmp.getType()) {
					case DOWN: {
						__ProcessDown(tmp);
						break;
					}
					case UP: {
						__ProcessUp(tmp);
						break;
					}
					case MOVE: {
						__ProcessMove(tmp);
						break;
					}
					case RESET: {
						send(new Command(UserInput.Button.BRESET, UserInput.Action.SET));
						__pow.set(0);
						break;
					}
				}
			}
		} if (data instanceof Key) {
			send(data);
		} else {
			if (__tap.state != Tap.State.UP) {
				if (super._ready()) {
					super.onRun(data);
				} else {
					super.onInit(data);
				}
			} else {
				/**/
				super._reset();
			}
		}
		/**
		 * scroll
		 */
		__ProcessScroll(data);
	}

	/**
	 * process data out
	 *
	 * @param data
	 */
	private boolean __reset = true;

	@Override
	protected Boolean send(Data data) {
		/**
		 */
		if (data instanceof DirectionY) {
			/**
			 */
			return __SendPointer((DirectionY) data);

		} else if (data instanceof RotationPower) {
			/**
			 */
			__pow.update(((RotationPower) data).getPower());
			/**
			 */
			if (__pow.get() <= Definitions.MIN_POW) {
				/**
				 */
				if (!__reset) {
					super.send(new Command(UserInput.Button.BRESET, UserInput.Action.SET));
					/**
					 */
					__reset = true;
				}
			} else {
				__reset = false;
			}
			return true;

		} else if (data instanceof Command) {
			/**
			 */
			return super.send(data);
		} else if (data instanceof Key) {
			/**
			 */
			return super.send(data);
		}
		return false;
	}

	/**
	 *
	 */
	private Position getPosition(DirectionY dir) {
		return new Position((float) Math.atan2(dir.getX(), dir.getY()), (float) Math.asin(dir.getZ()));
	}

	/**
	 * Process Tap
	 */
	private void __ProcessDown(Touch touch) {
		logger(LogType.INFO, "down=" + SystemClock.uptimeMillis());
		/**
		 * update
		 */
		__tap.state = Tap.State.DOWN;
		__tap.p_down.x = touch.getX();
		__tap.p_down.y = touch.getY();
		__tap.t_down = touch.getTime();
		__tap.zone = __tap.GetZone(__tap.p_down);
		/**
		 *
		 */
		__scroll.Down(touch.getX(), touch.getY());
	}

	/**
	 *
	 * @param touch
	 */
	private void __ProcessMove(Touch touch) {
		/**
		 * process
		 */
		switch (__tap.state) {
			case DOWN: {
				/**
				 */
				if (__tap.Moved(touch.getX(), touch.getY())) {
					__tap.p_move.x = __tap.p_down.x;
					__tap.p_move.y = __tap.p_down.y;
					__tap.t_move = touch.getTime();
					__tap.state = Tap.State.MOVE;
					/**/
					__state = State.KEY;
					break;
				}
				/**
				 */
				if (((touch.getTime() - __tap.t_down) < Definitions.MIN_TIME)) {
					break;
				}
				if (((touch.getTime() - __tap.t_down) < Definitions.LONG_TOUCH_TIMEOUT)) {
					/**
					 */
					if (__pow.get() <= Definitions.MIN_POW) {
						break;
					}
				}
				/**
				 */
				Tap.Zone zone = __tap.GetZone(__tap.p_down);
				/**
				 */
				switch (zone) {
					case SELECT: {
						send(new Command(UserInput.Button.BRESET, UserInput.Action.SET));
						/**
						 */
						__tap.state = Tap.State.PRESS;
						/**
						 */
						__pointer.Set(
							touch.getTime() + Definitions.MIN_TOUCH_TIMEOUT,
							touch.getTime() + Definitions.MAX_TIME
						);
						break;
					}
					case RIGHT: {
						/**
						 */
						send(new Command(UserInput.Button.BRIGHT, UserInput.Action.SET));
						/**
						 */
						__tap.state = Tap.State.PRESS;

						break;
					}
					case LEFT: {
						send(new Command(UserInput.Button.BLEFT, UserInput.Action.SET));
						/**
						 */
						__tap.state = Tap.State.PRESS;
						/**
						 */
						__pointer.Set(
							touch.getTime() + Definitions.MIN_TOUCH_TIMEOUT,
							touch.getTime() + Definitions.MAX_TIME
						);
						break;
					}
				}
				/**
				 * update
				 */
				__tap.zone = zone;
				__tap.p_move.x = touch.getX();
				__tap.p_move.y = touch.getY();
				break;
			}
			case PRESS: {
				if (__tap.isLong(touch.getTime(), touch.getX(), touch.getY())) {
					/**/
					__state = State.POINTER;
					/**/
				}else if (__tap.Moved(touch.getX(), touch.getY())) {
					__ProcessUp(touch);
					__ProcessDown(touch);
				} 
				break;
			}
			case MOVE: {
				/**
				 *
				 */
				__scroll.Update(touch.getX(), touch.getY());
				/**
				 *
				 */
				int MovedX = __tap.MovedX(touch.getX());
				for (; MovedX > 0; MovedX--, __tap.MoveX(1)) {
					send(new Command(UserInput.Button.KRIGHT, UserInput.Action.SET));
					send(new Command(UserInput.Button.KRIGHT, UserInput.Action.CLEAR));
				}
				for (; MovedX < 0; MovedX++, __tap.MoveX(-1)) {
					send(new Command(UserInput.Button.KLEFT, UserInput.Action.SET));
					send(new Command(UserInput.Button.KLEFT, UserInput.Action.CLEAR));
				}

				/**
				 *
				 */
				int MovedY = __tap.MovedY(touch.getY());
				for (; MovedY > 0; MovedY--, __tap.MoveY(1)) {
					send(new Command(UserInput.Button.KDOWN, UserInput.Action.SET));
					send(new Command(UserInput.Button.KDOWN, UserInput.Action.CLEAR));
				}
				for (; MovedY < 0; MovedY++, __tap.MoveY(-1)) {
					send(new Command(UserInput.Button.KUP, UserInput.Action.SET));
					send(new Command(UserInput.Button.KUP, UserInput.Action.CLEAR));
				}
				/**
				 *
				 */

				break;
			}
		}
		/**
		 * update
		 */
		__tap.t_move = touch.getTime();
	}

	/**
	 *
	 * @param touch
	 */
	private void __ProcessUp(Touch touch) {
		switch (__tap.state) {
			case DOWN: {
				/**
				 *
				 */
				switch (__tap.zone) {
					case SELECT: {
						switch (__state) {
							case KEY: {
								send(new Command(UserInput.Button.KENTER, UserInput.Action.SET));
								send(new Command(UserInput.Button.KENTER, UserInput.Action.CLEAR));
								break;
							}
							case POINTER: {
								send(new Command(UserInput.Button.BLEFT, UserInput.Action.SET));
								send(new Command(UserInput.Button.BLEFT, UserInput.Action.CLEAR));
								break;
							}
						}
						break;
					}
					case RIGHT: {
						send(new Command(UserInput.Button.BRIGHT, UserInput.Action.SET));
						send(new Command(UserInput.Button.BRIGHT, UserInput.Action.CLEAR));
						break;
					}
					case LEFT: {
						send(new Command(UserInput.Button.BLEFT, UserInput.Action.SET));
						send(new Command(UserInput.Button.BLEFT, UserInput.Action.CLEAR));
						break;
					}
				}
				break;
			}
			case PRESS: {
				/**
				 *
				 */
				switch (__tap.zone) {
					case RIGHT: {
						send(new Command(UserInput.Button.BRIGHT, UserInput.Action.CLEAR));
						/**
						 *
						 */
						__pointer.Set(
							touch.getTime() + Definitions.WAIT_TIME,
							touch.getTime() + Definitions.WAIT_TIME
						);
						break;
					}
					case LEFT: {
						send(new Command(UserInput.Button.BLEFT, UserInput.Action.CLEAR));

						/**
						 *
						 */
						__pointer.Set(
							touch.getTime() + Definitions.MAX_TIME,
							touch.getTime() + Definitions.MIN_TIME
						);
						break;
					}
					case SELECT: {
						/**
						 *
						 */
						if (__tap.isShort(touch.getTime(), touch.getX(), touch.getY())) {
							switch (__state) {
								case KEY: {
									send(new Command(UserInput.Button.KENTER, UserInput.Action.SET));
									send(new Command(UserInput.Button.KENTER, UserInput.Action.CLEAR));
									break;
								}
								case POINTER: {
									send(new Command(UserInput.Button.BLEFT, UserInput.Action.SET));
									send(new Command(UserInput.Button.BLEFT, UserInput.Action.CLEAR));
									break;
								}
							}
						}
						/**
						 */
						__pointer.Set(
							touch.getTime() + Definitions.WAIT_TIME,
							touch.getTime() + Definitions.WAIT_TIME
						);
						break;
					}
				}
				break;
			}
			case MOVE: {
				/**
				 */
				__scroll.Up();
				break;
			}
		}
		/**
		 * update
		 */
		__tap.state = Tap.State.UP;
		__tap.t_up = touch.getTime();
		__tap.p_up.x = touch.getX();
		__tap.p_up.y = touch.getY();
	}

	/**
	 *
	 */
	private void __ProcessScroll(Data data) {
		/**
		 */
		if (!(data instanceof MoveI.Accelerometer)) {
			return;
		}
		/**
		 */
		switch (__tap.state) {
			case UP: {
				int MovedX = __scroll.MovedX();
				for (; MovedX > 0; MovedX--, __scroll.MoveX(1)) {
					send(new Command(UserInput.Button.KRIGHT, UserInput.Action.SET));
					send(new Command(UserInput.Button.KRIGHT, UserInput.Action.CLEAR));
				}
				for (; MovedX < 0; MovedX++, __scroll.MoveX(-1)) {
					send(new Command(UserInput.Button.KLEFT, UserInput.Action.SET));
					send(new Command(UserInput.Button.KLEFT, UserInput.Action.CLEAR));
				}

				/**
				 *
				 */
				int MovedY = __scroll.MovedY();
				for (; MovedY > 0; MovedY--, __scroll.MoveY(1)) {
					send(new Command(UserInput.Button.KDOWN, UserInput.Action.SET));
					send(new Command(UserInput.Button.KDOWN, UserInput.Action.CLEAR));
				}
				for (; MovedY < 0; MovedY++, __scroll.MoveY(-1)) {
					send(new Command(UserInput.Button.KUP, UserInput.Action.SET));
					send(new Command(UserInput.Button.KUP, UserInput.Action.CLEAR));
				}
				/**
				 */
				__scroll.Break();
				break;
			}
		}
	}

	/**
	 *
	 */
	private boolean __SendPointer(DirectionY data) {
		/**
		 */
		Pointer.Zone z = __pointer.GetZone(data.getTime());
		switch (z) {
			case WAITING: {
				/**
				 */
				__pointer.zone = z;
				/**
				 */
				return super.send(new LinkI.Echo(1));
			}
			case MOVE: {
				/**
				 */
				__pointer.zone = z;
				/**
				 */
				return super.send(getPosition(data));
			}
			case STOP: {
				/**
				 */
				__pointer.zone = z;
				/**
				 */

				__state = State.KEY;
				break;
			}
		}
		return true;
	}
}
