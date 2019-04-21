package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Element extends ToDo {
    public List<Tag> tags = new ArrayList<>();
    public int etat;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String vals = "\n" + "</br>DateCreation: " + sdf.format(super.getDateCreation()) + "\n" +
                "</br>DateDerModif: " + sdf.format(super.getDateDerModif()) + "\n" +
                "</br>Titre: " + super.getTitre() + "\n" +
                "</br>Description: " + super.getDescription() + "\n" +
                "</br>";
        return vals;
    }


}
