package site.chagok.server.member.exception;

import javax.persistence.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException() {
        super("cannot find member");
    }
}
