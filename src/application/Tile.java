package application;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.animation.Timeline;
import javafx.animation.RotateTransition;

public class Tile extends Pane { // tile properties 
    public static final int TILE_SIZE = 30;
    public boolean isSolid = false;
    public boolean isHazard = false;
    public boolean hasKey = false;
    public boolean isBounce = false;
    public boolean isPortalIn = false;
    public boolean isPortalOut = false;
    private boolean passable = false;

    public ImageView keyImage = null;

    public char type;

    public static boolean barriersHidden = false; // static global lists for interaction
    public static List<Tile> barrierList = new ArrayList<>();
    public static List<Tile> allTiles = new ArrayList<>();
    public static List<Tile> buttonList = new ArrayList<>();

    public boolean opened = false;

    // img assets 
    private static final Image GLOW_IMG = new Image(Tile.class.getResource("tiles.png").toExternalForm());
    private static final Image WATER_IMG = new Image(Tile.class.getResource("water.png").toExternalForm());
    private static final Image LAVA_IMG = new Image(Tile.class.getResource("lava.png").toExternalForm());
    private static final Image BLUE_DOOR_IMG = new Image(Tile.class.getResource("Eclipse_Door.jpg").toExternalForm());
    public static final Image RED_DOOR_IMG = new Image(Tile.class.getResource("Duke_Door.png").toExternalForm());
    private static final Image BUTTON_IMG = new Image(Tile.class.getResource("button.png").toExternalForm());
    private static final Image BARRIER_IMG = new Image(Tile.class.getResource("LaserBeam.png").toExternalForm());
    private static final Image SPIKE_IMG = new Image(Tile.class.getResource("blade_spike.png").toExternalForm());
    private static final Image POISON_IMG = new Image(Tile.class.getResource("Green_Poison.png").toExternalForm());
    private static final Image KEY_IMG = new Image(Tile.class.getResource("key_2.png").toExternalForm());
    private static final Image PORTAL_IN_IMG = new Image(Tile.class.getResource("swirl_portal.png").toExternalForm());
    private static final Image PORTAL_OUT_IMG = new Image(Tile.class.getResource("outportal.png").toExternalForm());
    private static final Image SPRING_IMG = new Image(Tile.class.getResource("spring.png").toExternalForm());

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public char getTileType() {
        return type;
    }

