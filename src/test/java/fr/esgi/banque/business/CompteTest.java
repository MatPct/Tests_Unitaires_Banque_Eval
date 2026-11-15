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

	@Test
	void verifierSoldeInitialEtDecouvertAutoriseObligatoires() {
		// Arrange
		BigDecimal soldeInitial = new BigDecimal("100.00");
		BigDecimal decouvertAutorise = new BigDecimal("50.00");

		// Act + Assert : solde initial null
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("FR001", "Dupont", null, decouvertAutorise))
				.withMessage("Le solde initial et le découvert autorisé sont obligatoires");

		// Act + Assert : découvert autorisé null
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("FR001", "Dupont", soldeInitial, null))
				.withMessage("Le solde initial et le découvert autorisé sont obligatoires");

		// Act + Assert : découvert autorisé strictement négatif
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("FR001", "Dupont", soldeInitial, new BigDecimal("-0.01")))
				.withMessage("Le découvert autorisé doit être positif ou nul");
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new Compte("FR001", "Dupont", soldeInitial, new BigDecimal("-50")))
				.withMessage("Le découvert autorisé doit être positif ou nul");
	}

	@Test
	void crediterNAccepteQuUnMontantStrictementPositif() {
		// Arrange
		Compte compte = new Compte("FR001", "Dupont", new BigDecimal("100.00"), new BigDecimal("50.00"));

		// Act + Assert : montant null
		assertThatIllegalArgumentException()
				.isThrownBy(() -> compte.crediter(null))
				.withMessage("Le montant à créditer doit être strictement positif");

		// Act + Assert : montant nul
		assertThatIllegalArgumentException()
				.isThrownBy(() -> compte.crediter(BigDecimal.ZERO))
				.withMessage("Le montant à créditer doit être strictement positif");

		// Act + Assert : montant négatif
		assertThatIllegalArgumentException()
				.isThrownBy(() -> compte.crediter(new BigDecimal("-0.01")))
				.withMessage("Le montant à créditer doit être strictement positif");


	}

	@Test
	void debiterNAccepteQuUnMontantStrictementPositif() {
		// Arrange
		Compte compte = new Compte("FR001", "Dupont", new BigDecimal("100.00"), new BigDecimal("50.00"));

		// Act + Assert : montant null
		assertThatIllegalArgumentException()
				.isThrownBy(() -> compte.debiter(null))
				.withMessage("Le montant à débiter doit être strictement positif");

		// Act + Assert : montant nul
		assertThatIllegalArgumentException()
				.isThrownBy(() -> compte.debiter(BigDecimal.ZERO))
				.withMessage("Le montant à débiter doit être strictement positif");

		// Act + Assert : montant négatif
		assertThatIllegalArgumentException()
				.isThrownBy(() -> compte.debiter(new BigDecimal("-0.01")))
				.withMessage("Le montant à débiter doit être strictement positif");
	}
}
