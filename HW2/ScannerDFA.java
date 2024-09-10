public class ScannerDFA {

    //fields
    private String inputStream;
    private char currentChar;
    private String currentToken;
    private String currentState;
    private boolean noError;
    private boolean ongoingDFA;

    //constructor
    public ScannerDFA(String inputStream){
        this.inputStream = inputStream.replaceAll("\\s+", "");
        currentChar = inputStream.charAt(0);
        currentState = "A";
        noError = true;
        currentToken = "";
        ongoingDFA = true;
    }

    //methods
    public String printStream(){
        return inputStream;
    }

    public boolean showError(){
        return noError;
    }


    //Prototype only ignore the shit inside
    public void runDFA(){
        switch(currentState){
            case "A":
                if (Character.isDigit(currentChar)){
                    updateDFA();
                    currentState = "B";
                    break;
                }
                else {
                    switch(currentChar){
                        case '=':
                            updateDFA();
                            currentState = "E";
                            break;
                        case '+':
                            updateDFA();
                            currentState = "PLUS";
                            ongoingDFA = false;
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
        //SAMPLE ONLY
        // switch(currentState){
        //     case "C":
        //         if (Character.isDigit(currentChar)){
        //             updateDFA();
        //             currentState = "N";
        //         }
        //         switch(currentChar){
        //             case 'X':
        //                 updateDFA();
        //                 currentState = "A";
        //             case 'Y':
        //                 updateDFA();
        //                 currentState = "B";
        //         }
        // }
    }

    public String printTokenLexeme(){
        ongoingDFA = true;
        currentState = "A";
        currentToken = "";
        String lexeme;
        String tokenLexeme;
        while (ongoingDFA && !inputStream.isEmpty()){
            runDFA();
        }
        if (!noError){
            tokenLexeme = "Lexical Error reading character \"" + Character.toString(currentChar) + "\"";
        }
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