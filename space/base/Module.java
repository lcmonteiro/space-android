/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.base;

import android.content.SharedPreferences;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author monteiro
 */
public class Module extends Thread implements ModuleI {

	/**
	 * Data Types
	 */
	protected static class Control extends Data {

		/**
		 */
		public enum Type {

			OPEN, PAUSE, PLAY, CLOSE
		}
		/**
		 */
		protected final Type _type;

		/**
		 */
		public Control(Type type) {
			super();
			/**
			 *
			 */
			_type = type;
		}

		/**
		 */
		public Type getType() {
			return _type;
		}
	}

	/**
	 *
	 */
	public static class ModuleControl extends Control {

		public ModuleControl(Control.Type type) {
			super(type);
		}
	}

	/**
	 *
	 */
	public static class ModuleException extends Data {

		/**
		 */
		final protected String _module;
		final protected String _what;

		/**
		 */
		public ModuleException(String module, String what) {
			super();
			/**
			 */
			_module = module;
			_what = what;
		}

		/**
		 */
		public String getModule() {
			return _module;
		}

		/**
		 */
		public String getWhat() {
			return _what;
		}
	}

	/**
	 *
	 */
	public static class LogEntry extends Data {

		/**
		 */
		public enum Type {

			INFO, WARNING, ERROR
		}
		/**
		 */
		protected final String _txt;
		protected final Type _type;
		protected final int _attr;

		/**
		 */
		public LogEntry(String txt) {
			this(Type.INFO, txt, 0);
		}

		public LogEntry(String txt, int attr) {
			this(Type.INFO, txt, attr);
		}

		public LogEntry(Type type, String txt) {
			this(type, txt, 0);
		}

		public LogEntry(Type type, String txt, int attr) {
			super();
			/**
			 *
			 */
			_type = type;
			_txt = txt;
			_attr = attr;
		}

		/**
		 */
		public Type getType() {
			return _type;
		}

		public int getAttr() {
			return _attr;
		}

		public String getText() {
			return _txt;
		}
	}

	/**
	 * screen
	 */
	public static class Screen extends Data {

		/**
		 */
		protected final Integer _x;
		protected final Integer _y;
		protected final Float _dx;
		protected final Float _dy;

		/**
		 */
		public Screen(Integer x, Integer y, Float dx, Float dy) {
			super();
			/**
			 *
			 */
			_x = x;
			_y = y;
			_dx = dx;
			_dy = dy;
		}

		/**
		 */
		public Integer getX() {
			return _x;
		}

		public Integer getY() {
			return _y;
		}

		public Float getDX() {
			return _dx;
		}

		public Float getDY() {
			return _dy;
		}
	}

	/**
	 * Vector
	 */
	public static class Vector extends Data {

		/**
		 */
		final private Integer _x;
		final private Integer _y;

		/**
		 */
		public Vector(Integer x, Integer y) {
			super();
			/**
			 *
			 */
			_x = x;
			_y = y;
		}

		/**
		 */
		public Integer getX() {
			return _x;
		}

		public Integer getY() {
			return _y;
		}
	}

	/**
	 *
	 */
	public static class Vector3D extends Data {

		/**
		 */
		final private Integer _x;
		final private Integer _y;
		final private Integer _z;

		/**
		 */
		public Vector3D(Integer x, Integer y, Integer z) {
			super();
			/**
			 *
			 */
			_x = x;
			_y = y;
			_z = z;
		}

		/**
		 */
		public Integer getX() {
			return _x;
		}

		public Integer getY() {
			return _y;
		}

		public Integer getZ() {
			return _z;
		}
	}

	/**
	 */
	public enum Status {

		DISABLE, INIT, PAUSE, RUN
	}
	/**
	 * queue IN
	 */
	final protected BlockingQueue<Data> __in;

	/**
	 * queues OUT
	 */
	final private List<List<ModuleI>> __out;
	/**
	 *
	 */
	protected Status _status;

	protected Integer _retries;

	protected AppI _appI;

