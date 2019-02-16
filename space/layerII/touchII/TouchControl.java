/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.touchII;

/**
 * imports
 */
import space.base.App;
import space.base.AppI;
import space.base.Data;
import space.base.Module;
import space.layerI.TouchI.Touch;

/**
 *
 * @author monteiro
 */
public class TouchControl extends Module {

	/**
	 * data
	 */
	public static class PowerDir extends Data {

		/**
		 *
		 */
		final private Integer[] _dir;

		/**
		 *
		 */
		public PowerDir(Integer x, Integer y, Integer z) {
			super();
			/*
			 */
			_dir = new Integer[]{x, y, z};
		}

		/**
		 *
		 */
		public Integer[] getDir() {
			return _dir;
		}

		public Integer getX() {
			return _dir[0];
		}

		public Integer getY() {
			return _dir[1];
		}

		public Integer getZ() {
			return _dir[2];
		}
		/**
		 * 
		 */
		public Boolean isEmpty(){
			return (_dir[0]==0) && (_dir[1]==0) && (_dir[2]==0);
		}
	}

	/**
	 * data
	 */
	public static class ViewDir extends Data {

		/**
		 *
		 */
		final private Integer[] _dir;

		/**
		 *
		 */
		public ViewDir(Integer y, Integer z) {
			super();
			/*
			 */
			_dir = new Integer[]{y, z};
		}

		/**
		 *
		 */
		public Integer[] getDir() {
			return _dir;
		}

		public Integer getY() {
			return _dir[1];
		}

		public Integer getZ() {
			return _dir[2];
		}
	}

	/**
	 *
	 */
	public static final Integer __TOUCHS_MAX__ = 2;

	public static final Integer __TOUCHS_POW__ = 2;

	public static final Integer __TOUCHS_VIEW__ = 1;
	/**
	 * map configure
	 */
	public static final Integer __TOL0__ = 5;

	public static final Integer __TOLn__ = 10;

	public static final Integer __POW__ = 2;

	public static final Integer __MIN__ = 1; //(mm)

	public static final Integer __MAX__ = 40; //(mm)
	/**
	 *
	 */
	private Integer _map_tol0 = 0;

	private Integer _map_toln = 0;

	private Integer _map_min = 0;

	private Integer _map_max = 0;

	private Double _map_f = 0.0;

	/**
	 *
	 */
	private final Integer[] _refx;
	private final Integer[] _refy;

	private final Integer[] _curx;
	private final Integer[] _cury;
	/**
	 *
	 */
	private Integer _n_touchs;

