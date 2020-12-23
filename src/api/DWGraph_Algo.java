
package api;

import com.google.gson.*;

import java.io.*;
import java.util.*;
/**
 * Implements a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 *
 * @author boaz.benmoshe
 *
 */
public class DWGraph_Algo implements dw_graph_algorithms {


    DWGraph_DS graph = new DWGraph_DS();
    private Queue<node_data> que;                           //isConnected Method
    private HashSet<node_data> checkNodes;                  //isConnected
    private HashSet<node_data> transposeCheckNodes;                  //isConnected

    private Map<node_data, Boolean> vis;                    //visited
    private Map<node_data, node_data> prev;                 //parents
    private PriorityQueue<node_data> pQue;                  //for shortest path as list

    /**
     * Comparator using got priority queue.
     * set node weight as priority
     */
    public static class CompareDis implements Comparator<node_data> {

        @Override
        public int compare(node_data node1, node_data node2) {
            return Double.compare(node1.getWeight(), node2.getWeight());
        }
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        graph = (DWGraph_DS) g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * Reset all graph nodes tag to zero
     * @param g
     */
    public void reset(DWGraph_DS g) {
        for (node_data i : g.getV()) {
            i.setTag(0);
        }
    }
    /**
     * Transpose all graph edges directions - using in "isConnected" method
     * @param g
     */
    private directed_weighted_graph graphTranspose(directed_weighted_graph g) {
        directed_weighted_graph ret = new DWGraph_DS();

        for (node_data runner : g.getV()) {
            NodeData temp = new NodeData(runner.getKey());
            temp.setTag(0);
            ret.addNode(temp);
        }

        for (edge_data edge : graph.getEdges()) {
            ret.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
        }

        return ret;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph copyFrom = graph;
        directed_weighted_graph copyTo = new DWGraph_DS();
        HashSet<node_data> ver = new HashSet<>(graph.getV());       //all vertices as set

        while (!ver.isEmpty()) {
            if (ver.iterator().hasNext()) {
                node_data node = ver.iterator().next();
                LinkedList<node_data> que = new LinkedList<>();
                que.add(node);
                while (!que.isEmpty()) {
                    node = que.poll();
                    copyTo.addNode(node);

                    for (edge_data curNei : copyFrom.getE(node.getKey())) {     //iterate on node neighbors
                        double edgeWeight = curNei.getWeight();   //edge weight
                        node_data destNode = copyFrom.getNode(curNei.getDest());
                        if (ver.contains(destNode)) {
                            copyTo.addNode(destNode);                    //add node to copied graph
                            que.add(destNode);
                            copyTo.connect(node.getKey(), destNode.getKey(), edgeWeight); // add edge(mark as neighbor)
                            ver.remove(destNode);
                        } else if (!(copyTo.getE(node.getKey()).contains(destNode))) {   //if dest and node are not connected yet
                            copyTo.connect(node.getKey(), destNode.getKey(), edgeWeight);
                        }
                    }
                    ver.remove(node);
                }
            }
        }
        ((DWGraph_DS) copyTo).setMC(copyFrom.getMC());       //set tha same mc
        return copyTo;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node.
     * assuming directional graph (all n*(n-1) ordered pairs).
     * @return
     */
    @Override
    public boolean isConnected() {
        if (graph.nodeSize() == 0 || graph.nodeSize() == 1)
            return true;
        node_data start = graph.getV().stream().iterator().next();
        this.que = new LinkedList<node_data>();
        this.checkNodes = new HashSet<node_data>();
        this.transposeCheckNodes = new HashSet<node_data>();
        boolean gIsConnected = false;
        boolean gtIsConnected = false;


        for (node_data cur : graph.getV())
            cur.setTag(0);

        start.setTag(1);
        que.add(start);
        while (!que.isEmpty()) {
            node_data at = que.poll();
            checkNodes.add(at);
            for (edge_data cur : graph.getE(at.getKey())) {
                node_data dest = graph.getNode(cur.getDest());
                if (dest.getTag() == 0) {
                    if (graph.getE(dest.getKey()).size() != 0) {
                        dest.setTag(1);
                        que.add(dest);
                    }
                }
            }
        }

        for (node_data cur : graph.getV())
            cur.setTag(0);
        if (checkNodes.size() == graph.nodeSize())
            gIsConnected = true;


        // start transposing and check
        DWGraph_DS transposeGraph = new DWGraph_DS();
        transposeGraph = (DWGraph_DS) graphTranspose(graph);

        for (node_data cur : transposeGraph.getV())
            cur.setTag(0);

        node_data start2 = transposeGraph.getV().stream().iterator().next();
        start2.setTag(1);
        que.add(start2);
        while (!que.isEmpty()) {
            node_data at = que.poll();
            transposeCheckNodes.add(at);
            for (edge_data cur : transposeGraph.getE(at.getKey())) {
                node_data dest = transposeGraph.getNode(cur.getDest());
                if (dest.getTag() == 0) {
                    if (transposeGraph.getE(dest.getKey()).size() != 0) {
                        dest.setTag(1);
                        que.add(dest);
                    }
                }
            }
        }
        for (node_data cur : transposeGraph.getV())
            cur.setTag(0);

        if (transposeCheckNodes.size() == transposeGraph.nodeSize())
            gtIsConnected = true;


        if (gIsConnected && gtIsConnected) {
            return true;
        }
        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        node_data start = graph.getNode(src);                                    //int to node
        node_data finish = graph.getNode(dest);                                  //int to node
        this.prev = new HashMap<node_data, node_data>();                         //parents
        this.pQue = new PriorityQueue<>(graph.nodeSize(), new CompareDis());     //priority queue by distance
        Map<node_data, Boolean> vis1 = new HashMap<node_data, Boolean>();        //visited

        if (src == dest)
            return 0.0;

        if (start == null || finish == null)
            return -1;



            for (node_data set : graph.getV())
                set.setWeight(-1);                   //set each node distance "unreachable"

            pQue.add(start);
            vis1.put(start, true);
            start.setWeight(0);
            while (!pQue.isEmpty()) {
                node_data current = pQue.poll();

                for (edge_data edge : graph.getE(current.getKey())) {        //current node neighbors checking and update
                    int d = edge.getDest();
                    node_data nodeDest = graph.getNode(d);
                    if (!vis1.containsKey(nodeDest.getKey())) {
                        if (nodeDest.getWeight() == -1)                       //if "unreachable" tag it reach but max distance
                            nodeDest.setWeight(Double.MAX_VALUE);

                        double newDis = current.getWeight() + edge.getWeight(); //the new distance
                        if (newDis < nodeDest.getWeight()) {                    //choose the shortest
                            nodeDest.setWeight(newDis);
                            prev.put(nodeDest, current);
                            pQue.add(nodeDest);
                        }
                    }
                }
                vis1.put(current, true);
            }
            return graph.getNode(dest).getWeight();
        }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * If no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {

        node_data source = graph.getNode(src);                                          //int to node
        node_data finish = graph.getNode(dest);                                         //int to node
        this.prev = new HashMap<node_data, node_data>();                                //parents
        this.pQue = new PriorityQueue<>(graph.nodeSize(), new CompareDis());            //queue
        Map<node_data, Boolean> vis = new HashMap<node_data, Boolean>();
        LinkedList<node_data> directions = new LinkedList<node_data>();

        if (src == dest) {
            directions.add(source);
            return directions;
        }

        if (source == null || finish == null)
            return null;

            for (node_data set : graph.getV())
                set.setWeight(-1);                                       //set each node distance "unreachable"

            pQue.add(source);
            vis.put(source, true);
            source.setWeight(0);
            node_data current = source;
            while (!pQue.isEmpty()) {
                current = pQue.poll();

                if (current.getKey()==finish.getKey())
                    break;
                for (edge_data edge : graph.getE(current.getKey())) {        //current node neighbors checking and update
                    int d = edge.getDest();
                    node_data nodeDest = graph.getNode(d);
                    if (!vis.containsKey(nodeDest)) {
                        if (nodeDest.getWeight() == -1)                       //if "unreachable" tag it reach but max distance
                            nodeDest.setWeight(Integer.MAX_VALUE);

                        double newDis = current.getWeight() + edge.getWeight(); //the new distance
                        if (newDis < nodeDest.getWeight()) {                    //choose the shortest
                            nodeDest.setWeight(newDis);
                            prev.put(nodeDest, current);
                            pQue.add(nodeDest);
                        }
                    }
                }
                vis.put(current, true);
            }

            if(!(current.getKey()==finish.getKey()))
                return null;

//            if (!current.equals(finish)) {
//                return null;
//            }
            for (node_data node = finish; node != null; node = prev.get(node)) {    //adding nodes directions in reverse order
                directions.add(node);
            }
            Collections.reverse(directions);
            return directions;

        }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param filename - - the file name include a relative path.
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonGraph = new JsonObject();
        JsonArray jsonNodes = new JsonArray();
        JsonArray jsonEdges = new JsonArray();
        for (node_data node : graph.getV()) {
            JsonObject json = new JsonObject();
            String loc = node.getLocation().x() + "," + node.getLocation().y() + "," + node.getLocation().z();
            json.addProperty("pos", loc);
            json.addProperty("id", node.getKey());
            jsonNodes.add(json);
        }
        for (edge_data edge : ((DWGraph_DS) graph).getEdges()) {
            JsonObject json = new JsonObject();
            json.addProperty("src", edge.getSrc());
            json.addProperty("w", edge.getWeight());
            json.addProperty("dest", edge.getDest());
            jsonEdges.add(json);
        }
        jsonGraph.add("Edges", jsonEdges);
        jsonGraph.add("Nodes", jsonNodes);

        String fullGraphJson = gson.toJson(jsonGraph);
        try {
            PrintWriter pw = new PrintWriter(filename);
            pw.write(fullGraphJson);
            pw.close();
            return true;
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }
    /**
     * This method loads a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new DwGraphJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            directed_weighted_graph toInit = gson.fromJson(reader, directed_weighted_graph.class);
            this.init(toInit);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
