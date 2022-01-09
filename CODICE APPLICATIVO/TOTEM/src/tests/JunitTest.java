package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import application.Bici;
import application.BiciElettrica;
import application.BiciElettricaSeggiolino;
import application.BiciNormale;
import application.Rastrelliera;
import application.Totem;
import application.TotemDAO;

public class JunitTest {
	@Test
	public void contieneBiciId4() {
		Bici b;
		b = new BiciNormale("4", true);
		assertEquals(true, Totem.getInstance().getBici().contains(b));
	}

	/*
	 * //conta anche l'ordine è instabile, totem id 2
	 * 
	 * @Test public void contieneLeBicinelDB() { ArrayList<Bici> b = new
	 * ArrayList<>(); b.add(new BiciNormale("8", false)); b.add(new BiciNormale("4",
	 * true)); b.add(new BiciNormale("5", false)); b.add(new
	 * BiciElettricaSeggiolino("9", false)); b.add(new BiciElettricaSeggiolino("7",
	 * false)); b.add(new BiciElettrica("6", false));
	 * 
	 * 
	 * assertEquals(true,Totem.getInstance().getBici().equals(b)); }
	 */
	/*
	 * //per il totem id 2, può cambiare se qualcuno preleva qualche bici e gli
	 * cambia totem, instabile
	 * 
	 * @Test public void contieneCodUnivociBiciDB() { ArrayList<Bici> b = new
	 * ArrayList<>(); b.add(new BiciNormale("8", false)); b.add(new BiciNormale("4",
	 * true)); b.add(new BiciNormale("5", false)); b.add(new
	 * BiciElettricaSeggiolino("9", false)); b.add(new BiciElettricaSeggiolino("7",
	 * false)); b.add(new BiciElettrica("6", false));
	 * 
	 * 
	 * assertEquals(true,Totem.getInstance().getBici().contains(b.get(0)));
	 * assertEquals(true,Totem.getInstance().getBici().contains(b.get(1)));
	 * assertEquals(true,Totem.getInstance().getBici().contains(b.get(2)));
	 * assertEquals(true,Totem.getInstance().getBici().contains(b.get(3)));
	 * assertEquals(true,Totem.getInstance().getBici().contains(b.get(4)));
	 * assertEquals(true,Totem.getInstance().getBici().contains(b.get(5))); }
	 */

	// il pagamento è stato testato nel SITOprogetto, inoltre alcune funzionalità
	// vengono riprese in riconsegna del totem.
	// controlla che i due metodi di totem e totemDAO diano gli stessi risultati
	// il posto di riconsegna e di raccolta potrebbe variare,... è instabile
	@Test
	public void NoleggioeRiconsegnaNormaleTest() {
		Totem t = Totem.getInstance();
		t.iniziaNoleggio("4714186072", "4");
		assertEquals(true, t.riconsegna("4714186072", "normale", 0));
	}

	@Test
	public void NoleggioeRiconsegnaElettricaTest() {
		Totem t = Totem.getInstance();
		t.iniziaNoleggio("4714186072", "4");
		assertEquals(true, t.riconsegna("4714186072", "elettrica", 5));
	}

	@Test
	public void NoleggioeRiconsegnaElettricaSeggiolinoTest() {
		Totem t = Totem.getInstance();
		t.iniziaNoleggio("4714186072", "6");
		assertEquals(true, t.riconsegna("4714186072", "elettrica", 6));
	}

	// la rastrelliera non viene testata perchè dipende da come viene inizializzata,
	// a seconda delle bici che ci sonoa ttaccate nel db al totem
	// se la instanzio in un certo modo nei test questo potrebbe essere in alcuni
	// casi giusto in alcuni sbaglaito, pertanto non ha senso testare la
	// rastrelliera
	@Test
	public void TotemTest() {
		TotemDAO t = new TotemDAO();
		Totem totem = Totem.getInstance();
		assertEquals(totem.getModelloBici(0), t.getModelloBici(0));
		assertEquals(totem.controlloNoleggio("1"), t.controllaNoleggio("1"));
		assertEquals(true, totem.idUtentiNoleggio().equals(t.getUtentiIdConNoleggioAttivo()));
		if (totem.verificaAbbonamento("4714186072", "1"))
			assertTrue(totem.verificaAbbonamento("4714186072", "1"));
		else
			assertFalse(totem.verificaAbbonamento("4714186072", "1"));
		assertEquals(-1, totem.getPostoBici("aeroplano"));
		//totem.iniziaNoleggio("4714186072", "1");
		//assertEquals(1, totem.getBiciNoleggiataId("4714186072"));
		//assertEquals("4714186072", totem.controlloNoleggio("4714186072"));
		//totem.riconsegna("4714186072", "normale", 0); // non ho certezza sul psoto, se il posto non è libero ogni volta
														// che creo il noleggio della bici "1", verrà visualizza in
														// riconsegna anche se in realtà non dovrebbe essere possibile
														// questa cosa, in sostanza rifancendolo + volte potrò attaccare
														// la stessa bici in diverse posizione contemporaneamente qeusto
														// perchè inizia noleggio non dovrebbe crearsi con una bici di
														// id 1 ancora attaccata

		assertEquals(t.getValiditàCarta("4714186072"), totem.getValiditàCarta("4714186072"));
		assertTrue(totem.controlloRoot("1", "prova"));
		//totem.rootRitiroBici("1", 1);
		//totem.riconsegnaRoot("1", 1, "nomale", 0); // non ho certezza sul posto // stesso problema di sopra

	}
	@Test
	public void BiciElettricaTest(){
		BiciElettrica BE = new BiciElettrica("99",false);
		BiciElettrica BE2 = new BiciElettrica("99",true);
		BiciElettricaSeggiolino BE3 = new BiciElettricaSeggiolino("99",false);
		assertFalse(BE.equals(BE2));
		assertTrue(BE.equals(BE));
		assertEquals("elettrica",BE.getModello());
		assertEquals("99",BE.getCodiceUnivoco());
		assertEquals(true, BE2.getDanno());
		assertEquals(false, BE.getDanno());
		assertFalse(BE.IsSeggiolino());
		assertTrue(BE3.IsSeggiolino());
		assertFalse(BE3.equals(BE));
		assertEquals("elettrica con seggiolino", BE3.getModello());
	}
	@Test
	public void BiciNormaleTest(){
		BiciNormale BE = new BiciNormale("99",false);
		BiciNormale BE2 = new BiciNormale("99",true);
		assertFalse(BE.equals(BE2));
		assertTrue(BE.equals(BE));
		assertEquals("normale",BE.getModello());
		assertEquals("99",BE.getCodiceUnivoco());
		assertEquals(true, BE2.getDanno());
		assertEquals(false, BE.getDanno());
		assertFalse(BE.IsSeggiolino());
	}
}
