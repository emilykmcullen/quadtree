import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Quadtree {
    private int MAX_OBJECTS = 10; //how many objects a node can hold before it splits
    private int MAX_LEVELS = 5; //deepest level subnode

    private int level; //current node level (0 is the topmost level)
    private ArrayList objects;
    private Rectangle bounds; //2D space that the node occupies
    private Quadtree[] nodes; //four subnodes

    public Quadtree(int pLevel, Rectangle pBounds){
        level = pLevel;
        objects = new ArrayList();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    //the objects the quadtree will hold are rectangles

    
}
