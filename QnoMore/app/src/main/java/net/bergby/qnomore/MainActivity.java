package net.bergby.qnomore;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import net.bergby.qnomore.fragments.EditProfileFragment;
import net.bergby.qnomore.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView nav_user = (TextView) hView.findViewById(R.id.nav_view_user);
        TextView nav_email = (TextView) hView.findViewById(R.id.nav_view_email);
        CircleImageView nav_userImage = (CircleImageView) hView.findViewById(R.id.profile_image);

        Intent intent = getIntent();
        String email = intent.getStringExtra(LoginActivity.nav_email);
        String user = intent.getStringExtra(LoginActivity.nav_user);
        String image = intent.getStringExtra(LoginActivity.nav_userImage);

        nav_email.setText(email);
        nav_user.setText(user);
        Picasso.with(this)
                .load(image)
                .into(nav_userImage);

        initSidebar();
    }

    private void initSidebar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPurchase();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Runs the "Home" fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.content_main, homeFragment);
        fragmentTransaction.commit();

        // Selects the "Home" navigation-drawer item
        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.content_main, homeFragment);
        }
        else if (id == R.id.nav_account_circle)
        {
            Log.i("Info", "Account clicked");
            // Fragment handling
            EditProfileFragment editProfileFragment = new EditProfileFragment();

            fragmentTransaction.replace(R.id.content_main, editProfileFragment);
        }
        else if (id == R.id.nav_share)
        {
            Log.i("Info", "Share clicked");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.e("Error", "Connection Failed");
    }

}
