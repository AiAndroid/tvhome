package com.video.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cineaste Search Result
 *
 * Created by zhuzhenhua on 16-1-8.
 */
public class CineasteSR extends BaseEntity {

    public Data data;

    public static class Data implements Serializable {
        public int count;
        public List<Cineaste> cineastes;

        public List<String> getTags() {
            List<String> tags = new ArrayList<>(cineastes.size());
            for (int i = 0, size = cineastes.size(); i < size; i++)
                tags.add(cineastes.get(i).name);
            return tags;
        }
    }

}