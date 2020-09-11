package com.java.tangningjing.bean;

public class indices {
    public double acitivity ;
    public int citations ;
    public double diversity ;
    public int gindex ;
    public int hindex ;
    public double newStar ;
    public int pubs ;
    public double risingStar ;
    public double sociability ;

    public indices(){};

    public void setAcitivity(double acitivity) {
        try {
            this.acitivity = acitivity;
        }
        catch(Exception e){
            this.acitivity = -1 ;
        }
    }

    public void setCitations(int citations) {
        this.citations = citations;
        try {
            this.citations = citations;
        }
        catch(Exception e){
            this.citations =-1 ;
        }
    }

    public void setDiversity(double diversity) {


        try {
            this.diversity = diversity;
        }
        catch(Exception e){
            this.diversity =-1 ;
        }
    }

    public void setGindex(int gindex) {

        try {
            this.gindex = gindex;
        }
        catch(Exception e){
            this.gindex=-1 ;
        }
    }

    public void setHindex(int hindex) {
        try {
            this.hindex = hindex;
        }
        catch(Exception e){
            this.hindex=-1 ;
        }
    }

    public void setNewStar(double newStar) {
        try {
            this.newStar = newStar;
        }
        catch(Exception e){
            this.newStar=-1 ;
        }
    }

    public void setPubs(int pubs) {

        try {
            this.pubs = pubs;
        }
        catch(Exception e){
            this.pubs=-1 ;
        }
    }

    public void setRisingStar(double risingStar) {
        this.risingStar = risingStar;
    }

    public void setSociability(double sociability) {
        this.sociability = sociability;
    }

    public double getAcitivity() {
        return acitivity;
    }

    public double getDiversity() {
        return diversity;
    }

    public double getNewStar() {
        return newStar;
    }

    public double getRisingStar() {
        return risingStar;
    }

    public double getSociability() {
        return sociability;
    }

    public int getCitations() {
        return citations;
    }

    public int getGindex() {
        return gindex;
    }

    public int getHindex() {
        return hindex;
    }

    public int getPubs() {
        return pubs;
    }

}
