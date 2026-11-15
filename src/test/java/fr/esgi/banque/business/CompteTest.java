package fr.esgi.banque.business;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Test d'exemple fourni pour vérifier que le projet est bien configuré.
 * À compléter (partie A).
 */
class CompteTest {

	@Test
	void shouldIncreaseBalanceWhenCrediting() {
		// Arrange
		Compte compte = new Compte("FR001", "Alice", new BigDecimal("100.00"), new BigDecimal("200"));

		// Act
		compte.crediter(new BigDecimal("50.00"));

		// Assert
		assertThat(compte.getSolde()).isEqualByComparingTo("150.00");
	}

	@Test
	void verifierNumeroEtTitulaireObligatoire() {
		// Arrange
		BigDecimal soldeInitial = new BigDecimal("100.00");
		BigDecimal decouvertAutorise = new BigDecimal("50.00");

		// Act + Assert : numéro null, vide ou blanc
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte(null, "Dupont", soldeInitial, decouvertAutorise))
				.withMessage("Le numéro de compte est obligatoire");
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("", "Dupont", soldeInitial, decouvertAutorise))
				.withMessage("Le numéro de compte est obligatoire");

		// Act + Assert : titulaire null, vide ou blanc
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("FR001", null, soldeInitial, decouvertAutorise))
				.withMessage("Le titulaire est obligatoire");
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("FR001", "", soldeInitial, decouvertAutorise))
				.withMessage("Le titulaire est obligatoire");
	}

}
