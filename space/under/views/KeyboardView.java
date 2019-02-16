/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import java.util.HashMap;
import java.util.Map;
import space.layerI.TouchI.Key;
import space.under.input.MapInput;

/**
 * @author Luis Monteiro
 */
public class KeyboardView extends EditText implements TextWatcher, OnKeyListener {

	/**
	 */
	final protected Drawable __icon;
	/**/
	final protected GradientDrawable __back;
	/**
	 */
	boolean _enable = false;
	/**
	 */
	int _cursor = 0;

	/**
	 */
	public KeyboardView(Context context, int icon) {
		super(context);
		/**/
		__icon = context.getResources().getDrawable(icon);
		/**
		 */
		__back = new GradientDrawable();
		__back.setColors(new int[]{
			Color.CYAN,
			Color.CYAN,
			Color.TRANSPARENT
		});
		__back.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		__back.setShape(GradientDrawable.OVAL);
		/**/
		setBackgroundColor(Color.BLACK);
		setTextColor(Color.WHITE);
		setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		setGravity(Gravity.CENTER);
		setPadding(0, 0, 0, 0);
		/**/
		setFocusableInTouchMode(false);
		setFocusable(false);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!_enable) {
			return;
		}
		/*------------------------------------------------------------------*
		 * move cursor
		 *------------------------------------------------------------------*/
		int move = start + before - _cursor;
		for (; move < 0; ++move) {
			sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		}
		for (; move > 0; --move) {
			sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		}
		/*------------------------------------------------------------------*
		 * delete
		 *------------------------------------------------------------------*/
		for (int n = before; n > 0; --n) {
			sendKey(KeyEvent.KEYCODE_DEL);
		}
		/*------------------------------------------------------------------*
		 * Insert
		 *------------------------------------------------------------------*/
		for (int i = start, n = count; n > 0; --n, ++i) {
			char ch = s.charAt(i);
			try {
				if (Character.isUpperCase(ch)) {
					sendKey(KeyEvent.KEYCODE_SHIFT_LEFT, true);
					try {
						sendKey(MapInput.CHARACTER.get(Character.toLowerCase(ch)));
					} catch (NullPointerException e) {
						sendKeyExtension(ch);
					}
					sendKey(KeyEvent.KEYCODE_SHIFT_LEFT, false);
				} else {
					try {
						sendKey(MapInput.CHARACTER.get(Character.toLowerCase(ch)));
					} catch (NullPointerException e) {
						sendKeyExtension(ch);
					}
				}
			} catch (NullPointerException e) {
				Log.w("wip", "##ch=" + s.charAt(i) + "not found");
			}
		}
		/*------------------------------------------------------------------*
		 * update cursor
		 *------------------------------------------------------------------*/
		_cursor = start + count;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			/* do something */
			sendKey(keyCode);
		}
		return false;
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		Log.d("test", "onKeyPreIme: " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			setFocusableInTouchMode(false);
//			setFocusable(false);
			_enable = false;
			/**/
			setText("");
		}
		return super.onKeyPreIme(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		sendKey(keyCode);
		return super.onKeyDown(keyCode, event);
	}

	MotionEvent __down;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_DOWN: {
				setFocusableInTouchMode(true);
				setFocusable(true);
				__down = event;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				if (Math.abs(event.getY() - __down.getY()) < Math.min(getHeight(), getWidth())
					&& Math.abs(event.getX() - __down.getX()) < Math.min(getHeight(), getWidth())) {
					_enable = true;
				} else {
					setFocusableInTouchMode(false);
					setFocusable(false);
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
			View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec)
		);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		/**/
		int s = Math.min(w, h);
		int cw = w >> 1;
		int ch = h >> 1;
		/**
		 */
		__back.setBounds(
			(int) (cw - s * 0.5), (int) (ch - s * 0.5), (int) (cw + s * 0.5), (int) (ch + s * 0.5)
		);
		__back.setGradientRadius((int) (__back.getBounds().height() * 0.5));
		/**
		 */
		__icon.setBounds(
			(int) (cw - s * 0.2), (int) (ch - s * 0.17), (int) (cw + s * 0.2), (int) (ch + s * 0.17)
		);
	}

	/**
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		/**
		 * clear
		 */
