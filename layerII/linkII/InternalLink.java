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
import space.layerI.LinkI;

/**
 *
 * @author monteiro
 */
public class InternalLink extends LinkI {

	/**
	 */
	public InternalLink(AppI app) {
		super(app, 7777, 0);
	}
	public InternalLink(AppI app, String address) {
		super(app, 7777, address, 0);
	}
}
