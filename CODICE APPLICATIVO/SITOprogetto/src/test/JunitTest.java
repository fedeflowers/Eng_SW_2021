package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import org.junit.Test;

import application.Abbonamento;
import application.AbbonamentoAnnuale;
import application.AbbonamentoGiornaliero;
import application.AbbonamentoSettimanale;
import application.Pagamento;


public class JunitTest {
	//il db va svuotato prima di fare questi test, altrimenti si avranno valori univoci gi‡ presenti nel db.
	@Test
	public void AbbonamentoAnnualeTest() {
		AbbonamentoAnnuale a = new AbbonamentoAnnuale();
		Date d = new Date(System.currentTimeMillis());
		Date scadenzaAbbonamento = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(scadenzaAbbonamento);
		c.add(Calendar.DATE, 390); // aggiungo + di un anno;
		scadenzaAbbonamento = convertUtilToSql(c.getTime());
		a.creaAbbonamentoFirstTime("jack", "1", "43458451434", scadenzaAbbonamento, false, "Annuale"); // se Ë la prima volta che viene eseguito aggiunto l'abbonamento
		a.creaAbbonamentoFirstTime("jack", "1", "4345845", d, false, null); //con data odierna, errore scadenza carta
		a.creaAbbonamentoFirstTime("jack", "1", "43458451434", scadenzaAbbonamento, false, "Annuale"); // errore carta gi‡ presente
		 
		assertFalse(a.rinnovaAbb("jack")); 
	}
	
	@Test
	public void AbbonamentoSettimanaleTest() {
		AbbonamentoSettimanale a = new AbbonamentoSettimanale();
		Date d = new Date(System.currentTimeMillis());
		Date scadenzaAbbonamento = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(scadenzaAbbonamento);
		c.add(Calendar.DATE, 390); // aggiungo + di un anno;
		scadenzaAbbonamento = convertUtilToSql(c.getTime());
		a.creaAbbonamentoFirstTime("jacky", "1", "49123983", scadenzaAbbonamento, false, "Settimanale"); // se Ë la prima volta che viene eseguito aggiunto l'abbonamento
		a.creaAbbonamentoFirstTime("jacky", "1", "4345845", d, false, null); //con data odierna, errore scadenza carta
		a.creaAbbonamentoFirstTime("jacky", "1", "49123983", scadenzaAbbonamento, false, "Settimanale"); // errore carta gi‡ presente
		 
		a.rinnovaAbb("jacky"); //fallimento
	}
	
	@Test
	public void AbbonamentoGiornalieroTest() {
		AbbonamentoGiornaliero a = new AbbonamentoGiornaliero();
		Date d = new Date(System.currentTimeMillis());
		Date scadenzaAbbonamento = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(scadenzaAbbonamento);
		c.add(Calendar.DATE, 390); // aggiungo + di un anno;
		scadenzaAbbonamento = convertUtilToSql(c.getTime());
		a.creaAbbonamentoFirstTime("jackyp", "1", "491239833", scadenzaAbbonamento, false, "Giornaliero"); // se Ë la prima volta che viene eseguito aggiunto l'abbonamento
		a.creaAbbonamentoFirstTime("jackyp", "1", "4345845", d, false, null); //con data odierna, errore scadenza carta
		a.creaAbbonamentoFirstTime("jackyp", "1", "491239833", scadenzaAbbonamento, false, "Giornaliero"); // errore carta gi‡ presente
		
		a.rinnovaAbb("jackyp"); //fallimento
		 
	}
	
	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
	
	// funziona la prima volta con carta non inizializzata altrimenti si ha exception
	@Test
	public void PagamentoTest() throws SQLException {
		Pagamento p = Pagamento.getInstance();
		p.inizializzaCarta("100900010", 2000);
		p.pagamento("100900012", 5);
		p.pagamento("100900012", 132123); //saldo insufficiente
	}
	@Test
	public void AbbonamentoTest() {
		Abbonamento.inviaFormStudente("ciao@ciao", "mialno", "9999", "crittografia", "4713458144"); //presente nel db codice univoco
		Abbonamento.inviaFormStudente("ciao@ciao", "mialno", "9999", "crittografia", "47134581442131"); // non persente
		assertFalse(Abbonamento.unicit‡NomeUtente("jack"));//nome presente nel db, quindi non univoco
		assertTrue(Abbonamento.unicit‡NomeUtente("fassfasfsfadddassda"));// nome non presente nel db
		Abbonamento.getUltimoCodiceAbbonamentoInserito();
		assertTrue(Abbonamento.login("jack", "1")); // presente nel db
		assertFalse(Abbonamento.login("awfwfa", "1213123")); // non presente nel db
	}
}
