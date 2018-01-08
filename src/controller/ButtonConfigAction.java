package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import view.ConfigReader;

/**
 * Created by david on 16/03/2017.
 */
public class ButtonConfigAction extends AbstractAction {
	private ConfigReader configReader;
	private Simulation s;

    public ButtonConfigAction(ConfigReader configReader, String text, Simulation s) {
        super(text);
        this.configReader = configReader;
        this.s = s;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
        configReader.dispose();
        s.decrementCD();
    }
}
