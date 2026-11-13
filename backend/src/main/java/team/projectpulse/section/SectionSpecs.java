package team.projectpulse.section;

import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class SectionSpecs {

    private SectionSpecs() {
        // Static Specification factories only; never instantiated.
    }

    public static Specification<Section> containsSectionName(String providedSectionName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("sectionName")), "%" + providedSectionName.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Section> belongsToCourse(Integer courseId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("course").get("courseId"), courseId);
    }

    public static Specification<Section> hasActiveStatus(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), isActive);
    }

}
