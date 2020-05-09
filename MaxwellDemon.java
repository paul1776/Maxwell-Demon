import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math.*;

public class MaxwellDemon extends JFrame implements ActionListener {

    // Game Window
    JFrame gameWindow;

    // Buttons at top of view
    JPanel buttonPanel;
    JButton addButton;
    JButton resetButton;

    // Game panel and painted view in center of screen
    JPanel gamePanel;
    GameView game;

    // Temperature at bottom of view
    JPanel temperaturePanel;
    JLabel leftTemperature;
    JLabel rightTemperature;

    // Tallies number of times wall is opened
    int wallTimes;

    // Timer and click counter to manage ball movements and temperature math
    Timer clicky;
    int clicks = 0;

    // Balls instantiation
    FastBall[] fast;
    SlowBall[] slow;
    int fastCount;
    int slowCount;

    // Timer multiple for clicker and ball math
    double deltat = 0.1; //  in seconds

    // MaxwellDemon default constructor
    public MaxwellDemon() {
        gameWindow = new JFrame("Maxwell's Demon");
        gameWindow.setSize(1000, 600);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Walls opened tally set to 0
        wallTimes = 0;

        // Start timer
        clicky = new Timer((int)(1000 * deltat), this);
        clicky.start();

        // Instantiating balls and adding 4 balls to the frame
        fastCount = 0;
        slowCount = 0;
        fast = new FastBall[1000];
        slow = new SlowBall[1000];
        addBalls();

        // Load gameView
        game = new GameView();
        gameWindow.add(game);

        // Create button panel at the top of the view
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GRAY);
        addButton = new JButton("Add Particles");
        resetButton = new JButton("Reset");
        addButton.addActionListener(this);
        resetButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);
        buttonPanel.setLayout(new GridLayout(1,2));
        gameWindow.add(buttonPanel, BorderLayout.PAGE_START);

        // Create temperature panel at the bottom of the view
        temperaturePanel = new JPanel();
        temperaturePanel.setBackground(Color.GRAY);
        leftTemperature = new JLabel("Left", SwingConstants.CENTER);
        rightTemperature = new JLabel("Right", SwingConstants.CENTER);
        leftTemperature.setFont(new Font("Courier", Font.BOLD, 18));
        rightTemperature.setFont(new Font("Courier", Font.BOLD, 18));
        temperaturePanel.add(leftTemperature);
        temperaturePanel.add(rightTemperature);
        temperaturePanel.setLayout(new GridLayout(1,2));
        gameWindow.add(temperaturePanel, BorderLayout.PAGE_END);

        // Add a mouse listener to open and close the wall
        gameWindow.addMouseListener(new MouseAdapter(){
            public void mousePressed( MouseEvent m ) {
                wallTimes++;
                game.openWall();
                game.repaint();
            }
            public void mouseReleased( MouseEvent m) {
                game.closeWall();
                game.repaint();
            }
        });

        // Set all changes to visible
        gameWindow.setVisible(true);
    }

    // GameView class to build the central game view
    public class GameView extends JComponent {
        // to track wall status
        private boolean haveWall = true;

        public void openWall() {
            haveWall = false;
        }

        public void closeWall() {
            haveWall = true;
        }

        public boolean getWallStatus() {
            return haveWall;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g); // call to JFrame paintComponent()

            // Creates central green rectangle
            g.setColor(Color.green);
            g.fillRect(50, 80, 900, 440);

            // Creates the two room labels
            g.setColor(Color.black);
            g.drawString("Left Room", 50, 75);
            g.drawString("Right Room", 879, 75);

            // Draws wall
            if (getWallStatus()) {
                g.setColor(Color.black);
                g.drawLine(499, 0, 499, 600);
                g.drawLine(500, 0, 500, 600);
                g.drawLine(501, 0, 501, 600);
            }

            // Draws abcense of wall
            if (!getWallStatus()) {
                g.drawLine(499, 0, 499, 80);
                g.drawLine(500, 0, 500, 80);
                g.drawLine(501, 0, 501, 80);
                g.drawLine(499, 520, 499, 600);
                g.drawLine(500, 520, 500, 600);
                g.drawLine(501, 520, 501, 600);

                g.setColor(Color.green);
                g.drawLine(499, 80, 499, 520);
                g.drawLine(500, 80, 500, 520);
                g.drawLine(501, 80, 501, 520);
            }

            // Draws balls
            for ( int i=0; i<slowCount; i++) {
                slow[i].drawMe(g);
                fast[i].drawMe(g);
            }
        }
    }

    // Ball class to handle the balls on screen
    public class Ball
    {
        double x, y;
        double vx, vy;
        double oldx, oldy;

        public Ball( int x1, int y1 )
        {
            x = x1; y = y1;
            vx = Math.random() * 100 - 50;
            vy = Math.random() * 100 - 50;
        }

        public Ball()
        {
            x = Math.random() * 400 + 100;
            y = Math.random() * 400 + 100;
            vx = Math.random() * 100 - 50;
            vy = Math.random() * 100 - 50;
        }

        public double getX() {
            return x;
        }

        public void move( double deltat )
        {
            oldx = x; oldy = y;
            x += vx * deltat;
            y += vy * deltat;
            stayOnScreen();
        }

        public void stayOnScreen(){
            if (game.getWallStatus()) {
                if (x < 55) { vx *= -1; }
                if (y < 85) { vy *= -1; }
                if ((x > 495) && (x < 505)) { vx *= -1; }
                if (x > 895) { vx *= -1; }
                if (y > 515) { vy *= -1; }
            }

            if (!game.getWallStatus()) {
                if (x < 55) { vx *= -1; }
                if (y < 85) { vy *= -1; }
                if (x > 895) { vx *= -1; }
                if (y > 515) { vy *= -1; }
            }
        }

        public void drawMe( Graphics g ) { }
    }

    // FastBall inherits Ball, creates the fast balls
    public class FastBall extends Ball {
        FastBall() {
            super();
        }

        FastBall(int x, int y) {
            super(x, y);
        }

        public void drawMe ( Graphics g )
        {
            g.setColor( Color.RED );
            g.fillOval( (int)(x-2), (int)(y-2), 5, 5 );
        }
    }

    // SlowBall inherits Ball, creates the slow balls
    public class SlowBall extends Ball {
        SlowBall() {
            super();
        }

        SlowBall(int x, int y) {
            super(x, y);
        }

        public void drawMe (Graphics g)
        {
            g.setColor( Color.BLUE );
            g.fillOval( (int)(x-2), (int)(y-2), 5, 5 );
        }
    }

    // Moves all the balls
    public void moveAll()
    {
        for (int i=0; i<slowCount; i++ ) {
            slow[i].move(deltat);
            fast[i].move(deltat*2);
        }
    }

    // Adds Balls in the center of each room and randomizes direction of travel
    public void addBalls()
    {
        fast[fastCount++] = new FastBall(250, 300);
        fast[fastCount++] = new FastBall(750, 300);

        slow[slowCount++] = new SlowBall(250, 300);
        slow[slowCount++] = new SlowBall(750, 300);
    }

    // computes the temperature of each room
    public double[] getTemperatures() {
        double leftTemp;
        double rightTemp;
        double[] temperatures = new double[2];

        int slowLeft = 0;
        int fastLeft = 0;
        int slowRight = 0;
        int fastRight = 0;

        for (int i = 0; i < fastCount; i++) {
            if (fast[i].getX() < 500) { fastLeft++; }
            else { fastRight++; }
        }

        for (int i = 0; i < slowCount; i++) {
            if (slow[i].getX() < 500) { slowLeft++; }
            else { slowRight++; };
        }

        leftTemp = 3 * fastLeft + 1 * slowLeft;
        rightTemp = 3 * fastRight + 1 * slowRight;

        temperatures[0] = leftTemp;
        temperatures[1] = rightTemp;

        return temperatures;
    }

    // Updates the two temperature JLabel at the bottom of the view with the appropriate values
    public void setTemperatures() {
        double[] temperatures = getTemperatures();
        leftTemperature.setText(String.valueOf(temperatures[0]) + " °");
        rightTemperature.setText(String.valueOf(temperatures[1]) + " °");
    }

    // Computes the response to the timer and the two buttons at the top of the view
    @Override
    public void actionPerformed( ActionEvent e )
    {
        // moves all balls with each click
        // To save compute, calculates temperature every 20 clicks
        if ( e.getSource()==clicky ) {
            moveAll();

            if (clicks == 0) { setTemperatures(); }
            else if (clicks > 20) { clicks = 0; }
            else { clicks++; }
        }
        else if ( e.getSource()==resetButton ) {
            double[] temperatures = getTemperatures();
            double temperatureDiff = Math.abs(temperatures[0] - temperatures[1]);

            JOptionPane.showMessageDialog(null, "The wall was closed " + wallTimes + " times.\n" + "The final " +
                            "difference in temperature was " + temperatureDiff,
                    "Maxwell's Demon Score", JOptionPane.INFORMATION_MESSAGE);

            FastBall[] reset_fast = new FastBall[1000];
            SlowBall[] reset_slow = new SlowBall[1000];

            wallTimes = 0;

            fast = reset_fast;
            fastCount = 0;
            slow = reset_slow;
            slowCount = 0;

            addBalls();
        }
        else if ( e.getSource()==addButton ) {
            addBalls();
        }
        game.repaint();
    }

    // Main function for the Maxwell Class. Creates an instance.
    public static void main(String[] args) {
        MaxwellDemon test = new MaxwellDemon();
    }
}