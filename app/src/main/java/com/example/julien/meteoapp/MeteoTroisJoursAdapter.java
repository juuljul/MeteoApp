package com.example.julien.meteoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by julien on 14/04/2016.
 */
public class MeteoTroisJoursAdapter extends ArrayAdapter {


    ArrayList<WeatherModel> weatherModels;
    Context context;

    public MeteoTroisJoursAdapter(Context context, int resource, ArrayList<WeatherModel> weatherModels) {
        super(context, resource, weatherModels);
        this.context = context;
        this.weatherModels = weatherModels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder= null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meteo_layout, parent, false);

            holder = new ViewHolder();

            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.tempMin = (TextView) convertView.findViewById(R.id.tempMin);
            holder.tempMax = (TextView) convertView.findViewById(R.id.tempMax);
            holder.iconMeteo = (ImageView) convertView.findViewById(R.id.iconMeteo);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();

            final ProgressBar loadingCircle = (ProgressBar) convertView.findViewById(R.id.loadingCircle);
            loadingCircle.setVisibility(View.VISIBLE);

            // On convertit la date en nombres récupérée dans leJSon en une date avec jour de la semaine et mois en lettres
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateMeteo = null;
            try {
                dateMeteo = df.parse(weatherModels.get(position).getDt_txt());
                SimpleDateFormat formater = null;
                formater = new SimpleDateFormat("EEEE dd MMMM yyyy 'à' hh:mm:ss", Locale.FRANCE);
                holder.date.setText(formater.format(dateMeteo));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            // On affiche les températures max et min qu'on convertit de Kelvin en Celsius
            holder.tempMin.setText("Min: " + (int)(weatherModels.get(position).getMain().getTemp_min()- 273.15) + "°");
            holder.tempMax.setText("Max: " + (int)(weatherModels.get(position).getMain().getTemp_max()- 273.15) + "°");

            // Je n'ai distingué que les valeurs de JSon "Clear","Clouds","Rain", il en existe certainement d'autres, elles seront par defaut représentées par l'icone orage
            switch (weatherModels.get(position).getWeather().get(0).getMain()){
                case "Clear":
                    holder.iconMeteo.setImageResource(R.drawable.soleil);
                    break;
                case "Clouds":
                    holder.iconMeteo.setImageResource(R.drawable.nuage);
                    break;
                case "Rain":
                    holder.iconMeteo.setImageResource(R.drawable.pluie);
                    break;
                default:
                    holder.iconMeteo.setImageResource(R.drawable.orage);
                    break;
            }
            // Le progressBar d'attente disparait une fois que toute la meteo du jour donné est affichée
            loadingCircle.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView date, tempMin, tempMax;
        ImageView iconMeteo;
    }

}
