package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaFX application for visualizing the Union-Find data structure.
 */
public class Javafx extends Application {
    private UnionFind unionFind = new UnionFind(10);  // 10 nodes
    private Circle[] circles = new Circle[10];
    private Map<Circle, Text> nodeLabels = new HashMap<>();
    private Map<Circle, List<CubicCurve>> nodeCurves = new HashMap<>();
    private Pane root;
    private VBox controlBox = new VBox(10);
    private TextArea logArea = new TextArea();
    private Label statsLabel = new Label();

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        initializeUI(primaryStage);
    }

    /**
     * Initializes the user interface.
     *
     * @param primaryStage the primary stage for this application
     */
    private void initializeUI(Stage primaryStage) {
        root = new Pane();
        root.setPrefSize(800, 600);

        controlBox.setAlignment(Pos.CENTER);
        controlBox.setLayoutX(50);
        controlBox.setLayoutY(450);
        controlBox.setPrefWidth(700);

        logArea.setPrefHeight(100);
        logArea.setEditable(false);

        setupNodes();

        unionFind.addUnionListener(this::drawCurveBetweenNodes);

        setupControlBox(primaryStage);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Union-Find Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Sets up the draggable nodes.
     */
    private void setupNodes() {
        for (int i = 0; i < circles.length; i++) {
            Circle circle = createDraggableNode(i, 50 + i * 60, 200);
            Text label = new Text(String.valueOf(i));
            label.setX(circle.getCenterX() - 4);
            label.setY(circle.getCenterY() + 4);
            nodeLabels.put(circle, label);
            nodeCurves.put(circle, new ArrayList<>());
            circles[i] = circle;
            root.getChildren().addAll(circle, label);
        }
    }

    /**
     * Sets up the control box with input fields and buttons.
     *
     * @param primaryStage the primary stage for this application
     */
    private void setupControlBox(Stage primaryStage) {
        TextField node1 = new TextField();
        node1.setPromptText("Node 1");
        TextField node2 = new TextField();
        node2.setPromptText("Node 2");
        Button unionButton = new Button("Union");
        Button checkButton = new Button("Check Connection");
        Button saveButton = new Button("Save State");
        Button loadButton = new Button("Load State");

        Label resultLabel = new Label();

        unionButton.setOnAction(e -> handleUnionAction(node1, node2, resultLabel));
        checkButton.setOnAction(e -> handleCheckAction(node1, node2, resultLabel));
        saveButton.setOnAction(e -> handleSaveAction(primaryStage));
        loadButton.setOnAction(e -> handleLoadAction(primaryStage));

        controlBox.getChildren().addAll(node1, node2, unionButton, checkButton, saveButton, loadButton, resultLabel, statsLabel, logArea);
        root.getChildren().add(controlBox);
    }

    /**
     * Handles the union action.
     *
     * @param node1       the first node input field
     * @param node2       the second node input field
     * @param resultLabel the label to display the result
     */
    private void handleUnionAction(TextField node1, TextField node2, Label resultLabel) {
        int n1 = Integer.parseInt(node1.getText());
        int n2 = Integer.parseInt(node2.getText());
        unionFind.union(n1, n2);
        updateLog();
        updateStats();
        resultLabel.setText("Union done between " + n1 + " and " + n2);
    }

    /**
     * Handles the check connection action.
     *
     * @param node1       the first node input field
     * @param node2       the second node input field
     * @param resultLabel the label to display the result
     */
    private void handleCheckAction(TextField node1, TextField node2, Label resultLabel) {
        int n1 = Integer.parseInt(node1.getText());
        int n2 = Integer.parseInt(node2.getText());
        if (unionFind.connected(n1, n2)) {
            resultLabel.setText("Nodes " + n1 + " and " + n2 + " are connected.");
        } else {
            resultLabel.setText("Nodes " + n1 + " and " + n2 + " are not connected.");
        }
        updateLog();
        updateStats();
    }

    /**
     * Handles the save state action.
     *
     * @param primaryStage the primary stage for this application
     */
    private void handleSaveAction(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            saveState(file);
        }
    }

    /**
     * Handles the load state action.
     *
     * @param primaryStage the primary stage for this application
     */
    private void handleLoadAction(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            loadState(file);
        }
    }

    /**
     * Creates a draggable node (circle) for visualization.
     *
     * @param id the node id
     * @param x  the initial x-coordinate
     * @param y  the initial y-coordinate
     * @return the created Circle object
     */
    private Circle createDraggableNode(int id, double x, double y) {
        Circle circle = new Circle(15, Color.LIGHTBLUE);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setStroke(Color.DARKBLUE);

        circle.setOnMousePressed(event -> {
            circle.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        circle.setOnMouseDragged(event -> {
            double[] initialPos = (double[]) circle.getUserData();
            double offsetX = event.getSceneX() - initialPos[0];
            double offsetY = event.getSceneY() - initialPos[1];

            circle.setCenterX(circle.getCenterX() + offsetX);
            circle.setCenterY(circle.getCenterY() + offsetY);

            Text label = nodeLabels.get(circle);
            label.setX(circle.getCenterX() - 4);
            label.setY(circle.getCenterY() + 4);

            updateCurves(circle);

            circle.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        return circle;
    }

    /**
     * Updates the position of the curves attached to the given node.
     *
     * @param circle the circle node
     */
    private void updateCurves(Circle circle) {
        List<CubicCurve> curves = nodeCurves.get(circle);
        for (CubicCurve curve : curves) {
            if (curve.getStartX() == circle.getCenterX() && curve.getStartY() == circle.getCenterY()) {
                curve.setStartX(circle.getCenterX());
                curve.setStartY(circle.getCenterY());
            } else {
                curve.setEndX(circle.getCenterX());
                curve.setEndY(circle.getCenterY());
            }
            updateCurveControlPoints(curve);
        }
    }

    /**
     * Updates the control points of a given curve.
     *
     * @param curve the curve to update
     */
    private void updateCurveControlPoints(CubicCurve curve) {
        double controlX1 = (curve.getStartX() + curve.getEndX()) / 2;
        double controlY1 = Math.min(curve.getStartY(), curve.getEndY()) - 50;
        double controlX2 = (curve.getStartX() + curve.getEndX()) / 2;
        double controlY2 = Math.min(curve.getStartY(), curve.getEndY()) - 50;

        curve.setControlX1(controlX1);
        curve.setControlY1(controlY1);
        curve.setControlX2(controlX2);
        curve.setControlY2(controlY2);
    }

    /**
     * Draws a cubic curve between two nodes when a union operation is performed.
     *
     * @param p the first node
     * @param q the second node
     */
    private void drawCurveBetweenNodes(int p, int q) {
        Circle pCircle = circles[p];
        Circle qCircle = circles[q];
        double startX = pCircle.getCenterX();
        double startY = pCircle.getCenterY();
        double endX = qCircle.getCenterX();
        double endY = qCircle.getCenterY();

        CubicCurve curve = new CubicCurve();
        curve.setStartX(startX);
        curve.setStartY(startY);
        curve.setEndX(endX);
        curve.setEndY(endY);
        updateCurveControlPoints(curve);
        curve.setStroke(Color.GRAY);
        curve.setStrokeWidth(2);
        curve.setFill(null);

        root.getChildren().add(curve);

        nodeCurves.get(pCircle).add(curve);
        nodeCurves.get(qCircle).add(curve);
    }

    /**
     * Updates the log area with the latest operation log from the Union-Find structure.
     */
    private void updateLog() {
        List<String> log = unionFind.getOperationLog();
        logArea.setText(String.join("\n", log));
    }

    /**
     * Updates the statistics label with the current component count and operation count.
     */
    private void updateStats() {
        int componentCount = unionFind.getComponentCount();
        int operationCount = unionFind.getOperationCount();
        statsLabel.setText("Components: " + componentCount + " | Operations: " + operationCount);
    }

    /**
     * Saves the current state of the Union-Find structure to a file.
     *
     * @param file the file to save to
     */
    private void saveState(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(unionFind);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the state of the Union-Find structure from a file.
     *
     * @param file the file to load from
     */
    private void loadState(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            UnionFind loadedUnionFind = (UnionFind) in.readObject();
            unionFind = loadedUnionFind;
            updateLog();
            updateStats();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
