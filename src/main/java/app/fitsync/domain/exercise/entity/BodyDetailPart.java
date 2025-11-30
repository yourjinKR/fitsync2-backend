package app.fitsync.domain.exercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "body_detail_parts")
@NoArgsConstructor
public class BodyDetailPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_part_id", nullable = false)
    private BodyPart bodyPart;

    @Override
    public String toString() {
        return "BodyDetailPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bodyPart=" + bodyPart +
                '}';
    }
}
