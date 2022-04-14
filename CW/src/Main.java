/**
 * Name :Sadurshan Ravindran
 * IIT ID: 20200596
 * UOW ID : w1833588
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<String> puzzleRows = new ArrayList<>();
    static int noOfRows = 0;
    static char[][] puzzle;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        System.out.println("Enter File Name");
        String fileName = input.next();
        File myObj = new File("benchmarks/" + fileName + ".txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            noOfRows++;
            puzzleRows.add(myReader.nextLine());
        }
        System.out.println("BELOW IS THE PUZZLE");
        createPuzzle();
        printPuzzle();

        if(checkValidDocument()){

            System.out.println("---------------------------------------------");
            System.out.println("SLIDING PATH");
            SlidePath slidePath = new SlidePath(puzzle);
            slidePath.findPath();
            System.out.println("---------------------------------------------");

            System.out.println();
            System.out.println();
            System.out.println();

            System.out.println("---------------------------------------------");
            System.out.println("ONE BY ONE PATH");

            PathFinding pathFinding = new PathFinding(puzzle,fileName);
            pathFinding.findPath();
            System.out.println("---------------------------------------------");
            System.exit(0);

        }
        else{
            System.out.println("THE DOCUMENT IS NOT VALID");
        }
    }
    /**
     * method to create the puzzle in a 2d array
     */
    public static void createPuzzle(){
        puzzle = new char[noOfRows][noOfRows];
        for( int i =0; i<puzzleRows.size(); i++){
            puzzle[i] = puzzleRows.get(i).toCharArray();
        }
    }

    /**
     * method to print the puzzle
     */
    public static void printPuzzle(){
        for (char[] chars : puzzle) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    /**
     * method to check in the document has a start point, end point!!
     */
    private static boolean checkValidDocument() {
        boolean hasStartPos = false;
        boolean hasEndPos = false;
        for (char[] chars : puzzle) {
            for (char aChar : chars) {
                if (aChar == 'S') {
                    hasStartPos = true;
                }
                if (aChar == 'F') {
                    hasEndPos = true;
                }
            }
        }
        return hasStartPos && hasEndPos;
    }

}
