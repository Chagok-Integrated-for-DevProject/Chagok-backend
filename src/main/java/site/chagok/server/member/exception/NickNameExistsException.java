package site.chagok.server.member.exception;

public class NickNameExistsException extends UpdateInfoException{

    public NickNameExistsException() {
        super("nickname_01", "nickname already exists");
    }
}
