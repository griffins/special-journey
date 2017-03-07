package io.gitlab.asyndicate.uhai;

import android.graphics.drawable.Drawable;

public interface GenericListItemInterface {
    String getPrimaryText();
    String getSecondaryText();
    Drawable getPrimaryImage();
    PayloadRunnable getAction();
}
