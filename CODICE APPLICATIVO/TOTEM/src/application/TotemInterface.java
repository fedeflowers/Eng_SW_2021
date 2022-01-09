package application;

	import java.sql.Date;
	import java.util.ArrayList;

	import javafx.application.Application;
	import javafx.stage.Stage;
	import javafx.scene.Scene;
	import javafx.scene.layout.BorderPane;
	import javafx.fxml.FXMLLoader;

	//Ë come se fosse un totem controller

	public interface TotemInterface {

		public boolean verificaAbbonamento(String codiceUnivoco, String password);
		public ArrayList<Bici> getBici();
		public Bici[] getBiciArray();
		public int getPostoBici(String modello);
		public int sbloccaBici(String modello);
		public void iniziaNoleggio(String idUtente, String idBici);
		public String getIdUtenteLoggato();
		public String controllaRiconsegna(String IDutente);
		public ArrayList<Integer> postiLiberi();
		public ArrayList<String> idUtentiNoleggio();

		public ArrayList<String> IdImpiegatiRiallocazione() ;

		public ArrayList<String> IdBiciNoleggio(String idUtente);

		public String getModelloBici(int id);
		public String controlloNoleggio(String IDutente) ;
		public boolean riconsegna(String IDutente, String modello, int posto);
		public int getBiciNoleggiataId(String idUtente);

		public void segnalaDanno(int idBici);
		public void setInizioAbbonamento(String IDUtente);
		public Date getValidit‡Carta(String IDutente);
		// controlla la consegna e nel caso aumenta la consegna tardiva e annulla
		// l'abbonamento se necessario
		public void controlloConsegnaTardiva(String IDutente);

		public boolean controlloRoot(String codice, String password);
		public void rootRitiroBici(String codiceImpiegato, int idBici);
		public boolean riconsegnaRoot(String codiceImpiegato, int idBici, String modello, int posto);
		public void setTotemNull(int idBici);

		public int getID();
		
		
	}

