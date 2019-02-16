/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.views;
/**
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * GameView
 * @author Luis Monteiro
 */
public class GameView extends SurfaceView {

	private GameLoopThread __loop;
	/**
	 */
	protected Rect __region;

	/**
	 * game loop
	 */
	public class GameLoopThread extends Thread {

		final private GameView __view;
		/**/
		private long __fps = 10;

		/**
		 */
		public GameLoopThread(GameView view, long fps) {
			__view = view;
			__fps = fps;
		}

		@Override
		public void run() {
			long period = 1000 / __fps;
			long currTime = System.currentTimeMillis();
			while (!interrupted()) {
				Canvas c = null;
				/**
				 * sleep
				 */
				long nextTime = currTime + period;
				try {
					sleep(nextTime - System.currentTimeMillis());
				} catch (IllegalArgumentException e) {
					nextTime = System.currentTimeMillis();
				} catch (InterruptedException e) {
					break;
				}
				currTime = nextTime;
				/**
				 */
				try {
					c = __view.getHolder().lockCanvas();

					synchronized (__view.getHolder()) {
						if (__view.onUpdate() < 0.5) {
							/**/
							period = (int) (period * 0.9 + 1000 * 0.1);
						} else {
							period = 1000 / __fps;	
						}
						/**/
						__view.onDraw(c);
					}
				} catch (NullPointerException e) {
					break;
				} finally {
					if (c != null) {
						__view.getHolder().unlockCanvasAndPost(c);
					}
				}
			}
		}
	}

	public GameView(Context context, long fps) {
		super(context);
		__loop = new GameLoopThread(this, fps);
		/**
		 *
		 */
		getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				while (true) {
					try {
						__loop.interrupt();
						__loop.join();
						__loop = new GameLoopThread(__loop.__view, __loop.__fps);
					} catch (InterruptedException e) {
						continue;
					}
					break;
				}
				/**
				 */
				onDestroy();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				/**
				 */
				onCreate(holder.getSurfaceFrame());
				/**
				 */
				__loop.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
				int d = Log.d("view", "w" + width + "h" + height);
			}
		});
	}

	/**
	 */
	protected void onCreate(Rect region) {
		__region = region;
	}

	/**
	 */
	protected float onUpdate() {
		return 0;
	}

	/**
	 */
	protected void onDestroy() {
	}

	/**
	 * utils
	 */
	public int GetRealSize(Rect reg, float size) {
		return (int) (Math.min(reg.height(), reg.width()) * size);
	}

	public int GetRealSize(float size) {
		return (int) (Math.min(__region.height(), __region.width()) * size);
	}
}
