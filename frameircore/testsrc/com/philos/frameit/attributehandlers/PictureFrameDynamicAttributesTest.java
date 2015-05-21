/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.philos.frameit.attributehandlers;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philos.frameit.model.PictureFrameModel;

/**
 * JUnit Tests for the Frameitcore extension
 */
public class PictureFrameDynamicAttributesTest extends HybrisJUnit4TransactionalTest {
	/**
	 * Edit the local|project.properties to change logging behaviour (properties log4j.*).
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PictureFrameDynamicAttributesTest.class.getName());

	@Before
	public void setUp() {
		// implement here code executed before each test
	}

	@After
	public void tearDown() {
		// implement here code executed after each test
	}

	@Test
	public void testPictureFrameDynamicAttributes() {
		final PictureFrameModel model = Registry.getCoreApplicationContext().getBean("modelService", ModelService.class).create(PictureFrameModel.class);

		model.setWidth(new Integer(1000));
		model.setHeight(new Integer(1000));
		model.setBorderWidth(new Integer(100));

		final String dimensions = model.getDimensions();
		Assert.assertEquals("Dimensions should be 1000x1000cms", "1000x1000cms", dimensions);

		final String displayArea = model.getDisplayArea();
		Assert.assertEquals("Display area should be 900x900cms", "900x900cms", displayArea);
	}
}
