package application;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public interface RastrellieraInterface {

	public int disponibilitÓBiciclette();
	
	public ArrayList<Integer> getPostiLiberi();

	public ArrayList<Bici> disponibilitÓBici();
	
	public Bici[] disponibilitÓBiciArray();

	// restituisce il primo posto con tale modello di bici in ordine crescente in
	// base alla disponibilitÓ
	// ritorna -1 se il modello di bici non Ŕ presente
	public int getPosto(String modelloBici);

	// observer
	public void ricaricaBici();

	public Bici getBici(int posto);
	// true se l'aggancio Ŕ avvenuto correttamente se no false
	public boolean  agganciaBici(Bici b, int posto);

	public void sblocca(int posto);

}
