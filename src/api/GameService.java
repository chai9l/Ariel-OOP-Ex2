package api;

public class GameService implements game_service{
    @Override
    public String getGraph() {
        return null;
    }

    @Override
    public String getPokemons() {
        return null;
    }

    @Override
    public String getAgents() {
        return null;
    }

    @Override
    public boolean addAgent(int start_node) {
        return false;
    }

    @Override
    public long startGame() {
        return 0;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public long stopGame() {
        return 0;
    }

    @Override
    public long chooseNextEdge(int id, int next_node) {
        return 0;
    }

    @Override
    public long timeToEnd() {
        return 0;
    }

    @Override
    public String move() {
        return null;
    }

    @Override
    public boolean login(long id) {
        return false;
    }
}
