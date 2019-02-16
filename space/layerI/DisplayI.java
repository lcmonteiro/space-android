/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
/**
 */
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
/**
 */
import space.base.AppI;
import space.base.Module;
import space.layerI.SerialI.SerialBuf;
import space.under.graphview.GraphView;
import space.under.graphview.series.BarGraphSeries;
import space.under.graphview.series.BaseSeries;
import space.under.graphview.series.DataPoint;
import space.under.graphview.series.LineGraphSeries;

/**
 *
 * @author root
 */
public class DisplayI extends Module {

	/**
	 *
	 */
	final protected FrameLayout _layout;

	/**
	 *
	 * @param context
	 */
	public DisplayI(AppI app) {
		super(app, 1024);
		/**
		 *
		 */
		_layout = new FrameLayout(app.getContext());
	}

	/**
	 *
	 * @return
	 */
	public FrameLayout getLayout() {
		return _layout;
	}

	/**
	 * Objects
	 */
	protected class ImageD extends ImageView {

		public ImageD() {
			super(_appI.getContext());
		}

		public ImageD(int id) {
			super(_appI.getContext());
			setImageResource(id);
		}

		public void SetBink(final int duration, final int offset) {
			_appI.execute(new Runnable() {
				public void run() {
					clearAnimation();
					Animation anim = new AlphaAnimation(0.0f, 1.0f);
					anim.setDuration(duration);
					anim.setStartOffset(offset);
					anim.setRepeatMode(Animation.REVERSE);
					anim.setRepeatCount(Animation.INFINITE);
					startAnimation(anim);
				}
			});
		}

		public void SetTansparency(final float factor) {
			_appI.execute(new Runnable() {
				public void run() {
					setAlpha(factor);
				}
			});
		}

		public void Clear() {
			_appI.execute(new Runnable() {
				public void run() {
					clearAnimation();
				}
			});
		}

		/**
		 *
		 * @param img
		 */
		public void SetData(CameraI.ImageJpeg img) {
			final Bitmap i = BitmapFactory.decodeStream(new ByteArrayInputStream(img.GetData()));

			_appI.execute(new Runnable() {
				public void run() {
					setImageBitmap(i);
				}
			});
		}
	}
	/**
	 *
	 */
	protected class TextD extends TextView {

		public TextD() {
			super(_appI.getContext());
		}
		public TextD(CharSequence txt) {
			super(_appI.getContext());
			setText(txt);
		}
		/**
		 *
		 */
		public void Clear() {
			_appI.execute(new Runnable() {
				public void run() {
					clearAnimation();
				}
			});
		}

		public void SetBink(final int duration, final int offset) {
			_appI.execute(new Runnable() {
				public void run() {
					clearAnimation();
					Animation anim = new AlphaAnimation(0.0f, 1.0f);
					anim.setDuration(duration);
					anim.setStartOffset(offset);
					anim.setRepeatMode(Animation.REVERSE);
					anim.setRepeatCount(Animation.INFINITE);
					startAnimation(anim);

				}
			});
		}

		public void SetData(final LogEntry txt) {
			_appI.execute(new Runnable() {
				public void run() {
					setText(txt.getText() + " ");
				}
			});
		}

		public void SetColor(final int color) {
			_appI.execute(new Runnable() {
				public void run() {
					setTextColor(color);
				}
			});
		}
	}
	/**
	 *
	 */
	protected class EditD extends EditText {

		public EditD() {
			super(_appI.getContext());
		}
		/**
		 *
		 */
		public void Clear() {
			_appI.execute(new Runnable() {
				public void run() {
					clearAnimation();
				}
			});
		}

		public void SetBink(final int duration, final int offset) {
			_appI.execute(new Runnable() {
				public void run() {
					clearAnimation();
					Animation anim = new AlphaAnimation(0.0f, 1.0f);
					anim.setDuration(duration);
					anim.setStartOffset(offset);
					anim.setRepeatMode(Animation.REVERSE);
					anim.setRepeatCount(Animation.INFINITE);
					startAnimation(anim);

				}
			});
		}

		public void SetData(final LogEntry txt) {
			_appI.execute(new Runnable() {
				public void run() {
					setText(txt.getText() + " ");
				}
			});
		}

		public void SetColor(final int color) {
			_appI.execute(new Runnable() {
				public void run() {
					setTextColor(color);
				}
			});
		}
	}
	/**
	 *
	 */
	protected class LoggerD extends ScrollView {

		private final TextView __txt;

		public LoggerD() {
			super(_appI.getContext());
			/**
			 *
			 */
			__txt = new TextView(_appI.getContext());
			/**
			 *
			 */
			addView(__txt);
		}

		/**
		 *
		 * @param img
		 */
		public void SetData(final LogEntry txt) {
			_appI.execute(new Runnable() {
				public void run() {
					__txt.append("\n" + txt.getText());
				}
			});
		}

		public void SetData(final SerialBuf buf) {
			_appI.execute(new Runnable() {
				public void run() {
					__txt.append("\n" + Arrays.toString(buf.getBuf()));
				}
			});
		}

	}
	/**
	 *
	 */
	protected class ChartD extends GraphView {

		/**
		 *
		 */
		private BaseSeries<DataPoint>[] __series;

		private final Integer __size;

		/**
		 *
		 */
		public ChartD(Integer size, Integer[] type) {
			super(_appI.getContext());
			/**
			 *
			 */
			__series = new BaseSeries[type.length];
			/**
			 *
			 */
			for (int i = 0; i < type.length; i++) {

				switch (type[i]) {
					case 0: {
						__series[i] = new LineGraphSeries();
						break;
					}
					case 1: {
						__series[i] = new BarGraphSeries();
						break;
					}
					case 2: {
						__series[i] = new BarGraphSeries();
						break;
					}
				}
				addSeries(__series[i]);
			}
			/**
			 *
			 */
			__size = size;
		}

		/**
		 *
		 */
		public void AddData(final Vector p, final Integer i) {
			_appI.execute(new Runnable() {
				public void run() {
					__series[i].appendData(
						new DataPoint((double) p.getX(), (double) p.getY()), true, __size
					);
				}
			});
		}

		public void SetVector(final Vector3D v, final Integer i) {
			_appI.execute(new Runnable() {
				public void run() {
					__series[i].resetData(new DataPoint[]{
						new DataPoint(0, 0),
						new DataPoint(1, (double) v.getX()),
						new DataPoint(2, (double) v.getY()),
						new DataPoint(3, (double) v.getZ()),
						new DataPoint(4, 0),});
				}
			});
		}
	}
	/**
	 *
	 */
	protected class AdD {

		AdView __view;

		public AdD(String id) {
			this(id, 3, 1.0f);
		}

		public AdD(String id, int type) {
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
		public AdD Rebuild() {
			AdView view = new AdView(_appI.getContext());
			view.setAlpha(__view.getAlpha());
			view.setAdSize(__view.getAdSize());
			view.setAdUnitId(__view.getAdUnitId());
			view.loadAd(new AdRequest.Builder().build());
			__view = view;
			return this;
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
//					__view.loadAd(new AdRequest.Builder().build());
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
	 * clear all layout
	 * @param layout 
	 */
	protected void ClearLayout(ViewGroup view) {
		for (int x = 0; x < view.getChildCount(); x++) {
			if (view.getChildAt(x) instanceof ViewGroup) {
				ClearLayout((ViewGroup) view.getChildAt(x));
			}
		}
		view.removeAllViewsInLayout();
	}
}
