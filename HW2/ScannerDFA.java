public class ScannerDFA {

    //fields
    private String inputStream;
    private char currentChar;
    private String currentToken;
    private String currentState;
    private boolean noError;

    //constructor
    public ScannerPractice(String inputStream){
        this.inputStream = inputStream;
        currentChar = inputStream.charAt(0);
        currentState = "C";
        noError = true;
        currentToken = "";
    }

    //methods
    public String printStream(){
        return inputStream;
    }

    public boolean showError(){
        return noError;
    }


    //Prototype only ignore the shit inside
    public String runDFA(){
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

    }

    public void updateDFA(){
        currentToken += currentChar;
        inputStream = inputStream.substring(1);
        currentChar = inputStream.charAt(0);
    }


}