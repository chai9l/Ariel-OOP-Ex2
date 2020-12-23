package api;

import java.util.Collection;
import java.util.HashSet;
/**
 * Implements the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 * @author boaz.benmoshe
 *
 */
public class NodeData implements node_data {

    private Integer key;
    int tag;
    double weight;
    String info;
    HashSet<node_data> neighbors;
    geo_location location;



    public NodeData(int key) {
        this.key = key;
        this.tag = -1;
        this.info = " ";
        this.neighbors = new HashSet<>();
        this.location = new GeoLocation();
    }

    public NodeData(double x, double y, double z, int key, String info, int tag, double weight) {
        this.location = new GeoLocation(x,y,z);
        this.key = key;
        this.info = info;
        this.tag=tag;
        this.weight = weight;
        this.neighbors = new HashSet<>();

    }

    public NodeData(double x, double y, double z, int key) {
        this.key = key;
        this.location = new GeoLocation(x,y,z);
        this.weight=-1;
        this.info="";
        this.tag=-1;
        this.neighbors = new HashSet<>();
    }

    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    @Override
    public int getKey() {
        return key;
    }
    /** Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return location;
    }
    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location = p;
    }
    /**
     * Returns the weight associated with this node.
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }
    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }


    /**
     * Adding node as neighbor of other node
     * @param t - the added neighbor
     */
    public void addNi(node_data t) {
        if (!neighbors.contains(t)) {
            neighbors.add(t);
        }
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the node neighbors.
     * @return Collection<node_data>
     */
    public Collection<node_data> getNi() {
        return neighbors;
    }
    /**
     * Removing node from being a neighbor of other node
     * @param t - the removed neighbor
     */
    public void removeNi(node_data t) {
        if (!neighbors.contains(t)) {
            return;
        }
        neighbors.remove(t);
    }
    /**
     * Removing all nodes from being a neighbors of other node
     */
    public void removeAllNi(){
        this.neighbors.clear();
    }
    /**
     * Implements a geo location <x,y,z>, aka Point3D
     */
    public static class GeoLocation implements geo_location {
        private double x, y, z;

        public GeoLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public GeoLocation() {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
        }

        @Override
        public double x() {
            return this.x;
        }

        @Override
        public double y() {
            return this.y;
        }

        @Override
        public double z() {
            return this.z;
        }

        @Override
        public double distance(geo_location g) {
            double dx = this.x - g.x();
            double dy = this.y - g.y();
            double dz = this.z - g.z();
            double ret = (dx * dx + dy * dy + dz * dz);
            return Math.sqrt(ret);
        }
    }
}
