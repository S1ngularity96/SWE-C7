package daten;

/**
 * Speichert alle daten die f?r alle taps relevfant sein k?nnen.
 *
 */
public class Benutzer {

	private Studiengang studiengang;
	private float wunschnote;
	private float versuche;

	public Benutzer(Studiengang studiengang, float wunschnote, float versuche) {
		super();
		this.studiengang = studiengang;
		this.wunschnote = wunschnote;
		this.versuche = versuche;
	}

	/**
	 * Berechnet die durchschnitts Note der Module. Module mit mehr Credits
	 * werden st?rker gewichtet. Module ohne Note oder Softskill Module werden
	 * nicht beachtet.
	 * 
	 * @return summe(note*credits)/summe(credits)
	 */
	public float durchschnittsNote() {
		float summe = 0;
		float credits = 0;
		for (Modul m : studiengang.getModule()) {
			if (m.isGeschrieben() && m.getTyp() != Typ.SOFTSKILL) {
				summe += m.getCredits() * m.getNote();
				credits += m.getCredits();
			}
		}
		if (credits == 0) {
			return 0;
		}
		return summe / credits;
	}

	/**
	 * Berechnet die durchschnitts Note aus den Plan-Moten der Module. Module
	 * mit mehr Credits werden st?rker gewichtet. Module ohne Plan-Note oder
	 * Softskill Module werden nicht beachtet.
	 * 
	 * @return summe(planNote*credits)/summe(credits)
	 */
	public float durchschnittsPlanNote() {
		float summe = 0;
		float credits = 0;
		for (Modul m : studiengang.getModule()) {
			if (m.isGeschrieben() && m.getTyp() != Typ.SOFTSKILL) {
				summe += m.getCredits() * m.getPlanNote();
				credits += m.getCredits();
			}
		}
		if (credits == 0) {
			return 0;
		}
		return summe / credits;
	}

	public float PlanSchnitt(){
		float note = 0;
		float credits = 0;
		for (int c = 0; c < studiengang.getModule().size(); c++){
			if (studiengang.getModul(c).getPlanNote() != 0){
				note += studiengang.getModul(c).getPlanNote() * studiengang.getModul(c).getCredits();
				credits += studiengang.getModul(c).getCredits();
			}
		}
		if (credits == 0){
			return 0;
		}
		return note/credits;
	}

	public Studiengang getStudiengang() {
		return studiengang;
	}

	public void setWunschNote(float wunschnote) {
		this.wunschnote = wunschnote;
	}

	public float getWunschnote() {
		return wunschnote;
	}

	public float getVersuche() { return versuche; }

	public void setVersuche(int versuche) { this.versuche = versuche; }

	@Override
	public String toString() {
		String s = "<<Benutzer>>\n";

		s += "Studiengang: " + studiengang.getName();
		s += "\nwunschnote: " + wunschnote;
		s += "\nbenoetigte Credits: " + studiengang.getBenoetigteCredits();
		s += "\nanz. Semester: " + studiengang.getAnzSemester();
		s += "\nanz. Wahlmodule: " + studiengang.getAnzWahl();
		s += "\nanz. Softskill: " + studiengang.getAnzSoftskill();
		s += "\nmax. verbleibende Versuche: " + studiengang.getMaxVerbleibendeVersuche() + "\n\n";

		s += "durchschnitts Note: " + durchschnittsNote() + "\n";
		//s += "durchschnitts Plan-Note: " + durchschnittsPlanNote() + "\n\n";
		s += "durchschnitts Plan-Note: " + PlanSchnitt() + "\n\n";

		s += "Modul Nr. |Name                                                        |Credits|Note|Plan  Note|Versuche|Pruefungsdatum|Ablaufdatum|Semester|Typ"
				+ "\n";
		s += "----------+------------------------------------------------------------+-------+----+----------+--------+--------------+--------+-----------"
				+ "\n";
		for (int i = 0; i < studiengang.getModuleSize(); i++) {
			Modul m = studiengang.getModul(i);
			s += String.format("%-10s", m.getModulnummer()) + "|";
			s += String.format("%-60s", m.getName().substring(0, Math.min(m.getName().length(), 60))) + "|";
			s += String.format("%-7s", m.getCredits()) + "|";
			s += String.format("%-4s", m.getNote()) + "|";
			s += String.format("%-10s", m.getPlanNote()) + "|";
			s += String.format("%-8s", m.getVersuche()) + "|";
			s += String.format("%-14s", m.getPruefungsDatum()) + "|";
			s += String.format("%-11s", m.getAblaufdatum()) + "|";
			s += String.format("%-8s", m.getSemester()) + "|";
			s += String.format("%-10s", m.getTyp());
			s += "\n";
		}
		return s;
	}
}
