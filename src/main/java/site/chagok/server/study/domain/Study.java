package site.chagok.server.study.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
public class Study {
    @Id
    private Long id;
    @OneToMany(mappedBy = "study")
    private List<StudyScrap> studyScraps = new ArrayList<>();


    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TechStack> techStacks = new ArrayList<>();

}
