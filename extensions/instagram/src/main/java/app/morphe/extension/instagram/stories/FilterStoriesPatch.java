package app.morphe.extension.instagram.stories;


import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public class FilterStoriesPatch {

    public static List<Object> filterStories(
            List<Object> storiesList,
            String enumStoryTypeField
    ) throws IllegalAccessException, NoSuchFieldException {
        Iterator<Object> iterator = storiesList.iterator();

        while (iterator.hasNext()) {
            Object story = iterator.next();

            Field f = story.getClass().getDeclaredField(enumStoryTypeField);
            Object currentStory = f.get(story);
            if (currentStory == null) continue;

            String currentStoryType = currentStory.toString();
            Logger.printInfo(() -> "Current story type: " + currentStoryType);

            if (!currentStoryType.equals("user_reel")) {
                iterator.remove();
            }
        }

        return storiesList;
    }
}
