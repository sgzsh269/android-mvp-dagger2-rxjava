package com.sagarnileshshah.carouselmvp.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sagarnileshshah.carouselmvp.ApplicationComponent;
import com.sagarnileshshah.carouselmvp.R;

/**
 * The abstract base container responsible for showing and destroying {@link Fragment} and handling
 * back and up navigation using the Fragment back stack. This is based on the
 * Fragment Oriented Architecture explained here
 * http://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/
 */
public abstract class FoaBaseActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener, BaseFragmentInteractionListener {

    protected ApplicationComponent applicationComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        initDependencyInjection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDependencyInjection();
    }

    @Override
    protected void onPause() {
        releaseDependencyInjection();
        super.onPause();
    }

    @Override
    public void showFragment(Class<? extends Fragment> fragmentClass, Bundle bundle,
            boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                fragmentClass.getSimpleName());
        Bundle container;
        if (fragment == null) {
            try {
                container = new Bundle();
                container.putBundle(Properties.BUNDLE_KEY_WRAPPED_BUNDLE, bundle);
                fragment = fragmentClass.newInstance();
                fragment.setArguments(container);
            } catch (InstantiationException e) {
                throw new RuntimeException("New Fragment should have been created", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("New Fragment should have been created", e);
            }
        }
        container = fragment.getArguments();
        container.putBundle(Properties.BUNDLE_KEY_WRAPPED_BUNDLE, bundle);

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentPlaceHolder, fragment,
                fragmentClass.getSimpleName());


        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public <T extends Fragment> void showFragment(Class<T> fragmentClass) {
        showFragment(fragmentClass, null, false);
    }


    public void showRetainedFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceHolder);
        if (fragment != null) {
            fragmentTransaction.replace(R.id.fragmentPlaceHolder, fragment,
                    fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
        shouldShowActionBarUpButton();
    }

    public void popFragmentBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void shouldShowActionBarUpButton() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            popFragmentBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldShowActionBarUpButton();
    }

    protected void initDependencyInjection() {
        if (applicationComponent == null) {
            applicationComponent = ((BaseApplication) getApplication()).getApplicationComponent();
            applicationComponent.inject(this);
        }
    }

    private void releaseDependencyInjection() {
        applicationComponent = null;
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}


