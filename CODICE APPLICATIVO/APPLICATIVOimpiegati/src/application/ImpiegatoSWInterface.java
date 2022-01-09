package application;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public interface ImpiegatoSWInterface{

	public boolean loginRoot(String codice, String password);

	public void aggiungiBici(String tipo, boolean danno, boolean seggiolino);
	public String getIdUtenteLoggato() ;
	public void insRitiroBici(String codiceImpiegato, int idBici);
	public int getMaxIdBici();
	public ArrayList<Integer> getIdBicidaEliminare() ;
	public void eliminaBici(Integer idBici);

	public void insertTotem(String via);
	public boolean trovaVieNonUnivocheTotem(String via);
	public ArrayList<Integer> getListaTotemVuoti();

	public void deleteTotem(int idTotem);

	public ArrayList<String> ListaTotemDisponibilità();

	// calcola la media delle bici utilizzare al giorno data inizio stabilita di
	// fine è la data corrente
	public int numeroBiciMedioUtilizzatoAlGiorno() throws ParseException;

	// ritorna l'id del totem che è stato più utilizzato
	public int getTotempiùUsato();
	public int determinaFasciaOrariaPiùRichiesta();
}
