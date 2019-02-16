/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.input;

/**
 *
 */
import space.under.libc.UInput;
/**
 *
 */
import java.io.IOException;

/**
 *
 * @author root
 */
public class UserInput extends DeviceInput {

	/**
	 * Buttons
	 */
	public enum Button {

		BRESET, BLEFT, BRIGHT, BMIDDLE, KLEFT, KRIGHT, KUP, KDOWN, KENTER, KVOLUP, KVOLDOWN, KBACK
	}

	public enum Action {

		SET, CLEAR
	}

	/**
	 *
	 */
	protected int __fd;

	/**
	 *
	 */
	public UserInput() throws IOException {
		super();
		/**
		 * open file descriptor to interact
		 */
		__fd = __clib.open(SYS_INTERFACE, 4001, 0);
		/**
		 * check if could open
		 */
		if (__fd < 0) {
			throw new IOException();
		}
		/**
		 * configure
		 */
		UInput.UInputUserDev dev = new UInput.UInputUserDev("Wipointer");
		/**
		 *
		 */
		if (__clib.write(__fd, dev.getPointer(), dev.size()) < dev.size()) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_EVBIT, UInput.EV_REL) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_RELBIT, UInput.REL_X) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_RELBIT, UInput.REL_Y) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_EVBIT, UInput.EV_KEY) < 0) {
			throw new IOException();
		}
		/**
		 * register Keys
		 */
		for (int i = 0; i < MapInput.LINUX.size(); i++) {
			int k = (int) MapInput.LINUX.keyAt(i);
			if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, k) < 0) {
				throw new IOException();
			}
		}
		/**
		 * register Buttons
		 */
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.BTN_LEFT) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.BTN_RIGHT) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.BTN_MIDDLE) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_DEV_CREATE, 0) < 0) {
			throw new IOException();
		}
	}

	/**
	 *
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable {
		/**/
		__clib.ioctl(__fd, UInput.UI_DEV_DESTROY, 0);
		/**/
		__clib.close(__fd);
		/**/
		super.finalize();
	}

	/**
	 * Move
	 *
	 * @param x
	 * @param y
	 * @throws IOException
	 */
	public void move(int x, int y) throws IOException {
		if (x == 0 && y == 0) {
			return;
		}

		UInput.InputEvent event = new UInput.InputEvent();
		/**
		 * X
		 */
		event.type = UInput.EV_REL;
		event.code = UInput.REL_X;
		event.value = x;
		event.write();
		if (__clib.write(__fd, event.getPointer(), event.size()) < 0) {
			throw new IOException();
		}
		/**
		 * Y
		 */
		event.type = UInput.EV_REL;
		event.code = UInput.REL_Y;
		event.value = y;
		event.write();
		if (__clib.write(__fd, event.getPointer(), event.size()) < 0) {
			throw new IOException();
		}
		/**
		 * Sync
		 */
		event.type = UInput.EV_SYN;
		event.code = UInput.SYN_REPORT;
		event.value = 0;
		event.write();
		if (__clib.write(__fd, event.getPointer(), event.size()) < 0) {
			throw new IOException();
		}
	}

	/**
	 * Click
	 *
	 * @param btn
	 * @param pressed
	 * @throws IOException
	 */
	public void click(Button btn, Action act) throws IOException {
		switch (btn) {
			case BLEFT:
				__click(UInput.BTN_LEFT, act == Action.SET);
				break;
			case BRIGHT:
				__click(UInput.BTN_RIGHT, act == Action.SET);
				break;
			case BMIDDLE:
				__click(UInput.BTN_MIDDLE, act == Action.SET);
				break;
			case KUP:
				__click(UInput.KEY_UP, act == Action.SET);
				break;
			case KLEFT:
				__click(UInput.KEY_LEFT, act == Action.SET);
				break;
			case KRIGHT:
				__click(UInput.KEY_RIGHT, act == Action.SET);
				break;
			case KDOWN:
				__click(UInput.KEY_DOWN, act == Action.SET);
				break;
			case KENTER:
				__click(UInput.KEY_ENTER, act == Action.SET);
				break;
			case KVOLUP:
				__click(UInput.KEY_VOLUMEUP, act == Action.SET);
				break;
			case KVOLDOWN:
				__click(UInput.KEY_VOLUMEDOWN, act == Action.SET);
				break;
			case KBACK:
				__click(UInput.KEY_HOME, act == Action.SET);
				break;
		}
	}
	/**
	 * click by Key
	 * @param key
	 * @param pressed
	 * @throws IOException 
	 */
	public void click(int key, boolean pressed) throws IOException {
		__click(MapInput.ANDROID.get(key), pressed);
	}
	/**
	 * @param key
	 * @param pressed
	 * @throws IOException
	 */
	private void __click(int key, boolean pressed) throws IOException {
		UInput.InputEvent event = new UInput.InputEvent();
		/**
		 * Key
		 */
		event.type = UInput.EV_KEY;
		event.code = (short) key;
		event.value = pressed ? 1 : 0;
		event.write();
		if (__clib.write(__fd, event.getPointer(), event.size()) < 0) {
			throw new IOException();
		}
		/**
		 * Sync
		 */
		event.type = UInput.EV_SYN;
		event.code = UInput.SYN_REPORT;
		event.value = 0;
		event.write();
		if (__clib.write(__fd, event.getPointer(), event.size()) < 0) {
			throw new IOException();
		}
	}
}
