package application.Util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    private static boolean result;

    public static boolean display (String title, String msg, String icon) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.getIcons().add(new Image("/res/icons/" + icon));
        window.setMinWidth(250);

        Text msgText = new Text(msg);

        Button yesButton = new Button("Yes");
        yesButton.setMinSize(40, 30);
        yesButton.setOnAction(e -> {
            result = true;
            window.close();
        });

        Button noButton = new Button("No");
        noButton.setMinSize(40, 30);
        noButton.setOnAction(e -> {
            result = false;
            window.close();
        });

        HBox buttonsLayout = new HBox(10);
        buttonsLayout.getChildren().addAll(yesButton, noButton);
        buttonsLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(msgText, buttonsLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return result;
    }

    public static boolean display (String title, String msg) {
        return display(title, msg, "launcher.png");
    }

}
