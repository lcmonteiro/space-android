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
public class DisplayRemote extends DisplayI {

	/**
	 *
	 */
	final ImageD _img;
	final TextD _txt;

	/**
	 *
	 * @param log
	 */
	public DisplayRemote(AppI app) {
		super(app);
		/**
		 * create layout
		 */
		_img = new ImageD();

		_img.setRotation(90);

		//_img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		//_img.setScaleType(ImageView.ScaleType.FIT_XY);
//		_img.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//                _img.setAdjustViewBounds(true);
//		_img.setScaleType(ImageView.ScaleType.FIT_XY);
//		
		_layout.addView(_img);
		/**
		 *
		 */
		_txt = new TextD();

		_layout.addView(_txt);
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
			return;
		}
		if (data instanceof LogEntry) {
			_txt.SetData((LogEntry) data);
		}
	}

}
