package VisualEngine.engineTester;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
* <p>
* Tests AWTGLCanvas functionality
* <p>
* @version $Revision: 3418 $
* @author $Author: spasi $
* $Id: AWTTest.java 3418 2010-09-28 21:11:35Z spasi $
*/
public class AWTTest extends Frame {

  /** AWT GL canvas */
  private AWTGLCanvas canvas0, canvas1;

  private  volatile float angle;

  /**
   * C'tor
   */
  public AWTTest() throws LWJGLException {
    setTitle("LWJGL AWT Canvas Test");
    setSize(640, 320);
    setLayout(new GridLayout(1, 2));
    add(canvas0 = new AWTGLCanvas() {
      int current_height;
      int current_width;
      public void paintGL() {
        try {
          if (getWidth() != current_width || getHeight() != current_height) {
            current_width = getWidth();
            current_height = getHeight();
            glViewport(0, 0, current_width, current_height);
          }
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
          glRotatef(angle, 0f, 0f, 1.0f);
          glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
          glPopMatrix();
          swapBuffers();
          repaint();
        } catch (LWJGLException e) {
          throw new RuntimeException(e);
        }
      }
    });
    add(canvas1 = new AWTGLCanvas() {
      int current_height;
      int current_width;
      public void paintGL() {
        try {
          angle += 1.0f;
          if (getWidth() != current_width || getHeight() != current_height) {
            current_width = getWidth();
            current_height = getHeight();
            glViewport(0, 0, current_width, current_height);
          }
          glViewport(0, 0, getWidth(), getHeight());
          glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
          glClear(GL_COLOR_BUFFER_BIT);
          glMatrixMode(GL_PROJECTION);
          glLoadIdentity();
          gluOrtho2D(0.0f, (float) getWidth(), 0.0f, (float) getHeight());
          glMatrixMode(GL_MODELVIEW);
          glPushMatrix();
          glTranslatef(getWidth() / 2.0f, getHeight() / 2.0f, 0.0f);
          glRotatef(2*angle, 0f, 0f, -1.0f);
          glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
          glPopMatrix();
          swapBuffers();
          repaint();
        } catch (LWJGLException e) {
          throw new RuntimeException(e);
        }
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });
    setResizable(true);
    setVisible(true);
  }

  public static void main(String[] args) throws LWJGLException {
    new AWTTest();
  }
}
