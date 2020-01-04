package application.Util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display (String title, String msg, String icon) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.getIcons().add(new Image("/icons/" + icon));
        window.setMinWidth(250);

        Text msgText = new Text(msg);
        msgText.setFont(Font.font("verdana", 12));
        Button closeButton = new Button("Okay");
        closeButton.setFont(Font.font("verdana", 12));
        closeButton.setMinSize(48, 32);
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(msgText, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void display (String title, String msg) {
        display(title, msg, "launcher.png");
    }

}
