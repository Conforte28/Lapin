package bj.lapins.suivi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "mises_bas")
public class MiseBas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mere_id", nullable = false)
    private Lapin mere;

    @ManyToOne
    @JoinColumn(name = "pere_id")
    private Lapin pere;

    @Column(nullable = false)
    private LocalDate dateMiseBas;

    @Column(nullable = false)
    private int nombreNesVivants;

    @Column(nullable = false)
    private int nombreMortNes;

    @Column(length = 500)
    private String observation;

    public MiseBas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lapin getMere() {
        return mere;
    }

    public void setMere(Lapin mere) {
        this.mere = mere;
    }

    public Lapin getPere() {
        return pere;
    }

    public void setPere(Lapin pere) {
        this.pere = pere;
    }

    public LocalDate getDateMiseBas() {
        return dateMiseBas;
    }

    public void setDateMiseBas(LocalDate dateMiseBas) {
        this.dateMiseBas = dateMiseBas;
    }

    public int getNombreNesVivants() {
        return nombreNesVivants;
    }

    public void setNombreNesVivants(int nombreNesVivants) {
        this.nombreNesVivants = nombreNesVivants;
    }

    public int getNombreMortNes() {
        return nombreMortNes;
    }

    public void setNombreMortNes(int nombreMortNes) {
        this.nombreMortNes = nombreMortNes;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
