/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.base;

/**
 *
 */
import android.content.Context;
import android.os.SystemClock;
import android.view.WindowManager;
import java.io.IOException;
import static java.lang.Thread.interrupted;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author monteiro
 */
public class AppBackend extends App implements AppI {

	/**
	 */
	final protected Context _context;
	/**
	 */
	private LinkBroadcast __broadcast;
	/**
	 */
	public AppBackend(Context context) {
		super();
		/**
		 */
		_context = context;
	}

	/**
	 * registration
	 */
	protected void register(Module module) {
		_services.add(module);
	}

	/**
	 *
	 */
	public void create() {
		/**
		 * get metrics
		 */
		((WindowManager) _context.getSystemService(
			Context.WINDOW_SERVICE
		)).getDefaultDisplay().getMetrics(_metrics);
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
		 * initialization input module
		 */
		if (_input != null) {
			_input.receive(screen_info);
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
		/**
		 */
	}

	/**
	 *
	 */
	public void destroy() {
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
		 */
		_services.clear();
	}

	/**
	 * AppI
	 */
	public Context getContext() {
		return (Context) _context;
	}

	public void execute(Runnable exe) {
	}

	public void pauseContext() {
	}

	/**
	 * LinkBroadcast
	 */
	public void playLinkBroadcast(Integer port, String type) {
		try {
			__broadcast.start();
		} catch (NullPointerException ex) {
			__broadcast = new LinkBroadcast(port, type);
			__broadcast.start();
		} catch (IllegalThreadStateException ex) {
			__broadcast = new LinkBroadcast(port, type);
			__broadcast.start();
		} catch (Exception ex) {
			logger(LogType.WARNING, ex.getMessage() + "" + ex.getClass());
		}
	}
	public void pauseLinkBroadcast() {
		/**/
		try {
			__broadcast.interrupt();
			__broadcast.join();
		} catch (Exception ex) {
			logger(LogType.WARNING, ex.getMessage() + "" + ex.getClass());
		}
	}
	protected class LinkBroadcast extends Thread {
		/**
		 */
		DatagramPacket __packet = null;
		DatagramSocket __client = null;
		Integer __port = 0;
		String __type = null;
		/**
		 */
		public LinkBroadcast(Integer port, String type) {
			__port = port;
			__type = type;
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
					__client = new DatagramSocket();
					__client.setBroadcast(true);
					/**/
					__packet = new DatagramPacket(
						__type.getBytes(), 
						__type.length(), 
						InetAddress.getByName("255.255.255.255"), 
						__port
					);
					/**/
					try {
						while (true) {		
							__client.send(__packet);
							/**/
							sleep(4000);
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
			} catch (UnknownHostException ex) {
				Logger.getLogger(AppBackend.class.getName()).log(Level.SEVERE, null, ex);
			}
			/**/
			__close();
			/**/
			__client = null;
			/**/
			logger(LogType.INFO, "End Link Broadcast =" + getName());
		}

		private void __close() {
			/**
			 */
			if (__client != null) {
				/**/
				__client.close();
			}
		}

	}
}
