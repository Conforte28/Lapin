package bj.lapins.suivi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sorties")
public class Sortie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "lapin_id", nullable = false, unique = true)
    private Lapin lapin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeSortie type;

    @Column(nullable = false)
    private LocalDate dateSortie;

    @Column(precision = 14, scale = 2)
    private BigDecimal montant;

    @Column(length = 150)
    private String acheteur;

    @Column(length = 500)
    private String observation;

    public Sortie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lapin getLapin() {
        return lapin;
    }

    public void setLapin(Lapin lapin) {
        this.lapin = lapin;
    }

    public TypeSortie getType() {
        return type;
    }

    public void setType(TypeSortie type) {
        this.type = type;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(String acheteur) {
        this.acheteur = acheteur;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
