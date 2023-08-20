package site.chagok.server.study.repository;

import org.springframework.data.jpa.domain.Specification;
import site.chagok.server.common.contstans.TechStack;
import site.chagok.server.study.domain.Study;

import javax.persistence.criteria.*;
import java.util.List;

public class StudySpecification {




    public static Specification<Study> equalsTitle(String title){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),"%"+title+"%");
    }
    public static Specification<Study> equalsTechStack(List<String> techStacks){
        System.out.println(techStacks);
        return (root, query, criteriaBuilder) -> {
            Expression<List<String>> techStacksPath = root.join("techStacks");
            //techStacks는 다른 테이블이기에 .get이 아닌 .join
            return techStacksPath.in(techStacks);
        };
    }
}
