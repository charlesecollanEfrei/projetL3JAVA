package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide a view of the vehicles and passengers in the city.
 *
 * @author David J. Barnes and Michael Kolling. Modified A. Morelle
 * @version 2013.12.30
 */
public class CityGUI extends JFrame implements Actor {
    private static final long serialVersionUID = 20131230;
    //public static JLabel missedPickUp = new JLabel("");
    private City city;
    private CityView cityView;

    private JLabel all_taxi;
    private JLabel taxi_libre;
    private JLabel all_shuttle;
    private JLabel shuttle_libre;
    private JLabel passenger_libre;

    private JPanel panelVehicle;
    private List<JLabel> vehicle_stats;
    /**
     * Constructor for objects of class CityGUI
     *
     * @param city : the city whose state is to be displayed.
     */
    public CityGUI(City city) {
    	
        // Create and set up the window
        super("Simulation of taxis operating on a city grid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Init attributes
        this.city = city;
        cityView = new CityView(city.getWidth(), city.getHeight());
        vehicle_stats = new ArrayList<>();

        // Create and set up the content pane
        createContentPane();

        // Size and display this frame
        displayGUI();
    }

    /**
     * Create and set up the content pane
     */
    private void createContentPane() {
        JPanel panelG = new JPanel();
        JPanel info = new JPanel();
        panelVehicle = new JPanel();
        info.setLayout(new GridLayout(0,2));
        panelVehicle.setLayout(new GridLayout(0,2));
        panelG.setLayout(new BorderLayout());


        info.add(new JLabel("Largeur de la grille : "));
        info.add(new JLabel(String.valueOf(city.getWidth())));
        info.add(new JLabel("Longueur de la grille : "));
        info.add(new JLabel(String.valueOf(city.getHeight())));

        info.add(new JLabel("Nombre de taxi(s) : "));
        all_taxi = new JLabel(String.valueOf(0));
        info.add(all_taxi);

        info.add(new JLabel("Nombre de taxi(s) libre(s) : "));
        taxi_libre = new JLabel(String.valueOf(0));
        info.add(taxi_libre);

        info.add(new JLabel("Nombre de bus : "));
        all_shuttle = new JLabel(String.valueOf(0));
        info.add(all_shuttle);

        info.add(new JLabel("Nombre de bus libre(s) : "));
        shuttle_libre = new JLabel(String.valueOf(0));
        info.add(shuttle_libre);

        info.add(new JLabel("Nombre de passager(s) libre(s) : "));
        passenger_libre = new JLabel(String.valueOf(0));
        info.add(passenger_libre);

        for(Item item : city.getItems()) {
            if(item instanceof Taxi) {
                JLabel lab = new JLabel("Idle Count du Taxi " + ((Taxi) item).getIdentifiant() + " : " + ((Taxi) item).getIdleCount());
                vehicle_stats.add(lab);
                panelVehicle.add(lab);
            }
            if(item instanceof Shuttle) {
                JLabel lab = new JLabel("Nombre de passagers du bus " + ((Shuttle) item).getIdentifiant() + " : " + ((Shuttle) item).getPassengers());
                vehicle_stats.add(lab);
                panelVehicle.add(lab);
            }
        }

        panelG.add(info, BorderLayout.NORTH);
        panelG.add(panelVehicle, BorderLayout.CENTER);
        //panelG.add(missedPickUp, BorderLayout.SOUTH);
        getContentPane().add(cityView, BorderLayout.CENTER);
        getContentPane().add(panelG, BorderLayout.NORTH);
        
     
        this.setMinimumSize(new Dimension(500,500));
        //this.setResizable(false);
        
    }

    /**
     * Size and display this frame
     */
    private void displayGUI() {
        // Set up an initial size (90% of the screen height)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //int frameHeight = (int) (screenSize.height * 0.90);
        //int frameWidth = frameHeight;
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        
        // Size this window to fit the preferred size and layouts
        // of its components
        pack();
        // Display
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Display the current state of the city.
     */
    public void act() {

        cityView.preparePaint();

        List<Item> items = city.getItems();
        for (Item item : items) {
            if (item instanceof DrawableItem) {
                DrawableItem it = (DrawableItem) item;
                Location location = it.getLocation();
                cityView.drawImage(location.getX(), location.getY(),
                        it.getImage());
            }
        }

        cityView.repaint();
        refresh_info();

    }
    private int getAllTaxi() {
        int taxi = 0;
        for(Item item : city.getItems()) {
            if(item instanceof Taxi) taxi++;
        }
        return taxi;
    }

    private int getFreeTaxi() {
        int taxi = 0;
        for(Item item : city.getItems()) {
            if(item instanceof Taxi)
                if(((Taxi) item).isFree()) taxi++;
        }
        return taxi;
    }

    private int getFreePassenger() {
        int passenger = 0;
        for(Item item : city.getItems()) {
            if(item instanceof Passenger) {
                passenger++;
            }
        }
        return passenger;
    }

    private int getAllShuttle() {
        int shuttle = 0 ;
        for(Item item : city.getItems()) {
            if(item instanceof Shuttle) {
                shuttle++;
            }
        }
        return shuttle;
    }

    private int getFreeShuttle() {
        int shuttle = 0;
        for(Item item : city.getItems()) {
            if(item instanceof Shuttle)
                if(((Shuttle) item).isFree())
                    shuttle++;
        }
        return shuttle;
    }

    private void refresh_info() {
        all_taxi.setText(String.valueOf(getAllTaxi()));
        taxi_libre.setText(String.valueOf(getFreeTaxi()));
        all_shuttle.setText(String.valueOf(getAllShuttle()));
        shuttle_libre.setText(String.valueOf(getFreeShuttle()));
        passenger_libre.setText(String.valueOf(getFreePassenger()));
        int i = 0;

        for(Item item : city.getItems()) {
            if(item instanceof Taxi) {
                vehicle_stats.get(i).setText("Idle Count du Taxi " + ((Taxi) item).getIdentifiant() + " : " + ((Taxi) item).getIdleCount());
                i++;
            }
            if(item instanceof Shuttle) {
                vehicle_stats.get(i).setText("Nombre de passagers du bus " + ((Shuttle) item).getIdentifiant() + " : " + ((Shuttle) item).getPassengers());
                i++;
            }
        }

        this.repaint();
    }


    /*************************************************************************/

    /**
     * Provide a graphical view of a rectangular city. This is a nested class (a
     * class defined inside a class) which defines a custom component for the
     * user interface. This component displays the city. This is rather advanced
     * GUI stuff - you can ignore this for your project if you like.
     */
    private class CityView extends JPanel {
        static final long serialVersionUID = 20131230;

        private final int VIEW_SCALING_FACTOR = 10;

        private int cityWidth, cityHeight;
        private int xScale, yScale; // panel size / city size
        private Dimension size;    // size of this panel
        private Graphics g;
        private Image cityImage;

        /**
         * Create a new CityView component.
         */
        public CityView(int cityWidth, int cityHeight) {
            this.cityWidth = cityWidth;
            this.cityHeight = cityHeight;
            setBackground(Color.white);
            size = new Dimension(0, 0);
        }

        public void preparePaint() {
            // If the size has changed...
            if (!size.equals(getSize())) {
                size = getSize();
                cityImage = cityView.createImage(size.width, size.height);
                g = cityImage.getGraphics();

                xScale = size.width / cityWidth;
                if (xScale < 1) {
                    xScale = VIEW_SCALING_FACTOR;
                }
                yScale = size.height / cityHeight;
                if (yScale < 1) {
                    yScale = VIEW_SCALING_FACTOR;
                }
            }

            // Draw the grid
            g.setColor(Color.white);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.gray);
            for (int i = 0, x = 0; x < size.width; i++, x = i * xScale) {
                g.drawLine(x, 0, x, size.height - 1);
            }
            for (int i = 0, y = 0; y < size.height; i++, y = i * yScale) {
                g.drawLine(0, y, size.width - 1, y);
            }
        }

        /**
         * Draw the image for a particular item.
         */
        public void drawImage(int x, int y, Image image) {
            g.drawImage(image, x * xScale + 1, y * yScale + 1,
                    xScale - 1, yScale - 1, this);
        }

        /**
         * The city view component needs to be redisplayed. Copy the internal
         * image to screen.
         */
        @Override
        public void paintComponent(Graphics g) {
            if (cityImage != null) {
                g.drawImage(cityImage, 0, 0, null);
            }
        }

    } // End internal class CityView

    /*************************************************************************/

} // End class CityGUI
