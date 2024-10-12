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
    private void Prg(){
        Blk();
        EndOfFile();
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
                    out.println("Statement Recognized");
                }
                else {
                    out.println("Semicolon expected.");
                }
            }
            else {
                out.println("Assign expected.");
            }
        }
        else if (tokens.get(0).equals("Print")){
            tokens.remove(0);
            if (tokens.get(0).equals("(")){
                tokens.remove(0);
                Arg();
                ArgFollow();
                if (tokens.get(0).equals(")")){
                    tokens.remove(0);
                    if (tokens.get(0).equals("Semicolon")){
                        tokens.remove(0);
                        out.println("Print Statement Recognized");
                    }
                    else {
                        out.println("Semicolon expected.");
                    }
                }
                else {
                    out.println("Closing parenthesis expected.");
                }
            }
            else {
                out.println("Opening parenthesis expected.");
            }
        }
        else if (tokens.get(0).equals("If")){
            tokens.remove(0);
            out.println("If Statement Begins");
            Cnd();
            if (tokens.get(0).equals(":")){
                tokens.remove(0);
                Blk();
                Iffollow();
                out.println("If Statement Ends");
            }
            else {
                out.println("Colon expected.");
            }
        }
        else {
            StmError();
        }
    }

    private void StmError(){
        out.print("Invalid Statement");
    }

    private void ArgFollow(){
        if (tokens.get(0).equals(",")){
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
                out.println("Incomplete if Statement");
            }
        }
        else {
            IffollowError();
        }
    }

    private void IffollowError(){
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
        else {
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
        else {
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
        else {
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
            if (tokens.get(0).equals("(")){
                tokens.remove(0);
                Exp();
                if (tokens.get(0).equals(")")){
                    tokens.remove(0);
                }
                else {
                    out.println("Closing parenthesis expected");
                }
            }
            else {
                out.println("Opening parenthesis expected");
            }
        }
        else if (tokens.get(0).equals("(")){
            tokens.remove(0);
            Exp();
            if (tokens.get(0).equals(")")){
                tokens.remove(0);
            }
            else {
                out.println("Closing parenthesis expected");
            }
        }
        else {
            ValError();
        }
    }

    private void ValError(){
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
            RelError();
        }
    }

    private void RelError(){
    }

    private void EndOfFile(){

    }

}