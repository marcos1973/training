package com.philos.frameit.jobs;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.philos.frameit.model.ProcessIncomingOrderUpdatesCronJobModel;
import com.philos.frameit.order.jaxb.OrderUpdate;

public class ProcessIncomingOrderUpdatesJobPerformable extends AbstractJobPerformable<ProcessIncomingOrderUpdatesCronJobModel> {

	/**
	 * Hook method called by the hybris framework for this job.
	 * 
	 * @param cronJobModel the container cron-job model instance/object
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel)
	 */
	@Override
	public PerformResult perform(final ProcessIncomingOrderUpdatesCronJobModel cronJobModel) {
		// We could delegate this to a service to separate the business logic out from the job performable
		final List<File> orderUpdateFiles = getOrderUpdateFiles(cronJobModel.getSourceFolder());

		for (final File orderUpdateFile : orderUpdateFiles) {
			try {
				final OrderUpdate update = unmarshalOrderUpdate(orderUpdateFile);
				processOrderUpdate(update);

				// TODO rename order-update file or move it to another folder (../processed) etc..
			} catch (final JAXBException e) {
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			} catch (final UnknownOrderStatusException e) {
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Gets the order update files from the folder provided.
	 * 
	 * @param sourceFolder the source folder to look for order update files in.
	 * @return the list of files in that folder
	 */
	public List<File> getOrderUpdateFiles(final String sourceFolder) {
		final WildcardFileFilter fileFilter = new WildcardFileFilter("orderUpdate*.xml");
		final File folder = new File(sourceFolder);
		final File[] files = folder.listFiles((FileFilter) fileFilter);
		return Arrays.asList(files);
	}

	/**
	 * Process the order update data. Update the order (very simple example).
	 * 
	 * @param update the update
	 * @throws UnknownOrderStatusException if the order status
	 */
	public void processOrderUpdate(final OrderUpdate update) throws UnknownOrderStatusException {
		final OrderModel example = new OrderModel();
		example.setCode(update.getOrderID());
		final OrderModel order = getFlexibleSearchService().getModelByExample(example);
		if ("DELIVERED".equalsIgnoreCase(update.getStatus())) {
			order.setStatus(OrderStatus.COMPLETED);
			// We might have an interceptor or an event might be published where more business logic could happen for
			// onSave...
			getModelService().save(order);
		} else {
			throw new UnknownOrderStatusException("Unknown order status (" + update.getStatus() + ") for order: " + update.getOrderID());
		}
	}

	/**
	 * Unmarshall the order update XML file given.
	 * 
	 * @param orderUpdateFile the order update file
	 * @return the unmarshalled JAXB object
	 * @throws JAXBException
	 */
	public final OrderUpdate unmarshalOrderUpdate(final File orderUpdateFile) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(OrderUpdate.class);

		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final OrderUpdate orderUpdate = (OrderUpdate) jaxbUnmarshaller.unmarshal(orderUpdateFile);

		return orderUpdate;
	}

	/**
	 * Getter for the flexible search service (actually defined in the superclass but without a getter).
	 * 
	 * @return the flexible search service.
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * Getter for the model service (actually defined in the superclass but without a getter).
	 * 
	 * @return the model service.
	 */
	public ModelService getModelService() {
		return modelService;
	}
}
