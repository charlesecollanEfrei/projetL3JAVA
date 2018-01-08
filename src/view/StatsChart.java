package view;
import javax.swing.JPanel;

import model.Vehicle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 21/03/2017.
 */
public class StatsChart extends ApplicationFrame {
    private List<Vehicle> vehicles;
    public StatsChart(String title, List<Vehicle> list)
    {
        super(title);
        vehicles = new ArrayList<>(list);
    }
    private PieDataset createDataset()
    {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for(Vehicle vehicle : vehicles) {
            dataset.setValue(vehicle.getIdentifiant(), vehicle.getNbPickUp());
        }
        return dataset;
    }
    private static JFreeChart createChart(PieDataset dataset)
    {
        JFreeChart chart = ChartFactory.createPieChart(
                "Nombre de pickup pour chaque vehicule",
                dataset,
                true,
                true,
                false);

        return chart;
    }
    public JPanel createDemoPanel()
    {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    public void displayGUI() {
        setSize(new Dimension(600,600));
        setContentPane(createDemoPanel());
        setVisible(true);
    }
}
