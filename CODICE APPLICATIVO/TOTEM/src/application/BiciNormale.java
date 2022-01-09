package application;

public class BiciNormale implements Bici {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceUnivoco == null) ? 0 : codiceUnivoco.hashCode());
		result = prime * result + (danno ? 1231 : 1237);
		result = prime * result + ((modello == null) ? 0 : modello.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BiciNormale other = (BiciNormale) obj;
		if (codiceUnivoco == null) {
			if (other.codiceUnivoco != null)
				return false;
		} else if (!codiceUnivoco.equals(other.codiceUnivoco))
			return false;
		if (danno != other.danno)
			return false;
		if (modello == null) {
			if (other.modello != null)
				return false;
		} else if (!modello.equals(other.modello))
			return false;
		return true;
	}
	private String modello;
	private String codiceUnivoco;
	private boolean danno;
	
	public BiciNormale(String codiceUnivoco, boolean danno) {
		this.modello = "normale";
		this.codiceUnivoco = codiceUnivoco;
		this.danno = danno;
	}
	
	@Override
	public String getModello() {
		return this.modello;
	}

	@Override
	public String getCodiceUnivoco() {
		return this.codiceUnivoco;
	}



	@Override
	public boolean getDanno() {
		return danno;
	}
	@Override
	public boolean IsSeggiolino() {
		return false;
	}
}
