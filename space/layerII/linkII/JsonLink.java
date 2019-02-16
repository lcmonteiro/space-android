/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.linkII;

/**
 * imports
 */
import space.base.AppI;
import space.base.Data;
import space.layerI.LinkI;
import space.layerII.fusionII.Fusion.DirectionX;
import space.layerII.fusionII.Fusion.DirectionY;
import space.layerII.fusionII.Fusion.DirectionZ;
import space.layerII.fusionII.Fusion.RotationPower;
/**
 *
 */
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import space.layerI.InputI.Command;
import space.layerI.InputI.Position;
import space.layerI.TouchI.Key;

/**
 *
 * @author monteiro
 */
public class JsonLink extends LinkI {

	/**
	 * @param port
	 */
	public JsonLink(AppI app, Integer port) {
		super(app, port, 1, 254, 0);
		/**
		 *
		 */
		setName("jlink");
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
	}

	/**
	 * json serialization
	 *
	 * @param out
	 * @param data
	 * @throws IOException
	 */
	@Override
	protected void writeData(ObjectOutputStream out, Data data) throws IOException {
		if (data instanceof Position) {
			Position tmp = (Position) data;
			try {
				JSONObject d = new JSONObject();
				d.put("X", tmp.getX());
				d.put("Y", tmp.getY());
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("PTR", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}
		} else if (data instanceof Command) {
			Command tmp = (Command) data;
			try {
				JSONObject d = new JSONObject();
				d.put("S", tmp.getBTN());
				d.put("A", tmp.getACT());
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("CMD", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}

		} else if (data instanceof DirectionX) {
			DirectionX tmp = (DirectionX) data;
			try {
				JSONObject d = new JSONObject();
				d.put("N", tmp.getX());
				d.put("W", tmp.getY());
				d.put("A", tmp.getZ());
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("U_X", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}
		} else if (data instanceof DirectionY) {
			DirectionY tmp = (DirectionY) data;
			try {
				JSONObject d = new JSONObject();
				d.put("N", tmp.getX());
				d.put("W", tmp.getY());
				d.put("A", tmp.getZ());
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("U_Y", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}
		} else if (data instanceof DirectionZ) {
			DirectionZ tmp = (DirectionZ) data;
			try {
				JSONObject d = new JSONObject();
				d.put("N", tmp.getX());
				d.put("W", tmp.getY());
				d.put("A", tmp.getZ());
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("U_Z", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}
		} else if (data instanceof RotationPower) {
			RotationPower tmp = (RotationPower) data;
			try {
				JSONObject d = new JSONObject();
				d.put("X", tmp.getPower());
				d.put("Y", 0);
				d.put("Z", 0);
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("POW", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}
		} else if (data instanceof Key) {
			Key tmp = (Key) data;
			try {
				JSONObject d = new JSONObject();
				d.put("C", tmp.getCode());
				d.put("S", tmp.getPress());
				d.put("T", tmp.getTime());
				JSONObject cmd = new JSONObject();
				cmd.put("KEY", d);
				JSONArray arr = new JSONArray();
				arr.put(cmd);
				/**
				 * send
				 */
				write(out, arr.toString());
				/**
				 *
				 */
			} catch (JSONException ex) {
				logger(LogType.WARNING, "JSONException");
			}
		}
	}

	/**
	 *
	 * @param out
	 * @param data
	 * @throws IOException
	 */
	protected void write(ObjectOutputStream out, String data) throws IOException {
		out.write(data.getBytes());
		out.write("\n".getBytes());
		out.flush();
	}
}
