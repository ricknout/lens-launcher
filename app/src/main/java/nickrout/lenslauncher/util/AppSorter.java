package nickrout.lenslauncher.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;

/**
 * Created by nicholasrout on 2016/05/28.
 */
public class AppSorter {

    public enum SortType {
        LABEL_ASCENDING(R.string.setting_sort_type_label_ascending),
        LABEL_DESCENDING(R.string.setting_sort_type_label_descending),
        INSTALL_DATE_ASCENDING(R.string.setting_sort_type_install_date_ascending),
        INSTALL_DATE_DESCENDING(R.string.setting_sort_type_install_date_descending),
        OPEN_COUNT_ASCENDING(R.string.setting_sort_type_open_count_ascending),
        OPEN_COUNT_DESCENDING(R.string.setting_sort_type_open_count_descending);

        int mDisplayNameResId;

        SortType(int displayNameResId) {
            mDisplayNameResId = displayNameResId;
        }

        public int getDisplayNameResId() {
            return mDisplayNameResId;
        }
    }

    public static void sort(ArrayList<App> apps, SortType sortType) {
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
