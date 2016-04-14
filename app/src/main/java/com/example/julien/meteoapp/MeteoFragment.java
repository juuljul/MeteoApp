package com.example.julien.meteoapp;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class MeteoFragment extends Fragment {

    private final static String DEBUT_URL_METEO = "http://api.openweathermap.org/data/2.5/forecast?q=";
    // Fin de l'url avec l'API key que j'ai obtenue en m'inscrivant et qui permet d'acceder au Json
    private final static String FIN_URL_METEO ="&APPID=8cae1724449fdda131a00f91c5dae4b5";

    ListView listView;
    MeteoTroisJoursAdapter adapter;
    String villeChoisie;
    WebView webView;
    double longitude, latitude =0;

    public MeteoFragment() {
    }

    public void setVilleChoisie(String villeChoisie) {
        this.villeChoisie = villeChoisie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meteo, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        webView = (WebView) view.findViewById(R.id.webView);

        // L'URL finale est composée de la ville choisie dans le spinner du MainActivity intercalée entre debut_url et fin_url
        String urlTotal = DEBUT_URL_METEO + villeChoisie +FIN_URL_METEO;
        new MeteoTask().execute(urlTotal);

        return view;
    }

    class MeteoTask extends AsyncTask<String,Void,ArrayList<WeatherModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherModel> result) {
            super.onPostExecute(result);

            adapter = new MeteoTroisJoursAdapter(getActivity(),R.layout.meteo_layout,result);
            listView.setAdapter(adapter);

            // On affiche dans le webView la page Google Maps avec les longitude et latitude récupérées dans le Json
            webView.setWebViewClient(new myWebClient());
            webView.getSettings().setJavaScriptEnabled(true);
            String urlMap = "http://www.google.com/maps/place/" + longitude + "," + latitude;
            webView.loadUrl(urlMap);
        }

        public class myWebClient extends WebViewClient {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        protected ArrayList<WeatherModel> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList <WeatherModel> weatherModels = new ArrayList<>();

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                JSONObject jsonParent = new JSONObject(buffer.toString());

                // On récupère les longitude et latitude de la ville en JSon pour pouvoir ensuite les injecter dans l'Url pour le WebView
                JSONObject city = jsonParent.getJSONObject("city");
                JSONObject coord = city.getJSONObject("coord");
                longitude = coord.getDouble("lon");
                latitude = coord.getDouble("lat");

                JSONArray array = jsonParent.getJSONArray("list");
                Gson gson = new Gson();
                // On ne récupère que la météo actuelle et celle des 24 et 48h suivantes, comme les valeurs du tableau sont séparées de 3h,
                // on prend les éléments du tableau à la 0e, 8e, et 16e position.
                for (int i=0;i<3;i++){
                    JSONObject object = array.getJSONObject(8*i);
                    WeatherModel weatherModel = gson.fromJson(object.toString(),WeatherModel.class);
                    weatherModels.add(weatherModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection!=null){
                    connection.disconnect();
                }
                try {
                    if (reader!=null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return weatherModels;
        }
    }


}
