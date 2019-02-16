/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 * imports
 */
import space.base.AppI;
import space.base.Module;
import space.base.Data;
import space.under.usbserial.driver.UsbSerialDriver;
import space.under.usbserial.driver.UsbSerialPort;
import space.under.usbserial.driver.UsbSerialProber;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.ByteBuffer;
import java.io.IOException;

/**
 *
 * @author monteiro
 */
public class SerialI extends Module {

	/**
	 * Data Types
	 */
	public static class SerialBuf extends Data {

		protected byte[] _b;

		public SerialBuf(byte[] b) {
			super();
			/**
			 *
			 */
			_b = b;
		}

		/**
		 *
		 * @return
		 */
		public byte[] getBuf() {
			return _b;
		}
	}
	/**
	 * configuration
	 */
	final protected Integer _speed;
	/**
	 *
	 */
	private final ExecutorService _executor;
	/**
	 *
	 */
	private InputOutputManager _serial;

	/**
	 *
	 * @param port
	 * @param ip
	 */
	public SerialI(AppI app, Integer speed) {
		super(app, 10);
		/**
		 * configuration
		 */
		_speed = speed;
		/**
		 * variables
		 */
		_executor = Executors.newSingleThreadExecutor();
		/**
		 *
		 */
		_serial = null;
	}

	@Override
	protected Integer open(Integer retries) {
		/**
		 * get USB service
		 */
		UsbManager manager = (UsbManager) _appI.getContext().getSystemService(Context.USB_SERVICE);
		/**
		 * find serial
		 */
		for (UsbSerialDriver d : UsbSerialProber.getDefaultProber().findAllDrivers(manager)) {

			UsbDeviceConnection conn = manager.openDevice(d.getDevice());

			if (conn != null) {
				for (UsbSerialPort p : d.getPorts()) {

					_appI.execute(new Runnable() {

						public void run() {
							Toast.makeText(_appI.getContext(), "open", Toast.LENGTH_LONG).show();
						}
					});

					try {
						p.open(conn);
						p.setParameters(_speed, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

					} catch (IOException ex) {
						logger(LogType.WARNING, "Open Fail");
						continue;
					}

					_serial = new InputOutputManager(p);
					/**
					 *
					 */
					_executor.submit(_serial);
					/**
					 *
					 */
					return super.open(retries);
				}
			}
		}
		return 1000;
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onRun(Data data) {
		/**
		 *
		 */
		if (data instanceof SerialBuf) {
			sendData(((SerialBuf) data).getBuf());
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer close(Integer retries) {
		/**
		 *
		 */
		_serial.stop();
		/**
		 *
		 */
		return super.close(retries);
	}

	/**
	 *
	 */
	private enum State {

		STOPPED,
		RUNNING,
		STOPPING
	}

	/**
	 * interface
	 */
	protected void sendData(byte[] data) {
		_serial.write(data);
	}
	/**
	 * 
	 * callback 
	 */
	public void onNewData(byte[] data) {
		send(new SerialBuf(data));
	}

	public void onRunError(Exception e) {
	}

	public void onDebug(String data) {
	}

	/**
	 *
	 */
	public class InputOutputManager implements Runnable {

		private static final int WAIT_MILLIS = 200;

		private static final int BUFSIZ = 4096;
		/**
		 *
		 */
		private final UsbSerialPort __driver;

		private State __state = State.STOPPED;
		/**
		 *
		 */
		private final ByteBuffer __buffer = ByteBuffer.allocate(BUFSIZ);

		/**
		 * Creates a new instance with the provided listener.
		 */
		public InputOutputManager(UsbSerialPort driver) {
			__driver = driver;
		}

		public synchronized void write(byte[] data) {
			try {
				__driver.write(data, WAIT_MILLIS);
			} catch (IOException ex) {
				onRunError(ex);
			}
		}

		public synchronized void stop() {
			if (__state == State.RUNNING) {
				onDebug("Stop requested");
				__state = State.STOPPING;
			}
		}

		@Override
		public void run() {
			synchronized (this) {
				__state = State.RUNNING;
			}
			while (true) {
				synchronized (this) {
					if (__state != State.RUNNING) {
						break;
					}
				}
				/**
				 *
				 */
				int len = 0;
				try {
					len = __driver.read(__buffer.array(), WAIT_MILLIS);
				} catch (IOException ex) {
					onRunError(ex);
					break;
				}
				/**
				 *
				 */
				if (len > 0) {
					final byte[] data = new byte[len];
					/**
					 *
					 */
					__buffer.get(data, 0, len);
					/**
					 *
					 */
					onNewData(data);
				}
				__buffer.clear();
			}
			synchronized (this) {
				__state = State.STOPPED;
				onDebug("Stopped");
			}
		}
	}

}
