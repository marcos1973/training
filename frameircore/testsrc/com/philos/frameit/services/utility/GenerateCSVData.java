package com.philos.frameit.services.utility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class GenerateCSVData {
	private final String[] glassTypes = { "STANDARD", "PERSPEX", "NONE" };
	private final String[] widths = { "10", "20", "30", "40", "50", "100" };
	private final String[] heights = { "10", "20", "30", "40", "50", "100" };
	private final String[] borderWidths = { "1", "2", "3", "4", "5", "10" };
	private final String[] categories = { "cat1", "cat2" };
	private final String[] pictures = { "frame1-200x200", "frame2-200x200", "frame3-200x200" };
	private final String[] thumbnails = { "frame1-50x50", "frame2-50x50", "frame3-50x50" };

	@Test
	public void testGeneratePictureFrameData() throws FileNotFoundException {
		final FileOutputStream fos = new FileOutputStream("pictureFrameData.impex");
		final PrintWriter pw = new PrintWriter(fos);

		pw.println("$productCatalog=Default");
		pw.println("$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,virtual=true,default=$productCatalog:Staged]");
		pw.println("$approved=approvalStatus(code)[virtual=true,default='approved']");
		pw.println("$unit=unit(code)[virtual=true,default='pieces']");
		pw.println();
		pw.println("INSERT_UPDATE PictureFrame;code[unique=true];ean;name[lang=en];width;height;borderWidth;glass(code);supercategories(code,$catalogVersion);picture(code,$catalogVersion);thumbnail(code,$catalogVersion);$catalogVersion;$approved;$unit");
		pw.println();

		for (int i = 1; i < 301; i++) {
			final StringBuffer buffer = new StringBuffer();

			buffer.append(';').append(i).append(';').append(i).append(";Test frame ").append(i);

			buffer.append(';').append(widths[(int) (System.nanoTime() % widths.length)]);
			buffer.append(';').append(heights[(int) (System.nanoTime() % heights.length)]);
			buffer.append(';').append(borderWidths[(int) (System.nanoTime() % borderWidths.length)]);
			buffer.append(';').append(glassTypes[(int) (System.nanoTime() % glassTypes.length)]);
			buffer.append(';').append(categories[(int) (System.nanoTime() % categories.length)]);
			buffer.append(';').append(pictures[(int) (System.nanoTime() % pictures.length)]);
			buffer.append(';').append(thumbnails[(int) (System.nanoTime() % thumbnails.length)]);

			pw.print(buffer.toString());
			pw.println();
		}

		pw.println();

		pw.close();
	}

	@Test
	public void testGeneratePriceRowData() throws FileNotFoundException {
		final FileOutputStream fos = new FileOutputStream("priceRowData.impex");
		final PrintWriter pw = new PrintWriter(fos);

		pw.println("$productCatalog=Default");
		pw.println("$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,virtual=true,default=$productCatalog:Staged]");
		pw.println("$unit=unit(code)[virtual=true,default='pieces']");
		pw.println("$currency=currency(isocode)[virtual=true,default='EUR']");

		pw.println("INSERT_UPDATE PriceRow;product(code,$catalogVersion)[unique=true];price;$currency;$catalogVersion;$unit;");
		pw.println();

		for (int i = 1; i < 301; i++) {
			final StringBuffer buffer = new StringBuffer();

			buffer.append(';').append(i).append(';').append(i + (System.nanoTime() % 100));

			pw.println(buffer.toString());
		}

		pw.println();

		pw.close();
	}
}
