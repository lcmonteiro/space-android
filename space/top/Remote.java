/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.top;

import android.app.Activity;
import space.base.AppFrontend;
import space.layerI.CameraI;
import space.layerI.LinkI;
import space.layerII.displayII.DisplayRemote;
import space.layerII.motorII.MotorArduino;
import space.layerIII.moveIII.FlyControl;

/**
 *
 * @author monteiro
 */
public class Remote extends AppFrontend {

	public Remote(Activity activity) {
		super(activity);
	}
	/**
	 *
	 */
	private FlyControl __flyControl;

	/**
	 *
	 */
	@Override
	public void open() {
		/**
		 * layerI
		 */
		_link = new LinkI(this, 6969, 1000);

		_camera = new CameraI(this, 0, 5);

		_display = new DisplayRemote(this);

		_serial = new MotorArduino(this);
		/**
		 * layer III
		 */
		__flyControl = new FlyControl(this);
		/**
		 * connections
		 */
		_camera.connect(_link);

		_link.connect(__flyControl);
		/**
		 * register
		 */
		register(_camera);

		register(_link);

		register(_display);
		
		register(__flyControl);
		/**
		 *
		 */
		super.open();
	}

}
