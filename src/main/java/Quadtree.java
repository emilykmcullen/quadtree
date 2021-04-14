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

    //CLEAR THE QUADTREE
    public void clear(){
        objects.clear();

        for (int i=0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    //SPLIT THE NODE INTO 4 SUBNODES
    //Divides the nodes into 4 equal parts and initializes the subnodes with the new bounds
    private void split(){
        int subWidth = (int)(bounds.getWidth() / 2);
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        nodes[0] = new Quadtree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level+1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    //DETERMINE WHICH NODE WITHIN THE BOUNDS THE OBJECT BELONGS TO
    //-1 means the object cannot completely fit within a child node and is part of the parent node
    private int getIndex(Rectangle pRect) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth()/2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight()/2);

        //Object can fit completely within the top quadrants
        boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
        //Object can fit completely within the bottom quadrants
        boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

        //Object can fit within the left quadrants
        if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint){
            if (topQuadrant){
                index = 1; //top left quadrant
            }
            else if (bottomQuadrant){
                index = 2; //bottom left quadrant
            }
        }

        //Object can fit completely within the right quadrants
        if (pRect.getX() > verticalMidpoint){
            if (topQuadrant){
                index = 0; //top right quadrant
            }
            else if (bottomQuadrant){
                index = 3; //bottom right quadrant
            }
        }
        return index;
    }




}
