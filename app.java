// package application;
import javafx.application.Application;
// import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Scanner;


public class App extends Application {

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> functionSeries;
    private double xMin = -20;
    private double xMax = 20;
    // private double yMax = 100;
    // private double yMin = 100;

    @Override
    public void start(Stage primaryStage) {
        // Define the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create the line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Function Grapher");

        // Define the data series
        functionSeries = new XYChart.Series<>();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter function: ");
        String expression = input.nextLine();

        expression = expression.replaceAll(" ", "");
        // System.out.println("remove spaced: "+ expression);
        expression = Utils.validator(expression);

        while (expression == "") {
            System.out.println("Invalid expression, enter again: ");
            expression = Utils.validator(input.nextLine());
        }
        expression = '(' + expression + ')';
        functionSeries.setName(expression);
        
        // Generate initial data points for the function f(x) = sin(x)
        plotGraph(expression);

        // Add the function series to the chart
        lineChart.getData().add(functionSeries);

        // Create text fields and buttons for user input
        TextField functionField = new TextField(expression);
        functionField.setPromptText("Enter function (e.g., sin(x), x^2)");
        Button plotButton = new Button("Plot");
        Button zoomInButton = new Button("Zoom In");
        Button zoomOutButton = new Button("Zoom Out");
        Button closeButton = new Button("Close");

        // plotButton.setOnAction(e -> plotGraph(functionField.getText()));
        plotButton.setOnAction(e -> newPlot(functionField.getText()));
        zoomInButton.setOnAction(e -> zoomIn());
        zoomOutButton.setOnAction(e -> zoomOut());
        closeButton.setOnAction(e -> closeFunction(primaryStage));

        HBox inputBox = new HBox(10, new Label("Function:"), functionField, plotButton, zoomInButton, zoomOutButton, closeButton);
        inputBox.setPadding(new Insets(10));

        // Create the scene and set it in the stage
        VBox root = new VBox(inputBox, lineChart);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Set the title and show the stage
        primaryStage.setTitle("Function Grapher");
        primaryStage.show();
    }

    private void plotGraph(String function) {
        // Clear previous data points
        functionSeries.getData().clear();

        // Evaluate the user-defined function for each x-value
        try {
            
            Utils.nodePointer functionRoot = Utils.build(function);
            // System.out.println("built");
            // Utils.postorder(functionRoot);
            // System.out.println("printed?");
            
            // Function<Double, Double> userFunction = k -> Utils.getFunction(x, functionRoot);
            for (double x = xMin; x <= xMax; x += 0.1) {
                double y = Utils.getFunction(x, functionRoot);
                // if (y <= yMax && y >= yMin) {
                functionSeries.getData().add(new XYChart.Data<>(x, y));
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void newPlot(String expression) {
        expression = expression.replaceAll(" ", "");
        expression = Utils.validator(expression);
        if (expression == "") {
            return;
        }
        functionSeries.setName(expression);
        plotGraph(expression);
    }

    private void zoomIn() {
        double delta = 1;
        // yMin *= (1.1*delta);
        // yMax *= (0.9*delta);
        xMin += delta;
        xMax -= delta;
        plotGraph(functionSeries.getName());
    }

    private void zoomOut() {
        double delta = 1;
        // yMin *= (0.9*delta);
        // yMax *= (1.1*delta);
        xMin -= delta;
        xMax += delta;
        plotGraph(functionSeries.getName());
    }

    private void closeFunction(Stage primaryStage) {
        // Close the entire application
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
