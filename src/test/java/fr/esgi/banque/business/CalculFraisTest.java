package fr.esgi.banque.business;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @Test
    void fraisDeTenueSontDeDeuxEurosParMois() {
        // Arrange
        CalculFrais calculFrais = new CalculFrais();

        // Act + Assert : titulaire de 40 ans, solde moyen modeste, sans carte premium
        assertThat(calculFrais.fraisMensuels(40, new BigDecimal("500.00"), false))
                .isEqualByComparingTo("2.00");

        // Act + Assert : le montant ne dépend ni de l'âge ni du solde (tant que F3 et F4 ne s'appliquent pas)
        assertThat(calculFrais.fraisMensuels(65, new BigDecimal("0.00"), false))
                .isEqualByComparingTo("2.00");
        assertThat(calculFrais.fraisMensuels(30, new BigDecimal("999.99"), false))
                .isEqualByComparingTo("2.00");
        assertThat(calculFrais.fraisMensuels(50, new BigDecimal("-200.00"), false))
                .isEqualByComparingTo("2.00");
    }

    @Test
    void fraisDeTenueOffertsAuxTitulairesDeMoinsDe26Ans() {
        // Arrange
        CalculFrais calculFrais = new CalculFrais();
        BigDecimal soldeMoyen = new BigDecimal("500.00");

        // Act + Assert : moins de 26 ans, frais de tenue offerts
        assertThat(calculFrais.fraisMensuels(18, soldeMoyen, false)).isEqualByComparingTo("0.00");
        assertThat(calculFrais.fraisMensuels(22, soldeMoyen, false)).isEqualByComparingTo("0.00");
        assertThat(calculFrais.fraisMensuels(25, soldeMoyen, false)).isEqualByComparingTo("0.00");

        // Act + Assert : 26 ans pile, les frais de tenue sont dus
        assertThat(calculFrais.fraisMensuels(26, soldeMoyen, false)).isEqualByComparingTo("2.00");

        // Act + Assert : au-delà de 26 ans, les frais de tenue sont dus
        assertThat(calculFrais.fraisMensuels(27, soldeMoyen, false)).isEqualByComparingTo("2.00");
    }

    @Test
    void fraisDeTenueOffertsSiSoldeMoyenAtteint1000EurosOuPlus() {
        // Arrange
        CalculFrais calculFrais = new CalculFrais();
        int age = 40;

        // Act + Assert : en dessous du seuil, les frais de tenue sont dus
        assertThat(calculFrais.fraisMensuels(age, new BigDecimal("999.99"), false)).isEqualByComparingTo("2.00");
        assertThat(calculFrais.fraisMensuels(age, new BigDecimal("0.00"), false)).isEqualByComparingTo("2.00");

        // Act + Assert : exactement au seuil, les frais de tenue sont offerts
        assertThat(calculFrais.fraisMensuels(age, new BigDecimal("1000"), false)).isEqualByComparingTo("0.00");
        assertThat(calculFrais.fraisMensuels(age, new BigDecimal("1000.00"), false)).isEqualByComparingTo("0.00");

        // Act + Assert : au-dessus du seuil, les frais de tenue sont offerts
        assertThat(calculFrais.fraisMensuels(age, new BigDecimal("1000.01"), false)).isEqualByComparingTo("0.00");
        assertThat(calculFrais.fraisMensuels(age, new BigDecimal("5000.00"), false)).isEqualByComparingTo("0.00");
    }

    @Test
    void cartePremiumAjouteCinqEuros() {
        // Arrange
        CalculFrais calculFrais = new CalculFrais();

        // Act + Assert : tenue due (40 ans, solde moyen modeste), 2,00 + 5,00
        assertThat(calculFrais.fraisMensuels(40, new BigDecimal("500.00"), true)).isEqualByComparingTo("7.00");

        // Act + Assert : tenue offerte par l'âge (moins de 26 ans), seul le supplément reste dû
        assertThat(calculFrais.fraisMensuels(20, new BigDecimal("500.00"), true)).isEqualByComparingTo("5.00");

        // Act + Assert : tenue offerte par le solde moyen (1 000 € ou plus), seul le supplément reste dû
        assertThat(calculFrais.fraisMensuels(40, new BigDecimal("2000.00"), true)).isEqualByComparingTo("5.00");

        // Act + Assert : tenue offerte par l'âge et par le solde moyen, seul le supplément reste dû
        assertThat(calculFrais.fraisMensuels(20, new BigDecimal("2000.00"), true)).isEqualByComparingTo("5.00");

        // Act + Assert : sans carte premium, aucun supplément
        assertThat(calculFrais.fraisMensuels(40, new BigDecimal("500.00"), false)).isEqualByComparingTo("2.00");
        assertThat(calculFrais.fraisMensuels(20, new BigDecimal("2000.00"), false)).isEqualByComparingTo("0.00");
    }
}
