package com.doc.semi_compiler.controller;

import com.doc.semi_compiler.compiler.Compiler;
import com.doc.semi_compiler.compiler.Node;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Controller {


    @FXML
    private MenuItem menuItemNew;
    @FXML
    private MenuItem menuItemOpen;
    @FXML
    private MenuItem menuItemSave;
    @FXML
    private MenuItem menuItemSaveAs;
    @FXML
    private MenuItem menuItemExit;
    @FXML
    private MenuItem menuItemCut;
    @FXML
    private MenuItem menuItemCopy;
    @FXML
    private MenuItem menuItemPaste;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private Button buttonNewFile;
    @FXML
    private Button buttonOpen;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonSaveAs;
    @FXML
    private Button buttonCut;
    @FXML
    private Button buttonCopy;
    @FXML
    private Button buttonPaste;
    @FXML
    private Button buttonScanner;
    @FXML
    private Button buttonParser;

    @FXML
    public TextArea areaCode;

    @FXML
    public TextArea areaConsole;

    private File currentFile;

    public ArrayList<String> fileReader(String filePath) throws Exception 
    {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

        String line;
        ArrayList<String> lines = new ArrayList<String>();

        while ((line = reader.readLine()) != null)
            lines.add(line.trim() + ' ');

        reader.close();
        return lines;
    }

    public void fileWriter(String content, String filePath) throws Exception {
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }

    @FXML
    public void addIcons() {

        MenuItem[] menuItems = { menuItemNew, menuItemOpen, menuItemSave, menuItemSaveAs,
                menuItemCut, menuItemCopy, menuItemPaste, menuItemAbout, menuItemExit };

        Button[] buttons = { buttonNewFile, buttonOpen, buttonSave, buttonSaveAs, buttonCut,
                buttonCopy, buttonPaste, buttonScanner, buttonParser };

        String[] images = { "newFile", "openFile", "save", "saveAs",
                "cut", "copy", "paste", "about", "exit" };

        for (int i = 0; i < 9; i++) {
            ImageView icon = new ImageView(
                    new Image(getClass().getResourceAsStream("/com/doc/semi_compiler/icons/" + images[i] + ".png")));
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            menuItems[i].setGraphic(icon);
        }

        images[7] = "scanner";
        images[8] = "parser";

        for (int i = 0; i < 9; i++) {
            ImageView icon = new ImageView(
                    new Image(getClass().getResourceAsStream("/com/doc/semi_compiler/icons/" + images[i] + ".png")));
            if (i < 7) {
                icon.setFitHeight(30);
                icon.setFitWidth(30);
            } else {
                icon.setFitHeight(60);
                icon.setFitWidth(60);
            }
            buttons[i].setGraphic(icon);
        }
    }

    @FXML
    public void chooseFileExplorer(Event event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose a file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            currentFile = selectedFile;
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                areaCode.setText(content.toString());
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An Error Occurred");
                alert.setContentText("something went wrong during reading the file please try again");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void newFile(Event event) {
        areaCode.clear();
        currentFile = null;
    }

    @FXML
    void copyText(Event event) {
        String selectedText = areaCode.getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            clipboard.setContent(content);
        }
    }

    @FXML
    void cutText(Event event) {
        String selectedText = areaCode.getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            clipboard.setContent(content);

            int start = areaCode.getSelection().getStart();
            int end = areaCode.getSelection().getEnd();
            areaCode.replaceText(start, end, "");
        }
    }

    @FXML
    void exit(Event event) {
        System.exit(0);
    }

    @FXML
    public void pasteText(Event event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            String clipboardText = clipboard.getString();
            int caretPosition = areaCode.getCaretPosition();
            areaCode.insertText(caretPosition, clipboardText);
        }
    }

    /**
     * *****file menu****
     * save file functionality
     * save as file functionality
     */
    @FXML
    public void saveFile(MouseEvent event) {
        if (currentFile != null) {
            // Save to the currently opened file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                writer.write(areaCode.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // No file is currently open, prompt the user to create a new one
            saveFileAs(event);
        }
    }

    @FXML
    public void saveFileAs(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File As");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(areaCode.getScene().getWindow());
        if (file != null) {
            currentFile = file;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(areaCode.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * *****main menu****
     * save file functionality
     * save as file functionality
     */


    @FXML
    private void scanner(MouseEvent event) {
        areaConsole.setText("Scanning\n\n");
        if (currentFile == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No File Opened");
            alert.setContentText("Please open a file or save your code before running the parser.");
            alert.showAndWait();
            return;
        }
 
        try {
            ArrayList<String> lines = fileReader(currentFile.getAbsolutePath());

            Compiler newCompiler = new Compiler();
 
            String result = newCompiler.scanner(lines);
            areaConsole.appendText(result);


            if(newCompiler.getScannerError() == false)
            {
                String currentFilePath = currentFile.getAbsolutePath();
                String newFilePath = currentFilePath.replaceFirst("\\.txt$", "") + "_scanner.txt";
                fileWriter(result, newFilePath);
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An Error Occurred");
            alert.setContentText("Something went wrong during scanning the file. Please try again.");
            alert.showAndWait();
        }
    }

    private void openImageInNewScene(String filePath) {
    try {
        Image image = new Image(new FileInputStream(filePath));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800); 

        StackPane pane = new StackPane(imageView);
        Scene scene = new Scene(pane, 900, 600);

        Stage stage = new Stage();
        stage.setTitle("Syntax Tree");
        stage.setScene(scene);
        stage.show();

    } catch (FileNotFoundException e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("File Not Found");
        alert.setContentText("The syntax tree image could not be found. Please try again.");
        alert.showAndWait();
    }
}

    @FXML
    void parser(MouseEvent event) { 

        areaConsole.setText("Parsing\n\n");
        if (currentFile == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No File Opened");
            alert.setContentText("Please open a file or save your code before running the parser.");
            alert.showAndWait();
            return;
        }

        try {
            ArrayList<String> lines = fileReader(currentFile.getAbsolutePath());

            Compiler newCompiler = new Compiler();

            String content = newCompiler.scanner(lines);
            
            if(newCompiler.getScannerError() == false)
            {
                Node root = newCompiler.parse();

                if(newCompiler.getParserError() == false)
                {
                    newCompiler.generateSyntaxTree(root);
                    String currentFilePath = currentFile.getAbsolutePath();
                    String newFilePath = currentFilePath.replaceFirst("\\.txt$", "") + "_syntaxtree.png";
                    Graphviz.fromGraph(newCompiler.getSyntaxTree()).render(Format.PNG).toFile(new File(newFilePath));

                    areaConsole.appendText("syntax tree generated successfully");

                    openImageInNewScene(newFilePath);
                }
                else 
                {
                    areaConsole.appendText("Parsing Error\n\n");
                    areaConsole.appendText(newCompiler.getErrorStr());
                }
            }
            else
            {
                areaConsole.appendText("Scanning Error\n\n" + content);
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An Error Occurred");
            alert.setContentText("Something went wrong during parsing the file. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    public void initialize() {
        addIcons();
    }
}
