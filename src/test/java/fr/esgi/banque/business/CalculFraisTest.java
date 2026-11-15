package fr.esgi.banque.business;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class CalculFraisTest {

    @Test
    void verifierTitulaireMajeurEtSoldeMoyenObligatoire() {
        // Arrange
        CalculFrais calculFrais = new CalculFrais();
        BigDecimal soldeMoyen = new BigDecimal("500.00");

        // Act + Assert : titulaire mineur
        assertThatIllegalArgumentException()
                .isThrownBy(() -> calculFrais.fraisMensuels(17, soldeMoyen, false))
                .withMessage("Le titulaire doit être majeur");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> calculFrais.fraisMensuels(0, soldeMoyen, false))
                .withMessage("Le titulaire doit être majeur");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> calculFrais.fraisMensuels(-1, soldeMoyen, false))
                .withMessage("Le titulaire doit être majeur");

        // Act + Assert : solde moyen null (avec un titulaire majeur)
        assertThatIllegalArgumentException()
                .isThrownBy(() -> calculFrais.fraisMensuels(30, null, false))
                .withMessage("Le solde moyen est obligatoire");

        // Act + Assert : 18 ans pile est accepté
        assertThatCode(() -> calculFrais.fraisMensuels(18, soldeMoyen, false))
                .doesNotThrowAnyException();
    }
}
