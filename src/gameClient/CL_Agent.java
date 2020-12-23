package gameClient;
import api.*;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class CL_Agent {
		public static final double EPS = 0.0001;
		private static int count = 0;
		private static int seed = 3331;
		private int id;
		private geo_location pos;
		private double speed;
		private edge_data currEdge;
		private node_data currNode;
		private directed_weighted_graph gameGraph;
		private CL_Pokemon currFruit;
		private long _sg_dt;
		private double value;
		private LinkedList<node_data> curPath = null;
		private int dest = 0;

		public CL_Agent(directed_weighted_graph g, int start_node) {
			gameGraph = g;
			setMoney(0);
			this.currNode = gameGraph.getNode(start_node);
			pos = currNode.getLocation();
			id = -1;
			setSpeed(0);
		}
	public void update(String json) {
		JSONObject line;
		try {
			// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {this.id = id;}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				this.dest = dest; 									// ****update*****
				double value = ttt.getDouble("value");
				this.pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	//@Override
	public int getSrcNode() {return this.currNode.getKey();}

	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this.id+","
				+ "\"value\":"+this.value+","
				+ "\"src\":"+this.currNode.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.getSpeed()+","
				+ "\"pos\":\""+pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;
	}

		private void setMoney(double v) {this.value = v;}

		public boolean setNextNode(int dest) {
			boolean ans = false;
			int src = this.currNode.getKey();
			this.currEdge = gameGraph.getEdge(src, dest);
			if(currEdge !=null) { ans=true; }
			else { currEdge = null;}
			return ans;
		}
		public void setCurrNode(int src) {
			this.currNode = gameGraph.getNode(src);
		}

		public boolean isMoving() {
			return this.currEdge !=null;
		}

		public String toString() {
		return toJSON();
	}
		public String toString1() {
		String ans=""+this.getID()+","+	pos+", "+isMoving()+","+this.getValue();
		return ans;
	}
		public int getID() {
			return this.id;
		}
	
		public geo_location getLocation() {
			return pos;
		}

		public double getValue() {
			return this.value;
		}

		public int getNextNode() {
			int ans = -2;
			if(this.currEdge ==null) {
				ans = -1;}
			else {
				ans = this.currEdge.getDest();
			}
			return ans;
		}

		public double getSpeed() {
			return this.speed;
		}

		public void setSpeed(double v) {
			this.speed = v;
		}

		public CL_Pokemon get_curr_fruit() {
			return currFruit;
		}

		public void set_curr_fruit(CL_Pokemon curr_fruit) {
			this.currFruit = curr_fruit;
		}

		public void set_SDT(long ddtt) {
			long ddt = ddtt;
			if(this.currEdge !=null) {
				double weight = get_curr_edge().getWeight();
				geo_location srcLoc = gameGraph.getNode(get_curr_edge().getSrc()).getLocation();
				geo_location destLoc = gameGraph.getNode(get_curr_edge().getDest()).getLocation();
				double de = srcLoc.distance(destLoc);
				double dist = pos.distance(destLoc);
				if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
					 dist = currFruit.getLocation().distance(this.pos);
				}
				double norm = dist/de;
				double dt = weight*norm / this.getSpeed();
				ddt = (long)(1000.0*dt);
			}
			this.set_sg_dt(ddt);
		}
		
		public edge_data get_curr_edge() {
			return this.currEdge;
		}

		public long get_sg_dt() {
			return _sg_dt;
		}

		public void set_sg_dt(long _sg_dt) {
			this._sg_dt = _sg_dt;
		}

		//(added functions) agent path as list - check if need

		public LinkedList<node_data> getCurPath(){ return this.curPath; }

		public void setCurPath(LinkedList<node_data> path){
			this.curPath = path;
		}

		public void set_curr_edge(edge_data e){this.currEdge = e;}

		public int get_agent_dest(){return this.dest;}


	}
