package be.vdab.entities;

import java.io.Serializable;

public class Klant implements Serializable {	
	private static final long serialVersionUID = 1L;
	private int id;
	private final String voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord;	
	
	public Klant(int id, String voornaam, String familienaam, String straat,
			String huisnr, String postcode, String gemeente,
			String gebruikersnaam, String paswoord) {
		this(voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord);
		this.id = id;		
	}
	
	public Klant(String voornaam, String familienaam, String straat,
			String huisnr, String postcode, String gemeente,
			String gebruikersnaam, String paswoord) {		
		this.voornaam = voornaam;
		this.familienaam = familienaam;
		this.straat = straat;
		this.huisnr = huisnr;
		this.postcode = postcode;
		this.gemeente = gemeente;
		this.gebruikersnaam = gebruikersnaam;
		this.paswoord = paswoord;
	}
	
	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getVoornaam() {
		return voornaam;
	}

	public String getFamilienaam() {
		return familienaam;
	}

	public String getStraat() {
		return straat;
	}

	public String getHuisnr() {
		return huisnr;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getGemeente() {
		return gemeente;
	}

	public String getPaswoord() {
		return paswoord;
	}
	
	
	@Override
	public String toString() {		
		return voornaam + " " + familienaam + " " + straat + " " + huisnr + " " + postcode + " " + gemeente;
	}
	
	public boolean isPaswoordJuist(String ingave){		
		return paswoord.equals(ingave);
	}	
	
	public static boolean isInputValid(String input) { // Deze static function
		// valideert de naam zonder een Pizza instance te moeten maken (zie
		// verder)
		return input != null && !input.isEmpty();
	}
	
	
	
}
