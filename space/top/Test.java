/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.top;

import space.layerII.displayII.DisplayTest;
import space.layerII.testII.TestSerial;
import android.app.Activity;
import space.base.AppFrontend;
import space.layerI.TouchI;
import space.layerII.motorII.MotorArduino;
import space.layerII.testII.TestDisplay;
import space.layerII.touchII.TouchControl;
import space.layerIII.moveIII.FlyControl;

/**
 *
 * @author monteiro
 */
public class Test extends AppFrontend {

	public Test(Activity activity) {
		super(activity);
	}

	/**
	 *
	 */
	private TestSerial __testSerial;
	private TestDisplay __testDisplay;
	private TouchControl __touchControl;
	private FlyControl __flyControl;

	/**
	 *
	 */
	@Override
	public void open() {
		/**
		 * layerI
		 */
//		_camera = new CameraI(0, 5);
//
		_touch = new TouchI(this);

		_serial = new MotorArduino(this);
		
		_display = new DisplayTest(this);
		/**
		 * layerII
		 */
//		__testSerial = new TestSerial();
//		__testDisplay = new TestDisplay();
		__touchControl = new TouchControl(this);
		/**
		 * layer III
		 */
		__flyControl = new FlyControl(this);
		/**
		 * connections
		 */
		_touch.connect(__touchControl);

		__touchControl.connect(_display);
		__touchControl.connect(__flyControl);
		
		__flyControl.connect(_serial);
		
//		_serial.connect(_display);
//		_serial.connect(__testSerial);
//		__testSerial.connect(_serial);
//		_camera.connect(_display);
//		__testDisplay.connect(_display);
		/**
		 * register
		 */
		register(_touch);
		register(_serial);
		register(__touchControl);
		register(__flyControl);
//		register(_camera);
//		register(__testSerial);
//		register(__testDisplay);
		register(_display);
		/**
		 *
		 */
		super.open();
	}

}
