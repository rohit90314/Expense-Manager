package com.example.rohit.expensemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;

    private Dashboard_Fragment dash_frag;
    private IncomeFragment inc_frag;
    private ExpenseFragment exp_frag;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        dash_frag = new Dashboard_Fragment();
        inc_frag = new IncomeFragment();
        exp_frag = new ExpenseFragment();

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.Draw_open,R.string.Draw_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        setFragment(dash_frag);
        bottomNavigationView = findViewById(R.id.btmNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.dashboard:
                        setFragment(dash_frag);
                        bottomNavigationView.setBackgroundResource(R.color.dash_clr);

                        return true;

                    case R.id.income:
                        setFragment(inc_frag);
                        bottomNavigationView.setBackgroundResource(R.color.inc_clr);
                        return true;

                    case R.id.expense:
                        setFragment(exp_frag);
                        bottomNavigationView.setBackgroundResource(R.color.exp_clr);

                        return true;

                    default:
                        return false;

                }
            }
        });
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END))
        {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else
        {
            super.onBackPressed();
        }

    }

    public void displaySelcetedListener(int id)
    {
        Fragment fragment = null;
        switch(id)
        {
            case R.id.Dashboard_Nav:
                fragment = dash_frag;
                bottomNavigationView.setBackgroundResource(R.color.dash_clr);

                break;
            case R.id.Income_Nav:
                fragment = inc_frag;
                bottomNavigationView.setBackgroundResource(R.color.inc_clr);
                break;

            case R.id.Expense_Nav:
                bottomNavigationView.setBackgroundResource(R.color.exp_clr);
                fragment = exp_frag;
                break;
            case R.id.Logout_Nav:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            default:
                fragment = null;
        }
        if((fragment !=null))
        {
            setFragment(fragment);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelcetedListener(item.getItemId());
        return true;
    }
}
