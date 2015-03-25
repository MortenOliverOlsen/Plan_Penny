package morten.plan_penny.Main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by morten on 3/25/15.
 */
public class FragmentLayout extends LinearLayout {

    Float xFraction;

    public FragmentLayout(Context context) {
        super(context);
    }

    public FragmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setXFraction(final float fraction) {
        float translationX = getWidth() * fraction;
        setTranslationX(translationX);
    }

    public float getXFraction() {
        if (getWidth() == 0) {
            return 0;
        }
        return getTranslationX() / getWidth();
    }
}
