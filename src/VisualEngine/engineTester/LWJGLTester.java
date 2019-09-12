package VisualEngine.engineTester;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.PixelFormat;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LWJGLTester {

  public static class CustomAWTGLCanvas extends AWTGLCanvas {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomAWTGLCanvas() throws LWJGLException {
      super(new PixelFormat(8, 8, 0, 4));
    }

    @Override
    public void paintGL() {
      try {
        glViewport(0, 0, getWidth(), getHeight());
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, (float) getWidth(), 0.0f, (float) getHeight());
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glColor3f(1f, 1f, 0f);
        glTranslatef(getWidth() / 2.0f, getHeight() / 2.0f, 0.0f);
        glRotatef(51f, 0f, 0f, 1.0f);
        glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
        glPopMatrix();
        swapBuffers();
      } catch (LWJGLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws LWJGLException {

    CustomAWTGLCanvas canvas = new CustomAWTGLCanvas();

    JPanel panel = new JPanel(new BorderLayout());
    panel.setMinimumSize(new Dimension(100, 100));
    panel.add(canvas);

    JFrame frame = new JFrame("AWTGLCanvas - multisampling");
    frame.setPreferredSize(new Dimension(640, 480));
    frame.add(panel);
    frame.pack();

    frame.setVisible(true);
  }
}