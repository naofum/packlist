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

import junit.framework.TestCase;

/**
 * @author Created by nbossard on 24/01/16.
 */
public class ItemTest extends TestCase {

    public static final String ITEM_TEST_NAME = "ItemTestName";
    public static final String UPDATED_ITEM_TEST_NAME = "UpdatedItemTestName";
    private Item mTestItem;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTestItem = new Item(ITEM_TEST_NAME);
    }

    public void testGetName() throws Exception {
        assertTrue(mTestItem.getName().contentEquals(ITEM_TEST_NAME));
    }

    public void testSetName() throws Exception {
        assertTrue(mTestItem.getName().contentEquals(ITEM_TEST_NAME));
        mTestItem.setName(UPDATED_ITEM_TEST_NAME);
        assertTrue(mTestItem.getName().contentEquals(UPDATED_ITEM_TEST_NAME));
    }

    public void testToString() throws Exception {
        assertNotNull(mTestItem.toString());
        assertTrue(mTestItem.toString().contains(ITEM_TEST_NAME));
    }
}