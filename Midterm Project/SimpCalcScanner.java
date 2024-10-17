import java.util.ArrayList;

public class SimpCalcScanner {

    //fields
    private String inputStream;    // Input string to be tokenized
    private char currentChar;      // Current character being processed
    private String currentToken;   // Current token being built
    private String currentState;   // Current DFA state
    private boolean noError;       // Error flag to track if there's been an error
    private boolean ongoingDFA;    // DFA running state flag
    private ArrayList<String> tokenIDs;   // List to store identified tokens

    
    // Constructor to initialize with an input stream
    public SimpCalcScanner(String inputStream) {
        this.inputStream = inputStream;
        if (this.inputStream.length() > 0) {
            currentChar = this.inputStream.charAt(0);
        } else {
            currentChar = '\0';  
        }
        currentState = "A";
        noError = true;
        currentToken = "";
        ongoingDFA = true;
        tokenIDs = new ArrayList<>();
    }

    // Methods
    // Method to return the input stream
    public String printStream() {
        return inputStream;
    }

    // Method to return if there is an error
    public boolean showError() {
        return noError;
    }

    // Method to skip over comments in the input
    public void skipComment() {
        while (currentChar != '\n' && inputStream.length() > 0) {
            inputStream = inputStream.substring(1); 
            if (inputStream.length() > 0) {
                currentChar = inputStream.charAt(0); 
            }
        }
        if (inputStream.length() > 0) {
            inputStream = inputStream.substring(1); 
            if (inputStream.length() > 0) {
                currentChar = inputStream.charAt(0); 
            }
        }
        currentToken = ""; 
    }

        // Print the token and lexeme
        public String gettoken() {
            ongoingDFA = true; 
            currentState = "A"; 
            currentToken = ""; 
            String tokenLexeme; 
            
            // Process tokens until DFA finishes or input stream is empty
            while (ongoingDFA && !inputStream.isEmpty()) {
                runDFA(); // Run DFA to process next character
            }
    
            // If input is exhausted but there’s still a token being processed
            if (inputStream.isEmpty() && !currentToken.isEmpty() && ongoingDFA) {
                // If the last token is a number, classify it as a number
                if (currentState.equals("J") || currentState.equals("L") || currentState.equals("O")) {
                    currentState = "Number";  
                } else if (isKeyword(currentToken)) {
                    currentState = getKeywordToken(currentToken);
                } else {
                    currentState = "Identifier";
                }
                ongoingDFA = false;
            }
    
            // Check for errors and return appropriate output
            if (!noError) {
                int lastIndex = tokenIDs.size() - 1;
                String lastError = tokenIDs.get(lastIndex); 
                
                // Add error to token lexeme
                tokenLexeme = lastError + "\nError";
                tokenIDs.add("Error"); 
                noError = true; 
            } else {
                // No error, return the state and token
                tokenLexeme = currentState + "  " + currentToken.trim();
                tokenIDs.add(getTokenID()); 
            }
    
            return tokenLexeme; 
        }
    
        // Method to update the DFA state by consuming the current character
        public void updateDFA() {
            currentToken += currentChar; 
            if (inputStream.length() > 0) {
                inputStream = inputStream.substring(1); 
            }
            if (inputStream.length() > 0) {
                currentChar = inputStream.charAt(0); 
            }
        }
    
        // Method to retrieve the list of token IDs
        public ArrayList<String> getTokenIDs(){
            tokenIDs.add("EndOfFile");
            return tokenIDs;
        }
    
        // Method to add an error message to the token list
        private void addError(String errorMessage) {
            tokenIDs.add("Error"); 
            tokenIDs.add("Lexical Error: " + errorMessage); 
            noError = false; 
            ongoingDFA = false; 
        }

    
    // Method to check if a token is a keyword
    public boolean isKeyword(String token) {
        return token.equals("IF") || token.equals("PRINT") || token.equals("ELSE") || token.equals("ENDIF") ||
               token.equals("SQRT") || token.equals("AND") || token.equals("OR") || token.equals("NOT");
    }

    // Main DFA logic for tokenizing the input
    public void runDFA() {
        switch (currentState) {
            // Initial state, handles whitespace, comments, and operators
            case "A":
                
                if (Character.isWhitespace(currentChar)) {
                    updateDFA();
                    break;
                }

                // Handle comments
                if (currentChar == '/') {
                    updateDFA();
                    if (currentChar == '/') {
                        skipComment();  
                        currentState = "A";
                        break;
                    } else {
                        currentState = "Divide";  
                        ongoingDFA = false;
                        break;
                    }
                }

                // Handle digits (numbers)
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "J"; 
                } 
                // Handle letters (identifiers/keywords)
                else if (Character.isLetter(currentChar) || currentChar == '_') {
                    updateDFA();
                    currentState = "G"; 
                } 
                // Handle other operators and delimiters
                else {
                    switch (currentChar) {
                        case '=': 
                            updateDFA();
                            currentState = "Equal";
                            ongoingDFA = false;
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
                            currentState = "B";
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
                        case '\"': 
                            updateDFA();
                            currentState = "I"; 
                            break;
                        default: 
                            addError("Illegal character/character sequence");
                            updateDFA(); 
                            currentState = "A"; 
                            break;
                    }
                }
                break;

            // Handle various states for multi-character operators
            case "B": 
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "Assign";
                    ongoingDFA = false;
                } else {
                    currentState = "Colon";
                    ongoingDFA = false;
                }
                break;

