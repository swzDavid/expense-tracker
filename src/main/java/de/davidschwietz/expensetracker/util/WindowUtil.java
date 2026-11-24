package de.davidschwietz.expensetracker.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class WindowUtil {

    // Pfad relativ zu src/main/resources
    private static final String DIALOG_ICON = "/images/plus.png";

    /** Setzt das Icon oben links in der Titelleiste des Dialogs. */
    public static void applyIcon(Stage dialog) {
        try (InputStream in = WindowUtil.class.getResourceAsStream(DIALOG_ICON)) {
            if (in != null) {
                dialog.getIcons().add(new Image(in));
            } else {
                System.err.println("Icon not found: " + DIALOG_ICON);
            }
        } catch (IOException e) {
            System.err.println("Could not load icon: " + DIALOG_ICON);
        }
    }
}
