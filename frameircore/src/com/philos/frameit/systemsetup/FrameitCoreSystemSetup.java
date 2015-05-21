package com.philos.frameit.systemsetup;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImportService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.philos.frameit.constants.FrameitcoreConstants;
import com.philos.frameit.services.SolrIndexerService;
import com.philos.frameit.services.SyncJobService;

@SystemSetup(extension = FrameitcoreConstants.EXTENSIONNAME, process = Process.ALL, type = Type.ALL)
public class FrameitCoreSystemSetup extends AbstractSystemSetup {
	private final Logger LOG = Logger.getLogger(FrameitCoreSystemSetup.class);

	@Autowired
	private SyncJobService syncJobService;
	@Autowired
	private SolrIndexerService solrIndexerService;

	/**
	 * Create the essential data for the extension. Generally speaking, put as little in here as possible, create
	 * specific project data options/parameters instead (it's more flexible).
	 */
	@Override
	@SystemSetup(extension = FrameitcoreConstants.EXTENSIONNAME, process = Process.ALL, type = Type.ESSENTIAL)
	public void createEssentialData() {
		// Import essential data
		LOG.info("Importing essential data...");

		super.importData("/essentialData/countries.impex");

		LOG.info("Essential data import complete.");
	}

	/**
	 * Create the project data for the extension. We examine the parameters passed in the context parameter to see what
	 * options were selected for import.
	 * 
	 * @param context the system set-up context object
	 */
	@Override
	@SystemSetup(extension = FrameitcoreConstants.EXTENSIONNAME, process = Process.ALL, type = Type.PROJECT)
	public void createProjectData(final SystemSetupContext context) {
		// Import project data
		// Tax data
		final String taxesOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_taxes");
		if (taxesOption != null) {
			LOG.info("Import taxes option selected: " + taxesOption);
			// Import data based on the value of the option...
			if ("True".equals(taxesOption)) {
				LOG.info("Import completed successfully: " + importTaxData());
			}
		}

		// Discount data
		final String discountsOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_discounts");
		if (discountsOption != null) {
			LOG.info("Import discounts option selected: " + discountsOption);
			// Import data based on the value of the option...
			if ("True".equals(discountsOption)) {
				LOG.info("Import completed successfully: " + importDiscountData());
			}
		}

		// Picture frame data
		final String pictureFramesOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_picture_frames");
		if (pictureFramesOption != null) {
			LOG.info("Import product data option selected: " + pictureFramesOption);
			// Import data based on the value of the option...
			if ("All".equals(pictureFramesOption)) {
				LOG.info("Import completed successfully: " + importPictureFrames(false));
			} else if ("Test".equals(pictureFramesOption)) {
				LOG.info("Import completed successfully: " + importPictureFrames(true));
			}
		}

		// Users
		final String usersOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_users");
		if (usersOption != null) {
			LOG.info("Import user data option selected: " + usersOption);
			// Import data based on the value of the option...
			if ("All".equals(usersOption)) {
				LOG.info("Import completed successfully: " + importUsers(false));
			} else if ("Test".equals(usersOption)) {
				LOG.info("Import completed successfully: " + importUsers(true));
			}
		}

		// Workflow data
		final String workflowOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_workflow");
		if (workflowOption != null) {
			LOG.info("Import workflow option selected: " + workflowOption);
			// Import data based on the value of the option...
			if ("True".equals(workflowOption)) {
				LOG.info("Import completed successfully: " + importWorkflowData());
			}
		}

		// Email templates data
		final String emailTemplatesOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_email");
		if (emailTemplatesOption != null) {
			LOG.info("Import email templates option selected: " + emailTemplatesOption);
			// Import data based on the value of the option...
			if ("True".equals(emailTemplatesOption)) {
				LOG.info("Import completed successfully: " + importEmailTemplatesData());
			}
		}

		// Product sync job
		final String productSyncOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_product_sync");
		if (productSyncOption != null) {
			LOG.info("Sync product catalog option selected: " + productSyncOption);
			// Import data based on the value of the option...
			if ("Create only".equals(productSyncOption) || "Create and execute".equals(productSyncOption)) {
				LOG.info("Product Catalog SyncJob created successfully: " + createProductCatalogSyncJob());
			}
			if ("Create and execute".equals(productSyncOption) || "Execute only".equals(productSyncOption)) {
				LOG.info("Product Catalog SyncJob executed successfully: " + executeProductSyncJob());
			}
		}

		// Solr configuration
		final String solrConfigOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_solr_config");
		if (solrConfigOption != null) {
			LOG.info("Import solr config option selected: " + solrConfigOption);
			// Import data based on the value of the option...
			if ("True".equals(solrConfigOption)) {
				LOG.info("Import completed successfully: " + importSolrConfigData());
			}
		}

		// CMS data
		final String cmsOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_cms");
		if (cmsOption != null) {
			LOG.info("Import CMS data option selected: " + cmsOption);
			// Import data based on the value of the option...
			if ("True".equals(cmsOption)) {
				LOG.info("Import completed successfully: " + importCMSData());
			}
		}

		// CMS sync job
		final String cmsSyncOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_cms_sync");
		if (cmsSyncOption != null) {
			LOG.info("Sync CMS catalog option selected: " + cmsSyncOption);
			// Import data based on the value of the option...
			if ("Create only".equals(cmsSyncOption) || "Create and execute".equals(cmsSyncOption)) {
				LOG.info("CMS Catalog SyncJob created successfully: " + createCMSCatalogSyncJob());
			}
			if ("Create and execute".equals(cmsSyncOption) || "Execute only".equals(cmsSyncOption)) {
				LOG.info("CMS Catalog SyncJob executed successfully: " + executeCMSSyncJob());
			}
		}

		// Solr indexer job
		final String solrOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_solr_jobs");
		if (solrOption != null) {
			LOG.info("Solr indexing option selected: " + solrOption);
			// Import data based on the value of the option...
			if ("Create only".equals(solrOption) || "Create and execute".equals(solrOption)) {
				LOG.info("Solr indexer jobs created successfully: " + createSolrIndexerJob());
			}
			if ("Create and execute".equals(solrOption) || "Execute only".equals(solrOption)) {
				LOG.info("Solr indexer jobs executed successfully: " + executeSolrIndexerJob());
			}
		}

		// CMS data
		final String orderUpdatesJobOption = context.getParameter(FrameitcoreConstants.EXTENSIONNAME + "_order_updates_job");
		if (orderUpdatesJobOption != null) {
			LOG.info("Import order updates job option selected: " + orderUpdatesJobOption);
			// Import data based on the value of the option...
			if ("True".equals(orderUpdatesJobOption)) {
				LOG.info("Import completed successfully: " + importOrderUpdatesJob());
			}
		}
	}

