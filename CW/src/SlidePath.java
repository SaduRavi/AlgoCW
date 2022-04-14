/**
 * Name :Sadurshan Ravindran
 * IIT ID: 20200596
 * UOW ID : w1833588
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SlidePath {
    private static final String ANSI_RED = "\u001B[31m";  // color code red
    public static final String ANSI_RESET = "\u001B[0m";  // color code to reset
    private final char[][] puzzle;                        // puzzle array
    private final boolean[][] visitedArray;               // array to hold the visited positions
    private Positions startPos;                           // starting position
    private Positions endPos;                             // ending position
    private final ArrayList<Positions> nodeQueue = new ArrayList<>();   //arraylist to hold the nodes to be evaluated
    private static long startTimeSlidingPuzzle;
    private static long endTimeSlidingPuzzle;
    /**
     * constructor
     * @param puzzle the puzzle
     */
    public SlidePath(char[][] puzzle){
        this.visitedArray = new boolean[puzzle.length][puzzle.length];
        this.puzzle = puzzle;
    }

    /**
     * Method to find the ending position
     */
    private void findEndPosition() {
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
     * Method to find the starting position
     */
    private void findStartPosition() {
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
     * MMethod to check if the position noe position is valid
     * @param row  row number of the node
     * @param column column number of the node
     * @return true or false according to the valid positioning of the node
     */
    public boolean checkValidPosition(int row, int column) {
        return row >= 0 && column >= 0
                && row < puzzle.length && column < puzzle[0].length
                && puzzle[row][column] != '0'
                && !visitedArray[row][column];
    }

    /**
     * Method to find sliding path in the puzzle
     */
    public void findPath() throws IOException {
        startTimeSlidingPuzzle = System.nanoTime();
        findStartPosition();    // identifying the starting position
        findEndPosition();      // identifying the ending position

        nodeQueue.add(startPos);    //adding the starting position to the queue to be evaluated
        visitedArray[startPos.getRowNumber()][startPos.getColumnNumber()] = true;   //setting the position true in the visited array
        Positions currentPosition;
        boolean pathFound = false;

        while (!nodeQueue.isEmpty()) {      //loop until all the nodes in the queue are evaluated
            currentPosition = nodeQueue.remove(0);  //removing the first node
            int rowNumberVisited = currentPosition.getRowNumber();  //getting the row number of that node
            int columnNumberVisited = currentPosition.getColumnNumber();    //getting the column number of that node

            if (puzzle[rowNumberVisited][columnNumberVisited] == 'F') { //if the row number and column number at the position F the path is found
                System.out.println("PATH FOUND");
                endTimeSlidingPuzzle = System.nanoTime();
                displayPath(currentPosition);   //displaying the path
                pathFound = true;
                break;
            }
            slideNode(currentPosition, -1, 0);      //sliding the node to the left
            slideNode(currentPosition, 1, 0);       //sliding the node to the right
            slideNode(currentPosition, 0, 1);       //sliding the node down
            slideNode(currentPosition, 0, -1);      //sliding the node up
        }

        if (!pathFound){ //if path not found
            System.out.println("COULD NOT FIND A PATH");
        }
    }

    /**
     * Method to slide the node
     * @param position the node to slide
     * @param x the increment of the x value
     * @param y the increment of the y value
     */
    public void slideNode(Positions position, int x, int y) {
        int row = position.getRowNumber();
        int column = position.getColumnNumber();

        while(true) {
            row += y;
            column += x;

            if (!checkValidPosition(row, column)) { //checking if the position is valid or not
                break;
            }

            if (puzzle[row][column] == 'F') {  //if the position is the end position
                Positions neighbourItem = new Positions(row, column); // create a position object and store the x and y values
                neighbourItem.setParent(position); // set the parent or the previous position of the new node
                nodeQueue.add(0, neighbourItem);    //add the node to the front of the queue
                neighbourItem.direction = getMovingDirection(position,neighbourItem);   //get the direction of the node movement
                visitedArray[row][column] = true; //set visited in the array true
                break;
            }

            int nextRow  = row + y;
            int nextColumn = column + x;
            // if the next position is a wall or a rock the position before is stored
            if ((nextRow < 0 || nextColumn < 0) || (nextRow >= puzzle.length || nextColumn >= puzzle.length) || (puzzle[nextRow][nextColumn] == '0')) {
                Positions neighbourItem = new Positions(row, column);
                neighbourItem.setParent(position);
                nodeQueue.add(neighbourItem);
                neighbourItem.direction = getMovingDirection(position,neighbourItem);
                visitedArray[row][column] = true;
                break;
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
        if (neighbour.getColumnNumber() < node.getColumnNumber()){
            direction = "LEFT";
        }
        if (neighbour.getColumnNumber() > node.getColumnNumber()){
            direction = "RIGHT";
        }
        if (neighbour.getRowNumber() > node.getRowNumber()){
            direction = "DOWN";
        }
        if (neighbour.getRowNumber() < node.getRowNumber()){
            direction = "UP";
        }
        return direction;
    }

    /**
     * Method to display the path
     * @param currentPosition the final position used to back track to the start position to find the path
     */
    private void displayPath(Positions currentPosition) throws IOException {
        FileWriter fw= null;
        try {
            fw = new FileWriter("src/SlidePath.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Positions> path = new ArrayList<>();
        Positions currentNode = currentPosition;
        while (currentNode.getParent() != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }
        Collections.reverse(path);
        System.out.println("01. START AT (" + (startPos.getColumnNumber()+1) + "," + (startPos.getRowNumber()+1) + ")");
        fw.write("01. START AT (" + (startPos.getColumnNumber()+1) + "," + (startPos.getRowNumber()+1) + ")\n" );
        int count = 2;
        for (Positions positions : path) {
            if (count < 10){
                System.out.println("0" + count  + ". " + "MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")");
                fw.write("0" + count  + ". " + "MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")\n");
            }
            else {
                System.out.println(count  + ". " + "MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")");
                fw.write(count  + ". " + "MOVE " + positions.direction + " TO (" + (positions.getColumnNumber()+1) + "," + (positions.getRowNumber()+1) + ")\n");
            }
            count++;
        }
        System.out.println(count + ". "  + "DONE!");
        fw.write(count + ". "  + "DONE!\n");
        double slidingElapsedTime = (endTimeSlidingPuzzle - startTimeSlidingPuzzle);
        System.out.println("Elapsed time: " + slidingElapsedTime + "ns");
        fw.write("Elapsed time: " + slidingElapsedTime + "ns");
        fw.close();
        System.out.println();
        displaySolvedPuzzle(path);
    }

    /**
     * Method to display the solved puzzle
     * @param path all the positions the node has travelled
     */
    public void displaySolvedPuzzle(ArrayList<Positions> path){
        System.out.println("* DISPLAYING FOUND PATH *");

        String[][] solvedPuzzle2 = new String[puzzle.length][puzzle.length];
        for( int i =0; i<solvedPuzzle2.length; i++){
            for( int j =0; j<solvedPuzzle2[i].length; j++) {
                solvedPuzzle2[i][j] = String.valueOf(puzzle[i][j]);
            }
        }

        String pathSign2 = "v<^>";

        path.add(0,startPos);
        for (int i = 0 ; i< path.size()-1; i++){
            if (path.get(i).getRowNumber() < path.get(i+1).getRowNumber()){
                solvedPuzzle2[path.get(i+1).getRowNumber()][path.get(i+1).getColumnNumber()] = ANSI_RED + pathSign2.charAt(0) + ANSI_RESET;
            }
            if (path.get(i).getColumnNumber() > path.get(i+1).getColumnNumber()){
                solvedPuzzle2[path.get(i+1).getRowNumber()][path.get(i+1).getColumnNumber()] = ANSI_RED + pathSign2.charAt(1) + ANSI_RESET;
            }
            if (path.get(i).getRowNumber() > path.get(i+1).getRowNumber()){
                solvedPuzzle2[path.get(i+1).getRowNumber()][path.get(i+1).getColumnNumber()] = ANSI_RED + pathSign2.charAt(2) + ANSI_RESET;
            }
            if (path.get(i).getColumnNumber() < path.get(i+1).getColumnNumber()){
                solvedPuzzle2[path.get(i+1).getRowNumber()][path.get(i+1).getColumnNumber()] = ANSI_RED + pathSign2.charAt(3) + ANSI_RESET;
            }
        }

        String endSign = "F";
        solvedPuzzle2[endPos.getRowNumber()][endPos.getColumnNumber()] = String.valueOf(endSign.charAt(0));
        for (String[] chars : solvedPuzzle2) {
            System.out.print("  ");
            for (String aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }
}
