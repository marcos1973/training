package com.philos.frameit.order;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.philos.frameit.order.jaxb.OrderUpdate;

public class GenerateOrderUpdate {

	@Test
	public void testGenerateOrderUpdate() throws FileNotFoundException {
		final OrderUpdate update = new OrderUpdate();
		update.setOrderID("testID");
		update.setStatus("Delivered");

		try {
			final File file = new File("orderUpdate" + System.currentTimeMillis() + ".xml");

			final JAXBContext jaxbContext = JAXBContext.newInstance(OrderUpdate.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Marshall it to file
			jaxbMarshaller.marshal(update, file);

			// For informational purposes
			jaxbMarshaller.marshal(update, System.out);
		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}
}
