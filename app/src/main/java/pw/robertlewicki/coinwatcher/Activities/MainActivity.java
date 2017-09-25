package pw.robertlewicki.coinwatcher.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import pw.robertlewicki.coinwatcher.Adapters.SectionsPagerAdapter;
import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;
import pw.robertlewicki.coinwatcher.R;

public class MainActivity extends AppCompatActivity implements IFileStorageHandler
{
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(), getApplication(), this);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                sectionsPagerAdapter.queryCurrencies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                sectionsPagerAdapter.queryCurrencies(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            return true;
        }
        else if(id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveToFile(String filepath, String data)
    {
        try
        {
            FileOutputStream file = openFileOutput(filepath, Context.MODE_PRIVATE);
            file.write(data.getBytes());
            file.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String loadFromFile(String filepath)
    {
        try
        {
            String fileContent;

            FileInputStream file = openFileInput(filepath);
            InputStreamReader inputStreamReader = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String buffer;
            while((buffer = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(buffer);
            }
            fileContent = stringBuilder.toString();
            return fileContent;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveToSharedPreferences(String key, long value)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    @Override
    public long loadFromSharedPreferences(String key)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long value = preferences.getLong(key, -1);
        return value;
    }
}
