package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    DWGraph_DS graph;
    DWGraph_DS graph2;
    node_data n1 = new NodeData(1);
    node_data n2 = new NodeData(2);
    node_data n3 = new NodeData(3);

    @BeforeEach
    void setup() {
        graph = new DWGraph_DS();
        graph2 = new DWGraph_DS();
    }

    @Test
    void hasEdge() {
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.connect(1,3,10);
        assertFalse(graph.hasEdge(1,2));
        assertTrue(graph.hasEdge(1,3));
        graph.removeEdge(1, 3);
        graph.connect(1,2,15);
        assertTrue(graph.hasEdge(1,2));
        assertFalse(graph.hasEdge(1,3));
    }

    @Test
    void connect() {
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.connect(1,3,10);
        assertFalse(graph.hasEdge(1,2));
        assertTrue(graph.hasEdge(1,3));

        graph.removeEdge(1, 3);
        graph.connect(1,2,15);
        assertTrue(graph.hasEdge(1,2));
        assertFalse(graph.hasEdge(1,3));
    }

    @Test
    void getV() {
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.connect(1,2,10);
        graph.connect(1,3,20);

        Collection<node_data> g = graph.getV();
        Iterator<node_data> runner = g.iterator();
        while(runner.hasNext()) {
            node_data check = runner.next();
            assertNotNull(check);
        }
    }

    @Test
    void getE() {
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.connect(1,2,10);
        graph.connect(1,3,20);

        Collection<edge_data> g = graph.getE(1);
        Iterator<edge_data> runner = g.iterator();
        while(runner.hasNext()) {
            edge_data check = runner.next();
            assertNotNull(check);
        }
    }

    @Test
    void removeNode() {
        int size = 3;
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        assertTrue(graph.nodeSize() == size);
        graph.removeNode(1);
        assertTrue(graph.nodeSize() == size-1);
        graph.removeNode(2);
        assertTrue(graph.nodeSize() == size-2);
        graph.removeNode(3);
        assertTrue(graph.nodeSize() == size-3);
        assertNull(graph.removeNode(1));
        assertNull(graph.removeNode(2));
        assertNull(graph.removeNode(3));
    }

    @Test
    void removeEdge() {
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.connect(1,3,10);
        assertTrue(graph.hasEdge(1,3));
        graph.removeEdge(1,3);
        assertFalse(graph.hasEdge(1,3));
    }

    @Test
    void nodeSize() {
        for(int i=1 ; i<=5 ; i++) {
            graph.addNode(new NodeData(i));
            graph2.addNode(new NodeData(i+5));
        }

        assertEquals(graph2.nodeSize(), graph.nodeSize());
        graph.removeNode(5);
        assertNotEquals(graph2.nodeSize(), graph.nodeSize());
    }

    @Test
    void edgeSize() {
        for(int i=1 ; i<=3 ; i++) {
            graph.addNode(new NodeData(i));
            graph2.addNode(new NodeData(i+3));
        }
        graph.connect(1,3,10);
        graph.connect(1,2,15);
        graph2.connect(4,5,20);
        graph2.connect(4,6,25);

        assertEquals(graph.edgeSize(),graph2.edgeSize());
        graph.removeEdge(1,3);
        assertNotEquals(graph.edgeSize(),graph2.edgeSize());
    }

}