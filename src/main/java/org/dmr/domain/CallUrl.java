package org.dmr.domain;

import java.util.LinkedHashMap;

/**
 * Created by davidmartinezros on 10/04/2017.
 */
public interface CallUrl {
    void put(int key, String value);

    String get(int key) throws Exception;

    LinkedHashMap<Integer,String> getLRU();
}
