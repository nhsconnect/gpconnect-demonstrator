package uk.gov.hscic.common.util;

public final class NhsCodeValidator {
    
    private NhsCodeValidator(){}
    
    public static boolean nhsNumberValid(String nhsNumber){
        
        // NHS Numbers should be 10 digits long
        if(nhsNumber.length() < 10 || nhsNumber.length() > 10) return false;
        
        // The NHS number should only contain numeric values
        if(!nhsNumber.matches("[0-9]+")) return false;
        
        // Modulus 11 Checked
        String[] nhsNumberDigits = nhsNumber.split("(?!^)");
        
        int result = Integer.parseInt(nhsNumberDigits[0]) * 10;
        result += Integer.parseInt(nhsNumberDigits[1]) * 9;
        result += Integer.parseInt(nhsNumberDigits[2]) * 8;
        result += Integer.parseInt(nhsNumberDigits[3]) * 7;
        result += Integer.parseInt(nhsNumberDigits[4]) * 6;
        result += Integer.parseInt(nhsNumberDigits[5]) * 5;
        result += Integer.parseInt(nhsNumberDigits[6]) * 4;
        result += Integer.parseInt(nhsNumberDigits[7]) * 3;
        result += Integer.parseInt(nhsNumberDigits[8]) * 2;
        result = 11 - (result % 11);
        if(result == 11) result = 0;
        
        int checkDigit = Integer.parseInt(nhsNumberDigits[9]);
        
        if(result == checkDigit){
            return true;
        } else {
            return false;
        }
        
    }
}
