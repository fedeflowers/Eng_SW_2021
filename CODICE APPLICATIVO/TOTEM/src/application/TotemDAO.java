package application;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// ctrl shift f
public class TotemDAO {

	private Connection connection = null;

	public TotemDAO() {
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

	public boolean controlloAbbonamento(String codUnivoco, String password) {
		boolean passwordValida = false;
		boolean abbonamentoValido = false;

		String query = "SELECT  a.\"fine\", b.\"Password\"\r\n"
				+ "	FROM public.\"Abbonamento\" as a join public.\"Utenti\" as b on a.\"CodiceUnivoco\" = b.\"CodiceUnivoco\"\r\n"
				+ "	where a.\"CodiceUnivoco\" = ?";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, codUnivoco);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				System.out.println("password:" + myRs.getString(2));
				if (myRs.getString(2).equals(password)) {
					passwordValida = true;
				}

				Date dataOdierna = new Date(System.currentTimeMillis()); // data odierna
				Date fine = myRs.getDate(1);
				if (fine == null || fine.after(dataOdierna)) {
					abbonamentoValido = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (abbonamentoValido == true && passwordValida == true) {
			return true;
		} else {
			return false;
		}
	}

	// ritorna la rastrelliera del totem tramite il db
	public Rastrelliera inizializzaRastrelliera(Integer idTotem) {
		Bici[] rastrelliera = new Bici[8];
		int indiceBiciNormale = 0;
		int indiceBiciElettrica = 5;

		String query = "SELECT \"Id\", tipo, danno, totem, seggiolino\r\n"
				+ "FROM public.\"Biciclette\" where totem = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idTotem);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				// la tabella ha 4 campi e il 2 Ë il tipo , lo 1 Ë l'id della bici, Danno Ë il
				// 3;

				if (myRs.getString(2).trim().equals("elettrica") && indiceBiciElettrica < 8) {
					if (myRs.getBoolean(5)) {// seggiolino
						rastrelliera[indiceBiciElettrica] = new BiciElettricaSeggiolino( myRs.getString(1), myRs.getBoolean(3));
						System.out.println("bici inizializzata con il totem, tipo: " + myRs.getString(2) + " indice "
								+ indiceBiciElettrica);
						indiceBiciElettrica++;
					} else {
						rastrelliera[indiceBiciElettrica] = new BiciElettrica( myRs.getString(1),
								myRs.getBoolean(3));
						System.out.println("bici inizializzata con il totem, tipo: " + myRs.getString(2) + " indice "
								+ indiceBiciElettrica);
						indiceBiciElettrica++;
					}

				}

				if (!myRs.getString(2).trim().equals("elettrica") && indiceBiciNormale < 5) {
					rastrelliera[indiceBiciNormale] = new BiciNormale( myRs.getString(1),
							myRs.getBoolean(3));
					System.out.println("bici inizializzata con il totem, tipo: " + myRs.getString(2) + " indice "
							+ indiceBiciNormale);
					indiceBiciNormale++;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Rastrelliera r = Rastrelliera.getInstance(rastrelliera);
		return r;
	}

	public void iniziaNoleggio(String IDutente, String IDbici) {
		Totem t = Totem.getInstance();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String query = "INSERT INTO public.\"Noleggio\"(utente, bici, inizio, fine, multa,totem) VALUES (?, ?, ?, ?, false,?)";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ps.setInt(2, Integer.parseInt(IDbici));
			ps.setTimestamp(3, timestamp);
			ps.setTimestamp(4, null);
			ps.setInt(5, t.getID());
			ps.execute();
			System.out.println("noleggio iniziato: " + timestamp);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// ritorna null se non ha mai noleggiato
	public String controllaNoleggio(String IDutente) {
		Timestamp timestampAttuale = new Timestamp(System.currentTimeMillis());
		String query = "SELECT  fine FROM public.\"Noleggio\" where utente = ?";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				if (myRs.getString(1) == null) {
					System.out.println("l' utente ha gi‡ noleggiato");
					return IDutente;
				}
				Timestamp fine = myRs.getTimestamp(1);
				long difference_In_Time = Math.abs(timestampAttuale.getTime() - fine.getTime());
				long difference_In_Minutes = Math.abs((difference_In_Time / (1000 * 60)));
				if (difference_In_Minutes < 5) {// non sono ancora passati 5 min dall'ultima
												// consegna
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Non sono ancora passati 5 min, aspetta");
					alert.showAndWait();
					System.out.println("tempo da aspettare in minuti: " + (5 - difference_In_Minutes));
					return IDutente;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	// ritorna null se l'utente non ha noleggi in corso
	public String controllaRiconsegna(String IDutente) {
		Timestamp timestampAttuale = new Timestamp(System.currentTimeMillis());
		String query = "SELECT  fine FROM public.\"Noleggio\" where utente = ?";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				if (myRs.getString(1) == null) {
					System.out.println("l' utente ha un noleggio in corso");
					return IDutente;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// per la simulazione, ogni volta che starto il totem voglio avere i noleggi
	// vuoti
	public void eliminaNoleggi() {
		String query = "DELETE FROM public.\"Noleggio\"";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);

			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getUtentiIdConNoleggioAttivo() {
		ArrayList<String> res = new ArrayList<>();

		String query = "SELECT utente\r\n" + "	FROM public.\"Noleggio\" where fine IS NULL; ";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);

			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				if (!res.contains(myRs.getString(1)))
					res.add(myRs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	// sono i root che hanno tolto delle bici per riconsegnarle e applicare la
	// riallocazione delle bici
	public ArrayList<String> getImpiegatiConBiciPrelevate() {
		ArrayList<String> res = new ArrayList<>();

		String query = "SELECT impiegato\r\n" + "	FROM public.\"ImpiegatiRitiroBici\"\r\n"
				+ "	where fine is null;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);

			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				if (!res.contains(myRs.getString(1)))
					res.add(myRs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	public ArrayList<String> getIdBici(String idUtente) {
		ArrayList<String> res = new ArrayList<>();

		String query = "SELECT B.\"Id\"\r\n"
				+ "	FROM public.\"Noleggio\" as N join public.\"Biciclette\" as B ON N.\"bici\" = B.\"Id\" where N.\"utente\" = ? and N.\"fine\" IS NULL;";

		String query2 = "SELECT B.\"Id\"\r\n"
				+ "FROM public.\"ImpiegatiRitiroBici\" as N join public.\"Biciclette\" as B ON N.\"bici\" = B.\"Id\" \r\n"
				+ "where N.\"impiegato\" = ? and N.\"fine\" IS NULL;";
		PreparedStatement ps;
		PreparedStatement ps1;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, idUtente);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				res.add(myRs.getString(1));
			}

			ps1 = connection.prepareStatement(query2);
			ps1.setString(1, idUtente);
			ResultSet myRs2 = ps1.executeQuery();
			while (myRs2.next()) {
				res.add(myRs2.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	// null se non Ë presente la bici
	public Integer getBiciNoleggiataId(String idUtente) {

		String query = "SELECT  bici\r\n" + "	FROM public.\"Noleggio\" where utente =? AND fine is null;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, idUtente);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				return Integer.parseInt(myRs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// null se non Ë presente la bici
	public Boolean getDannoBici(int idBici) {
		String query = "SELECT danno\r\n" + "	FROM public.\"Biciclette\" where \"Id\" = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idBici);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				return myRs.getBoolean(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void inserisciDanno(int idBici) {
		String query = "UPDATE public.\"Biciclette\"\r\n" + "	SET  danno=true\r\n" + "	WHERE \"Id\"=?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idBici);
			ps.execute();

			System.out.println("segnalazione danno inserita correttamente");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setInizioAbbonamento(String IDUtente) { // sull'utente loggato
		String queryControllo = "SELECT tipo, inizio\r\n" + "	FROM public.\"Abbonamento\"\r\n"
				+ "	where \"CodiceUnivoco\" =?"; // prendo tipo e inizio

		String queryAggiorna = "UPDATE public.\"Abbonamento\"\r\n" // l'inizio Ë sempre data odierna, la fine cambia in
																	// base al tipo
				+ "	SET inizio= current_date, fine= ?\r\n" + "	WHERE \"CodiceUnivoco\" = ?";

		PreparedStatement ps;
		PreparedStatement ps1;

		Date dataFine = new Date(System.currentTimeMillis()); // data odierna
		Calendar c = Calendar.getInstance();
		c.setTime(dataFine);

		try {
			ps = connection.prepareStatement(queryControllo);
			ps1 = connection.prepareStatement(queryAggiorna);
			ps.setString(1, IDUtente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			if (myRs.getDate(2) == null) { // se l'inizio Ë null e quindi non Ë ancora stato settato l'abb.
				// se Ë giornaliero lo setto in un modo se Ë settimanale in un altro
				if (myRs.getString(1).trim().equals("Giornaliero")) {
					c.add(Calendar.DATE, 1);
					dataFine = convertUtilToSql(c.getTime());
					ps1.setDate(1, dataFine);
					ps1.setString(2, IDUtente);
					ps1.execute();

				} else if (myRs.getString(1).trim().equals("Settimanale")) {
					c.add(Calendar.DATE, 7);
					dataFine = convertUtilToSql(c.getTime());
					ps1.setDate(1, dataFine);
					ps1.setString(2, IDUtente);
					ps1.execute();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

	// ritorna la differenza in minuti tra inizio noleggio e fine noleggio -1 se
	// qualcosa va storto
	public long diffInizioFineNoleggio(String IDUtente) {
		Timestamp fine = new Timestamp(System.currentTimeMillis());
		Timestamp inizio = null;
		String query = "SELECT  inizio\r\n" + "	FROM public.\"Noleggio\"\r\n"
				+ "	WHERE \"utente\" = ? AND fine IS NULL;";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDUtente);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				inizio = myRs.getTimestamp(1);
			}

			System.out.println(inizio);
			System.out.println(fine);
			long difference_In_Time = inizio.getTime() - fine.getTime();

			/*
			 * long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60));
			 */

			long difference_In_Minutes = (difference_In_Time / (1000 * 60));

			return Math.abs(difference_In_Minutes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	// ritorna il numero di carta se lo trova per l'utente altrimenti null
	public String getNumeroCarta(String IDutente) {
		String query = "SELECT  \"numeroCarta\"\r\n" + "	FROM public.\"Utenti\"\r\n"
				+ "	where \"CodiceUnivoco\" = ?;";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			return myRs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void fineNoleggio(String IDutente) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String query = "UPDATE public.\"Noleggio\"\r\n" + "	SET  fine=current_timestamp\r\n"
				+ "	WHERE utente = ? and fine IS NULL;";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ps.execute();
			System.out.println("noleggio finito: " + timestamp);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// ritorna la consegna tardiva dell'utente altrimenti -1
	private int getConsegnaTardiva(String IDutente) {
		String query = "SELECT\"ConsegnaTardiva\"\r\n" + "	FROM public.\"Abbonamento\"\r\n"
				+ "	where \"CodiceUnivoco\" = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			return myRs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void controlloConsegnaTardiva(String IDutente) {
		String queryAnnullaAbbonamento = "UPDATE public.\"Abbonamento\"\r\n" + "	SET  fine=?\r\n"
				+ "	WHERE \"CodiceUnivoco\" = ?;";

		String queryAumentoConsegnaTardiva = "UPDATE public.\"Abbonamento\"\r\n"
				+ "	SET \"ConsegnaTardiva\"=\"ConsegnaTardiva\"+1\r\n" + "	WHERE \"CodiceUnivoco\" =?;";
		String query = "SELECT inizio, fine FROM public.\"Noleggio\" where \"utente\" = ? order by fine DESC limit 1"; // prende
																														// solo
																														// l'ultima
																														// riconsegna
		Timestamp inizio = null;
		Timestamp fine = null;

		PreparedStatement ps;
		PreparedStatement ps1;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			inizio = myRs.getTimestamp(1);
			fine = myRs.getTimestamp(2);

			long difference_In_Time = Math.abs(inizio.getTime() - fine.getTime());
			long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

			if (difference_In_Hours >= 2) {
				// query per aumentare la consegna tardiva dell'utente e se essa Ë >=3 annullare
				// l'abbonamento
				ps1 = connection.prepareStatement(queryAumentoConsegnaTardiva);
				ps1.setString(1, IDutente);
				ps1.execute();
				if (getConsegnaTardiva(IDutente) >= 3) {// allora l'abbonamento dev'essere annullato
					ps = connection.prepareStatement(queryAnnullaAbbonamento);
					ps.setTimestamp(1, inizio); // setto la data di fine come la data di inizio che sta ad indicare che
												// l'abbonamento non Ë + valido
					ps.setString(2, IDutente);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ritorna la validit‡ della carta altrimenti null
	public Date getValidit‡Carta(String IDutente) {
		String query = "SELECT \"validit‡Carta\"\r\n" + "	FROM public.\"Utenti\"\r\n"
				+ "	where \"CodiceUnivoco\" = ?";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, IDutente);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			return myRs.getDate(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void aggiornaTotem(int idBici, int idTotem) {
		String query = "UPDATE public.\"Biciclette\"\r\n" + "	SET totem=?\r\n" + "	WHERE \"Id\"=?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idTotem);
			ps.setInt(2, idBici);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean controlloLoginRoot(String codice, String password) {
		String query = "SELECT \"Password\"\r\n" + "	FROM public.\"Impiegati\"\r\n"
				+ "	where \"CodiceImpiegato\" = ?";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, codice);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				if (myRs.getString(1).equals(password)) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void rootRitiroBici(String codiceImpiegato, int idBici) {
		String query = "INSERT INTO public.\"ImpiegatiRitiroBici\"(\r\n" + "	impiegato, bici, inizio)\r\n"
				+ "	VALUES (?, ?, current_timestamp);";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, codiceImpiegato);
			ps.setInt(2, idBici);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void riconsegnaRoot(String codiceImpiegato, int idBici) {
		String query = "UPDATE public.\"ImpiegatiRitiroBici\"\r\n" + "	SET  fine=current_timestamp\r\n"
				+ "	WHERE impiegato = ? AND bici = ? AND fine is null";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, codiceImpiegato);
			ps.setInt(2, idBici);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// dato id bici mi restituisce il modello
	public String getModelloBici(int id) {
		String query = "SELECT  tipo\r\n" + "	FROM public.\"Biciclette\"\r\n" + "	Where \"Id\" = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet myRs = ps.executeQuery();
			if (myRs.next()) {
				return myRs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// setta il totem della corrispettiva bici a null nella tabella delle
	// biciclette, questo per quando la bici Ë in noleggio
	// e non ha totem a cui essere agganciata
	public void setTotemNull(int idBici) {
		String query = "UPDATE public.\"Biciclette\"\r\n" + "	SET  totem=NULL\r\n" + "	WHERE \"Id\" = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idBici);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// mitrova se l'abbonamento Ë registrato come studente o no
	public boolean trovaStudenteVeridicit‡(String idUtente) {
		String query = "SELECT studente\r\n"
				+ "	FROM public.\"Abbonamento\"\r\n"
				+ "	where \"CodiceUnivoco\" = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, idUtente);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				return myRs.getBoolean(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*public static void main(String[] args) {
		TotemDAO t = new TotemDAO();
		System.out.println(t.allTotemIds());
	}*/

}
