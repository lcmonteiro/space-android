/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.base;

/**
 *
 */
import space.layerI.DisplayI;
import space.layerI.TouchI;
/**
 *
 */
import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.io.IOException;
import static java.lang.Thread.interrupted;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author monteiro
 */
public class AppFrontend extends App implements AppI {
	/**
	 */
	final protected Activity _activity;
	/**
	 *
	 */
	final protected List<Module> _applications;
	/**
	 * level I
	 */
	protected DisplayI _display = null;
	/**
	 */
	private LinkListener __listener;
	/**
	 */
	public AppFrontend(Activity activity) {
		super();
		/**
		 */
		_activity = activity;
		/**
		 */
		_applications = new ArrayList<Module>();
	}

	protected enum RegisterType {

		APPLICATION, SERVICE
	}

	/**
	 * registration
	 */
	protected void register(Module module) {
		register(module, RegisterType.APPLICATION);
	}

	/**
	 */
	protected void register(Module module, RegisterType type) {
		switch (type) {
			case APPLICATION:
				_applications.add(module);
				break;
			case SERVICE:
				_services.add(module);
				break;
		}
	}

	/**
	 *
	 */
	public void create() {
		/**
		 * main
		 */
		start();
		/**
		 * start services
		 */
		for (Module m : _services) {
			m.start();
		}
		/**
		 * wait to start services
		 */
		SystemClock.sleep(1000);
		/**
		 * open main
		 */
		receive(new ModuleControl(ModuleControl.Type.OPEN));
		/**
		 * open services
		 */
		for (Module m : _services) {
			m.receive(new ModuleControl(ModuleControl.Type.OPEN));
		}
	}

