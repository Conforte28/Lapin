package bj.lapins.suivi.entity;

public enum TypeSortie {
    VENTE("Vente"),
    DECES("Décès");

    private final String libelle;

    TypeSortie(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