            case "C": 
                if (currentChar == '*') {
                    updateDFA();
                    currentState = "Raise"; 
                    ongoingDFA = false;
                } else {
                    currentState = "Multiply"; 
                    ongoingDFA = false;
                }
                break;

            // Handle less than/greater than cases
            case "D": 
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "LTEqual"; 
                    ongoingDFA = false;
                } else {
                    currentState = "LessThan"; 
                    ongoingDFA = false;
                }
                break;

            case "E": // Greater than
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "GTEqual"; 
                    ongoingDFA = false;
                } else {
                    currentState = "GreaterThan"; 
                    ongoingDFA = false;
                }
                break;

            // Handle "!=" (not equal) case
            case "F":
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "NotEqual"; 
                    ongoingDFA = false;
                } else {
                    addError("Illegal character/character sequence"); 
                    updateDFA(); 
                    currentState = "A"; 
                }
                break;

            // Handle identifier/keyword
            case "G": 
                if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
                    updateDFA();
                    currentState = "G"; 
                } else {
                    currentToken = currentToken.trim(); 
                    ongoingDFA = false; 
                    if (isKeyword(currentToken)) { 
                        currentState = getKeywordToken(currentToken); 
                    } else {
                        currentState = "Identifier"; 
                    }
                }
                break;

            // Handle strings
            case "I": 
                if (currentChar != '\"' && currentChar != '\n' && currentChar != '\0') { 
                    updateDFA();
                } else if (currentChar == '\"') { 
                    updateDFA();
                    ongoingDFA = false;
                    currentState = "String"; 
                } else { 
                    addError("Unterminated string"); 
                    updateDFA(); 
                    currentState = "A"; 
                }
                break;

            // Handle numbers
            case "J": 
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "J"; 
                } else if (currentChar == 'e' || currentChar == 'E') { 
                    updateDFA();
                    currentState = "M"; 
                } else if (currentChar == '.') { 
                    updateDFA();
                    currentState = "K"; 
                } else {
                    ongoingDFA = false; 
                    currentState = "Number";
                }
                break;

            // Handle decimals
            case "K": 
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "L"; 
                } else {
                    addError("Invalid number format"); 
                    updateDFA(); 
                    currentState = "A"; 
                }
                break;

            // Handle exponents
            case "L": 
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "L"; 
                } else if (currentChar == 'e' || currentChar == 'E') { 
                    updateDFA();
                    currentState = "M"; 
                } else {
                    ongoingDFA = false;
                    currentState = "Number"; 
                }
                break;

            // Handle exponent part
            case "M": 
                if (currentChar == '+' || currentChar == '-') { 
                    updateDFA();
                    currentState = "N"; 
                } else if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "O"; 
                } else {
                    addError("Invalid number format"); 
                    updateDFA(); 
                    currentState = "A"; 
                }
                break;

            // Handle signed exponent
            case "N":
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "O"; 
                } else {
                    addError("Invalid number format"); 
                    updateDFA(); 
                    currentState = "A"; 
                }
                break;

            // Finalize the exponent part
            case "O": 
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "O"; 
                } else {
                    ongoingDFA = false;
                    currentState = "Number"; 
                }
                break;

            default: 
                addError("Unknown token"); 
                updateDFA(); 
                currentState = "A"; 
                break;
        }
    }

    // Method to return the keyword token's state
    public String getKeywordToken(String token) {
        switch (token) {
            case "IF":
                return "If";
            case "PRINT":
                return "Print";
            case "ELSE":
                return "Else";
            case "ENDIF":
                return "Endif";
            case "SQRT":
                return "Sqrt";
            case "AND":
                return "And";
            case "OR":
                return "Or";
            case "NOT":
                return "Not";
            default:
                return "Identifier"; 
        }
    }

    // Method to return the token ID based on the current DFA state
    private String getTokenID() {
        switch (currentState) {
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
            case "Raise":
                return "Raise";
            case "GTEqual":
                return "GTEqual";
            case "LTEqual":
                return "LTEqual";
            case "GreaterThan":
                return "GreaterThan";
            case "LessThan":
                return "LessThan";
            case "NotEqual":
                return "NotEqual";
            case "Number":
                return "Number";
            case "Assign":
                return "Assign";
            case "If":
                return "If";
            case "Print":
                return "Print";
            case "Else":
                return "Else";
            case "Endif":
                return "Endif";
            case "Sqrt":
                return "Sqrt";
            case "And":
                return "And";
            case "Or":
                return "Or";
            case "Not":
                return "Not";
            case "Identifier":
                return "Identifier";
            case "String":
                return "String";
            default:
                return "Unknown"; 
        }
    }
}