	/**
	 *
	 */
	public void open() {
		/**
		 * get metrics
		 */
		_activity.getWindowManager().getDefaultDisplay().getMetrics(_metrics);
		/**
		 *
		 */
		if (_display != null) {
			_activity.setContentView(
				_display.getLayout(), new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
				)
			);
		}
		/**
		 * start applications
		 */
		for (Module m : _applications) {
			m.start();
		}
		/**
		 * wait for apps starting
		 */
		SystemClock.sleep(1000);
		/**
		 * screen info
		 */
		Data screen_info = new Screen(
			_metrics.widthPixels,
			_metrics.heightPixels,
			(float) (_metrics.xdpi * 0.03937),
			(float) (_metrics.ydpi * 0.03937)
		);
		/**
		 * initialization touch module
		 */
		if (_touch != null) {
			_touch.receive(screen_info);
		}
		/**
		 * initialization display module
		 */
		if (_display != null) {
			_display.receive(screen_info);
		}
		/**
		 * initialization input module
		 */
		if (_input != null) {
			_input.receive(screen_info);
		}
		/**
		 * open modules
		 */
		for (Module m : _applications) {
			m.receive(new ModuleControl(ModuleControl.Type.OPEN));
		}
		/**
		 * play main
		 */
		receive(new ModuleControl(ModuleControl.Type.PLAY));
		/**
		 * play services
		 */
		for (Module m : _services) {
			m.receive(new ModuleControl(ModuleControl.Type.PLAY));
		}
	}

	/**
	 * pause module
	 */
	public void pause() {
		for (Module m : _applications) {
			m.receive(new ModuleControl(ModuleControl.Type.PAUSE));
		}
	}

	/**
	 * play
	 */
	public void play() {
		/**
		 * send play
		 */
		for (Module m : _applications) {
			m.receive(new ModuleControl(ModuleControl.Type.PLAY));
		}
	}

	/**
	 * close module
	 */
	public void close() {
		/**
		 * send close
		 */
		for (Module m : _applications) {
			m.receive(new ModuleControl(ModuleControl.Type.CLOSE));
		}
		/**
		 * pause main
		 */
		receive(new ModuleControl(ModuleControl.Type.PAUSE));
		/**
		 * pause services
		 */
		for (Module m : _services) {
			m.receive(new ModuleControl(ModuleControl.Type.PAUSE));
		}
		/**
		 *
		 */
		_applications.clear();
	}

	/**
	 *
	 */
	public void destroy() {
		/**
		 * close main
		 */
		receive(new ModuleControl(ModuleControl.Type.CLOSE));
		/**
		 * close services
		 */
		for (Module m : _services) {
			m.receive(new ModuleControl(ModuleControl.Type.CLOSE));
		}
		/**
		 *
		 */
		_services.clear();
	}
	/**
	 *
	 */
	private int __touch_count = 0;

	/**
	 *
	 */
	public void touch(MotionEvent event) {
		/**
		 *
		 */
		__touch_count++;
		/**
		 *
		 */
		if (_touch == null) {
			return;
		}
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_DOWN: {
				int i = event.getActionIndex();
				int id = event.getPointerId(i);
				/**
				 *
				 */
				_touch.receive(
					new TouchI.Touch(
						id, (int) event.getX(i), (int) event.getY(i), TouchI.Touch.Type.DOWN
					)
				);
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				int i = __touch_count % event.getPointerCount();
				int id = event.getPointerId(i);
				/**
				 *
				 */
				_touch.receive(
					new TouchI.Touch(
						id, (int) event.getX(i), (int) event.getY(i), TouchI.Touch.Type.MOVE
					)
				);
				break;
			}
			case MotionEvent.ACTION_POINTER_UP: {
				int i = event.getActionIndex();
				int id = event.getPointerId(i);
				/**
				 *
				 */
				_touch.receive(
					new TouchI.Touch(
						id, (int) event.getX(i), (int) event.getY(i), TouchI.Touch.Type.UP
					)
				);
				break;
			}
			case MotionEvent.ACTION_UP: {
				int i = event.getActionIndex();
				int id = event.getPointerId(i);
				/**
				 *
				 */
				_touch.receive(
					new TouchI.Touch(
						id, (int) event.getX(), (int) event.getY(), TouchI.Touch.Type.UP
					)
				);
				_touch.receive(
					new TouchI.Touch(
						0, 0, 0, TouchI.Touch.Type.RESET
					)
				);
				break;
			}
			case MotionEvent.ACTION_CANCEL: {
				_touch.receive(
					new TouchI.Touch(
						0, 0, 0, TouchI.Touch.Type.RESET
					)
				);
				break;
			}
		}
	}

	/**
	 * Key
	 * @param code
	 * @param press
	 */
	public boolean key(int code, boolean press) {
		_touch.receive(
			new TouchI.Key(
				code, press
			)
		);
		return true;
	}
	/**
	 * AppI
	 */
	public Context getContext() {
		return (Context) _activity;
	}

	public void execute(Runnable exe) {
		_activity.runOnUiThread(exe);
	}

	public void pauseContext() {
		_activity.runOnUiThread(new Runnable() {
			public void run() {
				_activity.moveTaskToBack(true);
			}
		});
	}
	/**
	 * LinkListener
	 */
	public void playLinkListener(Integer port) {
		try {
			__listener.start();
		} catch (NullPointerException ex) {
			__listener = new LinkListener(port);
			__listener.start();
		} catch (IllegalThreadStateException ex) {
			__listener = new LinkListener(port);
			__listener.start();
		} catch (Exception ex) {
			logger(LogType.WARNING, ex.getMessage() + "" + ex.getClass());
		}
	}
	public void pauseLinkListener() {
		/**/
		try {
			__listener.interrupt();
			__listener.join();
		} catch (Exception ex) {
			logger(LogType.WARNING, ex.getMessage() + "" + ex.getClass());
		}
	}
	protected class LinkListener extends Thread {
		/**
		 */
		private final byte[] __msg = new byte[1500];
		DatagramPacket __packet = new DatagramPacket(__msg, __msg.length);
		DatagramSocket __server = null;
		Integer __port = 0;
		/**
		 */
		public LinkListener(Integer port) {
			__port = port;
		}
		/**
		 * interrupt
		 */
		@Override
		public void interrupt() {
			/**/
			super.interrupt();
			/**/
			__close();
		}

		/**
		 * main
		 */
		@Override
		public void run() {
			/**
			 */
			try {
				while (!interrupted()) {
					/**/
					__server = new DatagramSocket(__port);
					/**/
					try {
						while (true) {
							__server.receive(__packet);
							/**/
							onLinkListenerFound(
								__packet.getAddress().getHostAddress(),
								new String(__packet.getData(), 0, __packet.getLength())
							);
						}
					} catch (IOException ex) {
						Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
					}
					if (isInterrupted()) {
						throw new InterruptedException();
					}
					/**/
					__close();
				}
			} catch (InterruptedException ex) {
				logger(LogType.WARNING, "InterruptedException (" + ex.getMessage() + "" + ex.getClass() + ")" + this);
			} catch (SocketException ex) {
				logger(LogType.WARNING, "SocketException (" + ex.getMessage() + "" + ex.getClass() + ")" + this);
			}
			/**/
			__close();
			/**
			 */
			__server = null;
			/**/
			logger(LogType.INFO, "End Link Listener =" + getName());
		}

		private void __close() {
			/**
			 */
			if (__server != null) {
				/**/
				__server.close();
			}
		}

	}
	/**
	 */
	public void onLinkListenerFound(String address, String type) {
	}
}
