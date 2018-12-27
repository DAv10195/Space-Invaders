package levels;
import java.util.ArrayList;
import java.util.List;
import animations.GameLevel;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Velocity;
import sprites.Block;
import sprites.Sprite;
/**
 * @author David Abramov.
 * Invaders class implementation.
 */
public class Invaders {
    private final int winX = 800;
    private final int winY = 600;
    private final int padSpeed = 420;
    private final int padWidth = 80;
    private final int numAliens = 50;
    private final int levelNumber;
    /**
     * constructor for Invaders object.
     * @param ln **level number**
     */
    public Invaders(int ln) {
        this.levelNumber = ln;
    }
    /**
     * no balls for invaders level.
     * @return **0**
     */
    public int numberOfBalls() {
        return 0;
    }
    /**
     * no ball velocities for invaders level.
     * @return **null**
     */
    public List<Velocity> initialBallVelocities() {
        return null;
    }
    /**
     * no ball locations for invaders level.
     * @return **null**
     */
    public List<Point> ballLocations() {
        return null;
    }
    /**
     * returns paddle speed.
     * @return **integer**
     */
    public int paddleSpeed() {
        return padSpeed;
    }
    /**
     * returns paddle width.
     * @return **integer**
     */
    public int paddleWidth() {
        return padWidth;
    }
    /**
     * returns level name.
     * @return **String**
     */
    public String levelName() {
        return "Battle no. ".concat(String.valueOf(this.levelNumber));
    }
    /**
     * returns invaders black background Sprite.
     * @return **Sprite**
     */
    public Sprite getBackground() {
        Sprite background  = new Sprite() {
            public void drawOn(DrawSurface d) {
                d.setColor(java.awt.Color.BLACK);
                d.fillRectangle(0, 0, winX, winY);
            }
            public void timePassed(double dt) {
                //to be updated...
            }
            public void addToGame(GameLevel g) {
                g.addSprite(this);
            }
        };
        return background;
    }
    /**
     * creates 3 Shields protecting the player from aliens.
     * @return **Block List**
     */
    public ArrayList<Block> blocks() {
        ArrayList<Block> toRet = new ArrayList<Block>();
        java.awt.Color c = java.awt.Color.CYAN;
        int x = 130;
        int y = 500;
        for (int i = 0; i < 30; i++) {
            Block b1 = new Block(new Point(x + i * 5, y), 5, 5, null, 4, 1);
            Block b2 = new Block(new Point(x + i * 5, y + 5), 5, 5, null, 4, 1);
            Block b3 = new Block(new Point(x + i * 5, y + 10), 5, 5, null, 4, 1);
            b1.addClr(-1, c);
            b2.addClr(-1, c);
            b3.addClr(-1, c);
            toRet.add(b1);
            toRet.add(b2);
            toRet.add(b3);
        }
        x = 330;
        y = 500;
        for (int i = 0; i < 30; i++) {
            Block b1 = new Block(new Point(x + i * 5, y), 5, 5, null, 4, 1);
            Block b2 = new Block(new Point(x + i * 5, y + 5), 5, 5, null, 4, 1);
            Block b3 = new Block(new Point(x + i * 5, y + 10), 5, 5, null, 4, 1);
            b1.addClr(-1, c);
            b2.addClr(-1, c);
            b3.addClr(-1, c);
            toRet.add(b1);
            toRet.add(b2);
            toRet.add(b3);
        }
        x = 530;
        y = 500;
        for (int i = 0; i < 30; i++) {
            Block b1 = new Block(new Point(x + i * 5, y), 5, 5, null, 4, 1);
            Block b2 = new Block(new Point(x + i * 5, y + 5), 5, 5, null, 4, 1);
            Block b3 = new Block(new Point(x + i * 5, y + 10), 5, 5, null, 4, 1);
            b1.addClr(-1, c);
            b2.addClr(-1, c);
            b3.addClr(-1, c);
            toRet.add(b1);
            toRet.add(b2);
            toRet.add(b3);
        }
        return toRet;
    }
    /**
     * returns number of aliens.
     * @return **integer**
     */
    public int numberOfBlocksToRemove() {
        return numAliens;
    }
    /**
     * gets speed of aliens depending on level number.
     * @return **integer**
     */
    public int getSpeed() {
        return this.levelNumber * 60;
    }
}