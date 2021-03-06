package application;

import java.sql.Date;

abstract public class Abbonamento {

	public static final AbbonamentoDAO abbonamentoDao = new AbbonamentoDAO(); // Ŕ immutabile

	public static void inviaFormStudente(String email, String universitÓ, String matricola, String corso,
			String codiceUnivocoAbb) {
		abbonamentoDao.inserisciFormsStudente(email, universitÓ, matricola, corso, codiceUnivocoAbb);
	};

	public static boolean unicitÓNomeUtente(String nome) {
		return abbonamentoDao.controllaUnicitÓNomeUtente(nome);
	}

	abstract public String creaAbbonamentoFirstTime(String nome, String password, String numeroCarta, Date dataValiditÓ,
			boolean studente, String tipo);

	public static String getUltimoCodiceAbbonamentoInserito() {
		return AbbonamentoDAO.getUltimoCodiceAbbonamentoInserito();
	}
	
	public static boolean login(String nomeUtente, String password) {
		return abbonamentoDao.login(nomeUtente, password);
	}
	
	abstract public boolean rinnovaAbb(String nomeUtente);
}
