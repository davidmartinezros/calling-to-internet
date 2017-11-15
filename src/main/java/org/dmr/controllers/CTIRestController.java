package org.dmr.controllers;

import org.dmr.services.CallUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@RestController
@RequestMapping("/api")
public class CTIRestController {
	
	@Autowired
    private CallUrlService callService;
	
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @RequestMapping(value = "/cti/getTagsFromGoogle", method = RequestMethod.POST)
    public String getTagsFromGoogle(
    		@RequestParam(name="tags") String... strings) {
    	
        return callService.getTagsFromGoogle(strings);
        
    }
    
    @RequestMapping(value = "/cti/getTagsOfContentUrl", method = RequestMethod.POST)
    public String getTagsOfContentUrl(
    		@RequestParam(name="url") String string1,
    		@RequestParam(name="tag") String string2) {
    	
        return callService.getTagsOfContentUrl(string1, string2);
        
    }
    
    @RequestMapping(value = "/cti/getContentUrl", method = RequestMethod.POST)
    public String getContentUrl(
    		@RequestParam(name="url") String string) {
    	
        return callService.getContentUrl(string);
        
    }
    
    @RequestMapping(value = "/cti/state", method = RequestMethod.GET)
    public String getState() throws Exception {
    	
    	return "OK";
    	
    }
    
}