	/**
	 *
	 */
	public TouchControl(AppI app) {
		super(app, 20);
		/**
		 *
		 */
		_refx = new Integer[__TOUCHS_MAX__];
		_refy = new Integer[__TOUCHS_MAX__];
		_curx = new Integer[__TOUCHS_MAX__];
		_cury = new Integer[__TOUCHS_MAX__];
		/**
		 * initialization
		 */
		for (int i = 0; i < __TOUCHS_MAX__; i++) {
			_refx[i] = (int) (i * 1000);
			_refy[i] = (int) (i * 1000);
			_curx[i] = (int) (i);
			_cury[i] = (int) (i);
		}
		/**
		 * 
		 */
		_n_touchs = 0;
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onInit(Data data) {
		if (data instanceof App.Screen) {
			__Init(((App.Screen) data).getDX(), ((App.Screen) data).getDY());
		}
		/**
		 * 
		 */
		ClearPower();
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof Touch) {
			/**
			 */
			Touch t = (Touch) data;
			/**
			 * 
			 */
			logger(
				LogType.INFO, "id=" + t.getID()
				+ " x0=" + _curx[0] + " x1=" + _curx[1]
				+ " y0=" + _cury[0] + " y1=" + _cury[1]
			);
			/*
			 */
			switch (t.getType()) {

				case DOWN: {
					_n_touchs += 1;
					/*
					 */
					_refx[t.getID() % __TOUCHS_MAX__] = t.getY();
					_refy[t.getID() % __TOUCHS_MAX__] = t.getX();
					/*
					 *
					 */
					_curx[t.getID() % __TOUCHS_MAX__] = t.getY();
					_cury[t.getID() % __TOUCHS_MAX__] = t.getX();
					/*
					 * 
					 */
					break;
				}
				case MOVE: {
					/*
					 */
					_curx[t.getID() % __TOUCHS_MAX__] = t.getY();
					_cury[t.getID() % __TOUCHS_MAX__] = t.getX();
					/* 
					 */
					if (_n_touchs.equals(__TOUCHS_POW__)) {

//						logger(
//							LogType.INFO, "id=" + t.getID()
//							+ " r0=" + _refy[0] + " r1=" + _refy[1]
//							+ " c0=" + _cury[0] + " c1=" + _cury[1]
//						);
						/**
						 * verify if touch are lined up
						 */
						if (Math.abs(_refy[0] - _refy[1]) > _map_tol0) {
							ClearPower();
							break;
						}
						if (Math.abs(_cury[0] - _cury[1]) > _map_toln) {
							ClearPower();
							break;
						}
						/**
						 * intuition
						 */
						Integer x = __Gap(_curx) - __Gap(_refx);

						Integer y = __Mean(_curx) - __Mean(_refx);

						Integer z = __Mean(_cury) - __Mean(_refy);
						/**
						 * add normalize direction add values
						 */
						x = __Direction(x);
						/**
						 *
						 */
						y *= 2;
						/**
						 * remove overlap
						 */
						x = __RemoveAbs(x, y);
						/**
						 * send
						 */
						send(new PowerDir(__Map(x), __Map(y), __Map(z)));
						/**
						 *
						 */
					} else if (_n_touchs.equals(__TOUCHS_VIEW__)) {
						/**
						 * for X
						 */
						Integer x = __Map(__DiffX(0));

						/**
						 * for y
						 */
						Integer y = __Map(__DiffY(0));
						/**
						 *
						 */
						send(new ViewDir(x, y));
					}
					break;
				}
				case UP: {
					/*
					 */
					_n_touchs -= 1;
					/*
					 */
					ClearPower();
					/**
					 * 
					 */
					break;
				}
				case RESET: {
					/*
					 */
					_n_touchs = 0;
					/*
					 */
					ClearPower();
					/**
					 * 
					 */
					break;
				}
			}
		}
	}

	/**
	 *
	 */
	private void __Init(Float dx, Float dy) {
		_map_tol0 = (int) (dx * __TOL0__);

		_map_toln = (int) (dx * __TOLn__);

		_map_min = (int) (dx * __MIN__);

		_map_max = (int) (dx * __MAX__);

		_map_f = (100.0 / Math.pow(_map_max - _map_min, __POW__));
	}

	/**
	 *
	 */
	private Integer __Map(Integer val) {
		if (val >= _map_min) {
			return (int) (_map_f * Math.pow(val, __POW__));
		}
		if (val <= (-_map_min)) {
			return (int) (-_map_f * Math.pow(-val, __POW__));
		}
		return 0;
	}

	/**
	 * math operation
	 */
	private Integer __Mean(Integer[] vec) {
		Integer sum = 0;
		for (Integer v : vec) {
			sum += v;
		}
		return sum / vec.length;
	}

	private Integer __Gap(Integer[] vec) {
		return vec[1] - vec[0];
	}

	private Integer __RemoveAbs(Integer v, Integer r) {
		int abs_v = Math.abs(v);
		int abs_r = Math.abs(r);
		/**
		 *
		 */
		if (abs_v <= abs_r) {
			return 0;
		}
		/**
		 *
		 */
		return v > 0 ? abs_v - abs_r : abs_r - abs_v;
	}

	/**
	 *
	 */
	private Integer __DiffX(Integer touch) {
		return _curx[touch] - _refx[touch];
	}

	private Integer __DiffY(Integer touch) {
		return _curx[touch] - _refx[touch];
	}

	private Integer __Direction(Integer v) {
		return (_refx[1] - _refx[0]) > 0 ? v : -v;
	}

	/**
	 * actions
	 */
	private void ClearPower() {
		for (int i = 0; i < __TOUCHS_MAX__; i++) {
			_refx[i] = (int) (i * 1000);
			_refy[i] = (int) (i * 1000);
		}
		send(new PowerDir(0, 0, 0));
	}
}
