package be.vdab.entities;

import java.io.Serializable;

public class Reservatie implements Serializable {	
	private static final long serialVersionUID = 1L;
	private int id, klantid, voorstellingsid, plaatsen;
	
	public Reservatie(int klantID,int voorstellingID, int plaatsen){
		this.klantid = klantID;
		this.voorstellingsid = voorstellingID;
		this.plaatsen = plaatsen;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKlantid() {
		return klantid;
	}

	public void setKlantid(int klantid) {
		this.klantid = klantid;
	}

	public int getVoorstellingsid() {
		return voorstellingsid;
	}

	public int getPlaatsen() {
		return plaatsen;
	}

}
