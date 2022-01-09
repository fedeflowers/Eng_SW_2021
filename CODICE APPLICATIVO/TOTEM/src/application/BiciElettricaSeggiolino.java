package application;

public class BiciElettricaSeggiolino extends BiciElettrica {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (seggiolino ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BiciElettricaSeggiolino other = (BiciElettricaSeggiolino) obj;
		if (seggiolino != other.seggiolino)
			return false;
		return true;
	}

	private boolean seggiolino;
	
	public BiciElettricaSeggiolino( String codiceUnivoco, boolean danno) {
		super( codiceUnivoco,danno);
		seggiolino = true;
		super.modello += " con seggiolino";
	}

	@Override
	public boolean IsSeggiolino() {
		return seggiolino;
	}
	
}
