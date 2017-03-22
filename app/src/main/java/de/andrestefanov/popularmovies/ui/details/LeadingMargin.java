package de.andrestefanov.popularmovies.ui.details;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public class LeadingMargin implements LeadingMarginSpan.LeadingMarginSpan2 {

    private int marginSpace;

    private int numberOfLines;

    public LeadingMargin(int numberOfLines, int marginSpace) {
        this.marginSpace = marginSpace;
        this.numberOfLines = numberOfLines;
    }

    @Override
    public int getLeadingMarginLineCount() {
        return numberOfLines;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return first ? marginSpace : 0;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

    }

}
