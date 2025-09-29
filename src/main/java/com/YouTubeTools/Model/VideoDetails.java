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
public class VideoDetails {
    private String id;
    private String title;
    private String description;
    private List<String> tags;
    private String thumbnailUrl;
    private String channelTitle;
    private String publishedAt;
}
