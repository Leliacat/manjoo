package com.leliacat.foodpornproject.activities;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakePic extends AppCompatActivity {

    static final int  REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prisePhoto();

    }

    public String nomPhoto()
    {
        SimpleDateFormat datePresente = new SimpleDateFormat("ddMMyyyy_HHmmssMM"); //Creer un objet servant à obtenir un timestamp
        String timestamp = datePresente.format(new Date()); //On capture notre timestamp dans une chaîne de caractère
        String nomPhoto = " FoodPorn" + timestamp + ".jpg"; //On crée le nom de notre photo a partir de ce timestamp
        return nomPhoto;
    }

    public void prisePhoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap)extras.get("data");
            sauvegardeImage(photo);
        }
        Intent allezMain = new Intent(this, MainActivity.class);
        startActivity(allezMain);
    }

    private void sauvegardeImage(Bitmap photo)
    {   ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File fichier = wrapper.getDir("Images", MODE_PRIVATE);//On créer un fichier égal à un dossier de notre mémoire de stockage interne. (Si le dossier n'existe pas il est créer à l'appel de cette fonction)
        fichier = new File(fichier, nomPhoto());

        try{
            OutputStream fluxImage; //On créer un flux
            fluxImage = new FileOutputStream(fichier); //On indique que ce flux servira à enregistrer une image dans un dossier
            photo.compress(Bitmap.CompressFormat.JPEG,100, fluxImage); //On enregistre notre image passé en argument dans ce dossier
            fluxImage.flush(); //On libère la mémoire tampon de notre flux
            fluxImage.close();//On referme notre flux
            Toast.makeText(getApplicationContext(),"reussi",Toast.LENGTH_LONG);

        }
        catch(Exception e)
        {

        }

    }

}