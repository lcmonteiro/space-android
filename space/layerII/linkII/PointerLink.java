/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.linkII;

/**
 * imports
 */
import space.base.AppI;
import space.base.Data;
import space.layerI.InputI.Command;
import space.layerI.InputI.Position;
import space.layerI.LinkI;
import space.under.filters.FirAngle;
import space.under.input.UserInput.Button;

/**
 *
 * @author monteiro
 */
public class PointerLink extends LinkI {
	/**
	 */
	private final FirAngle __fx;
	private final FirAngle __fy;

	/**
	 */
	public PointerLink(AppI app) {
		super(app, 6868, 10000);
		/**
		 */
		__fx = new FirAngle(new float[]{0.1f, 0.25f, 0.3f, 0.25f, 0.1f});
		__fy = new FirAngle(new float[]{0.1f, 0.25f, 0.3f, 0.25f, 0.1f});
		/**
		 */
		setPriority(Thread.MAX_PRIORITY);
	}

	@Override
	protected Boolean send(Data data) {
		if (data instanceof Position) {
			/**
			 */
			return super.send(__process((Position) data));
		}
		if (data instanceof Command) {
			/**
			 */
			if (((Command) data).getBTN()== Button.BRESET) {
				__reset();
			}
		}
		return super.send(data);
	}

	/**
	 */
	private void __reset() {
		/**
		 */
		__fx.reset();
		__fy.reset();
	}

	/**
	 * process
	 */
	private Position __process(Position pos) {
		/**
		 */
		__fx.update(pos.getX());
		__fy.update(pos.getY());
		/**
		 */
		return pos.update(
			__fx.get(), __fy.get()
		);
	}
}
