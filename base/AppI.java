package space.base;

import android.content.Context;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author monteiro
 */
public interface AppI {
	/**
	 * get context
	 */
	public Context getContext();
	/**
	 * execute
	 */
	public void execute(Runnable exe);
	/**
	 * execute pause
	 */
	public void pauseContext();
}
