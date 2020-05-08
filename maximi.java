import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class maximi extends JFrame implements ActionListener {

    int wallTimes;

    JButton addButton;
    JButton resetButton;
    JPanel gamePanel;
    JPanel buttonPanel;
    JFrame gameWindow;

    GameView gamePaint;

    Timer clicky;
    FastBall[] fast;
    SlowBall[] slow;
    int fastCount;
    int slowCount;
    int totalCount;
    double deltat = 0.1; //  in seconds

    public maximi() {
        gameWindow = new JFrame("Maxwell's Demon");
        gameWindow.setSize(1000, 600);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        wallTimes = 0;
        fastCount = 0;
        slowCount = 0;
        fast = new FastBall[1000];
        slow = new SlowBall[1000];

        addBalls();

        clicky = new Timer((int)(1000 * deltat), this);
        clicky.start();

        gamePaint = new GameView();
        gameWindow.add(gamePaint);

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
        gameWindow.setVisible(true);

        gameWindow.addMouseListener(new MouseAdapter(){
            public void mousePressed( MouseEvent m ) {
                wallTimes++;
                gamePaint.openWall();
                gamePaint.repaint();
            }
            public void mouseReleased( MouseEvent m) {
                gamePaint.closeWall();
                gamePaint.repaint();
            }
        });
    }

    public class GameView extends JComponent {
        private boolean haveWall = true;

        public void changeWall() {
            haveWall = !haveWall;
        }

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

            g.setColor(Color.green);
            g.fillRect(50, 80, 900, 440);

            g.setColor(Color.black);

            g.drawString("Left Room", 50, 75);
            g.drawString("Right Room", 879, 75);

            if (getWallStatus()) {
                g.setColor(Color.black);
                g.drawLine(499, 0, 499, 600);
                g.drawLine(500, 0, 500, 600);
                g.drawLine(501, 0, 501, 600);
            }

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

            for ( int i=0; i<slowCount; i++) {
                slow[i].drawMe(g);
                fast[i].drawMe(g);
            }
        }
    }

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

        public void move( double deltat )
        {
            oldx = x; oldy = y;
            x += vx * deltat;
            y += vy * deltat;
            stayOnScreen();
        }

        public void stayOnScreen(){
            if (gamePaint.getWallStatus()) {
                if (x < 55) { vx *= -1; }
                if (y < 85) { vy *= -1; }
                if ((x > 495) && (x < 505)) { vx *= -1; }
                if (x > 895) { vx *= -1; }
                if (y > 515) { vy *= -1; }
            }

            if (!gamePaint.getWallStatus()) {
                if (x < 55) { vx *= -1; }
                if (y < 85) { vy *= -1; }
                if (x > 895) { vx *= -1; }
                if (y > 515) { vy *= -1; }
            }
        }

        public void drawMe( Graphics g ) { }
    }

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

    public void moveAll()
    {
        for (int i=0; i<slowCount; i++ ) {
            slow[i].move(deltat);
            fast[i].move(deltat*2);
        }
    }

    public void addBalls()
    {
        fast[fastCount++] = new FastBall(250, 300);
        fast[fastCount++] = new FastBall(750, 300);

        slow[slowCount++] = new SlowBall(250, 300);
        slow[slowCount++] = new SlowBall(750, 300);
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource()==clicky ) {
            moveAll();
        }
        else if ( e.getSource()==resetButton ) {
            int gameScore = 1;
            JOptionPane.showMessageDialog(null, "The wall was closed " + wallTimes + " times.\n" + "The final " +
                            "temperature was " + gameScore,
                    "Maxwell's Demon Score", JOptionPane.INFORMATION_MESSAGE);

            FastBall[] reset_fast = new FastBall[1000];
            SlowBall[] reset_slow = new SlowBall[1000];

            fast = reset_fast;
            slow = reset_slow;

            addBalls();
        }
        else if ( e.getSource()==addButton ) {
            addBalls();
        }
        gamePaint.repaint();
    }

    public static void main(String[] args) {
        maximi test = new maximi();
    }
}