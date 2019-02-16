/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.fusionII;

/**
 * imports
 */
import space.base.AppI;
import space.base.Data;
import space.base.Module;
import space.layerI.MoveI.Accelerometer;
import space.layerI.MoveI.Gyroscope;
import space.layerI.MoveI.Magnetic;
import space.under.vectors4D.Direction;
import space.under.vectors4D.Movement;
import space.under.vectors4D.Position;

/**
 *
 * @author monteiro
 */
public class Fusion extends Module {

	/**
	 * data
	 */
	protected static class FusionData extends Data {

		/**
		 *
		 */
		final private Direction _dir;

		/**
		 *
		 */
		public FusionData(float x, float y, float z) {
			super();
			/*
			 */
			_dir = new Direction(x, y, z);
		}

		/**
		 *
		 */
		public float getX() {
			return _dir.getX();
		}

		public float getY() {
			return _dir.getY();
		}

		public float getZ() {
			return _dir.getZ();
		}
	}

	public static class DirectionX extends FusionData {

		/**
		 *
		 */
		public DirectionX(float x, float y, float z) {
			super(x, y, z);
		}
	}

	public static class DirectionY extends FusionData {

		/**
		 *
		 */
		public DirectionY(float x, float y, float z) {
			super(x, y, z);
		}
	}

	public static class DirectionZ extends FusionData {

		/**
		 *
		 */
		public DirectionZ(float x, float y, float z) {
			super(x, y, z);
		}
	}

	protected static class Power extends Data {

		/**
		 *
		 */
		final private float __p;

		/**
		 *
		 */
		public Power(float p) {
			super();
			/*
			 */
			__p = p;
		}

		/**
		 *
		 */
		public float getPower() {
			return __p;
		}
	}

	public static class RotationPower extends Power {

		/**
		 *
		 */
		public RotationPower(float p) {
			super(p);
		}
	}

	/**
	 * definitions
	 */
	public static class Source {

		public static final int ACC = 0;
		public static final int MAG = 1;
		public static final int ROT = 2;
		public static final int N = 3;
	}

	public static class Output {

		public static final int X = 0;
		public static final int Y = 1;
		public static final int Z = 2;
		public static final int P = 3;
		public static final int N = 4;
	}

	protected static class Index {

		public static final int CURRENT = 0;
		public static final int LAST_1 = 1;
		public static final int LAST_2 = 2;
	}

	protected static class Filter {

		public static final int MAG = 5;
		public static final int ACC = 5;
	}

	protected static class Range {

		public static final int MAG_DOWN = 20;
		public static final int MAG_UP = 80;
		public static final int ACC_DOWN = 9;
		public static final int ACC_UP = 11;
	}

	/**
	 * memory
	 */
	protected Position __acc[];
	protected Position __mag[];
	protected Position __rot[];
	protected Position __pos[];
	protected Direction __basis[];
	/**
	 * memory
	 */
	protected boolean __init[];
	protected boolean __run[];
	protected boolean __out[];
	/**
	 * send period
	 */
	protected int __trigger;
	protected int __counter;

