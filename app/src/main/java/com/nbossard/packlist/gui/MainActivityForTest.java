/*
 * PackList is an open-source packing-list for Android
 *
 * Copyright (c) 2016 Nicolas Bossard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.nbossard.packlist.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nbossard.packlist.PackListApp;
import com.nbossard.packlist.R;
import android.util.Log;

import com.nbossard.packlist.model.Trip;
import com.nbossard.packlist.process.saving.ISavingModule;

import java.util.UUID;

import hugo.weaving.DebugLog;


/**
 * Main activity for robotium tests.
 */
public class MainActivityForTest extends AppCompatActivity implements IMainActivity {

// *********************** CONSTANTS**********************************************************************

    /** Log tag. */
    private static final String TAG = MainActivity.class.getName();


// *********************** FIELDS ***************************************************************************

    /** The Floating Action Button. */
    private FloatingActionButton mFab;

    /** Saving module to retrieve Trip. */
    private ISavingModule mSavingModule;

// *********************** METHODS **************************************************************************

    @DebugLog
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.mainact__fab);

        // Handle deep-app indexing
        onNewIntent(getIntent());
    }

    @DebugLog
    @Override
    protected final void onStart() {
        super.onStart();

        /* The saving module to retrieve and update data (trips).*/
        mSavingModule = ((PackListApp) getApplication()).getSavingModule();

        openMainActivityFragment();
    }

    /**
     * Opens the provided fragment in a dialog.
     *
     * @param parDialogStandardFragment fragment to be opened
     */
    @DebugLog
    private void openDialogFragment(final DialogFragment parDialogStandardFragment) {
        if (parDialogStandardFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            parDialogStandardFragment.show(ft, "changelogdemo_dialog");
        }
    }

    @DebugLog
    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @DebugLog
    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_whatsnew) {
            openDialogFragment(new DialogStandardFrag());
        } else  if (id == R.id.action_about) {
            openAboutActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Open {@link AboutActivity} on top of this activity. */
    @DebugLog
    private void openAboutActivity() {
        Intent view = new Intent(this, AboutActivity.class);
        view.setAction(Intent.ACTION_VIEW);
        startActivity(view);
    }

    /**
     * For deep-app indexing.
     * @param intent sic
     */
    @DebugLog
    protected final void onNewIntent(final Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String tripId = data.substring(data.lastIndexOf("/") + 1);
            Trip loadedTrip = mSavingModule.loadSavedTrip(UUID.fromString(tripId));
            openTripDetailFragment(loadedTrip);
        }
    }

// ----------- implementing interface IMainActivity -------------------

    @Override
    @DebugLog
    public final void saveTrip(final Trip parTrip) {
       Log.d(TAG, "saveTrip() faked");
    }

    /**
     * Handle user click on one line and open a new fragment allowing him to see trip
     * Characteristics.
     * @param parTrip trip object to be displayed
     */
    @DebugLog
    @Override
    public final void openTripDetailFragment(final Trip parTrip) {

        // Create fragment and give it an argument specifying the article it should show
        TripDetailFragment newFragment =  TripDetailFragment.newInstance(parTrip);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.mainactcont__fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        // updating FAB action
        mFab.hide();
    }

    @Override
    public void openNewTripFragment(UUID parTripId) {

    }

    @Override
    public final void showFAB(final boolean parShow) {
        if (parShow) {
            mFab.show();
        } else {
            mFab.hide();
        }
    }
    // ----------- end of implementing interface IMainActivity ------------

// *********************** PRIVATE METHODS ******************************************************************

    /**
     * Open a new fragment allowing him to view trip list.
     */
    @DebugLog
    private void openMainActivityFragment() {

        // Create fragment and give it an argument specifying the article it should show
        MainActivityFragment newFragment = new MainActivityFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.mainactcont__fragment, newFragment);
        // NO add to backstack, this is lowest level fragment

        // Commit the transaction
        transaction.commit();

        // updating FAB action
        mFab.show();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                openNewTripFragment();
            }
        });
    }

    /**
     * Handle user click on "add a trip" button and open a new fragment allowing him to input trip
     * Characteristics.
     */
    @DebugLog
    @VisibleForTesting
    protected void openNewTripFragment() {

        // Create fragment and give it an argument specifying the article it should show
        NewTripFragment newFragment = new NewTripFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.mainactcont__fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        // updating FAB action
        mFab.hide();

    }
//
}
