package com.YouTubeTools.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private String id;
    private String title;
    private String description;
    private List<String> tags;
    private String thumbnailUrl;
    private String channelTitle;
    private String publishedAt;

    /**
     * Returns tags as a comma-separated string.
     * This is useful for Thymeleaf to display or copy tags.
     */
    public String getTagsAsString() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join(", ", tags);
    }

}
