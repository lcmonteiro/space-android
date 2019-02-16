/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.base;

/**
 *
 */
import space.layerI.CameraI;
import space.layerI.LinkI;
import space.layerI.MoveI;
import space.layerI.SerialI;
import space.layerI.TouchI;
import space.layerI.InputI;
/**
 *
 */
import android.util.DisplayMetrics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author monteiro
 */
public class App extends Module {

	/**
	 * Data Types
	 */
	public static class AppControl extends Control {

		String _module;

		/**
		 */
		public AppControl(String module, Type type) {
			super(type);
			/**
			 */
			_module = module;
		}

		/**
		 */
		public String getModule() {
			return _module;
		}
	}
	/**
	 * /**
	 *
	 */
	final protected DisplayMetrics _metrics;
	/**
	 *
	 */
	final protected List<Module> _services;
	/**
	 * level I
	 */
	protected CameraI _camera = null;

	protected SerialI _serial = null;

	protected TouchI _touch = null;

	protected MoveI _move = null;

	protected LinkI _link = null;

	protected InputI _input = null;

	/**
	 */
	public App() {
		super(null, 10);
		/**
		 */
		_metrics = new DisplayMetrics();

		_services = new ArrayList<Module>();
	}

	/**
	 */
	protected void sendClose(Module module) {
		send(module.getName(), new ModuleControl(Control.Type.CLOSE));
	}

	protected void sendPause(Module module) {
		send(module.getName(), new ModuleControl(Control.Type.PAUSE));
	}

	protected void sendPlay(Module module) {
		send(module.getName(), new ModuleControl(Control.Type.PLAY));
	}

	protected void sendPausePlay(Module module) {
		send(module.getName(), new ModuleControl(Control.Type.PAUSE));
		send(module.getName(), new ModuleControl(Control.Type.PLAY));
	}

	/**
	 */
	protected void sendPause(LinkI link, Module module) {
		send(link.getName(), new AppControl(module.getName(), Control.Type.PAUSE));
	}

	protected void sendPlay(LinkI link, Module module) {
		send(link.getName(), new AppControl(module.getName(), Control.Type.PLAY));
	}

	protected void sendPausePlay(LinkI link, Module module) {
		send(link.getName(), new AppControl(module.getName(), Control.Type.PAUSE));
		send(link.getName(), new AppControl(module.getName(), Control.Type.PLAY));
	}
}
