package nickrout.lenslauncher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Pro {

    public static final boolean PRO = false;

    private static final String PRO_URL = "https://play.google.com/store/apps/details?id=nickrout.lenslauncherpro";

    public static void showPro(Context context) {
        Intent proIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRO_URL));
        context.startActivity(proIntent);
    }
}
