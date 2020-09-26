import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;

public class LifeApplication extends JFrame implements Runnable, MouseListener, MouseMotionListener {

    private BufferStrategy strategy;
    private Graphics offscreenBuffer;
    private boolean gameState[][][]= new boolean[40][40][2];
    private int gameStateFrontBuffer = 0;
    private boolean isGameRunning = false;
    private boolean initialised;
    String line = null;
    private String filename = "C:\\Users\\Conor\\Desktop\\life.txt";
    private String[][] arr = new String[40][40];
    private char[] chars = new char[1600];


    public LifeApplication()
    {
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int x = screensize.width/2 - 400;
        int y = screensize.height/2 - 400;
        setBounds(x,y,800,800);
        setVisible(true);
        this.setTitle("Conway's Game of Life");

        createBufferStrategy(2);
        strategy = getBufferStrategy();
        offscreenBuffer = strategy.getDrawGraphics();

        addMouseListener(this);
        addMouseMotionListener(this);

        for(x = 0; x < 40; x++)
        {
            for(y = 0; y < 40; y++)
            {
                gameState[x][y][0] = gameState[x][y][1] = false;
            }
        }

        Thread t = new Thread(this);
        t.start();

        initialised = true;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try{Thread.sleep(100);}
            catch(InterruptedException e){}

            if(isGameRunning)
                applyRules();

            this.repaint();
        }
    }

    private void applyRules()
    {
        int front = gameStateFrontBuffer;
        int back = (front +1)%2;

        for(int x = 0; x < 40; x++)
        {
            for(int y = 0; y < 40; y++)
            {
                int liveneighbours = 0;
                for(int xx = -1; xx <= 1; xx++)
                {
                    for(int yy = -1; yy <= 1; yy++)
                    {
                        if(xx!=0 || yy!=0)
                        {
                            int xxx = x+xx;
                            if(xxx < 0)
                                xxx=39;
                            else if(xxx > 39)
                                xxx = 0;
                            int yyy = y + yy;
                            if(yyy < 0)
                                yyy=39;
                            else if (yyy>39)
                                yyy=0;
                            if(gameState[xxx][yyy][front])
                                liveneighbours++;
                        }
                    }
                }

                if(gameState[x][y][front])
                {
                    if(liveneighbours < 2)
                        gameState[x][y][back] = false;
                    else if(liveneighbours < 4)
                        gameState[x][y][back] = true;
                    else
                        gameState[x][y][back] = false;
                }
                else
                {
                    if(liveneighbours == 3)
                        gameState[x][y][back] = true;
                    else
                        gameState[x][y][back] = false;
                }
            }
        }
        gameStateFrontBuffer = back;
    }

    private void randomiseGameState()
    {
        for(int x = 0; x < 40; x++)
        {
            for(int y = 0; y < 40; y++)
            {
                gameState[x][y][gameStateFrontBuffer] = (Math.random()< 0.25);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent m)
    {

    }

    @Override
    public void mouseEntered(MouseEvent m)
    {

    }

    @Override
    public void mouseExited(MouseEvent m)
    {

    }

    @Override
    public void mousePressed(MouseEvent m)
    {
        if(!isGameRunning)
        {
            int x = m.getX();
            int y = m.getY();
            if(x>=10 && x<=85 && y>=40 && y <= 70) {
                isGameRunning = true;
                return;
            }

            if(x>=110 && x<=215 && y>=40 && y <= 70) {
                randomiseGameState();
                return;
            }

            if(x>=300 && x<=385 && y>=40 && y <= 70)
            {
                // save data
                for(int i = 0; i < 40; i++)
                {
                    for(int j = 0; j < 40; j++)
                    {
                        if(gameState[i][j][gameStateFrontBuffer])
                        {
                            arr[i][j] = "1";
                        }
                        else{
                            arr[i][j] = "0";
                        }
                    }

                }

                try{
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                    for(int i = 0; i < 40; i++)
                    {
                        for(int j = 0; j < 40; j++)
                        {
                            writer.write(arr[i][j]);
                        }
                    }

                    writer.close();
                    System.out.println("Save Complete");
                }
                catch(IOException e){
                }

                return;
            }
            if(x>=400 && x<=475 && y>=40 && y <= 70)
            {
                // load data
                Scanner s;
                try {
                    s = new Scanner(new BufferedReader(new FileReader(filename)));
                    while (s.hasNext())
                    {
                        String str = s.next();
                        chars = str.toCharArray();
                        int n = 0;
                        for(int i = 0; i < 40; i++)
                        {
                            for(int j = 0; j < 40; j++)
                            {
                                if(chars[n] == '1'){
                                    gameState[i][j][gameStateFrontBuffer] = true;
                                }

                                else{
                                    gameState[i][j][gameStateFrontBuffer] = false;
                                }
                                n++;
                            }
                        }

                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }

        }

        int x = m.getX()/20;
        int y = m.getY()/20;

        gameState[x][y][gameStateFrontBuffer] = !gameState[x][y][gameStateFrontBuffer];

        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent m)
    {

    }

    public void paint(Graphics g)
    {
        g = offscreenBuffer;
        if(!initialised)
            return;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 800);

        g.setColor(Color.WHITE);
        for(int x = 0; x < 40; x++)
        {
            for(int y = 0; y < 40; y++)
            {
                if(gameState[x][y][gameStateFrontBuffer])
                    g.fillRect(x * 20, y * 20, 20, 20);
            }
        }

        if(!isGameRunning)
        {
            g.setColor(Color.GREEN);
            g.fillRect(10, 40, 70, 30);
            g.fillRect(110, 40, 100, 30);
            g.fillRect(300, 40, 70, 30);
            g.fillRect(400, 40, 70, 30);
            g.setFont(new Font("Times", Font.PLAIN, 24));
            g.setColor(Color.BLACK);
            g.drawString("Start", 22, 62);
            g.drawString("Random", 112, 62);
            g.drawString("Save", 307, 62);
            g.drawString("Load", 407, 62);
        }
        strategy.show();
    }

    @Override
    public void mouseDragged(MouseEvent m) {
        int x = m.getX()/20;
        int y = m.getY()/20;
        if(!gameState[x][y][gameStateFrontBuffer])
            gameState[x][y][gameStateFrontBuffer] = !gameState[x][y][gameStateFrontBuffer];

    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        LifeApplication w = new LifeApplication();
    }
}