	/**
	 * Create the list of options ({@link SystemSetupParameter} objects) for the project data import.
	 * 
	 * @return a list of {@link SystemSetupParameter} objects representing the project data import options.
	 */
	@Override
	@SystemSetupParameterMethod(extension = FrameitcoreConstants.EXTENSIONNAME)
	public List<SystemSetupParameter> getSystemSetupParameters() {
		final SystemSetupParameter parameter1 = new SystemSetupParameter("discounts");
		parameter1.setLabel("Discounts:");
		parameter1.addValue("True", true);
		parameter1.addValue("False");

		final SystemSetupParameter parameter2 = new SystemSetupParameter("taxes");
		parameter2.setLabel("Taxes:");
		parameter2.addValue("True", true);
		parameter2.addValue("False");

		final SystemSetupParameter parameter3 = new SystemSetupParameter("picture_frames");
		parameter3.setLabel("Import picture frame data:");
		parameter3.addValue("All");
		parameter3.addValue("Test", true);
		parameter3.addValue("None");

		final SystemSetupParameter parameter4 = new SystemSetupParameter("users");
		parameter4.setLabel("Import user data:");
		parameter4.addValue("All");
		parameter4.addValue("Test", true);
		parameter4.addValue("None");

		final SystemSetupParameter parameter5 = new SystemSetupParameter("workflow");
		parameter5.setLabel("Import workflow data:");
		parameter5.addValue("True", true);
		parameter5.addValue("False");

		final SystemSetupParameter parameter6 = new SystemSetupParameter("email");
		parameter6.setLabel("Import email template data:");
		parameter6.addValue("True", true);
		parameter6.addValue("False");

		final SystemSetupParameter parameter7 = new SystemSetupParameter("product_sync");
		parameter7.setLabel("Product Catalog Sync Job:");
		parameter7.addValue("None");
		parameter7.addValue("Create only");
		parameter7.addValue("Create and execute", true);
		parameter7.addValue("Execute only");

		final SystemSetupParameter parameter8 = new SystemSetupParameter("solr_config");
		parameter8.setLabel("Import Solr configuration data:");
		parameter8.addValue("True", true);
		parameter8.addValue("False");

		final SystemSetupParameter parameter9 = new SystemSetupParameter("cms");
		parameter9.setLabel("Import CMS data:");
		parameter9.addValue("True", true);
		parameter9.addValue("False");

		final SystemSetupParameter parameter10 = new SystemSetupParameter("cms_sync");
		parameter10.setLabel("CMS Catalog Sync Job:");
		parameter10.addValue("None");
		parameter10.addValue("Create only");
		parameter10.addValue("Create and execute", true);
		parameter10.addValue("Execute only");

		final SystemSetupParameter parameter11 = new SystemSetupParameter("solr_jobs");
		parameter11.setLabel("Solr indexer jobs:");
		parameter11.addValue("None");
		parameter11.addValue("Create only");
		parameter11.addValue("Create and execute", true);
		parameter11.addValue("Execute only");

		final SystemSetupParameter parameter12 = new SystemSetupParameter("order_updates_job");
		parameter12.setLabel("Incoming order updates job:");
		parameter12.addValue("True", true);
		parameter12.addValue("False");

		final List<SystemSetupParameter> parameters = new ArrayList<SystemSetupParameter>();
		parameters.add(parameter1);
		parameters.add(parameter2);
		parameters.add(parameter3);
		parameters.add(parameter4);
		parameters.add(parameter5);
		parameters.add(parameter6);
		parameters.add(parameter7);
		parameters.add(parameter8);
		parameters.add(parameter9);
		parameters.add(parameter10);
		parameters.add(parameter11);
		parameters.add(parameter12);

		return parameters;
	}

