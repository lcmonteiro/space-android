/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.linkII;

/**
 * imports
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 */
import space.base.AppI;
import space.base.Data;
import space.layerI.LinkI;

/**
 *
 * @author monteiro
 */
public class FastLink extends LinkI {

	/**
	 *
	 */
	final private ByteArrayOutputStream __out;
	final private PipedInputStream __in;
	/**
	 *
	 */
	private DatagramSocket __socket;
	/**
	 *
	 */
	final private byte[] buffer_w;
	final private byte[] buffer_r;

	/**
	 * @param port
	 */
	public FastLink(AppI app, Integer port, int start, int stop) {
		super(app, port, start, stop, 0);
		/**
		 *
		 */
		buffer_w = new byte[1500];
		buffer_r = new byte[1500];
		/**
		 *
		 */
		__out = new ByteArrayOutputStream();
		__in = new PipedInputStream();
	}

	public FastLink(AppI app, Integer port) {
		super(app, port, 0);
		/**
		 *
		 */
		buffer_w = new byte[1500];
		buffer_r = new byte[1500];
		/**
		 *
		 */
		__out = new ByteArrayOutputStream();
		__in = new PipedInputStream();
	}

	/**
	 *
	 * @param channel
	 * @throws SocketException
	 */
	@Override
	protected void configure(Socket channel) throws SocketException {
		/**
		 * channel settings
		 */
		channel.setTcpNoDelay(true);
		channel.setKeepAlive(true);
		/**
		 *
		 */
		__socket = new DatagramSocket(null);
		/**
		 *
		 */
		receive(new Echo(10));
	}

	/**
	 *
	 */
	@Override
	protected ObjectOutputStream CreateOutputStream(Socket channel) throws IOException {
		/**
		 *
		 */
		__socket.bind(
			new InetSocketAddress(
				channel.getLocalAddress().getHostAddress(), channel.getLocalPort() + 1
			)
		);
		/**
		 *
		 */
		return new ObjectOutputStream(new PipedOutputStream(__in));
	}

	@Override
	protected ObjectInputStream CreateInputStream(Socket channel) throws IOException {
		/**
		 *
		 */
		__socket.connect(
			new InetSocketAddress(
				channel.getInetAddress().getHostAddress(), channel.getPort() + 1
			)
		);

		receive(new Echo(10));
		/**
		 *
		 */
		return null;
	}

	@Override
	protected void writeData(ObjectOutputStream out, Data data) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(data);
		oos.close();

//		/**
//		 * serialize object
//		 */
//		out.writeObject(data);
//		out.flush();
//		/**
//		 * create packet
//		 */
//		int len = __in.read(buffer_w);
		DatagramPacket packet = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length);
		/**
		 * send
		 */
		__socket.send(packet);
	}

	@Override
	protected Data readData(ObjectInputStream in) throws OptionalDataException, ClassNotFoundException, ClassCastException {
		/**
		 * create packet
		 */
		DatagramPacket packet = new DatagramPacket(buffer_r, buffer_r.length);
		try {
			/**
			 * receive
			 */
			__socket.receive(packet);

			/**
			 *
			 */
			ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));

//			__out.write(packet.getData());
//			__out.flush();
//			
//			ByteArrayInputStream buf_in = new ByteArrayInputStream(__out.toByteArray());
//
//			ObjectInputStream input = new ObjectInputStream(buf_in);
			/**
			 * unserialize object
			 */
			return (Data) input.readObject();
		} catch (IOException ex) {
			Logger.getLogger(FastLink.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
