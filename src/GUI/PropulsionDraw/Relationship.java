package GUI.PropulsionDraw;




public class Relationship {

    private BoxElement parent;
    private BoxElement child;

    public Relationship(BoxElement parent, BoxElement child) {
        this.parent = parent;
        this.child = child;
    }

    public BoxElement getChild() {
        return child;
    }

    public BoxElement getParent() {
        return parent;
    }

}