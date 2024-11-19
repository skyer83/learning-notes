package com.lulala.jfk.staticAndFinal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024/11/18 17:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    /** 品牌 */
    private String name;

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

}