	/**
	 * ImpEx the taxes data.
	 * 
	 * @return the result of the imports
	 */
	public boolean importTaxData() {
		return importData("/tax/taxSetup.impex");
	}

	/**
	 * ImpEx the discount data.
	 * 
	 * @return the result of the imports
	 */
	public boolean importDiscountData() {
		return importData("/discounts/discountSetup.impex");
	}

	/**
	 * Utility method to import the picture frame data with the {@link ImportService}. The 'test' parameter is used to
	 * determine the path to the data, and to use an import configuration with 'removeOnSuccess' set to 'false'.
	 * 
	 * @param test Import the test data or the production data?
	 * @return true if the import was successful, false otherwise.
	 */
	public boolean importPictureFrames(final boolean test) {
		if (test) {
			return importData("/test/pictureFrameData/categories.impex", false) && //
					importData("/test/pictureFrameData/pictureFrameImages.impex", false) && //
					importData("/test/pictureFrameData/pictureFrames.impex", false) && //
					importData("/test/pictureFrameData/priceRows.impex", false);
		} else {
			return importData("/pictureFrameData/categories.impex") && //
					importData("/pictureFrameData/pictureFrameImages.impex", false) && //
					importData("/pictureFrameData/pictureFrames.impex") && //
					importData("/pictureFrameData/priceRows.impex");
		}
	}

