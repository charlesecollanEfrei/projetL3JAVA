package model;

import java.util.*;
/**
 * Model the operation of a taxi company, operating different
 * types of vehicle. This version operates a only taxis.
 *
 * @author David J. Barnes and Michael Kolling. Modified A. Morelle
 * @version 2013.12.30
 */
public class TaxiCompany {
    private final int NUMBER_OF_TAXIS;
    private final int NUMBER_OF_SHUTTLES;

    // The vehicles operated by the company.
    private List<Vehicle> vehicles;

    private City city;

    // The associations between vehicles and the passengers
    // they are to pick up.
    private Map<Vehicle, Passenger> assignments;

    /**
     * @param city The city.
     */
    public TaxiCompany(City city) {
        this.city = city;
        NUMBER_OF_TAXIS = 3;
        NUMBER_OF_SHUTTLES = 0;
        vehicles = new LinkedList<>();
        assignments = new HashMap<>();
        setupVehicles();
    }

    public TaxiCompany(City city, int taxis, int shuttles) {
        this.city = city;
        NUMBER_OF_TAXIS = taxis;
        NUMBER_OF_SHUTTLES = shuttles;
        vehicles = new LinkedList<>();
        assignments = new HashMap<>();
        setupVehicles();
    }

    /**
     * Request a pickup for the given passenger.
     *
     * @param passenger The passenger requesting a pickup.
     * @return Whether a free vehicle is available.
     */
    public boolean requestPickup(Passenger passenger) {
        Vehicle vehicle = scheduleVehicle(passenger);
        if (vehicle != null) {
            assignments.put(vehicle, passenger);
            vehicle.setPickupLocation(passenger.getPickupLocation());
            vehicle.addPassengerWaiting(passenger);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A vehicle has arrived at a pickup point
     * (where a passenger is supposed to be waiting).
     *
     * @param The vehicle at the pickup point.
     */
    public void arrivedAtPickup(Vehicle vehicle) throws MissingPassengerException {
        Passenger passenger = assignments.remove(vehicle);
        if(passenger == null) {
            throw new MissingPassengerException(vehicle);
        }
        city.removeItem(passenger);
        vehicle.pickup(passenger);
        vehicle.removePassengerWaiting(passenger);
    }

    public void arrivedAtPickup(Vehicle vehicle, Passenger passenger) throws MissingPassengerException {
        if(passenger == null) {
            throw new MissingPassengerException(vehicle);
        }
        city.removeItem(passenger);
        vehicle.pickup(passenger);
        vehicle.removePassengerWaiting(passenger);
    }


    /**
     * A vehicle has arrived at a passenger's destination.
     *
     * @param The vehicle at the destination.
     * @param The passenger being dropped off.
     */
    public void arrivedAtDestination(Vehicle vehicle,
                                     Passenger passenger) {
    }

    /**
     * @return The list of vehicles.
     */
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Find a free vehicle, if any.
     *
     * @return A free vehicle, or null if there is none.
     */
    private Vehicle scheduleVehicle(Passenger passenger) {
        // On cherche le premier vehicule libre afin de le comparer plus tard aux autres vehicules libres par rapport à la distance au passenger
        Vehicle closestVehicle = vehicles.stream().filter(v -> v.isFree()).findFirst().orElse(null);
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isFree()) {
                if(vehicle.getLocation().distance(passenger.getPickupLocation()) < closestVehicle.getLocation().distance(passenger.getPickupLocation())) {
                    closestVehicle = vehicle;
                }
            }
        }
        return closestVehicle;
    }

    /**
     * Set up this company's vehicles. The optimum number of
     * vehicles should be determined by analysis of the
     * data gathered from the simulation.
     * <p>
     * Vehicles start at random locations.
     */
    private void setupVehicles() {
        int cityWidth = city.getWidth();
        int cityHeight = city.getHeight();

        // Use a fixed random seed for predictable behavior.
        // Or use different seeds for less predictable behavior.
        Random rand = new Random(12345);

        // Create the taxis.
        for (int i = 0; i < NUMBER_OF_TAXIS; i++) {
            Taxi taxi =
                    new Taxi(this,
                            new Location(rand.nextInt(cityWidth),
                                    rand.nextInt(cityHeight)));
            vehicles.add(taxi);
            city.addItem(taxi);
        }

        for(int i = 0; i < NUMBER_OF_SHUTTLES; i++) {
            Shuttle shuttle = new Shuttle(10, this, new Location(rand.nextInt(cityWidth), rand.nextInt(cityHeight)));
            vehicles.add(shuttle);
            city.addItem(shuttle);
        }
    }
}
