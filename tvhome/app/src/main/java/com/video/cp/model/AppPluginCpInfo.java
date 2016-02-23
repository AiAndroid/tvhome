package com.video.cp.model;

/**
 * Created by lijianbo1 on 15-3-25.
 */
public class AppPluginCpInfo extends BaseEpItem {

    public AppPluginCpInfo(RawCpItem item) {
       copyFrom(item);
    }


    // define specific get method for convenience
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(super.toString());
        // add more field output

        return sb.toString();
    }
}