	/**
	 * Utility method to import the user data with the {@link ImportService}. The 'test' parameter is used to determine
	 * the path to the data, and to use an import configuration with 'removeOnSuccess' set to 'false'.
	 * 
	 * @param test Import the test data or the production data?
	 * @return true if the import was successful, false otherwise.
	 */
	public boolean importUsers(final boolean test) {
		if (test) {
			return importData("/test/userData/users.impex", false);
		} else {
			return importData("/userData/users.impex");
		}
	}

	/**
	 * Create the product catalog sync job.
	 * 
	 * @return true if the job was created successfully, false if an exception was thrown creating the job.
	 */
	private boolean createProductCatalogSyncJob() {
		try {
			getSyncJobService().createProductCatalogSyncJob("Default");
			return true;
		} catch (final SystemException e) {
			LOG.error("An exception was thrown trying to create the sync job for the product content catalog 'Default'!", e);
			return false;
		}
	}

	/**
	 * Execute the CMS catalog sync.
	 * 
	 * @return true if it executed successfully, false if an exception was thrown.
	 */
	private boolean executeProductSyncJob() {
		try {
			getSyncJobService().executeCatalogSyncJob("Default");
			return true;
		} catch (final SystemException e) {
			LOG.error("An exception was thrown trying to sync the product content catalog 'Default'!", e);
			return false;
		}
	}

	/**
	 * Create the CMS catalog sync job.
	 * 
	 * @return true if the job was created successfully, false if an exception was thrown creating the job.
	 */
	private boolean createCMSCatalogSyncJob() {
		try {
			getSyncJobService().createContentCatalogSyncJob("frameit_content");
			return true;
		} catch (final SystemException e) {
			LOG.error("An exception was thrown trying to create the sync job for the CMS content catalog 'frameit_content'!", e);
			return false;
		}
	}

	/**
	 * Execute the CMS catalog sync.
	 * 
	 * @return true if it executed successfully, false if an exception was thrown.
	 */
	private boolean executeCMSSyncJob() {
		try {
			getSyncJobService().executeCatalogSyncJob("frameit_content");
			return true;
		} catch (final SystemException e) {
			LOG.error("An exception was thrown trying to sync the CMS content catalog 'frameit_content'!", e);
			return false;
		}
	}

	/**
	 * ImpEx the solr configuration data.
	 * 
	 * @return the result of the imports
	 */
	private boolean importSolrConfigData() {
		return importData("/solr/solr.impex");
	}

	/**
	 * Create the solr indexer cron-jobs
	 * 
	 * @return true if the jobs were created successfully, false otherwise
	 */
	private boolean createSolrIndexerJob() {
		try {
			// Create the jobs
			getSolrIndexerService().createSolrIndexerCronJobs("solrFacetSearchConfig");
			// Import triggers for them
			if (!importData("/solr/solrTriggers.impex")) {
				LOG.error("Could not import the cron-job triggers!");
				return false;
			}
			// Activate the triggers
			getSolrIndexerService().activateSolrIndexerCronJobs("solrFacetSearchConfig");
			return true;
		} catch (final SystemException e) {
			return false;
		}
	}

	/**
	 * Execute the full solr indexer job.
	 * 
	 * @return the result of the indexer job
	 */
	private boolean executeSolrIndexerJob() {
		try {
			getSolrIndexerService().executeSolrIndexerCronJob("solrFacetSearchConfig", true);
			return true;
		} catch (final SystemException e) {
			return false;
		}
	}

