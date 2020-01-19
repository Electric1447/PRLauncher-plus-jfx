package application;

import application.Objects.WadList;
import application.Util.AlertBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.prefs.Preferences;

public class Main extends Application {

    Preferences pref = Preferences.userNodeForPackage(Main.class);

    final String VERSION = "0.3.0jfx";

    Stage window;
    Scene mainScene;

    int w_Width = 440;
    int w_Height = 750;

	VBox vbox = new VBox(10);
    int defaultPadding = 12;

    WadList iwads = new WadList();

    Button runButton, dirButton;

    ChoiceBox<String> executableCB, iwadCB, compCB, diffCB;

    Text folderPathT, execT, iwadT, compT, diffT, warpT, argT;
    TextField folderPathTF, warpTF, argTF;

    String folderPath = "", iwadPath = "", warpStr = "", argStr = "";

    String[] prboomExec = new String[] {"prboom-plus.exe", "glboom-plus.exe"};
    String[] prboomExecNames = new String[] {"PRBoom+ (" + prboomExec[0] + ")", "GLBoom+ (" + prboomExec[1] + ")"};

    String[] complevels = new String[] {"-1 - Current Prboom-plus", "0 - Doom v1.2", "1 - Doom v1.666", "2 - Doom v1.9", "3 - Ultimate Doom", "4 - Final Doom's doom2.exe", "5 - DOSDoom", "6 - TASDDOOM",
            "7 - Boom's compatibility mode", "8 - Boom v2.01", "9 - Boom v2.02", "10 - LxDoom v1.4.x", "11 - MBF", "12 - PrBoom v2.03beta", "13 - PrBoom v2.1.0", "14 - PrBoom v2.1.1-2.2.6",
            "15 - PrBoom v2.3.x", "16 - PrBoom v2.4.0", "17 - Latest PrBoom-plus"};

    String[] difficulties = new String[] {"None (select ingame)", "I'm too young to die", "Hey, not too rough", "Hurt me plenty", "Ultra-Violence", "Nightmare!"};

    int cExecutable = 0, cIWad = 0, cComplevel = 0, cDifficulty = 0;

    Font textFont = Font.font("verdana", FontWeight.MEDIUM, 16);
    Font textEditFont = Font.font("verdana", 12);

    public void print (Object obj) {
        System.out.println(obj);
    }

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("PRLauncher+ " + VERSION);

        folderPath = pref.get("folderPath", folderPath);
        cExecutable = pref.getInt("cExecutable", cExecutable);
        cIWad = pref.getInt("cIWad", cIWad);
        cComplevel = pref.getInt("cComplevel", cComplevel);
        cDifficulty = pref.getInt("cDifficulty", cDifficulty);
        warpStr = pref.get("warpStr", warpStr);
        argStr = pref.get("argStr", argStr);


        iwads.detectWads(folderPath);

        // If cIWad is out of index
        if (cIWad > iwads.getWadsFullTitle().length)
            cIWad = 0;

        folderPathT = new Text("PRBoom+ folder location:");
        folderPathT.setFont(textFont);
        execT = new Text("PRBoom+ Executable:");
        execT.setFont(textFont);
        iwadT = new Text("IWAD:");
        iwadT.setFont(textFont);
        compT = new Text("Enter Compatibility Level:");
        compT.setFont(textFont);
        diffT = new Text("Enter Difficulty:");
        diffT.setFont(textFont);
        warpT = new Text("Enter Level:");
        warpT.setFont(textFont);
        argT = new Text("Additional Arguments:");
        argT.setFont(textFont);

        folderPathTF = new TextField(folderPath);
        folderPathTF.setFont(textEditFont);
        warpTF = new TextField(warpStr);
        warpTF.setFont(textEditFont);
        argTF = new TextField(argStr);
        argTF.setFont(textEditFont);

