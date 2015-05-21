package com.philos.frameit.services;

import java.util.List;

import com.philos.frameit.model.PictureFrameModel;

public interface PictureFrameService {

	/**
	 * Get all picture frames.
	 * 
	 * @return a list of picture frames.
	 */
	List<PictureFrameModel> getAllPictureFrames();

	/**
	 * Get a picture frame by its code.
	 * 
	 * @param code the code
	 * @return the picture frame
	 */
	PictureFrameModel getPictureFrameByCode(String code);

	/**
	 * Get a list of picture frames matching the width and height specified.
	 * 
	 * @param width the width
	 * @param height the height
	 * @return a list of picture frames matching the dimensions specified.
	 */
	List<PictureFrameModel> getPictureFramesByDimensions(Integer width, Integer height);
}
