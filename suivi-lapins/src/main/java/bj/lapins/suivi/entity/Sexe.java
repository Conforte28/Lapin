package bj.lapins.suivi.entity;

public enum Sexe {
    MALE("Mâle"),
    FEMELLE("Femelle"),
    INCONNU("Inconnu");

    private final String libelle;

    Sexe(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
