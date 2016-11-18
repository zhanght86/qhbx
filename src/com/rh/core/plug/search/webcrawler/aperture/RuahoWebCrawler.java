package com.rh.core.plug.search.webcrawler.aperture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.semanticdesktop.aperture.crawler.Crawler;
import org.semanticdesktop.aperture.datasource.config.DomainBoundaries;
import org.semanticdesktop.aperture.datasource.config.RegExpPattern;
import org.semanticdesktop.aperture.datasource.web.WebDataSource;
import org.semanticdesktop.aperture.rdf.RDFContainer;
import org.semanticdesktop.aperture.rdf.impl.RDFContainerFactoryImpl;

/**
 * ruaho web crawler
 * @author liwei
 * 
 */
public class RuahoWebCrawler {
    // rdf factory
    private static final ModelFactory M_FACTORY = RDF2Go.getModelFactory();

    /**
     * get web crawler
     * @param startUrl - start url
     * @param depth - depth of web page
     * @param maxBytes - max bytes of page
     * @param cds - crawl data source
     * @return crawler object
     */
    public Crawler getCrawler(String startUrl, int depth, long maxBytes, CrawlDataSource cds) {
        DomainBoundaries boundaries = new DomainBoundaries();
        boolean identifyingMimeType = true;
        boolean extractingContents = true;
        boolean includeEmbeddedResources = false;
        boolean verbose = true;
        File outputFile = new File("./webcrawler.rdf");
        List<String> includeList = cds.getIncludeList();
        if (null != includeList) {
            for (String patten : includeList) {
                boundaries.addIncludePattern(new RegExpPattern(patten));
            }
        }
        List<String> excludeList = cds.getExcludeList();
        if (null != excludeList) {
            for (String patten : excludeList) {
                boundaries.addExcludePattern(new RegExpPattern(patten));
            }
        }

        // create a data source configuration
        RDFContainerFactoryImpl factory = new RDFContainerFactoryImpl();
        RDFContainer config = factory.newInstance("urn:test:exampleimapsource");
        WebDataSource source = new WebDataSource();
        source.setConfiguration(config);
        source.setRootUrl(startUrl);
        source.setIncludeEmbeddedResources(includeEmbeddedResources);
        source.setDomainBoundaries(boundaries);

        if (depth >= 0) {
            source.setMaximumDepth(depth);
        }
        RuahoCrawlerHandler handler = new RuahoCrawlerHandler(identifyingMimeType, extractingContents, verbose,
                outputFile, cds);

        Model newModel = M_FACTORY.createModel();
        newModel.open();

        // setup a crawler that can handle this type of DataSource
        Crawler crawler = CrawlerHelper.getCrawler(newModel, source, handler);

        return crawler;
    }

    /**
     * test code
     * @param args - null
     */
    public static void main(String[] args) {
        String startUrl = "http://news.sina.com.cn/";
        int depth = 1;
        long maxBytes = 1024 * 4;

        List<String> includeList = new ArrayList<String>();
        includeList.add("http://news.sina.com.cn/.*.shtml");
        RuahoWebCrawler test = new RuahoWebCrawler();
        // setup a crawler that can handle this type of DataSource
        Crawler crawler = test.getCrawler(startUrl, depth, maxBytes, new CrawlDataSource());
        crawler.crawl();

        System.out.println("----------end-------");

    }

}
