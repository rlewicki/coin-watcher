package pw.robertlewicki.coinwatcher.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import pw.robertlewicki.coinwatcher.Adapters.SectionsPagerAdapter;
import pw.robertlewicki.coinwatcher.R;

public class MainActivity extends AppCompatActivity
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

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplication());

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
}
