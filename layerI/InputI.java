/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 * imports
 */
import java.io.IOException;
/**
 *
 */
import space.base.AppI;
import space.base.Module;
import space.base.Data;
import space.layerI.TouchI.Key;
import space.under.input.DeviceInput;
import space.under.input.UserInput;

/**
 *
 * @author monteiro
 */
public class InputI extends Module {

	/**
	 * data
	 */
	public static class Position extends Data {

		/**
		 */
		final private float _p[];

		/**
		 */
		public Position(float x, float y) {
			super();
			/*
			 */
			_p = new float[]{x, y};
		}

		/**
		 */
		public float getX() {
			return _p[0];
		}

		public float getY() {
			return _p[1];
		}

		/**
		 */
		public Position update(float x, float y) {
			_p[0] = x;
			_p[1] = y;
			return this;
		}
	}

	public static class Command extends Data {
		/**
		 */
		final private UserInput.Button __btn;

		final private UserInput.Action __act;

		/**
		 */
		public Command(UserInput.Button btn, UserInput.Action act) {
			super();
			/**/
			__btn = btn;
			/**/
			__act = act;
		}

		/**
		 */
		public UserInput.Button getBTN() {
			return __btn;
		}

		public UserInput.Action getACT() {
			return __act;
		}
	}

	/**
	 */
	protected UserInput _uinput;

	/**
	 */
	public InputI(AppI app) {
		super(app, 200);
	}

	/**
	 * open input
	 *
	 * @param retries
	 * @return
	 */
	@Override
	protected Integer open(Integer retries) {

		try {
			DeviceInput.permissionRequest();
		} catch (IOException ex) {
			logger(LogType.WARNING, "Permission Request Fail");
			/**
			 */
			send(new ModuleException(getName(), "Open UInput Fail"));
			/**
			 */
			return (retries + 1) * 1000;
		}
		/**
		 * create _uinput
		 */
		if (_uinput == null) {
			try {
				_uinput = new UserInput();
			} catch (IOException ex) {
				logger(LogType.WARNING, "Create Mouse Fail: " + ex.getMessage());
				/**
				 */
				send(new ModuleException(getName(), "Open UInput Fail"));
				/**
				 */
				return (retries + 1) * 1000;
			}
		}
		return 0;
	}

	/**
	 */
	private float __x_carrier = 0.0f;
	private float __y_carrier = 0.0f;

	@Override
	public void onRun(Data data) {
		/**
		 */
		if (data instanceof Position) {
			Position tmp = (Position) data;
			try {
				/**
				 * prepare
				 */
				float x = tmp.getX() + __x_carrier;
				float y = tmp.getY() + __y_carrier;
				/**
				 * update position
				 */
				_uinput.move((int) x, (int) y);
				/**
				 * update carrier
				 */
				__x_carrier = x - (float) (int) x;
				__y_carrier = y - (float) (int) y;

			} catch (IOException ex) {
				/**/
				logger(LogType.WARNING, "Move Fail");
			}
		} else if (data instanceof Command) {
			Command tmp = (Command) data;
			try {
				/**
				 * update button
				 */
				_uinput.click(tmp.getBTN(), tmp.getACT());
				
			} catch (IOException ex) {
				/**/
				logger(LogType.WARNING, "Tap Fail");
			}
			/**
			 */
			__x_carrier = 0.0f;
			__y_carrier = 0.0f;
		} else if (data instanceof Key) {
			Key tmp = (Key) data;
			try {
				/**
				 * update key
				 */
				_uinput.click(tmp.getCode(), tmp.getPress());
				
			} catch (IOException ex) {
				/**/
				logger(LogType.WARNING, "Tap Fail");
			}
			/**
			 */
			__x_carrier = 0.0f;
			__y_carrier = 0.0f;
		}
	}

	/**
	 *
	 * @param retries
	 * @return
	 */
	@Override
	protected synchronized Integer close(Integer retries) {
		try {
			/**
			 * remove permissions
			 */
			DeviceInput.permissionRemove();
		} catch (IOException ex) {
			logger(LogType.WARNING, "Permission Remove Fail");
		}
		return 0;
	}

}
