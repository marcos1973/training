package com.philos.frameit.services.impl;

import de.hybris.platform.cronjob.jalo.Trigger;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.jalo.SolrfacetsearchManager;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerCronJob;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.philos.frameit.services.SolrIndexerService;

/**
 * Default implementation of {@link SolrIndexerService}.
 */
@SuppressWarnings("deprecation")
public class DefaultSolrIndexerService implements SolrIndexerService {
	private static final Logger LOG = Logger.getLogger(DefaultSolrIndexerService.class);

	private CronJobService cronJobService;
	private ModelService modelService;

	/**
	 * Create the full and update cron-jobs for the solr facet search config specified by name.
	 * 
	 * @param solrFacetSearchConfigName the name of the solr facet search config object
	 * @see com.philos.frameit.services.SolrIndexerService#createSolrIndexerCronJobs(java.lang.String)
	 */
	@Override
	public void createSolrIndexerCronJobs(final String solrFacetSearchConfigName) {
		final SolrFacetSearchConfig solrFacetConfig = getSolrFacetSearchConfigForName(solrFacetSearchConfigName);
		if (solrFacetConfig != null) {
			getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.FULL);
			getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.UPDATE);
		}
	}

	/**
	 * Execute either the full cron-job or the update cron-job (depending on the value of the fullReIndex parameter).
	 * 
	 * @param solrFacetSearchConfigName the name of the solr facet search config object
	 * @param fullReIndex full re-index (true) or update
	 * @see com.philos.frameit.services.SolrIndexerService#executeSolrIndexerCronJob(java.lang.String, boolean)
	 */
	@Override
	public void executeSolrIndexerCronJob(final String solrFacetSearchConfigName, final boolean fullReIndex) {
		final SolrFacetSearchConfig solrFacetConfig = getSolrFacetSearchConfigForName(solrFacetSearchConfigName);
		if (solrFacetConfig != null) {
			executeSolrIndexerCronJob(solrFacetConfig, fullReIndex ? IndexerOperationValues.FULL : IndexerOperationValues.UPDATE);
		}
	}

	/**
	 * Activate the triggers for the indexer cron-jobs.
	 * 
	 * @param solrFacetSearchConfigName the name of the solr facet search config object
	 * @see com.philos.frameit.services.SolrIndexerService#activateSolrIndexerCronJobs(java.lang.String)
	 */
	@Override
	public void activateSolrIndexerCronJobs(final String solrFacetSearchConfigName) {
		final SolrFacetSearchConfig solrFacetConfig = getSolrFacetSearchConfigForName(solrFacetSearchConfigName);
		if (solrFacetConfig != null) {
			activateSolrIndexerCronJobs(getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.FULL));
			activateSolrIndexerCronJobs(getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.UPDATE));
		}
	}

	/**
	 * Utility method to look up a solr facet search config object by its name.
	 * 
	 * @param solrFacetSearchConfigName the name of the solr facet search config object.
	 * @return the solr facet search config object.
	 */
	public SolrFacetSearchConfig getSolrFacetSearchConfigForName(final String solrFacetSearchConfigName) {
		try {
			return SolrfacetsearchManager.getInstance().getSolrFacetConfig(solrFacetSearchConfigName);
		} catch (final FlexibleSearchException ignore) {
			return null;
		}
	}

	/**
	 * Get or create solr indexer job.
	 * 
	 * @param solrFacetSearchConfig the solr facet search config
	 * @param indexerOperation the operation (full or update)
	 * @return the solr indexer cron-job
	 */
	protected SolrIndexerCronJob getSolrIndexerJob(final SolrFacetSearchConfig solrFacetSearchConfig, final IndexerOperationValues indexerOperation) {
		SolrIndexerCronJob indexerCronJob = getExistingSolrIndexerJob(solrFacetSearchConfig, indexerOperation);
		if (indexerCronJob == null) {
			indexerCronJob = createSolrIndexerJob(solrFacetSearchConfig, indexerOperation);
		}
		return indexerCronJob;
	}

	/**
	 * Get the solr indexer cron-job for the operation (full or update) if it exists, or return null.
	 * 
	 * @param solrFacetSearchConfig the solr facet search configuration object
	 * @param indexerOperation the operation (full or update)
	 * @return the solr indexer cron-job if it exists or null
	 */
	protected SolrIndexerCronJob getExistingSolrIndexerJob(final SolrFacetSearchConfig solrFacetSearchConfig, final IndexerOperationValues indexerOperation) {
		// Look to see if a cron job exists
		final String indexerCronJobName = buildSolrCronJobCode(solrFacetSearchConfig, indexerOperation);

		try {
			final CronJobModel cronJob = getCronJobService().getCronJob(indexerCronJobName);
			if (cronJob instanceof SolrIndexerCronJobModel) {
				return (SolrIndexerCronJob) getModelService().getSource(cronJob);
			}
		} catch (final UnknownIdentifierException ignore) {
			// Ignore
		} catch (final AmbiguousIdentifierException ignore) {
			// Ignore
		}
		return null;
	}

	/**
	 * Create the solr indexer cron-job for the operation (full or update).
	 * 
	 * @param solrFacetSearchConfig the solr facet search configuration
	 * @param indexerOperation the operation (full or update)
	 * @return the solr indexer cron-job
	 */
	protected SolrIndexerCronJob createSolrIndexerJob(final SolrFacetSearchConfig solrFacetSearchConfig, final IndexerOperationValues indexerOperation) {
		final String indexerCronJobName = buildSolrCronJobCode(solrFacetSearchConfig, indexerOperation);

		try {
			final EnumerationValue indexerOperationEnum = getModelService().getSource(indexerOperation);
			return SolrfacetsearchManager.getInstance().createSolrIndexerCronJob(indexerCronJobName, solrFacetSearchConfig, indexerOperationEnum);
		} catch (final JaloTypeException e) {
			throw new SystemException("Cannot create indexer job [" + indexerCronJobName + "] due to: " + e.getMessage(), e);
		}
	}

	/**
	 * Create the job code from the operation and solr facet search config name.
	 * 
	 * @param solrFacetSearchConfig the solr facet search config
	 * @param indexerOperation the operation (full or update)
	 * @return the cron-job code value
	 */
	protected String buildSolrCronJobCode(final SolrFacetSearchConfig solrFacetSearchConfig, final IndexerOperationValues indexerOperation) {
		return indexerOperation.getCode() + "-" + solrFacetSearchConfig.getName() + "-cronJob";
	}

	/**
	 * Execute the solr indexer cron-job specified by solr facet search config and the indexer operation (full or
	 * update).
	 * 
	 * @param solrFacetSearchConfig the solr facet search config
	 * @param indexerOperation the operation (full or update)
	 */
	protected void executeSolrIndexerCronJob(final SolrFacetSearchConfig solrFacetSearchConfig, final IndexerOperationValues indexerOperation) {
		final SolrIndexerCronJob solrIndexerJob = getSolrIndexerJob(solrFacetSearchConfig, indexerOperation);
		if (solrIndexerJob != null) {
			LOG.info("Starting solr index update for [" + solrFacetSearchConfig.getName() + "] ...");

			if (!solrIndexerJob.isActiveAsPrimitive()) {
				solrIndexerJob.setActive(true);
			}
			solrIndexerJob.getJob().perform(solrIndexerJob, true);

			LOG.info("Starting solr index update complete for [" + solrFacetSearchConfig.getName() + "]");
		}
	}

	/**
	 * Set the triggers and cron-job - mark them as active and set a 5 minute execution time.
	 * 
	 * @param solrIndexerJob the indexer cron-job.
	 */
	protected void activateSolrIndexerCronJobs(final SolrIndexerCronJob solrIndexerJob) {
		if (solrIndexerJob != null) {
			if (!solrIndexerJob.isActiveAsPrimitive()) {
				solrIndexerJob.setActive(true);
			}

			final List<Trigger> triggers = solrIndexerJob.getTriggers();
			if (triggers != null && !triggers.isEmpty()) {
				final Date now = new Date();
				final Date fiveMinutesTime = new Date(now.getTime() + 1000 * 60 * 5);

				for (final Trigger trigger : triggers) {
					if (!trigger.isActiveAsPrimitive()) {
						trigger.setActive(true);
						trigger.setActivationTime(fiveMinutesTime);
					}
				}
			}
		}
	}

	/**
	 * Getter for the cron-job service.
	 * 
	 * @return the cron-job service.
	 */
	protected CronJobService getCronJobService() {
		return cronJobService;
	}

	/**
	 * Setter for the cron-job service.
	 * 
	 * @param cronJobService the cron-job service.
	 */
	@Required
	public void setCronJobService(final CronJobService cronJobService) {
		this.cronJobService = cronJobService;
	}

	/**
	 * Getter for the model service.
	 * 
	 * @return the model service.
	 */
	protected ModelService getModelService() {
		return modelService;
	}

	/**
	 * Setter for the model service.
	 * 
	 * @param modelService the model service.
	 */
	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}
}
