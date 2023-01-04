package com.example.cg_2;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Objects;

public class Controller {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<Point2D>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {

            if (Objects.requireNonNull(event.getButton()) == MouseButton.PRIMARY) {
                handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        graphicsContext.clearRect(0, 0, 800, 800);
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());


        final int POINT_RADIUS = 3;
        graphicsContext.fillOval(
                clickPoint.getX() - POINT_RADIUS, clickPoint.getY() - POINT_RADIUS,
                2 * POINT_RADIUS, 2 * POINT_RADIUS);

        points.add(clickPoint);
        if (points.size() > 1) {
            for (int i = 0; i < points.size(); i++) {
                graphicsContext.fillOval(
                        points.get(i).getX() - POINT_RADIUS, points.get(i).getY() - POINT_RADIUS,
                        2 * POINT_RADIUS, 2 * POINT_RADIUS);
            }
            for (int x = 1; x < 801; x++) {
                graphicsContext.strokeLine(x, interpolate(points, x), x - 1, interpolate(points, x - 1));
            }
        }
    }

    public static double interpolate(ArrayList<Point2D> points, double xValue) {
        double[] x = new double[points.size()];
        double[] y = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x[i] = points.get(i).getX();
            y[i] = points.get(i).getY();
        }
        int n = x.length;
        double result = 0;
        for (int i = 0; i < n; i++) {
            double product = y[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    product *= (xValue - x[j]) / (x[i] - x[j]);
                }
            }
            result += product;
        }
        return result;
    }


}