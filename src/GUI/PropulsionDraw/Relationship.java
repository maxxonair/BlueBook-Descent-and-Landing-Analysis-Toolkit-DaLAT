package GUI.PropulsionDraw;


import javax.swing.JComponent;


public class Relationship {

    private JComponent parent;
    private JComponent child;

    public Relationship(JComponent parent, JComponent child) {
        this.parent = parent;
        this.child = child;
    }

    public JComponent getChild() {
        return child;
    }

    public JComponent getParent() {
        return parent;
    }

}