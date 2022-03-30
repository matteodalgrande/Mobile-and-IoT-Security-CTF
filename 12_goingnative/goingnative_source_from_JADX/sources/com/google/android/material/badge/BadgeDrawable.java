package com.google.android.material.badge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.C0089R;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;

public class BadgeDrawable extends Drawable implements TextDrawableHelper.TextDrawableDelegate {
    private static final int BADGE_NUMBER_NONE = -1;
    public static final int BOTTOM_END = 8388693;
    public static final int BOTTOM_START = 8388691;
    static final String DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX = "+";
    private static final int DEFAULT_MAX_BADGE_CHARACTER_COUNT = 4;
    private static final int DEFAULT_STYLE = C0089R.style.Widget_MaterialComponents_Badge;
    private static final int DEFAULT_THEME_ATTR = C0089R.attr.badgeStyle;
    private static final int MAX_CIRCULAR_BADGE_NUMBER_COUNT = 9;
    public static final int TOP_END = 8388661;
    public static final int TOP_START = 8388659;
    private WeakReference<View> anchorViewRef;
    private final Rect badgeBounds = new Rect();
    private float badgeCenterX;
    private float badgeCenterY;
    private final float badgeRadius;
    private final float badgeWidePadding;
    private final float badgeWithTextRadius;
    private final WeakReference<Context> contextRef;
    private float cornerRadius;
    private WeakReference<FrameLayout> customBadgeParentRef;
    private float halfBadgeHeight;
    private float halfBadgeWidth;
    private int maxBadgeNumber;
    private final SavedState savedState;
    private final MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable();
    private final TextDrawableHelper textDrawableHelper;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BadgeGravity {
    }

    public static final class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        /* access modifiers changed from: private */
        public int alpha = 255;
        /* access modifiers changed from: private */
        public int backgroundColor;
        /* access modifiers changed from: private */
        public int badgeGravity;
        /* access modifiers changed from: private */
        public int badgeTextColor;
        /* access modifiers changed from: private */
        public int contentDescriptionExceedsMaxBadgeNumberRes;
        /* access modifiers changed from: private */
        public CharSequence contentDescriptionNumberless;
        /* access modifiers changed from: private */
        public int contentDescriptionQuantityStrings;
        /* access modifiers changed from: private */
        public int horizontalOffset;
        /* access modifiers changed from: private */
        public boolean isVisible;
        /* access modifiers changed from: private */
        public int maxCharacterCount;
        /* access modifiers changed from: private */
        public int number = -1;
        /* access modifiers changed from: private */
        public int verticalOffset;

        public SavedState(Context context) {
            this.badgeTextColor = new TextAppearance(context, C0089R.style.TextAppearance_MaterialComponents_Badge).textColor.getDefaultColor();
            this.contentDescriptionNumberless = context.getString(C0089R.string.mtrl_badge_numberless_content_description);
            this.contentDescriptionQuantityStrings = C0089R.plurals.mtrl_badge_content_description;
            this.contentDescriptionExceedsMaxBadgeNumberRes = C0089R.string.mtrl_exceed_max_badge_number_content_description;
            this.isVisible = true;
        }

