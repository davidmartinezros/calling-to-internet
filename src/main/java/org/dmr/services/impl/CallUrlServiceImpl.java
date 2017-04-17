package org.dmr.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.springframework.stereotype.Service;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@Service
public class CallUrlServiceImpl implements CallUrlService {
	
    private CallUrl lru;
    private int nextKey;
    
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
