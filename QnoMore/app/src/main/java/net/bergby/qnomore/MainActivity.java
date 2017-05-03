package net.bergby.qnomore;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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
import net.bergby.qnomore.fragments.*;
import net.bergby.qnomore.helpClasses.JsonParserGetMenus;
import net.bergby.qnomore.services.OrderCountDown;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, FoodDrinkFragment.FoodDrinkButtonChosenListener,
        WarmColdFragment.HotColdButtonChosenListener, RestaurantSelectorFragment.RestaurantItemClickedListener, MenuSelectorFragment.MenuItemClickedListener, CheckOutFragment.CheckOutFragmentInterface
{
    // GLOBAL VARIABLES
    private boolean food;
    private boolean drinks;


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

        food = false;
        drinks = false;

        initSidebar();
    }

    private void initSidebar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Runs the "Home" fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.content_main, homeFragment, "HOME");
        Bundle bundle = new Bundle();
        bundle.putString("message", "Welcome!");
        homeFragment.setArguments(bundle);
        fragmentTransaction.commit();

        // Selects the "Home" navigation-drawer item
        navigationView.setCheckedItem(R.id.nav_home);

        // Listener for the fab button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPurchase();
                fab.setVisibility(View.GONE);
                navigationView.setCheckedItem(R.id.menu_none);
            }
        });

    }

    private void startPurchase()
    {
        // Fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FoodDrinkFragment foodDrinkFragment = new FoodDrinkFragment();
        fragmentTransaction.replace(R.id.content_main, foodDrinkFragment, "LOCKED");

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_main);

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        // If it is anything in the backstack
        else if (backStackEntryCount == 0)
        {
            // If the current fragment don't got the tag "locked", go back
            if (!"LOCKED".equals(currentFragment.getTag()))
            {
                getFragmentManager().popBackStack();
            }
            else
            {
                Log.i("Message", "On LOCKED fragment");
            }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Welcome!");
            homeFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_main, homeFragment, "LOCKED");
            if (findViewById(R.id.fab).getVisibility() == View.GONE)
            {
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        }
        else if (id == R.id.nav_order)
        {
            Log.i("Info", "Order clicked");
            MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
            fragmentTransaction.replace(R.id.content_main, myOrdersFragment, "SECOND");
        }
        else if (id == R.id.nav_account_circle)
        {
            Log.i("Info", "Account clicked");
            // Fragment handling
            EditProfileFragment editProfileFragment = new EditProfileFragment();

            fragmentTransaction.replace(R.id.content_main, editProfileFragment, "LOCKED");
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

    @Override
    public void onFoodDrinkButtonSelected(int button)
    {
        // Fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WarmColdFragment warmColdFragment;
        /*
        Button = 1: Drinks
        Button = 2: Food
         */

        drinks = false;
        food = false;

        switch (button)
        {
            case 1:
                drinks = true;
                warmColdFragment = new WarmColdFragment();
                fragmentTransaction.replace(R.id.content_main, warmColdFragment, "SECOND");
                break;
            case 2:
                food = true;
                warmColdFragment = new WarmColdFragment();
                fragmentTransaction.replace(R.id.content_main, warmColdFragment, "SECOND");
                break;
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onHotColdButtonSelected(int button) throws JSONException, IOException, ExecutionException, InterruptedException
    {
        // Fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //FRAGMENT HERE
        RestaurantSelectorFragment restaurantSelectorFragment = new RestaurantSelectorFragment();

        JsonParserGetMenus jsonParser;
        ArrayList<String> restaurantList;
        Bundle bundle;

        /*
        Button = 3: Warm
        Button = 4: Cold
         */

        switch (button)
        {
            case 3:
                jsonParser = new JsonParserGetMenus("https://server.bergby.net/QnoMoreAPI/api/menus", true, false, food, drinks);
                restaurantList = jsonParser.getRestaurantNames();
                bundle = new Bundle();
                bundle.putStringArrayList("restaurantList", restaurantList);
                restaurantSelectorFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_main, restaurantSelectorFragment, "SECOND");
                break;
            case 4:
                jsonParser = new JsonParserGetMenus("https://server.bergby.net/QnoMoreAPI/api/menus", false, true, food, drinks);
                restaurantList = jsonParser.getRestaurantNames();
                bundle = new Bundle();
                bundle.putStringArrayList("restaurantList", restaurantList);
                restaurantSelectorFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_main, restaurantSelectorFragment, "SECOND");
                break;
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onRestaurantItemClicked(String restaurant)
    {

        Bundle bundle;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MenuSelectorFragment menuSelectorFragment = new MenuSelectorFragment();

        bundle = new Bundle();
        bundle.putString("specificRestaurant" ,restaurant);
        menuSelectorFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_main, menuSelectorFragment, "SECOND");

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onMenuItemClicked(double sum, ArrayList<String> items)
    {

        Bundle bundle;

        if (sum != 0.0 || items.isEmpty())
        {
            String stringSum = String.valueOf(sum);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            CheckOutFragment checkOutFragment = new CheckOutFragment();

            // Puts the checkout information in the bundle
            bundle = new Bundle();
            bundle.putString("sum", stringSum);
            bundle.putStringArrayList("items", items);
            checkOutFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.content_main, checkOutFragment, "SECOND");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else
        {
            Log.w("Message", "Shopping cart is empty");
        }
    }

    private ArrayList<String> itemsGlobal;
    private double sumGlobal;

    @Override
    public void onCheckOutFragmentAction(ArrayList<String> items, double sum)
    {

        // Get all keys from preferences, and removes all "quantity" keys
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Map<String, ?> allSharedPrefs = preferences.getAll();
        for (Map.Entry<String, ?> entry : allSharedPrefs.entrySet())
        {
            if (entry.getKey().contains("quantity"))
            {
                editor.remove(entry.getKey());
                editor.apply();
            }
        }

        itemsGlobal = items;
        sumGlobal = sum;
        System.out.println(items + "  " + sum);
        Intent orderCountDownService = new Intent(this, OrderCountDown.class);
        orderCountDownService.putExtra("countDownTime", 10000);
        startService(orderCountDownService);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Thank you for your order!");
        homeFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_main, homeFragment, "LOCKED");
        fragmentTransaction.commit();

        Log.i("Order", "Started service");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            orderCountDownReceiver(intent, itemsGlobal, sumGlobal);
        }
    };

    private void orderCountDownReceiver(Intent intent, ArrayList<String> items, double sum)
    {
        if (!intent.getExtras().isEmpty())
        {
            long millisecondsUntilFinished = intent.getLongExtra("countdown", 0);
            boolean isDone = intent.getBooleanExtra("finished", false);
            if (isDone)
            {
                int icon = R.drawable.ic_check_box;
                String content = "Your order is complete! Click to see pickup code.";
                String title = "Your order is complete!";

                StringBuilder expandedContent = new StringBuilder();
                for (String s: items)
                {
                    expandedContent.append(s).append(", ");
                }

                expandedContent.append("\n").append("The total sum is: ").append("€").append(sum);

                int notificationId = new Random().nextInt(100);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Removes the old notification, and changes it to the pickup notification
                notificationManager.cancel(1);

                android.support.v4.app.NotificationCompat.Builder nBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(icon)
                                .setContentTitle(title)
                                .setContentText(content)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle()
                                .bigText(expandedContent));

                notificationManager.notify(notificationId, nBuilder.build());
            }
            else
            {
                String title = "Your order is being prepared!";
                int icon = R.drawable.ic_action_clock;
                int notificationId = 001;
                String timeLeft =
                String.format(Locale.getDefault(), "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisecondsUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilFinished))
                );

                android.support.v4.app.NotificationCompat.Builder nBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(icon)
                                .setContentTitle(title)
                                .setContentText(timeLeft);

                NotificationManager nNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                nNotifyMgr.notify(notificationId, nBuilder.build());

            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(OrderCountDown.COUNTDOWN_BR));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopService(new Intent(this, OrderCountDown.class));
    }
}
