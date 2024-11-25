import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private final int spriteSheetNumberOfColumn = 10;

    private double normalSpeed = 5; 
    private double runningSpeed = 10; 
    private boolean isRunning = false;

    private double health = 100.0; 
    private final double healthDecrementAmount = 1.0; 
    private boolean isGameOver = false;
    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
        this.speed = normalSpeed; 
    }

    public void setRunning(boolean running) {
        isRunning = running;
        speed = isRunning ? runningSpeed : normalSpeed; 
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment) {
        if (isGameOver) {
            return false; // Prevent movement if game is over
        }

        Rectangle2D.Double moved = new Rectangle2D.Double();
        switch (direction) {
            case EAST:
                moved.setRect(super.getHitBox().getX() + speed, super.getHitBox().getY(),
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case WEST:
                moved.setRect(super.getHitBox().getX() - speed, super.getHitBox().getY(),
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case NORTH:
                moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() - speed,
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case SOUTH:
                moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() + speed,
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
        }

        for (Sprite s : environment) {
            if ((s instanceof SolidSprite) && (s != this)) {
                if (((SolidSprite) s).intersect(moved)) {
                    reduceHealth(); // Reduce health on collision
                    if (isGameOver) {
                        return false; // Prevent further movement if game is over
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void reduceHealth() {
        health -= healthDecrementAmount; 
        if (health < 0) health = 0; 

        if (health == 0) {
            isGameOver = true; // Set game over flag when health reaches 0
        }

        System.out.println("Health: " + health + "%"); 
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void move() {
        switch (direction) {
            case NORTH -> this.y -= speed;
            case SOUTH -> this.y += speed;
            case EAST -> this.x += speed;
            case WEST -> this.x -= speed;
        }
    }

    public void moveIfPossible(ArrayList<Sprite> environment) {
        if (!isGameOver && isMovingPossible(environment)) {
            move();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (isGameOver) {
            // Draw game over text, further left
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 500, 400);  
            return; // Do not draw the sprite if the game is over
        }

        int index = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);

        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null);

        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y - 10, (int) (width * (health / 100)), 5);
        g.setColor(Color.BLACK);
        g.drawRect((int) x, (int) y - 10, (int) width,5);
    }

}
