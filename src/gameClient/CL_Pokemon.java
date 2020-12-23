package gameClient;

import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon implements Comparable<CL_Pokemon>  {
	private edge_data edge;
	private double value;
	private int type;
	private Point3D pos;
	private double min_dist;
	private int min_ro;
	
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		type = t;
	//	_speed = s;
		value = v;
		set_edge(e);
		pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject JsonPok = new JSONObject(json);
			int id = JsonPok.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{value="+ value +", type="+ type +"}";}

	public edge_data get_edge() {
		return edge;
	}

	public void set_edge(edge_data edge) {
		this.edge = edge;
	}

	public Point3D getLocation() {
		return pos;
	}

	public int getType() {return type;}

	public double getValue() {return value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}

	@Override
	public int compareTo(CL_Pokemon that) {
			if (this.value<that.value){return 1;}
			if(this.value>that.value){return -1;}
			return 0;
		}
	}



