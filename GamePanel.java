package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{

    public static int DELAY = 200; //stala dla timera
    Timer timer;
    Random random;
    int SCREEN_WIDTH = 800; // szerokosc pola gry
    int SCREEN_HEIGHT = 500; //wysokosc pola gry
    int UNIT_SIZE = 50; //jednostka pola gry
    int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE); //liczba jednostek pola gry
    int bodyParts = 4; // dlugosc wegorza
    int foodEaten = 0; // wynik zdobytego jedzenia
    int foodX; // wspolrzedne horyzontalne jedzenia
    int foodY; // wspolrzedne wertykalne jedzenia
    int x[] = new int[GAME_UNITS]; //tablica wspolrzednych horyzontalnych gry
    int y[] = new int[GAME_UNITS]; //tablica wspolrzednych wertykalnych gry
    char direction = 'R'; //kierunek
    boolean running = false; //ruch wegorza


    GamePanel(){


        random = new Random();
        this.setBounds(0,100,800,500);
        this.setBackground(Color.cyan);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    /** Metoda rozpoczynajaca gre */
    public void startGame() {

        newFood();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }


    /**
     * Metoda wykorzystujaca biblioteke graficzna
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }


    /**
     * Metoda rysujaca wegorza, jedzenie i wypisujaca wynik
     * @param g
     */
    public void draw(Graphics g) {

        if(running) {




            g.setColor(Color.blue);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
            for(int i = 0; i< bodyParts;i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(60,180,120));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.black);
            g.setFont( new Font("Serif",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    /** Metoda tworzaca nowe jedzenie */
    public void newFood(){
        foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        for(int i = bodyParts;i>0;i--)
        if((foodX == x[i])&& (foodY == y[i])){
            foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
            foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        }
    }
    /** Metoda definiujaca ruch obiektu */
    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    /** Metoda sprawdzajaca czy jedzenie zostalo zjedzone */
    public void checkFood() {
        if((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }
    /** Metoda sprawdzajaca kolizje z ogonem i scianami */
    public void checkCollisions() {
        //  kolizja glowy wegorza z tulowiem
        for(int i = bodyParts;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }
        //  kolizja glowy z lewa krawedzia mapy
        if(x[0] < 0) {
            running = false;
        }
        //  kolizja glowy z prawa krawedzia mapy
        if(x[0] > SCREEN_WIDTH-UNIT_SIZE) {
            running = false;
        }
        //  kolizja glowy z gora mapy
        if(y[0] < 0) {
            running = false;
        }
        //  kolizja glowy z dolem mapy
        if(y[0] > SCREEN_HEIGHT-UNIT_SIZE) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    /**
     * Metoda konczaca gre
     * @param g
     */
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.black);
        g.setFont( new Font("Serif",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.black);
        g.setFont( new Font("Serif",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    /**
     * Metoda okreslajaca czy obiekt sie porusza
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        /**
         * Metoda usprawniajaca sterowanie obiektem
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        try { Thread.sleep(20);}
                        catch (InterruptedException p) {
                            p.printStackTrace();}
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        try { Thread.sleep(20);}
                        catch (InterruptedException p) {
                            p.printStackTrace();}
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        try { Thread.sleep(20);}
                        catch (InterruptedException p) {
                            p.printStackTrace();}
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        try { Thread.sleep(20);}
                        catch (InterruptedException p) {
                            p.printStackTrace();}
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
