package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;
    private int activePointerId = -1;
    private Runnable flingRunnable;
    private boolean isBeingDragged;
    private int lastMotionY;
    OverScroller scroller;
    private int touchSlop = -1;
    private VelocityTracker velocityTracker;

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        int pointerIndex;
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        if (ev.getActionMasked() == 2 && this.isBeingDragged) {
            int i = this.activePointerId;
            if (i == -1 || (pointerIndex = ev.findPointerIndex(i)) == -1) {
                return false;
            }
            int y = (int) ev.getY(pointerIndex);
            if (Math.abs(y - this.lastMotionY) > this.touchSlop) {
                this.lastMotionY = y;
                return true;
            }
        }
        if (ev.getActionMasked() == 0) {
            this.activePointerId = -1;
            int x = (int) ev.getX();
            int y2 = (int) ev.getY();
            boolean z = canDragView(child) && parent.isPointInChildBounds(child, x, y2);
            this.isBeingDragged = z;
            if (z) {
                this.lastMotionY = y2;
                this.activePointerId = ev.getPointerId(0);
                ensureVelocityTracker();
                OverScroller overScroller = this.scroller;
                if (overScroller != null && !overScroller.isFinished()) {
                    this.scroller.abortAnimation();
                    return true;
                }
            }
        }
        VelocityTracker velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(ev);
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(androidx.coordinatorlayout.widget.CoordinatorLayout r13, V r14, android.view.MotionEvent r15) {
        /*
            r12 = this;
            r0 = 0
            int r1 = r15.getActionMasked()
            r2 = -1
            r3 = 1
            r4 = 0
            if (r1 == r3) goto L_0x004f
            r5 = 2
            if (r1 == r5) goto L_0x002f
            r5 = 3
            if (r1 == r5) goto L_0x0073
            r2 = 6
            if (r1 == r2) goto L_0x0015
            goto L_0x0081
        L_0x0015:
            int r1 = r15.getActionIndex()
            if (r1 != 0) goto L_0x001d
            r1 = r3
            goto L_0x001e
        L_0x001d:
            r1 = r4
        L_0x001e:
            int r2 = r15.getPointerId(r1)
            r12.activePointerId = r2
            float r2 = r15.getY(r1)
            r5 = 1056964608(0x3f000000, float:0.5)
            float r2 = r2 + r5
            int r2 = (int) r2
            r12.lastMotionY = r2
            goto L_0x0081
        L_0x002f:
            int r1 = r12.activePointerId
            int r1 = r15.findPointerIndex(r1)
            if (r1 != r2) goto L_0x0038
            return r4
        L_0x0038:
            float r2 = r15.getY(r1)
            int r2 = (int) r2
            int r5 = r12.lastMotionY
            int r5 = r5 - r2
            r12.lastMotionY = r2
            int r10 = r12.getMaxDragOffset(r14)
            r11 = 0
            r6 = r12
            r7 = r13
            r8 = r14
            r9 = r5
            r6.scroll(r7, r8, r9, r10, r11)
            goto L_0x0081
        L_0x004f:
            android.view.VelocityTracker r1 = r12.velocityTracker
            if (r1 == 0) goto L_0x0073
            r0 = 1
            r1.addMovement(r15)
            android.view.VelocityTracker r1 = r12.velocityTracker
            r5 = 1000(0x3e8, float:1.401E-42)
            r1.computeCurrentVelocity(r5)
            android.view.VelocityTracker r1 = r12.velocityTracker
            int r5 = r12.activePointerId
            float r1 = r1.getYVelocity(r5)
            int r5 = r12.getScrollRangeForDragFling(r14)
            int r9 = -r5
            r10 = 0
            r6 = r12
            r7 = r13
            r8 = r14
            r11 = r1
            r6.fling(r7, r8, r9, r10, r11)
        L_0x0073:
            r12.isBeingDragged = r4
            r12.activePointerId = r2
            android.view.VelocityTracker r1 = r12.velocityTracker
            if (r1 == 0) goto L_0x0081
            r1.recycle()
            r1 = 0
            r12.velocityTracker = r1
        L_0x0081:
            android.view.VelocityTracker r1 = r12.velocityTracker
            if (r1 == 0) goto L_0x0088
            r1.addMovement(r15)
        L_0x0088:
            boolean r1 = r12.isBeingDragged
            if (r1 != 0) goto L_0x0090
            if (r0 == 0) goto L_0x008f
            goto L_0x0090
        L_0x008f:
            r3 = r4
        L_0x0090:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.HeaderBehavior.onTouchEvent(androidx.coordinatorlayout.widget.CoordinatorLayout, android.view.View, android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: package-private */
    public int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset) {
        return setHeaderTopBottomOffset(parent, header, newOffset, Integer.MIN_VALUE, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    /* access modifiers changed from: package-private */
    public int setHeaderTopBottomOffset(CoordinatorLayout parent, V v, int newOffset, int minOffset, int maxOffset) {
        int newOffset2;
        int curOffset = getTopAndBottomOffset();
        if (minOffset == 0 || curOffset < minOffset || curOffset > maxOffset || curOffset == (newOffset2 = MathUtils.clamp(newOffset, minOffset, maxOffset))) {
            return 0;
        }
        setTopAndBottomOffset(newOffset2);
        return curOffset - newOffset2;
    }

    /* access modifiers changed from: package-private */
    public int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    /* access modifiers changed from: package-private */
    public final int scroll(CoordinatorLayout coordinatorLayout, V header, int dy, int minOffset, int maxOffset) {
        return setHeaderTopBottomOffset(coordinatorLayout, header, getTopBottomOffsetForScrollingSibling() - dy, minOffset, maxOffset);
    }

    /* access modifiers changed from: package-private */
    public final boolean fling(CoordinatorLayout coordinatorLayout, V layout, int minOffset, int maxOffset, float velocityY) {
        V v = layout;
        Runnable runnable = this.flingRunnable;
        if (runnable != null) {
            layout.removeCallbacks(runnable);
            this.flingRunnable = null;
        }
        if (this.scroller == null) {
            this.scroller = new OverScroller(layout.getContext());
        }
        this.scroller.fling(0, getTopAndBottomOffset(), 0, Math.round(velocityY), 0, 0, minOffset, maxOffset);
        if (this.scroller.computeScrollOffset()) {
            CoordinatorLayout coordinatorLayout2 = coordinatorLayout;
            FlingRunnable flingRunnable2 = new FlingRunnable(coordinatorLayout, layout);
            this.flingRunnable = flingRunnable2;
            ViewCompat.postOnAnimation(layout, flingRunnable2);
            return true;
        }
        CoordinatorLayout coordinatorLayout3 = coordinatorLayout;
        onFlingFinished(coordinatorLayout, layout);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onFlingFinished(CoordinatorLayout parent, V v) {
    }

    /* access modifiers changed from: package-private */
    public boolean canDragView(V v) {
        return false;
    }

    /* access modifiers changed from: package-private */
    public int getMaxDragOffset(V view) {
        return -view.getHeight();
    }

    /* access modifiers changed from: package-private */
    public int getScrollRangeForDragFling(V view) {
        return view.getHeight();
    }

    private void ensureVelocityTracker() {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
    }

    private class FlingRunnable implements Runnable {
        private final V layout;
        private final CoordinatorLayout parent;

        FlingRunnable(CoordinatorLayout parent2, V layout2) {
            this.parent = parent2;
            this.layout = layout2;
        }

        public void run() {
            if (this.layout != null && HeaderBehavior.this.scroller != null) {
                if (HeaderBehavior.this.scroller.computeScrollOffset()) {
                    HeaderBehavior headerBehavior = HeaderBehavior.this;
                    headerBehavior.setHeaderTopBottomOffset(this.parent, this.layout, headerBehavior.scroller.getCurrY());
                    ViewCompat.postOnAnimation(this.layout, this);
                    return;
                }
                HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
            }
        }
    }
}
