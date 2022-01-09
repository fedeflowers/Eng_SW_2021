package application;

import java.sql.SQLException;

public interface PagamentoInterface{

	public void inizializzaCarta (String numeroCarta, double saldo) throws SQLException;
	public boolean pagamento(String numeroCarta, double importo);
}
