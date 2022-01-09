package application;

import java.util.ArrayList;

public class Rastrelliera implements RastrellieraInterface{
	private static Rastrelliera single_instance = null;
	// avrò un array di bici in cui i primi indici indicheranno i posti per bici
	// normali e quell in fondo per bici elettriche;
	// rappresenteranno quindi il gancio fisico, una bici normale non potrà entrare
	// in quella elettrica e viceversa

	// i primi 5 ganci per le bici normali e gli ultimi 3 per le bici elettriche
	private Bici[] r;

// basta modificare questo per modificare la lunghezza della rastrelliera complessiva

	//@ requires r.length == 8;
	private Rastrelliera(Bici[] r) {
		if (r.length == 8) {
			this.r = new Bici[8];
			this.r = r;
		}
			
		else
			throw new IllegalArgumentException("la rastrelliera dev'essere composta da massimo/minimo  8 Bici");

		for (int i = 0; i < r.length; i++) {
			if (this.r[i] != null) {
				if (i < 5 && r[i].getModello().equals("elettrica")) {
					throw new IllegalArgumentException("non possono esserci bici elettriche prima del posto 5");
				}
				if (i >= 5 && !this.r[i].getModello().equals("elettrica") && !this.r[i].getModello().equals("elettrica con seggiolino") ) {
					throw new IllegalArgumentException("non possono esserci bici normali dopo il posto 5");
				}
			}

		}

	}

	public static Rastrelliera getInstance(Bici[] r) {
		if (single_instance == null)
			single_instance = new Rastrelliera(r);

		return single_instance;
	}


	//@ ensures \result >=0 && \result <= 8;
	public int disponibilitàBiciclette() {
		int count = 0;
		for (int i = 0; i < r.length; i++) {
			if (r[i] != null) {
				count++;
			}
		}
		return count;
	}
	
	public ArrayList<Integer> getPostiLiberi() {
		ArrayList<Integer> posti = new ArrayList<>();
		for (int i = 0; i < r.length; i++) {
			if (r[i] == null) {
				posti.add(i);
			}
		}
		return posti;
	}

	public ArrayList<Bici> disponibilitàBici() {
		ArrayList<Bici> modelliBici = new ArrayList<>();
		for (int i = 0; i < r.length; i++) {
			if (r[i] != null) {
				modelliBici.add(r[i]);
			}
		}
		return modelliBici;

	}

	//@ ensures \result.length == 8;
	public Bici[] disponibilitàBiciArray() {
		return r;

	}

	// restituisce il primo posto con tale modello di bici in ordine crescente in
	// base alla disponibilità
	// ritorna -1 se il modello di bici non è presente
	
	//@ensures \result== -1  ==> !(\exists int j; j >= 0 && j<r.length; r[j].getModello() == modelloBici) ;
	public int getPosto(String modelloBici) {
		for (int i = 0; i < r.length; i++) {
			if (r[i] != null) {
				if (r[i].getModello().trim().toLowerCase().equals(modelloBici.trim().toLowerCase())) {
					return i;
				}
			}

		}
		return -1;
	}

	// observer
	public void ricaricaBici() {
		for (int i = 5; i < r.length; i++) {
			if (r[i] != null) {
				BiciElettrica b = (BiciElettrica) r[i];
				b.ricarica();
			}
		}
	}
	//@ensures posto >=0 && posto< 8
	public Bici getBici(int posto) {
		if(posto >=r.length || posto < 0)
			throw new IllegalArgumentException();
		return r[posto];
	}
	
	// true se l'aggancio è avvenuto correttamente se no false
	public boolean  agganciaBici(Bici b, int posto) {
		if(b.getModello().equals("elettrica")) {
			if(posto<5) {//non è il gancio giusto
				System.out.println("ERRORE, non puoi attaccare una bici elettrica in quell'aggancio! ");
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("non puoi attaccare una bici elettrica in quell'aggancio!" );
				alert.showAndWait();*/
			}else {
				r[posto] = b;
				return true;
			}
		}else {
			if(posto<5) {//non è il gancio giusto
				r[posto] = b;
				return true;
			}else {
				System.out.println("ERRORE, non puoi attaccare una bici normale in quell'aggancio! ");
				/*Alert alert = new Alert(AlertType.INFORMATION);

				alert.setContentText("non puoi attaccare una bici normale in quell'aggancio!" );
				alert.showAndWait();*/
			}
		}
		return false;
	}
	//@ensures posto >=0 && posto< 8
	public void sblocca(int posto) {
		if(posto >= r.length  || posto < 0)
			throw new IllegalArgumentException();
		r[posto] = null; // la bici viene sbloccata
	}

}
