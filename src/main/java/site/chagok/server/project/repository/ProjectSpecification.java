package site.chagok.server.project.repository;

import org.springframework.data.jpa.domain.Specification;
import site.chagok.server.project.domain.Project;
import site.chagok.server.study.domain.Study;

import javax.persistence.criteria.Expression;
import java.util.List;

public class ProjectSpecification {

    public static Specification<Project> equalsTitle(String title){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),"%"+title+"%");
    }
    public static Specification<Project> equalsTechStack(List<String> techStacks){
        System.out.println(techStacks);
        return (root, query, criteriaBuilder) -> {
            Expression<List<String>> techStacksPath = root.join("techStacks");
            //techStacks는 다른 테이블이기에 .get이 아닌 .join
            return techStacksPath.in(techStacks);
        };
    }
}
