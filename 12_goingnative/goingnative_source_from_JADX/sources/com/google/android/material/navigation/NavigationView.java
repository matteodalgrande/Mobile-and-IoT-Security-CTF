package com.google.android.material.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.appcompat.C0005R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TintTypedArray;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.C0089R;
import com.google.android.material.internal.ContextUtils;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;

public class NavigationView extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int DEF_STYLE_RES = C0089R.style.Widget_Design_NavigationView;
    private static final int[] DISABLED_STATE_SET = {-16842910};
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    OnNavigationItemSelectedListener listener;
    private final int maxWidth;
    private final NavigationMenu menu;
    private MenuInflater menuInflater;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    /* access modifiers changed from: private */
    public final NavigationMenuPresenter presenter;
    /* access modifiers changed from: private */
    public final int[] tmpLocation;

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    public NavigationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, C0089R.attr.navigationViewStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NavigationView(android.content.Context r13, android.util.AttributeSet r14, int r15) {
        /*
            r12 = this;
            int r6 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r13, r14, r15, r6)
            r12.<init>(r0, r14, r15)
            com.google.android.material.internal.NavigationMenuPresenter r7 = new com.google.android.material.internal.NavigationMenuPresenter
            r7.<init>()
            r12.presenter = r7
            r0 = 2
            int[] r0 = new int[r0]
            r12.tmpLocation = r0
            android.content.Context r13 = r12.getContext()
            com.google.android.material.internal.NavigationMenu r8 = new com.google.android.material.internal.NavigationMenu
            r8.<init>(r13)
            r12.menu = r8
            int[] r2 = com.google.android.material.C0089R.styleable.NavigationView
            r9 = 0
            int[] r5 = new int[r9]
            r0 = r13
            r1 = r14
            r3 = r15
            r4 = r6
            androidx.appcompat.widget.TintTypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainTintedStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_android_background
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x003e
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_android_background
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r1)
            androidx.core.view.ViewCompat.setBackground(r12, r1)
        L_0x003e:
            android.graphics.drawable.Drawable r1 = r12.getBackground()
            if (r1 == 0) goto L_0x004c
            android.graphics.drawable.Drawable r1 = r12.getBackground()
            boolean r1 = r1 instanceof android.graphics.drawable.ColorDrawable
            if (r1 == 0) goto L_0x0076
        L_0x004c:
            com.google.android.material.shape.ShapeAppearanceModel$Builder r1 = com.google.android.material.shape.ShapeAppearanceModel.builder((android.content.Context) r13, (android.util.AttributeSet) r14, (int) r15, (int) r6)
            com.google.android.material.shape.ShapeAppearanceModel r1 = r1.build()
            android.graphics.drawable.Drawable r2 = r12.getBackground()
            com.google.android.material.shape.MaterialShapeDrawable r3 = new com.google.android.material.shape.MaterialShapeDrawable
            r3.<init>((com.google.android.material.shape.ShapeAppearanceModel) r1)
            boolean r4 = r2 instanceof android.graphics.drawable.ColorDrawable
            if (r4 == 0) goto L_0x0070
            r4 = r2
            android.graphics.drawable.ColorDrawable r4 = (android.graphics.drawable.ColorDrawable) r4
            int r4 = r4.getColor()
            android.content.res.ColorStateList r4 = android.content.res.ColorStateList.valueOf(r4)
            r3.setFillColor(r4)
        L_0x0070:
            r3.initializeElevationOverlay(r13)
            androidx.core.view.ViewCompat.setBackground(r12, r3)
        L_0x0076:
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_elevation
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0088
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_elevation
            int r1 = r0.getDimensionPixelSize(r1, r9)
            float r1 = (float) r1
            r12.setElevation(r1)
        L_0x0088:
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_android_fitsSystemWindows
            boolean r1 = r0.getBoolean(r1, r9)
            r12.setFitsSystemWindows(r1)
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_android_maxWidth
            int r1 = r0.getDimensionPixelSize(r1, r9)
            r12.maxWidth = r1
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_itemIconTint
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00a8
            int r1 = com.google.android.material.C0089R.styleable.NavigationView_itemIconTint
            android.content.res.ColorStateList r1 = r0.getColorStateList(r1)
            goto L_0x00af
        L_0x00a8:
            r1 = 16842808(0x1010038, float:2.3693715E-38)
            android.content.res.ColorStateList r1 = r12.createDefaultColorStateList(r1)
        L_0x00af:
            r2 = 0
            r3 = 0
            int r4 = com.google.android.material.C0089R.styleable.NavigationView_itemTextAppearance
            boolean r4 = r0.hasValue(r4)
            if (r4 == 0) goto L_0x00c0
            int r4 = com.google.android.material.C0089R.styleable.NavigationView_itemTextAppearance
            int r3 = r0.getResourceId(r4, r9)
            r2 = 1
        L_0x00c0:
            int r4 = com.google.android.material.C0089R.styleable.NavigationView_itemIconSize
            boolean r4 = r0.hasValue(r4)
            if (r4 == 0) goto L_0x00d1
            int r4 = com.google.android.material.C0089R.styleable.NavigationView_itemIconSize
            int r4 = r0.getDimensionPixelSize(r4, r9)
            r12.setItemIconSize(r4)
        L_0x00d1:
            r4 = 0
            int r5 = com.google.android.material.C0089R.styleable.NavigationView_itemTextColor
            boolean r5 = r0.hasValue(r5)
            if (r5 == 0) goto L_0x00e0
            int r5 = com.google.android.material.C0089R.styleable.NavigationView_itemTextColor
            android.content.res.ColorStateList r4 = r0.getColorStateList(r5)
        L_0x00e0:
            if (r2 != 0) goto L_0x00eb
            if (r4 != 0) goto L_0x00eb
            r5 = 16842806(0x1010036, float:2.369371E-38)
            android.content.res.ColorStateList r4 = r12.createDefaultColorStateList(r5)
        L_0x00eb:
            int r5 = com.google.android.material.C0089R.styleable.NavigationView_itemBackground
            android.graphics.drawable.Drawable r5 = r0.getDrawable(r5)
            if (r5 != 0) goto L_0x00fd
            boolean r6 = r12.hasShapeAppearance(r0)
            if (r6 == 0) goto L_0x00fd
            android.graphics.drawable.Drawable r5 = r12.createDefaultItemBackground(r0)
        L_0x00fd:
            int r6 = com.google.android.material.C0089R.styleable.NavigationView_itemHorizontalPadding
            boolean r6 = r0.hasValue(r6)
            if (r6 == 0) goto L_0x010e
            int r6 = com.google.android.material.C0089R.styleable.NavigationView_itemHorizontalPadding
            int r6 = r0.getDimensionPixelSize(r6, r9)
            r7.setItemHorizontalPadding(r6)
        L_0x010e:
            int r6 = com.google.android.material.C0089R.styleable.NavigationView_itemIconPadding
            int r6 = r0.getDimensionPixelSize(r6, r9)
            int r10 = com.google.android.material.C0089R.styleable.NavigationView_itemMaxLines
            r11 = 1
            int r10 = r0.getInt(r10, r11)
            r12.setItemMaxLines(r10)
            com.google.android.material.navigation.NavigationView$1 r10 = new com.google.android.material.navigation.NavigationView$1
            r10.<init>()
            r8.setCallback(r10)
            r7.setId(r11)
            r7.initForMenu(r13, r8)
            r7.setItemIconTintList(r1)
            int r10 = r12.getOverScrollMode()
            r7.setOverScrollMode(r10)
            if (r2 == 0) goto L_0x013b
            r7.setItemTextAppearance(r3)
        L_0x013b:
            r7.setItemTextColor(r4)
            r7.setItemBackground(r5)
            r7.setItemIconPadding(r6)
            r8.addMenuPresenter(r7)
            androidx.appcompat.view.menu.MenuView r7 = r7.getMenuView(r12)
            android.view.View r7 = (android.view.View) r7
            r12.addView(r7)
            int r7 = com.google.android.material.C0089R.styleable.NavigationView_menu
            boolean r7 = r0.hasValue(r7)
            if (r7 == 0) goto L_0x0161
            int r7 = com.google.android.material.C0089R.styleable.NavigationView_menu
            int r7 = r0.getResourceId(r7, r9)
            r12.inflateMenu(r7)
        L_0x0161:
            int r7 = com.google.android.material.C0089R.styleable.NavigationView_headerLayout
            boolean r7 = r0.hasValue(r7)
            if (r7 == 0) goto L_0x0172
            int r7 = com.google.android.material.C0089R.styleable.NavigationView_headerLayout
            int r7 = r0.getResourceId(r7, r9)
            r12.inflateHeaderView(r7)
        L_0x0172:
            r0.recycle()
            r12.setupInsetScrimsListener()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.navigation.NavigationView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    public void setOverScrollMode(int overScrollMode) {
        super.setOverScrollMode(overScrollMode);
        NavigationMenuPresenter navigationMenuPresenter = this.presenter;
        if (navigationMenuPresenter != null) {
            navigationMenuPresenter.setOverScrollMode(overScrollMode);
        }
    }

    private boolean hasShapeAppearance(TintTypedArray a) {
        return a.hasValue(C0089R.styleable.NavigationView_itemShapeAppearance) || a.hasValue(C0089R.styleable.NavigationView_itemShapeAppearanceOverlay);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    public void setElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.setElevation(elevation);
        }
        MaterialShapeUtils.setElevation(this, elevation);
    }

    private final Drawable createDefaultItemBackground(TintTypedArray a) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(ShapeAppearanceModel.builder(getContext(), a.getResourceId(C0089R.styleable.NavigationView_itemShapeAppearance, 0), a.getResourceId(C0089R.styleable.NavigationView_itemShapeAppearanceOverlay, 0)).build());
        materialShapeDrawable.setFillColor(MaterialResources.getColorStateList(getContext(), a, C0089R.styleable.NavigationView_itemShapeFillColor));
        return new InsetDrawable(materialShapeDrawable, a.getDimensionPixelSize(C0089R.styleable.NavigationView_itemShapeInsetStart, 0), a.getDimensionPixelSize(C0089R.styleable.NavigationView_itemShapeInsetTop, 0), a.getDimensionPixelSize(C0089R.styleable.NavigationView_itemShapeInsetEnd, 0), a.getDimensionPixelSize(C0089R.styleable.NavigationView_itemShapeInsetBottom, 0));
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.menuState = new Bundle();
        this.menu.savePresenterStates(state.menuState);
        return state;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable savedState) {
        if (!(savedState instanceof SavedState)) {
            super.onRestoreInstanceState(savedState);
            return;
        }
        SavedState state = (SavedState) savedState;
        super.onRestoreInstanceState(state.getSuperState());
        this.menu.restorePresenterStates(state.menuState);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener2) {
        this.listener = listener2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthSpec, int heightSpec) {
        int mode = View.MeasureSpec.getMode(widthSpec);
        if (mode == Integer.MIN_VALUE) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(widthSpec), this.maxWidth), BasicMeasure.EXACTLY);
        } else if (mode == 0) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(this.maxWidth, BasicMeasure.EXACTLY);
        }
        super.onMeasure(widthSpec, heightSpec);
    }

    /* access modifiers changed from: protected */
    public void onInsetsChanged(WindowInsetsCompat insets) {
        this.presenter.dispatchApplyWindowInsets(insets);
    }

    public void inflateMenu(int resId) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(resId, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(false);
    }

    public Menu getMenu() {
        return this.menu;
    }

    public View inflateHeaderView(int res) {
        return this.presenter.inflateHeaderView(res);
    }

    public void addHeaderView(View view) {
        this.presenter.addHeaderView(view);
    }

    public void removeHeaderView(View view) {
        this.presenter.removeHeaderView(view);
    }

    public int getHeaderCount() {
        return this.presenter.getHeaderCount();
    }

    public View getHeaderView(int index) {
        return this.presenter.getHeaderView(index);
    }

    public ColorStateList getItemIconTintList() {
        return this.presenter.getItemTintList();
    }

    public void setItemIconTintList(ColorStateList tint) {
        this.presenter.setItemIconTintList(tint);
    }

    public ColorStateList getItemTextColor() {
        return this.presenter.getItemTextColor();
    }

    public void setItemTextColor(ColorStateList textColor) {
        this.presenter.setItemTextColor(textColor);
    }

    public Drawable getItemBackground() {
        return this.presenter.getItemBackground();
    }

    public void setItemBackgroundResource(int resId) {
        setItemBackground(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setItemBackground(Drawable itemBackground) {
        this.presenter.setItemBackground(itemBackground);
    }

    public int getItemHorizontalPadding() {
        return this.presenter.getItemHorizontalPadding();
    }

    public void setItemHorizontalPadding(int padding) {
        this.presenter.setItemHorizontalPadding(padding);
    }

    public void setItemHorizontalPaddingResource(int paddingResource) {
        this.presenter.setItemHorizontalPadding(getResources().getDimensionPixelSize(paddingResource));
    }

    public int getItemIconPadding() {
        return this.presenter.getItemIconPadding();
    }

    public void setItemIconPadding(int padding) {
        this.presenter.setItemIconPadding(padding);
    }

    public void setItemIconPaddingResource(int paddingResource) {
        this.presenter.setItemIconPadding(getResources().getDimensionPixelSize(paddingResource));
    }

    public void setCheckedItem(int id) {
        MenuItem item = this.menu.findItem(id);
        if (item != null) {
            this.presenter.setCheckedItem((MenuItemImpl) item);
        }
    }

    public void setCheckedItem(MenuItem checkedItem) {
        MenuItem item = this.menu.findItem(checkedItem.getItemId());
        if (item != null) {
            this.presenter.setCheckedItem((MenuItemImpl) item);
            return;
        }
        throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
    }

    public MenuItem getCheckedItem() {
        return this.presenter.getCheckedItem();
    }

    public void setItemTextAppearance(int resId) {
        this.presenter.setItemTextAppearance(resId);
    }

    public void setItemIconSize(int iconSize) {
        this.presenter.setItemIconSize(iconSize);
    }

    public void setItemMaxLines(int itemMaxLines) {
        this.presenter.setItemMaxLines(itemMaxLines);
    }

    public int getItemMaxLines() {
        return this.presenter.getItemMaxLines();
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = AppCompatResources.getColorStateList(getContext(), value.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0005R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        int defaultColor = baseColor.getDefaultColor();
        int[] iArr = DISABLED_STATE_SET;
        return new ColorStateList(new int[][]{iArr, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{baseColor.getColorForState(iArr, defaultColor), colorPrimary, defaultColor});
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT < 16) {
            getViewTreeObserver().removeGlobalOnLayoutListener(this.onGlobalLayoutListener);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
    }

    private void setupInsetScrimsListener() {
        this.onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                NavigationView navigationView = NavigationView.this;
                navigationView.getLocationOnScreen(navigationView.tmpLocation);
                boolean z = true;
                boolean isBehindStatusBar = NavigationView.this.tmpLocation[1] == 0;
                NavigationView.this.presenter.setBehindStatusBar(isBehindStatusBar);
                NavigationView.this.setDrawTopInsetForeground(isBehindStatusBar);
                Activity activity = ContextUtils.getActivity(NavigationView.this.getContext());
                if (activity != null && Build.VERSION.SDK_INT >= 21) {
                    boolean isBehindSystemNav = activity.findViewById(16908290).getHeight() == NavigationView.this.getHeight();
                    boolean hasNonZeroAlpha = Color.alpha(activity.getWindow().getNavigationBarColor()) != 0;
                    NavigationView navigationView2 = NavigationView.this;
                    if (!isBehindSystemNav || !hasNonZeroAlpha) {
                        z = false;
                    }
                    navigationView2.setDrawBottomInsetForeground(z);
                }
            }
        };
        getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
    }

    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public Bundle menuState;

        public SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.menuState = in.readBundle(loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(this.menuState);
        }
    }
}
