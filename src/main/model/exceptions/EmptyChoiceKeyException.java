package model.exceptions;

public class EmptyChoiceKeyException extends Exception {
    public EmptyChoiceKeyException() {
        System.out.println("The choice key is empty.");
    }
}
