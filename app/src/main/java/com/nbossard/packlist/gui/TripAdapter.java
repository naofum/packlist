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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbossard.packlist.R;
import com.nbossard.packlist.model.Trip;

import java.util.List;

/**
 * An adapter for displaying a {@link Trip} in a ListView.
 *
 * @author Created by nbossard on 25/12/15.
 */
class TripAdapter extends BaseAdapter {

    // *********************** INNER CLASS *****************************************************************

    /**
     * View holder, works better for recycling of views.
     *
     * @author Nicolas BOSSARD
     *
     */
    private class InnerMyViewHolder
    {

        // getting views
        /**
         * Reference (result of findviewbyid) to the trip name.
         */
        private TextView tvName;

        /**
         * Reference (result of findviewbyid) to the trip start date.
         */
        private TextView tvStartDate;

        /** The arrow between start and end date. */
        private ImageView arrowDate;

        /**
         * Reference (result of findviewbyid) to the trip end date.
         */
        private TextView tvEndDate;
    }

    // ********************** FIELDS ************************************************************************

    /**
     * Devices to be displayed in the list.
     */
    private final List<Trip> mTripsList;

    /**
     * Provided context.
     */
    private final Context mContext;

    // *********************** METHODS **********************************************************************

    /**
     * Standard constructor.
     *
     * @param parResList
     *            data to be displayed in the list.
     * @param parContext
     *            context sic
     */
    TripAdapter(final List<Trip> parResList, final Context parContext)
    {
        super();
        mTripsList = parResList;
        mContext = parContext;
    }


    @Override
    public int getCount() {
        return mTripsList.size();
    }

    @Override
    public Object getItem(final int parPosition) {
        return mTripsList.get(parPosition);
    }

    @Override
    public long getItemId(final int parPosition) {
        return parPosition;
    }

    @Override
    public View getView(final int parPosition, View parConvertView, final ViewGroup parParentView) {
        InnerMyViewHolder vHolderRecycle;
        if (parConvertView == null)
        {
            vHolderRecycle = new InnerMyViewHolder();
            final LayoutInflater inflater = (LayoutInflater) TripAdapter.this.
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            parConvertView = inflater.inflate(R.layout.trip_adapter, parParentView, false);
        } else
        {
            vHolderRecycle = (InnerMyViewHolder) parConvertView.getTag();
        }
        // getting views
        vHolderRecycle.tvName = (TextView) parConvertView.findViewById(R.id.ta__name);
        vHolderRecycle.tvStartDate = (TextView) parConvertView.findViewById(R.id.ta__start_date);
        vHolderRecycle.arrowDate = (ImageView) parConvertView.findViewById(R.id.ta__arrow_date);
        vHolderRecycle.tvEndDate = (TextView) parConvertView.findViewById(R.id.ta__end_date);

        final Trip oneDev = mTripsList.get(parPosition);

        // updating views
        vHolderRecycle.tvName.setText(oneDev.getName());
        vHolderRecycle.tvStartDate.setText(oneDev.getFormattedStartDate());
        vHolderRecycle.tvEndDate.setText(oneDev.getFormattedEndDate());
        if ((oneDev.getStartDate() == null) && (oneDev.getEndDate() == null)) {
            vHolderRecycle.arrowDate.setVisibility(View.INVISIBLE);
        } else {
            vHolderRecycle.arrowDate.setVisibility(View.VISIBLE);
        }

        parConvertView.setTag(vHolderRecycle);
        return parConvertView;
    }
}
