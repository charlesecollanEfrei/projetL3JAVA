package view;

import controller.ButtonConfigAction;
import controller.Simulation;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

/**
 * Created by david on 16/03/2017.
 */
public class ConfigReader extends JFrame {
    private JSpinner width;
    private JSpinner height;
    private JSpinner taxis;
    private JSpinner shuttles;
    private JSpinner probability_passenger;
    private Simulation simulation;

    public ConfigReader(Simulation simulation) {
        super("Configuration de la ville");
        this.simulation = simulation;
        build();
    }

    private void build() {
        setSize(new Dimension(450, 200));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0,2));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(buildContentPane());
        setVisible(true);
    }

    private JPanel buildContentPane() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new GridLayout(0,2));
        pnl.add(new JLabel("Longueur de la fenetre : "));
        width = new JSpinner(new SpinnerNumberModel(25, 25, 50, 1));
        pnl.add(width);

        pnl.add(new JLabel("Hauteur de la fenetre : "));
        height = new JSpinner(new SpinnerNumberModel(25, 25, 50, 1));
        pnl.add(height);

        pnl.add(new JLabel("Nombre de taxis : "));
        taxis = new JSpinner(new SpinnerNumberModel(1, 1, 15, 1));
        pnl.add(taxis);

        pnl.add(new JLabel("Nombre de navettes : "));
        shuttles = new JSpinner(new SpinnerNumberModel(1, 1, 15, 1));
        pnl.add(shuttles);

        pnl.add(new JLabel("Probabilité d'apparition des passagers : "));
        probability_passenger = new JSpinner(new SpinnerNumberModel(0.06, 0.01, 1, 0.01));
        pnl.add(probability_passenger);

        JButton btn = new JButton(new ButtonConfigAction(this, "Configurer", simulation));
        pnl.add(btn);
        return pnl;

    }

    public int getWidthFrame() {
        try {
            width.commitEdit();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (Integer) width.getValue();
    }

    public int getHeightFrame() {
        try {
            height.commitEdit();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (Integer) height.getValue();
    }

    public int getTaxis() {
        try {
            taxis.commitEdit();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (Integer) taxis.getValue();
    }

    public int getShuttles() {

        return (Integer) shuttles.getValue();
    }

    public double getProbability_passenger () {
        try {
            probability_passenger.commitEdit();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (Double) probability_passenger.getValue();}

}
