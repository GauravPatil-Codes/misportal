package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;

@Embeddable
public class MonthlyTarget {
    @Column(name = "jan_target")
    private Integer jan = 0;
    
    @Column(name = "feb_target")
    private Integer feb = 0;
    
    @Column(name = "mar_target")
    private Integer mar = 0;
    
    @Column(name = "apr_target")
    private Integer apr = 0;
    
    @Column(name = "may_target")
    private Integer may = 0;
    
    @Column(name = "june_target")
    private Integer june = 0;
    
    @Column(name = "july_target")
    private Integer july = 0;
    
    @Column(name = "aug_target")
    private Integer aug = 0;
    
    @Column(name = "sep_target")
    private Integer sep = 0;
    
    @Column(name = "oct_target")
    private Integer oct = 0;
    
    @Column(name = "nov_target")
    private Integer nov = 0;
    
    @Column(name = "dec_target")
    private Integer dec = 0;
    
    // Default constructor
    public MonthlyTarget() {}

    // Getters
    public Integer getJan() { return jan; }
    public Integer getFeb() { return feb; }
    public Integer getMar() { return mar; }
    public Integer getApr() { return apr; }
    public Integer getMay() { return may; }
    public Integer getJune() { return june; }
    public Integer getJuly() { return july; }
    public Integer getAug() { return aug; }
    public Integer getSep() { return sep; }
    public Integer getOct() { return oct; }
    public Integer getNov() { return nov; }
    public Integer getDec() { return dec; }

    // Setters
    public void setJan(Integer jan) { this.jan = jan; }
    public void setFeb(Integer feb) { this.feb = feb; }
    public void setMar(Integer mar) { this.mar = mar; }
    public void setApr(Integer apr) { this.apr = apr; }
    public void setMay(Integer may) { this.may = may; }
    public void setJune(Integer june) { this.june = june; }
    public void setJuly(Integer july) { this.july = july; }
    public void setAug(Integer aug) { this.aug = aug; }
    public void setSep(Integer sep) { this.sep = sep; }
    public void setOct(Integer oct) { this.oct = oct; }
    public void setNov(Integer nov) { this.nov = nov; }
    public void setDec(Integer dec) { this.dec = dec; }

    @Override
    public String toString() {
        return "MonthlyTarget{" +
                "jan=" + jan +
                ", feb=" + feb +
                ", mar=" + mar +
                ", apr=" + apr +
                ", may=" + may +
                ", june=" + june +
                ", july=" + july +
                ", aug=" + aug +
                ", sep=" + sep +
                ", oct=" + oct +
                ", nov=" + nov +
                ", dec=" + dec +
                '}';
    }
}