	/**
	 * @param size
	 */
	public Module(AppI app, Integer size) {
		super();
		/**/
		__in = new LinkedBlockingQueue<Data>(size);
		/**/
		__out = new ArrayList<List<ModuleI>>();
		__out.add(new ArrayList<ModuleI>());
		__out.add(new ArrayList<ModuleI>());
		__out.add(new ArrayList<ModuleI>());
		/**/
		_status = Status.DISABLE;
		/**/
		_retries = 0;
		/**/
		_appI = app;
	}

	/**
	 * disconnect modules
	 *
	 * @param module
	 */
	public synchronized void disconnect(ModuleI module) {
		__out.get(0).remove(module);
	}
	
	public synchronized void disconnect(ModuleI module, int index) {
		__out.get(index).add(module);
	}
	/**
	 * connect modules
	 *
	 * @param module
	 */
	public synchronized void connect(ModuleI module) {
		__out.get(0).add(module);
	}

	public synchronized void connect(ModuleI module, int index) {
		__out.get(index).add(module);
	}

	/**
	 * get queue size
	 */
	public synchronized int size() {
		return __in.size();
	}

	/**
	 * Receive with address
	 *
	 * @param name
	 * @param data
	 */
	public void receive(String name, Data data) {
		if (name.equals(getName())) {
			receive(data);
		}
	}

	/**
	 * Receive
	 *
	 * @param data
	 */
	public void receive(Data data) {
		if (data == null) {
			return;
		}
		switch (getStatus()) {
			case DISABLE: {
				logger(LogType.INFO, "::receive::Disable");
				break;
			}
			default: {
				try {
					__in.add(data);
				} catch (IllegalStateException e) {
					logger(LogType.ERROR, "::receive::Queue");
				}
			}
		}
	}

	/**
	 */
	@Override
	public void run() {
		setName(getClass().getSimpleName());
		/**
		 */
		Data d = null;
		setStatus(Status.INIT);
		do {
			try {
				d = __in.take();

			} catch (InterruptedException e) {

				logger(LogType.ERROR, "::run::Queue");
			}
		} while (process(d) == Result.CONTINUE);
		setStatus(Status.DISABLE);
	}
	/**
	 * load
	 */
	public void load(SharedPreferences prefs) {
	}
	/**
	 * save
	 */
	public void save(SharedPreferences.Editor edit) {
	}
	/**
	 * open module
	 *
	 * @return
	 */
	protected Integer open(Integer retries) {
		_status = Status.PAUSE;
		return 0;
	}

	
	/**
	 * process data
	 *
	 * @param data
	 * @return
	 */
	public enum Result {

		BREAK, CONTINUE
	}

	/**
	 *
	 */
	private Result process(Data data) {

		/**
		 * callback
		 */
		switch (getStatus()) {
			case INIT: {
				/**
				 * control actions
				 */
				if (data instanceof ModuleControl) {
					switch (((ModuleControl) data).getType()) {
						case OPEN: {
							long time;
							try {
								time = open(_retries);
							} catch (Exception e) {
								time = 1;
							}
							/**
							 *
							 */
							if (time == 0) {
								_retries = 0;
								_status = Status.PAUSE;
							} else {
								/**
								 * save
								 */
								receive(data);
								/**
								 * sleep
								 */
								try {
									sleep(time);
								} catch (InterruptedException ex) {
									return Result.BREAK;
								}
								_retries += 1;
							}
							break;
						}
						case PLAY: {
							/**
							 * save
							 */
							receive(data);
							break;
						}
						case PAUSE: {
							/**
							 * save
							 */
							receive(data);
							break;
						}
						case CLOSE: {
							/**
							 * end thread
							 */
							return Result.BREAK;
						}
					}
				} else {
					/**
					 *
					 */
					onInit(data);
				}
				break;
			}
			case PAUSE: {
				/**
				 * control actions
				 */
				if (data instanceof ModuleControl) {
					switch (((ModuleControl) data).getType()) {
						case OPEN: {
							logger(LogType.WARNING, "Invalid Action for State");
							break;
						}
						case PLAY: {
							long time;
							try {
								time = play(_retries);
							} catch (Exception e) {
								time = 1;
							}
							/**
							 *
							 */
							if (time == 0) {
								_status = Status.RUN;
								_retries = 0;
							} else {
								/**
								 * save
								 */
								receive(data);
								/**
								 * sleep
								 */
								try {
									sleep(time);
								} catch (InterruptedException ex) {
									return Result.BREAK;
								}
								_retries += 1;
							}
							break;
						}
						case PAUSE: {
							logger(LogType.WARNING, "Invalid Action for State");
							break;
						}
						case CLOSE: {
							long time = close(_retries);
							/**
							 * end thread
							 */
							if (time == 0) {
								_status = Status.DISABLE;
								return Result.BREAK;
							}
							break;
						}
					}
				} else {
					/**
					 *
					 */
					onPause(data);
				}
				break;
			}
			case RUN: {
				/**
				 * control actions
				 */
				if (data instanceof ModuleControl) {
					switch (((ModuleControl) data).getType()) {
						case OPEN:
						case PLAY: {
							logger(LogType.WARNING, "Invalid Action for State");
							break;
						}
						case PAUSE: {
							long time;
							try {
								time = pause(_retries);
							} catch (Exception e) {
								time = 1;
							}
							/**
							 *
							 */
							if (time == 0) {
								_retries = 0;
								_status = Status.PAUSE;
							} else {
								/**
								 * save
								 */
								receive(data);
								/**
								 * sleep
								 */
								try {
									sleep(time);
								} catch (InterruptedException ex) {
									return Result.BREAK;
								}
								_retries += 1;
							}
							break;
						}
						case CLOSE: {
							long time = close(_retries);
							/**
							 * end thread
							 */
							if (time == 0) {
								_status = Status.DISABLE;
								return Result.BREAK;
							}
							break;
						}
					}
				} else {
					/**
					 *
					 */
					onRun(data);
				}
				break;
			}
			default: {
				logger(LogType.WARNING, "Invalid State");
			}
		}
		return Result.CONTINUE;
	}

