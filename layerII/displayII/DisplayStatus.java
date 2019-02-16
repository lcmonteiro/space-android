/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.displayII;

/**
 *
 */
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
/**
 *
 */
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
/**
 *
 */
import space.base.AppI;
import space.base.Data;
import space.layerI.DisplayI;

/**
 *
 * @author root
 */
public class DisplayStatus extends DisplayI {
	
	protected class AdD {
		
		AdView __view;
		
		public AdD(String id) {
			this(id,3,1.0f);
		}
		
		public AdD(String id, int type){
			this(id, type, 1.0f);
		}
		
		public AdD(String id, int type, float alpha) {
			__view = new AdView(_appI.getContext());
			switch (type) {
				case 1:
					__view.setAdSize(AdSize.BANNER);
					break;
				case 2:
					__view.setAdSize(AdSize.FULL_BANNER);
					break;
				default:
					__view.setAdSize(AdSize.SMART_BANNER);
			}
			__view.setAdUnitId(id);
			/**
			 * Create an ad request.
			 */
			AdRequest adRequest = new AdRequest.Builder().build();
			/**
			 *
			 */
			__view.loadAd(adRequest);
			/**
			 * 
			 */
			__view.setAlpha(alpha);
		}

		/**
		 *
		 */
		public View GetView() {
			return __view;
		}

		/**
		 *
		 */
		public void Resume() {
			_appI.execute(new Runnable() {
				public void run() {
					__view.resume();
				}
			});
		}

		/**
		 *
		 */
		public void Pause() {
			_appI.execute(new Runnable() {
				public void run() {
					__view.pause();
				}
			});
		}
	}
	/**
	 *
	 */
	private final ImageD __img;
	private final TextD __txt;
	private final TextD __txt_w;
	private final AdD __ad;

	/**
	 *
	 * @param log
	 */
	public DisplayStatus(AppI app, int icon, String ads, int adtype) {
		super(app);
		/**
		 * create layout
		 */
		__img = new ImageD(icon);
		
		_layout.addView(__img,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
		);
		/**
		 *
		 */
		__txt = new TextD();
		__txt.setAlpha(1.0f);
		
		_layout.addView(__txt,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT)
		);
		/**
		 *
		 */
		__txt_w = new TextD();
		__txt_w.setAlpha(0.6f);
		
		_layout.addView(__txt_w,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT)
		);
		/**
		 *
		 */
		__ad = new AdD(ads, adtype, 0.6f);
		_layout.addView(
			__ad.GetView(),
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER)
		);
		
	}
	
	public DisplayStatus(AppI app, int icon) {
		super(app);
		__ad = null;
		/**
		 * create layout
		 */
		__img = new ImageD(icon);
		
		_layout.addView(__img,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
		);
		/**
		 *
		 */
		__txt = new TextD();
		
		_layout.addView(__txt,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT)
		);
		/**
		 *
		 */
		__txt_w = new TextD();
		
		_layout.addView(__txt_w,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT)
		);
	}
	
	public DisplayStatus(AppI app) {
		super(app);
		__img = null;
		__ad = null;
		/**
		 * create layout
		 */
		__txt = new TextD();
		
		_layout.addView(__txt,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT)
		);
		/**
		 *
		 */
		__txt_w = new TextD();
		
		_layout.addView(__txt_w,
			new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT)
		);
		
	}

	@Override
	protected Integer open(Integer retries) {
		/**
		 * initialization
		 */
		__txt.SetData(new LogEntry("Searching ..."));
		__txt_w.SetData(new LogEntry("OK"));
		__txt_w.setTextColor(Color.GREEN);
		__img.SetBink(1000, 100);
		/**
		 * 
		 */
		return super.open(retries); 
	}
	
	@Override
	protected Integer play(Integer retries) {
		/**
		 *
		 */
		if (__ad != null) {
			__ad.Resume();
		}
		/**
		 *
		 */
		return super.play(retries);
	}
	
	@Override
	protected synchronized Integer pause(Integer retries) {
		if (__ad != null) {
			__ad.Pause();
		}
		/**
		 *
		 */
		return super.pause(retries);
	}
	
	@Override
	public void onInit(Data data) {
		onRun(data);
	}

	/**
	 *
	 */
	@Override
	public void onRun(Data data) {
		if (data instanceof LogEntry) {
			
			LogEntry tmp = (LogEntry) data;
			switch (tmp.getType()) {
				case INFO: {
					switch (tmp.getAttr()) {
						case 0: {
							__txt.SetData(tmp);
							__img.Clear();
							__img.SetTansparency(0.5f);
							break;
						}
						case 1: {
							__txt.SetData(tmp);
							__img.SetBink(1000, 100);
							__img.SetTansparency(1.0f);
							break;
						}
					}
					break;
				}
				case WARNING:
				case ERROR: {
					__txt_w.SetData(new LogEntry("("+tmp.getText()+")"+" KO"));
					__txt_w.setTextColor(Color.RED);
					break;
				}
				
			}
			
		}
	}
	
}
