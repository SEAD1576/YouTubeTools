package com.YouTubeTools.Controller;

import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Model.Video;
import com.YouTubeTools.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/youtube")
public class YouTubeTagsController {

    @Autowired
    private YouTubeService youTubeService;

    @Value("${youtube.api.key}")
    private String apiKey;

    private boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @PostMapping("/search")
    public String videoTags(@RequestParam("videoTitle") String videoTitle, Model model) {

        if (!isApiKeyConfigured()) {
            model.addAttribute("error", "API Key is not configured");
            return "home";
        }

        if (videoTitle == null || videoTitle.isEmpty()) {
            model.addAttribute("error", "Video Title is required");
            return "home";
        }

        try {
            SearchVideo result = youTubeService.searchVideos(videoTitle);

            // Safety checks: ensure primaryVideo is not null
            Video primaryVideo = result.getPrimaryVideo();
            if (primaryVideo == null) {
                primaryVideo = new Video();
                primaryVideo.setTags(Collections.emptyList());
                model.addAttribute("error", "No primary video found for this title");
            } else if (primaryVideo.getTags() == null) {
                primaryVideo.setTags(Collections.emptyList());
            }

            // Put primary video and its tags in the model
            model.addAttribute("primaryVideo", primaryVideo);
            model.addAttribute("primaryTags", primaryVideo.getTagsAsString()); // ✅ for Copy Tags button

            // Handle related videos
            if (result.getRelatedVideos() == null) {
                model.addAttribute("relatedVideos", Collections.emptyList());
                model.addAttribute("allTags", primaryVideo.getTagsAsString()); // ✅ still a String
            } else {
                // make sure each related video's tags list is not null
                result.getRelatedVideos().forEach(v -> {
                    if (v.getTags() == null) v.setTags(Collections.emptyList());
                });

                model.addAttribute("relatedVideos", result.getRelatedVideos());

                // Build "Copy All" tags string (primary + related)
                String allTags = primaryVideo.getTags().stream()
                        .collect(Collectors.joining(", "));

                String relatedTags = result.getRelatedVideos().stream()
                        .flatMap(v -> v.getTags().stream())
                        .collect(Collectors.joining(", "));

                // Merge primary + related tags (handle empty cases safely)
                String combinedTags = allTags.isEmpty() ? relatedTags :
                        relatedTags.isEmpty() ? allTags :
                                allTags + ", " + relatedTags;

                model.addAttribute("allTags", combinedTags); // ✅ for Copy All button
            }

            return "home";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "home";
        }
    }
}
