package application;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class ImpiegatoSW implements ImpiegatoSWInterface {
	private static ImpiegatoSW single_instance = null;
	private String idUtenteLoggato;// utente loggato
	private GestioneBiciRastrelliereDAO GBRDao;
	private StatisticaInterface stats;

	private ImpiegatoSW() {
		GBRDao = new GestioneBiciRastrelliereDAO();
		stats = Statistica.getInstance();
	}

	public static ImpiegatoSW getInstance() {
		if (single_instance == null) {
			single_instance = new ImpiegatoSW();
		}

		return single_instance;
	}

	public boolean loginRoot(String codice, String password) {
		if (GBRDao.controlloLoginRoot(codice, password)) {
			idUtenteLoggato = codice;
			return true;
		}
		return false;
	}

	public void aggiungiBici(String tipo, boolean danno, boolean seggiolino) {
		GBRDao.aggiungiBici(tipo, danno, seggiolino);
	}

	public String getIdUtenteLoggato() {
		return idUtenteLoggato;
	}

	public void insRitiroBici(String codiceImpiegato, int idBici) {
		GBRDao.insRitiroBici(codiceImpiegato, idBici);
	}

	public int getMaxIdBici() {
		return GBRDao.getMaxIdBici();
	}

	public ArrayList<Integer> getIdBicidaEliminare() {
		return GBRDao.getIdBicidaEliminare();
	}

	public void eliminaBici(Integer idBici) {
		GBRDao.eliminaBici(idBici);
	}

	public void insertTotem(String via) {
		GBRDao.insertTotem(via);
	}

	public boolean trovaVieNonUnivocheTotem(String via) {
		return GBRDao.trovaVieNonUnivocheTotem(via);
	}

	public ArrayList<Integer> getListaTotemVuoti() {
		return GBRDao.getListaTotemVuoti();
	}

	public void deleteTotem(int idTotem) {
		GBRDao.deleteTotem(idTotem);
	}

	public ArrayList<String> ListaTotemDisponibilit�() {
		return GBRDao.ListaTotemDisponibilit�();
	}

	// calcola la media delle bici utilizzare al giorno data inizio stabilita di
	// fine � la data corrente
	public int numeroBiciMedioUtilizzatoAlGiorno() throws ParseException {
		return stats.numeroBiciMedioUtilizzatoAlGiorno();
	}

	// ritorna l'id del totem che � stato pi� utilizzato
	public int getTotempi�Usato() {
		return stats.getTotempi�Usato();
	}

	public int determinaFasciaOrariaPi�Richiesta() {
		return stats.determinaFasciaOrariaPi�Richiesta();
	}

	/*
	 * public static void main(String[] args) throws ParseException { ImpiegatoSW i
	 * = new ImpiegatoSW();
	 * System.out.println(i.numeroBiciMedioUtilizzatoAlGiorno()); }
	 */
}
