package site.chagok.server.member.exception;

public class NickNameExistsException extends RuntimeException{

    public NickNameExistsException(String message) {
        super("nickname already exists");
    }
}
