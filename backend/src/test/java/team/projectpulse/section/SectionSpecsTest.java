package team.projectpulse.section;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SectionSpecsTest {

    @Mock
    Root<Section> root;

    @Mock
    CriteriaQuery<?> query;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    Path<String> sectionName;

    @Mock
    Expression<String> loweredSectionName;

    private Locale originalDefault;

    @BeforeEach
    void saveDefaultLocale() {
        this.originalDefault = Locale.getDefault();
    }

    @AfterEach
    void restoreDefaultLocale() {
        Locale.setDefault(this.originalDefault);
    }

    @Test
    @DisplayName("UC-SEC-find-sections: the section-name search pattern does not depend on the server's default locale")
    void testContainsSectionNameIsLocaleIndependent() {
        // The database side lowercases the column with SQL lower(), which maps 'I' to 'i'.
        // Under a Turkish default locale, Java's no-arg toLowerCase() maps 'I' to dotless 'ı',
        // so the pattern no longer matches what lower(sectionName) produces.
        given(this.root.<String>get("sectionName")).willReturn(this.sectionName);
        given(this.criteriaBuilder.lower(this.sectionName)).willReturn(this.loweredSectionName);
        Locale.setDefault(Locale.forLanguageTag("tr-TR"));

        SectionSpecs.containsSectionName("SECTION").toPredicate(this.root, this.query, this.criteriaBuilder);

        ArgumentCaptor<String> pattern = ArgumentCaptor.forClass(String.class);
        verify(this.criteriaBuilder).like(eq(this.loweredSectionName), pattern.capture());
        assertThat(pattern.getValue()).isEqualTo("%section%");
    }

}
