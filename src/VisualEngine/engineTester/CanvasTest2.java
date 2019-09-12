package VisualEngine.engineTester;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.*;

public class CanvasTest2
{
    public static void main(String args[])
    {
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("Java vendor: " + System.getProperty("java.vendor"));
        new CanvasTest2();//.startGame();
    }

    Frame frame = new Frame(getClass().getSimpleName());
    Canvas canvas = new Canvas();

    public CanvasTest2()
    {
        frame.setLayout(null);

        TextField field = new TextField("Hello World", 40);
        field.setBounds(20, 50, 100, 20);
        frame.add(field);

        Button button = new Button("Toggle LWJGL");
        button.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent evt) {
                if (gameState == 0) startGame(); else stopGame();
            }
        });
        button.setBounds(150, 50, 150, 20);
        frame.add(button);

        canvas.setBounds(0, 0, 640, 480);
        canvas.setFocusable(false);
        canvas.setBackground(Color.BLUE);
        frame.setBackground(Color.BLACK);
        frame.add(canvas);

        // this.pack();
        frame.setSize(640, 480);
        frame.setVisible(true);
    }

    Thread gameThread;
    volatile int gameState;

    void stopGame ()
    {
        gameState = 2;
        while (gameState == 2) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
        }
    }

    void startGame ()
    {
        gameState = 1;
        gameThread = new Thread() {
            @Override
            public void run() {
                try {
                    Display.setParent(canvas);
                    //Display.create(new PixelFormat().withBitsPerPixel(32).withDepthBits(24).withStencilBits(8));
                    Display.setDisplayMode(new DisplayMode(640, 480));
                    Display.create();

                    // init OpenGL
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0, 640, 0, 480, 1, -1);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);

                    while (gameState == 1) {
                        render();
                    }

                    Display.destroy();
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }

                gameState = 0;
            }
        };
        gameThread.setName("rendering");
        gameThread.start();
    }

    void render ()
    {
        // Clear the screen and depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // set the color of the quad (R,G,B,A)
        GL11.glColor3f(0.5f,0.5f,1.f);

        // draw quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(100,100);
        GL11.glVertex2f(100+200,100);
        GL11.glVertex2f(100+200,100+200);
        GL11.glVertex2f(100,100+200);
        GL11.glEnd();

        // Display.sync(45);
        Display.update();
    }
}