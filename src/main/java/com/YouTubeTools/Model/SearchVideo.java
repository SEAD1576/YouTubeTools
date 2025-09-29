                                                                                package com.YouTubeTools.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

                                                                                @Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchVideo {
    private Video primaryVideo;
    private List<Video> relatedVideos;

    public String getAllTagsAsString() {
        return Stream.concat(
                        Stream.ofNullable(primaryVideo)
                                .filter(v -> v.getTags() != null)
                                .flatMap(v -> v.getTags().stream()),
                        relatedVideos == null ? Stream.empty() :
                                relatedVideos.stream()
                                        .filter(v -> v.getTags() != null)
                                        .flatMap(v -> v.getTags().stream())
                )
                .distinct()
                .collect(Collectors.joining(", "));
    }


}
