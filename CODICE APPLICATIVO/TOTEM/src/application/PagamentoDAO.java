package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PagamentoDAO {
	private Connection connection = null;

	public PagamentoDAO() {
		try {
			Class.forName("org.postgresql.Driver");
			//ho inserito lo spazio ricorarsi di toglierlo
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BancaSimulazione", "postgres",
					"postgres");

			if (connection != null) {
				System.out.println("connection ok");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void inizializzaCarta (String numeroCarta, double saldo) throws SQLException {
		String query = "INSERT INTO public.\"CartaDiCredito\"(\r\n"
				+ "	\"NumeroCarta\", \"Saldo\")\r\n"
				+ "	VALUES (?, ?);";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, numeroCarta);
			ps.setDouble(2, saldo);
			ps.execute();
		} catch (SQLException e) {
			throw new SQLException();
		}
	}
	
	// ritorna true se il pagamento è avvenuto, false altrimenti
	public boolean pagamento(String numeroCarta, double importo) {
		double saldo = -1;
		String queryPagamento = "UPDATE public.\"CartaDiCredito\" \r\n"
				+ "	SET  \"Saldo\"=\"Saldo\"- ?\r\n"
				+ "	WHERE \"NumeroCarta\" = ?;";
		
		String querySaldo ="SELECT \"Saldo\"\r\n"
				+ "	FROM public.\"CartaDiCredito\"\r\n"
				+ "	where \"NumeroCarta\" = ?";
		
		PreparedStatement ps;
		PreparedStatement ps1;
		try {
			ps = connection.prepareStatement(querySaldo);
			ps.setString(1, numeroCarta);
			ResultSet myRs = ps.executeQuery();
			if(myRs.next()) {
				saldo = myRs.getInt(1);
			}else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Numero carta non presente.");
				alert.showAndWait();
				return false;
			}
			if(importo >0) {
				if(saldo-importo >=0) {
					ps1 = connection.prepareStatement(queryPagamento);
					ps1.setDouble(1, importo);
					ps1.setString(2, numeroCarta);
					ps1.execute();
					return true;
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Saldo disponibile non sufficiente");
					alert.showAndWait();
					return false;
				}
			}else {
				/*ps1 = connection.prepareStatement(queryPagamento);
				ps1.setDouble(1, importo);
				ps1.setString(2, numeroCarta);
				ps1.execute();*/
				//System.out.println("importo <= 0");
				return true;
			}
			

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	
	//lo uso per la multa
		public void pagamentoSenzaAvviso(String numeroCarta, double importo) {
			String queryPagamento = "UPDATE public.\"CartaDiCredito\" \r\n" + "	SET  \"Saldo\"=\"Saldo\"- ?\r\n"
					+ "	WHERE \"NumeroCarta\" = ?;";

			PreparedStatement ps;
			try {
				ps = connection.prepareStatement(queryPagamento);
				ps.setDouble(1, importo);
				ps.setString(2, numeroCarta);
				ps.execute();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
}
