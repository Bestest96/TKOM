package exceptions;

public class TranslationException extends Exception {

    public TranslationException() {
        super("Translation error!");
    }

    public TranslationException(String message) {
        super("Translation error! " + message);
    }
}