//		canvas.drawColor(0, Mode.CLEAR);
		canvas.drawColor(Color.BLACK);
		if (_enable) {
			super.onDraw(canvas);
		} else {
			__back.draw(canvas);
			__icon.draw(canvas);
		}
	}

	/**
	 * interface
	 */
	protected void sendKey(Key key) {
	}

	protected void sendKey(int code) {
	}

	protected void sendKey(int code, boolean press) {
	}
	/**
	 * US extensions
	 */
	public static final Map<Character, Key[]> EXTENDED_CHARACTER;

	static {
		EXTENDED_CHARACTER = new HashMap<Character, Key[]>();
		EXTENDED_CHARACTER.put('í', new Key[]{
			new Key(KeyEvent.KEYCODE_I, true),
			new Key(KeyEvent.KEYCODE_I, false),});
		EXTENDED_CHARACTER.put('á', new Key[]{
			new Key(KeyEvent.KEYCODE_A, true),
			new Key(KeyEvent.KEYCODE_A, false),});
		EXTENDED_CHARACTER.put('à', new Key[]{
			new Key(KeyEvent.KEYCODE_A, true),
			new Key(KeyEvent.KEYCODE_A, false),});
		EXTENDED_CHARACTER.put('é', new Key[]{
			new Key(KeyEvent.KEYCODE_E, true),
			new Key(KeyEvent.KEYCODE_E, false),});
		EXTENDED_CHARACTER.put('ã', new Key[]{
			new Key(KeyEvent.KEYCODE_A, true),
			new Key(KeyEvent.KEYCODE_A, false),});
		EXTENDED_CHARACTER.put('ç', new Key[]{
			new Key(KeyEvent.KEYCODE_C, true),
			new Key(KeyEvent.KEYCODE_C, false),});
		EXTENDED_CHARACTER.put('?', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_SLASH, true),
			new Key(KeyEvent.KEYCODE_SLASH, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('!', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_1, true),
			new Key(KeyEvent.KEYCODE_1, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('@', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_2, true),
			new Key(KeyEvent.KEYCODE_2, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('#', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_3, true),
			new Key(KeyEvent.KEYCODE_3, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('$', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_4, true),
			new Key(KeyEvent.KEYCODE_4, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('%', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_5, true),
			new Key(KeyEvent.KEYCODE_5, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('^', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_6, true),
			new Key(KeyEvent.KEYCODE_6, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('&', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_7, true),
			new Key(KeyEvent.KEYCODE_7, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('*', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_8, true),
			new Key(KeyEvent.KEYCODE_8, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('(', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_9, true),
			new Key(KeyEvent.KEYCODE_9, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put(')', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_0, true),
			new Key(KeyEvent.KEYCODE_0, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('_', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_MINUS, true),
			new Key(KeyEvent.KEYCODE_MINUS, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('+', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_EQUALS, true),
			new Key(KeyEvent.KEYCODE_EQUALS, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('"', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_APOSTROPHE, true),
			new Key(KeyEvent.KEYCODE_APOSTROPHE, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put(':', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_SEMICOLON, true),
			new Key(KeyEvent.KEYCODE_SEMICOLON, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('~', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_GRAVE, true),
			new Key(KeyEvent.KEYCODE_GRAVE, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('|', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_BACKSLASH, true),
			new Key(KeyEvent.KEYCODE_BACKSLASH, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('<', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_COMMA, true),
			new Key(KeyEvent.KEYCODE_COMMA, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('>', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_PERIOD, true),
			new Key(KeyEvent.KEYCODE_PERIOD, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('{', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_LEFT_BRACKET, true),
			new Key(KeyEvent.KEYCODE_LEFT_BRACKET, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
		EXTENDED_CHARACTER.put('}', new Key[]{
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, true),
			new Key(KeyEvent.KEYCODE_RIGHT_BRACKET, true),
			new Key(KeyEvent.KEYCODE_RIGHT_BRACKET, false),
			new Key(KeyEvent.KEYCODE_SHIFT_LEFT, false)
		});
	}

	void sendKeyExtension(char ch) {
		for (Key k : EXTENDED_CHARACTER.get(ch)) {
			sendKey(k);
		}
	}

}
