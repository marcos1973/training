package com.philos.frameit.services.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.philos.frameit.model.PictureFrameModel;
import com.philos.frameit.services.PictureFrameService;

public class PictureFrameServiceImpl implements PictureFrameService {

	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Get all picture frames.
	 * 
	 * @return a list of picture frames
	 * @see com.philos.frameit.services.PictureFrameService#getAllPictureFrames()
	 */
	@Override
	public List<PictureFrameModel> getAllPictureFrames() {
		final SearchResult<PictureFrameModel> result = getFlexibleSearchService().search("SELECT * FROM {" + PictureFrameModel._TYPECODE + "}");
		return result.getResult();
	}

	/**
	 * Find a picture frame by its code.
	 * 
	 * @param code the code
	 * @return the picture frame
	 * @see com.philos.frameit.services.PictureFrameService#getPictureFrameByCode(java.lang.String)
	 */
	@Override
	public PictureFrameModel getPictureFrameByCode(final String code) {
		final PictureFrameModel example = new PictureFrameModel();
		example.setCode(code);

		return getFlexibleSearchService().getModelByExample(example);
	}

	/**
	 * Query for picture frames by width and height.
	 * 
	 * @param width the width of the frame
	 * @param height the height of the frame.
	 * @return a list of picture frames
	 * @see com.philos.frameit.services.PictureFrameService#getPictureFramesByDimensions(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public List<PictureFrameModel> getPictureFramesByDimensions(final Integer width, final Integer height) {
		final String queryString = "SELECT * FROM {" + PictureFrameModel._TYPECODE + "} WHERE {" + PictureFrameModel.WIDTH + "} = ?width AND {"
				+ PictureFrameModel.HEIGHT + "} = ?height";
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("width", width);
		queryParams.put("height", height);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, queryParams);
		final SearchResult<PictureFrameModel> result = getFlexibleSearchService().search(query);

		return result.getResult();
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService() {
		if (modelService == null) {
			modelService = Registry.getCoreApplicationContext().getBean("modelService", ModelService.class);
		}
		return modelService;
	}

	/**
	 * @param modelService the modelService to set
	 */
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		if (flexibleSearchService == null) {
			flexibleSearchService = Registry.getCoreApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
		}
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}
