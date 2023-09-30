package site.chagok.server.member.exception;

public class NickNameExistsException extends UpdateInfoException{

    public NickNameExistsException() {
        super("nickname already exists");
    }
}
