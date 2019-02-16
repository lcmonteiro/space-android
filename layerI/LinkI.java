/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 * imports
 */
import android.content.Context;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.BindException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

/**
 *
 */
import space.base.Module;
import space.base.Data;
import space.base.AppI;

/**
 *
 * @author monteiro
 */
public class LinkI extends Module {

	/**
	 * echo data
	 */
	public static class Echo extends Data {

		public Echo(int power) {
			super();
			__power = power;
		}

		public int getPower() {
			return (__power = __power - 1);
		}
		private int __power;
	}

	public static class Status extends Data {

		public static final int DISCONNECT = 0;
		public static final int CONNECT = 1;

		public Status(String name, int status, String addr) {
			super();
			__status = status;
			__addr = addr;
			__name = name;
		}

		public int getStatus() {
			return __status;
		}

		public String getAddress() {
			return __addr;
		}

		public String getName() {
			return __name;
		}
		private final int __status;
		private final String __name;
		private final String __addr;
	}

	/**
	 * channels
	 */
	public static class Channel {

		public static final int DATA = 0;
		public static final int STATUS = 1;
	}

	/**
	 *
	 */
	public static class Definitions {

		public static final int SYNC = 10;
	}

	/**
	 * configuration
	 */
	final protected Boolean _server;
	final protected Integer _port;
	final protected String _addr;
	final protected int _start;
	final protected int _stop;
	final protected int _timeout;
	/**
	 *
	 */
	protected String __last_conn;
	/**
	 *
	 */
	private Interface __if;

	/**
	 *
	 * @param port
	 * @param ip
	 */
	public LinkI(AppI app, Integer port, Integer timeout) {
		super(app, 1024);
		/**
		 * configuration
		 */
		_server = Boolean.TRUE;
		_port = port;
		_addr = null;
		_start = 0;
		_stop = 0;
		_timeout = timeout;
		/**
		 * variables
		 */
		__if = new Interface();
	}

	public LinkI(AppI app, Integer port, String addr, Integer timeout) {
		super(app, 10240);
		/**
		 * configuration
		 */
		_server = Boolean.FALSE;
		_port = port;
		_addr = addr;
		_start = 1;
		_stop = 254;
		_timeout = timeout;
		/**
		 * variables
		 */
		__if = new Interface();
	}

	public LinkI(AppI app, Integer port, int start, int stop, Integer timeout) {
		super(app, 10240);
		/**
		 * configuration
		 */
		_server = Boolean.FALSE;
		_port = port;
		_addr = null;
		_start = start;
		_stop = stop;
		_timeout = timeout;
		/**
		 * variables
		 */
		__if = new Interface();
	}

