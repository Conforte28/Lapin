package bj.lapins.suivi.entity;

public enum Role {
    ADMIN("Administrateur"),
    AGENT("Agent");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
