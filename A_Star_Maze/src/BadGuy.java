import java.awt.Graphics;
import java.awt.Image;
import java.util.*;

public class BadGuy {
    Image myImage;
    int xPos, yPos;
    boolean hasPath=false;
    Node nodeMap[][] = new Node[40][40];
    Stack path = null;
    LinkedList openList = null;

    public BadGuy( Image i ) {
        myImage=i;
        xPos = 30;
        yPos = 10;
        path = new Stack();
        openList = new LinkedList();
        for(int x=0; x<40; x++){
            for(int y=0; y<40; y++){
                nodeMap[x][y] = new Node();
                nodeMap[x][y].x = x;
                nodeMap[x][y].y = y;
            }
        }
    }

    public int getDistance(int x1, int y1, int x2, int y2){
        return Math.abs(x1-x2)+Math.abs(y1-y2);
    }

    public void reCalcPath(boolean map[][],int targx, int targy) {
        for(int x=0; x<40; x++){
            for(int y=0; y<40; y++){
                nodeMap[x][y].isOpen = false;
                nodeMap[x][y].isClosed = map[x][y];
            }
        }

        openList.clear();
        path.clear();

        nodeMap[xPos][yPos].isOpen = true;
        nodeMap[xPos][yPos].g = 0;
        nodeMap[xPos][yPos].h = getDistance(xPos,yPos,targx,targy);
        nodeMap[xPos][yPos].f = nodeMap[xPos][yPos].g+nodeMap[xPos][yPos].h;
        openList.add(nodeMap[xPos][yPos]);

        boolean finished = false;

        while(!openList.isEmpty()){
            Node current = null;
            for (Object o : openList){
                Node n = (Node)o;
                if (current==null || current.f>n.f)
                    current = n;
            }

            if(current!=null){
                neighbourLoop:
                for(int xx=-1;xx<=1;xx++){
                    for(int yy=-1;yy<=1;yy++){
                        int xxx=current.x+xx, yyy=current.y+yy;
                        if(xxx>=0 && xxx<40 && yyy>=0 && yyy<40){
                            if(!nodeMap[xxx][yyy].isClosed && !nodeMap[xxx][yyy].isOpen){
                                nodeMap[xxx][yyy].isOpen=true;
                                openList.add(nodeMap[xxx][yyy]);
                                nodeMap[xxx][yyy].parentx = current.x;
                                nodeMap[xxx][yyy].parenty = current.y;
                                nodeMap[xxx][yyy].g = 1 + current.g;
                                nodeMap[xxx][yyy].h = getDistance(xxx,yyy,targx,targy);
                                nodeMap[xxx][yyy].f = nodeMap[xxx][yyy].g + nodeMap[xxx][yyy].h;
                                if(xxx==targx && yyy==targy){
                                    finished=true;
                                    break neighbourLoop;
                                }
                            }
                        }
                    }
                }
                current.isOpen = false;
                current.isClosed = true;
                openList.remove(current);
            }
        }

        if(finished){
            int x = targx, y = targy;
            while(x!= xPos || y!=yPos){
                path.push(nodeMap[x][y]);
                int parentx = nodeMap[x][y].parentx;
                int parenty = nodeMap[x][y].parenty;
                x = parentx;
                y = parenty;
            }
            hasPath = true;
        }
        else {
            hasPath = false;
        }
    }

    public void move(boolean map[][],int targx, int targy) {
        if (hasPath) {
            Node nextNode = (Node)path.pop();
            if(path.size()==0)
                hasPath = false;
            xPos = nextNode.x;
            yPos = nextNode.y;
        }
        else {
            // no path known, so just do a dumb 'run towards' behaviour
            int newx=xPos, newy=yPos;
            if (targx<xPos)
                newx--;
            else if (targx>xPos)
                newx++;
            if (targy<yPos)
                newy--;
            else if (targy>yPos)
                newy++;
            if (!map[newx][newy]) {
                xPos=newx;
                yPos=newy;
            }
        }
    }

    public void paint(Graphics g) {
        g.drawImage(myImage, xPos*20, yPos*20, null);
    }
}