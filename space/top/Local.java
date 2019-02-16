/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.top;

import android.app.Activity;

import space.base.AppFrontend;
import space.layerI.LinkI;
import space.layerI.TouchI;
import space.layerII.touchII.TouchControl;
import space.layerII.displayII.DisplayLocal;

/**
 *
 * @author monteiro
 */
public class Local extends AppFrontend {

	public Local(Activity activity) {
		super(activity);
	}
	/**
	 *
	 */
	private TouchControl __touchControl;

	/**
	 *
	 */
	@Override
	public void open() {
		/**
		 * layerI
		 */
		_link = new LinkI(this, 6969, null);

		_touch = new TouchI(this);

		_display = new DisplayLocal(this);
		/**
		 * layer II
		 */
		__touchControl = new TouchControl(this);
		/**
		 * connections
		 */
		_link.connect(_display);

		_touch.connect(__touchControl);

		__touchControl.connect(_link);
		/**
		 * register
		 */
		register(_link);

		register(_touch);

		register(_display);

		register(__touchControl);
		/**
		 *
		 */
		super.open();
	}

}
