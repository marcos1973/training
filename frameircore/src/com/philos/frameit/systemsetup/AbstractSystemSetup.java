package com.philos.frameit.systemsetup;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;

import java.util.List;

import org.apache.log4j.Logger;

public abstract class AbstractSystemSetup {
	private final static Logger LOG = Logger.getLogger(AbstractSystemSetup.class);

	private ImportService importService;

	/**
	 * Placeholder for create essential data for the extension - needs annotations in implementing class.
	 */
	public abstract void createEssentialData();

	/**
	 * Placeholder for create project data for the extension - needs annontations in implementing class.
	 * 
	 * @param context the context.
	 */
	public abstract void createProjectData(SystemSetupContext context);

	/**
	 * Placeholder for getting the project data parameter/option list - needs annotations in implementing class.
	 * 
	 * @return a list of project data options/parameters
	 */
	public abstract List<SystemSetupParameter> getSystemSetupParameters();

	/**
	 * Utility method to import a file. Wraps {@link #importData(String, boolean)} with the 'removeOnSuccess' parameter
	 * set to 'true'.
	 * 
	 * @param file the file to import (must be in the classpath of the app, i.e., somewhere under the /resources folder
	 *            of the extension)
	 * @return true if the import was successful, false otherwise.
	 */
	public boolean importData(final String file) {
		return importData(file, true);
	}

	/**
	 * Utility method to import a file. Creates the import configuration, and gets the input stream for the file
	 * specified via the classloader's {@link Class#getResourceAsStream(String)} method.
	 * 
	 * @param file the file to import (must be in the classpath of the app, i.e., somewhere under the /resources folder
	 *            of the extension)
	 * @param removeOnSuccess remove the import cronjob from the database on success or not.
	 * @return true if the import was successful, false otherwise
	 */
	public boolean importData(final String file, final boolean removeOnSuccess) {
		LOG.info("Importing file " + file + "...");

		final ImportConfig config = new ImportConfig();
		// Switch on legacy mode
		config.setLegacyMode(Boolean.TRUE);
		config.setRemoveOnSuccess(removeOnSuccess);
		config.setScript(new StreamBasedImpExResource(getClass().getResourceAsStream(file), "UTF8"));

		final ImportResult result = importService.importData(config);

		return result.isFinished() && result.isSuccessful();
	}

	/**
	 * Getter for the import service.
	 * 
	 * @return the import service
	 */
	public ImportService getImportService() {
		return importService;
	}

	/**
	 * Setter for the import service.
	 * 
	 * @param importService the import service
	 */
	public void setImportService(final ImportService importService) {
		this.importService = importService;
	}
}
