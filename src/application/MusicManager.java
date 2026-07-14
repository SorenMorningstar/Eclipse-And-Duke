package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer currentPlayer = null;
    public static void playBGM(String filename, boolean loop) { // handles bgm with looping and auto-stop
        stopBGM();
        try {
            Media media = new Media(MusicManager.class.getResource("/application/" + filename).toExternalForm());
            currentPlayer = new MediaPlayer(media);
            currentPlayer.setVolume(0.1);

            if (loop) {
                currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            } else {
                currentPlayer.setCycleCount(1);
                currentPlayer.setOnEndOfMedia(() -> {
                    stopBGM();
                });
            }

            currentPlayer.play();
        } catch (Exception e) {
            System.out.println("❌ Failed to play BGM: " + e.getMessage());
        }
    }
    
    public static void playBGMSequence(String first, String second) {
        if (currentPlayer != null) {
        	currentPlayer.stop();
        }

        Media media1 = new Media(Main.class.getResource("/application/" + first).toExternalForm());
        currentPlayer = new MediaPlayer(media1);
        currentPlayer.setCycleCount(1);
        currentPlayer.setOnEndOfMedia(() -> { // switch to second track and loop 
            Media media2 = new Media(Main.class.getResource("/application/" + second).toExternalForm());
            currentPlayer = new MediaPlayer(media2);
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
            currentPlayer.play();
        });
        currentPlayer.play();
    }

    public static void stopBGM() { // stop current track 
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }
    
    public static void playSoundEffect(String filename) { // sfx when in-game interactions occur 
        try {
            Media sound = new Media(MusicManager.class.getResource("/application/" + filename).toExternalForm());
            MediaPlayer player = new MediaPlayer(sound);
            player.setVolume(0.5);
            player.setOnEndOfMedia(player::dispose);
            player.play();
        } catch (Exception e) {
            System.out.println("❌ Failed to play sound effect: " + e.getMessage());
        }
    }
}


