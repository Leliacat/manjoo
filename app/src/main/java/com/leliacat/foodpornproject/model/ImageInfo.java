package com.leliacat.foodpornproject.model;

import android.graphics.Bitmap;

/**
 * Cet objet qui sera stocke dans la memoire interne du telephone contient toutes les informations utiles a notre image.
 */

public class ImageInfo {
    //I.Attributs

    private String titre;
    private Bitmap image;

    //II.Constructeurs
    public ImageInfo(String titre, Bitmap image)
    {

        this.titre = titre;
        this.image = image;
    }

    //III.Accesseurs
    public String getTitre() {
        return titre;
    }

    public Bitmap getImage() {
        return image;
    }

}
