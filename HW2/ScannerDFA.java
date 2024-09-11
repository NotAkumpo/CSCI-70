public class ScannerDFA {

    //fields
    //all necessary variables needed by the DFA is stored in the class fields
    private String inputStream; //everything in the input
    private char currentChar; //current character being pointed at
    private String currentToken; //current token being produced
    private String currentState; //current state of the scanner
    private boolean noError; //checks if an error occurs where an invalid character is read
    private boolean ongoingDFA; //boolean to keep track of being in a final state

    //constructor
    //fields are initialized here and the input stream is fed the input
    public ScannerDFA(String inputStream){
        this.inputStream = inputStream.replaceAll("\\s+", "");
        currentChar = inputStream.charAt(0);
        currentState = "A";
        noError = true;
        currentToken = "";
        ongoingDFA = true;
    }


    //methods
    //returns the input stream
    public String printStream(){
        return inputStream;
    }

    //checks if theres an error so far
    public boolean showError(){
        return noError;
    }

    /*The runDFA() function processes the current character in the input stream, starting 
    from the initial state "A" and transitioning to the appropriate state based on the character 
    type (e.g., digit, '=', '+', '-'). */ 
    public void runDFA(){
        switch(currentState){
            case "A":
                if (Character.isDigit(currentChar)){ // handles digit characters
                    updateDFA(); // updates the DFA state and moves on to the next character
                    currentState = "B";
                    break;
                }
                else { // handles non-digit characters
                    switch(currentChar){
                        case '=':
                            updateDFA(); 
                            currentState = "E";
                            break;
                        case '+':
                            updateDFA();
                            currentState = "PLUS";
                            ongoingDFA = false; // end DFA process and moves on to next token
                            break;
                        case '-':
                            updateDFA();
                            currentState = "MINUS";
                            ongoingDFA = false;
                            break;
                        default:
                            noError = false;
                            ongoingDFA = false;
                            break;
                    }
                    break;

                }
            case "B":
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "B";
                    break;
                }
                else {
                    ongoingDFA = false;
                    currentState = "NUM";
                    break;
                }
            case "E":
                switch(currentChar){
                    case '=':
                        updateDFA();
                        ongoingDFA = false;
                        break;
                    default:
                        noError = false;
                        ongoingDFA = false;
                        break;
                }
        }
    }
    // This method gets the current token lexeme as a string
    public String printTokenLexeme(){
        ongoingDFA = true;
        currentState = "A";
        currentToken = "";
        String lexeme;
        String tokenLexeme;
        // Runs the DFA until input is empty or moving on to next token
        while (ongoingDFA && !inputStream.isEmpty()){
            runDFA();
        }
        // Error message
        if (!noError){
            tokenLexeme = "Lexical Error reading character \"" + Character.toString(currentChar) + "\"";
        }
        //Some special cases to correct the state being printed 
        else { 
            if (currentState == "E"){
                currentState = "ASSIGN";
            } 
            else if (currentState == "B"){
                currentState = "NUM";
            }
            tokenLexeme = currentState + "  " + currentToken; 
        }
        return tokenLexeme;
    }

    // This method consumes the current character and updates the DFA
    public void updateDFA(){
        //add current character to token
        currentToken += currentChar;
        if (inputStream.length() > 0){
            //consume current character
            inputStream = inputStream.substring(1);
        }
        if (inputStream.length() > 0){
            //point at new current character
            currentChar = inputStream.charAt(0);
        }
    }


}