	/**
	 * Utility method to import the CMS data with the {@link ImportService}.
	 * 
	 * @return true if the import was successful, false otherwise.
	 */
	public boolean importCMSData() {
		return importData("/cmsData/siteSetup.impex") && //
				importData("/cmsData/components.impex") && //
				importData("/cmsData/standardPageTemplate.impex") && //
				importData("/cmsData/categoryPageTemplate.impex") && //
				importData("/cmsData/loginPageTemplate.impex") && //
				importData("/cmsData/cartPageTemplate.impex") && //
				importData("/cmsData/checkoutPageTemplate.impex") && //
				importData("/cmsData/searchResultsPageTemplate.impex") && //
				importData("/cmsData/myAccountPageTemplate.impex") && //
				importData("/cmsData/productPageTemplate.impex") && //
				importData("/cmsData/homePage.impex") && //
				importData("/cmsData/categoryPage.impex") && //
				importData("/cmsData/loginPage.impex") && //
				importData("/cmsData/cartPage.impex") && //
				importData("/cmsData/checkoutAddressPage.impex") && //
				importData("/cmsData/checkoutPaymentPage.impex") && //
				importData("/cmsData/checkoutSummaryPage.impex") && //
				importData("/cmsData/checkoutConfirmationPage.impex") && //
				importData("/cmsData/searchResultsPage.impex") && //
				importData("/cmsData/noSearchResultsPage.impex") && //
				importData("/cmsData/myAccountPage.impex") && //
				importData("/cmsData/myAccountProfilePage.impex") && //
				importData("/cmsData/orderHistoryPage.impex") && //
				importData("/cmsData/orderDetailsPage.impex") && //
				importData("/cmsData/productPage.impex") && //
				importData("/cmsData/myAccountAddressBookPage.impex") && //
				importData("/cmsData/wishlistPage.impex") && //
				importData("/cmsData/cmscockpit-users.impex") && //
				importData("/cmsData/cmscockpit-access-rights.impex");
	}

	/**
	 * Utility method to import the workflow data with the {@link ImportService}.
	 * 
	 * @return true if the import was successful, false otherwise.
	 */
	private boolean importWorkflowData() {
		return importData("/workflow/reviewWorkflow.impex");
	}

	/**
	 * Utility method to import the email template data with the {@link ImportService}.
	 * 
	 * @return true if the import was successful, false otherwise.
	 */
	private boolean importEmailTemplatesData() {
		return importData("/email/emailTemplates.impex");
	}

	/**
	 * Import a cron job and trigger to process incoming order update files.
	 * 
	 * @return the result of the imports (true or false)
	 */
	private boolean importOrderUpdatesJob() {
		return importData("/cronJobs/orderUpdateCronJob.impex") && //
				importData("/cronJobs/orderUpdateCronJobTrigger.impex");
	}

	/**
	 * Getter for the sync job service.
	 * 
	 * @return the sync job service.
	 */
	public SyncJobService getSyncJobService() {
		if (syncJobService == null) {
			syncJobService = Registry.getCoreApplicationContext().getBean("syncJobService", SyncJobService.class);
		}
		return syncJobService;
	}

	/**
	 * Setter for the sync job service.
	 * 
	 * @param syncJobService the sync job service.
	 */
	public void setSyncJobService(final SyncJobService syncJobService) {
		this.syncJobService = syncJobService;
	}

	/**
	 * Getter for the solr indexer service.
	 * 
	 * @return the solr indexer service.
	 */
	public SolrIndexerService getSolrIndexerService() {
		if (solrIndexerService == null) {
			solrIndexerService = Registry.getCoreApplicationContext().getBean("solrIndexerService", SolrIndexerService.class);
		}
		return solrIndexerService;
	}

	/**
	 * Setter for the solr indexer service.
	 * 
	 * @param solrIndexerService the solr indexer service.
	 */
	public void setSolrIndexerService(final SolrIndexerService solrIndexerService) {
		this.solrIndexerService = solrIndexerService;
	}
}
