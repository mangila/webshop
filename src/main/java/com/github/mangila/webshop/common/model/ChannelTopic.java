package com.github.mangila.webshop.common.model;

import java.util.EnumSet;
import java.util.List;

public enum ChannelTopic {
    ORDERS,
    PRODUCTS,
    DELIVERIES,
    INVENTORY,
    CUSTOMERS,
    PAYMENTS,
    UNKNOWN;


    public static List<String> getTopicsAsStrings() {
        return EnumSet.allOf(ChannelTopic.class)
                .stream()
                .map(Enum::toString)
                .toList();
    }

    public static ChannelTopic fromString(String topic) {
        return EnumSet.allOf(ChannelTopic.class)
                .stream()
                .filter(t -> t.toString().equalsIgnoreCase(topic))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
