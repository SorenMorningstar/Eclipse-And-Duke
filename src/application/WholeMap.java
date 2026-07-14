package application;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.TextAlignment;

public class WholeMap extends Pane {
    public String[] level;
    public Tile portalInTile;
    public Tile portalOutTile;
    
    private Tile[][] tileMap;
    private final Font pixelFont;
    private GameTimer gameTimer;
    private Eclipse eclipse; // initialize characters
    private Duke duke;

    public WholeMap(String[] level) {
        this.level = level;
        tileMap = new Tile[level.length][level[0].length()];
        this.setFocusTraversable(true); // allow keyboard input 

        Tile.allTiles.clear(); // clear all previous tiles 

        pixelFont = Font.loadFont(getClass().getResourceAsStream("PixelifySans.ttf"), 40);

        Image bg = new Image(WholeMap.class.getResource("BackGround1.jpg").toExternalForm()); // set background img
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(900);
        bgView.setFitHeight(600);
        getChildren().add(bgView);

        for (int y = 0; y < level.length; y++) { // add tile based on level 
            for (int x = 0; x < level[y].length(); x++) {
                char symbol = level[y].charAt(x);
                Tile tile = new Tile(symbol, x, y);
                if (symbol == '*') portalInTile = tile;
                if (symbol == '+') portalOutTile = tile;
                tileMap[y][x] = tile;
                getChildren().add(tile);
            }
        }
        
        Image gearImg = new Image(getClass().getResource("Setting_Icon.png").toExternalForm()); // pause menu 
        ImageView gearView = new ImageView(gearImg);
        gearView.setFitWidth(70);
        gearView.setFitHeight(60);
        gearView.setTranslateX(820);
        gearView.setTranslateY(10);
        gearView.setCursor(javafx.scene.Cursor.HAND);

        gearView.setOnMouseClicked(e -> {
            Main.isPaused = true;
            showPauseMenu();
        });

        getChildren().add(gearView);

    }

    public Tile getTileAt(int col, int row) { // return tiles at location 
        if (row >= 0 && row < tileMap.length && col >= 0 && col < tileMap[0].length) {
            return tileMap[row][col];
        }
        return null;
    }

    public double getMapWidth() { // map dimensions 
        return level[0].length() * Tile.TILE_SIZE;
    }

    public double getMapHeight() {
        return level.length * Tile.TILE_SIZE;
    }

    public void setPlayers(Eclipse eclipse, Duke duke) {
        this.eclipse = eclipse;
        this.duke = duke;
    }

    public void setTimer(GameTimer timer) {
        this.gameTimer = timer;
    }

    public GameTimer getTimer() {
        return gameTimer;
    }


    public boolean checkWinCondition() { // check if both characters have reached door
        return eclipse != null && duke != null &&
               eclipse.hasReachedDoor() && duke.hasReachedDoor();
    }

    public void showWinMessage(Runnable onNextLevel, int totalSeconds) { // win message 
        MusicManager.playSoundEffect("victory.mp3");

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        Label timeLabel = new Label(String.format("Time Used: %d min %02d sec", minutes, seconds));
        timeLabel.setFont(Font.font(pixelFont.getFamily(), 24));
        timeLabel.setStyle("-fx-text-fill: white;");

        
        VBox winBox = new VBox(20);
        winBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); -fx-padding: 30px; -fx-alignment: center;");
        winBox.setPrefWidth(400);
        winBox.setLayoutX(250);
        winBox.setLayoutY(180);

        Label winLabel = new Label("You Win!");
        winLabel.setFont(pixelFont);
        winLabel.setStyle("-fx-text-fill: White;");

        Button nextBtn = new Button("Next Level");
        nextBtn.setFont(Font.font(pixelFont.getFamily(), 18));
        nextBtn.setOnAction(e -> onNextLevel.run());

        Button menuBtn = new Button("Back to Main Menu");
        menuBtn.setFont(Font.font(pixelFont.getFamily(), 18));
        menuBtn.setOnAction(e -> Main.staticMainMenu.run());

