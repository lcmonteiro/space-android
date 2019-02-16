/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.displayII;

import android.view.Gravity;
import android.widget.FrameLayout;

import space.base.AppI;
import space.base.Data;
import space.layerI.DisplayI;
import space.layerI.CameraI.ImageJpeg;
import space.layerI.SerialI.SerialBuf;
import space.layerII.touchII.TouchControl.PowerDir;

/**
 *
 * @author root
 */
public class DisplayTest extends DisplayI {

	/**
	 *
	 */
	final ImageD _img;

	final LoggerD _log;

	final ChartD _chart;

	/**
	 *
	 * @param log
	 */
	public DisplayTest(AppI app) {
		super(app);
		/**
		 * create layout
		 */
		_img = new ImageD();

		_layout.addView(_img);
		/**
		 *
		 */
		_log = new LoggerD();
//
//		_layout.addView(_log);
//		/**
//		 *
//		 */
		_chart = new ChartD(20, new Integer[]{1});
		_chart.setClickable(false);
		_chart.measure(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		_chart.setLayoutParams(
			new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 200, Gravity.CENTER
			));
		_chart.getViewport().setMaxY(100);
		_chart.getViewport().setMinY(-100);
		_chart.getViewport().setYAxisBoundsManual(true);
		
		
		_layout.addView(_chart);
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
			_log.SetData((LogEntry) data);
			return;
		}
		if (data instanceof SerialBuf) {
			_log.SetData((SerialBuf) data);
		}
		if (data instanceof Vector3D) {
			_chart.SetVector((Vector3D) data, 0);
		}
		if (data instanceof PowerDir) {
			PowerDir tmp = (PowerDir) data;
			_chart.SetVector(new Vector3D(tmp.getX(), tmp.getY(), tmp.getZ()), 0);
		}
	}

}
