package com.java.tangningjing.bean;

public class profile {
    public String address ;
    public String affiliation_zh ;
    public String bio;
    public String edu ;
    public String homepage ;
    public String note ;

    public String position;
    public String work ;

    public String getAddress() {
        return address;
    }

    public String getAffiliation_zh() {
        return affiliation_zh;
    }

    public String getBio() {
        return bio;
    }

    public String getEdu() {
        return edu;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getNote() {
        return note;
    }



    public String getPosition() {
        return position;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        try{
            this.work = work;}
        catch(Exception e){
            this.work = "";
        }
    }

    public void setPosition(String position) {
        try{
            this.position = position;}
        catch(Exception e){
            this.position = "";
        }
    }


    public void setAddress(String address) {
        try{
        this.address = address;}
        catch(Exception e){
            this.address = "";
        }
    }

    public void setAffiliation_zh(String affiliation_zh) {

        try{
            this.affiliation_zh = affiliation_zh;}
        catch(Exception e){
            this.affiliation_zh = "";
        }
    }

    public void setBio(String bio) {
        try{
        this.bio = bio;}
        catch(Exception e){
                this.bio = "";
        }
    }

    public void setEdu(String edu) {
        try{
        this.edu = edu;}
        catch(Exception e){
            this.edu = "";
        }
    }

    public void setHomepage(String homepage) {
        try {
            this.homepage = homepage;
        }
                catch(Exception e){
                this.homepage = "";
            }
    }

    public void setNote(String note) {

        try {
            this.note = note;
        }
        catch(Exception e){
            this.note = "";
        }
    }

}
