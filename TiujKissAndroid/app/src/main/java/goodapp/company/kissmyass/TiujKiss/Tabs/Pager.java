package goodapp.company.kissmyass.TiujKiss.Tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * Created by mehme on 14.07.2016.
 */
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Tab1Home tab1 = new Tab1Home();
                return tab1;
            case 1:
                Tab2Chats tab2 = new Tab2Chats();
                return tab2;
            case 2:
                Tab3Feedback tab3 = new Tab3Feedback();
                return tab3;
            case 3:
                Tab4Profile tab4 = new Tab4Profile();
                return tab4;

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