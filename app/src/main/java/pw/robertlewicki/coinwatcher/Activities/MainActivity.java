package pw.robertlewicki.coinwatcher.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dagger.android.AndroidInjection;
import pw.robertlewicki.coinwatcher.Adapters.SectionsPagerAdapter;
import pw.robertlewicki.coinwatcher.R;

public class MainActivity extends AppCompatActivity
{
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setupUi();
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

        if(id == R.id.action_clear_list)
        {
            sectionsPagerAdapter.clearWatchList();
            return true;
        }
        else if(id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupUi()
    {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(), getApplication());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
