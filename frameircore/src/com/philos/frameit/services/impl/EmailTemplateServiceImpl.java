package com.philos.frameit.services.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.philos.frameit.model.template.EmailTemplateModel;
import com.philos.frameit.services.EmailTemplateService;

@Service("emailTemplateService")
@Scope("tenant")
public class EmailTemplateServiceImpl implements EmailTemplateService {
	private final static Logger LOG = Logger.getLogger(EmailTemplateService.class);
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Finds an email template by code
	 * 
	 * @param code the template code
	 * @return the template
	 * @see com.philos.frameit.services.EmailTemplateService#getByCode(java.lang.String)
	 */
	@Override
	public EmailTemplateModel getByCode(final String code) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Loading email template with code: " + code);
		}

		final EmailTemplateModel example = new EmailTemplateModel();
		example.setCode(code);

		return getFlexibleSearchService().getModelByExample(example);
	}

	/**
	 * Getter for the flexible search service.
	 * 
	 * @return the flexible search service.
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * Setter for the flexible search service.
	 * 
	 * @param flexibleSearchService the flexible search service.
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}
