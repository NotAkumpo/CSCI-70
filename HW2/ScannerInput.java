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

public class ScannerInput {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\Z");
        String a = scanner.next();

        ScannerDFA myScanner = new ScannerDFA(a);

        try {
            File output = new File("output.txt");
            if (output.createNewFile()) {
                PrintWriter out = new PrintWriter("output.txt");

                //Output here, should still be formatted properly
                while( !myScanner.printStream().isEmpty() && myScanner.showError() ){
                    out.println(myScanner.printTokenLexeme());
                }
                out.close();
            }
            else {
                System.out.println("An output file already exists. Please delete the current output file.");
            }
        } 
        catch (IOException e){
            System.out.println("An error occurred.");
        }



    }

}
