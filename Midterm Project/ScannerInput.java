//HOW TO RUN THIS PROGRAM
//STEP 1: OPEN THE TERMINAL IN THIS PROGRAM'S FOLDER. TYPE CMD IN THE FILE LOCATION.
//STEP 2: COMPILE THE PROGRAM. USE THE COMMAND: "javac *.java"
//STEP 3: RUN THE PROGRAM WITH YOUR DESIRED INPUT. USE THE COMMAND "java ScannerInput < input.txt"
//STEP 3.5: YOU CAN ALSO MANUALLY TYPE YOUR INPUT BUT YOU'LL HAVE TO DO SO IN ONE GO SO THIS IS NOT ADVISED
//STEP 3.6: MAKE SURE TO RUN THE PROGRAM ONLY WHEN output.txt DOES NOT EXIST IN THE SAME FOLDER. IF YOU DO,
//YOU'LL BE PROMPTED TO DELETE THE CURRENT output.txt
//STEP 4: THE DFA WILL OUTPUT TO A TEXT FILE CALLED output.txt

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class ScannerInput {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); //Create a scanner (this scanner is different from the DFA Scanner) to read input from the console
        scanner.useDelimiter("\\Z"); //setting the delimiter to read the entire input 
        String a = scanner.next(); //store the input into a string variable called 'a'

        SimpCalcScanner myScanner = new SimpCalcScanner(a); //creating a new SimpCalcScanner object that makes use of the input string

        try {
            File output = new File("output.txt"); //creates a new file for the output from the scannerDFA
            if (output.createNewFile()) { //double checks if the file does not exist and makes a new file if needed
                PrintWriter out = new PrintWriter("output.txt");

                //the program will continue printing as long as the printStream is not empty and no errors in the scannerDFA occur
                while( !myScanner.printStream().isEmpty() && myScanner.showError() ){
                    out.println(myScanner.printTokenLexeme()); //writes the token-lexeme pair to the output file
                }
                printTokens(myScanner.getTokenIDs(), out);
                out.close(); //close the PrintWriter after the writing is over
            }
            else {
                System.out.println("An output file already exists. Please delete the current output file."); //if another output file exists, tells the user to delete it first 
            }
            
        } 
        catch (IOException e){
            System.out.println("An error occurred."); //catches  any errors and displays an error message on the console
        }

    
    }

    private static void printTokens(ArrayList<String> tokenIDs, PrintWriter out) {
        // Format and print to the output file
        out.print("[ ");
        for (int i = 0; i < tokenIDs.size(); i++) {
            out.print("\"" + tokenIDs.get(i) + "\"");
            if (i < tokenIDs.size() - 1) {
                out.print(", ");
            }
        }
        out.println(" ]");
    }

}
