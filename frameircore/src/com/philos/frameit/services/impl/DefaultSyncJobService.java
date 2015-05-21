package com.philos.frameit.services.impl;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.philos.frameit.services.SyncJobService;

/**
 * Default implementation for {@link SyncJobService}
 */
@SuppressWarnings("deprecation")
public class DefaultSyncJobService implements SyncJobService {
	private static final Logger LOG = Logger.getLogger(DefaultSyncJobService.class);

	/**
	 * Creates a sync job for the catalog specified by the given ID. Instances ('executions') of this sync job will be
	 * used to perform the sync. Job names are configured as 'sync' + catalogId + ":Staged->Online".
	 * 
	 * @param catalogId the catalog ID
	 * @see com.philos.frameit.services.SyncJobService#createProductCatalogSyncJob(java.lang.String)
	 */
	@Override
	public void createProductCatalogSyncJob(final String catalogId) {
		// Check if the sync job already exists
		if (getCatalogSyncJob(catalogId) == null) {
			LOG.info("Creating product sync item job for [" + catalogId + "]");

			// Lookup the catalog name
			final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);

			// Create the sync job name
			final String jobName = "sync " + catalogId + ":" + CatalogManager.OFFLINE_VERSION + "->" + CatalogManager.ONLINE_VERSION;

			final SyncItemJob syncItemJob = CatalogManager.getInstance().configureSynchronizationJob(jobName, catalog, CatalogManager.OFFLINE_VERSION,
					CatalogManager.ONLINE_VERSION, true, false);

			// These attributes are not in the extensions we include so we can remove this, but
			// this is how it is done. I'm not sure what sequenceId is but product reviews would
			// be removed as they are created directly in the Online catalog and therefore do not
			// need to be synced.
			// removeAttributeFromSync(syncItemJob, Product.class, ProductModel.SEQUENCEID);
			// removeAttributeFromSync(syncItemJob, Product.class, ProductModel.PRODUCTREVIEWS);
			// removeAttributeFromSync(syncItemJob, PriceRow.class, PriceRowModel.SEQUENCEID);

			LOG.info("Created product sync item job [" + syncItemJob.getCode() + "]");
		}
	}

	/**
	 * Create a sync job for the content catalog specified by ID. Instances ('executions') of this sync job will be used
	 * to execute the sync. Job names are configured as 'sync' + catalogId + ":Staged->Online".
	 * 
	 * @param catalogId the ID of the catalog to create the sync job for.
	 * @see SyncJobService#createContentCatalogSyncJob(java.lang.String)
	 */
	@Override
	public void createContentCatalogSyncJob(final String catalogId) {
		// Check if the sync job already exists
		if (getCatalogSyncJob(catalogId) == null) {
			LOG.info("Creating content sync item job for [" + catalogId + "]");

			// Lookup the catalog name
			final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);

			// Create the sync job name
			final String jobName = "sync " + catalogId + ":" + CatalogManager.OFFLINE_VERSION + "->" + CatalogManager.ONLINE_VERSION;

			final SyncItemJob syncItemJob = CatalogManager.getInstance().configureSynchronizationJob(jobName, catalog, CatalogManager.OFFLINE_VERSION,
					CatalogManager.ONLINE_VERSION, true, true);

			syncItemJob.setRootTypes(JaloSession.getCurrentSession().getSessionContext(), getContentSyncRootTypes());

			final ComposedType cmsItemType = TypeManager.getInstance().getComposedType("CMSItem");

			final Collection<SyncAttributeDescriptorConfig> syncAttributeConfigurations = syncItemJob.getSyncAttributeConfigurations();
			for (final SyncAttributeDescriptorConfig syncAttributeDescriptorConfig : syncAttributeConfigurations) {
				final Type attributeType = syncAttributeDescriptorConfig.getAttributeDescriptor().getAttributeType();
				if (syncAttributeDescriptorConfig.getAttributeDescriptor().getEnclosingType().isAssignableFrom(cmsItemType)
						&& cmsItemType.isAssignableFrom(attributeType)
						|| (attributeType instanceof CollectionType && cmsItemType.isAssignableFrom(((CollectionType) attributeType).getElementType()))) {
					syncAttributeDescriptorConfig.setCopyByValue(true);
				}
			}

			LOG.info("Created content sync item job [" + syncItemJob.getCode() + "]");
		}
	}

	/**
	 * Execute the sync job. Create a new instance (execution) of the sync job and run it.
	 * 
	 * @param catalogId the ID of the catalog to sync.
	 * @see SyncJobService# executeCatalogSyncJob(String)
	 */
	@Override
	public void executeCatalogSyncJob(final String catalogId) {
		final SyncItemJob catalogSyncJob = getCatalogSyncJob(catalogId);
		if (catalogSyncJob == null) {
			LOG.error("Couldn't find 'SyncItemJob' for catalog [" + catalogId + "]", null);
		} else {
			final SyncItemCronJob syncJob = catalogSyncJob.newExecution();
			syncJob.setLogToDatabase(false);
			syncJob.setLogToFile(false);
			syncJob.setForceUpdate(false);

			LOG.info("Created cronjob [" + syncJob.getCode() + "] to synchronize catalog [" + catalogId + "] staged to online version.");
			LOG.info("Configuring full version sync");

			catalogSyncJob.configureFullVersionSync(syncJob);

			LOG.info("Starting synchronization, this may take a while ...");

			catalogSyncJob.perform(syncJob, true);

			LOG.info("Synchronization complete for catalog [" + catalogId + "]");
		}
	}

	/**
	 * Removes an attribute from synchronization
	 * 
	 * @param syncJob the synchronization job
	 * @param clazz the class
	 * @param attribute the attribute, that should be removed
	 */
	protected void removeAttributeFromSync(final SyncItemJob syncJob, final Class clazz, final String attribute) {
		final ComposedType composedType = TypeManager.getInstance().getComposedType(clazz);
		final AttributeDescriptor attributeDesc = composedType.getDeclaredAttributeDescriptor(attribute);
		final SyncAttributeDescriptorConfig cfg = syncJob.getConfigFor(attributeDesc, true);
		if (cfg != null && Boolean.TRUE.equals(cfg.isIncludedInSync())) {
			LOG.info("Removing [" + composedType.getCode() + "] attribute [" + attributeDesc.getQualifier() + "] from sync job [" + syncJob.getCode() + "]");
			cfg.setIncludedInSync(false);
		}
	}

	/**
	 * Utility method to get a sync job for the given catalog ID. Returns null if it has not yet been created.
	 * 
	 * @param catalogId the catalog ID
	 * @return the sync job (or null if the catalog and/or the Staged/Online versions could not be found.
	 */
	protected SyncItemJob getCatalogSyncJob(final String catalogId) {
		// Lookup the catalog name
		final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);
		if (catalog != null) {
			final CatalogVersion source = catalog.getCatalogVersion(CatalogManager.OFFLINE_VERSION);
			final CatalogVersion target = catalog.getCatalogVersion(CatalogManager.ONLINE_VERSION);

			if (source != null && target != null) {
				return CatalogManager.getInstance().getSyncJob(source, target);
			}
		}
		return null;
	}

	/**
	 * The sync job root types for content catalogs are different to the product catalogs. The base types CMSItem,
	 * CMSRelation, Media and MediaContainer should be added.
	 * 
	 * @return the list of composed types to be used as the root types of the sync job.
	 */
	protected List<ComposedType> getContentSyncRootTypes() {
		final TypeManager typeManager = TypeManager.getInstance();

		final List<ComposedType> rootTypes = new ArrayList<ComposedType>();

		rootTypes.add(typeManager.getComposedType("CMSItem"));
		rootTypes.add(typeManager.getComposedType("CMSRelation"));
		rootTypes.add(typeManager.getComposedType("Media"));
		rootTypes.add(typeManager.getComposedType("MediaContainer"));

		// Optionally add the BTGItem if BTG is loaded
		try {
			rootTypes.add(typeManager.getComposedType("BTGItem"));
		} catch (final JaloItemNotFoundException ignore) {
			// ignore
		}

		return rootTypes;
	}
}
