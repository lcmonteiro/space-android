/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.testII;

/**
 * imports
 */

import space.layerI.SerialI.SerialBuf;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 */
import space.base.AppI;
import space.base.Data;
import space.base.Module;
/**
 *
 * @author monteiro
 */
public class TestSerial extends Module {

	/**
	 */
	public TestSerial(AppI app) {
		super(app, 200);
	}

	/**
	 */
	@Override
	protected Integer play(Integer retries) {
		/**
		 */
		send(new Vector(1,1));
		send(new Vector(2,2));
		send(new Vector(3,3));
		send(new Vector(4,4));
		/**
		 */
		return super.play(retries);
	}

	/**
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof SerialBuf) {

			byte[] buf = ((SerialBuf) data).getBuf().clone();

			for (int i = 0; i < buf.length; i++) {
				buf[i]++;
				if (buf[i] != 0) {
					break;
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(TestSerial.class.getName()).log(Level.SEVERE, null, ex);
			}
			send(new SerialBuf(buf));
		}
	}
}
