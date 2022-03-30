package com.google.android.material.color;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;

public class MaterialColors {
    public static final float ALPHA_DISABLED = 0.38f;
    public static final float ALPHA_DISABLED_LOW = 0.12f;
    public static final float ALPHA_FULL = 1.0f;
    public static final float ALPHA_LOW = 0.32f;
    public static final float ALPHA_MEDIUM = 0.54f;

    private MaterialColors() {
    }

    public static int getColor(View view, int colorAttributeResId) {
        return MaterialAttributes.resolveOrThrow(view, colorAttributeResId);
    }

    public static int getColor(Context context, int colorAttributeResId, String errorMessageComponent) {
        return MaterialAttributes.resolveOrThrow(context, colorAttributeResId, errorMessageComponent);
    }

    public static int getColor(View view, int colorAttributeResId, int defaultValue) {
        return getColor(view.getContext(), colorAttributeResId, defaultValue);
    }

    public static int getColor(Context context, int colorAttributeResId, int defaultValue) {
        TypedValue typedValue = MaterialAttributes.resolve(context, colorAttributeResId);
        if (typedValue != null) {
            return typedValue.data;
        }
        return defaultValue;
    }

    public static int layer(View view, int backgroundColorAttributeResId, int overlayColorAttributeResId) {
        return layer(view, backgroundColorAttributeResId, overlayColorAttributeResId, 1.0f);
    }

    public static int layer(View view, int backgroundColorAttributeResId, int overlayColorAttributeResId, float overlayAlpha) {
        return layer(getColor(view, backgroundColorAttributeResId), getColor(view, overlayColorAttributeResId), overlayAlpha);
    }

    public static int layer(int backgroundColor, int overlayColor, float overlayAlpha) {
        return layer(backgroundColor, ColorUtils.setAlphaComponent(overlayColor, Math.round(((float) Color.alpha(overlayColor)) * overlayAlpha)));
    }

    public static int layer(int backgroundColor, int overlayColor) {
        return ColorUtils.compositeColors(overlayColor, backgroundColor);
    }

    public static int compositeARGBWithAlpha(int originalARGB, int alpha) {
        return ColorUtils.setAlphaComponent(originalARGB, (Color.alpha(originalARGB) * alpha) / 255);
    }
}