	/**
	 *
	 * @return
	 */
	private String getAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp()) {
					continue;
				}

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();

					if (addr instanceof Inet4Address) {
						return addr.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			logger(LogType.WARNING, "IP Address Not Found");
		}
		return null;
	}

	/**
	 * set address
	 */
	public void setAddress(String add) {
		__last_conn = add;
	}

	/**
	 *
	 * @param ip
	 * @param start
	 * @return
	 */
	private String getFirstAddress(String ip, Integer start) {
		String[] nums = ip.split("\\.");
		return String.format("%s.%s.%s.%d", nums[0], nums[1], nums[2], start);
	}

	/**
	 *
	 * @param ip
	 * @param end
	 * @return
	 */
	private String getNextAddress(String ip, Integer end) {
		String[] nums = ip.split("\\.");

		Integer i = Integer.parseInt(nums[3]) + 1;

		if (i > end) {
			return null;
		}
		return String.format("%s.%s.%s.%d", nums[0], nums[1], nums[2], i);
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer open(Integer retries) {
		/**
		 * load last address
		 */
		__last_conn = null;
		try {
			ObjectInputStream in = new ObjectInputStream(_appI.getContext().openFileInput(getName()));
			/**
			 */
			__last_conn = (String) in.readObject();
			/**
			 */
		} catch (IOException ex) {
			logger(LogType.INFO, "Load last address fail");
			__last_conn = null;
		} catch (ClassNotFoundException ex) {
			logger(LogType.INFO, "Load last address fail");
			__last_conn = null;
		}
		/**
		 *
		 */
		return super.open(retries);
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer play(Integer retries) {
		/**
		 */
		try {
			__if.start();
			__if.setName(getName());
		} catch (IllegalThreadStateException e) {
			__if = new Interface();
			/**/
			return (100);
		} catch (Exception e) {
			return ((retries + 1) * 1000);
		}
		/**
		 */
		return open(retries);
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onRun(Data data) {
		try {
			__if.write(data);
		} catch (Exception e) {
			logger(LogType.WARNING, "Write Error");
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer pause(Integer retries) {
		/**
		 * close interface
		 */
		try {
			__if.interrupt();
			__if.join();
		} catch (Exception ex) {
			logger(LogType.WARNING, ex.getMessage() + "" + ex.getClass());
		}
		/**
		 * save last address
		 */
		try {
			ObjectOutputStream out = new ObjectOutputStream(
				_appI.getContext().openFileOutput(getName(), Context.MODE_PRIVATE)
			);
			/**
			 */
			out.writeObject(__last_conn);
		} catch (IOException ex) {
			logger(LogType.WARNING, "save last address");
		}

		logger(LogType.INFO, "pause");
		/**
		 */
		return super.pause(retries);
	}

	/**
	 * close
	 *
	 * @param retries
	 * @return
	 */
	@Override
	protected Integer close(Integer retries) {
		pause(retries);
		return super.close(retries);
	}

	/**
	 * Interface context
	 */
	protected void configure(Socket channel) throws SocketException {
		/**
		 * channel settings
		 */
		channel.setTcpNoDelay(true);
		/**
		 *
		 */
		if (_timeout == 0) {
			channel.setKeepAlive(true);
		} else {
			channel.setSoTimeout(_timeout);
		}
	}

	protected ObjectOutputStream CreateOutputStream(Socket channel) throws IOException {
		return new ObjectOutputStream(new BufferedOutputStream(channel.getOutputStream()));
	}

	protected ObjectInputStream CreateInputStream(Socket channel) throws IOException {
		receive(new Echo(2));
		return new ObjectInputStream(channel.getInputStream());
	}

	protected void writeData(ObjectOutputStream out, Data data) throws IOException {
		out.writeUnshared(data);
		out.flush();
	}

	protected Data readData(ObjectInputStream in) throws IOException, OptionalDataException, ClassNotFoundException, ClassCastException {
		return (Data) in.readUnshared();
	}

	protected class Interface extends Thread {

		/**
		 */
		private Socket __channel = null;
		private ObjectInputStream __in = null;
		private ObjectOutputStream __out = null;

		/**
		 * interrupt
		 */
		@Override
		public void interrupt() {
			/**
			 */
			super.interrupt();
			/**
			 */
			__close();
		}

		/**
		 * Connect
		 *
		 * @return @throws java.lang.InterruptedException
		 */
		protected Socket connect() throws InterruptedException {
			Socket s = null;
			String a = null;
			String n = null;

			while (a == null) {
				if (_addr == null) {
					while ((a = getAddress()) == null) {
						/**
						 * check thread
						 */
						if (isInterrupted()) {
							throw new InterruptedException();
						}
						send(
							new LogEntry(LogEntry.Type.WARNING,
								"Check Network (offline) "
							),
							Channel.STATUS
						);
						/**
						 *
						 */
						sleep(6000);
					}
					a = getFirstAddress(a, _start);
					n = a;
				} else {
					a = _addr;
				}
				for (int i = 0; s == null; ++i) {
					/**
					 *
					 */
					s = new Socket();
					try {
						s.setReuseAddress(true);
						s.connect(new InetSocketAddress(a, _port), 100);
						/**/
						__last_conn = a;
						/**/
						return s;
						/**/
					} catch (IOException ex) {
						logger(LogType.WARNING, "Connect Fail( " + a + ":" + _port + " )");
					} finally {
						s = null;
					}
					if (_addr == null) {
						if (__last_conn != null && (i % Definitions.SYNC) == 0) {
							/**
							 * try with last connection
							 */
							a = __last_conn;
						} else {
							/**
							 * get next
							 */
							if ((a = n = getNextAddress(n, _stop)) == null) {
								send(new LogEntry(LogEntry.Type.WARNING,
									"Check Network( " + getAddress() + " ) "
								),
									Channel.STATUS
								);
								break;
							}
						}
					}
					/**
					 * check thread
					 */
					if (isInterrupted()) {
						throw new InterruptedException();
					}
				}
			}
			return s;
		}

		/**
		 * accept
		 *
		 * @return @throws java.io.IOException
		 */
		protected Socket accept() throws IOException, InterruptedException {
			/**
			 */
			Socket s = null;
			/**
			 */
			ServerSocket ss = new ServerSocket();
			ss.setReuseAddress(true);
			ss.setSoTimeout(500);
			try {
				ss.bind(new InetSocketAddress(_port), 1);
			} catch (BindException e) {
				ss.close();
				sleep(500);
				throw e;
			}
			/**
			 */
			while (s == null) {
				try {
					/**
					 */
					s = ss.accept();
				} catch (SocketTimeoutException ex) {
					logger(LogType.WARNING, "Accept Fail (" + ss + ":" + _port + ")");
				}
				if (isInterrupted()) {
					ss.close();
					if (s != null) {
						s.close();
					}
					throw new InterruptedException();
				}
			}
			/**
			 */
			ss.close();
			/**
			 */
			return s;
		}

		/**
		 * write
		 */
		public void write(Data data) {

			if (__out != null) {
				try {
					/**
					 */
					writeData(__out, data);
					/**
					 */
				} catch (IOException ex) {
					logger(LogType.WARNING, "Write Object Fail");
				}
			}
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
					/**
					 */
					try {
						if (_server) {
							__channel = accept();
						} else {
							__channel = connect();
						}
						/**
						 */
						configure(__channel);
						/**
						 */
					} catch (BindException ex) {
						logger(LogType.WARNING, "Connect/Accept Fail (" + ex.getMessage() + "" + ex.getClass() + ")" + this);
						continue;
					} catch (IOException ex) {
						logger(LogType.WARNING, "Connect/Accept Fail (" + ex.getMessage() + "" + ex.getClass() + ")" + this);
						continue;
					}
					logger(LogType.INFO, "Connect");
					/**
					 */
					try {
						/**
						 * out
						 */
						__out = CreateOutputStream(__channel);
						__out.flush();
						/**
						 *
						 */
						send(new Status(getName(), Status.CONNECT, __channel.getInetAddress().getHostName()), Channel.STATUS);
						/**
						 * in
						 */
						__in = CreateInputStream(__channel);
						/**
						 */
					} catch (SocketTimeoutException ex) {
						__close();
						continue;
					} catch (StreamCorruptedException ex) {
						__close();
						continue;
					} catch (IOException ex) {
						logger(LogType.WARNING, "" + ex.getClass());
						continue;
					}
					/**
					 */
					try {
						boolean timeout = false;
						while (true) {
							try {
								Data data = readData(__in);
								if (data instanceof Echo) {
									if (((Echo) data).getPower() > 0) {
										receive(data);
									}
								} else {
									send(data);
								}
								timeout = false;
							} catch (SocketTimeoutException ex) {
								logger(LogType.INFO, "SocketTimeoutException");
								if (timeout) {
									logger(LogType.WARNING, "Timeout (time=" + System.currentTimeMillis() + ")");
									break;
								}
								receive(new Echo(2));
								timeout = true;
							} catch (SocketException ex) {
								if (isInterrupted()) {
									throw new InterruptedException();
								}
								logger(LogType.WARNING, "SocketException" + ex);
								break;
							} catch (IOException ex) {
								logger(LogType.WARNING, "IOException");
								break;
							}
						}
					} catch (ClassNotFoundException ex) {
						logger(LogType.WARNING, "Class Not Found");
					} catch (ClassCastException ex) {
						logger(LogType.WARNING, "ClassCastException");
					}
					/**/
					__close();
				}
			} catch (InterruptedException ex) {
			}
			/**/
			__close();
			/**
			 */
			__channel = null;
			__out = null;
			__in = null;
			/**/
			logger(LogType.INFO, "End Link Interface =" + getName());
		}

		private void __close() {
			/**
			 */
			try {
				if (__in != null) {
					__in.close();
				}
				if (__out != null) {
					__out.close();
				}
				if (__channel != null) {
					/**
					 */
					send(new Status(getName(), Status.DISCONNECT, __channel.getInetAddress().getHostName()), Channel.STATUS);
					/**/
					__channel.close();
				}
				/**
				 */
			} catch (IOException ex) {
				logger(LogType.INFO, "Socket is closed");
			}
		}

	}
}
