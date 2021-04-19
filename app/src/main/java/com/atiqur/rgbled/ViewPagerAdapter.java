package com.atiqur.rgbled;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.atiqur.rgbled.fragments.FragmentOne;
import com.atiqur.rgbled.fragments.FragmentTwo;

public class ViewPagerAdapter extends FragmentStateAdapter {
    FragmentOne fragmentOne = new FragmentOne();
    FragmentTwo fragmentTwo = new FragmentTwo();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return fragmentOne;
            case 1:
                return fragmentTwo;
        }
        return new FragmentOne();
    }

    public FragmentOne getFragmentOne(){
        return fragmentOne;
    }

    public FragmentTwo getFragmentTwo(){
        return fragmentTwo;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
