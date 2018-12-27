package tasks;
import game.GameFlow;
/**
 * @author David Abramov.
 * GameTask class implementation.
 */
public class GameTask implements Task<Void> {
    private final GameFlow gf;
    /**
     * constructor for GameTask object.
     * @param g **GameFlow**
     */
    public GameTask(GameFlow g) {
        this.gf = g;
     }
     /**
      * runs Task.
      * @return **null**
      */
     public Void run() {
        this.gf.runLevels();
        return null;
     }
}