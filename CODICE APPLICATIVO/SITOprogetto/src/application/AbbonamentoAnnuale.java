package application;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AbbonamentoAnnuale extends Abbonamento {

	double SALDO_DEFAULT = 100;
	Pagamento pagamento = Pagamento.getInstance();
	private double costo;

	public AbbonamentoAnnuale() {
		costo = 36;
	};

	//ritorna l'id generato o la stringa vuota
	@Override
	public String creaAbbonamentoFirstTime(String nome, String password, String numeroCarta, Date dataValidit‡,
			boolean studente, String tipo) {
		java.sql.Date inizioAbb = new java.sql.Date(Calendar.getInstance().getTime().getTime()); // data odierna
		Date scadenzaAbbonamento = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(scadenzaAbbonamento);
		c.add(Calendar.DATE, 365); // aggiungo un anno;
		scadenzaAbbonamento = convertUtilToSql(c.getTime());

		if (dataValidit‡.after(scadenzaAbbonamento)) {
			try {
				pagamento.inizializzaCarta(numeroCarta, SALDO_DEFAULT); // creo la carta di default per l'utente
			}catch(SQLException e) {
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("carta gi‡ presente nel db!");
				alert.showAndWait();*/
				System.out.println("carta gi‡ presente nel db!");
				return "";
			}
			if (pagamento.pagamento(numeroCarta, costo)) {
				return abbonamentoDao.inserisciAbbonamentoFirstTime(nome, password, numeroCarta, dataValidit‡, studente, tipo,
						inizioAbb, scadenzaAbbonamento);
			} else {
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Pagamento non riuscito");
				alert.showAndWait();*/
				System.out.println("Pagamento non riuscito");
				return "";
			}
		} else {
			/*Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("La carta deve scadere dopo la fine dell'abbonamento!");
			alert.showAndWait();*/
			System.out.println("La carta deve scadere dopo la fine dell'abbonamento!");
		}
		return "";
	}

	@Override
	public boolean rinnovaAbb(String nomeUtente) {

		java.sql.Date inizioAbb = new java.sql.Date(Calendar.getInstance().getTime().getTime()); // data odierna
		Date scadenzaAbbonamento = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(scadenzaAbbonamento);
		c.add(Calendar.DATE, 365); // aggiungo un anno;
		scadenzaAbbonamento = convertUtilToSql(c.getTime());
		if (abbonamentoDao.getValidit‡Carta(nomeUtente).after(scadenzaAbbonamento)) {
			if (pagamento.pagamento(abbonamentoDao.getNumeroCarta(nomeUtente), costo)) {
				return abbonamentoDao.rinnovaAbb(nomeUtente, inizioAbb, scadenzaAbbonamento, "Annuale");
			} else {
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Pagamento non riuscito");
				alert.showAndWait();*/
				System.out.println("Pagamento non riuscito");
				return false;
			}
		} else {
			/*Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("La carta deve scadere dopo la fine dell'abbonamento!");
			alert.showAndWait();*/
			System.out.println("La carta deve scadere dopo la fine dell'abbonamento!");
			return false;
		}
	}

	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
}
