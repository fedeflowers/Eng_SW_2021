package application;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AbbonamentoSettimanale extends Abbonamento {

	double SALDO_DEFAULT = 100;
	Pagamento pagamento = Pagamento.getInstance();
	private double costo;
	// per abbonamento giornaliero e settimanale siccome non so quando andr‡ ad
	// attivarli dico che la carta deve avere validit‡ almeno un mese di scarto da
	// quando viene creato l'abbonamento
	// successivamente se la cart‡ sar‡ scaduta durante l'attivazione
	// dell'abbonamento non sar‡ possibile noleggiare

	public AbbonamentoSettimanale() {
		costo = 9;
	};

	// per abbonamento giornaliero e settimanale siccome non so quando andr‡ ad
	// attivarli dico che la carta deve avere validit‡ almeno un mese di scarto da
	// quando viene creato l'abbonamento
	// successivamente se la cart‡ sar‡ scaduta durante l'attivazione
	// dell'abbonamento non sar‡ possibile noleggiare

	@Override
	public String creaAbbonamentoFirstTime(String nome, String password, String numeroCarta, Date dataValidit‡,
			boolean studente, String tipo) {

		Date dataValidit‡MassimaCarta = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(dataValidit‡MassimaCarta);
		c.add(Calendar.DATE, 30);
		dataValidit‡MassimaCarta = convertUtilToSql(c.getTime()); // data odierna + 30
		if (dataValidit‡.after(dataValidit‡MassimaCarta)) { // allora scade dopo quindi ok
			try {
				pagamento.inizializzaCarta(numeroCarta, SALDO_DEFAULT); // creo la carta di default per l'utente
			} catch (SQLException e) {
				/*
				 * Alert alert = new Alert(AlertType.INFORMATION);
				 * alert.setContentText("carta gi‡ presente nel db!"); alert.showAndWait();
				 */
				System.out.println("carta gi‡ presente nel db!");
				return "";
			}

			if (pagamento.pagamento(numeroCarta, costo)) {
				return abbonamentoDao.inserisciAbbonamentoFirstTime(nome, password, numeroCarta, dataValidit‡, studente,
						tipo, null, null);
			} else {
				/*
				 * Alert alert = new Alert(AlertType.INFORMATION);
				 * alert.setContentText("Pagamento non riuscito"); alert.showAndWait();
				 */
				System.out.println("Pagamento non riuscito");
			}

		} else {
			/*
			 * Alert alert = new Alert(AlertType.INFORMATION); alert.
			 * setContentText("La carta deve avere scadenza uguale almeno ad un mese dalla creazione dell'abbonamento"
			 * ); alert.showAndWait();
			 */
			System.out
					.println("La carta deve avere scadenza uguale almeno ad un mese dalla creazione dell'abbonamento");
		}
		return "";
	}

	@Override
	public boolean rinnovaAbb(String nomeUtente) {
		Date dataValidit‡MassimaCarta = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(dataValidit‡MassimaCarta);
		c.add(Calendar.DATE, 30);
		dataValidit‡MassimaCarta = convertUtilToSql(c.getTime()); // data odierna + 30
		if (abbonamentoDao.getValidit‡Carta(nomeUtente).after(dataValidit‡MassimaCarta)) { // allora scade dopo quindi
																							// ok
			if (pagamento.pagamento(abbonamentoDao.getNumeroCarta(nomeUtente), costo)) {
				return abbonamentoDao.rinnovaAbb(nomeUtente, null, null, "Settimanale");
			} else {
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Pagamento non riuscito");
				alert.showAndWait();*/
				System.out.println("Pagamento non riuscito");
				return false;
			}

		} else {
			/*
			 * Alert alert = new Alert(AlertType.INFORMATION); alert.
			 * setContentText("La carta deve avere scadenza uguale almeno ad un mese dalla creazione dell'abbonamento"
			 * ); alert.showAndWait();
			 */
			System.out.println("La carta deve avere scadenza uguale almeno ad un mese dalla creazione dell'abbonamento");
			return false;
		}
	}

	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
}
