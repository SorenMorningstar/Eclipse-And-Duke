package application;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.List;

public class GameManager {

    private final Main mainApp;

    private Eclipse eclipse;
    private Duke duke;
    private WholeMap currentMap;
    private GameTimer gameTimer = new GameTimer();

    private AnimationTimer gameLoop;
    private boolean restartRequested = false;
    private boolean leftPressed, rightPressed, aPressed, dPressed;

    public static String[] currentLevel;
    public static AnimationTimer staticGameLoop;

    public GameManager(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    public void startGame(Stage stage, String[] level) { // start level 
        MusicManager.playBGM("level_theme.mp3", true); // play music 

        if (gameLoop != null) gameLoop.stop();

        currentLevel = level;
        currentMap = new WholeMap(currentLevel); 

        GameTimer timer = new GameTimer();
        currentMap.setTimer(timer);
        timer.init(currentMap); 

        if (level == LevelManager.level1) { // character spawn location 
            eclipse = new Eclipse(60, 480, currentMap);
            duke = new Duke(120, 480, currentMap);
        } else {
            eclipse = new Eclipse(700, 480, currentMap);
            duke = new Duke(760, 480, currentMap);
        }

        currentMap.getChildren().addAll(eclipse, duke); // add characters to scene
        currentMap.setPlayers(eclipse, duke);

        Scene gameScene = new Scene(currentMap, 900, 600); // set up game scene
        setKeyHandlers(gameScene, stage);
        stage.setScene(gameScene);
        stage.setTitle("Eclipse & Duke - Gameplay");

        currentMap.setFocusTraversable(true); // keyboard input 
        currentMap.requestFocus();
        currentMap.setOnMouseClicked(e -> currentMap.requestFocus());

        Main.staticRestart = () -> restartGame(stage);
        Main.staticMainMenu = () -> MenuManager.showMainMenu(stage);
        Main.staticNextLevel = () -> {
            if (currentLevel == LevelManager.level1) {
                startGame(stage, LevelManager.level2); // lvl1 -> lvl2
            } else if (currentLevel == LevelManager.level2) {
                startGame(stage, LevelManager.level3); //lvl2 -> lvl 3
            } else {
                startGame(stage, LevelManager.level1); // loop back to lvl1 after done with lvl 3
            }
        };

        restartRequested = false; // restart game 
        resetKeyStates();
        gameLoop = createGameLoop(stage);
        staticGameLoop = gameLoop;
        gameLoop.start();
    }

    private void restartGame(Stage stage) {
        startGame(stage, currentLevel);  // restart game with the current level
        gameTimer.stop();  
        gameTimer.init(currentMap);
        currentMap.requestFocus();  
    }

    private void resetKeyStates() { // reset keys 
        leftPressed = rightPressed = aPressed = dPressed = false;
    }

    private void setKeyHandlers(Scene scene, Stage stage) { // keyboard inputs 
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case LEFT -> leftPressed = true;
                case RIGHT -> rightPressed = true;
                case UP -> eclipse.jump();
                case A -> aPressed = true;
                case D -> dPressed = true;
                case W -> duke.jump();
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT -> { leftPressed = false; eclipse.stopMoving(); }
                case RIGHT -> { rightPressed = false; eclipse.stopMoving(); }
                case A -> { aPressed = false; duke.stopMoving(); }
                case D -> { dPressed = false; duke.stopMoving(); }
            }
        });
    }

    private AnimationTimer createGameLoop(Stage stage) { // game loop logic
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (Main.isPaused) return;

                eclipse.applyGravity(); // apply gravity 
                duke.applyGravity();
                Tile.checkButtons(List.of(eclipse, duke));

                if (leftPressed) eclipse.moveX(-3); // character movement
                if (rightPressed) eclipse.moveX(3);
                if (aPressed) duke.moveX(-3);
                if (dPressed) duke.moveX(3);
                
                duke.checkTeleport(); // check portal teleportation
                eclipse.checkTeleport();

                if (!restartRequested) { // collision with hazards
                    boolean poisonTouch = Tile.isTouching(eclipse, 'P') || Tile.isTouching(duke, 'P');
                    boolean spikeTouch  = Tile.isTouching(eclipse, 'S') || Tile.isTouching(duke, 'S');
                    boolean waterTouch  = Tile.isTouching(eclipse, 'W');
                    boolean lavaTouch   = Tile.isTouching(duke, 'L');

                    if (poisonTouch) { // death cases 
                        handleDeath("You were poisoned!", stage);
                    } else if (spikeTouch) {
                        handleDeath("You were impaled by spikes!", stage);
                    } else if (waterTouch) {
                        handleDeath("You drowned!", stage);
                    } else if (lavaTouch) {
                        handleDeath("You burned in lava!", stage);
                    }
                }
            }
        };
    }

    private void handleDeath(String message, Stage stage) { // death handler 
        eclipse.die();
        duke.die();
        gameTimer.stop();
        restartRequested = true;
        currentMap.showInGameDeathDialog(message, () -> restartGame(stage), () -> MenuManager.showMainMenu(stage));
    }
}
