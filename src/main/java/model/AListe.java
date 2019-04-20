package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public abstract class AListe {

    //
    private int id;
    //
    private String titre;
    //
    private String description;
    //
    private Date dateCreation;
    //
    private Date dateDerModif;

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitre() {
        return titre;
    }

    /**
     *
     * @param titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     *
     * @param dateCreation
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     *
     * @return
     */
    public Date getDateDerModif() {
        return dateDerModif;
    }

    /**
     *
     * @param dateDerModif
     */
    public void setDateDerModif(Date dateDerModif) {
        this.dateDerModif = dateDerModif;
    }

    @Override
    public String toString(){
        String vals = "\n" + "</br>Titre: " + titre + "\n" +
                "</br>Description: " + description + "\n" +
                //"</br>Liste d'éléments: " + listElement + "\n" +
                "</br>";
        return vals;
    }
}
