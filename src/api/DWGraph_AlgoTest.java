package api;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    private directed_weighted_graph graph;
    private dw_graph_algorithms algo;

        @BeforeEach
        void setup() {
            algo = new DWGraph_Algo();
            graph = new DWGraph_DS();
            algo.init(graph);
        }

        @Test
        void copy() {
            directed_weighted_graph copied = new DWGraph_DS();
            copied = algo.copy();
            assertNotSame(graph,copied);
        }

        @Test
        void isConnected() {
            node_data n1 = new NodeData(1);
            node_data n2 = new NodeData(2);
            node_data n3 = new NodeData(3);

            graph.addNode(n1);
            assertTrue(algo.isConnected());

            graph.addNode(n2);
            graph.addNode(n3);
            graph.connect(1,2,10);
            graph.connect(2,3,10);
            assertFalse(algo.isConnected());

            graph.connect(3,1,10);
            assertTrue(algo.isConnected());

            graph.removeEdge(1,2);
            assertFalse(algo.isConnected());

            graph.connect(1,3,10);
            graph.connect(3,2,10);
            assertTrue(algo.isConnected());
        }

        @Test
        void shortestPathDist() {
            double check;

            for(int i = 1; i<=10; i++) {
                node_data temp = new NodeData(i);
                graph.addNode(temp);
            }
            graph.connect(1,2,1);
            graph.connect(2,3, 1);
            graph.connect(3,4,1);
            graph.connect(4,5,1);
            graph.connect(5,10,1);

            graph.connect(1,6,1);
            graph.connect(6,7,1);
            graph.connect(7,8,1);
            graph.connect(8,9,1);
            graph.connect(9,10,2);

            check = 5;
            double ret = algo.shortestPathDist(1,10);
            assertTrue(check == ret);
            graph.removeNode(5);
            assertTrue(check == ret);
        }

        @Test
        void shortestPath() {
            for(int i = 1; i<=10; i++) {
                node_data temp = new NodeData(i);
                graph.addNode(temp);
            }
            graph.connect(1,2,1);
            graph.connect(2,3, 1);
            graph.connect(3,4,1);
            graph.connect(4,5,1);
            graph.connect(5,10,1);

            graph.connect(1,6,10);
            graph.connect(6,7,20);
            graph.connect(7,8,30);
            graph.connect(8,9,40);
            graph.connect(9,10,50);

            LinkedList<node_data> list = new LinkedList<>();
            for(int i = 1; i<=5; i++ ) {
                node_data temp = graph.getNode(i);
                list.add(temp);
            }
            node_data last = graph.getNode(10);
            list.add(last);
            assertEquals(list,algo.shortestPath(1,10));
            list.pollLast();
            assertNotEquals(list,algo.shortestPath(1,10));
        }


    @Test
    void save() {
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(1,2,10);
        graph.connect(2,3,10);

        boolean ret = algo.save("tests/check");
        System.out.println(ret);
    }

    @Test
    void load() {
        algo.load("data/A1");
        if(algo.getGraph().getNode(10).getLocation().x() == 35.197400995964486) {
            System.out.println(algo.getGraph().getNode(10).getLocation().x());
            System.out.println("load complete!");
        }
    }
}