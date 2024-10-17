import java.util.ArrayList;

public class SimpCalcScanner {

    // Class variables
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
        // Set currentChar to the first character in the input stream
        if (this.inputStream.length() > 0) {
            currentChar = this.inputStream.charAt(0);
        } else {
            currentChar = '\0';  // End of input character
        }
        // Initialize other variables
        currentState = "A";
        noError = true;
        currentToken = "";
        ongoingDFA = true;
        tokenIDs = new ArrayList<>();
    }

    // Method to return the input stream
    public String printStream() {
        return inputStream;
    }

    // Method to return if there is an error
    public boolean showError() {
        return noError;
    }

    // Main DFA logic for tokenizing the input
    public void runDFA() {
        switch (currentState) {
            // Initial state, handles whitespace, comments, and operators
            case "A":
                // Handle whitespace and new lines
                if (Character.isWhitespace(currentChar)) {
                    updateDFA();
                    break;
                }

                // Handle comments
                if (currentChar == '/') {
                    updateDFA();
                    if (currentChar == '/') {
                        skipComment();  // Skip the entire comment
                        currentState = "A";
                        break;
                    } else {
                        currentState = "Divide";  // It's a division operator
                        ongoingDFA = false;
                        break;
                    }
                }

                // Handle digits (numbers)
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "J"; // Number state
                } 
                // Handle letters (identifiers/keywords)
                else if (Character.isLetter(currentChar) || currentChar == '_') {
                    updateDFA();
                    currentState = "G"; // Identifier/Keyword state
                } 
                // Handle other operators and delimiters
                else {
                    switch (currentChar) {
                        case '=': // Assignment operator
                            updateDFA();
                            currentState = "Equal";
                            ongoingDFA = false;
                            break;
                        case '+': // Plus operator
                            updateDFA();
                            currentState = "Plus";
                            ongoingDFA = false;
                            break;
                        case '-': // Minus operator
                            updateDFA();
                            currentState = "Minus";
                            ongoingDFA = false;
                            break;
                        case ':': // Start of assignment (:=)
                            updateDFA();
                            currentState = "B";
                            break;
                        case ';': // Semicolon
                            updateDFA();
                            currentState = "Semicolon";
                            ongoingDFA = false;
                            break;
                        case ',': // Comma
                            updateDFA();
                            currentState = "Comma";
                            ongoingDFA = false;
                            break;
                        case '(': // Left parenthesis
                            updateDFA();
                            currentState = "LeftParen";
                            ongoingDFA = false;
                            break;
                        case ')': // Right parenthesis
                            updateDFA();
                            currentState = "RightParen";
                            ongoingDFA = false;
                            break;
                        case '*': // Multiply or exponentiation
                            updateDFA();
                            currentState = "C"; // Check for multiplication or exponentiation
                            break;
                        case '<': // Less than operator
                            updateDFA();
                            currentState = "D"; // Less than state
                            break;
                        case '>': // Greater than operator
                            updateDFA();
                            currentState = "E"; // Greater than state
                            break;
                        case '!': // Not equal operator
                            updateDFA();
                            currentState = "F"; // Not equal state
                            break;
                        case '\"': // Start of a string
                            updateDFA();
                            currentState = "I"; // String state
                            break;
                        default: // Unrecognized character
                            addError("Illegal character/character sequence");
                            updateDFA(); // Skip the problematic character
                            currentState = "A"; // Reset state to continue
                            break;
                    }
                }
                break;

            // Handle various states for multi-character operators
            case "B": // Assignment state (colon)
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "Assign";
                    ongoingDFA = false;
                } else {
                    currentState = "Colon";
                    ongoingDFA = false;
                }
                break;

            case "C": // Multiplication or exponentiation
                if (currentChar == '*') {
                    updateDFA();
                    currentState = "Raise"; // Exponentiation
                    ongoingDFA = false;
                } else {
                    currentState = "Multiply"; // Multiplication
                    ongoingDFA = false;
                }
                break;

            // Handle less than/greater than cases
            case "D": // Less than
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "LTEqual"; // Less than or equal to
                    ongoingDFA = false;
                } else {
                    currentState = "LessThan"; // Just less than
                    ongoingDFA = false;
                }
                break;

            case "E": // Greater than
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "GTEqual"; // Greater than or equal to
                    ongoingDFA = false;
                } else {
                    currentState = "GreaterThan"; // Just greater than
                    ongoingDFA = false;
                }
                break;

            // Handle "!=" (not equal) case
            case "F":
                if (currentChar == '=') {
                    updateDFA();
                    currentState = "NotEqual"; // Not equal to
                    ongoingDFA = false;
                } else {
                    addError("Illegal character/character sequence"); // Invalid character after '!'
                    updateDFA(); // Skip the problematic character
                    currentState = "A"; // Reset to continue
                }
                break;

            // Handle identifier/keyword
            case "G": // Identifier or keyword
                if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
                    updateDFA();
                    currentState = "G"; // Continue identifier
                } else {
                    currentToken = currentToken.trim(); // Remove extra spaces
                    ongoingDFA = false; // End identifier token
                    if (isKeyword(currentToken)) { // Check if it's a keyword
                        currentState = getKeywordToken(currentToken); 
                    } else {
                        currentState = "Identifier"; // Otherwise, it's an identifier
                    }
                }
                break;

            // Handle string literals
            case "I": // String state
                if (currentChar != '\"' && currentChar != '\n' && currentChar != '\0') { // Valid string character
                    updateDFA();
                } else if (currentChar == '\"') { // End of string
                    updateDFA();
                    ongoingDFA = false;
                    currentState = "String"; // Valid string token
                } else { // Unterminated string
                    addError("Unterminated string"); // Unterminated error
                    updateDFA(); // Skip to the next token
                    currentState = "A"; // Reset state to continue
                }
                break;

            // Handle numeric tokens
            case "J": // Numeric state
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "J"; // Continue with number
                } else if (currentChar == 'e' || currentChar == 'E') { // Exponent
                    updateDFA();
                    currentState = "M"; // Exponent state
                } else if (currentChar == '.') { // Decimal point
                    updateDFA();
                    currentState = "K"; // Decimal number state
                } else {
                    ongoingDFA = false; // End number token
                    currentState = "Number";
                }
                break;

            // Handle decimal number state
            case "K": 
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "L"; // Decimal number
                } else {
                    addError("Invalid number format"); // Error for invalid number
                    updateDFA(); // Skip character
                    currentState = "A"; // Reset state
                }
                break;

            // Handle exponents
            case "L": // After decimal
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "L"; // Continue decimal number
                } else if (currentChar == 'e' || currentChar == 'E') { // Handle exponent
                    updateDFA();
                    currentState = "M"; // Exponent state
                } else {
                    ongoingDFA = false;
                    currentState = "Number"; // End number token
                }
                break;

            // Handle exponent part
            case "M": 
                if (currentChar == '+' || currentChar == '-') { // Exponent sign
                    updateDFA();
                    currentState = "N"; // Signed exponent
                } else if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "O"; // Exponent number
                } else {
                    addError("Invalid number format"); // Error for bad exponent
                    updateDFA(); // Skip character
                    currentState = "A"; // Reset state
                }
                break;

            // Handle signed exponent
            case "N":
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "O"; // Exponent digits
                } else {
                    addError("Invalid number format"); // Error for invalid sign
                    updateDFA(); // Skip character
                    currentState = "A"; // Reset state
                }
                break;

            // Finalize the exponent part
            case "O": 
                if (Character.isDigit(currentChar)) {
                    updateDFA();
                    currentState = "O"; // Continue exponent
                } else {
                    ongoingDFA = false;
                    currentState = "Number"; // End of number
                }
                break;

            default: 
                addError("Unknown token"); // Unknown token encountered
                updateDFA(); // Skip the problematic character
                currentState = "A"; // Reset state to continue
                break;
        }
    }

    // Skip over comments in the input
    public void skipComment() {
        while (currentChar != '\n' && inputStream.length() > 0) {
            inputStream = inputStream.substring(1); // Remove comment characters
            if (inputStream.length() > 0) {
                currentChar = inputStream.charAt(0); // Update current character
            }
        }
        if (inputStream.length() > 0) {
            inputStream = inputStream.substring(1); // Move past newline after comment
            if (inputStream.length() > 0) {
                currentChar = inputStream.charAt(0); // Update current character
            }
        }
        currentToken = ""; // Clear the current token
    }

    // Check if a token is a keyword
    public boolean isKeyword(String token) {
        return token.equals("IF") || token.equals("PRINT") || token.equals("ELSE") || token.equals("ENDIF") ||
               token.equals("SQRT") || token.equals("AND") || token.equals("OR") || token.equals("NOT");
    }

    // Return the keyword token's state
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
                return "Identifier"; // Return identifier if it's not a keyword
        }
    }

    // Print the token and lexeme
    public String gettoken() {
        ongoingDFA = true; // Reset DFA flag
        currentState = "A"; // Reset state to the start
        currentToken = ""; // Clear current token
        String tokenLexeme; // Output token-lexeme pair
        
        // Process tokens until DFA finishes or input stream is empty
        while (ongoingDFA && !inputStream.isEmpty()) {
            runDFA(); // Run DFA to process next character
        }

        // If input is exhausted but there’s still a token being processed
        if (inputStream.isEmpty() && !currentToken.isEmpty() && ongoingDFA) {
            if (isKeyword(currentToken)) {
                currentState = getKeywordToken(currentToken); // Set state to keyword
            } else {
                currentState = "Identifier"; // Otherwise, it's an identifier
            }
            ongoingDFA = false; // End token processing
        }

        // Check for errors and return appropriate output
        if (!noError) {
            // Fetch the latest error message for output
            int lastIndex = tokenIDs.size() - 1;
            String lastError = tokenIDs.get(lastIndex); // Get the latest error message
            
            // Add error to token lexeme
            tokenLexeme = lastError + "\nError";
            tokenIDs.add("Error"); // Add error to token list
            noError = true; // Reset error flag
        } else {
            // No error, return the state and token
            tokenLexeme = currentState + "  " + currentToken.trim();
            tokenIDs.add(getTokenID()); // Add token ID
        }

        return tokenLexeme; // Return the token lexeme pair
    }

    // Update the DFA state by consuming the current character
    public void updateDFA() {
        currentToken += currentChar; // Add current character to token
        if (inputStream.length() > 0) {
            inputStream = inputStream.substring(1); // Remove the first character from input
        }
        if (inputStream.length() > 0) {
            currentChar = inputStream.charAt(0); // Update current character
        }
    }

    // Method to retrieve the list of token IDs
    public ArrayList<String> getTokenIDs(){
        tokenIDs.add("EndOfFile");
        return tokenIDs;
    }

    // Add an error message to the token list
    private void addError(String errorMessage) {
        tokenIDs.add("Error"); // Add generic error
        tokenIDs.add("Lexical Error: " + errorMessage); // Detailed error
        noError = false; // Set error flag to true
        ongoingDFA = false; // End current token processing
    }

    // Get the token ID based on the current DFA state
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
                return "Unknown"; // Return "Unknown" for unrecognized tokens
        }
    }
}
