package com.android.slw.http;

import java.io.Serializable;

/**
 * Created by liwu.shu on 2016/9/19.
 */
public class NewsCategory implements Serializable {

    public String categoryCode;
    public String categoryName;

    public NewsCategory() {

    }

    public NewsCategory(String categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "NewsCategory{" + "categoryCode='" + categoryCode + '\'' + ", categoryName='" + categoryName + '\''
                + '}';
    }

    @Override
    public int hashCode() {
        return categoryCode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof NewsCategory) {
            return ((NewsCategory) o).categoryCode.equals(categoryCode);
        }
        return false;
    }
}
