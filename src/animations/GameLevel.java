package animations;
import java.util.ArrayList;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Velocity;
import levels.Formation;
import levels.Invaders;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import sprites.Ball;
import sprites.Block;
import sprites.Collidable;
import sprites.Counter;
import sprites.GameEnvironment;
import sprites.LevelIndicator;
import sprites.LivesIndicator;
import sprites.Paddle;
import sprites.ScoreIndicator;
import sprites.Sprite;
import sprites.SpriteCollection;
/**
 * @author David Abramov.
 * Implementation of the game class.
 */
public class GameLevel implements Animation {
    private boolean spacePressed = false;
    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private final Formation aliens;
    private final int winX = 800;
    private final int winY = 600;
    private final int borderDim = 1;
    private final int radius = 4;
    private final Counter blkCtr;
    private final Counter ballCtr;
    private final Counter score;
    private final Counter lives;
    private final BlockRemover blkRm;
    private final BlockRemover shield;
    private final BallRemover ballRm;
    private final ScoreIndicator scrInd;
    private final ScoreTrackingListener scrList;
    private final LivesIndicator lvInd;
    private final LevelIndicator levInd;
    private Paddle padRef;
    private final AnimationRunner runner;
    private boolean running;
    private final biuoop.KeyboardSensor k;
    private final int framesForCount = 3;
    private final int countFrom = 3;
    private final Invaders info;
    private int playerShotDelayer = 20;
    private int enemyShotDelayer = 30;
    private final ArrayList<Ball> shots = new ArrayList<Ball>();
    private final double xRlim = winX - borderDim - 40;
    private final double xLlim = 1;
    private final int maxY = 500;
    private final int origSpeed;
    /**
     * constructor for GameLevel object.
     * @param r **AnimationRunner**
     * @param i **Invaders**
     * @param keyboard **KeyboardSensor**
     * @param l **lives Counter**
     * @param s **Score Counter**
     */
    public GameLevel(AnimationRunner r, Invaders i, biuoop.KeyboardSensor keyboard,
            Counter l, Counter s) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.origSpeed = i.getSpeed();
        this.aliens = new Formation(this.origSpeed);
        this.aliens.build();
        this.info = i;
        this.blkCtr = new Counter(this.info.numberOfBlocksToRemove());
        this.ballCtr = new Counter(0);
        this.score = s;
        this.lives = l;
        this.blkRm = new BlockRemover(this, this.blkCtr);
        this.shield = new BlockRemover(this, new Counter(0));
        this.ballRm = new BallRemover(this, this.ballCtr);
        this.scrInd = new ScoreIndicator(this.score);
        this.scrList = new ScoreTrackingListener(this.score);
        this.lvInd = new LivesIndicator(this.lives);
        this.levInd = new LevelIndicator(this.info.levelName());
        this.padRef = null;
        this.runner = r;
        this.k = keyboard;
    }
    /**
     * adds Collidable inputed object to the Collidables collection.
     * @param c **Collidable to add**
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }
    /**
     * removes Collidable inputed object from the Collidables collection.
     * @param c **Collidable to remove**
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }
    /**
     * adds Sprite inputed object to the sprites collection.
     * @param s **Sprite to add**
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }
    /**
     * removes Sprite inputed object from the sprites collection.
     * @param s **Sprite to remove**
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }
    /**
     * removes Alien.
     * @param b **Alien Block**
     */
    public void removeAlien(Block b) {
        this.aliens.removeAlien(b);
    }
    /**
     * initializes the game class and all of its objects.
     */
    public void initialize() {   //creation of border Blocks, -1 indicates no text to write.
        this.info.getBackground().addToGame(this);
        Block up = new Block(new Point(0, 0), winX, borderDim * 34, java.awt.Color.BLACK, radius, -2);
        up.addHitListener(this.ballRm);
        up.addClr(-1, java.awt.Color.GRAY);
        up.addToGame(this);
        Block left = new Block(new Point(0, 0), borderDim, winY, java.awt.Color.BLACK, radius, -2);
        left.addHitListener(this.ballRm);
        left.addClr(-1, java.awt.Color.GRAY);
        left.addToGame(this); //-2 indicates "death" Block.
        Block down = new Block(new Point(0, winY - borderDim), winX, borderDim, java.awt.Color.BLACK, radius, -2);
        down.addClr(-2, java.awt.Color.GRAY);
        down.addHitListener(this.ballRm);
        down.addToGame(this);
        Block right = new Block(new Point(winX - borderDim, 0), borderDim, winY, java.awt.Color.BLACK, radius, -2);
        right.addHitListener(this.ballRm);
        right.addClr(-1, java.awt.Color.GRAY);
        right.addToGame(this);
        ArrayList<Block> blocks = this.info.blocks();
        int numBlocks = blocks.size();
        for (int i = 0; i < numBlocks; i++) {
            blocks.get(i).addHitListener(this.shield);
            blocks.get(i).addHitListener(this.ballRm);
            blocks.get(i).addToGame(this);
        }
        ArrayList<Block> aliensList = this.aliens.getAliens();
        int numAliens = aliensList.size();
        for (int i = 0; i < numAliens; i++) {
            aliensList.get(i).addHitListener(this.blkRm);
            aliensList.get(i).addHitListener(this.scrList);
            aliensList.get(i).addHitListener(this.ballRm);
            aliensList.get(i).addToGame(this);
        }
        this.scrInd.addToGame(this);
        this.lvInd.addToGame(this);
        this.levInd.addToGame(this);
    }
    /**
     * generates Balls and Paddle.
     */
    private void genBallsPaddle() {
        int speed = this.info.paddleSpeed();
        int padWidth = this.info.paddleWidth();
        Point padStart = new Point(winX / 2 - (padWidth / 2), winY - radius * 3 - borderDim);
        Paddle p = new Paddle(padStart, padWidth, radius * 2, java.awt.Color.ORANGE, radius, this.k, speed);
        this.padRef = p;
        p.addToGame(this);
    }
    /**
     * runs one turn of the game.
     */
    public void playOneTurn() {
        while (this.lives.getValue() != 0 && this.blkCtr.getValue() != 0) {
            this.genBallsPaddle();
            this.running = true;
            int fps = this.runner.getFps();
            CountdownAnimation countDown = new CountdownAnimation(framesForCount, countFrom, this.sprites);
            this.runner.setFramesPerSecond(countDown.getSec());
            this.runner.run(countDown);
            this.runner.setFramesPerSecond(fps);
            this.runner.run(this);
        }
    }
    /**
     * puts one frame on surface.
     * @param d **surface**
     * @param dt **change in frames per small time unit**
     */
    public void doOneFrame(DrawSurface d, double dt) {
        if (this.playerShotDelayer > 0) {
            this.playerShotDelayer--;
        }
        if (this.enemyShotDelayer > 0) {
            this.enemyShotDelayer--;
        }
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        if (this.k.isPressed("p") || this.k.isPressed("P")) {
            this.runner.run(new KeyPressStoppableAnimation(this.k, "c", new PauseScreen()));
         }
        if (this.blkCtr.getValue() == 0) {
            this.running = false;
        }
        if (this.padRef.getHit()) {
            this.running = false;
            this.lives.decrease(1);
            int size = this.shots.size();
            for (int i = 0; i < size; i++) {
                this.shots.get(i).removeFromGame(this);
            }
            this.shots.clear();
            this.padRef.removeFromGame(this);
            this.aliens.resetPostions(this.origSpeed);
        }
        if (this.k.isPressed(KeyboardSensor.SPACE_KEY) && this.playerShotDelayer == 0) {
            if (!this.spacePressed) {
                Point p = this.padRef.getPadCenter();
                Point shot = new Point(p.getX(), p.getY() - 10);
                Ball b = new Ball(shot, radius, java.awt.Color.WHITE, this.environment);
                b.setVelocity(Velocity.fromAngleAndSpeed(90, 480));
                b.addToGame(this);
                this.shots.add(b);
                this.spacePressed = true;
                this.playerShotDelayer = 20;
            } else {
                this.spacePressed = false;
            }
        }
        if (this.enemyShotDelayer == 0) {
            Ball b = this.aliens.randShot(this.environment);
            b.addToGame(this);
            this.shots.add(b);
            this.enemyShotDelayer = 30;
        }
        if (!this.aliens.isEmpty()) {
            double alienSpeed = this.aliens.getAliens().get(0).getSpeed();
            double rightMost = this.aliens.getRightMost();
            double leftMost = this.aliens.getLeftMost();
            double downMost = this.aliens.getDownMost();
            if (downMost >= maxY) {
                this.running = false;
                this.lives.decrease(1);
                int size = this.shots.size();
                for (int i = 0; i < size; i++) {
                    this.shots.get(i).removeFromGame(this);
                }
                this.shots.clear();
                this.padRef.removeFromGame(this);
                this.aliens.resetPostions(this.origSpeed);
            }
            if (rightMost >= xRlim || leftMost <= xLlim) {
                alienSpeed = -1 * alienSpeed;
                alienSpeed = 1.1 * alienSpeed;
                this.aliens.setSpeed(alienSpeed);
                this.aliens.moveAllDown();
            }
        }
    }
    /**
     * stops Animation.
     * @return **boolean**
     */
    public boolean shouldStop() {
        return !this.running;
    }
    /**
     * returns number of Blocks in current Level.
     * @return **integer**
     */
    public int getNumBlk() {
        return this.blkCtr.getValue();
    }
}