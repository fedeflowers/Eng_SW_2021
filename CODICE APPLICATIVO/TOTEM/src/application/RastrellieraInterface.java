package application;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public interface RastrellieraInterface {

	public int disponibilit‡Biciclette();
	
	public ArrayList<Integer> getPostiLiberi();

	public ArrayList<Bici> disponibilit‡Bici();
	
	public Bici[] disponibilit‡BiciArray();

	// restituisce il primo posto con tale modello di bici in ordine crescente in
	// base alla disponibilit‡
	// ritorna -1 se il modello di bici non Ë presente
	public int getPosto(String modelloBici);

	// observer
	public void ricaricaBici();

	public Bici getBici(int posto);
	// true se l'aggancio Ë avvenuto correttamente se no false
	public boolean  agganciaBici(Bici b, int posto);

	public void sblocca(int posto);

}
