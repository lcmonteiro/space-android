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
public class Pointer extends DeviceInput {

	public enum Button {

		BLEFT, BRIGHT, BMIDDLE, KLEFT, KRIGHT, KUP, KDOWN, KENTER, KVOLUP, KVOLDOWN, KBACK
	}

	/**
	 *
	 */
	protected int __fd;

	/**
	 *
	 */
	public Pointer() throws IOException {
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
		UInput.UInputUserDev dev = new UInput.UInputUserDev("MouseLM");
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
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_UP) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_LEFT) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_RIGHT) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_VOLUMEDOWN) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_VOLUMEUP) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_DOWN) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_ENTER) < 0) {
			throw new IOException();
		}
		if (__clib.ioctl(__fd, UInput.UI_SET_KEYBIT, UInput.KEY_HOME) < 0) {
			throw new IOException();
		}
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
		/**
		 *
		 */
		__clib.ioctl(__fd, UInput.UI_DEV_DESTROY, 0);
		/**
		 *
		 */
		__clib.close(__fd);
		/**
		 *
		 */
		super.finalize();
	}

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

	public void click(Button btn, boolean pressed) throws IOException {

		UInput.InputEvent event = new UInput.InputEvent();
		/**
		 * Button
		 */
		event.type = UInput.EV_KEY;
		switch (btn) {
			case BLEFT:
				event.code = UInput.BTN_LEFT;
				break;
			case BRIGHT:
				event.code = UInput.BTN_RIGHT;
				break;
			case BMIDDLE:
				event.code = UInput.BTN_MIDDLE;
				break;
			case KUP:
				event.code = UInput.KEY_UP;
				break;
			case KLEFT:
				event.code = UInput.KEY_LEFT;
				break;
			case KRIGHT:
				event.code = UInput.KEY_RIGHT;
				break;
			case KDOWN:
				event.code = UInput.KEY_DOWN;
				break;
			case KENTER:
				event.code = UInput.KEY_ENTER;
				break;
			case KVOLUP:
				event.code = UInput.KEY_VOLUMEUP;
				break;
			case KVOLDOWN:
				event.code = UInput.KEY_VOLUMEDOWN;
				break;
			case KBACK:
				event.code = UInput.KEY_HOME;
				break;
			default:
				return;
		}
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
