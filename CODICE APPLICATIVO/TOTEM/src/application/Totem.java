package application;

import java.sql.Date;
import java.util.ArrayList;


//è come se fosse un totem controller

public class Totem implements TotemInterface{

	private PagamentoInterface pagamento = Pagamento.getInstance();
	private static Totem single_instance = null;
	private final int ID = 1; // da cambiare per istanziare un totem diverso dal 1 in base al database,
								// cambieranno le bici attaccate ad esso.
	// private String posizione; // =via
	private String idUtente;// utente loggato
	private RastrellieraInterface rastrelliera;
	private TotemDAO totemDao = new TotemDAO();
	// metodo che lo aggiunge al db o lo prende già dal db in base se posizione ci
	// sia già o no nel db

	private Totem() {

		rastrelliera = totemDao.inizializzaRastrelliera(ID);

	}

	public static Totem getInstance() {
		if (single_instance == null) {
			single_instance = new Totem();
			// DA ELIMINARE ALLA CONSEGNA DEL PROGETTO
			//single_instance.totemDao.eliminaNoleggi(); // restart al restart della simulazione. Si ripresenta la
														// situazione in cui nessuno ha noleggiato
		}

		return single_instance;
	}

	public boolean verificaAbbonamento(String codiceUnivoco, String password) {
		if (totemDao.controlloAbbonamento(codiceUnivoco, password)) {
			idUtente = codiceUnivoco; // id dell'utente loggato
			return true;
		}
		return false;
	}

	public ArrayList<Bici> getBici() {
		ArrayList<Bici> bici = rastrelliera.disponibilitàBici();
		for (Bici a : bici) {
			System.out.println("modello disponibile: " + a.getModello());
		}
		return bici;
	}

	public Bici[] getBiciArray() {
		return rastrelliera.disponibilitàBiciArray();
	}

	public int getPostoBici(String modello) {
		return rastrelliera.getPosto(modello);
	}

	// restituisce il primo posto con tale modello di bici in ordine crescente in
	// base alla disponibilità
	// ritorna -1 se il modello di bici non è presente
	public int sbloccaBici(String modello) {
		int posto = rastrelliera.getPosto(modello);
		if (posto >= 0) {
			//Bici b = rastrelliera.getBici(posto);//inutile
			rastrelliera.sblocca(posto);
			return posto;
		}

		return -1;

	}

	public void iniziaNoleggio(String idUtente, String idBici) {
		totemDao.iniziaNoleggio(idUtente, idBici);
	}

	public String getIdUtenteLoggato() {
		return idUtente;
	}

	public String controllaRiconsegna(String IDutente) {
		return totemDao.controllaRiconsegna(IDutente);
	}

	public ArrayList<Integer> postiLiberi() {
		return rastrelliera.getPostiLiberi();
	}

	public ArrayList<String> idUtentiNoleggio() {
		return totemDao.getUtentiIdConNoleggioAttivo();
	}

	public ArrayList<String> IdImpiegatiRiallocazione() {
		return totemDao.getImpiegatiConBiciPrelevate();
	}

	public ArrayList<String> IdBiciNoleggio(String idUtente) {
		return totemDao.getIdBici(idUtente);
	}

	public String getModelloBici(int id) {
		return totemDao.getModelloBici(id);
	}

	public String controlloNoleggio(String IDutente) {
		return totemDao.controllaNoleggio(IDutente);
	}

