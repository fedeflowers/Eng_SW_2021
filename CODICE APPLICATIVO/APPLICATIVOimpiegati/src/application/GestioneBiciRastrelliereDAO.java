package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.sun.glass.ui.Pixels.Format;

public class GestioneBiciRastrelliereDAO {

	private Connection connection = null;

	public GestioneBiciRastrelliereDAO() {
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

	// ritorna il max index nell tabella bici oppure 0
	public int getMaxIdBici() {
		String query = "SELECT max(\"Id\")\r\n" + "	FROM public.\"Biciclette\";";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ResultSet myRs = ps.executeQuery();
			myRs.next();
			return myRs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// aggiungi bici al db
	public void aggiungiBici(String tipo, boolean danno, boolean seggiolino) {
		String query = "INSERT INTO public.\"Biciclette\"(\r\n" + "	\"Id\", tipo, danno, seggiolino)\r\n"
				+ "	VALUES (?, ?, ?, ?);";
		int id = getMaxIdBici() + 1;
		PreparedStatement ps;
		try {
			// il totem lo metto a null
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, tipo);
			ps.setBoolean(3, danno);
			ps.setBoolean(4, seggiolino);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// l'impiegato che aggiunge la bici al db la può attaccare ai totem
	public void insRitiroBici(String codiceImpiegato, int idBici) {
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

	// restituisce gli id delle bici staccate dai totem
	private ArrayList<Integer> getIdBiciSenzaTotem() {
		ArrayList<Integer> result = new ArrayList<>();
		String query = "SELECT \"Id\"\r\n" + "	FROM public.\"Biciclette\"\r\n" + "	where totem is null";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ResultSet myRs = ps.executeQuery();
			while (myRs.next()) {
				result.add(myRs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// restituisce arralist degli id delle bici staccate dai totem e senza noleggio
	// attivo.
	public ArrayList<Integer> getIdBicidaEliminare() {
		ArrayList<Integer> biciSenzaTotem = getIdBiciSenzaTotem();
		ArrayList<Integer> biciSenzaTotemResult = getIdBiciSenzaTotem();

		String query = "SELECT  bici\r\n" + "	FROM public.\"Noleggio\"\r\n" + "	where fine is null and bici = ?";
		PreparedStatement ps;
		for (int id : biciSenzaTotem) {
			try {
				ps = connection.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet myRs = ps.executeQuery();
				while(myRs.next()) {
					if(myRs.getString(1)!= null) {  //se è diverso da null vuol dire che è noleggiata e quindi la devo togliere
						Integer i = myRs.getInt(1);
						biciSenzaTotemResult.remove(i); //exception perchè lo tolgo mentre ci scorro sopra quindi ne uso due così non mi da l'exception
					}
				}
				

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return biciSenzaTotemResult;
	}
	
	public void eliminaBici(Integer idBici) {
		String query = "DELETE FROM public.\"Biciclette\"\r\n"
				+ "	WHERE \"Id\" = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idBici);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//seleziona l'ultimo id aggiunto nella tabella
	private int getMaxIdTotem() {
		String query = "SELECT max(\"Id\")\r\n"
				+ "	FROM public.\"Totem\"";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				return myRs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void insertTotem(String via) {
		String query = "INSERT INTO public.\"Totem\"(\r\n"
				+ "	\"Id\", \"Via\")\r\n"
				+ "	VALUES (?, ?);";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, getMaxIdTotem()+1); //devo farlo perchè non ho messo serial nel db
			ps.setString(2, via);
			ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//dice se la via è già presente nel db
	public boolean trovaVieNonUnivocheTotem(String via) {//sostituisce una primary key estesa anche alla via
		String query = "SELECT  \"Via\"\r\n"
				+ "	FROM public.\"Totem\"\r\n"
				+ "	where \"Via\" = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, via);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				if(myRs.getString(1)!=null) {
					return true; //già presente nel db
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean controlloTotemVuoto(int idTotem) {
		String query = "SELECT \"Id\"\r\n"
				+ "	FROM public.\"Biciclette\"\r\n"
				+ "	where totem = ?;";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idTotem);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				if(myRs.getString(1)!=null) {
					return false; // sono presenti bici attaccate
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//ritorna la lista di id di totem vuoti
	public ArrayList<Integer> getListaTotemVuoti(){
		 ArrayList<Integer> result = new ArrayList<>();
		 
		 String query = "SELECT \"Id\"\r\n"
		 		+ "	FROM public.\"Totem\";";
		 PreparedStatement ps;
			try {
				ps = connection.prepareStatement(query);
				ResultSet myRs = ps.executeQuery();
				while(myRs.next()) {
					if(controlloTotemVuoto(myRs.getInt(1))) { //se è vuoto lo aggiungi al result
						result.add(myRs.getInt(1));
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
	}
	
	public void deleteTotem(int idTotem) {
		String query = "DELETE FROM public.\"Totem\"\r\n"
				+ "	WHERE \"Id\" = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idTotem);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//ritorna la disponibilità di bici di un totem
	public int getDisponibilitàBiciTotem(int idTotem) {
		String query = "SELECT count(\"Id\")\r\n"
				+ "	FROM public.\"Biciclette\"\r\n"
				+ "	where totem = ?";
		
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idTotem);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				return myRs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public ArrayList<String> ListaTotemDisponibilità(){
		ArrayList<String> result = new ArrayList<String>();
		
		String query = "SELECT \"Id\",\"Via\"\r\n"
				+ "	FROM public.\"Totem\";";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				String riga= "";
				 riga+="id: "+ myRs.getInt(1)+" Via: "+myRs.getString(2)+" disponibilità Biciclette: "+getDisponibilitàBiciTotem(myRs.getInt(1));//prendo l'id del totem e aggiungo disponibilità
				 result.add(riga);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
		
		
	/*public static void main(String[] args) {
		GestioneBiciRastrelliereDAO g = new GestioneBiciRastrelliereDAO();
		Time t = new Time(1000*60*60 *7);
		System.out.println(g.determinaFasciaOraria(t));
	}*/
	
}
