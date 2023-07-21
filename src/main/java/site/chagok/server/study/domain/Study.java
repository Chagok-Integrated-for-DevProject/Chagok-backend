package site.chagok.server.study.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Study {
    @Id
    private Long id;

    @OneToMany(mappedBy = "study")
    List<StudyScrap> studyScraps = new ArrayList<>();
}
