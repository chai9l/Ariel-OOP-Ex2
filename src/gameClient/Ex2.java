package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class Ex2 implements Runnable {

    private static MyFrame frame;
    private static Arena arena;
    static int movesCount = 0;
    private static long playerID;
    private static int scenario_num;
    static long dt = 100;

    public static void main(String[] args) {
        int flag = 0;
        try {
            String id = args[0];
            String level = args[1];
            playerID = Long.parseLong(id);
            scenario_num = Integer.parseInt(level);
            flag = 1;
        }catch(Exception e) {
        }
        if(flag == 0) {
            login();
        }
        Thread client = new Thread(new Ex2());
        client.start();
    }

    /** login function, asks for the entry of id and the level number.
     */
    private static void login() {

        try {
            String id = JOptionPane.showInputDialog("Enter your ID", "ID");
            String level = JOptionPane.showInputDialog("Enter level number", "Level");

            playerID = Long.parseLong(id);
            scenario_num = Integer.parseInt(level);

            if (scenario_num < 0)
                throw new RuntimeException();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new MyFrame(), "Invalid input.\n please insert a positive level.\n" +
                            " Press OK to enter default level (level number: 0)", "Error!", JOptionPane.ERROR_MESSAGE);
            scenario_num = 0;
        }
    }

    /**
     * This method loads a graph from a given json file.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param json - file name of JSON file
     * @return - iff the graph was successfully loaded it return the loaded graph.
     */
    private static directed_weighted_graph loadGraph(String json) {
        directed_weighted_graph gameGraph = new DWGraph_DS();
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONArray jsonNodes = line.getJSONArray("Nodes");
            for (int i = 0; i < jsonNodes.length(); i++) {
                JSONObject x = jsonNodes.getJSONObject(i);
                String[] loc = x.getString("pos").split(",");
                node_data n = new NodeData(new Double(loc[0]), new Double(loc[1]), new Double(loc[2]), x.getInt("id"));
                gameGraph.addNode(n);
            }
            JSONArray jsonEdges = line.getJSONArray("Edges");
            for (int i = 0; i < jsonEdges.length(); i++) {
                JSONObject x = jsonEdges.getJSONObject(i);
                int src = x.getInt("src");
                int dest = x.getInt("dest");
                double w = x.getDouble("w");
                gameGraph.connect(src, dest, w);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gameGraph;
    }

    @Override
    public void run() {
        int id = 313589038;

        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        game.login(id);

        String graphInfo = game.getGraph();
        dw_graph_algorithms graphAlgo = new DWGraph_Algo();
        directed_weighted_graph gameGraph = loadGraph(graphInfo);
        graphAlgo.init(gameGraph);

        String pks = game.getPokemons();
        System.out.println(graphInfo);
        System.out.println(game.getPokemons());

        init(game);
        game.startGame();

        frame.setTitle("Ex2 - OOP ,Level number: " + scenario_num);
        int ind = 0;
        dt = 100;

        while (game.isRunning()) {
            movesCount++;
            moveAgents(game, gameGraph);
            try {
                frame.repaint();
                //frame.update(arena);
                frame.setMoves(movesCount);
                long time = TimeUnit.MILLISECONDS.toSeconds(game.timeToEnd());
                frame.setTime(time);
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /** initialize the game.
     * @param game
     */
    private void init(game_service game) {

        String graphInfo = game.getGraph();
        dw_graph_algorithms graph = new DWGraph_Algo();
        directed_weighted_graph gameGraph = loadGraph(graphInfo);
        graph.init(gameGraph);
        String graphPoke = game.getPokemons();

        arena = new Arena();
        arena.setGraph(graph.getGraph());
        arena.setAlgoGraph(graph);
        arena.setPokemons(arena.json2Pokemons(graphPoke));
        frame = new MyFrame();
        frame.setSize(1000, 700);
        frame.update(arena);
        frame.show();

        List<CL_Pokemon> pokList = arena.getPokemons();
        PriorityQueue<CL_Pokemon> pokeQue = new PriorityQueue<>();

        for (CL_Pokemon cur : pokList) {
            arena.updateEdge(cur, graph.getGraph());
            pokeQue.add(cur);
        }
        arena.setQue(pokeQue);

        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject text_jason = line.getJSONObject("GameServer");
            int agentsNum = text_jason.getInt("agents");
            ArrayList<CL_Pokemon> pokeArr = Arena.json2Pokemons(game.getPokemons());

            for (int i = 0; i < pokeArr.size(); i++) {
                Arena.updateEdge(pokeArr.get(i), gameGraph);
            }
            for (int i = 0; i < agentsNum; i++) {
                int ind = i % pokeArr.size();
                CL_Pokemon poke = pokeArr.get(ind);
                int dest = poke.get_edge().getDest();
                if (poke.getType() < 0) {
                    dest = poke.get_edge().getSrc();
                }
                game.addAgent(dest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** moving all agents at the same time using the nextNode function.
     * @param game
     * @param gg
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> agentsList = arena.getAgents(lg, gg);
        arena.setAgents(agentsList);

        String fs = game.getPokemons();
        List<CL_Pokemon> pokemonList = arena.json2Pokemons(fs);
        arena.setPokemons(pokemonList);

        for (int i = 0; i < agentsList.size(); i++) {
            CL_Agent agent = agentsList.get(i);
            int id = agent.getID();
            int dest = agent.getNextNode();
            int src = agent.getSrcNode();
            double v = agent.getValue();


            if (dest == -1) {
                dest = nextNode(gg, src, agent);
                //added for *****update******
                for (CL_Agent a : agentsList) {
                    if (a!=agent) {
                        if(a.get_agent_dest() == dest){
                            //dest = secAgentNextNode(gg, src, agent);
                            a.setNextNode(secAgentNextNode(gg, src, agent));
                        }
                    }
                }
                agent.setCurrNode(dest);
                game.chooseNextEdge(agent.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    /** setting the next node using a given agent,graph and source node.
     * This method iterate all the pokemons in the graph and checking which one is
     * more close to the agent, and then checks the shortest path to the closest pokemon
     * and return the next node.
     * @param g
     * @param src
     * @param agent
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src, CL_Agent agent) {
        dw_graph_algorithms gAlgo = new DWGraph_Algo();
        gAlgo.init(g);

        LinkedList<node_data> path = new LinkedList<node_data>();

        double minDis = Double.MAX_VALUE;
        int dest = 0;


        for (CL_Pokemon i : arena.getPokemons()) {
            Arena.updateEdge(i, g);
            double dis = gAlgo.shortestPathDist(src, i.get_edge().getDest());
            if (src == i.get_edge().getDest()) {
                dis = gAlgo.shortestPathDist(src, i.get_edge().getSrc());
                minDis = dis;
                dest = i.get_edge().getSrc();
            }
            if (dis < minDis) {
                minDis = dis;
                dest = i.get_edge().getDest();
                agent.set_curr_fruit(i);
            }
        }
        int ret = dest;

        path = (LinkedList<node_data>) gAlgo.shortestPath(src, dest);
        agent.setCurPath(path);
        if (!path.isEmpty()) {
            path.remove(0).getKey();
            if (!path.isEmpty()) {
                ret = path.remove(0).getKey();
            }
        }
        return ret;
    }

    /**Setting the next node using a given agent,graph and source node.
     * If there is more the one agent, this method is giving the second agent
     * a different next node, so they will not go in the same direction.
     * @param g
     * @param src
     * @param agent
     * @return
     */
        private static int secAgentNextNode (directed_weighted_graph g,int src, CL_Agent agent){
            dw_graph_algorithms gAlgo = new DWGraph_Algo();
            gAlgo.init(g);

            LinkedList<node_data> path = new LinkedList<node_data>();

            double minDis = Double.MAX_VALUE;
            int dest = 0;
            int secAgentDest = 0;

            for (CL_Pokemon i : arena.getPokemons()) {
                    Arena.updateEdge(i, g);
                    double dis = gAlgo.shortestPathDist(src, i.get_edge().getDest());
                    if (src == i.get_edge().getDest()) {
                        dis = gAlgo.shortestPathDist(src, i.get_edge().getSrc());
                        minDis = dis;
                        dest = i.get_edge().getSrc();
                        return dest;
                    }

                    if (dis < minDis) {
                        minDis = dis;
                        dest = i.get_edge().getDest();
                    }
                    if (dis > minDis) {
                        secAgentDest = i.get_edge().getDest();
                        dest = secAgentDest;
                        agent.set_curr_fruit(i);

                    }

                }
                int ret = dest;

                path = (LinkedList<node_data>) gAlgo.shortestPath(src, dest);
                agent.setCurPath(path); // ********update********
                if (!path.isEmpty()) {
                    path.remove(0).getKey();
                    if (!path.isEmpty()) {
                        ret = path.remove(0).getKey();
                    }
                }
            try {
                Thread.sleep(dt);
            } catch (Exception e) {
                e.printStackTrace();
            }
                return ret;
            }
        }