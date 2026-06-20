package app.morphe.extension.instagram.hide.suggestedContent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public class HideSuggestedStoriesPatch {

    private static final List<String> BLOCKED_STORY_TYPES = List.of(
            "suggested_user_reel",
            "suggested_user",
            "ads_reel"
    );

    /**
     * Injection point.
     */
    public static List<Object> removeSuggestedStories(
            List<Object> storiesList,
            String reelTypeFieldName
    )
            throws IllegalAccessException, NoSuchFieldException {
        List<Object> patchedStoryList = new ArrayList<>(storiesList);

        Iterator<Object> iterator = patchedStoryList.iterator();
        while (iterator.hasNext()) {
            Object storyItem = iterator.next();
            Field f = storyItem.getClass().getDeclaredField(reelTypeFieldName);
            Object storyType = f.get(storyItem);
            if (storyType == null) continue;
            String currentStoryType = storyType.toString();
            Logger.printInfo(() -> "Current story type :" + currentStoryType);

            if (BLOCKED_STORY_TYPES.contains(currentStoryType)) {
                Logger.printInfo(() -> "Removing story type:" + currentStoryType);
                iterator.remove();
            }
        }
        return patchedStoryList;
    }
}
