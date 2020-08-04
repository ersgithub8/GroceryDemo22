package Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import Fragment.My_Pending_Order;
import Fragment.My_Past_Order;


public class PagerOrderAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerOrderAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                My_Pending_Order tab1 = new My_Pending_Order(0);
                return tab1;
            case 1:
                My_Pending_Order tab2 = new My_Pending_Order(4);
                return tab2;
            case 2:
                My_Pending_Order tab3 = new My_Pending_Order(5);
                return tab3;
            case 3:

                My_Pending_Order tab4 = new My_Pending_Order(3);
                return tab4;
//            case 4:
//                My_Pending_Order tab5 = new My_Pending_Order(3);
//                return tab5;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}