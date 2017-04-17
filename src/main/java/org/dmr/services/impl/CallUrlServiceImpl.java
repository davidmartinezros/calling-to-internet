package org.dmr.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dmr.domain.CallUrl;
import org.dmr.domain.impl.CallUrlImpl;
import org.dmr.services.CallUrlService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@Service
public class CallUrlServiceImpl implements CallUrlService {
	
    private CallUrl lru;
    private int nextKey;
    
    @Override
    public String getTagsFromGoogle(final String... strings) {
    	
    	String result = "";
    	
    	StringBuilder builder = new StringBuilder();
    	
    	for(int i = 0; i< strings.length; i++) {
    		
    		String s = strings[i];
    		
    		if(i > 0) {
    		
    			builder.append("+");
    		
    		}
    		
    	    builder.append(s);
    	}
    	
    	String google = "https://www.google.com/search?q=";
    	String charset = "UTF-8";
    	String userAgent = "David Martinez Ros (+http://davidmartinezros.com)";
    	
    	Elements links = null;
    	
    	try {
    		links = Jsoup.connect(google + URLEncoder.encode(builder.toString(), charset)).userAgent(userAgent).get().select(".g>.r>a");
    	} catch(IOException e) {
    		e.printStackTrace();
    	}

    	for (int i = 0; i < links.size(); i++) {
    		
    		Element link = links.get(i);
    		
    	    String title = link.text();
    	    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
    	    
    	    try {
    	    	url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), charset);
    	    } catch(UnsupportedEncodingException e) {
    	    	e.printStackTrace();
    	    }
    	    
    	    if (!url.startsWith("http")) {
    	        continue; // Ads/news/etc.
    	    }

    	    System.out.println("Title: " + title);
    	    System.out.println("URL: " + url);
    	    
    	    if(result.equals("")) {
    	    	result += "{";
    	    }
    	    
    	    result += "[" + title + "," + url + "]";
    	    
    	    if(i == links.size()-1) {
    	    	result += "}";
    	    } else {
    	    	result += ",";
    	    }
    	}
    	
    	return result;
    
    }
    
    @Override
    public String getTagsOfContentUrl(final String string1, final String string2) {
	    
    	String content = this.getContentUrl(string1);
    	System.out.println(content);
    	
	    String tags = Arrays.toString(getTagValues(content, string2).toArray());
	    System.out.println(tags);
	    
	    return tags;
	    
    }
    
    private List<String> getTagValues(final String str, final String tag) {
    	
    	final Pattern TAG_REGEX = Pattern.compile("<" + tag + ">(.+?)</" + tag + ">");
    	
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_REGEX.matcher(str);
	    
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    
	    return tagValues;
	
    }
    
    @Override
    public String getContentUrl(final String string) {
    	
    	URL url = null;
    	String result = null;
    	
    	try {
    		
    		url = new URL(string);
    		
    	} catch(MalformedURLException e) {
    		e.printStackTrace();
    		result = e.getMessage();
    	}
    	
    	if(url != null) {
    		
	    	try {
	    		
		    	InputStreamReader isr = new InputStreamReader(url.openStream());
		    	BufferedReader in = new BufferedReader(isr);
		        
		    	String content = "";
		    	String inputLine = null;
		    	
		        while ((inputLine = in.readLine()) != null) {
		        	content += inputLine;
		        }
		        
		        in.close();
		        
		        Document document = Jsoup.parse(content);
		        
		        result = document.toString();
		        
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    		result = e.getMessage();
	    	}
	    	
    	}
        
        return result;
    }

    public CallUrlServiceImpl() {
        this.lru = CallUrlImpl.getInstance();
        this.nextKey=0;
    }

    @Override
    public int addStringInLRU(final String string) {
    	
        int newKey = getNewKey();
        lru.put(newKey,string);

        return newKey;
    }

    @Override
    public String getStringFromLRU(final int key) throws Exception {
    	
        return lru.get(key);
    
    }

    @Override
    public LinkedHashMap<Integer,String> getLRUState() {
    	
        return lru.getLRU();
        
    }

    private int getNewKey(){
    	
        this.nextKey++;
        return nextKey;
        
    }
    
}
