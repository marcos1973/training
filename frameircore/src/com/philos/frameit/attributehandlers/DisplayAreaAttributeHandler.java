package com.philos.frameit.attributehandlers;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import com.philos.frameit.model.PictureFrameModel;

public class DisplayAreaAttributeHandler implements
	DynamicAttributeHandler<String, PictureFrameModel> {

    /**
     * Getter for the display area dynamic attribute.
     * 
     * @see de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform.servicelayer.model.AbstractItemModel)
     */
    @Override
    public String get(final PictureFrameModel model) {
	final int frameWidth = model.getWidth() == null ? 0 : model.getWidth()
		.intValue();
	final int frameHeight = model.getHeight() == null ? 0 : model
		.getHeight().intValue();
	final int borderWidth = model.getBorderWidth() == null ? 0 : model
		.getBorderWidth().intValue();

	return (frameWidth - borderWidth) + "x" + (frameHeight - borderWidth)
		+ "cms";
    }

    /**
     * Setter for the display area dynamic attribute - as this attribute is
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
		"Display area is a calculated/dynamic attribute!");
    }
}
