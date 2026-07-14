package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application { // static references 
    public static Runnable staticRestart;
    public static Runnable staticMainMenu;
    public static Runnable staticNextLevel;
    public static AnimationTimer staticGameLoop;
    public static boolean isPaused = false;

    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        gameManager = new GameManager(this); // initialize GameManager
        LevelManager.init(gameManager); // initialize levels with game logic
        MusicManager.playBGM("game_retro_bgm.mp3", true); 
        MenuManager.showStartupScreen(stage); // initial menu screen 
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}