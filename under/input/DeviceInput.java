/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.input;

/**
 *
 */
import java.io.IOException;
import space.under.libc.UInput;

/**
 *
 * @author root
 */
public class DeviceInput {

	/**
	 * definitions
	 */
	static final String SYS_INTERFACE = "/dev/uinput";
	/**
	 *
	 */
	static protected UInput.CLibrary __clib;

	/**
	 *
	 */
	/**
	 *
	 */
	public DeviceInput() {
		/**
		 * load posix lib
		 */
		__clib = UInput.LoadLibrary();
	}

	/**
	 * root permissions
	 */
	public static void permissionRequest() throws IOException {
		/**
		 *
		 */
		try {
			Runtime.getRuntime().exec("su -c chmod 666 " + SYS_INTERFACE).waitFor();
		} catch (InterruptedException ex) {
			throw new IOException();
		}
	}

	public static void permissionRemove() throws IOException {
		/**
		 *
		 */
		try {
			Runtime.getRuntime().exec("su -c chmod 660 " + SYS_INTERFACE).waitFor();
		} catch (InterruptedException ex) {
			throw new IOException();
		}
	}

}
