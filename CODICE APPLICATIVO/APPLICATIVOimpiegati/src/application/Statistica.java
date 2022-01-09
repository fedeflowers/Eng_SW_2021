package application;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class Statistica implements StatisticaInterface {

	private static Statistica single_instance = null;
	private StatisticaDAO statsDao;

	private Statistica() {
		statsDao = new StatisticaDAO();
	}

	public static Statistica getInstance() {
		if (single_instance == null) {
			single_instance = new Statistica();
		}

		return single_instance;
	}
	
	public int numeroBiciMedioUtilizzatoAlGiorno() throws ParseException {
		int finalResult = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		Date dataOdierna = new Date(System.currentTimeMillis()); // data di fine calcolo
		Date dataInizioCalcolo = convertUtilToSql(sdf.parse("2021/03/02"));

		long diff = dataOdierna.getTime() - dataInizioCalcolo.getTime();
		long days = (diff / (1000 * 60 * 60 * 24)); // diff in giorni tra le due date

		LocalDateTime today = LocalDateTime.now();// data di oggi ma con il metodo per aggiugnere 1 giorno
		today = today.minusDays(days); // parte dalla data di inizio calcolo

		for (long i = 0; i <= days; i += 1) {
			Date res = convertUtilToSql(Date.from((today.plusDays(i).atZone(ZoneId.systemDefault()).toInstant()))); // ottengo
																													// la
																													// data
																													// con
																													// 1
																													// giorno
																													// in
																													// +
																													// per
																													// calcolarci
																													// il
																													// numero
																													// di
																													// bici
																													// utilizzato
																													// in
																													// quella
																													// data
			int numeroBiciGiorno = statsDao.numeroBiciUtilizzateGiorno(res);
			System.out.println("Bici noleggiate al giorno: " + res + " numero: " + numeroBiciGiorno);

			finalResult += numeroBiciGiorno;
		}
		System.out.println();
		return finalResult / ((int) days + 1);

	}

	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

	// ritorna l'id del totem che è stato più utilizzato
	public int getTotempiùUsato() {
		int max = 0;
		int result = 1; // l'id del totem 1 c'è sempre, ciè almeno un totem
		ArrayList<Integer> listaTotems = statsDao.allTotemIds();
		for (int id : listaTotems) {
			if (statsDao.countNoleggiTotem(id) > max) {
				max = statsDao.countNoleggiTotem(id);
				result = id; // inserisco l'id del totem con noleggi maggiori
			}
		}
		return result;
	}

	public int determinaFasciaOrariaPiùRichiesta() {
		ArrayList<Time> tempi = statsDao.getTempiInizioNoleggio();
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		for (Time t : tempi) {
			if (statsDao.determinaFasciaOraria(t) == 1) {
				count1++;
			} else if (statsDao.determinaFasciaOraria(t) == 2) {
				count2++;
			} else {
				count3++;
			}
		}
		System.out.println("Fasce 1: " + count1);
		System.out.println("Fasce 2: " + count2);
		System.out.println("Fasce 3: " + count3);
		int max = count1;
		int result = 1;
		if (count2 > max) {
			max = count2;
			result = 2;
		}
		if (count3 > max) {
			max = count3;
			result = 3;
		}
		return result;
	}

	
}
