package nickrout.lenslauncher.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;

/**
 * Created by nicholasrout on 2016/05/28.
 */
public class AppSorter {

    public enum SORT_TYPE {
        LABEL_ASCENDING,
        LABEL_DESCENDING,
        INSTALL_DATE_ASCENDING,
        INSTALL_DATE_DESCENDING,
        OPEN_COUNT_ASCENDING,
        OPEN_COUNT_DESCENDING
    }

    public static void sort(ArrayList<App> apps, SORT_TYPE sortType) {
        switch (sortType) {
            case LABEL_ASCENDING:
                sortByLabelAscending(apps);
                break;
            case LABEL_DESCENDING:
                sortByLabelDescending(apps);
                break;
            case INSTALL_DATE_ASCENDING:
                sortByInstallDateAscending(apps);
                break;
            case INSTALL_DATE_DESCENDING:
                sortByInstallDateDescending(apps);
                break;
            case OPEN_COUNT_ASCENDING:
                sortByOpenCountAscending(apps);
                break;
            case OPEN_COUNT_DESCENDING:
                sortByOpenCountDescending(apps);
                break;
            default:

                break;
        }
    }

    public static void sortByLabelAscending(ArrayList<App> apps) {
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App a1, App a2) {
                return (a1.getLabel().toString()).compareToIgnoreCase(a2.getLabel().toString());
            }
        });
    }

    public static void sortByLabelDescending(ArrayList<App> apps) {
        sortByLabelAscending(apps);
        Collections.reverse(apps);
    }

    public static void sortByInstallDateAscending(ArrayList<App> apps) {
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App a1, App a2) {
                if (a1.getInstallDate() > a2.getInstallDate()) {
                    return -1;
                } else if (a1.getInstallDate() < a2.getInstallDate()) {
                    return +1;
                }
                return 0;
            }
        });
    }

    public static void sortByInstallDateDescending(ArrayList<App> apps) {
        sortByInstallDateAscending(apps);
        Collections.reverse(apps);
    }

    public static void sortByOpenCountAscending(ArrayList<App> apps) {
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App a1, App a2) {
                long a1OpenCount = AppPersistent.getOpenCountByPackageName(a1.getPackageName().toString());
                long a2OpenCount = AppPersistent.getOpenCountByPackageName(a2.getPackageName().toString());
                if (a1OpenCount > a2OpenCount) {
                    return -1;
                } else if (a1OpenCount < a2OpenCount) {
                    return +1;
                }
                return 0;
            }
        });
    }

    public static void sortByOpenCountDescending(ArrayList<App> apps) {
        sortByOpenCountAscending(apps);
        Collections.reverse(apps);
    }
}
