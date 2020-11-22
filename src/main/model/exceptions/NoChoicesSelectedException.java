package model.exceptions;

//Exception generated when the SelectedChoice object is empty
public class NoChoicesSelectedException extends Exception {
    public NoChoicesSelectedException() {
        System.out.println("There were no choices selected.");
    }

}
