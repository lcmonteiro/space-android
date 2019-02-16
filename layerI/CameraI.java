/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 * imports
 */
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import java.io.ByteArrayOutputStream;
/**
 * 
 */
import space.base.AppI;
import space.base.Module;
import space.base.Data;

/**
 *
 */
public class CameraI extends Module implements Camera.PreviewCallback {

	/**
	 *
	 */
	final protected Integer _fps;
	final protected Integer _id;
	/**
	 *
	 */
	protected Camera _camera;

	/**
	 * data
	 */
	public static class ImageJpeg extends Data {

		final private byte[] _img;
		final private Integer _id;

		public ImageJpeg(byte[] img, Integer id) {
			super();

			_img = img;
			_id = id;
		}

		public byte[] GetData() {
			return _img;
		}
	}

	/**
	 *
	 */
	public enum Type {

		IMAGE
	}

	/**
	 *
	 */
	public CameraI(AppI app, int id, int fps) {
		super(app, 200);
		/**
		 *
		 */
		_fps = fps;
		_id = id;
		/**
		 *
		 */
	}

	/**
	 *
	 */
	@Override
	protected Integer open(Integer retries) {
		/**
		 * open
		 */
		_camera = Camera.open();
		/**
		 * configure
		 */
		Camera.Parameters p = _camera.getParameters();

		int fps = (_fps * 1000);
		int min = 100000;
		int max = 0;
		for (int[] rate : p.getSupportedPreviewFpsRange()) {
			for (int r : rate) {
				if (r > max) {
					max = r;
				}
				if (r < min) {
					min = r;
				}
			}
		}
		if (fps > max) {
			fps = max;
		}
		if (fps < min) {
			fps = min;
		}
		p.setPreviewFpsRange(fps, fps);

		_camera.setParameters(p);
		/**
		 *
		 */
		_camera.setPreviewCallback(this);
		/**
		 *
		 */
		return super.open(retries);
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer play(Integer retries) {
		/**
		 *
		 */
		_camera.startPreview();
		/**
		 *
		 */
		return super.play(retries);
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer pause(Integer retries) {
		/**
		 *
		 */
		_camera.stopPreview();
		/**
		 *
		 */
		return super.pause(retries);
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected Integer close(Integer retries) {
		/**
		 *
		 */
		_camera.stopPreview();

		_camera.setPreviewCallback(null);

		_camera.release();

		_camera = null;
		/**
		 *
		 */
		return super.close(retries);
	}

	/**
	 *
	 */
	public void onPreviewFrame(byte[] bytes, Camera camera) {
		/**
		 *
		 */
		Camera.Parameters parameters = camera.getParameters();
		int width = parameters.getPreviewSize().width;
		int height = parameters.getPreviewSize().height;

		YuvImage yuv = new YuvImage(bytes, parameters.getPreviewFormat(), width, height, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		yuv.compressToJpeg(new Rect(0, 0, width, height), 30, out);
		
		if (bytes.length > 0) {
			send(new ImageJpeg(out.toByteArray(), _id));
		}
	}
}
