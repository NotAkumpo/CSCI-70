import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SimpCalcParser {

    //fields
    private ArrayList<String> tokens; //Contains all the token types produced by the Scanner
    PrintWriter out; //Writes to output file
    String fileName; //For the message when Parser is successful

    //constructor
    //Constructor also creates the output file for the Parser
    public SimpCalcParser(ArrayList<String> tokens, String parserFileName, String fileName){
        this.fileName = fileName;
        this.tokens = tokens;
        try {
            File output = new File(parserFileName); 
            if (output.createNewFile()) { 
                out = new PrintWriter(parserFileName);
            }
            else {
                System.out.println("An output file for the parser already exists. Please delete this current file."); 
            }
        } 
        catch (IOException e){
            System.out.println("An error occurred.");
        }
    }

    //methods
    //Prg routine to start the Parser
    public void Prg(){
        Blk();
        if (tokens.get(0).equals("EndOfFile")){
            out.println(fileName + " is a valid SimpCalc Program.");
            out.close();
        }
        else {
            out.println("End of file expected.");
            Error();
        }
    }

    //Blk routine
    private void Blk(){
        if (tokens.get(0).equals("Identifier") || tokens.get(0).equals("Print") || tokens.get(0).equals("If")) {
            Stm();
            Blk();
        }
    }

    //Stm routine
    private void Stm(){
        //This is how tokens and sub-routines are handled for the entirety of the Parser
        if (tokens.get(0).equals("Identifier")){ //When a token is find it goes into an if statement
            tokens.remove(0); //The token then gets consumed and the subroutine moves on to the next token/sub-routine
            if (tokens.get(0).equals("Assign")){
                tokens.remove(0);
                Exp();
                if (tokens.get(0).equals("Semicolon")){
                    tokens.remove(0);
                    out.println("Assignment Statement Recognized.");
                }
                else {
                    out.println("Parse Error: Semicolon expected.");
                    Error();
                }
            }
            else { //Else statements are used to detect errors, ie. when a token is found when it's not in the sub-routine
                out.println("Parse Error: Assign expected."); 
                Error();
            }
        }
        else if (tokens.get(0).equals("Print")){
            tokens.remove(0);
            if (tokens.get(0).equals("LeftParen")){
                tokens.remove(0);
                Arg();
                ArgFollow();
                if (tokens.get(0).equals("RightParen")){
                    tokens.remove(0);
                    if (tokens.get(0).equals("Semicolon")){
                        tokens.remove(0);
                        out.println("Print Statement Recognized.");
                    }
                    else {
                        out.println("Parse Error: Semicolon expected.");
                        Error();
                    }
                }
                else {
                    out.println("Parse Error: RightParen expected.");
                    Error();
                }
            }
            else {
                out.println("Parse Error: LeftParen expected.");
                Error();
            }
        }
        else if (tokens.get(0).equals("If")){
            tokens.remove(0);
            out.println("If Statement Begins");
            Cnd();
            if (tokens.get(0).equals("Colon")){
                tokens.remove(0);
                Blk();
                Iffollow();
                out.println("If Statement Ends.");
            }
            else {
                out.println("Parse Error: Colon expected.");
                Error();
            }
        }
        else {
            out.print("Invalid Statement");
            Error();
        }
    }

    //ArgFollow routine
    private void ArgFollow(){
        if (tokens.get(0).equals("Comma")){
            tokens.remove(0);
            Arg();
            ArgFollow();
        }
    }

    //Arg routine
    private void Arg(){
        if (tokens.get(0).equals("String")){
            tokens.remove(0);
        }
        else {
            Exp();
        }
    }

    //Iffollow routine
    private void Iffollow(){
        if (tokens.get(0).equals("Endif")){
            tokens.remove(0);
            if (tokens.get(0).equals("Semicolon")){
                tokens.remove(0);
            }
            else {
                out.print("Semicolon expected.");
            }
        }
        else if (tokens.get(0).equals("Else")){
            tokens.remove(0);
            Blk();
            if (tokens.get(0).equals("Endif")){
                tokens.remove(0);
                if (tokens.get(0).equals("Semicolon")){
                    tokens.remove(0);
                }
                else {
                    out.print("Semicolon expected.");
                }
            }
            else {
                out.println("Parse Error: Endif expected");
                Error();
            }
        }
        else {
            out.println("Parse Error: Incomplete if Statement");
            Error();
        }
    }

    
    //Exp routine
    private void Exp(){
        Trm();
        TrmFollow();
    }

    //TrmFollow routine
    private void TrmFollow(){
        if (tokens.get(0).equals("Plus")){
            tokens.remove(0);
            Trm();
            TrmFollow();
        }
        else if (tokens.get(0).equals("Minus")){
            tokens.remove(0);
            Trm();
            TrmFollow();
        }
    }

    //Trm routine
    private void Trm(){
        Fac();
        FacFollow();
    }

    //FacFollow routine
    private void FacFollow(){
        if (tokens.get(0).equals("Multiply")){
            tokens.remove(0);
            Fac();
            FacFollow();
        }
        else if (tokens.get(0).equals("Divide")){
            tokens.remove(0);
            Fac();
            FacFollow();
        }
    }

    //Fac routine
    private void Fac(){
        Lit();
        LitFollow();
    }

    //LitFollow routine
    private void LitFollow(){
        if (tokens.get(0).equals("Raise")){
            tokens.remove(0);
            Lit();
            LitFollow();
        }
    }

    //Lit routine
    private void Lit(){
        if (tokens.get(0).equals("Minus")){
            tokens.remove(0);
            Val();
        }
        else {
            Val();
        }
    }

    //Val routine
    private void Val(){
        if (tokens.get(0).equals("Identifier")){
            tokens.remove(0);
        }
        else if (tokens.get(0).equals("Number")){
            tokens.remove(0);
        }
        else if (tokens.get(0).equals("Sqrt")){
            tokens.remove(0);
            if (tokens.get(0).equals("LeftParen")){
                tokens.remove(0);
                Exp();
                if (tokens.get(0).equals("RightParen")){
                    tokens.remove(0);
                }
                else {
                    out.println("Parse Error: RightParen expected.");
                    Error();
                }
            }
            else {
                out.println("Parse Error: LeftParen expected.");
                Error();
            }
        }
        else if (tokens.get(0).equals("LeftParen")){
            tokens.remove(0);
            Exp();
            if (tokens.get(0).equals("RightParen")){
                tokens.remove(0);
            }
            else {
                out.println("Parse Error: RightParen expected.");
                Error();
            }
        }
        else {
            out.println("Parse Error: Value error.");
            Error();
        }
    }

    //Cnd routine
    private void Cnd(){
        Exp();
        Rel();
        Exp();
    }

    //Rel routine
    private void Rel(){
        if (tokens.get(0).equals("LessThan") || tokens.get(0).equals("=") || tokens.get(0).equals("GreaterThan") || 
        tokens.get(0).equals("GTEqual") || tokens.get(0).equals("NotEqual") || tokens.get(0).equals("LTEqual")){
            tokens.remove(0);
        }
        else {
            System.out.println(tokens.get(0));
            out.println("Parse Error: Missing relational operator.");
            Error();
        }
    }

    //When an error is found, this method is called which completes the output file and closes the program
    private void Error(){
        out.close();
        System.exit(0);
    }

}