        protected SavedState(Parcel in) {
            this.backgroundColor = in.readInt();
            this.badgeTextColor = in.readInt();
            this.alpha = in.readInt();
            this.number = in.readInt();
            this.maxCharacterCount = in.readInt();
            this.contentDescriptionNumberless = in.readString();
            this.contentDescriptionQuantityStrings = in.readInt();
            this.badgeGravity = in.readInt();
            this.horizontalOffset = in.readInt();
            this.verticalOffset = in.readInt();
            this.isVisible = in.readInt() != 0;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.backgroundColor);
            dest.writeInt(this.badgeTextColor);
            dest.writeInt(this.alpha);
            dest.writeInt(this.number);
            dest.writeInt(this.maxCharacterCount);
            dest.writeString(this.contentDescriptionNumberless.toString());
            dest.writeInt(this.contentDescriptionQuantityStrings);
            dest.writeInt(this.badgeGravity);
            dest.writeInt(this.horizontalOffset);
            dest.writeInt(this.verticalOffset);
            dest.writeInt(this.isVisible ? 1 : 0);
        }
    }

    public SavedState getSavedState() {
        return this.savedState;
    }

    static BadgeDrawable createFromSavedState(Context context, SavedState savedState2) {
        BadgeDrawable badge = new BadgeDrawable(context);
        badge.restoreFromSavedState(savedState2);
        return badge;
    }

    public static BadgeDrawable create(Context context) {
        return createFromAttributes(context, (AttributeSet) null, DEFAULT_THEME_ATTR, DEFAULT_STYLE);
    }

    public static BadgeDrawable createFromResource(Context context, int id) {
        AttributeSet attrs = DrawableUtils.parseDrawableXml(context, id, "badge");
        int style = attrs.getStyleAttribute();
        if (style == 0) {
            style = DEFAULT_STYLE;
        }
        return createFromAttributes(context, attrs, DEFAULT_THEME_ATTR, style);
    }

    private static BadgeDrawable createFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        BadgeDrawable badge = new BadgeDrawable(context);
        badge.loadDefaultStateFromAttributes(context, attrs, defStyleAttr, defStyleRes);
        return badge;
    }

    public void setVisible(boolean visible) {
        setVisible(visible, false);
        boolean unused = this.savedState.isVisible = visible;
        if (BadgeUtils.USE_COMPAT_PARENT && getCustomBadgeParent() != null && !visible) {
            ((ViewGroup) getCustomBadgeParent().getParent()).invalidate();
        }
    }

    private void restoreFromSavedState(SavedState savedState2) {
        setMaxCharacterCount(savedState2.maxCharacterCount);
        if (savedState2.number != -1) {
            setNumber(savedState2.number);
        }
        setBackgroundColor(savedState2.backgroundColor);
        setBadgeTextColor(savedState2.badgeTextColor);
        setBadgeGravity(savedState2.badgeGravity);
        setHorizontalOffset(savedState2.horizontalOffset);
        setVerticalOffset(savedState2.verticalOffset);
        setVisible(savedState2.isVisible);
    }

    private void loadDefaultStateFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = ThemeEnforcement.obtainStyledAttributes(context, attrs, C0089R.styleable.Badge, defStyleAttr, defStyleRes, new int[0]);
        setMaxCharacterCount(a.getInt(C0089R.styleable.Badge_maxCharacterCount, 4));
        if (a.hasValue(C0089R.styleable.Badge_number)) {
            setNumber(a.getInt(C0089R.styleable.Badge_number, 0));
        }
        setBackgroundColor(readColorFromAttributes(context, a, C0089R.styleable.Badge_backgroundColor));
        if (a.hasValue(C0089R.styleable.Badge_badgeTextColor)) {
            setBadgeTextColor(readColorFromAttributes(context, a, C0089R.styleable.Badge_badgeTextColor));
        }
        setBadgeGravity(a.getInt(C0089R.styleable.Badge_badgeGravity, TOP_END));
        setHorizontalOffset(a.getDimensionPixelOffset(C0089R.styleable.Badge_horizontalOffset, 0));
        setVerticalOffset(a.getDimensionPixelOffset(C0089R.styleable.Badge_verticalOffset, 0));
        a.recycle();
    }

    private static int readColorFromAttributes(Context context, TypedArray a, int index) {
        return MaterialResources.getColorStateList(context, a, index).getDefaultColor();
    }

    private BadgeDrawable(Context context) {
        this.contextRef = new WeakReference<>(context);
        ThemeEnforcement.checkMaterialTheme(context);
        Resources res = context.getResources();
        this.badgeRadius = (float) res.getDimensionPixelSize(C0089R.dimen.mtrl_badge_radius);
        this.badgeWidePadding = (float) res.getDimensionPixelSize(C0089R.dimen.mtrl_badge_long_text_horizontal_padding);
        this.badgeWithTextRadius = (float) res.getDimensionPixelSize(C0089R.dimen.mtrl_badge_with_text_radius);
        TextDrawableHelper textDrawableHelper2 = new TextDrawableHelper(this);
        this.textDrawableHelper = textDrawableHelper2;
        textDrawableHelper2.getTextPaint().setTextAlign(Paint.Align.CENTER);
        this.savedState = new SavedState(context);
        setTextAppearanceResource(C0089R.style.TextAppearance_MaterialComponents_Badge);
    }

    @Deprecated
    public void updateBadgeCoordinates(View anchorView, ViewGroup customBadgeParent) {
        if (customBadgeParent instanceof FrameLayout) {
            updateBadgeCoordinates(anchorView, (FrameLayout) customBadgeParent);
            return;
        }
        throw new IllegalArgumentException("customBadgeParent must be a FrameLayout");
    }

    public void updateBadgeCoordinates(View anchorView) {
        updateBadgeCoordinates(anchorView, (FrameLayout) null);
    }

    public void updateBadgeCoordinates(View anchorView, FrameLayout customBadgeParent) {
        this.anchorViewRef = new WeakReference<>(anchorView);
        if (!BadgeUtils.USE_COMPAT_PARENT || customBadgeParent != null) {
            this.customBadgeParentRef = new WeakReference<>(customBadgeParent);
        } else {
            tryWrapAnchorInCompatParent(anchorView);
        }
        if (!BadgeUtils.USE_COMPAT_PARENT) {
            updateAnchorParentToNotClip(anchorView);
        }
        updateCenterAndBounds();
        invalidateSelf();
    }

    public FrameLayout getCustomBadgeParent() {
        WeakReference<FrameLayout> weakReference = this.customBadgeParentRef;
        if (weakReference != null) {
            return (FrameLayout) weakReference.get();
        }
        return null;
    }

    private void tryWrapAnchorInCompatParent(final View anchorView) {
        ViewGroup anchorViewParent = (ViewGroup) anchorView.getParent();
        if (anchorViewParent == null || anchorViewParent.getId() != C0089R.C0092id.mtrl_anchor_parent) {
            WeakReference<FrameLayout> weakReference = this.customBadgeParentRef;
            if (weakReference == null || weakReference.get() != anchorViewParent) {
                updateAnchorParentToNotClip(anchorView);
                final FrameLayout frameLayout = new FrameLayout(anchorView.getContext());
                frameLayout.setId(C0089R.C0092id.mtrl_anchor_parent);
                frameLayout.setClipChildren(false);
                frameLayout.setClipToPadding(false);
                frameLayout.setLayoutParams(anchorView.getLayoutParams());
                frameLayout.setMinimumWidth(anchorView.getWidth());
                frameLayout.setMinimumHeight(anchorView.getHeight());
                int anchorIndex = anchorViewParent.indexOfChild(anchorView);
                anchorViewParent.removeViewAt(anchorIndex);
                anchorView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                frameLayout.addView(anchorView);
                anchorViewParent.addView(frameLayout, anchorIndex);
                this.customBadgeParentRef = new WeakReference<>(frameLayout);
                frameLayout.post(new Runnable() {
                    public void run() {
                        BadgeDrawable.this.updateBadgeCoordinates(anchorView, frameLayout);
                    }
                });
            }
        }
    }

    private static void updateAnchorParentToNotClip(View anchorView) {
        ViewGroup anchorViewParent = (ViewGroup) anchorView.getParent();
        anchorViewParent.setClipChildren(false);
        anchorViewParent.setClipToPadding(false);
    }

    public int getBackgroundColor() {
        return this.shapeDrawable.getFillColor().getDefaultColor();
    }

    public void setBackgroundColor(int backgroundColor) {
        int unused = this.savedState.backgroundColor = backgroundColor;
        ColorStateList backgroundColorStateList = ColorStateList.valueOf(backgroundColor);
        if (this.shapeDrawable.getFillColor() != backgroundColorStateList) {
            this.shapeDrawable.setFillColor(backgroundColorStateList);
            invalidateSelf();
        }
    }

    public int getBadgeTextColor() {
        return this.textDrawableHelper.getTextPaint().getColor();
    }

    public void setBadgeTextColor(int badgeTextColor) {
        int unused = this.savedState.badgeTextColor = badgeTextColor;
        if (this.textDrawableHelper.getTextPaint().getColor() != badgeTextColor) {
            this.textDrawableHelper.getTextPaint().setColor(badgeTextColor);
            invalidateSelf();
        }
    }

    public boolean hasNumber() {
        return this.savedState.number != -1;
    }

    public int getNumber() {
        if (!hasNumber()) {
            return 0;
        }
        return this.savedState.number;
    }

    public void setNumber(int number) {
        int number2 = Math.max(0, number);
        if (this.savedState.number != number2) {
            int unused = this.savedState.number = number2;
            this.textDrawableHelper.setTextWidthDirty(true);
            updateCenterAndBounds();
            invalidateSelf();
        }
    }

    public void clearNumber() {
        int unused = this.savedState.number = -1;
        invalidateSelf();
    }

    public int getMaxCharacterCount() {
        return this.savedState.maxCharacterCount;
    }

    public void setMaxCharacterCount(int maxCharacterCount) {
        if (this.savedState.maxCharacterCount != maxCharacterCount) {
            int unused = this.savedState.maxCharacterCount = maxCharacterCount;
            updateMaxBadgeNumber();
            this.textDrawableHelper.setTextWidthDirty(true);
            updateCenterAndBounds();
            invalidateSelf();
        }
    }

    public int getBadgeGravity() {
        return this.savedState.badgeGravity;
    }

    public void setBadgeGravity(int gravity) {
        if (this.savedState.badgeGravity != gravity) {
            int unused = this.savedState.badgeGravity = gravity;
            WeakReference<View> weakReference = this.anchorViewRef;
            if (weakReference != null && weakReference.get() != null) {
                View view = (View) this.anchorViewRef.get();
                WeakReference<FrameLayout> weakReference2 = this.customBadgeParentRef;
                updateBadgeCoordinates(view, weakReference2 != null ? (FrameLayout) weakReference2.get() : null);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public int getAlpha() {
        return this.savedState.alpha;
    }

    public void setAlpha(int alpha) {
        int unused = this.savedState.alpha = alpha;
        this.textDrawableHelper.getTextPaint().setAlpha(alpha);
        invalidateSelf();
    }

    public int getOpacity() {
        return -3;
    }

    public int getIntrinsicHeight() {
        return this.badgeBounds.height();
    }

    public int getIntrinsicWidth() {
        return this.badgeBounds.width();
    }

    public void draw(Canvas canvas) {
        if (!getBounds().isEmpty() && getAlpha() != 0 && isVisible()) {
            this.shapeDrawable.draw(canvas);
            if (hasNumber()) {
                drawText(canvas);
            }
        }
    }

    public void onTextSizeChange() {
        invalidateSelf();
    }

    public boolean onStateChange(int[] state) {
        return super.onStateChange(state);
    }

    public void setContentDescriptionNumberless(CharSequence charSequence) {
        CharSequence unused = this.savedState.contentDescriptionNumberless = charSequence;
    }

    public void setContentDescriptionQuantityStringsResource(int stringsResource) {
        int unused = this.savedState.contentDescriptionQuantityStrings = stringsResource;
    }

    public void setContentDescriptionExceedsMaxBadgeNumberStringResource(int stringsResource) {
        int unused = this.savedState.contentDescriptionExceedsMaxBadgeNumberRes = stringsResource;
    }

    public CharSequence getContentDescription() {
        Context context;
        if (!isVisible()) {
            return null;
        }
        if (!hasNumber()) {
            return this.savedState.contentDescriptionNumberless;
        }
        if (this.savedState.contentDescriptionQuantityStrings <= 0 || (context = (Context) this.contextRef.get()) == null) {
            return null;
        }
        if (getNumber() <= this.maxBadgeNumber) {
            return context.getResources().getQuantityString(this.savedState.contentDescriptionQuantityStrings, getNumber(), new Object[]{Integer.valueOf(getNumber())});
        }
        return context.getString(this.savedState.contentDescriptionExceedsMaxBadgeNumberRes, new Object[]{Integer.valueOf(this.maxBadgeNumber)});
    }

    public void setHorizontalOffset(int px) {
        int unused = this.savedState.horizontalOffset = px;
        updateCenterAndBounds();
    }

    public int getHorizontalOffset() {
        return this.savedState.horizontalOffset;
    }

    public void setVerticalOffset(int px) {
        int unused = this.savedState.verticalOffset = px;
        updateCenterAndBounds();
    }

    public int getVerticalOffset() {
        return this.savedState.verticalOffset;
    }

    private void setTextAppearanceResource(int id) {
        Context context = (Context) this.contextRef.get();
        if (context != null) {
            setTextAppearance(new TextAppearance(context, id));
        }
    }

    private void setTextAppearance(TextAppearance textAppearance) {
        Context context;
        if (this.textDrawableHelper.getTextAppearance() != textAppearance && (context = (Context) this.contextRef.get()) != null) {
            this.textDrawableHelper.setTextAppearance(textAppearance, context);
            updateCenterAndBounds();
        }
    }

    private void updateCenterAndBounds() {
        Context context = (Context) this.contextRef.get();
        WeakReference<View> weakReference = this.anchorViewRef;
        ViewGroup customBadgeParent = null;
        View anchorView = weakReference != null ? (View) weakReference.get() : null;
        if (context != null && anchorView != null) {
            Rect tmpRect = new Rect();
            tmpRect.set(this.badgeBounds);
            Rect anchorRect = new Rect();
            anchorView.getDrawingRect(anchorRect);
            WeakReference<FrameLayout> weakReference2 = this.customBadgeParentRef;
            if (weakReference2 != null) {
                customBadgeParent = (FrameLayout) weakReference2.get();
            }
            if (customBadgeParent != null || BadgeUtils.USE_COMPAT_PARENT) {
                (customBadgeParent == null ? (ViewGroup) anchorView.getParent() : customBadgeParent).offsetDescendantRectToMyCoords(anchorView, anchorRect);
            }
            calculateCenterAndBounds(context, anchorRect, anchorView);
            BadgeUtils.updateBadgeBounds(this.badgeBounds, this.badgeCenterX, this.badgeCenterY, this.halfBadgeWidth, this.halfBadgeHeight);
            this.shapeDrawable.setCornerSize(this.cornerRadius);
            if (!tmpRect.equals(this.badgeBounds)) {
                this.shapeDrawable.setBounds(this.badgeBounds);
            }
        }
    }

    private void calculateCenterAndBounds(Context context, Rect anchorRect, View anchorView) {
        float f;
        float f2;
        int access$500 = this.savedState.badgeGravity;
        if (access$500 == 8388691 || access$500 == 8388693) {
            this.badgeCenterY = (float) (anchorRect.bottom - this.savedState.verticalOffset);
        } else {
            this.badgeCenterY = (float) (anchorRect.top + this.savedState.verticalOffset);
        }
        if (getNumber() <= 9) {
            float f3 = !hasNumber() ? this.badgeRadius : this.badgeWithTextRadius;
            this.cornerRadius = f3;
            this.halfBadgeHeight = f3;
            this.halfBadgeWidth = f3;
        } else {
            float f4 = this.badgeWithTextRadius;
            this.cornerRadius = f4;
            this.halfBadgeHeight = f4;
            this.halfBadgeWidth = (this.textDrawableHelper.getTextWidth(getBadgeText()) / 2.0f) + this.badgeWidePadding;
        }
        int inset = context.getResources().getDimensionPixelSize(hasNumber() ? C0089R.dimen.mtrl_badge_text_horizontal_edge_offset : C0089R.dimen.mtrl_badge_horizontal_edge_offset);
        int access$5002 = this.savedState.badgeGravity;
        if (access$5002 == 8388659 || access$5002 == 8388691) {
            if (ViewCompat.getLayoutDirection(anchorView) == 0) {
                f = (((float) anchorRect.left) - this.halfBadgeWidth) + ((float) inset) + ((float) this.savedState.horizontalOffset);
            } else {
                f = ((((float) anchorRect.right) + this.halfBadgeWidth) - ((float) inset)) - ((float) this.savedState.horizontalOffset);
            }
            this.badgeCenterX = f;
            return;
        }
        if (ViewCompat.getLayoutDirection(anchorView) == 0) {
            f2 = ((((float) anchorRect.right) + this.halfBadgeWidth) - ((float) inset)) - ((float) this.savedState.horizontalOffset);
        } else {
            f2 = (((float) anchorRect.left) - this.halfBadgeWidth) + ((float) inset) + ((float) this.savedState.horizontalOffset);
        }
        this.badgeCenterX = f2;
    }

    private void drawText(Canvas canvas) {
        Rect textBounds = new Rect();
        String badgeText = getBadgeText();
        this.textDrawableHelper.getTextPaint().getTextBounds(badgeText, 0, badgeText.length(), textBounds);
        canvas.drawText(badgeText, this.badgeCenterX, this.badgeCenterY + ((float) (textBounds.height() / 2)), this.textDrawableHelper.getTextPaint());
    }

    private String getBadgeText() {
        if (getNumber() <= this.maxBadgeNumber) {
            return NumberFormat.getInstance().format((long) getNumber());
        }
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return "";
        }
        return context.getString(C0089R.string.mtrl_exceed_max_badge_number_suffix, new Object[]{Integer.valueOf(this.maxBadgeNumber), DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX});
    }

    private void updateMaxBadgeNumber() {
        this.maxBadgeNumber = ((int) Math.pow(10.0d, ((double) getMaxCharacterCount()) - 1.0d)) - 1;
    }
}
