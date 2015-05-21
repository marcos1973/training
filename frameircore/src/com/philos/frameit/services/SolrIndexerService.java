package com.philos.frameit.services;


/**
 * Service to set up solr indexes.
 */
public interface SolrIndexerService {
	/**
	 * Create the update and rebuild solr index cron jobs for the specified solrFacetSearchConfigName.
	 * 
	 * @param solrFacetSearchConfigName the solrFacetSearchConfigName
	 */
	void createSolrIndexerCronJobs(String solrFacetSearchConfigName);

	/**
	 * Run a solr indexer cron job.
	 * 
	 * @param solrFacetSearchConfigName the solrFacetSearchConfigName
	 * @param fullReIndex true to rebuild the index, false to update it
	 */
	void executeSolrIndexerCronJob(String solrFacetSearchConfigName, boolean fullReIndex);

	/**
	 * Activate the solr index cron jobs for the specified solrFacetSearchConfigName
	 * 
	 * @param solrFacetSearchConfigName the solrFacetSearchConfigName
	 */
	void activateSolrIndexerCronJobs(String solrFacetSearchConfigName);
}
