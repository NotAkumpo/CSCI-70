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
                    out.println();
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
