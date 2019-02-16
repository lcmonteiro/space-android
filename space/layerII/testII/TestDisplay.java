/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.layerII.testII;
/**
 */
import space.base.AppI;
import space.base.Module;

/**
 *
 * @author root
 */
public class TestDisplay extends Module {
	
	/**
	 *
	 */
	public TestDisplay(AppI app) {
		super(app, 200);
	}
	
	/**
	 * 
	 * @param retries
	 * @return 
	 */
	@Override
	protected Integer play(Integer retries) {
		/**
		 *
		 */
		send(new Vector3D(1,2,3));
		/**
		 *
		 */
		return super.play(retries);
	}
	
}
