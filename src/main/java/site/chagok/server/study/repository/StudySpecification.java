package site.chagok.server.study.repository;

import org.springframework.data.jpa.domain.Specification;
import site.chagok.server.common.contstans.contstans.TechStack;
import site.chagok.server.study.domain.Study;

import javax.persistence.criteria.*;
import java.util.List;

public class StudySpecification {



    public static Specification<Study> equalsTitle(String title){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),"%"+title+"%");
    }
    public static Specification<Study> equalsTechStack(List<String> techStacks){
        return (root, query, criteriaBuilder) -> {
            Join<Study, TechStack> join = root.join("techStacks", JoinType.LEFT);
            System.out.println(join.get("id"));
            return join.get("techStack").in(techStacks);
        };
    }
}
