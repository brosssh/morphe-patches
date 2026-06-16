package app.morphe.extension.instagram.hide.suggestedContent;

import java.util.List;

@SuppressWarnings("unused")
public class HideSuggestedContentPatch {

    private static final List<String> BLOCKED_STORY_TYPES = List.of("suggested_user_reel", "suggested_user");

    /**
     * Injection point.
     */
    public static String getValidStoryType(String storyType) {
        if (BLOCKED_STORY_TYPES.contains(storyType)) return null;

        return storyType;
    }
}