	public boolean riconsegna(String IDutente, String modello, int posto) {
		boolean riconsegna = false;

		int IDbici = totemDao.getBiciNoleggiataId(IDutente);
		Bici b = null;
		if (modello.trim().equals("elettrica")) {
			b = new BiciElettrica(String.valueOf(IDbici), totemDao.getDannoBici(IDbici));
		} else if (modello.trim().equals("normale")) {
			b = new BiciNormale( String.valueOf(IDbici), totemDao.getDannoBici(IDbici));
		} else {
			b = new BiciElettricaSeggiolino( String.valueOf(IDbici),
					totemDao.getDannoBici(IDbici));
		}
		if (rastrelliera.agganciaBici(b, posto)) { // controllo che il posto sia libero
			// se avviene il pagamento allora setto la fine del noleggio, se non ha soldi
			// sufficienti il noleggio non termina però la bici NON viene agganciata
			if (pagamento.pagamento(totemDao.getNumeroCarta(IDutente),
					calcolaImportoNoleggio(totemDao.diffInizioFineNoleggio(IDutente), modello.trim(), totemDao.trovaStudenteVeridicità(IDutente)))) {// inserisco
																											// nel db,
																											// ritorna i
																											// minuti
																											// sul quale
																											// devo
																											// invocare
																											// la
																											// funzione
																											// per//
																											// ottenere
																											// l'importo
				System.out.println("ID BICI: "+ IDbici);																				// da pagare
				
				totemDao.fineNoleggio(IDutente);
				totemDao.aggiornaTotem(IDbici, ID); // aggiorna il totem in cui viene riconsegnata nel db.
				riconsegna = true;
			}else {
				rastrelliera.sblocca(posto); // se non ho soldi la bici non viene agganciata e il noleggio continua
				System.out.println("non hai abbastanza soldi per pagare, il tuo noleggio non è terminato");
			}
		}
		rastrelliera.ricaricaBici(); // le bici vengono ricaricate
		return riconsegna;
	}

	public int getBiciNoleggiataId(String idUtente) {
		return totemDao.getBiciNoleggiataId(idUtente);
	}

	public void segnalaDanno(int idBici) {
		totemDao.inserisciDanno(idBici);
	}

	public void setInizioAbbonamento(String IDUtente) {
		totemDao.setInizioAbbonamento(IDUtente);
	}

	
	private double calcolaImportoNoleggio(long minutiTrascorsi, String modello, boolean studente) {
		double importo;
		if (modello.trim().equals("normale")) {
			if(studente == false) {
				if (minutiTrascorsi <= 30) {
					importo = 0;
				} else if (minutiTrascorsi <= 60) {
					importo = 0.50;
				} else if (minutiTrascorsi <= 90) {
					importo = 1;
				} else if (minutiTrascorsi <= 120) {
					importo = 1.50;
				} else {
					importo = 1.50 + (minutiTrascorsi / 60) * 2;
				}
			}else {
				if (minutiTrascorsi <= 120) {
					importo = 0;
				} else {
					importo = 1.50 + (minutiTrascorsi / 60) * 2;
				}
			}
			
		} else { //mi va bene qualsiasi modello tanto rimangono solo bici elettriche
			if (minutiTrascorsi <= 30) {
				importo = 0.25;
			} else if (minutiTrascorsi <= 60) {
				importo = 0.75;
			} else if (minutiTrascorsi <= 90) {
				importo = 1.75;
			} else if (minutiTrascorsi <= 120) {
				importo = 3.75;
			} else {
				importo = 3.75 + (minutiTrascorsi / 60) * 4;
			}
		}
		return importo;
	}

	public Date getValiditàCarta(String IDutente) {
		return totemDao.getValiditàCarta(IDutente);
	}

	// controlla la consegna e nel caso aumenta la consegna tardiva e annulla
	// l'abbonamento se necessario
	public void controlloConsegnaTardiva(String IDutente) {
		totemDao.controlloConsegnaTardiva(IDutente);
	}

	public boolean controlloRoot(String codice, String password) {
		if (totemDao.controlloLoginRoot(codice, password)) {
			idUtente = codice;
			return true;
		}
		return false;
	}

	public void rootRitiroBici(String codiceImpiegato, int idBici) {
		totemDao.rootRitiroBici(codiceImpiegato, idBici);
	}

	public boolean riconsegnaRoot(String codiceImpiegato, int idBici, String modello, int posto) {
		Bici b;
		if (modello.trim().equals("elettrica")) {
			b = new BiciElettrica( String.valueOf(idBici), totemDao.getDannoBici(idBici));
		} else if (modello.trim().equals("normale")) {
			b = new BiciNormale( String.valueOf(idBici), totemDao.getDannoBici(idBici));
		} else {
			b = new BiciElettricaSeggiolino(String.valueOf(idBici),
					totemDao.getDannoBici(idBici));
		}
		if (rastrelliera.agganciaBici(b, posto)) {
			totemDao.riconsegnaRoot(codiceImpiegato, idBici);
			totemDao.aggiornaTotem(idBici, ID);
			return true;
		}
		return false;
	}
	public void setTotemNull(int idBici) {
		totemDao.setTotemNull(idBici);
	}

	public int getID() {
		return ID;
	}
	
	
}
