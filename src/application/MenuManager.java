package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

public class MenuManager {
	
	private static MediaPlayer menuMediaPlayer;
	private static MediaPlayer backgroundPlayer;
	private static MediaView backgroundView;

    public static void showStartupScreen(Stage stage) {
        Font pixelFont = Font.loadFont(MenuManager.class.getResourceAsStream("PixelifySans.ttf"), 80);

        Label title = new Label("Eclipse & Duke"); // game title style 
        title.setFont(pixelFont);
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));
        title.setTranslateY(20);

        Button startButton = new Button("▶ Start"); // start button style 
        startButton.setFont(Font.font(pixelFont.getFamily(), 28));
        startButton.setOnAction(e -> {
            Main.staticMainMenu = () -> MenuManager.showMainMenu(stage);
            Tutorial_Screen.show(stage);
        });
        startButton.setStyle("-fx-background-color: #2196F3;-fx-text-fill: white;-fx-background-radius: 15;-fx-padding: 8 20 8 20;");

        HBox buttonBox = new HBox(startButton); // horizontal layout 
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(180, 20, 0, 0));

        VBox layout = new VBox(20, title, buttonBox); // vertical layout 
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: transparent;");

        Pane rootWithBG = WholeMap.wrapBG(layout, "mainmenu_background.jpeg");
        stage.setScene(new Scene(rootWithBG, 900, 600));
        stage.setTitle("Eclipse & Duke");
        stage.show();
    }

    public static Pane wrapBGWithVideo(Pane content, String videoFileName) { // place video as background 
        if (backgroundPlayer == null) {
            Media media = new Media(MenuManager.class.getResource(videoFileName).toExternalForm());
            backgroundPlayer = new MediaPlayer(media);
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the video
            backgroundPlayer.setAutoPlay(true);
            backgroundPlayer.setMute(true); // Optional
            backgroundView = new MediaView(backgroundPlayer);
            backgroundView.setFitWidth(900);
            backgroundView.setFitHeight(600);
            backgroundView.setPreserveRatio(false);
        }

        StackPane root = new StackPane(); // UI on top of video 
        root.getChildren().addAll(backgroundView, content);
        return root;
    }

    public static void showMainMenu(Stage stage) { // displays main menu 
        Font pixelFont = Font.loadFont(MenuManager.class.getResourceAsStream("PixelifySans.ttf"), 80);

        Label title = new Label("Eclipse & Duke");
        title.setFont(pixelFont);
        title.setStyle("-fx-text-fill: white;");
        title.setEffect(new DropShadow(10, Color.BLACK));

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setPadding(new Insets(60, 0, 0, 0));

        Button startButton = new Button("\uD83D\uDCC2 Level Select");
        Button totButton = new Button("\uD83D\uDCD8 Tutorial");
        Button exitButton = new Button("\u274C Exit ");

        // button styling 
        for (Button btn : new Button[]{startButton, totButton, exitButton}) {
            btn.setFont(Font.font(pixelFont.getFamily(), 20));
            btn.setStyle("-fx-background-color: #2196F3;-fx-text-fill: white;-fx-background-radius: 15;-fx-padding: 8 20 8 20;");
        }

        // button actions 
        startButton.setOnAction(e -> LevelManager.showLevelSelection(stage));
        totButton.setOnAction(e -> {
            Main.staticMainMenu = () -> MenuManager.showMainMenu(stage);
            Tutorial_Screen.show(stage);
        });
        exitButton.setOnAction(e -> stage.close());

        VBox buttonBox = new VBox(30, startButton, totButton, exitButton); // button layout 
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(-80, 100, 0, 0));

        BorderPane layout = new BorderPane();
        layout.setTop(titleBox);
        layout.setCenter(buttonBox);
        layout.setStyle("-fx-background-color: transparent;");

        Pane rootWithBG = wrapBGWithVideo(layout, "menu_bg.mp4"); 
        stage.setScene(new Scene(rootWithBG, 900, 600));
        stage.setTitle("Main Menu - Eclipse & Duke");
        stage.show();
    }
}
