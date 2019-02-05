package com.zhb.vezbanje.db.Vezbe;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.zhb.vezbanje.db.DateConverter;

import java.util.Date;

@Entity
public class VezbeModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String nazivVezbe;
    private String brojSerija;
    private String brojPonavljanja;
    private String brojKilograma;
    @TypeConverters(DateConverter.class)
    private Date datumVezbe;

    public VezbeModel(){}

    public VezbeModel(String nazivVezbe, String brojSerija, String brojPonavljanja, String brojKilograma, Date borrowDate) {
        this.nazivVezbe = nazivVezbe;
        this.brojSerija = brojSerija;
        this.brojPonavljanja = brojPonavljanja;
        this.brojKilograma = brojKilograma;

        this.datumVezbe = borrowDate;
    }

    public int getId() {
        return id;
    }

    public String getBrojPonavljanja() {
        return brojPonavljanja;
    }

    public String getBrojKilograma() {
        return brojKilograma;
    }

    public String getNazivVezbe() {
        return nazivVezbe;
    }

    public String getBrojSerija() {
        return brojSerija;
    }

    public Date getDatumVezbe() {
        return datumVezbe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNazivVezbe(String nazivVezbe) {
        this.nazivVezbe = nazivVezbe;
    }

    public void setBrojSerija(String brojSerija) {
        this.brojSerija = brojSerija;
    }

    public void setBrojPonavljanja(String brojPonavljanja) {
        this.brojPonavljanja = brojPonavljanja;
    }

    public void setBrojKilograma(String brojKilograma) {
        this.brojKilograma = brojKilograma;
    }

    public void setDatumVezbe(Date datumVezbe) {
        this.datumVezbe = datumVezbe;
    }
}
