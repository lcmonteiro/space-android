/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.displayII;

import space.base.AppI;
import space.base.Data;
import space.layerI.CameraI.ImageJpeg;
import space.layerI.DisplayI;

/**
 *
 * @author root
 */
public class DisplayLocal extends DisplayI {

	/**
	 *
	 */
	final ImageD _img;

	/**
	 *
	 * @param log
	 */
	public DisplayLocal(AppI app) {
		super(app);
		/**
		 * create layout
		 */
		_img = new ImageD();

		_layout.addView(_img);
		/**
		 *
		 */
	}

	/**
	 *
	 * @param data
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof ImageJpeg) {
			_img.SetData((ImageJpeg) data);
		}
	}

}
