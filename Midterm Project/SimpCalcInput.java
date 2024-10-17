// HOW TO RUN THIS PROGRAM
// STEP 1: OPEN THE TERMINAL IN THIS PROGRAM'S FOLDER. TYPE CMD IN THE FILE LOCATION.
// STEP 2: COMPILE THE PROGRAM. USE THE COMMAND: "javac *.java"
// STEP 3: RUN THE PROGRAM WITH YOUR DESIRED INPUT. USE THE COMMAND "java SimpCalcInput input.txt < input.txt"
// STEP 3.5: MAKE SURE TO TYPE THE COMMAND THIS WAY SINCE THE FIRST input.txt IS USED FOR THE OUTPUT FILES' NAMES 
// AND THE SECOND input.txt GIVES THE INPUT ITSELF. IF THERE IS AN ALREADY EXISTING SCANNER OR PARSER OUTPUT FOR 
// THE INPUT FILE'S NAME, IT WILL PROMPT TO DELETE THESE FILES TO AVOID DUPLICATES.
// STEP 4: THE SCANNER WILL OUTPUT TO A FILE NAMED [Input file name without .txt]scan.txt AND THE PARSER WILL
// OUTPUT TO A FILE NAMED [Input file name without .txt]parse.txt.

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class SimpCalcInput {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); //Create a scanner (this scanner is different from the DFA Scanner) to read input from the console
        scanner.useDelimiter("\\Z"); //setting the delimiter to read the entire input
        if (args.length == 0) {
            System.err.println("Please provide the input file name.");
            return;
        }

        //Some variables for the names of the output files
        String fileName = args[0];
        String newFileName = fileName.substring(0, fileName.length()-4);
        String scannerFileName = newFileName + "scan.txt";
        String parserFileName = newFileName + "parse.txt";
        String a = scanner.next(); //store the input into a string variable called 'a'

        SimpCalcScanner myScanner = new SimpCalcScanner(a); //creating a new SimpCalcScanner object that makes use of the input string
        SimpCalcParser myParser;

        try {
            File output = new File(scannerFileName); //creates a new file for the output from the scannerDFA
            if (output.createNewFile()) { //double checks if the file does not exist and makes a new file if needed
                PrintWriter out = new PrintWriter(scannerFileName);

                //the program will continue printing as long as the printStream is not empty and no errors in the scannerDFA occur
                while( !myScanner.printStream().isEmpty() && myScanner.showError() ){
                    out.println(myScanner.gettoken()); //writes the token-lexeme pair to the output file
                }

                //Instantiation of the Parser, token types produced by the Scanner is passed as a parameter
                myParser = new SimpCalcParser(myScanner.getTokenIDs(), parserFileName, fileName);

                out.close(); //close the PrintWriter after the writing is over

                //Prg routine is called to start the Parsing
                myParser.Prg();
            }
            else {
                System.out.println("Output files of that file name already exists. Please delete those current output files."); //if another output file exists, tells the user to delete it first 
            }
            
        } 
        catch (IOException e){
            System.out.println("An error occurred."); //catches any errors and displays an error message on the console
        }

    }
}
