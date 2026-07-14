package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Eclipse extends ImageView { // eclipse details 
    private double x, y;
    private double velocityY = 0;
    private double lastMoveX = 1;
    private boolean onGround = false;
    private boolean isAlive = true;
    private boolean reachedDoor = false;
    private int animationCounter = 0;
    private boolean carryingKey = false;
    private ImageView carriedKeyImg = null;
    private boolean canPlayStep = true;

    private WholeMap map;

    //image assets
    private final Image standImage = new Image(getClass().getResource("stand_1.png").toExternalForm());
    private final Image walkLeft1 = new Image(getClass().getResource("left_1.png").toExternalForm());
    private final Image walkLeft2 = new Image(getClass().getResource("left_2.png").toExternalForm());
    private final Image walkRight1 = new Image(getClass().getResource("right_1.png").toExternalForm());
    private final Image walkRight2 = new Image(getClass().getResource("right_2.png").toExternalForm());
    private final Image standImageKey = new Image(getClass().getResource("eclipse_stand_key.png").toExternalForm());
    private final Image walkLeftKey1 = new Image(getClass().getResource("eclipse_left_key1.png").toExternalForm());
    private final Image walkLeftKey2 = new Image(getClass().getResource("eclipse_left_key2.png").toExternalForm());
    private final Image walkRightKey1 = new Image(getClass().getResource("eclipse_right_key1.png").toExternalForm());
    private final Image walkRightKey2 = new Image(getClass().getResource("eclipse_right_key2.png").toExternalForm());

    private boolean frameToggle = false; // animation and portal flags
    private boolean justTeleported = false;
    private int jumpBuffer = 0;

    public void setJustTeleported(boolean val) { // teleport flags
        this.justTeleported = val;
    }

    public boolean hasJustTeleported() {
        return this.justTeleported;
    }

    public Eclipse(double startX, double startY, WholeMap map) { // initialize eclipse into the map 
        this.x = startX;
        this.y = startY;
        this.map = map;

        setFitWidth(Tile.TILE_SIZE);
        setFitHeight(Tile.TILE_SIZE * 2);
        updateCurrentImage();
        updatePosition();
    }

    public void moveX(double dx) {
        if (!isAlive || reachedDoor) return;

        double newX = clamp(x + dx, 0, map.getMapWidth() - getFitWidth()); // prevent eclipse from going out of bounds horizontally

        int top = (int) (y / Tile.TILE_SIZE);
        int bottom = (int) ((y + getFitHeight() - 1) / Tile.TILE_SIZE);
        int left = (int) (newX / Tile.TILE_SIZE);
        int right = (int) ((newX + getFitWidth() - 1) / Tile.TILE_SIZE);

        for (int row = top; row <= bottom; row++) { // eclipse interactions with other objects 
            for (int col = left; col <= right; col++) {
                Tile tile = map.getTileAt(col, row);
                if (tile != null) {
                    if (tile.type == 'X') openEclipseDoor(tile);
                    if (tile.isSolid()) return;
                    if (tile.type == 'K' && !this.hasKey()) {
                        this.setCarryingKey(true);
                        MusicManager.playSoundEffect("pop.mp3");
                        
                    }
                }
            }
        }

        x = newX;
        updatePosition();

        animationCounter++; // eclipse walking animation toggle
        if (animationCounter >= 6) {
            frameToggle = !frameToggle;
            animationCounter = 0;
        }

        lastMoveX = dx;
        Image nextImage = getWalkImage(dx > 0, frameToggle);
        if (getImage() != nextImage) setImage(nextImage);
    }

    public void applyGravity() { // gravity and vertical collision 
        if (!isAlive || reachedDoor) return;

        velocityY += 0.38;
        double newY = y + velocityY;

        int left = (int) (x / Tile.TILE_SIZE);
        int right = (int) ((x + getFitWidth() - 1) / Tile.TILE_SIZE);

        boolean hitGround = false;
        boolean hitCeiling = false;

        if (velocityY > 0) { // handle falling 
            int bottomY = (int) ((newY + getFitHeight() - 0.1) / Tile.TILE_SIZE);

            for (int col = left; col <= right; col++) {
                Tile groundTile = map.getTileAt(col, bottomY);

                if (groundTile != null) {
                    if (groundTile.type == 'X') openEclipseDoor(groundTile);

                    if (groundTile.type == 'K' && !hasKey()) {
                        setCarryingKey(true);
                        groundTile.removeKeyImage();
                        MusicManager.playSoundEffect("pop.mp3");
                    }

                    if (groundTile.type == '^') {
                        velocityY = -18;
                        onGround = false;
                        MusicManager.playSoundEffect("jump.mp3");  

                        return; // character bounce up immediately, skip solid check
                    }

                    if (groundTile.isSolid()) hitGround = true;
                }
            }

            if (hitGround) {
                newY = bottomY * Tile.TILE_SIZE - getFitHeight();
                velocityY = 0;

                if (jumpBuffer > 0) {
                    velocityY = -8.5;
                    jumpBuffer = 0;
                    onGround = false;
                } else {
                    onGround = true;
                }
            } else {
                onGround = false;
            }

        } else if (velocityY < 0) { // jumping up 
            int topY = (int) (newY / Tile.TILE_SIZE);

            for (int col = left; col <= right; col++) {
                Tile topTile = map.getTileAt(col, topY);
                if (topTile != null && topTile.isSolid()) {
                    hitCeiling = true;
                }
            }

            if (hitCeiling) {
                newY = (topY + 1) * Tile.TILE_SIZE;
                velocityY = 0;
            }
        }

        if (newY + getFitHeight() > map.getMapHeight()) { // prevent eclipse from jumping out of bounds
            newY = map.getMapHeight() - getFitHeight();
            velocityY = 0;
            onGround = true;
        }

        if (newY < 0) newY = 0;

        y = newY;
        updatePosition();

        if (jumpBuffer > 0) jumpBuffer--;
    }

    public void jump() { // buffer jump 
        if (!isAlive || reachedDoor) return;
        jumpBuffer = 5;
    }

    public void stopMoving() { // update eclipse img after stop moving 
        if (!isAlive || reachedDoor) return;
        updateStandImage();
    }

    private void openEclipseDoor(Tile doorTile) {
        if (doorTile.opened || reachedDoor) return;
        
        if (GameManager.currentLevel.equals(LevelManager.level3) && !this.hasKey()) {
            return; // door will not open without key (level 3) 
        }

        doorTile.setMouseTransparent(true);
        doorTile.setOpacity(0.4);
        doorTile.isSolid = false;
        doorTile.opened = true;
        reachedDoor = true;

        MusicManager.playSoundEffect("unlock.mp3");
        fadeOut();

        if (map != null && map.checkWinCondition()) { // check win
            map.getTimer().stop();  
            map.showWinMessage(Main.staticNextLevel, map.getTimer().getSeconds());
        }

    }

    private void fadeOut() {
        setVisible(false);
    }

    public void die() {
        isAlive = false;
        setVisible(false);
    }

    private void updatePosition() {
        setTranslateX(x);
        setTranslateY(y);
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(val, max));
    }

    public boolean hasReachedDoor() {
        return reachedDoor;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean hasKey() {
        return carryingKey;
    }

    public void setCarryingKey(boolean hasKey) {
        this.carryingKey = hasKey;
        updateCurrentImage(); // update character to carry key
    }

    public void updateCurrentImage() {
        Image next = getWalkImage(lastMoveX > 0, false);
        setImage(next);
    }

    public Image getWalkImage(boolean right, boolean toggle) { // walk animation based on direction and toggle 
        if (hasKey()) {
            return right ? (toggle ? walkRightKey1 : walkRightKey2) : (toggle ? walkLeftKey1 : walkLeftKey2);
        } else {
            return right ? (toggle ? walkRight1 : walkRight2) : (toggle ? walkLeft1 : walkLeft2);
        }
    }

    public void updateStandImage() {
        setImage(hasKey() ? standImageKey : standImage);
    }

    public void checkTeleport() { // teleportation 
        if (GameManager.currentLevel.equals(LevelManager.level3)) {
            if (Tile.isTouching(this, '*')) {
                if (!justTeleported) {
                    Tile outPortal = Tile.findPortalOut();
                    if (outPortal != null) {
                        this.x = Tile.TILE_SIZE * outPortal.getTranslateX() / Tile.TILE_SIZE;
                        this.y = Tile.TILE_SIZE * outPortal.getTranslateY() / Tile.TILE_SIZE - 5;
                        this.updatePosition();
                        justTeleported = true;
                    }
                }
            } else {
                justTeleported = false;
            }
        }
    }
}

