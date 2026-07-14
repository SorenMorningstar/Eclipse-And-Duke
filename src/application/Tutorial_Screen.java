package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class Tutorial_Screen {

    public static void show(Stage stage) { // display tutorial screen 
        VBox tutorialLayout = new VBox(30);
        tutorialLayout.setAlignment(javafx.geometry.Pos.CENTER);
        tutorialLayout.setPadding(new Insets(40));
        tutorialLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

        Font pixelFont = Font.loadFont(Main.class.getResourceAsStream("PixelifySans.ttf"), 22);
        Font emojiFont = Font.font("Segoe UI Emoji", 22);

        Label title = new Label("Tutorial Instructions");
        title.setFont(Font.font(pixelFont.getFamily(), FontWeight.BOLD, 30));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));

        TextFlow flow = new TextFlow(); // box for instruction text 
        flow.setTextAlignment(TextAlignment.LEFT);
        flow.setLineSpacing(6);
        flow.setPrefWidth(800);
        flow.setMaxWidth(800);

        flow.getChildren().addAll( // instruction text 
            new Text("🌀 ") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text("Welcome to Eclipse & Duke!\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    Two heroes, one mission.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    Survive deadly traps, trigger mechanisms, and reach the exits together!\n\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},

            new Text("☾ ") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text("Eclipse - Use arrow keys:\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - ← → to move\n    - ↑ to jump\n\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},

            new Text("🔴 ") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text("Duke - Use WASD:\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - A D to move\n    - W to jump\n\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},

            new Text("🏆") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text("Your Goal:\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - Get both characters to their matching doors to complete the level.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - If either character dies, the level restarts.\n\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},

            new Text("☠︎") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text("What Kills You:\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - ") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("🔥") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text(" Fire: Kills Eclipse instantly.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - ") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("💧") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text(" Water: Kills Duke instantly.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - ") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("🧪") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text(" Poison Water: Kills both characters.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - ") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("⚡") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text(" Spikes: Kills both characters.\n\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},

            new Text("⚒") {{ setFont(emojiFont); setFill(Color.WHITE); }},
            new Text("Other Mechanisms:\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - Yellow Button: Step on them to temporarily disable barriers.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - Spring: Boosts the character upward for a higher jump.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - Portal: Enter one portal to instantly teleport to the linked portal.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }},
            new Text("    - Key: Required to open exit door.\n") {{ setFont(pixelFont); setFill(Color.WHITE); }}
        );

        Button backButton = new Button("\uD83D\uDD19 Back To Main Menu"); // button to return to previous page 
        backButton.setFont(Font.font(pixelFont.getFamily(), 18));
        backButton.setStyle(
        	    "-fx-background-color: #2196F3;" +  
        	    "-fx-text-fill: white;" +
        	    "-fx-background-radius: 15;" +
        	    "-fx-padding: 8 20 8 20;"
        	);

        backButton.setOnAction(e -> Main.staticMainMenu.run());

        tutorialLayout.getChildren().addAll(title, flow, backButton);

        ScrollPane scrollPane = new ScrollPane(tutorialLayout); // enable scrolling 
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Pane rootWithBG = WholeMap.wrapBG(scrollPane, "mainmenu_background.jpeg");
        Scene tutorialScene = new Scene(rootWithBG, 900, 600);
        stage.setScene(tutorialScene);
        stage.setTitle("Tutorial - Eclipse & Duke");
        
        Platform.runLater(() -> scrollPane.setVvalue(0)); // start at top of page when scrolling 
    }
}

