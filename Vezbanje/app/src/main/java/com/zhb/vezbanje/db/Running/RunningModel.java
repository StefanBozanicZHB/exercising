package com.zhb.vezbanje.db.Running;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.zhb.vezbanje.db.DateConverter;

import java.util.Date;

@Entity
public class RunningModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String nazivVezbe;
    private String distanca;
    private String trajanje;
    private String kalorije;
    private String prosekNa400;
    private boolean teg;

    @TypeConverters(DateConverter.class)
    private Date datumVezbe;

    public RunningModel(){}

    public RunningModel(String nazivVezbe, String distanca, String trajanje, String kalorije, String prosekNa400,  boolean teg, Date borrowDate) {
        this.nazivVezbe = nazivVezbe;
        this.distanca = distanca;
        this.trajanje = trajanje;
        this.kalorije = kalorije;
        this.prosekNa400 = prosekNa400;
        this.teg = teg;

        this.datumVezbe = borrowDate;
    }

    public boolean isTeg() {
        return teg;
    }

    public void setTeg(boolean teg) {
        this.teg = teg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNazivVezbe(String nazivVezbe) {
        this.nazivVezbe = nazivVezbe;
    }

    public void setDistanca(String distanca) {
        this.distanca = distanca;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public void setKalorije(String kalorije) {
        this.kalorije = kalorije;
    }

    public void setProsekNa400(String prosekNa400) {
        this.prosekNa400 = prosekNa400;
    }

    public void setDatumVezbe(Date datumVezbe) {
        this.datumVezbe = datumVezbe;
    }

    public int getId() {
        return id;
    }

    public String getNazivVezbe() {
        return nazivVezbe;
    }

    public String getDistanca() {
        return distanca;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public String getKalorije() {
        return kalorije;
    }

    public String getProsekNa400() {
        return prosekNa400;
    }

    public Date getDatumVezbe() {
        return datumVezbe;
    }
}