	/**
	 *
	 */
	public Fusion(AppI app, int[] init, int[] run, int[] out, int trigger) {
		super(app, 200);
		/**
		 * update sources for initialization
		 */
		__init = new boolean[]{false, false, false};

		for (int i : init) {
			if (i < Source.N) {
				__init[i] = true;
			}
		}
		/**
		 * update sources for running
		 */
		__run = new boolean[]{false, false, false};

		for (int i : run) {
			if (i < Source.N) {
				__run[i] = true;
			}
		}
		/**
		 *
		 */
		__out = new boolean[]{false, false, false, false};

		for (int i : out) {
			if (i < Output.N) {
				__out[i] = true;
			}
		}
		/**
		 *
		 */
		__trigger = trigger;
		/**
		 * reset memory
		 */
		_reset();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected Integer play(Integer retries) {
		if (_ready()) {
			return 0;
		}
		return 1;
	}

	@Override
	protected synchronized Integer pause(Integer retries) {
		_reset();
		return 0;
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onInit(Data data) {

		if (data instanceof Gyroscope) {
			/**
			 *
			 */
			Gyroscope tmp = (Gyroscope) data;
			/**
			 *
			 */
			__rot[Index.LAST_1] = __rot[Index.CURRENT];
			__rot[Index.LAST_2] = __rot[Index.LAST_1];
			__rot[Index.CURRENT] = new Position(tmp.getPoint(), tmp.getTime());
			/**
			 *
			 */
			if (!__init[Source.ACC]) {
				/**
				 *
				 */
				receive(new Accelerometer(tmp.getTime(), 0.0f, 0.0f, 1.0f));
			}
			/**
			 *
			 */
		} else if (data instanceof Accelerometer) {
			Accelerometer tmp = (Accelerometer) data;
			/**
			 *
			 */
			__acc[Index.LAST_2] = __acc[Index.LAST_1];
			__acc[Index.LAST_1] = __acc[Index.CURRENT];
			/**
			 *
			 */
			__acc[Index.CURRENT] = new Position(tmp.getPoint(), tmp.getTime());
			/**
			 *
			 */
			if (!__init[Source.MAG]) {
				/**
				 *
				 */
				receive(new Magnetic(tmp.getTime(), tmp.getZ(), tmp.getX(), tmp.getY()));
			}
		} else if (data instanceof Magnetic) {
			Magnetic tmp = (Magnetic) data;
			/**
			 *
			 */
			__mag[Index.LAST_2] = __mag[Index.LAST_1];
			__mag[Index.LAST_1] = __mag[Index.CURRENT];
			/**
			 *
			 */
			__mag[Index.CURRENT] = new Position(tmp.getPoint(), tmp.getTime());
			/**
			 *
			 */
			if (!__init[Source.ROT]) {
				/**
				 *
				 */
				receive(new Gyroscope(tmp.getTime(), 0.001f, 0.001f, 0.001f));
			}
		}
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onPause(Data data) {
		onInit(data);
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof Accelerometer) {

			if (__run[Source.ACC]) {
				/**
				 *
				 */
				Accelerometer tmp = (Accelerometer) data;
				/**
				 *
				 */
				__acc[Index.LAST_2] = __acc[Index.LAST_1];

				__acc[Index.LAST_1] = new Position(tmp.getPoint(), tmp.getTime());
				/**
				 *
				 */
			}

		} else if (data instanceof Magnetic) {

			if (__run[Source.MAG]) {
				/**
				 *
				 */
				Magnetic tmp = (Magnetic) data;
				/**
				 *
				 */
				__mag[Index.LAST_2] = __mag[Index.LAST_1];

				__mag[Index.LAST_1] = new Position(tmp.getPoint(), tmp.getTime());
				/**
				 *
				 */
			}

		} else if (data instanceof Gyroscope) {
			/**
			 * pre fusion
			 */
			__fusion_acc_mag(Index.LAST_2, data.getTime());
			/**
			 *
			 */
			if (__run[Source.ROT]) {
				/**
				 *
				 */
				Gyroscope tmp = (Gyroscope) data;
				/**
				 *
				 */
				__fusion_rot(new Position(tmp.getPoint(), tmp.getTime()));
				/**
				 *
				 */
			}
			__fusion_acc_mag(Index.LAST_1, data.getTime());
			/**
			 *
			 */
			__update_basis();
			/**
			 *
			 */
			if (__counter > __trigger) {
				/**
				 * send
				 */
				__send_data();
				/**
				 * reset counter
				 */
				__counter = 1;
			} else {
				/**
				 * update counter
				 */
				__counter++;
			}
		}
	}

	/**
	 *
	 */
	protected final boolean _ready() {
		return (__acc[Index.LAST_2] != null && __mag[Index.LAST_2] != null && __rot[Index.LAST_2] != null);
	}

	/**
	 *
	 */
	protected final void _reset() {
		/**
		 *
		 */
		__counter = 1;

		__acc = new Position[]{null, null, null};

		__mag = new Position[]{null, null, null};

		__rot = new Position[]{null, null, null};

		__basis = new Direction[]{null, null, null};
	}

	/**
	 *
	 * @param index
	 * @param time
	 */
	private void __fusion_acc_mag(int index, long time) {
		/**
		 * fusion mag
		 */
		if (__run[Source.MAG] && time == __mag[index].time()) {
			/**
			 * check value
			 */
			if (__mag[index].space() < Range.MAG_UP && __mag[index].space() > Range.MAG_DOWN) {
				/**
				 * update
				 */
				__mag[Index.CURRENT].self_time(time)
					.self_product(Filter.MAG).self_sum(__mag[index]).self_normalize();
			}
		}
		/**
		 * fusion acc
		 */
		if (__run[Source.ACC] && time == __acc[index].time()) {
			/**
			 * check value
			 */
			if (__acc[index].space() < Range.ACC_UP && __acc[index].space() > Range.ACC_DOWN) {
				/**
				 * update
				 */
				__acc[Index.CURRENT].self_time(time)
					.self_product(Filter.ACC).self_sum(__acc[index]).self_normalize();
			}
		}
	}

	/**
	 *
	 * @param index
	 * @param time
	 */
	private void __fusion_rot(Position rot) {
		/**
		 * delta time
		 */
		long dt = (rot.time() - __rot[Index.CURRENT].time());
		/**
		 * check delta time
		 */
		if (dt <= 100 && rot.space() > 0.0f) {
			/**
			 * update delta time based on last rot
			 */
			Movement move = new Movement(
				rot,
				rot.space(),
				(long) ((float) dt * 0.5f * (1.0f + __rot[Index.CURRENT].space() / rot.space()))
			);
			/**
			 */
			__acc[Index.CURRENT].self_rotate_inv(move);
			/**
			 */
			__mag[Index.CURRENT].self_rotate_inv(move);
		}
		/**
		 * update current rotation
		 */
		__rot[Index.CURRENT] = rot;
	}

	/**
	 * update basis
	 */
	private void __update_basis() {
		__basis[0] = new Direction(__acc[Index.CURRENT].cross_product(__mag[Index.CURRENT]));
		__basis[1] = new Direction(__acc[Index.CURRENT].cross_product_inv(__basis[0]));
		__basis[2] = new Direction(__acc[Index.CURRENT]);
		/**
		 * update mag if not enable
		 */
		if (!__run[Source.MAG]) {
			__mag[Index.CURRENT] = new Position(__basis[1], __mag[Index.CURRENT].time());
		}
	}

	/**
	 * update basis
	 */
	private void __send_data() {
		if (__out[Output.X]) {
			send(new DirectionX(__basis[0].getX(), __basis[1].getX(), __basis[2].getX()));
		}
		if (__out[Output.Y]) {
			send(new DirectionY(__basis[0].getY(), __basis[1].getY(), __basis[2].getY()));
		}
		if (__out[Output.Z]) {
			send(new DirectionZ(__basis[0].getZ(), __basis[1].getZ(), __basis[2].getZ()));
		}
		if (__out[Output.P]) {
			send(new RotationPower(__rot[Index.CURRENT].space()));
		}
	}

}
