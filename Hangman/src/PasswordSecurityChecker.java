package org.app.utils;

public class PasswordSecurityChecker {
  static int minLength = 5;
  static char[] specialMarks = {'\'', '!', '?', '.', ',', '-', '_', '/', '$'};
  public static boolean passwordIsSecure(String password){
    return hasSpecialMarks(password)
            && hasInt(password)
            && hasUpperAndLowerCaseLetters(password)
            && password.length() >= 5;
  }

  // Check if the password has at least 1 upper case and lower case letter
  private static boolean hasUpperAndLowerCaseLetters(String password){
    char[] passwordChars = password.toCharArray();
    short upperCaseLetters = 0;
    short lowerCaseLetters = 0;
    for(int i = 0; i < passwordChars.length; i++){
      if (Character.isLowerCase(passwordChars[i])) lowerCaseLetters++;
      if(Character.isUpperCase(passwordChars[i])) upperCaseLetters++;
    }
    if(lowerCaseLetters > 0
    && upperCaseLetters > 0) return true;
    else return false;
  }

  // Check, if the password contains at least 1 special character
  private static boolean hasSpecialMarks(String password){
    char[] passwordChars = password.toCharArray();
    for (char passwordChar : passwordChars) {
      for (char specialMark : specialMarks) {
        if (passwordChar == specialMark) return true;
      }
    }
    return false;
  }

  // Check if the password contains at least 1 int
  private static boolean hasInt(String password){
    return password.matches(".*\\d+.*");
  }

  // Give all available special chars
  public static String allowedChars(){
    StringBuilder result = new StringBuilder();
    for(int i = 0; i < specialMarks.length - 1; i++){
      result.append(String.format("'%c', ", specialMarks[i]));
    }
    result.append(String.format("'%c'", specialMarks[specialMarks.length - 1]));
    return result.toString();
  }

  public static void main(String[] args) {
    // System.out.printf(allowedChars());
    // System.out.println(hasSpecialMarks("Aaaawawa"));
    System.out.println(hasInt("adwwadadadw"));
    System.out.println(hasUpperAndLowerCaseLetters("aaaaaa"));
    System.out.println(hasSpecialMarks("Arscg!!?$"));
    System.out.println(passwordIsSecure("Ars!adsdw"));
  }
}
