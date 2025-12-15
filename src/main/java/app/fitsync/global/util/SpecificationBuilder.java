package app.fitsync.global.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

    private final List<Specification<T>> specs = new ArrayList<>();

    public static <T> SpecificationBuilder<T> builder() {
        return new SpecificationBuilder<>();
    }

    public SpecificationBuilder<T> andEqual(String fieldName, Object value) {
        if (value != null) {
            specs.add(((root, query, builder) -> builder.equal(root.get(fieldName), value)));
        }
        return this;
    }

    public SpecificationBuilder<T> andLike(String fieldName, String value) {
        if (value != null && !value.trim().isEmpty()) {
            specs.add((root, query, builder) -> builder.like(root.get(fieldName), "%" + value + "%"));
        }
        return this;
    }

    public SpecificationBuilder<T> and(boolean condition, Specification<T> spec) {
        if (condition) {
            specs.add(spec);
        }
        return this;
    }

    public Specification<T> build() {
        if (specs.isEmpty()) {
            return null; // 또는 Specification.where(null);
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
