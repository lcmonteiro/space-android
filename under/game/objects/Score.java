/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.objects;
/**
 * Score
 * @author Luis Monteiro
 */
public class Score extends Text {
	/**
	 */
	private int __score = 0;
	/**
	 */
	public Score(int score, Properties p) {
		super("" + score, p);
		__score = score;
	}
	public Score(Properties p) {
		super("0", p);
	}
	/**
	 * set goal
	 */
	public void setGoal() {
		__score++;
		setText("" + __score);
	}
	public void setGoals(int n) {
		__score += n;
		setText("" + __score);
	}
	/**
	 * reset
	 */
	public void reset(){
		__score = 0;
		setText("" + __score);
	}
	/**
	 * set
	 */
	public void set(int n) {
		__score = n;
		setText("" + __score);
	}
	/**
	 * get
	 */
	public int get(){
		return __score;
	}
}