        winBox.getChildren().addAll(winLabel, timeLabel, nextBtn, menuBtn);
        getChildren().add(winBox);
    }

    public void showInGameDeathDialog(String message, Runnable onRestart, Runnable onMainMenu) { // death message 
    	MusicManager.playSoundEffect("fail.mp3");

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        overlay.setPrefSize(getWidth(), getHeight());

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("PixelifySans.ttf"), 20);

        VBox dialog = new VBox(15);
        dialog.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 15; -fx-background-radius: 10;");
        dialog.setMaxWidth(250);
        dialog.setMaxHeight(160);
        dialog.setAlignment(Pos.CENTER);

        Label msg = new Label(message);
        msg.setFont(pixelFont);
        msg.setStyle("-fx-text-fill: white;");
        msg.setWrapText(true);
        msg.setMaxWidth(230);
        msg.setTextAlignment(TextAlignment.CENTER); 
        msg.setAlignment(Pos.CENTER);

        Button restartBtn = new Button("Play Again");
        Button menuBtn = new Button("Main Menu");

        restartBtn.setFont(pixelFont);
        menuBtn.setFont(pixelFont);

        restartBtn.setOnAction(e -> {
            getChildren().remove(overlay);
            onRestart.run();
        });

        menuBtn.setOnAction(e -> {
            getChildren().remove(overlay);
            onMainMenu.run();
        });

        dialog.getChildren().addAll(msg, restartBtn, menuBtn);

        overlay.getChildren().add(dialog);
        StackPane.setAlignment(dialog, Pos.CENTER);

        getChildren().add(overlay);
    }

    public static Pane wrapBG(Node content, String bgImageFile) { // set background img
        StackPane root = new StackPane();

        BackgroundImage bg = new BackgroundImage(
            new Image(Main.class.getResourceAsStream(bgImageFile)),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, true)
        );

        root.setBackground(new Background(bg));
        root.getChildren().add(content);
        return root;
    }
    
    public void showPauseMenu() { // ingame pause menu 
        VBox menu = new VBox(15);
        menu.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-padding: 20;");
        menu.setAlignment(Pos.CENTER);
        menu.setMaxWidth(500);
        menu.setTranslateX(325);
        menu.setTranslateY(180);

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("PixelifySans.ttf"), 24);
        Font emojiFont = Font.font("Segoe UI Emoji", 18);

        Label pauseTitle = new Label("⏸ PAUSE");
        pauseTitle.setFont(Font.font(pixelFont.getFamily(), 30));
        pauseTitle.setTextFill(Color.WHITE);
        pauseTitle.setEffect(new DropShadow(4, Color.BLACK)); // Optional shadow

        Button resumeBtn = createStyledButton("▶", "Continue", emojiFont, pixelFont);
        Button mainMenuBtn = createStyledButton("🏠", "Back To Main Page", emojiFont, pixelFont);

        resumeBtn.setOnAction(e -> {
            getChildren().remove(menu);
            Main.isPaused = false;
        });

        mainMenuBtn.setOnAction(e -> {
            Main.isPaused = false; 
            getChildren().remove(menu);
            Main.staticMainMenu.run();
        });

        menu.getChildren().addAll(pauseTitle, resumeBtn, mainMenuBtn);
        getChildren().add(menu);
    }

    // custom button styling 
    private Button createStyledButton(String emoji, String label, Font emojiFont, Font pixelFont) {
        Text emojiText = new Text(emoji + " ");
        emojiText.setFont(emojiFont);
        emojiText.setFill(Color.WHITE);

        Text labelText = new Text(label);
        labelText.setFont(pixelFont);
        labelText.setFill(Color.WHITE);

        TextFlow flow = new TextFlow(emojiText, labelText);
        flow.setLineSpacing(0);
        flow.setPrefHeight(Region.USE_COMPUTED_SIZE);

        Button button = new Button();
        button.setGraphic(flow);
        button.setMaxHeight(35); 
        button.setMinHeight(35);

        button.setStyle(
            "-fx-background-color: #3E3EFC;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 15;" +
            "-fx-padding: 4 16 4 16;"
        );
        return button;
    }
}