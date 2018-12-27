package game;
import java.io.File;
import java.io.IOException;
import animations.Animation;
import animations.AnimationRunner;
import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import animations.Menu;
import animations.MenuAnimation;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import tasks.GameTask;
import tasks.QuitTask;
import tasks.ShowHiScoresTask;
import tasks.Task;
/**
 * @author David Abramov.
 * Ass7Game class, which runs the game.
 */
public class Ass7Game {
    /**
     * generates High Scores table.
     * @return **HighScoresAnimation**
     */
    private HighScoresAnimation hiScrGen() {
        int tableSize = 10;
        HighScoresTable h = null;
        File f = new File("highscores");
        try {   //case file already exists.
            if (f.isFile()) {
                h = HighScoresTable.loadFromFile(f);
                if (h.size() > 1) {   //case table loaded successfully.
                    return new HighScoresAnimation(h);
                }
            } else {
                f.createNewFile();
                h = new HighScoresTable(tableSize);
                h.save(f);
                return new HighScoresAnimation(h);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        h = new HighScoresTable(tableSize);
        return new HighScoresAnimation(h);
    }
    /**
     * runs the Game.
     * @param g **GUI**
     * @param r **AnimationRunner**
     * @param k **KeyboardSensor**
     */
    private void gameRunner(GUI g, AnimationRunner r, KeyboardSensor k) {
        Menu<Task<Void>> menu = new MenuAnimation<Task<Void>>(k);
        menu.addSelection("q", "Quit", new QuitTask(g));
        Animation a = new KeyPressStoppableAnimation(k, KeyboardSensor.SPACE_KEY, this.hiScrGen());
        menu.addSelection("h", "High Scores", new ShowHiScoresTask(r, a));
        GameFlow game = new GameFlow(g, r, k);
        menu.addSelection("s", "Start Game", new GameTask(game));
        while (true) {
            r.run(menu);
            Task<Void> task = menu.getStatus();
            task.run();
            menu = new MenuAnimation<Task<Void>>(k);
            menu.addSelection("q", "Quit", new QuitTask(g));
            a = new KeyPressStoppableAnimation(k, KeyboardSensor.SPACE_KEY, this.hiScrGen());
            menu.addSelection("h", "High Scores", new ShowHiScoresTask(r, a));
            game = new GameFlow(g, r, k);
            menu.addSelection("s", "Start Game", new GameTask(game));
        }
    }
    /**
     * main for Ass7Game class, which creates, initializes and runs the game.
     * @param args **array of Strings inputed to main from command prompt**
     */
    public static void main(String[] args) {
        final int winX = 800;
        final int winY = 600;
        final int framesPerSecond = 60;
        GUI gui = new GUI("Space Invaders", winX, winY);
        biuoop.KeyboardSensor k = gui.getKeyboardSensor();
        AnimationRunner runner = new AnimationRunner(gui, framesPerSecond);
        Ass7Game mainRunner = new Ass7Game();
        mainRunner.gameRunner(gui, runner, k);
    }
}