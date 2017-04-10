package org.dmr.domain.impl;


import java.util.LinkedHashMap;

import org.dmr.domain.CallUrl;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
public class CallUrlImpl implements CallUrl {
    private static CallUrlImpl ourInstance = new CallUrlImpl(3);
    private LinkedHashMap<Integer,String> lruMap;
    private int lruSize;

    public static CallUrlImpl getInstance() {
        return ourInstance;
    }

    public CallUrlImpl(int lruSize) {
        this.lruMap = new LinkedHashMap<>();
        this.lruSize = lruSize;
    }

    public void put(int key, String value) {
        if(lruMap.values().size()>=lruSize){
            lruMap.remove(lruMap.keySet().iterator().next());
        }
        lruMap.put(key, value);
    }

    public String get(int key) throws Exception {
        if(lruMap.get(key)!=null){
            final String value = lruMap.get(key);
            lruMap.remove(key);
            lruMap.put(key, value);
            return value;
        } else {
            throw new NullPointerException("Key is not in cache");
        }
    }

    @Override
    public LinkedHashMap<Integer, String> getLRU() {
        return this.lruMap;
    }

    @Override
    public String toString(){
        String lruString = "";
        for (String value : lruMap.values()) {
            lruString = lruString + value + " ";
        }
        return lruString;
    }
}
