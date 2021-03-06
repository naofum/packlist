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

package com.nbossard.packlist.model;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test class for {@link Trip} class.
 * @author Created by nbossard on 23/01/16.
 */
public class TripTest  {

    private static final String TRIP_NAME = "London";
    private static final String UPDATED_TRIP_NAME = "Dublin";
    private static final GregorianCalendar TRIP_DATE = new GregorianCalendar(2015,2,25);
    private static final GregorianCalendar UPDATED_TRIP_DATE = new GregorianCalendar(2015,2,28);
    private static final GregorianCalendar TRIP_END = new GregorianCalendar(2015,3,30);
    private static final GregorianCalendar UPDATED_TRIP_END = new GregorianCalendar(2015,4,30);
    private static final String TRIP_NOTE = "A nice trip";
    private static final String UPDATED_TRIP_NOTE = "A REALLY nice trip";
    public static final String NEW_ITEM_NAME = "newItemName";
    private Trip mTestTrip;

    @Before
    public void setUp() throws Exception {
        mTestTrip = new Trip(TRIP_NAME, TRIP_DATE, TRIP_END, TRIP_NOTE);
    }

    @Test
    public void testSetNote() throws Exception {
        assertEquals(mTestTrip.getNote(), TRIP_NOTE);
        mTestTrip.setNote(UPDATED_TRIP_NOTE);
        assertEquals(mTestTrip.getNote(), UPDATED_TRIP_NOTE);
    }

    @Test
    public void testGetNote() throws Exception {
        assertEquals(mTestTrip.getNote(), TRIP_NOTE);
        mTestTrip.setNote(UPDATED_TRIP_NOTE);
        assertEquals(mTestTrip.getNote(), UPDATED_TRIP_NOTE);
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(mTestTrip.getName(), TRIP_NAME);
        mTestTrip.setName(UPDATED_TRIP_NAME);
        assertEquals(mTestTrip.getName(), UPDATED_TRIP_NAME);
    }

    @Test
    public void testSetName() throws Exception {
        assertEquals(mTestTrip.getName(), TRIP_NAME);
        mTestTrip.setName(UPDATED_TRIP_NAME);
        assertEquals(mTestTrip.getName(), UPDATED_TRIP_NAME);
    }

    @Test
    public void testGetStartDate() throws Exception {
        assertEquals(mTestTrip.getStartDate(), TRIP_DATE);
        mTestTrip.setStartDate(UPDATED_TRIP_DATE);
        assertEquals(mTestTrip.getStartDate(), UPDATED_TRIP_DATE);
    }

    @Test
    public void testSetStartDate() throws Exception {
        assertEquals(mTestTrip.getStartDate(), TRIP_DATE);
        mTestTrip.setStartDate(UPDATED_TRIP_DATE);
        assertEquals(mTestTrip.getStartDate(), UPDATED_TRIP_DATE);
    }

    @Test
    public void testGetEndDate() throws Exception {
        assertEquals(mTestTrip.getEndDate(), TRIP_END);
        mTestTrip.setEndDate(UPDATED_TRIP_END);
        assertEquals(mTestTrip.getEndDate(), UPDATED_TRIP_END);
    }

    @Test
    public void testSetEndDate() throws Exception {
        assertEquals(mTestTrip.getEndDate(), TRIP_END);
        mTestTrip.setEndDate(UPDATED_TRIP_END);
        assertEquals(mTestTrip.getEndDate(), UPDATED_TRIP_END);
    }

    @Test
    public void testGetUUID() throws Exception {
        assertNotNull(mTestTrip.getUUID().toString());
        assertTrue(mTestTrip.getUUID().toString().length()>0);
    }

    @Test
    public void testAddItem() throws Exception {
        assertTrue(mTestTrip.getListItem().size()==0);
        mTestTrip.addItem(NEW_ITEM_NAME);
        assertTrue(mTestTrip.getListItem().size()==1);
        assertNotNull(mTestTrip.getListItem().get(0).getName());
        assertTrue(mTestTrip.getListItem().get(0).getName().contentEquals(NEW_ITEM_NAME));
    }

    @Test
    public void testGetListItem() throws Exception {
        assertTrue(mTestTrip.getListItem().size()==0);
        mTestTrip.addItem(NEW_ITEM_NAME);
        assertTrue(mTestTrip.getListItem().size()==1);
    }

    @Test
    public void testToString() throws Exception {
        assertNotNull(mTestTrip.toString());
        assertTrue(mTestTrip.toString().contains(TRIP_NAME));
        assertTrue(mTestTrip.toString().contains(TRIP_NOTE));
    }
}