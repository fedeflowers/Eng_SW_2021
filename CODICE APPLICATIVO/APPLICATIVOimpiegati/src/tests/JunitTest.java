package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import application.GestioneBiciRastrelliereDAO;
import application.ImpiegatoSW;
import application.Statistica;

public class JunitTest {
	@Test
	public void controlloLoginTest() {
		ImpiegatoSW sw = ImpiegatoSW.getInstance();
		sw = ImpiegatoSW.getInstance(); // secondo branch
		assertEquals(false, sw.loginRoot("1", "dasjiasdi"));
		assertEquals(true, sw.loginRoot("1", "prova"));
		assertEquals("1", sw.getIdUtenteLoggato());
	}

	@Test
	public void controlloBiciTest() {
		GestioneBiciRastrelliereDAO g = new GestioneBiciRastrelliereDAO();
		ImpiegatoSW sw = ImpiegatoSW.getInstance();
		sw.aggiungiBici("normale", false, false);
		sw.eliminaBici(sw.getMaxIdBici());
		sw.aggiungiBici("elettrica", false, false);
		sw.eliminaBici(sw.getMaxIdBici());
		sw.aggiungiBici("elettrica", true, true);
		assertEquals(g.getIdBicidaEliminare(), sw.getIdBicidaEliminare());
		sw.eliminaBici(sw.getMaxIdBici());

		assertEquals(g.getIdBicidaEliminare(), sw.getIdBicidaEliminare());// finisco i brackets

		// sw.insRitiroBici("1", 1); andrebbe riconsegnata ma la riconsegna viene fatta
		// in TOTEM
		// altrimenti i dati del db non rimangono corretti

	}

	@Test
	public void controlloTotemTest() {
		GestioneBiciRastrelliereDAO g = new GestioneBiciRastrelliereDAO();
		ImpiegatoSW sw = ImpiegatoSW.getInstance();
		assertTrue(sw.trovaVieNonUnivocheTotem("via Verdi"));
		assertFalse(sw.trovaVieNonUnivocheTotem("via Verdasddasi"));
		sw.insertTotem("test");
		assertEquals(g.ListaTotemDisponibilità(), sw.ListaTotemDisponibilità());
		assertEquals(g.getListaTotemVuoti(), sw.getListaTotemVuoti());
		sw.deleteTotem(sw.getListaTotemVuoti().get(0));
	}

	@Test
	public void controlloStatisticaTest() {
		ImpiegatoSW sw = ImpiegatoSW.getInstance();
		Statistica s = Statistica.getInstance();
		s = Statistica.getInstance(); // secondo bracket
		try {
			assertEquals(s.numeroBiciMedioUtilizzatoAlGiorno(),sw.numeroBiciMedioUtilizzatoAlGiorno());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertEquals(s.getTotempiùUsato(), sw.getTotempiùUsato());
		assertEquals(s.determinaFasciaOrariaPiùRichiesta(), sw.determinaFasciaOrariaPiùRichiesta());
	}
	
	//il resto sono controllers non ha molto senso testarli
}
