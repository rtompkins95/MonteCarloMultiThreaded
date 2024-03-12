package com.pi;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class DartBoardVisualization extends JFrame {
    private final List<XYPair> hits;
    private final List<XYPair> misses;

    public DartBoardVisualization(List<XYPair> hits, List<XYPair> misses) {
        super("Dartboard Visualization");
        this.hits = hits;
        this.misses = misses;

        initUI();
    }

    private void initUI() {
        XYSeriesCollection dataset = getSeriesCollection();

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Dartboard Hits and Misses",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Get the plot of the chart
        XYPlot plot = (XYPlot) chart.getPlot();

        // Set custom colors for hits and misses
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);  // Hits (index 0)
        plot.getRenderer().setSeriesPaint(1, Color.RED);   // Misses (index 1)
        plot.getRenderer().setSeriesPaint(2, Color.GREEN); // Circle (index 2)
        plot.getRenderer().setSeriesPaint(3, Color.BLACK); // Square (index 3)

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1080, 1080));

        setContentPane(chartPanel);
        pack();
        setLocationRelativeTo(null); // Center the window
    }

    private XYSeriesCollection getSeriesCollection() {
        System.out.println("Aggregating X-Y Coordinates");
        XYSeries hitSeries = new XYSeries("Hits");
        for (XYPair pair: hits) {
            hitSeries.add(pair.x, pair.y);
        }

        XYSeries missSeries = new XYSeries("Misses");
        for (XYPair pair: misses) {
            missSeries.add(pair.x, pair.y);
        }

        // Create series for the circle
        XYSeries circleSeries = new XYSeries("Circle");
        for (double theta = 0; theta <= 2 * Math.PI; theta += 0.01) {
            double x = Math.cos(theta); // Circle's x coordinate
            double y = Math.sin(theta); // Circle's y coordinate
            circleSeries.add(x, y);
        }

        // Create series for the square
        XYSeries squareSeries = new XYSeries("Square");
        // Vertices of the square
        squareSeries.add(-1.0, -1.0);
        squareSeries.add(-1.0, 1.0);
        squareSeries.add(1.0, 1.0);
        squareSeries.add(1.0, -1.0);
        squareSeries.add(-1.0, -1.0); // Close the square

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(hitSeries);
        dataset.addSeries(missSeries);
        dataset.addSeries(circleSeries);
        dataset.addSeries(squareSeries); // Add the square series to the dataset
        return dataset;
    }

    public void render() {
        System.out.println("Rendering graph...");
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
