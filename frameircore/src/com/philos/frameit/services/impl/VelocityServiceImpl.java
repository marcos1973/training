package com.philos.frameit.services.impl;

import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.philos.frameit.services.VelocityService;

@Service("velocityService")
@Scope("tenant")
public class VelocityServiceImpl implements VelocityService {
	private final static Logger LOG = Logger.getLogger(VelocityService.class);

	/**
	 * Gets the HTML content for the template provided with the data provided.
	 * 
	 * @param textTemplate the email template
	 * @param data a map containing the data to populate the template with
	 * @return the string holding the content
	 * @see com.philos.frameit.services.VelocityService#toString(String, java.util.Map)
	 */
	@Override
	public String toString(final String textTemplate, final Map<String, Object> data) {
		// first, we init the runtime engine.
		Velocity.setProperty("input-encoding", "UTF-8");
		Velocity.setProperty("output-encoding", "UTF-8");
		Velocity.init();

		// lets make a Context and put data into it
		final VelocityContext context = new VelocityContext();
		for (final String key : data.keySet()) {
			context.put(key, data.get(key));
		}

		// lets render the string template provided
		final StringWriter output = new StringWriter();

		Velocity.evaluate(context, output, "textTemplate", textTemplate);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Returning: " + output);
		}

		return output.toString();
	}
}
