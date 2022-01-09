package application;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AbbonamentoDAO {

	private static String ultimoCodiceAbbonamentoInserito = null;
	private Connection connection = null;
	private static final long LIMIT = 10000000000L;
	private static long last = 0;

	public AbbonamentoDAO() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ProgettoUML", "postgres",
					"postgres");

			if (connection != null) {
				System.out.println("connection ok");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ritorna l'id dell'abbonaemnto
	public String inserisciAbbonamentoFirstTime(String nome, String password, String numeroCarta, Date dataValidit‡,
			boolean studente, String tipo, Date inizioAbbonamento, Date scadenzaAbbonamento) {
		// devo fare due query per riempire due tabelle con comandi di insert
		long IDgenerated = IDuniqueGenerator();
		// java.sql.Date today = new
		// java.sql.Date(Calendar.getInstance().getTime().getTime()); // data odierna
		String queryAbbonamento = "INSERT INTO public.\"Abbonamento\"(\r\n"
				+ "	\"CodiceUnivoco\", tipo, inizio, fine, studente)\r\n" + "	VALUES (?, ?, ?, ?, ?);";
		PreparedStatement ps2;
		try {
			ps2 = connection.prepareStatement(queryAbbonamento);
			ps2.setString(1, String.valueOf(IDgenerated));
			ps2.setString(2, tipo);
			ps2.setDate(3, inizioAbbonamento);
			ps2.setDate(4, scadenzaAbbonamento);
			ps2.setBoolean(5, studente);

			ps2.execute();
			String queryUtenti = "INSERT INTO public.\"Utenti\"(\r\n"
					+ "	\"CodiceUnivoco\", \"Password\", \"Nome\", \"numeroCarta\", \"validit‡Carta\")\r\n"
					+ "	VALUES (?, ?, ?, ?, ?);";
			PreparedStatement ps;
			ps = connection.prepareStatement(queryUtenti);
			ps.setString(1, String.valueOf(IDgenerated));
			ps.setString(2, password);
			ps.setString(3, nome);
			ps.setString(4, numeroCarta);
			ps.setDate(5, dataValidit‡);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();

		}

		System.out.println("Abbonamento inserito correttamente nel db");
		ultimoCodiceAbbonamentoInserito = String.valueOf(IDgenerated);
		return Long.toString(IDgenerated);

	}

	public long IDuniqueGenerator() {
		// 10 digits.
		long id = System.currentTimeMillis() % LIMIT;
		if (id <= last) {
			id = (last + 1) % LIMIT;
		}
		return last = id;

	}

	public boolean controllaUnicit‡NomeUtente(String nome) {
		String query = "SELECT \"Nome\"\r\n" + "	FROM public.\"Utenti\"\r\n" + "	where \"Nome\" = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, nome);
			ResultSet myRs = ps.executeQuery();
			if (!myRs.next()) {
				return true; // nome utente unico, non ho trovato nessun risultato
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;// nome utente non unico
	}

	public static String getUltimoCodiceAbbonamentoInserito() {
		return ultimoCodiceAbbonamentoInserito;
	}

	public void inserisciFormsStudente(String email, String universit‡, String matricola, String corso,
			String codiceUnivocoAbb) {
		String query = "INSERT INTO public.\"Universit‡\"(\r\n"
				+ "	\"Email\", \"Matricola\", \"Universit‡\", \"Corso\", \"Abbonamento\")\r\n"
				+ "	VALUES (?, ?, ?, ?, ?);";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, matricola);
			ps.setString(3, universit‡);
			ps.setString(4, corso);
			ps.setString(5, codiceUnivocoAbb);

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// ritorna la data di validit‡ del nomeutente, null in caso di exception
	public Date getValidit‡Carta(String nomeUtente) {
		String query = "SELECT  \"validit‡Carta\"\r\n"
				+ "	FROM public.\"Utenti\" as u\r\n"
				+ "	where u.\"Nome\" = ?";
		
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, nomeUtente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			return myRs.getDate(1);
	

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean login(String nomeUtente, String password) {
		String query = "SELECT  \"Password\"\r\n" + "	FROM public.\"Utenti\" as U where U.\"Nome\" = ?";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, nomeUtente);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				if (myRs.getString(1).equals(password)) {
					System.out.println("login corretto");
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	//avrei potuto usare il cascade ma non l'ho fatto...
	public boolean rinnovaAbb(String nomeUtente, Date inizio, Date fine, String tipo) {
		boolean studente = false;
		String IDgenerated = String.valueOf(IDuniqueGenerator());
		String getCodUnivoco = "SELECT A.\"CodiceUnivoco\"\r\n"
				+ "	FROM public.\"Abbonamento\" AS A join public.\"Utenti\" AS U on A.\"CodiceUnivoco\" = U.\"CodiceUnivoco\"\r\n"
				+ "	where U.\"Nome\" =?;";

		String queryAbbFinito = "SELECT fine,tipo\r\n"
				+ "	FROM public.\"Abbonamento\" AS A join public.\"Utenti\" AS U on A.\"CodiceUnivoco\" = U.\"CodiceUnivoco\"\r\n"
				+ "	where U.\"Nome\" = ?;";

		String queryVerificaStudente = "SELECT \"Abbonamento\"\r\n"
				+ "	FROM public.\"Universit‡\" as U join public.\"Utenti\" as T ON U.\"Abbonamento\" = T.\"CodiceUnivoco\" \r\n"
				+ "	where T.\"Nome\" = ?";

		String queryAggiungiAbbonamento = "INSERT INTO public.\"Abbonamento\"(\r\n"
				+ "	\"CodiceUnivoco\", tipo, inizio, fine, studente)\r\n" + "	VALUES (?, ?, ?, ?, ?);";

		String queryAggiornaTabUtenti = "UPDATE public.\"Utenti\"\r\n" + "	SET \"CodiceUnivoco\"=?\r\n"
				+ "	WHERE \"CodiceUnivoco\" = ?;";

		String queryAggiornaTabUni = "UPDATE public.\"Universit‡\"\r\n" + "	SET  \"Abbonamento\"=?\r\n"
				+ "	WHERE \"Abbonamento\" = ?;";

		PreparedStatement ps1;
		PreparedStatement ps2;
		PreparedStatement ps3;
		PreparedStatement ps4;
		PreparedStatement ps5;
		PreparedStatement ps6;

		try {
			ps1 = connection.prepareStatement(queryAbbFinito);
			ps1.setString(1, nomeUtente);
			ResultSet myRs1 = ps1.executeQuery();
			myRs1.next();
			if (myRs1.getDate(1) == null ) { // allora ho un abbonamento in corso
				System.out.println("abbonamento in corso");
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Impossibile rinnovare abbonamento, Ë presente un abbonamento ancora in corso");
				alert.showAndWait();*/
				System.out.println("Impossibile rinnovare abbonamento, Ë presente un abbonamento ancora in corso");
				return false;
			}else if(myRs1.getString(2).trim().equals("Annuale")) {  // incaso di abbonamento annuale non basta che sia null
				Date scadenzaAbb = myRs1.getDate(1);
				Date today = new Date(System.currentTimeMillis()); // data odierna
				if(today.before(scadenzaAbb)) {
					System.out.println("Impossibile rinnovare abbonamento, Ë presente un abbonamento ancora in corso");
					/*Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Impossibile rinnovare abbonamento, Ë presente un abbonamento ancora in corso");
					alert.showAndWait();*/
					return false;
				}
			}

			ps2 = connection.prepareStatement(queryVerificaStudente);
			ps2.setString(1, nomeUtente);
			ResultSet myRs2 = ps2.executeQuery();
			if (myRs2.next()) { // allora ho uno studente
				System.out.println("ok studente");
				studente = true;
			}
			// a questo punto devo inserire il nuovo abbonamento e aggiornare il codice
			// univoco delle tabelle dell'utente e dell'universit‡
			// devo anche controllare che l'abbonamente precedente sia finito

			// verifico pagamento e poi aggiungo rinnovo
			ps3 = connection.prepareStatement(queryAggiungiAbbonamento);
			ps3.setString(1, IDgenerated);
			ps3.setString(2, tipo);
			ps3.setDate(3, inizio);
			ps3.setDate(4, fine);
			ps3.setBoolean(5, studente);
			ps3.execute();
			System.out.println("aggiunto");

			// AGGIORNO LE ALTRE TABELLE
			ps4 = connection.prepareStatement(getCodUnivoco);
			ps4.setString(1, nomeUtente);
			ResultSet myRs4 = ps4.executeQuery();
			myRs4.next();
			String OldCodiceUnivoco = myRs4.getString(1);

			ps5 = connection.prepareStatement(queryAggiornaTabUtenti);
			ps5.setString(1, IDgenerated);
			ps5.setString(2, OldCodiceUnivoco);
			ps5.execute();

			ps6 = connection.prepareStatement(queryAggiornaTabUni);
			ps6.setString(1, IDgenerated);
			ps6.setString(2, OldCodiceUnivoco);
			ps6.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("codice Univoco Rinnovato: "+ IDgenerated);
		return true;

	}

	// null se non lo trova
	public String getNumeroCarta(String nomeUtente) {
		String query = "\r\n"
				+ "	SELECT  \"numeroCarta\"\r\n"
				+ "	FROM public.\"Utenti\" \r\n"
				+ "	WHERE \"Nome\" = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, nomeUtente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			return myRs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
