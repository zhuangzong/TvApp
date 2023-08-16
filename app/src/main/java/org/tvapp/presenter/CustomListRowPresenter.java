package org.tvapp.presenter;

import androidx.leanback.widget.ListRowPresenter;

/**
 *  this class is used to remove the default selection effect
 */
public class CustomListRowPresenter extends ListRowPresenter {
    @Override
    public boolean isUsingDefaultListSelectEffect() {
        return false;
    }
}
