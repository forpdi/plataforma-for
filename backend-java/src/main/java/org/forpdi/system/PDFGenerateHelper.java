package org.forpdi.system;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.enterprise.context.RequestScoped;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

@RequestScoped
public class PDFGenerateHelper {
	
	public Image getImageFromTextArea(String imgTag) throws BadElementException, MalformedURLException, IOException {
		String encodedStr = imgTag.replaceAll("<img src=\"data:image/.*;base64,", "").replaceAll("\">", "");
		byte bytes[] = java.util.Base64.getDecoder().decode(encodedStr);
		return Image.getInstance(bytes);
	}
}
