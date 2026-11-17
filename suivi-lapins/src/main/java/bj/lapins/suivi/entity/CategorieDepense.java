package bj.lapins.suivi.entity;

public enum CategorieDepense {
    ALIMENTATION("Alimentation"),
    SOINS("Soins"),
    ENTRETIEN("Entretien"),
    AUTRE("Autre");

    private final String libelle;

    CategorieDepense(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
