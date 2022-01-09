package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class StatisticaDAO {
	private Connection connection = null;

	public StatisticaDAO() {
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

	// conta i noleggi effettuati al totem indicato
	public int countNoleggiTotem(int idTotem) {
		String query = "SELECT count(totem)\r\n" + "	FROM public.\"Noleggio\"\r\n" + "	where totem = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idTotem);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				return myRs.getInt(1);
			}
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// ritorna la lista di timestamp dell'inizio del noleggio per determinare succ
	// la fascia oraria
	public ArrayList<Time> getTempiInizioNoleggio() {
		ArrayList<Time> result = new ArrayList<>();
		String query = "SELECT inizio\r\n" + "	FROM public.\"Noleggio\";";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				result.add(myRs.getTime(1));
			}
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// ci sono 3 fasce orarie 1: 00.00 - 8.00 2: 8.00 - 16.00 3: 16.00 - 00.00, -1
	// se si verifica un errore
	public int determinaFasciaOraria(Time t) {
		Time primo = new Time(1000 * 60 * 60 * 7);
		Time secondo = new Time(1000 * 60 * 60 * 15);
		Time terzo = new Time(1000 * 60 * 60 * 23);
		if ((t.after(terzo) && t.before(primo)) || t.equals(primo)) {
			return 1;
		} else if ((t.after(primo) && t.before(secondo)) || t.equals(secondo)) {
			return 2;
		} else if ((t.after(secondo) && t.before(terzo)) || t.equals(terzo)) {
			return 3;
		}
		return -1;
	}

	// ritora tutti gli id dei totem nel db
	public ArrayList<Integer> allTotemIds() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		String query = "SELECT \"Id\"\r\n" + "	FROM public.\"Totem\";";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				res.add(myRs.getInt(1));
			}
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	// ritorna il numero di bici utilizzare nella data indicata
	public int numeroBiciUtilizzateGiorno(Date d) {
		String query = "SELECT count(utente)\r\n" + "	FROM public.\"Noleggio\"\r\n"
				+ "	where (inizio)::TIMESTAMP::DATE = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setDate(1, d);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				return myRs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
