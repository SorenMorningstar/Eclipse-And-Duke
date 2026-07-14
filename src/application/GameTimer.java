package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameTimer {
    private Label timerLabel;
    private Timeline timer;
    private final IntegerProperty secondsElapsed = new SimpleIntegerProperty(0);

    public void init(Pane mapRoot) {

    	if (timer != null) timer.stop(); // stop previous timer if new already running 
        if (timerLabel != null) mapRoot.getChildren().remove(timerLabel);

        Font fredoka = Font.loadFont(Main.class.getResourceAsStream("PixelifySans.ttf"), 20); // custom font 

        timerLabel = new Label(); // custom font styling 
        timerLabel.setFont(fredoka);
        timerLabel.setStyle("-fx-text-fill: white;");
        timerLabel.setTranslateX(10);
        timerLabel.setTranslateY(10);
        mapRoot.getChildren().add(timerLabel);

        secondsElapsed.set(0); // reset timer 
        updateTimerLabelText(0);

        secondsElapsed.addListener((obs, oldVal, newVal) -> 
        updateTimerLabelText(newVal.intValue())
    );
        // increase time every second
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> 
            secondsElapsed.set(secondsElapsed.get() + 1)
        ));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stop() { // stop timer 
        if (timer != null) timer.stop();
    }

    public int getSeconds() {
        return secondsElapsed.get();
    }
    
    private void updateTimerLabelText(int totalSeconds) { // timer display in X min YY sec format 
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("Time: %d min %02d sec", minutes, seconds));
    }
}