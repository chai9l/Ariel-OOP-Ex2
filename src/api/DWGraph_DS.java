package api;

import org.w3c.dom.Node;

import java.util.*;
/**
 * Implements a directional weighted graph.
 * Support a large number of nodes (over 100,000).
 * Based on an efficient compact representation
 */
public class DWGraph_DS implements directed_weighted_graph {


    private HashMap<Integer,node_data> graph = new HashMap<>();
    private HashMap<String,edge_data> edges = new HashMap<>();
    private int mc;

    public DWGraph_DS() {
    }

    private String generateKey(int node1, int node2) {
        String s = node1 + "/" + node2;
        return s;
    }

    public node_data getFirst() {
        return graph.values().stream().findFirst().get();
    }

    public boolean hasEdge(int node1, int node2) {
        if(graph == null) { return false; }
        if(!graph.containsKey(node1) && !graph.containsKey(node2)) {
            return false;
        }
        NodeData n1 = (NodeData) graph.get(node1);
        String s = generateKey(node1,node2);
        edge_data ret = edges.get(s);
        if(ret == null) {
            return false;
        }
        return true;
    }
    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return graph.get(key);
    }
    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        String temp = generateKey(src,dest);
        return edges.get(temp);
    }
    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!this.graph.containsKey(n.getKey())) {
            this.graph.put(n.getKey(), n);
            this.mc++;
        }
    }
    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {

        if (!this.graph.containsKey(src) || !this.graph.containsKey(dest) || src == dest || w<=0)
            return;
        NodeData n1 = (NodeData) graph.get(src);
        NodeData n2 = (NodeData) graph.get(dest);
        String key = generateKey(src,dest);
        edge_data e = new EdgeData(src,dest,w);

        if (!this.hasEdge(src, dest)){
            this.edges.put(key,e);
            n1.addNi(n2);
            this.mc++;
        }
        if(this.hasEdge(src,dest) && this.getEdge(src, dest).getWeight() != w) {
            this.edges.put(key,e);
            this.mc++;
            }
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        if(graph == null) { return null; }
        return graph.values();
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if(graph == null || edges.size() == 0) { return null; }
        NodeData node = (NodeData)getNode(node_id);
        HashSet<edge_data> ret = new HashSet<>();
        for(node_data curr : node.getNi()) {
            String temp = generateKey(node.getKey(),curr.getKey());
            ret.add(edges.get(temp));
        }
        return ret;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges in the graph.
     * @return Collection<edge_data>
     */
    public Collection<edge_data>getEdges(){
        return edges.values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_data removeNode(int key) {
        NodeData ret = (NodeData)graph.get(key);
        LinkedList<node_data> l = new LinkedList<>();
        if (ret != null) {
            for (node_data i : ret.getNi()) {
                l.add(i);
            }

            for(node_data i : l) {
                removeEdge(i.getKey(),key);
                removeEdge(key,i.getKey());
                // mc--;
            }

            for(node_data i : graph.values()){
                removeEdge(i.getKey(),key);
            }
            mc++;

            return graph.remove(ret.getKey());
        }
        return null;
    }
    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(graph==null) { return null; }
        if(graph.containsKey(src) && graph.containsKey(dest)) {
            String s = generateKey(src, dest);
            String s2 = generateKey(dest, src);
            edge_data ret = edges.get(s);
            if (hasEdge(src, dest)) {
                edges.remove(s);
                NodeData nodeX = (NodeData)graph.get(src);
                NodeData nodeY = (NodeData)graph.get(dest);
                nodeX.removeNi(nodeY);
                nodeY.removeNi(nodeX);
                mc++;
                return ret;
            }
        }
        return null;
    }
    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return graph.size();
    }
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return edges.size();
    }
    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }
    /**
     * Set new MC to the copied graph,
     * @param copyMC
     */
    public void setMC(int copyMC) {this.mc = copyMC;}
}