	/**
	 * callback
	 */
	public void onInit(Data data) {
	}

	public void onPause(Data data) {
	}

	public void onRun(Data data) {
	}

	/**
	 * pause module
	 *
	 * @return
	 */
	protected Integer pause(Integer retries) {
		return 0;
	}

	/**
	 *
	 * start/restart module
	 *
	 * @return
	 */
	protected Integer play(Integer retries) {
		return 0;
	}

	/**
	 * close module
	 *
	 * @return
	 */
	protected Integer close(Integer retries) {
		return 0;
	}

	/**
	 * get status
	 *
	 * @return
	 */
	public synchronized Status getStatus() {
		return _status;
	}

	/**
	 * set Status
	 *
	 * @return
	 */
	public synchronized void setStatus(Status status) {
		_status = status;
	}

	/**
	 * Send command
	 *
	 * @param data command
	 * @return
	 */
	protected Boolean send(Data data) {
		for (ModuleI m : __out.get(0)) {
			m.receive(data);
		}
		return Boolean.TRUE;
	}

	protected Boolean send(String module, Data data) {
		for (ModuleI m : __out.get(0)) {
			m.receive(module, data);
		}
		return Boolean.TRUE;
	}

	protected Boolean send(Module module, Data data) {
		for (ModuleI m : __out.get(0)) {
			m.receive(module.getName(), data);
		}
		return Boolean.TRUE;
	}

	protected Boolean send(Data data, int index) {
		for (ModuleI m : __out.get(index)) {
			m.receive(data);
		}
		return Boolean.TRUE;
	}

	protected Boolean send(String module, Data data, int index) {
		for (ModuleI m : __out.get(index)) {
			m.receive(module, data);
		}
		return Boolean.TRUE;
	}

	protected Boolean send(Module module, Data data, int index) {
		for (ModuleI m : __out.get(index)) {
			m.receive(module.getName(), data);
		}
		return Boolean.TRUE;
	}

	/**
	 * log
	 */
	public enum LogType {

		ERROR, WARNING, INFO, DEBUG
	}

	public void logger(LogType type, String msg) {
		switch (type) {
			case DEBUG: {
				Log.d(getName(), msg != null ? msg : "");
				break;
			}
			case INFO: {
				Log.i(getName(), msg != null ? msg : "");
				break;
			}
			case WARNING: {
				Log.w(getName(), msg != null ? msg : "");
				break;
			}
			case ERROR: {
				Log.e(getName(), msg != null ? msg : "");
				break;
			}
		}
	}

}
