/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package space.under.game.models;
/**
 */
import android.graphics.Canvas;
import android.graphics.Color;
/**
 */
import space.under.game.objects.Edge;
import space.under.game.vectors.Position;
/**
 * Field
 * @author Luis Monteiro
 */
public class Field {

	static public class Properties {
		/**
		 */
		public Properties() {
		}
		/**
		 */
		public Properties SetStart(int x, int y) {
			start_x = x;
			start_y = y;
			return this;
		}
		/**
		 */
		public Properties SetSize(int x, int y) {
			size_x = x;
			size_y = y;
			return this;
		}
		/**
		 */
		public Properties SetGoal(int s) {
			goal_s = s;
			return this;
		}
		/**
		 */
		public Properties SetColor(int c) {
			color = c;
			return this;
		}
		/**
		 */
		public Properties SetEdge(int e) {
			edge_s = e;
			return this;
		}
		/**
		 * default
		 */
		protected int start_x = 0;
		protected int start_y = 0;
		protected int size_x = 0;
		protected int size_y = 0;
		protected int goal_s = 10;
		protected int edge_s = 10;
		/**/
		protected int color = Color.parseColor("#EEEEEE");
	;
	}
	/**
	 *
	 */
	private final Edge __edge[];
	/**
	 *
	 */
	public Field(Properties p) {
		int x = p.size_x - p.edge_s;
		int y = p.size_y - p.edge_s;
		int sz = ((y - p.goal_s + p.edge_s) / 2);
		__edge = new Edge[]{
			new Edge(new Position(p.start_x, p.start_y), new Edge.Properties()
			.SetSize(p.edge_s, sz)
			.SetColor(p.color)
			),
			new Edge(new Position(p.start_x, p.start_y + sz + p.goal_s), new Edge.Properties()
			.SetSize(p.edge_s, sz)
			.SetColor(p.color)
			),
			new Edge(new Position(p.start_x + x, p.start_y), new Edge.Properties()
			.SetSize(p.edge_s, sz)
			.SetColor(p.color)
			),
			new Edge(new Position(p.start_x + x, p.start_y + sz + p.goal_s), new Edge.Properties()
			.SetSize(p.edge_s, sz)
			.SetColor(p.color)
			),
			new Edge(new Position(p.start_x, p.start_y), new Edge.Properties()
			.SetSize(x + p.edge_s, p.edge_s)
			.SetColor(p.color)
			),
			new Edge(new Position(p.start_x, p.start_y + y), new Edge.Properties()
			.SetSize(x + p.edge_s, p.edge_s)
			.SetColor(p.color)
			)
		};
	}
	/**
	 * get Edges
	 */
	public Edge[] getEdges() {
		return __edge;
	}
	/**
	 * Draw
	 *
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		for (Edge e : __edge) {
			e.draw(canvas);
		}
	}
	/**
	 * set Alpha
	 *
	 * @param alpha
	 */
	public void setAlpha(int alpha) {
		for (Edge e : __edge) {
			e.setAlpha(alpha);
		}
	}
}
