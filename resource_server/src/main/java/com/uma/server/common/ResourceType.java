package com.uma.server.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 08/05/2013
 */

public enum ResourceType {
    PHONE("phone");

    private static Map<String, ResourceType> lookup = new HashMap<String, ResourceType>();

    static {
        for (ResourceType enumType : values()) {
            lookup.put(enumType.getValue(), enumType);
        }
    }

    private final String m_value;

    private ResourceType(String p_value) {
        m_value = p_value;
    }

    public String getValue() {
        return m_value;
    }

    public static ResourceType fromValue(String p_value) {
        return lookup.get(p_value);
    }
}
