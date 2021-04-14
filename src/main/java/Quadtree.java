import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Quadtree {
    private int MAX_OBJECTS = 10; //how many objects a node can hold before it splits
    private int MAX_LEVELS = 5; //deepest level subnode

    private int level; //current node level (0 is the topmost level)
    private ArrayList<Rectangle> objects;
    private Rectangle bounds; //2D space that the node occupies
    private Quadtree[] nodes; //four subnodes

    public Quadtree(int pLevel, Rectangle pBounds){
        level = pLevel;
        objects = new ArrayList<Rectangle>();
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


    //INSERT THE OBJECT INTO THE QUADTREE
    //If the node exceeds the capacity it will split and add all the objects to their corresponding nodes
    public void insert(Rectangle pRect) {
        //does this current node have nodes? If yes:
        if (nodes[0] != null){
            int index = getIndex(pRect);
            //gets the appropriate index using the position of the rect
            if (index != -1){
                nodes[index].insert(pRect);
                //inserts the object in the node at this index, so it's now in the node tree in the correct node
                return;
            }
        }
        //if there are no child nodes or the object doesn't fit into a child node (goes over the bounds borders)
        //adds pRect into the objects list for the current (parent) node
        objects.add(pRect);

        //has the node overflown with objects?
        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS){
            if (nodes[0] == null){
                //does this node have subnodes? if not split into subnodes
                split();
            }

            int i=0;
            while (i < objects.size()){
                //loop through all the objects in the list and get the index of each
                int index = getIndex(objects.get(i));
                //if index is not -1, insert the object into the new node and remove form objects list in current node
                //so any objects that can fit completely into the child nodes are put there
                //and any that cant (they cross over the bounds borders) stay in the current (parent) node
                if (index != 1) {
                    nodes[index].insert(objects.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }


    //RETURN ALL OBJECTS IN ALL NODES THAT COULD COLLIDE WITH THE GIVEN OBJECT
    public ArrayList<Rectangle> retrieve(ArrayList<Rectangle> returnObjects, Rectangle pRect){
        int index = getIndex(pRect);
        //if we have child nodes and the pRect can fit completely into one of them:
        if(index != -1 && nodes[0] != null){
            //go to the node at that index and call the function again
            //this keeps going until there are no more nodes or the index is -1 (so it is overlapping the bounds)
            nodes[index].retrieve(returnObjects, pRect);
        }

        //when index is -1 or there are no child nodes, all the objects at this node are added to returnObjects
        returnObjects.addAll(objects);

        return returnObjects;
    }





}