    public Tile(char type, int x, int y) { // tile types and unique properties 
        this.type = type;
        ImageView tileView = new ImageView();
        tileView.setFitWidth(TILE_SIZE);
        tileView.setFitHeight(TILE_SIZE);

        switch (type) {
            case '#': // solid block 
                isSolid = true;
                tileView.setImage(GLOW_IMG);
                tileView.setOpacity(0.80);
                getChildren().add(tileView);
                break;
            case '.': // empty 
                break;
            case '/': // invisible solid block 
                isSolid = true; 
                setOpacity(0);  
                break;
            case 'W': // water
                isHazard = true;
                tileView.setImage(WATER_IMG);
                tileView.setOpacity(0.55);
                getChildren().add(tileView);
                break;
            case 'L': // lava 
                isHazard = true;
                tileView.setImage(LAVA_IMG);
                tileView.setOpacity(0.55);
                getChildren().add(tileView);
                break;
            case 'X': // eclipse's door 
                ImageView doorX = new ImageView(BLUE_DOOR_IMG);
                doorX.setFitWidth(60);
                doorX.setFitHeight(70);
                doorX.setTranslateY(-40);
                getChildren().add(doorX);
                break;
            case 'Y': // duke's door 
                ImageView doorY = new ImageView(RED_DOOR_IMG);
                doorY.setFitWidth(60);
                doorY.setFitHeight(70);
                doorY.setTranslateY(-40);
                getChildren().add(doorY);
                break;
            case 'B': // button to toggle barrier 
                ImageView btn = new ImageView(BUTTON_IMG);
                btn.setFitWidth(30);
                btn.setFitHeight(15);
                btn.setTranslateY(16);
                getChildren().add(btn);
                buttonList.add(this); 
                break;
            case 'Z': // barrier 
                isSolid = true;
                passable = false;
                tileView.setImage(BARRIER_IMG); 
                FadeTransition fadeTransition = new FadeTransition(); // pulse fade animation
                fadeTransition.setNode(tileView);
                fadeTransition.setFromValue(0.5); // initial opacity (dimmed)
                fadeTransition.setToValue(1.0); // max opacity 
                fadeTransition.setCycleCount(Timeline.INDEFINITE); // loop
                fadeTransition.setAutoReverse(true); // reverse the effect to make it pulse
                fadeTransition.setDuration(Duration.seconds(1)); 
                fadeTransition.play(); // fade animation
                getChildren().add(tileView);
                barrierList.add(this);
                break;
            case 'P': // poison 
                isHazard = true;
                tileView.setImage(POISON_IMG);
                tileView.setOpacity(1.0);
                getChildren().add(tileView);
                break;
            case 'S': // spike/blade
                isHazard = true;
                ImageView spike = new ImageView(SPIKE_IMG);
                spike.setFitWidth(30);
                spike.setFitHeight(30);
                spike.setLayoutX((Tile.TILE_SIZE - spike.getFitWidth()) / 2);
                spike.setLayoutY((Tile.TILE_SIZE - spike.getFitHeight()) / 2);

                getChildren().add(spike);

                // move spike up and down 
                TranslateTransition move = new TranslateTransition();
                move.setNode(this);
                move.setDuration(Duration.seconds(3));
                move.setByY(120);
                move.setAutoReverse(true);
                move.setCycleCount(TranslateTransition.INDEFINITE);
                move.play();
                
                RotateTransition rotate = new RotateTransition(); // rotate spike animation 
                rotate.setNode(spike);
                rotate.setDuration(Duration.seconds(0.5)); // adjust speed
                rotate.setByAngle(360);
                rotate.setCycleCount(RotateTransition.INDEFINITE);
                rotate.play();
                break;
            case '^': // spring
            	isBounce = true;
                ImageView spring = new ImageView(SPRING_IMG);
                spring.setFitWidth(30);
                spring.setFitHeight(30);
                getChildren().add(spring);
                break;
            case 'K': // key 
                hasKey = true;
                keyImage = new ImageView(KEY_IMG);
                keyImage.setFitWidth(30);
                keyImage.setFitHeight(15);
                keyImage.setTranslateY(0);
                getChildren().add(keyImage);
                break;
            case '*': // in portal 
                isPortalIn = true;
                type = '*';
                ImageView portalIn = new ImageView(PORTAL_IN_IMG);
                portalIn.setFitWidth(30);
                portalIn.setFitHeight(60);
                getChildren().add(portalIn);
                Tile.allTiles.add(this); 
                break;
            case '+': // out portal 
                isPortalOut = true;
                type = '+';
                ImageView portalOut = new ImageView(PORTAL_OUT_IMG);
                portalOut.setFitWidth(30);
                portalOut.setFitHeight(60);
                getChildren().add(portalOut);
                Tile.allTiles.add(this);
                break;
        }
        setTranslateX(x * TILE_SIZE); // tile location 
        setTranslateY(y * TILE_SIZE);
        allTiles.add(this);
    }

    public void hideBarrier() { // hide barrier handler 
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), this);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> setMouseTransparent(true));
        fadeOut.play();
        isSolid = false; // no collision 
    }

    public void showBarrier() { // show barrier handler 
        setMouseTransparent(false);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), this);
        fadeIn.setToValue(1);
        fadeIn.play();
        isSolid = true;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isHazard() {
        return isHazard;
    }
    
    public char getType() {
        return type;
    }

    // check if character is touching specific tiles 
    public static boolean isTouching(ImageView character, char tileType) {
        Bounds charBounds = character.getBoundsInParent();

        Bounds slimBounds = new BoundingBox( // slightly shrink character collision bounds
            charBounds.getMinX() + 5,    // tighter left
            charBounds.getMinY() + 5,    // tighter top
            charBounds.getWidth() - 10,  // reduce width
            charBounds.getHeight() - 10  // reduce height
        );

        for (Tile tile : allTiles) {
            if (tile.getType() == tileType &&
                tile.getBoundsInParent().intersects(slimBounds)) {
                return true;
            }
        }
        return false;
    }

    // check buttons and handlers barrier hide/show 
    public static void checkButtons(List<ImageView> characters) {
        boolean anyButtonTouched = false;

        for (Tile button : buttonList) {
            for (ImageView iv : characters) {
                if (iv.getBoundsInParent().intersects(button.getBoundsInParent())) {
                    anyButtonTouched = true;
                    break; // no need to check more characters for this button
                }
            }
            if (anyButtonTouched) break;
        }

        if (anyButtonTouched && !barriersHidden) {
            for (Tile barrier : barrierList) {
                barrier.hideBarrier();
            }
            barriersHidden = true;
        } else if (!anyButtonTouched && barriersHidden) {
            for (Tile barrier : barrierList) {
                barrier.showBarrier();
            }
            barriersHidden = false;
        }
    }
    
    public void removeKeyImage() { // remove tile img 
        if (keyImage != null) {
            getChildren().remove(keyImage);
            keyImage = null;
        }
    }
    
    public static Tile findPortalOut() {
        for (Tile t : allTiles) {
            if (t.type == '+') {
                return t;
            }
        }
        return null;
    }
}