package application;

import application.Util.AlertBox;
import application.Util.ConfirmBox;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;

public class Main extends Application {

    Preferences pref = Preferences.userNodeForPackage(Main.class);

    final String VERSION = "0.2.0";

    Stage window;
    Scene mainScene;

    int w_Width = 440;
    int w_Height = 460;

    VBox vbox = new VBox(10);

    Button runButton, dirButton;
    Text folderPathT, warpT, argT;
    TextField folderPathTF, warpTF, argTF;

    Font textFont = Font.font("verdana", FontWeight.MEDIUM, 16);
    Font texteditFont = Font.font("verdana", 12);

    String folderPath = "", warpStr = "", argStr = "";
    String[] prboomExecutables = new String[] {"prboom-plus.exe", "glboom-plus.exe"};
    String cExecutable = prboomExecutables[1];

    public void print (String str) {
        System.out.println(str);
    }

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("PRLauncher+ " + VERSION);

        folderPath = pref.get("folderPath", folderPath);
        warpStr = pref.get("warpStr", warpStr);
        argStr = pref.get("argStr", argStr);

        folderPathT = new Text("PRBoom+ folder location:");
        folderPathT.setFont(textFont);
        warpT = new Text("Enter level:");
        warpT.setFont(textFont);
        argT = new Text("Additional Arguments:");
        argT.setFont(textFont);

        folderPathTF = new TextField(folderPath);
        folderPathTF.setFont(texteditFont);
        warpTF = new TextField(warpStr);
        warpTF.setFont(texteditFont);
        argTF = new TextField(argStr);
        argTF.setFont(texteditFont);

        dirButton = new Button("Select PRBoom+ Directory");
        dirButton.setOnAction(e -> {
            File selectedDirectory = new DirectoryChooser().showDialog(window);

            if (selectedDirectory != null) {
                folderPath = selectedDirectory.getAbsolutePath();
                folderPathTF.setText(folderPath);
                pref.put("folderPath", folderPath);
            }
        });

        runButton = new Button("Launch");
        runButton.setPrefWidth(8000);
        runButton.setPrefHeight(40);
        runButton.setFont(textFont);
        runButton.setOnAction(e -> {

            folderPath = folderPathTF.getText();
            pref.put("folderPath", folderPath);

            warpStr = warpTF.getText();
            pref.put("warpStr", warpStr);

            argStr = argTF.getText();
            pref.put("argStr", argStr);

            String warp_temp = "";
            if (!warpTF.getText().equals(""))
                warp_temp = "-warp ";

            try {
                if (folderPath.equals(""))
                    AlertBox.display("Error", "Please insert the path to your PRBoom+ folder", "error.png");
                else if (!(new File(folderPath + "\\" + cExecutable).exists()))
                    AlertBox.display("Error", "Cant find " + cExecutable, "error.png");
                else
                    Runtime.getRuntime().exec(folderPath + "\\" + cExecutable + " " + argStr + " " + warp_temp + warpStr);
            } catch (Exception excp) {
                excp.printStackTrace();
            }
        });

        Image banner = new Image("/res/drawable/banner.png");
        ImageView bannerIV = new ImageView(banner);
        VBox bannerVBox = new VBox(bannerIV);
        bannerVBox.setAlignment(Pos.TOP_LEFT);

        VBox pathVBox = new VBox(10);
        pathVBox.setPadding(new Insets(10, 0, 20,0));
        pathVBox.getChildren().addAll(folderPathT, folderPathTF, dirButton);

        VBox warpVBox = new VBox(10);
        warpVBox.setPadding(new Insets(0, 0, 20,0));
        warpVBox.getChildren().addAll(warpT, warpTF);

        VBox argVBox = new VBox(10);
        argVBox.setPadding(new Insets(0, 0, 20,0));
        argVBox.getChildren().addAll(argT, argTF);

        VBox runVBox = new VBox();
        runVBox.setPadding(new Insets(0, 15, 0,15));
        runVBox.getChildren().addAll(runButton);

        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(bannerVBox, pathVBox, warpVBox, argVBox, runVBox);

        StackPane layout = new StackPane();
        layout.getChildren().add(vbox);

        mainScene = new Scene(layout, w_Width, w_Height);
        window.getIcons().add(new Image("/res/icons/launcher.png"));
        window.setScene(mainScene);
        window.setMinWidth(w_Width + 40);
        window.setMinHeight(w_Height + 40);
        window.setMaxWidth(960);
        window.setMaxHeight(w_Height + 80);
        window.show();
    }

}
