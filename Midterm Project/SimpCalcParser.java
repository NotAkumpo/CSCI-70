import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SimpCalcParser {

    //fields
    private ArrayList<String> tokens;
    PrintWriter out;

    //constructor
    public SimpCalcParser(ArrayList<String> tokens){
        this.tokens = tokens;
        try {
            File output = new File("output_parse.txt"); 
            if (output.createNewFile()) { 
                out = new PrintWriter("output_parse.txt");
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
    public void Prg(){
        Blk();
        if (tokens.get(0).equals("EndOfFile")){
            out.println("Sample is a valid SimpCalc Program.");
            out.close();
        }
        else {
            out.println("End of file expected.");
            Error();
        }
    }

    private void Blk(){
        if (tokens.get(0).equals("Identifier") || tokens.get(0).equals("Print") || tokens.get(0).equals("If")) {
            Stm();
            Blk();
        }
    }

    private void Stm(){
        if (tokens.get(0).equals("Identifier")){
            tokens.remove(0);
            if (tokens.get(0).equals("Assign")){
                tokens.remove(0);
                Exp();
                if (tokens.get(0).equals("Semicolon")){
                    tokens.remove(0);
                    out.println("Statement Recognized.");
                }
                else {
                    out.println("Parse Error: Semicolon expected.");
                    Error();
                }
            }
            else {
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

    private void ArgFollow(){
        if (tokens.get(0).equals("Comma")){
            tokens.remove(0);
            Arg();
            ArgFollow();
        }
    }

    private void Arg(){
        if (tokens.get(0).equals("String")){
            tokens.remove(0);
        }
        else {
            Exp();
        }
    }

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

    
    private void Exp(){
        Trm();
        TrmFollow();
    }

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

    private void Trm(){
        Fac();
        FacFollow();
    }

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

    private void Fac(){
        Lit();
        LitFollow();
    }

    private void LitFollow(){
        if (tokens.get(0).equals("Raise")){
            tokens.remove(0);
            Lit();
            LitFollow();
        }
    }

    private void Lit(){
        if (tokens.get(0).equals("Minus")){
            tokens.remove(0);
            Val();
        }
        else {
            Val();
        }
    }

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

    private void Cnd(){
        Exp();
        Rel();
        Exp();
    }

    private void Rel(){
        if (tokens.get(0).equals("LessThan") || tokens.get(0).equals("Equal") || tokens.get(0).equals("GreaterThan") || 
        tokens.get(0).equals("GTEqual") || tokens.get(0).equals("NotEqual") || tokens.get(0).equals("LTEqual")){
            tokens.remove(0);
        }
        else {
            out.println("Parse Error: Missing relational operator.");
            Error();
        }
    }

    private void Error(){
        out.close();
        System.exit(0);
    }

}