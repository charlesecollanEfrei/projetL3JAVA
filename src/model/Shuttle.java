package model;

import javax.swing.*;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A shuttle is able to carry multiple passengers.
 * This implementation is non-functional.
 *
 * @author David J. Barnes and Michael Kolling. Modified A. Morelle
 * @version 2013.12.30
 */
public class Shuttle extends Vehicle implements DrawableItem {
    // The maximum number of passengers for this shuttle.
    private final int MAXPASSENGERS;
    // The list of destinations for the shuttle.
    private List<Location> destinations;
    // The list of passengers on the shuttle.
    private List<Passenger> passengers;

    private Image emptyImage;
    private Image passengerImage;
    private static int nb_shuttle = 0;

    /**
     * Constructor for objects of class Shuttle
     *
     * @param maxPassengers The max number of passengers. Must be positive.
     * @param company       The taxi company. Must not be null.
     * @param location      The vehicle's starting point. Must not be null.
     * @throws NullPointerException If company or location is null.
     */
    public Shuttle(int maxPassengers, TaxiCompany company, Location location) {
        super(company, location, "S" + nb_shuttle++);
        MAXPASSENGERS = maxPassengers;
        destinations = new LinkedList<>();
        passengers = new LinkedList<>();
        emptyImage = new ImageIcon(getClass().getResource(
                "/images/bus.jpg")).getImage();
        passengerImage = new ImageIcon(getClass().getResource(
                "/images/bus+persons.jpg")).getImage();
    }

    /**
     * Carry out a shuttle's actions.
     */
    public void act() {
        Location target = getTargetLocation();
        if (target != null) {
            Location next = getLocation().nextLocation(target);
            setLocation(next);
            if (next.equals(target)) {
                // Si ca ne correspond à aucune destination des passagers à bord on regarde si c'est un pickup
                if (!passengers.stream().anyMatch(p -> p.getDestination().equals(getLocation()))) {
                    Passenger passenger = getPassengersWaiting().stream().filter(p -> p.getPickupLocation().equals(target)).findFirst().get();
                    notifyPickupArrival(passenger);
                    destinations.removeIf(location -> location.equals(target));
                    // Si c'est la destination d'un seul passager on dépose les passagers
                } else if (passengers.stream().anyMatch(p -> p.getDestination().equals(getLocation()))) {
                    offloadPassenger();
                }
                clearTargetLocation();
                chooseTargetLocation();
            }
        } else {
            incrementIdleCount();
        }
    }

    /**
     * @return Whether or not this vehicle is free.
     */
    public boolean isFree() {

        return (passengers.size() < MAXPASSENGERS) ? true : false;
    }

    /**
     * Receive a pickup location.
     *
     * @location The pickup location.
     */
    public void setPickupLocation(Location location) {
        destinations.add(location);
        chooseTargetLocation();
    }

    /**
     * Receive a passenger.
     * Add the destination to the list.
     *
     * @param passenger The passenger.
     */
    public void pickup(Passenger passenger) {
        passengers.add(passenger);
        destinations.add(passenger.getDestination());
        chooseTargetLocation();
    }

    /**
     * Decide where to go next, based on the list of
     * possible destinations.
     */
    private void chooseTargetLocation() {
        Location target = (destinations.isEmpty() ? null : destinations.get(0));
        for (Location l : destinations) {
            if (l.distance(getLocation()) < target.distance(getLocation())) {
                target = l;
            }
        }
        setTargetLocation(target);
    }

    /**
     * Offload all passengers whose destination is the
     * current location.
     */
    public void offloadPassenger() {
        Iterator<Passenger> it = passengers.iterator();
        while (it.hasNext()) {
            Passenger p = it.next();
            if (p.getDestination().equals(getLocation())) {
                notifyPassengerArrival(p);
                it.remove();
            }
        }
        destinations.removeIf(location -> location.equals(getLocation()));
    }

    /**
     * @return The maximum number of passengers for this shuttle
     */
    public int getMaxPassengers() {
        return MAXPASSENGERS;
    }

    public int getPassengers() { return passengers.size();}

    @Override
    public Image getImage() {
        if (passengers.isEmpty()) return emptyImage;
        else return passengerImage;
    }
}
