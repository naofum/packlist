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

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nbossard.packlist.R;
import com.nbossard.packlist.databinding.FragmentTripDetailBinding;
import com.nbossard.packlist.model.Item;
import com.nbossard.packlist.model.Trip;
import com.nbossard.packlist.process.saving.ISavingModule;

import hugo.weaving.DebugLog;
/*
@startuml
    class com.nbossard.packlist.gui.TripDetailFragment {
    }

@enduml
 */

/**
 * Open a Trip for viewing / editing.
 * @author Created by nbossard on 09/01/16.
 */
public class TripDetailFragment extends Fragment {

    // ********************** CONSTANTS *********************************************************************

    /** Bundle mandatory parameter when instantiating this fragment. */
    public static final String BUNDLE_PAR_TRIP_ID = "bundleParTripId";

    // *********************** FIELDS ***********************************************************************

    /** The root view, will be used to findViewById. */
    private View mRootView;

    /** Trip object to be displayed and added item. */
    private Trip mRetrievedTrip;

    /** Supporting activity, to save trip.*/
    private IMainActivity mIMainActivity;

    /**
     * The object to support Contextual Action Bar (CAB).
     */
    private ActionMode mActionMode;

    /** List of {@link Item} view. */
    private ListView mItemListView;

    /** Edit text. */
    private EditText mNewItemEditText;

    /** Add item button. */
    private Button mAddItemButton;

    // *********************** LISTENERS ********************************************************************

    /**
     * Listener for click on one item of the list.
     * Opens a new fragment displaying detail on item.
     */
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        @DebugLog
        public void onItemClick(final AdapterView<?> parent,
                                final View view,
                                final int parPosition,
                                final long id) {
            Item selectedItem = (Item) mItemListView.getItemAtPosition(parPosition);
            selectedItem.setPacked(!selectedItem.isPacked());
            mIMainActivity.saveTrip(mRetrievedTrip);
            populateList();
        }
    };

    /**
     * Listener for long click on one item of the list.
     * Opens the contextual action bar.
     */
    @NonNull
    private AdapterView.OnItemLongClickListener mLongClickListener =
            new AdapterView.OnItemLongClickListener() {
        @DebugLog
        @Override
        public boolean onItemLongClick(final AdapterView<?> arg0, final View arg1,
                                       final int pos, final long id) {

            // keep item selected
            mItemListView.setItemChecked(pos, true);

            // starting action mode
            mActionMode = getActivity().startActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
                    mode.setTitle("Selected");

                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_main_cab, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_delete:
                            int position = (int) mActionMode.getTag();
                            deleteItemClicked(position);
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(final ActionMode mode) {
                }
            });
            mActionMode.setTag(pos);
            arg1.setSelected(true);
            return true;
        }

    };


    // *********************** METHODS **********************************************************************

    /**
     * Create a new instance of MyFragment that will be initialized
     * with the given arguments.
     * @param parTrip ttrip to be displayed
     */
    public static TripDetailFragment newInstance(final Trip parTrip) {
        TripDetailFragment f = new TripDetailFragment();
        Bundle b = new Bundle();
        b.putSerializable(BUNDLE_PAR_TRIP_ID, parTrip);
        f.setArguments(b);
        return f;
    }

    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIMainActivity = (IMainActivity) getActivity();

        Bundle args = getArguments();
        if (args != null) {
            mRetrievedTrip = (Trip) args.getSerializable(BUNDLE_PAR_TRIP_ID);
        }

    }

    /**
    * Create the view for this fragment, using the arguments given to it.
    */
    @Override
    public final View onCreateView(final LayoutInflater inflater,
                                       final ViewGroup container,
                                       final Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        // Magic of binding
        // Do not use this syntax, it will overwrite actvity (we are in a fragment)
        //mBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_trip_detail);
        FragmentTripDetailBinding mBinding = DataBindingUtil.bind(mRootView);
        mBinding.setTrip(mRetrievedTrip);
        mBinding.executePendingBindings();

        return mRootView;
    }

    @DebugLog
    @Override
    public final void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO old style, improve this
        mNewItemEditText = (AppCompatEditText) mRootView.findViewById(R.id.trip_detail__new_item__edit);

        mAddItemButton = (Button) mRootView.findViewById(R.id.trip_detail__new_item__button);
        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickAddItem();
            }
        });
        mAddItemButton.setEnabled(false);
        disableButtonIfEmptyText(mAddItemButton);

        mNewItemEditText.setOnEditorActionListener(new AppCompatEditText.OnEditorActionListener() {
            @DebugLog
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    mAddItemButton.performClick();
                    handled = true;
                }
                return handled;
            }
        });

        final Button editButton = (Button) mRootView.findViewById(R.id.trip_detail__edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditTrip();
            }
        });

        populateList();
    }

    @DebugLog
    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @DebugLog
    @Override
    public void onStop() {
        super.onStop();
    }

    @DebugLog
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Handle click on "Add item" button.
     * Will add a new item.
     */
    public final void onClickEditTrip() {
        ((IMainActivity) getActivity()).openNewTripFragment(mRetrievedTrip.getUUID());
    }

    /**
     * Handle click on "Add item" button.
     * Will add a new item.
     */
    public final void onClickAddItem() {
        String tmpStr = mNewItemEditText.getText().toString();
        mRetrievedTrip.addItem(tmpStr);
        mIMainActivity.saveTrip(mRetrievedTrip);
        mNewItemEditText.setText("");
        populateList();
    }

    // *********************** PRIVATE METHODS **************************************************************

    /**
     * Effectively delete selected item then refresh the list.
     *
     * @param parPosition position in list of item to be deleted
     */
    private void deleteItemClicked(final int parPosition) {
        Item selectedItem = (Item) mItemListView.getItemAtPosition(parPosition);
        mRetrievedTrip.deleteItem(selectedItem.getUUID());
        mIMainActivity.saveTrip(mRetrievedTrip);
        mActionMode.finish();
        populateList();
    }

    /**
     * Disable the "Add item" button if item text is empty.
     * @param parMButton button to be disabled
     */
    private void disableButtonIfEmptyText(final Button parMButton) {
        EditText newItem = (EditText) mRootView.findViewById(R.id.trip_detail__new_item__edit);
        newItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s,
                                          final int start,
                                          final int count,
                                          final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s,
                                      final int start,
                                      final int before,
                                      final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                parMButton.setEnabled(s.length() > 0);
            }
        });
    }

    /**
     * Populate list with data in {@link ISavingModule}.
     */
    private void populateList() {
        mItemListView = (ListView) mRootView.findViewById(R.id.trip_detail__list);
        mItemListView.setEmptyView(mRootView.findViewById(R.id.trip_detail__list_empty));
        ItemAdapter itemAdapter = new ItemAdapter(mRetrievedTrip.getListItem(), this.getActivity());
        mItemListView.setAdapter(itemAdapter);
        mItemListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mItemListView.setOnItemClickListener(mItemClickListener);
        mItemListView.setOnItemLongClickListener(mLongClickListener);
        mItemListView.invalidate();
    }

}
