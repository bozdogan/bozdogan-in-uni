package org.bozdogan.util;

import java.util.HashMap;
import java.util.Map;

/** Cipher info util for notebook */
public class MetaData{
    public static String serialize(Map<String, String> metadata){
        StringBuilder _serialized = new StringBuilder();

        _serialized.append("{");
        for(Map.Entry e: metadata.entrySet())
            _serialized.append(e.getKey()).append("=").append(e.getValue()).append(",");

        _serialized.append("}");
        return _serialized.toString();
    }

    public static Map<String, String> deserialize(String metadata){
        HashMap<String, String> _deserialized = new HashMap<>();

        int lastCommaPos = metadata.lastIndexOf(",");
        // ignore '{' and '}'. if no comma is present just treat all the thing as one variable.
        metadata = metadata.substring(1, lastCommaPos!=-1 ? lastCommaPos : metadata.length()-1);
        String[] items = metadata.split(",");
        for(String item: items){
            String[] pair = item.split("=",2);
            if(pair.length==2)
                _deserialized.put(pair[0], pair[1]);
        }

        return _deserialized;
    }
}
