package app.fitsync.domain.exercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "body_parts")
@NoArgsConstructor
public class BodyPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "bodyPart")
    private List<BodyDetailPart> detailParts = new ArrayList<>();

    @Override
    public String toString() {
        return "BodyPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detailParts=" + detailParts +
                '}';
    }
}
