package com.example.julien.meteoapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {


    Spinner choixVille;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choixVille = (Spinner) findViewById(R.id.choixVille);
        adapter = ArrayAdapter.createFromResource(this,R.array.noms_villes,R.layout.support_simple_spinner_dropdown_item);
        choixVille.setAdapter(adapter);

        // On place le fragment qui contient le logo d'accueil
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AccueilFragment accueilFragment = new AccueilFragment();
        transaction.add(R.id.fragmentContainer,accueilFragment);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayWeather(View view) {
        // On remplace le fragment avec le logo d'accueil par le fragment avec la météo des 3 jours et le plan de la ville
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MeteoFragment meteoFragment = new MeteoFragment();
        meteoFragment.setVilleChoisie(choixVille.getSelectedItem().toString());
        transaction.replace(R.id.fragmentContainer, meteoFragment);
        transaction.commit();
    }



}
