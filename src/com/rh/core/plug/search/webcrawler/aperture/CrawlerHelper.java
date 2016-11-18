package com.rh.core.plug.search.webcrawler.aperture;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.semanticdesktop.aperture.accessor.base.ModelAccessData;
import org.semanticdesktop.aperture.accessor.impl.DefaultDataAccessorRegistry;
import org.semanticdesktop.aperture.crawler.Crawler;
import org.semanticdesktop.aperture.crawler.web.WebCrawler;
import org.semanticdesktop.aperture.datasource.filesystem.FileSystemDataSource;
import org.semanticdesktop.aperture.datasource.web.WebDataSource;
import org.semanticdesktop.aperture.hypertext.linkextractor.impl.DefaultLinkExtractorRegistry;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.rdf.impl.RDFContainerImpl;

/**
 * @author liwei
 * 
 */
public class CrawlerHelper {

    // rdf factory
    private static final ModelFactory M_FACTORY = RDF2Go.getModelFactory();

    // I used 1000 bytes at first, but found that some documents have
    // meta tag well past the first 1000 bytes.
    // (e.g. http://cn.promo.yahoo.com/customcare/music.html)
    private static final int CHUNK_SIZE = 2000;

    // NUTCH-1006 Meta equiv with single quotes not accepted
    private static Pattern metaPattern =
            Pattern.compile("<meta\\s+([^>]*http-equiv=(\"|')?content-type(\"|')?[^>]*)>",
                    Pattern.CASE_INSENSITIVE);
    private static Pattern charsetPattern =
            Pattern.compile("charset=\\s*([a-z][_\\-0-9a-z]*)",
                    Pattern.CASE_INSENSITIVE);

    /**
     * 判断html所属字符编码
     * @param html - html content
     * @return - encoding
     */
    public static String sniffCharacterEncoding(byte[] html) {
        int length = html.length < CHUNK_SIZE ? html.length : CHUNK_SIZE;

        // We don't care about non-ASCII parts so that it's sufficient
        // to just inflate each byte to a 16-bit value by padding.
        // For instance, the sequence {0x41, 0x82, 0xb7} will be turned into
        // {U+0041, U+0082, U+00B7}.
        String str = "";
        try {
            str = new String(html, 0, length,
                    Charset.forName("ASCII").toString());
        } catch (UnsupportedEncodingException e) {
            // code should never come here, but just in case...
            return null;
        }

        Matcher metaMatcher = metaPattern.matcher(str);
        String encoding = null;
        if (metaMatcher.find()) {
            Matcher charsetMatcher = charsetPattern.matcher(metaMatcher.group(1));
            if (charsetMatcher.find()) {
                encoding = new String(charsetMatcher.group(1));
            }
        }

        return encoding;
    }

    /**
     * 截取html
     * @param html - html string
     * @param preTag - 开始标识
     * @param postTag - 结束标识
     * @return 截取后结果, 如果截取结果为空，我们将返回空字符串""
     */
    public static String subHtmlText(String html, String preTag, String postTag) {
        String text = "";
        if (null != preTag && 0 < preTag.length() && null != postTag
                && 0 < postTag.length()) {
            int preIndex = html.indexOf(preTag);
            int sink = preIndex + preTag.length();
            int postIndex = html.indexOf(postTag, sink);
            if (preIndex > -1 && postIndex > -1) {
                text = html.substring(preIndex, postIndex);
                // strip HTML
                String lnTag = "${ln}";
                text = text.replace("<p>", lnTag);
                text = Jsoup.parse(text).text();
                text = text.replace(lnTag, "\n");
            }
        }
        return text;
    }

    /**
     * get crawler
     * @param model - model
     * @param source - data source
     * @param loader - data handler
     * @return new crawler
     */
    public static Crawler getCrawler(Model model,
            org.semanticdesktop.aperture.datasource.DataSource source,
            RuahoCrawlerHandler loader) {
        // setup a crawler that can handle this type of DataSource
        WebCrawler crawler = new WebCrawler();
        crawler.setDataSource(source);
        crawler.setDataAccessorRegistry(new DefaultDataAccessorRegistry());
        crawler.setMimeTypeIdentifier(new MagicMimeTypeIdentifier());
        crawler.setLinkExtractorRegistry(new DefaultLinkExtractorRegistry());
        crawler.setCrawlerHandler(loader);

        // setup accessdata
        ModelAccessData accessData = null;
        try {
            accessData = newAccessData(model, source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        crawler.setAccessData(accessData);
        return crawler;
    }

    /**
     * get match categorys
     * @param siteCategorys - category mapping
     * @param targetUrl - url
     * @return - matched categorys name
     */
    public static List<String> getMatchCategorys(Map<String, String> siteCategorys, String targetUrl) {
        List<String> matchCatList = new ArrayList<String>();
        Set<String> keys = siteCategorys.keySet();
        // find match categorys list
        for (String name : keys) {
            String regExp = siteCategorys.get(name);
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(targetUrl);
            if (m.matches()) {
                matchCatList.add(name);
            }
        }
        return matchCatList;
    }

    /**
     * @param model - model
     * @param source - data source
     * @return model access data object
     * @throws IOException - throw ioexception ,when initialize error
     */
    private static ModelAccessData newAccessData(Model model,
            org.semanticdesktop.aperture.datasource.DataSource source)
            throws IOException {
        ModelAccessData accessData = new ModelAccessData(model);
        accessData.initialize();
        return accessData;
    }

    /**
     * get file system datasource
     * @param url - file url
     * @param file - root folder
     * @param followSymbolicLinks - 根据图标，和文件描述部分，扫描与之有关联的文件。
     * @param maxBytes - max bytes of file
     * @param depth - file depth
     * @return - data source
     */
    @SuppressWarnings("unused")
    private static FileSystemDataSource getDataSource(String url, String file, boolean followSymbolicLinks,
            long maxBytes, int depth) {
        FileSystemDataSource result = new FileSystemDataSource();
        org.semanticdesktop.aperture.rdf.RDFContainer container = newInstance(url);
        result.setConfiguration(container);
        result.setRootFolder(file);
        result.setFollowSymbolicLinks(Boolean.valueOf(followSymbolicLinks));
        result.setMaximumSize(Long.valueOf(maxBytes));
        result.setMaximumDepth(Integer.valueOf(depth));
        return result;
    }

    /**
     * get web datasource
     * @param url - root url
     * @param maxBytes - max bytes of web page
     * @param depth - page depth
     * @return - web datasource
     */
    @SuppressWarnings("unused")
    private static WebDataSource getDataSource(String url, long maxBytes, int depth) {
        WebDataSource result = new WebDataSource();
        org.semanticdesktop.aperture.rdf.RDFContainer container = newInstance(url);
        result.setConfiguration(container);
        result.setRootUrl(url);
        result.setMaximumSize(Long.valueOf(maxBytes));
        result.setMaximumDepth(Integer.valueOf(depth));
        return result;
    }

    /**
     * new RDFContainer instance
     * @param uri - uri
     * @return RDFContainerImpl
     */
    private static RDFContainerImpl newInstance(String uri) {
        try {
            Model newModel = M_FACTORY.createModel();
            newModel.open();
            return new RDFContainerImpl(newModel, uri);
        } catch (ModelRuntimeException me) {
            throw new RuntimeException(me);
        }
    }

}
