/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerI;

/**
 * imports
 */
import java.util.HashSet;
import java.util.Set;
import space.base.Module;
import space.base.Data;
import space.base.AppI;
/**
 *
 * @author monteiro
 */
public class TouchI extends Module {

	/**
	 * data
	 */
	public static class Touch extends Data {

		/**
		 *
		 */
		public enum Type {

			DOWN, UP, MOVE, RESET
		}
		/**
		 *
		 */
		final private Integer _id;
		final private Integer _x;
		final private Integer _y;
		final private Type _type;

		/**
		 *
		 */
		public Touch(Integer id, Integer x, Integer y, Type type) {
			super();
			/*
			 */
			_id = id;
			_x = x;
			_y = y;
			_type = type;
		}

		/**
		 *
		 */
		public Integer getID() {
			return _id;
		}

		public Integer getX() {
			return _x;
		}

		public Integer getY() {
			return _y;
		}

		public Type getType() {
			return _type;
		}
	}
	/**
	 * data
	 */
	public static class Key extends Data {
		/**
		 *
		 */
		final private int _code;
		final private boolean _press;

		/**
		 *
		 */
		public Key(int code, boolean press) {
			super();
			/*
			 */
			_code = code;
			_press = press;
		}
		/**
		 *
		 */
		public int getCode() {
			return _code;
		}

		public boolean getPress() {
			return _press;
		}
	}
	/**
	 *
	 */
	final private Set<Integer> __blockkeys = new HashSet<Integer>();
	public TouchI(AppI app, int[] blockkeys) {
		super(app, 200);
		/**
		 */
		for(int k: blockkeys){
			__blockkeys.add(k);
		}
	}
	public TouchI(AppI app) {
		super(app, 200);
	}
	/**
	 * 
	 */
	@Override
	public void onRun(Data data) {
		if(data instanceof Key){
			if(__blockkeys.contains(((Key)data).getCode())){
				return;
			}
		}
		send(data);
	}

	@Override
	public void onInit(Data data) {
		if(data instanceof Key){
			if(__blockkeys.contains(((Key)data).getCode())){
				return;
			}
		}
		send(data);
	}
	
	
}
