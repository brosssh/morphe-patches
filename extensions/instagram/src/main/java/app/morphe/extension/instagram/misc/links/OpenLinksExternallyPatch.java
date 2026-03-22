package app.morphe.extension.instagram.misc.links;

import android.net.Uri;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.shared.Utils;

@SuppressWarnings("unused")
public final class OpenLinksExternallyPatch {

    /**
     * Injection point.
     */
    public static boolean openExternally(String url) {
        try {
            // The "url" parameter to this function will be of the form.
            // https://l.instagram.com/?u=<actual url>&e=<tracking id>
            String actualUrl = Uri.parse(url).getQueryParameter("u");
            if (actualUrl != null) {
                Utils.openLink(actualUrl);
                return true;
            }

        } catch (Exception ex) {
            Logger.printException(() -> "openExternally failure", ex);
        }

        return false;
    }
}
