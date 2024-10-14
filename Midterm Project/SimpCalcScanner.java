import java.util.ArrayList;
public class SimpCalcScanner {

    //fields
    //all necessary variables needed by the DFA is stored in the class fields

    private String inputStream; //everything in the input
    private char currentChar; //current character being pointed at
    private String currentToken; //current token being produced
    private String currentState; //current state of the scanner
    private boolean noError; //checks if an error occurs where an invalid character is read
    private boolean ongoingDFA; //boolean to keep track of being in a final state
    private ArrayList<String> tokenIDs; //list to store token IDs

    //constructor
    //fields are initialized here and the input stream is fed the input

    public SimpCalcScanner(String inputStream){
        this.inputStream = inputStream.replaceAll("\\s+", "");
        //currentChar = inputStream.charAt(0);
        if (this.inputStream.length() > 0) {
            currentChar = this.inputStream.charAt(0);
        } else {
            currentChar = '\0'; // End of input
        }
        currentState = "A";
        noError = true;
        currentToken = "";
        ongoingDFA = true;
        tokenIDs = new ArrayList<>();
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
                    currentState = "J";
                    break;
                }
                else { // handles non-digit characters
                    switch(currentChar){
                        case '=':
                            updateDFA(); 
                            currentState = "Equal";
                            ongoingDFA = false; // end DFA process and moves on to next token
                            break;
                        case '+':
                            updateDFA();
                            currentState = "Plus";
                            ongoingDFA = false; 
                            break;
                        case '-':
                            updateDFA();
                            currentState = "Minus";
                            ongoingDFA = false;
                            break;
                        case ':':
                            updateDFA();
                            currentState = "B"; // move to state where = is possible
                            break;
                        case ';':
                            updateDFA();
                            currentState = "Semicolon";
                            ongoingDFA = false;
                            break;
                        case ',':
                            updateDFA();
                            currentState = "Comma";
                            ongoingDFA = false;
                            break;
                        case '(':
                            updateDFA();
                            currentState = "LeftParen";
                            ongoingDFA = false;
                            break;
                        case ')':
                            updateDFA();
                            currentState = "RightParen";
                            ongoingDFA = false;
                            break;
                        case '*':
                            updateDFA();
                            currentState = "C";
                            break;
                        case '/':
                            updateDFA();
                            currentState = "H";
                            break;
                        case '<':
                            updateDFA();
                            currentState = "D";
                            break;
                        case '>':
                            updateDFA();
                            currentState = "E";
                            break;
                        case '!':
                            updateDFA();
                            currentState = "F";
                            break;
                        default:
                            noError = false;
                            ongoingDFA = false;
                            break;
                    }
                    break;
                }
            case "B":
                switch(currentChar){
                    case '=':
                        updateDFA();
                        currentState = "Assign";
                        ongoingDFA = false;
                        break;
                    default:
                        currentState = "Colon";
                        ongoingDFA = false;
                        break;
                }
                break;
            case "C":
                switch(currentChar){
                    case '*':
                        updateDFA();
                        currentState = "Raise";
                        ongoingDFA = false;
                        break;
                    default:
                        currentState = "Multiply";
                        ongoingDFA = false;
                        break;
                }
                break;
            case "D":
                switch(currentChar){
                    case '=':
                        updateDFA();
                        currentState = "LTEqual";
                        ongoingDFA = false;
                        break;
                    default:
                        currentState = "LessThan";
                        ongoingDFA = false;
                        break;
                } 
                break;
            case "E":
                switch(currentChar){
                    case '=':
                        updateDFA();
                        currentState = "GTEqual";
                        ongoingDFA = false;
                        break;
                    default:
                        currentState = "GreaterThan";
                        ongoingDFA = false;
                        break;
                } 
                break; 
            case "F":
                switch(currentChar){
                    case '=':
                        updateDFA();
                        currentState = "NotEqual";
                        ongoingDFA = false;
                        break;
                    default:
                        noError = false;
                        ongoingDFA = false;
                } 
                break;              
            case "H":
                switch(currentChar){
                    case '/':
                        updateDFA();
                        while (currentChar != '\n' && inputStream.length() > 0) {
                            updateDFA();
                        }
                        ongoingDFA = false;
                        break;
                    default:
                        currentState = "Divide";
                        ongoingDFA = false;
                        break;
                }
                break;
            case "J":
                // if character is still a digit
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "J";
                    break;
                }
                // if character is e, E
                if (currentChar == 'e' || currentChar == 'E'){
                    updateDFA();
                    currentState = "M";
                    break;
                }
                // if character is .
                if (currentChar == '.'){
                    updateDFA();
                    currentState = "K";
                    break;
                }
                // other
                else {
                    ongoingDFA = false;
                    currentState = "Number";
                    break;
                }
                
            case "K": // float pt. 1
                // if character is a digit
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "L";
                    break;
                }
                // if there's no digit after the decimal
                else {
                    noError = false; // error: no digit after decimal
                    ongoingDFA = false;
                    break;
                }

            case "L": // float pt. 2
                // if character is digit
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "L";
                    break;
                } 
                // if character is e, E
                if (currentChar == 'e' || currentChar == 'E'){
                    updateDFA();
                    currentState = "M";
                    break;
                }
                // other
                else {
                    ongoingDFA = false;
                    currentState = "Number";
                    break;
                }
            case "M": // exponent
                // if character is -, +
                if (currentChar == '-' || currentChar == '+'){
                    updateDFA();
                    currentState = "N";
                    break;
                }
                // if character is a digit
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "O";
                    break;
                }
                // if there's no digit after e, E
                else {
                    noError = false; // error: no digit after e, E
                    ongoingDFA = false;
                    break;
                }
            case "N": // exponent pt. 2
                // if character is a digit
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "O";
                    break;
                }
                // if there's no digit after -, +
                else {
                    noError = false; // error: no digit after -, +
                    ongoingDFA = false;
                    break;
                }
            case "O": // exponent pt. 3
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "O";
                    break;
                }
                else {
                    ongoingDFA = false;
                    currentState = "Number";
                    break;
                }
        }
    }
    // This method gets the current token lexeme as a string
    public String printTokenLexeme(){
        ongoingDFA = true;
        currentState = "A";
        currentToken = "";
        String tokenLexeme;
        // Runs the DFA until input is empty or moving on to next token
        while (ongoingDFA && !inputStream.isEmpty()){
            runDFA();
        }
        // Error message
        if (!noError){
            tokenLexeme = "Lexical Error reading character \"" + Character.toString(currentChar) + "\"";
            tokenIDs.add("Error"); 
        } 
        else { 
            tokenLexeme = currentState + "  " + currentToken; 
            tokenIDs.add(getTokenID());
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
    // Method to retrieve the list of token IDs
    public ArrayList<String> getTokenIDs(){
        return tokenIDs;
    }

    // Helper method to map currentState to token ID strings
    private String getTokenID(){
        switch(currentState){
            case "Equal":
                return "=";
            case "Plus":
                return "Plus";
            case "Minus":
                return "Minus";
            case "Colon":
                return "Colon";
            case "Semicolon":
                return "Semicolon";
            case "Comma":
                return "Comma";
            case "LeftParen":
                return "LeftParen";
            case "RightParen":
                return "RightParen";
            case "Multiply":
                return "Multiply";
            case "Divide":
                return "Divide";
            case "LTEqual":
                return "LTEqual";
            case "LessThan":
                return "LessThan";
            case "GTEqual":
                return "GTEqual";
            case "GreaterThan":
                return "GreaterThan";
            case "NotEqual":
                return "NotEqual";
            case "Number":
                return "Number";
            case "Assign":
                return "Assign";
            case "Raise":
                return "Raise";
            default:
                return "Unknown";
        }
    }
}