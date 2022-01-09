package application;

import java.sql.Date;

abstract public class Abbonamento {

	public static final AbbonamentoDAO abbonamentoDao = new AbbonamentoDAO(); // Ë immutabile

	public static void inviaFormStudente(String email, String universit‡, String matricola, String corso,
			String codiceUnivocoAbb) {
		abbonamentoDao.inserisciFormsStudente(email, universit‡, matricola, corso, codiceUnivocoAbb);
	};

	public static boolean unicit‡NomeUtente(String nome) {
		return abbonamentoDao.controllaUnicit‡NomeUtente(nome);
	}

	abstract public String creaAbbonamentoFirstTime(String nome, String password, String numeroCarta, Date dataValidit‡,
			boolean studente, String tipo);

	public static String getUltimoCodiceAbbonamentoInserito() {
		return AbbonamentoDAO.getUltimoCodiceAbbonamentoInserito();
	}
	
	public static boolean login(String nomeUtente, String password) {
		return abbonamentoDao.login(nomeUtente, password);
	}
	
	abstract public boolean rinnovaAbb(String nomeUtente);
}
