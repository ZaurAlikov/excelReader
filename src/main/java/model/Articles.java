package model;

public class Articles {
    private String oldArt;
    private String newArt;

    public Articles(String oldArt, String newArt) {
        this.oldArt = oldArt;
        this.newArt = newArt;
    }

    public String getOldArt() {
        return oldArt;
    }

    public void setOldArt(String oldArt) {
        this.oldArt = oldArt;
    }

    public String getNewArt() {
        return newArt;
    }

    public void setNewArt(String newArt) {
        this.newArt = newArt;
    }
}
