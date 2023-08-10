package site.chagok.server.study.repository;

import org.springframework.data.jpa.domain.Specification;
import site.chagok.server.study.domain.Study;
import site.chagok.server.study.dto.GetStudyPreviewDto;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class StudySpecification {
    public static Specification<Study> equalsTitle(String title){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),"%"+title+"%");
    }
//    public static Specification<GetStudyPreviewDto> equalsTechStack(List<String> techStacks){
//        return()
//    }
}
