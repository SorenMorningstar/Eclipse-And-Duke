package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LevelManager {
	
    private static Main mainApp;
    private static GameManager gameManager;
    public static void init(GameManager gm) { // initialize gamemanager so it can start levels 
        gameManager = gm;
    }

    public static final String[] level1 = { // level designs using tiles 
        "//////////////////////////////",
        "/............................/",
        "/............................/",
        "/............................/",
        "/..........#.#..#............/",
        "/........#.#.#..#..#..X..Y.../",
        "/...##########.###############",
        "/............................/",
        "##.........................../",
        "###........................../",
        "####........................./",
        "######......................./",
        "/........#####...#########.../",
        "/............................/",
        "/...........................##",
        "/..........................###",
        "/.........................####",
        "/.........................####",
        "######WWW####LLL#####PPP######",
        "##############################"
    };

    public static final String[] level2 = {
        "//////////////////////////////",
        "/............................/",
        "/............................/",
        "/............................/",
        "/...Y..X....SSS............../",
        "#########################..../",
        "/.............Z............../",
        "/.............Z.............##",
        "/.............Z............###",
        "/.............Z...........####",
        "/........B....Z.....B....#####",
        "/....#########################",
        "##.........................../",
        "###........................../",
        "####......LLL.....WWW......../",
        "#####.....###.....###......../",
        "######......................./",
        "#######....................../",
        "##########WWW#####LLL#########",
        "##############################"
    };

    public static final String[] level3 = {
        "//////////////////////////////",
        "/............................/",
        "/............................/",
        "/......................X..Y../",
        "/.*..............#############",
        "/...K......................../",
        "/.######...................../",
        "/............................/",
        "/......................+...../",
        "/..............^^............/",
        "/.............###########..../",
        "/............................/",
        "/............................/",
        "/............................/",
        "/............................/",
        "/............................/",
        "/............................/",
        "/.......^^.................../",
        "############WWW####LLL########",
        "##############################"
    };

    public static void showLevelSelection(Stage stage) { // level selector 
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        Font pixelFont = Font.loadFont(LevelManager.class.getResourceAsStream("PixelifySans.ttf"), 40);
        Label title = new Label("Choose Your Level");
        title.setFont(pixelFont);
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));

        Button level1Btn = new Button("Level 1");
        Button level2Btn = new Button("Level 2");
        Button level3Btn = new Button("Level 3");
        Button backBtn = new Button("Back to Menu");

        // button style 
        for (Button btn : new Button[]{level1Btn, level2Btn, level3Btn, backBtn}) {
            btn.setFont(Font.font(pixelFont.getFamily(), 20));
            btn.setStyle(
                "-fx-background-color: #2196F3;" +  // Blue
                "-fx-text-fill: white;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 8 20 8 20;"
            );
        }

        level1Btn.setOnAction(e -> gameManager.startGame(stage, LevelManager.level1)); // use button to open each level 
        level2Btn.setOnAction(e -> gameManager.startGame(stage, LevelManager.level2));  
        level3Btn.setOnAction(e -> gameManager.startGame(stage, LevelManager.level3));  
        backBtn.setOnAction(e -> mainApp.staticMainMenu.run());

        layout.getChildren().addAll(title, level1Btn, level2Btn, level3Btn, backBtn); // add button to scene
        Pane rootWithBG = MenuManager.wrapBGWithVideo(layout, "menu_bg.mp4");
        stage.setScene(new Scene(rootWithBG, 900, 600));
    }

}