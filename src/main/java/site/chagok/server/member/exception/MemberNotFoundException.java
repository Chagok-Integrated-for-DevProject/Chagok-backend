package site.chagok.server.member.exception;

import site.chagok.server.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super("member_01", "cannot find member");
    }
}
