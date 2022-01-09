package application;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public interface StatisticaInterface {

	
	
	public int numeroBiciMedioUtilizzatoAlGiorno() throws ParseException ;
	public int getTotempiùUsato();
	public int determinaFasciaOrariaPiùRichiesta();

	
}
