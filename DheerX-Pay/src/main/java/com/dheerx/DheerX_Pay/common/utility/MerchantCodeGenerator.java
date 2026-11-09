package com.dheerx.DheerX_Pay.common.utility;

import java.util.UUID;

public class MerchantCodeGenerator {

    public static String generate() {
        return "MRC-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}