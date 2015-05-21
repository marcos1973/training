package com.philos.frameit.services;

/**
 * Service that handles creating synchronization jobs.
 */
public interface SyncJobService {

	/**
	 * Ensure that a product catalog sync job exists for the specified catalog
	 * id. The sync job is created between the Staged and Online catalog
	 * versions only if there is no existing sync job.
	 * 
	 * @param catalogId
	 *            the catalog id to search sync job for.
	 */
	void createProductCatalogSyncJob(String catalogId);

	/**
	 * Ensure that a cms content catalog sync job exists for the specified
	 * catalog id. The sync job is created between the Staged and Online catalog
	 * versions only if there is no existing sync job.
	 * 
	 * @param catalogId
	 *            the catalog id
	 */
	void createContentCatalogSyncJob(String catalogId);

	/**
	 * Run the catalog sync for the specified catalog.
	 * 
	 * @param catalogId
	 *            the catalog id
	 */
	void executeCatalogSyncJob(String catalogId);
}
