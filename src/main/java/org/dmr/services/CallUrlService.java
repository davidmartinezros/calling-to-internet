package org.dmr.services;

import java.util.LinkedHashMap;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
public interface CallUrlService {
    int addStringInLRU(String string);

    String getStringFromLRU(int id) throws Exception;

    LinkedHashMap<Integer,String> getLRUState();
}
