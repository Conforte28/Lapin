package bj.lapins.suivi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lapins")
public class Lapin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sexe sexe;

    @Column(length = 100)
    private String race;

    private LocalDate dateNaissance;

    @Column(length = 100)
    private String clapier;

    @Column(precision = 6, scale = 2)
    private BigDecimal poidsKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutLapin statut = StatutLapin.VIVANT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mise_bas_origine_id")
    private MiseBas miseBasOrigine;

    public Lapin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getClapier() {
        return clapier;
    }

    public void setClapier(String clapier) {
        this.clapier = clapier;
    }

    public BigDecimal getPoidsKg() {
        return poidsKg;
    }

    public void setPoidsKg(BigDecimal poidsKg) {
        this.poidsKg = poidsKg;
    }

    public StatutLapin getStatut() {
        return statut;
    }

    public void setStatut(StatutLapin statut) {
        this.statut = statut;
    }

    public MiseBas getMiseBasOrigine() {
        return miseBasOrigine;
    }

    public void setMiseBasOrigine(MiseBas miseBasOrigine) {
        this.miseBasOrigine = miseBasOrigine;
    }
}
