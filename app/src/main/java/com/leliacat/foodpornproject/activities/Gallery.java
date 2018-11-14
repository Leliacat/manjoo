package com.leliacat.foodpornproject.activities;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.leliacat.foodpornproject.R;
import com.leliacat.foodpornproject.UI.CustomAdapter;
import com.leliacat.foodpornproject.model.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {


    // on cree une variable globale nous permettant de connaître a tout moment l'index du numero de l'item sur lequel l'utilisateur a cliqué
    public static String positionItem = "";
    // Nous sert à enregistrer les noms de nos differentes images
    String[] nomImage;
    // Une liste d'objets nous permettant d'associer une image avec un titre
    private List<ImageInfo> tableauImage = new ArrayList<ImageInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // On utilise un wrapper pour récupérer notre fichier image
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File fichier = wrapper.getDir("Images", MODE_PRIVATE);

        // on génère la liste des noms de nos différentes images
        nomImage = fichier.list();

        chargerTableauImage();

        // On passe notre objet à notre adapteur.
        ListAdapter galerieAdpater = new CustomAdapter(this, tableauImage);
        final ListView galerie = (ListView) findViewById(R.id.gallery_listview_id);
        galerie.setAdapter(galerieAdpater);

        galerie.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //Si l'utilisateur réalise un click long
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // On utilise un wrapper pour récupérer notre fichier image.
                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                File fichier = wrapper.getDir("Images", MODE_PRIVATE);

                //On créer un dossier indiquant le chemin de l'image correspondant à l'item selectionné.$
                File cheminImage = new File(fichier, nomImage[position]);
                boolean supression = cheminImage.delete(); //supprime l'image
                recreate();
                return true;
            }
        });

    }

    private void chargerTableauImage() {

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File fichier = wrapper.getDir("Images", MODE_PRIVATE);
        Bitmap image = null;

        for (int i = 0; i < nomImage.length; i++) {

            try {
                File cheminImage = new File(fichier, nomImage[i]); //On ouvre chaque image disponible dans notre stockage interne
                image = BitmapFactory.decodeFile(cheminImage.getPath());//
            } catch (Exception e) {

            }

            tableauImage.add(new ImageInfo(nomImage[i], image));//et on l'ajoute à notre tableau d'objet
        }
    }
}