        executableCB = new ChoiceBox<>();
        executableCB.getItems().addAll(prboomExecNames);
        executableCB.setValue(prboomExecNames[cExecutable]);
        executableCB.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            cExecutable = t1.intValue();
            pref.putInt("cExecutable", cExecutable);
        });

        iwadCB = new ChoiceBox<>();
        iwadCB.getItems().addAll(iwads.getWadsFullTitle());
        iwadCB.setValue(iwads.getWadFullTitle(cIWad));
        iwadCB.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            cIWad = t1.intValue();
            if (cIWad == -1) cIWad = 0; // cIWad can't be -1;
            pref.putInt("cIWad", cIWad);
        });

        compCB = new ChoiceBox<>();
        compCB.getItems().addAll(complevels);
        compCB.setValue(complevels[cComplevel]);
        compCB.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            cComplevel = t1.intValue();
            pref.putInt("cComplevel", cComplevel);
        });

        diffCB = new ChoiceBox<>();
        diffCB.getItems().addAll(difficulties);
        diffCB.setValue(difficulties[cDifficulty]);
        diffCB.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            cDifficulty = t1.intValue();
            pref.putInt("cDifficulty", cDifficulty);
        });

        dirButton = new Button("Select PRBoom+ Directory");
        dirButton.setOnAction(e -> {
            File selectedDirectory = new DirectoryChooser().showDialog(window);

            if (selectedDirectory != null) {
                folderPath = selectedDirectory.getAbsolutePath();
                folderPathTF.setText(folderPath);
                pref.put("folderPath", folderPath);
                iwads.detectWads(folderPath);
                iwadCB.getItems().setAll(iwads.getWadsFullTitle());
                iwadCB.setValue(iwads.getWadFullTitle(cIWad));
            }
        });

        runButton = new Button("Launch");
        runButton.setPrefWidth(8000);
        runButton.setPrefHeight(40);
        runButton.setFont(textFont);
        runButton.setOnAction(e -> {

            String warp_temp = " ", diff_temp = " ", iwad_temp = " ";

            folderPath = folderPathTF.getText();
            pref.put("folderPath", folderPath);

            warpStr = warpTF.getText();
            pref.put("warpStr", warpStr);

            argStr = argTF.getText();
            pref.put("argStr", argStr);

            if (!warpTF.getText().equals(""))
                warp_temp = " -warp " + warpStr;

            if (cDifficulty != 0)
                diff_temp = " -skill " + cDifficulty;

            if (!iwads.getWadFilename(cIWad).equals(""))
                iwad_temp = " -iwad " + iwads.getWadFilename(cIWad);

            try {
                if (folderPath.equals(""))
					AlertBox.display("Error", "Please insert the path to your PRBoom+ folder", "error.png");
                else if (!(new File(folderPath + "\\" + prboomExec[cExecutable]).exists()))
                    AlertBox.display("Error", "Cant find " + prboomExec[cExecutable], "error.png");
                else {
                    print(folderPath + "\\" + prboomExec[cExecutable] + iwad_temp + " -complevel " + (cComplevel - 1) + warp_temp + diff_temp + " " + argStr);
                    Runtime.getRuntime().exec(folderPath + "\\" + prboomExec[cExecutable] + iwad_temp + " -complevel " + (cComplevel - 1) + warp_temp + diff_temp + " " + argStr);
                }
            } catch (Exception excp) {
                excp.printStackTrace();
            }
        });


        Image banner = new Image("/res/drawable/banner.png");
        ImageView bannerIV = new ImageView(banner);
        VBox bannerVBox = new VBox(bannerIV);
        bannerVBox.setAlignment(Pos.TOP_LEFT);

        VBox pathVBox = new VBox(10);
        pathVBox.setPadding(new Insets(10, 0, defaultPadding, 0));
        pathVBox.getChildren().addAll(folderPathT, folderPathTF, dirButton);

        VBox execVBox = new VBox(10);
        execVBox.setPadding(new Insets(0, 0, defaultPadding, 0));
        execVBox.getChildren().addAll(execT, executableCB);

        VBox iwadVBox = new VBox(10);
        iwadVBox.setPadding(new Insets(0, 0, defaultPadding, 0));
        iwadVBox.getChildren().addAll(iwadT, iwadCB);

        VBox compVBox = new VBox(10);
        compVBox.setPadding(new Insets(0, 0, defaultPadding, 0));
        compVBox.getChildren().addAll(compT, compCB);

        VBox diffVBox = new VBox(10);
        diffVBox.setPadding(new Insets(0, 0, defaultPadding, 0));
        diffVBox.getChildren().addAll(diffT, diffCB);

        VBox warpVBox = new VBox(10);
        warpVBox.setPadding(new Insets(0, 0, defaultPadding, 0));
        warpVBox.getChildren().addAll(warpT, warpTF);

        VBox argVBox = new VBox(10);
        argVBox.setPadding(new Insets(0, 0, defaultPadding, 0));
        argVBox.getChildren().addAll(argT, argTF);

        VBox runVBox = new VBox();
        runVBox.setPadding(new Insets(0, 15, 0, 15));
        runVBox.getChildren().addAll(runButton);

        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(bannerVBox, pathVBox, execVBox, iwadVBox, compVBox, diffVBox, warpVBox, argVBox, runVBox);

        StackPane layout = new StackPane();
        layout.getChildren().add(vbox);

        mainScene = new Scene(layout, w_Width, w_Height);
        window.getIcons().add(new Image("/res/icons/launcher.png"));
        window.setScene(mainScene);
        window.setMinWidth(w_Width + 40);
        window.setMinHeight(w_Height + 40);
        window.setMaxWidth(960);
        window.setMaxHeight(w_Height + 120);
        window.show();
    }

}

