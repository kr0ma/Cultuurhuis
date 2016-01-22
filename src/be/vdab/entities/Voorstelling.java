package be.vdab.entities;

import java.math.BigDecimal;
import java.util.Date;


public class Voorstelling {
	private int id, genreID, vrijeplaatsen;
	private String titel, uitvoerders;
	private Date datum;
	private BigDecimal prijs;
	
	public Voorstelling(int id, int genreID, int vrijeplaatsen, String titel,
			String uitvoerders, Date datum, BigDecimal prijs) {
		this.id = id;
		this.genreID = genreID;
		this.vrijeplaatsen = vrijeplaatsen;
		this.titel = titel;
		this.uitvoerders = uitvoerders;
		this.datum = datum;
		this.prijs = prijs;
	}	
	
	// getters
	public int getId() {
		return id;
	}

	public int getGenreID() {
		return genreID;
	}

	public int getVrijeplaatsen() {
		return vrijeplaatsen;
	}

	public String getTitel() {
		return titel;
	}

	public String getUitvoerders() {
		return uitvoerders;
	}

	public Date getDatum() {
		return datum;
	}

	public BigDecimal getPrijs() {
		return prijs;
	}
	
	// setters	
	public void setVrijeplaatsen(int vrijeplaatsen) {
		this.vrijeplaatsen = vrijeplaatsen;
	}
	
	public void verminderVrijePlaatsen(int aantal){
		this.vrijeplaatsen -= aantal;
	}
	
	public boolean isAantalOK(int aantal){
		return aantal >= 1 && aantal <= vrijeplaatsen;
	}


}
