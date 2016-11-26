package com.example.avinash.erailseva;

/**
 * Created by avinash on 30/10/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    String uname;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount,String b) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.uname = b;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                tab1 order = new tab1();
                order.uname = this.uname;
                return order;
            case 1:
                tab2 trans = new tab2();
                trans.uname = this.uname;
                return trans;
            case 2:
                tab3 wallet = new tab3();
                wallet.uname = this.uname;
                return wallet;
            default:
                return null;
        }
    }
    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}


