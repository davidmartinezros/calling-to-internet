package org.dmr.controllers;

import java.util.LinkedHashMap;

import org.dmr.services.CallUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@RestController
@RequestMapping("/api")
public class LRURestController {
    private CallUrlService lruService;
    private LinkedHashMap<Integer,String> linkedHashMap;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LRURestController(CallUrlService lruService) {
    	
        this.lruService = lruService;
        this.linkedHashMap = new LinkedHashMap<>();
        
    }
    
    @RequestMapping(value = "/lru/getTagsFromGoogle", method = RequestMethod.POST)
    public String getTagsFromGoogle(@RequestParam(name="tags") String... strings) {
    	
        return lruService.getTagsFromGoogle(strings);
        
    }
    
    @RequestMapping(value = "/lru/getTagsOfContentUrl", method = RequestMethod.POST)
    public String getTagsOfContentUrl(@RequestParam(name="url") String string1, @RequestParam(name="tag") String string2) {
    	
        return lruService.getTagsOfContentUrl(string1, string2);
        
    }
    
    @RequestMapping(value = "/lru/getContentUrl", method = RequestMethod.POST)
    public String getContentUrl(@RequestParam(name="url") String string) {
    	
        return lruService.getContentUrl(string);
        
    }

    @RequestMapping(value = "/lru/add", method = RequestMethod.POST)
    public int addStringInCache(@RequestParam String string) {
    	
        return lruService.addStringInLRU(string);
        
    }

    @RequestMapping(value = "/lru/{key}")
    public String getStringByKey(@PathVariable int key) throws Exception {
    	
        return lruService.getStringFromLRU(key);
    
    }

    @RequestMapping(value = "/lru/state")
    public LinkedHashMap<Integer,String> getLRUState() throws InterruptedException {

        ResourceSubscriber<LinkedHashMap<Integer,String>> subscriber = new ResourceSubscriber<LinkedHashMap<Integer,String>>() {
            @Override
            public void onNext(LinkedHashMap<Integer,String> s) {
                linkedHashMap = s;
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("Complete");
            }
        };
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Flowable.just(lruService.getLRUState()).subscribeWith(subscriber));

        Thread.sleep(5000);

        return linkedHashMap;
        
    }
    
}
