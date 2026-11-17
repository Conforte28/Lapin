package bj.lapins.suivi.entity;

public enum StatutLapin {
    VIVANT("Vivant"),
    VENDU("Vendu"),
    MORT("Mort");

    private final String libelle;

    StatutLapin(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
