package com.philos.frameit.services;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philos.frameit.model.PictureFrameModel;
import com.philos.frameit.systemsetup.FrameitCoreSystemSetup;

/**
 * JUnit Tests for the PictureFrameService
 */
public class PictureFrameServiceTest extends HybrisJUnit4TransactionalTest {

	/**
	 * Edit the local|project.properties to change logging behaviour (properties log4j.*).
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PictureFrameServiceTest.class.getName());

	private final PictureFrameService pictureFrameService = Registry.getCoreApplicationContext().getBean("pictureFrameService", PictureFrameService.class);

	@Before
	public void setUp() {
		LOG.info("Current tenant is: " + Registry.getCurrentTenantNoFallback().getTenantID());

		// Be aware here that the user running the tests is not the admin user, and is therefore subject
		// to restrictions - such as catalog version in the session and only retrieving products in queries
		// that have their approval status set to approved.

		// Because it is a transactional unit test, the data inserted here will be automatically removed
		// (which can make it tricky to debug when you need to see the data).

		// Set the catalog version in the session
		final CatalogVersionService catalogVersionService = Registry.getCoreApplicationContext().getBean("catalogVersionService", CatalogVersionService.class);
		catalogVersionService.setSessionCatalogVersion("Default", "Staged");

		// Import test data
		final FrameitCoreSystemSetup setup = Registry.getCoreApplicationContext().getBean("frameitCoreSystemSetup", FrameitCoreSystemSetup.class);
		if (!setup.importPictureFrames(true)) {
			LOG.warn("Import of test data seems to have failed!!");
		}
	}

	@After
	public void tearDown() {
		// implement here code executed after each test
	}

	/**
	 * Test the {@link PictureFrameService#getPictureFrameByCode(String)} method. Also serves to test the impex data -
	 * i.e., this is the method that asserts that the data retrieved is what we expect from the import.
	 */
	@Test
	public void testGetPictureFrameByCode() {
		PictureFrameModel model = pictureFrameService.getPictureFrameByCode("111");
		// Data from pictureFrames.impex
		Assert.assertEquals("Code was not as expected!", "111", model.getCode());
		Assert.assertEquals("EAN was not as expected!", "111", model.getEan());
		Assert.assertEquals("Width was not as expected!", new Integer(100), model.getWidth());
		Assert.assertEquals("Height was not as expected!", new Integer(100), model.getHeight());
		Assert.assertEquals("Name was not as expected!", "Test frame 1", model.getName());
		Assert.assertEquals("Border width was not as expected!", new Integer(10), model.getBorderWidth());
		// Unit
		Assert.assertNotNull("Unit is missing!", model.getUnit());
		Assert.assertEquals("Unit is not as expected!", "pieces", model.getUnit().getCode());
		// Dynamic attributes
		Assert.assertEquals("Code was not as expected!", "100x100cms", model.getDimensions());
		Assert.assertEquals("Code was not as expected!", "90x90cms", model.getDisplayArea());
		// Categories (from categories.impex)
		Assert.assertNotNull("Categories are missing!", model.getSupercategories());
		Assert.assertEquals("Categories were not as expected", "cat1", model.getSupercategories().iterator().next().getCode());
		// Prices (from priceRows.impex)
		Assert.assertNotNull("Price is missing!", model.getEurope1Prices());
		Assert.assertEquals("Price is not as expected!", new Double(150.00), model.getEurope1Prices().iterator().next().getPrice());
		// Images
		Assert.assertNotNull("Picture is missing!", model.getPicture());
		Assert.assertEquals("Picture is not as expected!", "frame1-200x200", model.getPicture().getCode());
		Assert.assertNotNull("Thumbnail is missing!", model.getThumbnail());
		Assert.assertEquals("Thumbnail is not as expected!", "frame1-50x50", model.getThumbnail().getCode());

		model = pictureFrameService.getPictureFrameByCode("333");
		Assert.assertEquals("Code was not as expected!", "333", model.getCode());
		Assert.assertEquals("EAN was not as expected!", "333", model.getEan());
		Assert.assertEquals("Width was not as expected!", new Integer(30), model.getWidth());
		Assert.assertEquals("Height was not as expected!", new Integer(20), model.getHeight());
		Assert.assertEquals("Name was not as expected!", "Test frame 3", model.getName());
		Assert.assertEquals("Border width was not as expected!", new Integer(2), model.getBorderWidth());
		// Unit
		Assert.assertNotNull("Unit is missing!", model.getUnit());
		Assert.assertEquals("Unit is not as expected!", "pieces", model.getUnit().getCode());
		// Dynamic attributes
		Assert.assertEquals("Code was not as expected!", "30x20cms", model.getDimensions());
		Assert.assertEquals("Code was not as expected!", "28x18cms", model.getDisplayArea());
		// Categories (from categories.impex)
		Assert.assertNotNull("Categories are missing!", model.getSupercategories());
		Assert.assertEquals("Categories were not as expected", "cat1", model.getSupercategories().iterator().next().getCode());
		// Prices (from priceRows.impex)
		Assert.assertNotNull("Price is missing!", model.getEurope1Prices());
		Assert.assertEquals("Price is not as expected!", new Double(350.00), model.getEurope1Prices().iterator().next().getPrice());
		// Images
		Assert.assertNotNull("Picture is missing!", model.getPicture());
		Assert.assertEquals("Picture is not as expected!", "frame3-200x200", model.getPicture().getCode());
		Assert.assertNotNull("Thumbnail is missing!", model.getThumbnail());
		Assert.assertEquals("Thumbnail is not as expected!", "frame3-50x50", model.getThumbnail().getCode());
	}

	@Test
	public void testGetPictureFramesByDimensions() {
		final List<PictureFrameModel> models = pictureFrameService.getPictureFramesByDimensions(new Integer(100), new Integer(100));
		Assert.assertEquals("Number of picture frames retrieved was not as expected!", 1, models.size());
	}

	@Test
	public void testGetAllPictureFrames() {
		final List<PictureFrameModel> models = pictureFrameService.getAllPictureFrames();
		Assert.assertEquals("Number of picture frames retrieved was not as expected!", 3, models.size());
	}
}
