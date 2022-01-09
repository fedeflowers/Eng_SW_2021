package application;

import java.sql.SQLException;

public class Pagamento {
	private static Pagamento single_instance = null;
	private PagamentoDAO pagamentoDao;
	

	private Pagamento() {
		pagamentoDao = new PagamentoDAO();
	}
	
	public static Pagamento getInstance() {
		if (single_instance == null) {
			single_instance = new Pagamento();
		}

		return single_instance;
	}
	
	public void inizializzaCarta (String numeroCarta, double saldo) throws SQLException {
		pagamentoDao.inizializzaCarta(numeroCarta, saldo);
	}
	
	public boolean pagamento(String numeroCarta, double importo) {
		return pagamentoDao.pagamento(numeroCarta, importo);
	}
}
