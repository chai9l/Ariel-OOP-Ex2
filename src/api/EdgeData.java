package api;
/**
 * Implements the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 * @author boaz.benmoshe
 *
 */
public class EdgeData implements edge_data{

    int src;
    int dest;
    double weight;
    String info;
    String secInfo;
    int tag;

    public EdgeData(int s, int d,double w) {
        src = s;
        dest = d;
        weight = w;
        info = "";
        secInfo = "";
        tag = 0;
    }
    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return src;
    }
    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return dest;
    }
    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
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
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }
    /**
     * Returns second remark (meta data) associated with this edge.
     * @return
     */
    public String getSecInfo() {
        return secInfo;
    }
    /**
     * Allows changing the second remark (meta data) associated with this edge.
     * @param s
     */
    public void setSecInfo(String s) {
        secInfo = s;
    }

}
