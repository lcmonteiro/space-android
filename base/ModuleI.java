package space.base;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author monteiro
 */
public interface ModuleI {
	/**
	 * 
	 * @param name
	 * @param data 
	 */
	public void receive(String name, Data data);
	/**
	 * 
	 * @param data 
	 */
	public void receive(Data data);
}
