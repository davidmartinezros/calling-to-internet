package org.dmr.services.impl;

import java.util.LinkedHashMap;

import org.dmr.domain.CallUrl;
import org.dmr.domain.impl.CallUrlImpl;
import org.dmr.services.CallUrlService;
import org.springframework.stereotype.Service;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
@Service
public class CallUrlServiceImpl implements CallUrlService{
    private CallUrl lru;
    private int nextKey;

    public CallUrlServiceImpl() {
        this.lru = CallUrlImpl.getInstance();
        this.nextKey=0;
    }

    @Override
    public int addStringInLRU(String string) {
        int newKey = getNewKey();
        lru.put(newKey,string);

        return newKey;
    }

    @Override
    public String getStringFromLRU(int key) throws Exception {
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
