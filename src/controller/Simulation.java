package controller;

import model.Actor;
import model.City;
import model.PassengerSource;
import model.TaxiCompany;
import view.CityGUI;
import view.ConfigReader;
import view.StatsChart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Run the simulation by asking a collection of actors to act.
 *
 * @author David J. Barnes and Michael Kolling. Modified A. Morelle
 * @version 2013.12.30
 */
public class Simulation {
    private List<Actor> actors;
    private CountDownLatch windowClosed;
    private StatsChart sc;

    /**
     * Create the initial set of actors for the simulation.
     */
    public Simulation() {
    	windowClosed = new CountDownLatch(1);
    	
    	  ConfigReader config = new ConfigReader(this);
          try {
        	  windowClosed.await();
          } catch (InterruptedException e) {
        	  e.printStackTrace();
          }
          
        actors = new LinkedList<>();
        City city = new City(config.getWidthFrame(), config.getHeightFrame());
        TaxiCompany company = new TaxiCompany(city, config.getTaxis(), config.getShuttles());
        PassengerSource source = new PassengerSource(city, company , config.getProbability_passenger());

        actors.addAll(company.getVehicles());
        sc = new StatsChart("Statistiques des vehicules", company.getVehicles());
        actors.add(source);
        actors.add(new CityGUI(city));
    }
    /**
     * Run the simulation for a fixed number of steps.
     * Pause after each step to allow the GUI to keep up.
     */
    public void run() {
        for (int i = 0; i < 400; i++) {
            step();
            wait(400);
        }
        System.out.println("End simulation");
        sc.displayGUI();
    }

    /**
     * Take a single step of the simulation.
     */
    public void step() {
        for (Actor actor : actors) {
            actor.act();
        }
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to cause a small delay.
     *
     * @param milliseconds The number of milliseconds to wait.
     */
    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignore the exception
        }
    }
    
    public void decrementCD(){
        windowClosed.countDown();
        }
    
}



