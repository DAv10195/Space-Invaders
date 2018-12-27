package levels;
import java.util.ArrayList;
import java.util.Random;
import geometry.Point;
import geometry.Velocity;
import input.BlockCreator;
import input.ImageGetter;
import sprites.Ball;
import sprites.Block;
import sprites.GameEnvironment;
/**
 * @author David Abramov.
 * Formation class implementation.
 */
public class Formation {
    private final ArrayList<Block> aliens;
    private java.awt.Image img = null;
    private final Point start = new Point(35, 35);
    private final int initialSpeed;
    /**
     * constructor for Formation object.
     * @param s **Alien speed**
     */
    public Formation(int s) {
        this.aliens = new ArrayList<Block>();
        this.initialSpeed = s;
    }
    /**
     * loads image.
     */
    private void genImage() {
        ImageGetter ig = new ImageGetter();
        this.img = ig.load("enemy.png");
    }
    /**
     * add inputed Alien Block to Aliens List.
     * @param b **Block**
     */
    public void removeAlien(Block b) {
        this.aliens.remove(b);
    }
    /**
     * builds Formation of Alien Blocks.
     */
    public void build() {
        this.genImage();
        if (this.img == null) {
            throw new RuntimeException("Error");
        }
        final int shotRadius = 4;
        final int hitPts = 1;
        final int width = 40;
        final int height = 30;
        final int speed = this.initialSpeed;
        final java.awt.Image fImg = this.img;
        BlockCreator bc = new BlockCreator() {
            public Block create(int xpos, int ypos) {
                Point upperLeft = new Point((double) xpos, (double) ypos);
                Block b = new Block(upperLeft, (double) width, (double) height, null, (double) shotRadius, hitPts);
                b.addImg(-1, fImg);
                b.setSpeed(speed);
                return b;
            }
        };
        int y = (int) start.getY();
        int x = (int) start.getX();
        for (int i = 0; i < 10; i++) {
            Block b = bc.create(x, y);
            this.aliens.add(b);
            x = x + width + 10;
        }
        y = y + height + 10;
        x = (int) start.getX();
        for (int i = 0; i < 10; i++) {
            Block b = bc.create(x, y);
            this.aliens.add(b);
            x = x + width + 10;
        }
        y = y + height + 10;
        x = (int) start.getX();
        for (int i = 0; i < 10; i++) {
            Block b = bc.create(x, y);
            this.aliens.add(b);
            x = x + width + 10;
        }
        y = y + height + 10;
        x = (int) start.getX();
        for (int i = 0; i < 10; i++) {
            Block b = bc.create(x, y);
            this.aliens.add(b);
            x = x + width + 10;
        }
        y = y + height + 10;
        x = (int) start.getX();
        for (int i = 0; i < 10; i++) {
            Block b = bc.create(x, y);
            this.aliens.add(b);
            x = x + width + 10;
        }
    }
    /**
     * returns List of Alien Blocks.
     * @return **List of Blocks**
     */
    public ArrayList<Block> getAliens() {
        return this.aliens;
    }
    /**
     * gets left most x value of formation.
     * @return **double**
     */
    public double getLeftMost() {
        ArrayList<Block> l = this.getAliens();
        int size = l.size();
        double toRet = l.get(0).getUpperLeft().getX();
        for (int i = 1; i < size; i++) {
            if (l.get(i).getUpperLeft().getX() < toRet) {
                toRet = l.get(i).getUpperLeft().getX();
            }
        }
        return toRet;
    }
    /**
     * gets right most x value of formation.
     * @return **double**
     */
    public double getRightMost() {
        ArrayList<Block> l = this.getAliens();
        int size = l.size();
        double toRet = l.get(0).getUpperLeft().getX();
        for (int i = 1; i < size; i++) {
            if (l.get(i).getUpperLeft().getX() > toRet) {
                toRet = l.get(i).getUpperLeft().getX();
            }
        }
        return toRet;
    }
    /**
     * gets down most y value of formation.
     * @return **double**
     */
    public double getDownMost() {
        ArrayList<Block> l = this.getAliens();
        int size = l.size();
        double toRet = l.get(0).getUpperLeft().getY() + l.get(0).getHeight();
        for (int i = 1; i < size; i++) {
            if (l.get(i).getUpperLeft().getY() + l.get(i).getHeight() > toRet) {
                toRet = l.get(i).getUpperLeft().getY() + l.get(i).getHeight();
            }
        }
        return toRet;
    }
    /**
     * sets Aliens speed.
     * @param s **speed to set**
     */
    public void setSpeed(double s) {
        int size = this.aliens.size();
        for (int i = 0; i < size; i++) {
            this.aliens.get(i).setSpeed(s);
        }
    }
    /**
     * moves down all aliens.
     */
    public void moveAllDown() {
        int size = this.aliens.size();
        for (int i = 0; i < size; i++) {
            this.aliens.get(i).moveDown();
        }
    }
    /**
     * answers if formation is empty.
     * @return **boolean**
     */
    public boolean isEmpty() {
        return this.aliens.isEmpty();
    }
    /**
     * returns a List of bottom Alien Blocks.
     * @param env **GameEnvironment**
     * @return **Block List**
     */
    public Ball randShot(GameEnvironment env) {
        ArrayList<Block> downBlk = new ArrayList<Block>();
        double downMost = this.getDownMost();
        ArrayList<Block> l = this.getAliens();
        int size = l.size();
        for (int i = 0; i < size; i++) {
            if (l.get(i).getUpperLeft().getY() + l.get(i).getHeight() >= downMost) {
                downBlk.add(l.get(i));
            }
        }
        Random rand = new Random();
        int toShot = rand.nextInt(downBlk.size());
        Point midBottom = downBlk.get(toShot).genBottomMiddle();
        Point p = new Point(midBottom.getX(), midBottom.getY() + 10);
        Ball b = new Ball(p, 4, java.awt.Color.RED, env);
        b.setVelocity(Velocity.fromAngleAndSpeed(270, 480));
        return b;
    }
    /**
     * moves all aliens to their starting Point.
     * @param s **original speed to set**
     */
    public void resetPostions(double s) {
        int size = this.aliens.size();
        for (int i = 0; i < size; i++) {
            this.aliens.get(i).setOriginal();
            this.aliens.get(i).setSpeed(s);
        }
    }
}