package com.example.groceryapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHandler {

    Context context;
    View view;
    BottomNavigationView nav;
    Menu menu;
    Intent result;
    Intent starter;
    int id_selected;

    public NavigationHandler() {
        //need intent

    }

    public void initVars() {
        context = view.getContext();

        Bundle extras = starter.getExtras();
        int id_selected = (int) extras.get("id_selected");
//        assignMenu();

    }

//    public int getMenu() {
//
//    }
//
//    public int getFlag() {
//
//    }

    public int getMenu(int prev_menu, int prev_item) {
        if (prev_item == R.id.nav_main_home) return R.menu.master_menu;
        else if(prev_menu == -1 && prev_item ==-1)return R.menu.master_menu;
        else return R.menu.master_menu;

    }

    public int getChecked(int prev_menu, int prev_item) {
        if (prev_item == R.id.nav_main_home) return 2;
        else if (prev_menu == -1 && prev_item == -1) return 2;
        return 2;


    }


    public void assignMenu() {
        int menu;
        int flag;
        switch (id_selected){
            default:
                menu = R.menu.master_menu;
                flag = 2;
                break;
            case R.id.nav_main_home:
                menu = R.menu.master_menu;
                flag = 2;
                break;
            case R.id.nav_signup:
                menu = R.menu.master_menu;
                flag = 3;
                break;
            case R.id.nav_login:
                menu = R.menu.master_menu;
                flag = 4;
                break;
        }


//        MenuInflater inflater = getMenuInflater();




    }

    public void assignChecked(int i) {
        BottomNavigationViewHelper.disableShiftMode(nav);
        Menu menuOn = nav.getMenu();
        MenuItem menuItem = menuOn.getItem(i);
        menuItem.setChecked(true);
    }



    public NavigationHandler(View _view, Intent _starter, BottomNavigationView _nav) {
        nav = _nav;
        initVars();



    }

    public Intent handle(int id, Context context) {
        Intent intent;
        int id_selected_flag = -1;
        switch (id) {
            default:
                intent = new Intent(context, MainActivity.class);
                id_selected_flag = 2;
                break;
            case R.id.nav_login:
                intent = new Intent(context, ShowLoginActivity.class);
                break;
            case R.id.nav_signup:
                intent = new Intent(context, SignUpActivity.class);
                break;
        }


        intent = addIntentExtras(intent);
        return intent;
    }

    public Intent addIntentExtras(Intent intent) {
        return intent;
    }


    public Intent goToRoot(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return(intent);
    }
    public Intent goToLogin(Context context) {
        return new Intent(context, ShowLoginActivity.class);
    }
    public Intent goToSignUp(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    public Intent showLogin() {
        Intent intent = new Intent(context, ShowLoginActivity.class);
        return (intent);
    }

    public Intent showSignup(View view) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return (intent);
    }






}


