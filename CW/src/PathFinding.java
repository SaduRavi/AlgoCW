/**
 * Name :Sadurshan Ravindran
 * IIT ID: 20200596
 * UOW ID : w1833588
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PathFinding {
    private static final String ANSI_RED = "\u001B[31m";  //color code red
    public static final String ANSI_RESET = "\u001B[0m";  //color code to reset
    private final char[][] puzzle;  //the puzzle array
    private Positions startPos;     //starting position
    private Positions endPos;       //final destination in the puzzle
    private final String fileName;
    private static long startTimeOneByOnePuzzle;
    private static long endTimeOneByOnePuzzle;
    /**
     * construct
     * @param puzzle the user input puzzle
     */
    public PathFinding(char[][] puzzle, String fileName) {
        this.puzzle = puzzle;
        this.fileName = fileName;
    }

    /**
     * Method to find the starting position from the puzzle
     */
    private void findStartPosition() {
        for( int i = 0 ; i<puzzle.length; i++){
            for ( int j = 0; j < puzzle[i].length; j++){
                String start = "S";
                if (puzzle[i][j] == start.charAt(0)){
                    startPos = new Positions(i,j);
                }
            }
        }
    }

    /**
     * Method the find the end position from the puzzle
     */
    private void findEndPosition() {
        for( int i = 0 ; i<puzzle.length; i++){
            for ( int j = 0; j < puzzle[i].length; j++){
                String start = "F";
                if (puzzle[i][j] == start.charAt(0)){
                    endPos = new Positions(i,j);
                }
            }
        }
    }

    /**
     * Method to the find the path in the puzzle
     */
    public void findPath() throws IOException {
        startTimeOneByOnePuzzle = System.nanoTime();
        findStartPosition();    //finding the starting position
        findEndPosition();      //finding the ending position
        ArrayList<Positions> openSet = new ArrayList<>();       //to hold the nodes to be evaluated
        ArrayList<Positions> closeSet = new ArrayList<>();      //to hold the already evaluated nodes

        openSet.add(startPos);
        while (openSet.size() > 0) {
            Positions node = openSet.get(0);

            for (int i = 1; i < openSet.size(); i++) {
                if ((openSet.get(i).getFCost() < node.getFCost()) || (openSet.get(i).getFCost() == node.getFCost())) {
                    if (openSet.get(i).hCost < node.hCost)
                        node = openSet.get(i);
                }
            }

            openSet.remove(node);
            closeSet.add(node);

            if (node.getColumnNumber() == endPos.getColumnNumber() && node.getRowNumber() == endPos.getRowNumber()) {
                System.out.println();
                System.out.println("* * ONE BY ONE PATH FOUND * *");
                endTimeOneByOnePuzzle = System.nanoTime();
                displayShortestPath(node);
                break;
            }
            ArrayList<Positions> neighbours =  getNeighbours(node);

            for (Positions neighbour : neighbours) {
                if (closeSet.contains(neighbour)) {
                    continue;
                }

                int costValue = node.gCost + getDistanceBetweenNodes(node, neighbour);
                if (costValue < neighbour.gCost || !openSet.contains(neighbour)) {
                    neighbour.gCost = costValue;
                    neighbour.hCost = getDistanceBetweenNodes(neighbour, endPos);
                    neighbour.setParent(node);
                    neighbour.direction = getMovingDirection(node,neighbour);
                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }

        }
    }

    /**
     * Method to get the direction
     * @param node current position of the node
     * @param neighbour next position of the node
     * @return the direction of the node has moved from the previous node
     */
    private String getMovingDirection(Positions node, Positions neighbour) {
        String direction = "";
        if (neighbour.getColumnNumber() < node.getColumnNumber() && neighbour.getRowNumber() == node.getRowNumber()){
            direction = "LEFT";
        }
        if (neighbour.getColumnNumber() < node.getColumnNumber() && neighbour.getRowNumber() < node.getRowNumber()){
            direction = "TOP LEFT";
        }
        if (neighbour.getColumnNumber() > node.getColumnNumber() && neighbour.getRowNumber() == node.getRowNumber()){
            direction = "RIGHT";
        }
        if (neighbour.getColumnNumber() > node.getColumnNumber() && neighbour.getRowNumber() < node.getRowNumber()){
            direction = "TOP RIGHT";
        }
        if (neighbour.getRowNumber() > node.getRowNumber() && neighbour.getColumnNumber() == node.getColumnNumber()){
            direction = "DOWN";
        }
        if (neighbour.getColumnNumber() < node.getColumnNumber() && neighbour.getRowNumber() > node.getRowNumber()){
            direction = "BOTTOM LEFT";
        }
        if (neighbour.getRowNumber() < node.getRowNumber() && neighbour.getColumnNumber() == node.getColumnNumber()){
            direction = "UP";
        }
        if (neighbour.getColumnNumber() > node.getColumnNumber() && neighbour.getRowNumber() > node.getRowNumber()){
            direction = "BOTTOM RIGHT";
        }
        return direction;
    }
    /**
     * Method to display the path
     * @param endNode the final position used to back track to the start position to find the path
     */
    public void displayShortestPath(Positions endNode) throws IOException {
        FileWriter fw= null;
        try {
            fw = new FileWriter("src/Path.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Positions> path = new ArrayList<>();
        Positions currentNode = endNode;
        while (currentNode.getParent() != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }
        Collections.reverse(path);
        int count = 2;
        System.out.println("01. START AT (" + (startPos.getColumnNumber()+1) + "," + (startPos.getRowNumber()+1) + ")");
        fw.write("01. START AT (" + (startPos.getColumnNumber()+1) + "," + (startPos.getRowNumber()+1) + ")\n");
        for (Positions positions : path) {
            if (count < 10){
                System.out.println("0" + count + ". MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")");
                fw.write("0" + count + ". MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")\n");
            }
            else {
                System.out.println(count + ". MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")");
                fw.write(count + ". MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")\n");
            }
            count ++;
        }
        System.out.println(count + ". DONE!");
        fw.write(count + ". DONE!\n");
        double elapsedTime = (endTimeOneByOnePuzzle - startTimeOneByOnePuzzle);
        System.out.println("Elapsed time: " + elapsedTime + "ns");
        fw.write("Elapsed time: " + elapsedTime + "ns");
        fw.close();
        displaySolvedPuzzle(path);
    }
    /**
     * Method to display the solved puzzle
     * @param path all the positions the node has travelled
     */
    public void displaySolvedPuzzle(ArrayList<Positions> path){
        String[][] solvedPuzzle = new String[puzzle.length][puzzle.length];
        for( int i =0; i<solvedPuzzle.length; i++){
            for( int j =0; j<solvedPuzzle[i].length; j++) {
                solvedPuzzle[i][j] = String.valueOf(puzzle[i][j]);
            }
        }

        String pathSign = "*";
        for (Positions positions : path) {
            solvedPuzzle[positions.getRowNumber()][positions.getColumnNumber()] = ANSI_RED + pathSign.charAt(0) + ANSI_RESET;
        }
        String endSign = "F";
        solvedPuzzle[endPos.getRowNumber()][endPos.getColumnNumber()] = String.valueOf(endSign.charAt(0));

        System.out.println("* DISPLAYING FOUND PATH *");
        for (String[] chars : solvedPuzzle) {
            System.out.print("  ");
            for (String aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    /**
     * getting the distance between two nodes
     * @param nodeA first node
     * @param posTwo second node
     * @return the distance between the nodes
     */
    public int getDistanceBetweenNodes(Positions nodeA, Positions posTwo) {
        int distanceX = Math.abs(nodeA.getColumnNumber() - posTwo.getColumnNumber());
        int distanceY = Math.abs(nodeA.getRowNumber() - posTwo.getRowNumber());
        if (distanceX > distanceY)
            return 14*distanceY + 10* (distanceX-distanceY);
        return 14*distanceX + 10 * (distanceY-distanceX);
    }

    /**
     * Getting the neighbours of a node
     * @param node the node whose neighbours to be found
     * @return arraylist of all the neighbour nodes
     */
    public ArrayList<Positions> getNeighbours(Positions node) {
        ArrayList<Positions> neighbours = new ArrayList<>();

        int y = node.getRowNumber();
        int x = node.getColumnNumber();
        String rocks = "0";

        if(fileName.charAt(0) == 'p'){
            for (int xValue = 0; xValue <= 1; xValue++) {
                for (int yValue = -1; yValue <= 1; yValue++) {
                    if (xValue == 0 && yValue == 0)
                        continue;

                    int checkX = node.getColumnNumber() + xValue;
                    int checkY = node.getRowNumber() + yValue;

                    if (checkX > 0 && checkX < puzzle.length-1 && checkY > 0 && checkY < puzzle.length-1 && puzzle[checkY][checkX] != '0') {
                        neighbours.add(new Positions(checkY,checkX));
                    }
                }
            }
        }
        else {
            int nXRight = x + 1;
              if(!(nXRight > puzzle.length-1)){
                if(puzzle[y][nXRight] != rocks.charAt(0)){
                    Positions pos = new Positions(y,nXRight);
                    neighbours.add(pos);
                }
            }

            int nXLeft = x - 1;
            if(!(nXLeft < 0)){
                if(puzzle[y][nXLeft] != rocks.charAt(0)){
                    Positions pos = new Positions(y,nXLeft);
                    neighbours.add(pos);
                }
            }

            int nYTop = y - 1;
            if(!(nYTop < 0)){
                if(puzzle[nYTop][x] != rocks.charAt(0)){
                    Positions pos = new Positions(nYTop,x);
                    neighbours.add(pos);
                }
            }

            int nYDown = y + 1;
            if(!(nYDown > puzzle.length-1)){
                if(puzzle[nYDown][x] != rocks.charAt(0)){
                    Positions pos = new Positions(nYDown,x);
                    neighbours.add(pos);
                }
            }
        }
        return neighbours;
    }

}
