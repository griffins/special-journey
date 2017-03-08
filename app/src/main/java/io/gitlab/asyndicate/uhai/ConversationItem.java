package io.gitlab.asyndicate.uhai;

import android.graphics.drawable.Drawable;

import to.us.datagrip.textdrawable.TextDrawable;
import to.us.datagrip.textdrawable.util.ColorGenerator;

public class ConversationItem implements GenericListItemInterface {
    public String title, peek;
    private PayloadRunnable action;
    private int data;

    @Override
    public String getPrimaryText() {
        return getTitle();
    }

    @Override
    public String getSecondaryText() {
        return getLineTotal();
    }

    @Override
    public Drawable getPrimaryImage() {
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
        String img = String.valueOf(getPrimaryText().charAt(0));
        Drawable drawable = mDrawableBuilder.build(img, mColorGenerator.getColor(img));
        return drawable;
    }

    @Override
    public PayloadRunnable getAction() {
        return action;
    }

    public void setAction(PayloadRunnable action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLineTotal() {
        return peek;
    }

    public void setPeek(String lineTotal) {
        this.peek = lineTotal;
    }

    public int getFlags() {
        return data;
    }

    public void setFlags(int data) {
        this.data = data;
    }
}
