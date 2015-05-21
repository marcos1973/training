package com.philos.frameit.attributehandlers;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import com.philos.frameit.model.PictureFrameModel;

public class DimensionsAttributeHandler implements
	DynamicAttributeHandler<String, PictureFrameModel> {

    /**
     * Getter for the dimensions dynamic attribute.
     * 
     * @see de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform.servicelayer.model.AbstractItemModel)
     */
    @Override
    public String get(final PictureFrameModel model) {
	return model.getWidth() + "x" + model.getHeight() + "cms";
    }

    /**
     * Setter for the dimensions dynamic attribute - as this attribute is
     * calculated/dynamically generated, we cannot set this directly.
     * 
     * Note: we could parse it here and calculate it back to set the other
     * dimension attributes but I prefer not to as it is confusing to set one
     * attribute and have it set two others instead.
     * 
     * @see de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler
     *      #set(de.hybris.platform.servicelayer.model.AbstractItemModel,
     *      java.lang.Object)
     */
    @Override
    public void set(final PictureFrameModel model, final String value) {
	throw new UnsupportedOperationException(
		"Dimensions is a calculated/dynamic attribute!");
    }
}
