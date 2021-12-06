package Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.example.groceryapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.groceryapp.databinding.ActivityCustomerNavBinding;
import com.google.firebase.auth.FirebaseUser;

public class CustomerNav extends AppCompatActivity {

    private ActivityCustomerNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        FirebaseUser current = (FirebaseUser) extras.get("logged in");
        binding = ActivityCustomerNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_cust_home, R.id.navigation_cart, R.id.navigation_cust_history, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_customer_nav);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);



    }




    public void goToStoreView() {
        Intent intent = new Intent(this, StoreNav.class);
    }

}