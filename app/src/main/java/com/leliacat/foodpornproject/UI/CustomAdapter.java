package com.leliacat.foodpornproject.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.leliacat.foodpornproject.R;
import com.leliacat.foodpornproject.model.ImageInfo;

public class CustomAdapter extends ArrayAdapter<ImageInfo>  {

    public CustomAdapter(Context context, List<ImageInfo> colonne) //On passe à notre adapteur un ArrayList  d'objet contenant le titre de nos photo et les photo elle-même
    {
        super(context,R.layout.custom_row,colonne);
    }



    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customView=inflater.inflate(R.layout.custom_row,parent,false);

        ImageInfo ImageActuelle= getItem(position);//L'item 0 de notre list view correspond à l'objet se trouvant l'index 0 de notre ArrayList
        //L'item 1 de notre list view correspond à l'objet se trouvant à l'index 1 de notre ArrayList

        TextView  titre=(TextView) customView.findViewById(R.id.customrow_textView_titre_id);
        ImageView photo=(ImageView) customView.findViewById(R.id.customrow_imageView_id);

        titre.setText(ImageActuelle.getTitre()); //On définit le titre de l'objet comme étant le titre de notre item correspondant
        photo.setImageBitmap(ImageActuelle.getImage()); //pareil avec la photo.

        return customView;
    }
}

