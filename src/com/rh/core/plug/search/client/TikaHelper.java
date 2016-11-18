package com.rh.core.plug.search.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.rh.core.plug.search.ExtractResult;

/**
 * @author liwei
 *
 */
public class TikaHelper {

	/** tika object */
	private static final Tika TIKA = new Tika();

	/**
	 * extraction rich text
	 * 
	 * @param inputStream
	 *            - inputstream
	 * @return content - string
	 * @throws IOException IO 异常
	 * 
	 */
	public static ExtractResult extractText(InputStream inputStream) throws IOException {
		// TODO 超大文件测试，buff size 设置
		// text extracting
		Parser parser = new AutoDetectParser();
		ParseContext context = new ParseContext();
		context.set(Parser.class, parser);

		// ContentHandler content = new BodyContentHandler();
		StringWriter textBuffer = new StringWriter();
		ContentHandler content = new BodyContentHandler(textBuffer);
		Metadata metadata = new Metadata();
		try {
			parser.parse(inputStream, content, metadata, context);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("text extracting error, "
					+ e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException("text extracting error, "
					+ e.getMessage());
		} catch (TikaException e) {
			e.printStackTrace();
			throw new RuntimeException("text extracting error, "
					+ e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        ExtractResult result = new ExtractResult();

        String text = content.toString().replace("�", "");
        result.setContent(text);
        String mType = metadata.get("Content-Type");
        mType = parseMimeType(mType);
        result.setMType(mType);
        return result;
	}

	/**
	 * extract file suffix from inputStream
	 * @param inputStream - InputStream
	 * @return suffix 
	 * @throws IOException - throws ioexception, if read inputstream error
	 */
	public static String extractSuffix(InputStream inputStream)
			throws IOException {
		Metadata met = new Metadata();
		try {
			TIKA.parseToString(inputStream, met);
		} catch (TikaException e) {
			e.printStackTrace();
		}
		String contentType = met.get(Metadata.CONTENT_TYPE);

		return parseMimeType(contentType);
	}

	/**
	 * parse mimtype to type <br>
	 * application/msword ==> doc
	 * 
	 * @param mType
	 *            - mime type string
	 * @return type string
	 */
	public static String parseMimeType(String mType) {
		String docType = "";
		if (mType.contains("word")) {
			docType = "doc";
		} else if (mType.contains("excel")
				|| mType.contains("openxmlformats-officedocument.spreadsheetml")) {
			docType = "xls";
		} else if (mType.contains("powerpoint")
				|| mType.contains("openxmlformats-officedocument.presentationml")) {
			docType = "ppt";
		} else if (mType.contains("text/plain")) {
			docType = "txt";
		} else if (mType.contains("application/zip")) {
			docType = "zip";
		} else if (mType.contains("/pdf")) {
			docType = "pdf";
		} else {
			docType = mType;
		}
		return docType;
	}


}
