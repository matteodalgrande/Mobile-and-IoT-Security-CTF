package androidx.constraintlayout.solver.widgets;

import androidx.appcompat.widget.ActivityChooserView;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintWidget {
    public static final int ANCHOR_BASELINE = 4;
    public static final int ANCHOR_BOTTOM = 3;
    public static final int ANCHOR_LEFT = 0;
    public static final int ANCHOR_RIGHT = 1;
    public static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int BOTH = 2;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    private static final boolean USE_WRAP_DIMENSION_FOR_SPREAD = false;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    private boolean OPTIMIZE_WRAP;
    private boolean OPTIMIZE_WRAP_ON_RESOLVED;
    private boolean hasBaseline;
    public ChainRun horizontalChainRun;
    public int horizontalGroup;
    public HorizontalWidgetRun horizontalRun;
    private boolean inPlaceholder;
    public boolean[] isTerminalWidget;
    protected ArrayList<ConstraintAnchor> mAnchors;
    public ConstraintAnchor mBaseline;
    int mBaselineDistance;
    public ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    public ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    public float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    private boolean mInVirtuaLayout;
    public boolean mIsHeightWrapContent;
    private boolean[] mIsInBarrier;
    public boolean mIsWidthWrapContent;
    private int mLastHorizontalMeasureSpec;
    private int mLastVerticalMeasureSpec;
    public ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    public int mMatchConstraintDefaultHeight;
    public int mMatchConstraintDefaultWidth;
    public int mMatchConstraintMaxHeight;
    public int mMatchConstraintMaxWidth;
    public int mMatchConstraintMinHeight;
    public int mMatchConstraintMinWidth;
    public float mMatchConstraintPercentHeight;
    public float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    private boolean mMeasureRequested;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    public ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    boolean mResolvedHasRatio;
    public int[] mResolvedMatchConstraintDefault;
    public ConstraintAnchor mRight;
    boolean mRightHasCentered;
    public ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    public float[] mWeight;
    int mWidth;

    /* renamed from: mX */
    protected int f77mX;

    /* renamed from: mY */
    protected int f78mY;
    public boolean measured;
    private boolean resolvedHorizontal;
    private boolean resolvedVertical;
    public WidgetRun[] run;
    public ChainRun verticalChainRun;
    public int verticalGroup;
    public VerticalWidgetRun verticalRun;

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public WidgetRun getRun(int orientation) {
        if (orientation == 0) {
            return this.horizontalRun;
        }
        if (orientation == 1) {
            return this.verticalRun;
        }
        return null;
    }

    public void setFinalFrame(int left, int top, int right, int bottom, int baseline, int orientation) {
        setFrame(left, top, right, bottom);
        setBaselineDistance(baseline);
        if (orientation == 0) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = false;
        } else if (orientation == 1) {
            this.resolvedHorizontal = false;
            this.resolvedVertical = true;
        } else if (orientation == 2) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = true;
        } else {
            this.resolvedHorizontal = false;
            this.resolvedVertical = false;
        }
    }

    public void setFinalLeft(int x1) {
        this.mLeft.setFinalValue(x1);
        this.f77mX = x1;
    }

    public void setFinalTop(int y1) {
        this.mTop.setFinalValue(y1);
        this.f78mY = y1;
    }

    public void setFinalHorizontal(int x1, int x2) {
        this.mLeft.setFinalValue(x1);
        this.mRight.setFinalValue(x2);
        this.f77mX = x1;
        this.mWidth = x2 - x1;
        this.resolvedHorizontal = true;
    }

    public void setFinalVertical(int y1, int y2) {
        this.mTop.setFinalValue(y1);
        this.mBottom.setFinalValue(y2);
        this.f78mY = y1;
        this.mHeight = y2 - y1;
        if (this.hasBaseline) {
            this.mBaseline.setFinalValue(this.mBaselineDistance + y1);
        }
        this.resolvedVertical = true;
    }

    public void setFinalBaseline(int baselineValue) {
        if (this.hasBaseline) {
            int y1 = baselineValue - this.mBaselineDistance;
            this.f78mY = y1;
            this.mTop.setFinalValue(y1);
            this.mBottom.setFinalValue(this.mHeight + y1);
            this.mBaseline.setFinalValue(baselineValue);
            this.resolvedVertical = true;
        }
    }

    public boolean isResolvedHorizontally() {
        return this.resolvedHorizontal || (this.mLeft.hasFinalValue() && this.mRight.hasFinalValue());
    }

    public boolean isResolvedVertically() {
        return this.resolvedVertical || (this.mTop.hasFinalValue() && this.mBottom.hasFinalValue());
    }

    public void resetFinalResolution() {
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        int mAnchorsSize = this.mAnchors.size();
        for (int i = 0; i < mAnchorsSize; i++) {
            this.mAnchors.get(i).resetFinalResolution();
        }
    }

    public void ensureMeasureRequested() {
        this.mMeasureRequested = true;
    }

    public boolean hasDependencies() {
        int mAnchorsSize = this.mAnchors.size();
        for (int i = 0; i < mAnchorsSize; i++) {
            if (this.mAnchors.get(i).hasDependents()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDanglingDimension(int orientation) {
        if (orientation == 0) {
            if ((this.mLeft.mTarget != null ? 1 : 0) + (this.mRight.mTarget != null ? 1 : 0) < 2) {
                return true;
            }
            return false;
        }
        if ((this.mTop.mTarget != null ? 1 : 0) + (this.mBottom.mTarget != null ? 1 : 0) + (this.mBaseline.mTarget != null ? 1 : 0) < 2) {
            return true;
        }
        return false;
    }

    public boolean isInVirtualLayout() {
        return this.mInVirtuaLayout;
    }

    public void setInVirtualLayout(boolean inVirtualLayout) {
        this.mInVirtuaLayout = inVirtualLayout;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxDimension[0] = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxDimension[1] = maxHeight;
    }

    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public void setHasBaseline(boolean hasBaseline2) {
        this.hasBaseline = hasBaseline2;
    }

    public boolean getHasBaseline() {
        return this.hasBaseline;
    }

    public boolean isInPlaceholder() {
        return this.inPlaceholder;
    }

    public void setInPlaceholder(boolean inPlaceholder2) {
        this.inPlaceholder = inPlaceholder2;
    }

    /* access modifiers changed from: protected */
    public void setInBarrier(int orientation, boolean value) {
        this.mIsInBarrier[orientation] = value;
    }

    public void setMeasureRequested(boolean measureRequested) {
        this.mMeasureRequested = measureRequested;
    }

    public boolean isMeasureRequested() {
        return this.mMeasureRequested && this.mVisibility != 8;
    }

    public int getLastHorizontalMeasureSpec() {
        return this.mLastHorizontalMeasureSpec;
    }

    public int getLastVerticalMeasureSpec() {
        return this.mLastVerticalMeasureSpec;
    }

    public void setLastMeasureSpec(int horizontal, int vertical) {
        this.mLastHorizontalMeasureSpec = horizontal;
        this.mLastVerticalMeasureSpec = vertical;
        setMeasureRequested(false);
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f77mX = 0;
        this.f78mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintMaxHeight = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mGroupsToSolver = false;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
        this.mInVirtuaLayout = false;
        boolean[] zArr2 = this.mIsInBarrier;
        zArr2[0] = false;
        zArr2[1] = false;
        this.mMeasureRequested = true;
    }

    public boolean oppositeDimensionDependsOn(int orientation) {
        int oppositeOrientation = orientation == 0 ? 1 : 0;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[orientation];
        DimensionBehaviour oppositeDimensionBehaviour = dimensionBehaviourArr[oppositeOrientation];
        if (dimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && oppositeDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            return true;
        }
        return false;
    }

    public boolean oppositeDimensionsTied() {
        return this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public ConstraintWidget() {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f77mX = 0;
        this.f78mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
    }

    public ConstraintWidget(String debugName) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f77mX = 0;
        this.f78mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
        setDebugName(debugName);
    }

    public ConstraintWidget(int x, int y, int width, int height) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f77mX = 0;
        this.f78mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        this.f77mX = x;
        this.f78mY = y;
        this.mWidth = width;
        this.mHeight = height;
        addAnchors();
    }

    public ConstraintWidget(String debugName, int x, int y, int width, int height) {
        this(x, y, width, height);
        setDebugName(debugName);
    }

    public ConstraintWidget(int width, int height) {
        this(0, 0, width, height);
    }

    public void ensureWidgetRuns() {
        if (this.horizontalRun == null) {
            this.horizontalRun = new HorizontalWidgetRun(this);
        }
        if (this.verticalRun == null) {
            this.verticalRun = new VerticalWidgetRun(this);
        }
    }

    public ConstraintWidget(String debugName, int width, int height) {
        this(width, height);
        setDebugName(debugName);
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget widget) {
        this.mParent = widget;
    }

    public void setWidthWrapContent(boolean widthWrapContent) {
        this.mIsWidthWrapContent = widthWrapContent;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public void setHeightWrapContent(boolean heightWrapContent) {
        this.mIsHeightWrapContent = heightWrapContent;
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public void connectCircularConstraint(ConstraintWidget target, float angle, int radius) {
        immediateConnect(ConstraintAnchor.Type.CENTER, target, ConstraintAnchor.Type.CENTER, radius, 0);
        this.mCircleConstraintAngle = angle;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setVisibility(int visibility) {
        this.mVisibility = visibility;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String name) {
        this.mDebugName = name;
    }

    public void setDebugSolverName(LinearSystem system, String name) {
        this.mDebugName = name;
        SolverVariable left = system.createObjectVariable(this.mLeft);
        SolverVariable top = system.createObjectVariable(this.mTop);
        SolverVariable right = system.createObjectVariable(this.mRight);
        SolverVariable bottom = system.createObjectVariable(this.mBottom);
        left.setName(name + ".left");
        top.setName(name + ".top");
        right.setName(name + ".right");
        bottom.setName(name + ".bottom");
        system.createObjectVariable(this.mBaseline).setName(name + ".baseline");
    }

    public void createObjectVariables(LinearSystem system) {
        SolverVariable createObjectVariable = system.createObjectVariable(this.mLeft);
        SolverVariable createObjectVariable2 = system.createObjectVariable(this.mTop);
        SolverVariable createObjectVariable3 = system.createObjectVariable(this.mRight);
        SolverVariable createObjectVariable4 = system.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            system.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        String str = "";
        StringBuilder append = new StringBuilder().append(this.mType != null ? "type: " + this.mType + " " : str);
        if (this.mDebugName != null) {
            str = "id: " + this.mDebugName + " ";
        }
        return append.append(str).append("(").append(this.f77mX).append(", ").append(this.f78mY).append(") - (").append(this.mWidth).append(" x ").append(this.mHeight).append(")").toString();
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.f77mX;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.f77mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.f78mY;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.f78mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getOptimizerWrapWidth() {
        int w;
        int w2 = this.mWidth;
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return w2;
        }
        if (this.mMatchConstraintDefaultWidth == 1) {
            w = Math.max(this.mMatchConstraintMinWidth, w2);
        } else if (this.mMatchConstraintMinWidth > 0) {
            w = this.mMatchConstraintMinWidth;
            this.mWidth = w;
        } else {
            w = 0;
        }
        int i = this.mMatchConstraintMaxWidth;
        if (i <= 0 || i >= w) {
            return w;
        }
        return this.mMatchConstraintMaxWidth;
    }

    public int getOptimizerWrapHeight() {
        int h;
        int h2 = this.mHeight;
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return h2;
        }
        if (this.mMatchConstraintDefaultHeight == 1) {
            h = Math.max(this.mMatchConstraintMinHeight, h2);
        } else if (this.mMatchConstraintMinHeight > 0) {
            h = this.mMatchConstraintMinHeight;
            this.mHeight = h;
        } else {
            h = 0;
        }
        int i = this.mMatchConstraintMaxHeight;
        if (i <= 0 || i >= h) {
            return h;
        }
        return this.mMatchConstraintMaxHeight;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getLength(int orientation) {
        if (orientation == 0) {
            return getWidth();
        }
        if (orientation == 1) {
            return getHeight();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getRootX() {
        return this.f77mX + this.mOffsetX;
    }

    /* access modifiers changed from: protected */
    public int getRootY() {
        return this.f78mY + this.mOffsetY;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public int getHorizontalMargin() {
        int margin = 0;
        ConstraintAnchor constraintAnchor = this.mLeft;
        if (constraintAnchor != null) {
            margin = 0 + constraintAnchor.mMargin;
        }
        ConstraintAnchor constraintAnchor2 = this.mRight;
        if (constraintAnchor2 != null) {
            return margin + constraintAnchor2.mMargin;
        }
        return margin;
    }

    public int getVerticalMargin() {
        int margin = 0;
        if (this.mLeft != null) {
            margin = 0 + this.mTop.mMargin;
        }
        if (this.mRight != null) {
            return margin + this.mBottom.mMargin;
        }
        return margin;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int orientation) {
        if (orientation == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (orientation == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public boolean hasBaseline() {
        return this.hasBaseline;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public void setX(int x) {
        this.f77mX = x;
    }

    public void setY(int y) {
        this.f78mY = y;
    }

    public void setOrigin(int x, int y) {
        this.f77mX = x;
        this.f78mY = y;
    }

    public void setOffset(int x, int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public void setGoneMargin(ConstraintAnchor.Type type, int goneMargin) {
        int i = C02141.f79x4c44d048[type.ordinal()];
        if (i == 1) {
            this.mLeft.mGoneMargin = goneMargin;
        } else if (i == 2) {
            this.mTop.mGoneMargin = goneMargin;
        } else if (i == 3) {
            this.mRight.mGoneMargin = goneMargin;
        } else if (i == 4) {
            this.mBottom.mGoneMargin = goneMargin;
        }
    }

    public void setWidth(int w) {
        this.mWidth = w;
        int i = this.mMinWidth;
        if (w < i) {
            this.mWidth = i;
        }
    }

    public void setHeight(int h) {
        this.mHeight = h;
        int i = this.mMinHeight;
        if (h < i) {
            this.mHeight = i;
        }
    }

    public void setLength(int length, int orientation) {
        if (orientation == 0) {
            setWidth(length);
        } else if (orientation == 1) {
            setHeight(length);
        }
    }

    public void setHorizontalMatchStyle(int horizontalMatchStyle, int min, int max, float percent) {
        this.mMatchConstraintDefaultWidth = horizontalMatchStyle;
        this.mMatchConstraintMinWidth = min;
        this.mMatchConstraintMaxWidth = max == Integer.MAX_VALUE ? 0 : max;
        this.mMatchConstraintPercentWidth = percent;
        if (percent > 0.0f && percent < 1.0f && horizontalMatchStyle == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setVerticalMatchStyle(int verticalMatchStyle, int min, int max, float percent) {
        this.mMatchConstraintDefaultHeight = verticalMatchStyle;
        this.mMatchConstraintMinHeight = min;
        this.mMatchConstraintMaxHeight = max == Integer.MAX_VALUE ? 0 : max;
        this.mMatchConstraintPercentHeight = percent;
        if (percent > 0.0f && percent < 1.0f && verticalMatchStyle == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    public void setDimensionRatio(String ratio) {
        int commaIndex;
        if (ratio == null || ratio.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int dimensionRatioSide = -1;
        float dimensionRatio = 0.0f;
        int len = ratio.length();
        int commaIndex2 = ratio.indexOf(44);
        if (commaIndex2 <= 0 || commaIndex2 >= len - 1) {
            commaIndex = 0;
        } else {
            String dimension = ratio.substring(0, commaIndex2);
            if (dimension.equalsIgnoreCase("W")) {
                dimensionRatioSide = 0;
            } else if (dimension.equalsIgnoreCase("H")) {
                dimensionRatioSide = 1;
            }
            commaIndex = commaIndex2 + 1;
        }
        int colonIndex = ratio.indexOf(58);
        if (colonIndex < 0 || colonIndex >= len - 1) {
            String r = ratio.substring(commaIndex);
            if (r.length() > 0) {
                try {
                    dimensionRatio = Float.parseFloat(r);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            String nominator = ratio.substring(commaIndex, colonIndex);
            String denominator = ratio.substring(colonIndex + 1);
            if (nominator.length() > 0 && denominator.length() > 0) {
                try {
                    float nominatorValue = Float.parseFloat(nominator);
                    float denominatorValue = Float.parseFloat(denominator);
                    if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                        dimensionRatio = dimensionRatioSide == 1 ? Math.abs(denominatorValue / nominatorValue) : Math.abs(nominatorValue / denominatorValue);
                    }
                } catch (NumberFormatException e2) {
                }
            }
        }
        if (dimensionRatio > 0.0f) {
            this.mDimensionRatio = dimensionRatio;
            this.mDimensionRatioSide = dimensionRatioSide;
        }
    }

    public void setDimensionRatio(float ratio, int dimensionRatioSide) {
        this.mDimensionRatio = ratio;
        this.mDimensionRatioSide = dimensionRatioSide;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float horizontalBiasPercent) {
        this.mHorizontalBiasPercent = horizontalBiasPercent;
    }

    public void setVerticalBiasPercent(float verticalBiasPercent) {
        this.mVerticalBiasPercent = verticalBiasPercent;
    }

    public void setMinWidth(int w) {
        if (w < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = w;
        }
    }

    public void setMinHeight(int h) {
        if (h < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = h;
        }
    }

    public void setDimension(int w, int h) {
        this.mWidth = w;
        int i = this.mMinWidth;
        if (w < i) {
            this.mWidth = i;
        }
        this.mHeight = h;
        int i2 = this.mMinHeight;
        if (h < i2) {
            this.mHeight = i2;
        }
    }

    public void setFrame(int left, int top, int right, int bottom) {
        int w = right - left;
        int h = bottom - top;
        this.f77mX = left;
        this.f78mY = top;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && w < this.mWidth) {
            w = this.mWidth;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && h < this.mHeight) {
            h = this.mHeight;
        }
        this.mWidth = w;
        this.mHeight = h;
        int i = this.mMinHeight;
        if (h < i) {
            this.mHeight = i;
        }
        int i2 = this.mMinWidth;
        if (w < i2) {
            this.mWidth = i2;
        }
    }

    public void setFrame(int start, int end, int orientation) {
        if (orientation == 0) {
            setHorizontalDimension(start, end);
        } else if (orientation == 1) {
            setVerticalDimension(start, end);
        }
    }

    public void setHorizontalDimension(int left, int right) {
        this.f77mX = left;
        int i = right - left;
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public void setVerticalDimension(int top, int bottom) {
        this.f78mY = top;
        int i = bottom - top;
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    /* access modifiers changed from: package-private */
    public int getRelativePositioning(int orientation) {
        if (orientation == 0) {
            return this.mRelX;
        }
        if (orientation == 1) {
            return this.mRelY;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void setRelativePositioning(int offset, int orientation) {
        if (orientation == 0) {
            this.mRelX = offset;
        } else if (orientation == 1) {
            this.mRelY = offset;
        }
    }

    public void setBaselineDistance(int baseline) {
        this.mBaselineDistance = baseline;
        this.hasBaseline = baseline > 0;
    }

    public void setCompanionWidget(Object companion) {
        this.mCompanionWidget = companion;
    }

    public void setContainerItemSkip(int skip) {
        if (skip >= 0) {
            this.mContainerItemSkip = skip;
        } else {
            this.mContainerItemSkip = 0;
        }
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public void setHorizontalWeight(float horizontalWeight) {
        this.mWeight[0] = horizontalWeight;
    }

    public void setVerticalWeight(float verticalWeight) {
        this.mWeight[1] = verticalWeight;
    }

    public void setHorizontalChainStyle(int horizontalChainStyle) {
        this.mHorizontalChainStyle = horizontalChainStyle;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int verticalChainStyle) {
        this.mVerticalChainStyle = verticalChainStyle;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(ConstraintAnchor.Type startType, ConstraintWidget target, ConstraintAnchor.Type endType, int margin, int goneMargin) {
        getAnchor(startType).connect(target.getAnchor(endType), margin, goneMargin, true);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin) {
        if (from.getOwner() == this) {
            connect(from.getType(), to.getOwner(), to.getType(), margin);
        }
    }

    public void connect(ConstraintAnchor.Type constraintFrom, ConstraintWidget target, ConstraintAnchor.Type constraintTo) {
        connect(constraintFrom, target, constraintTo, 0);
    }

    public void connect(ConstraintAnchor.Type constraintFrom, ConstraintWidget target, ConstraintAnchor.Type constraintTo, int margin) {
        if (constraintFrom == ConstraintAnchor.Type.CENTER) {
            if (constraintTo == ConstraintAnchor.Type.CENTER) {
                ConstraintAnchor left = getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor right = getAnchor(ConstraintAnchor.Type.RIGHT);
                ConstraintAnchor top = getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor bottom = getAnchor(ConstraintAnchor.Type.BOTTOM);
                boolean centerX = false;
                boolean centerY = false;
                if ((left == null || !left.isConnected()) && (right == null || !right.isConnected())) {
                    connect(ConstraintAnchor.Type.LEFT, target, ConstraintAnchor.Type.LEFT, 0);
                    connect(ConstraintAnchor.Type.RIGHT, target, ConstraintAnchor.Type.RIGHT, 0);
                    centerX = true;
                }
                if ((top == null || !top.isConnected()) && (bottom == null || !bottom.isConnected())) {
                    connect(ConstraintAnchor.Type.TOP, target, ConstraintAnchor.Type.TOP, 0);
                    connect(ConstraintAnchor.Type.BOTTOM, target, ConstraintAnchor.Type.BOTTOM, 0);
                    centerY = true;
                }
                if (centerX && centerY) {
                    getAnchor(ConstraintAnchor.Type.CENTER).connect(target.getAnchor(ConstraintAnchor.Type.CENTER), 0);
                } else if (centerX) {
                    getAnchor(ConstraintAnchor.Type.CENTER_X).connect(target.getAnchor(ConstraintAnchor.Type.CENTER_X), 0);
                } else if (centerY) {
                    getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(target.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0);
                }
            } else if (constraintTo == ConstraintAnchor.Type.LEFT || constraintTo == ConstraintAnchor.Type.RIGHT) {
                connect(ConstraintAnchor.Type.LEFT, target, constraintTo, 0);
                connect(ConstraintAnchor.Type.RIGHT, target, constraintTo, 0);
                getAnchor(ConstraintAnchor.Type.CENTER).connect(target.getAnchor(constraintTo), 0);
            } else if (constraintTo == ConstraintAnchor.Type.TOP || constraintTo == ConstraintAnchor.Type.BOTTOM) {
                connect(ConstraintAnchor.Type.TOP, target, constraintTo, 0);
                connect(ConstraintAnchor.Type.BOTTOM, target, constraintTo, 0);
                getAnchor(ConstraintAnchor.Type.CENTER).connect(target.getAnchor(constraintTo), 0);
            }
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_X && (constraintTo == ConstraintAnchor.Type.LEFT || constraintTo == ConstraintAnchor.Type.RIGHT)) {
            ConstraintAnchor left2 = getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor targetAnchor = target.getAnchor(constraintTo);
            ConstraintAnchor right2 = getAnchor(ConstraintAnchor.Type.RIGHT);
            left2.connect(targetAnchor, 0);
            right2.connect(targetAnchor, 0);
            getAnchor(ConstraintAnchor.Type.CENTER_X).connect(targetAnchor, 0);
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_Y && (constraintTo == ConstraintAnchor.Type.TOP || constraintTo == ConstraintAnchor.Type.BOTTOM)) {
            ConstraintAnchor targetAnchor2 = target.getAnchor(constraintTo);
            getAnchor(ConstraintAnchor.Type.TOP).connect(targetAnchor2, 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(targetAnchor2, 0);
            getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(targetAnchor2, 0);
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_X && constraintTo == ConstraintAnchor.Type.CENTER_X) {
            getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), 0);
            getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), 0);
            getAnchor(ConstraintAnchor.Type.CENTER_X).connect(target.getAnchor(constraintTo), 0);
        } else if (constraintFrom == ConstraintAnchor.Type.CENTER_Y && constraintTo == ConstraintAnchor.Type.CENTER_Y) {
            getAnchor(ConstraintAnchor.Type.TOP).connect(target.getAnchor(ConstraintAnchor.Type.TOP), 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(target.getAnchor(ConstraintAnchor.Type.BOTTOM), 0);
            getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(target.getAnchor(constraintTo), 0);
        } else {
            ConstraintAnchor fromAnchor = getAnchor(constraintFrom);
            ConstraintAnchor toAnchor = target.getAnchor(constraintTo);
            if (fromAnchor.isValidConnection(toAnchor)) {
                if (constraintFrom == ConstraintAnchor.Type.BASELINE) {
                    ConstraintAnchor top2 = getAnchor(ConstraintAnchor.Type.TOP);
                    ConstraintAnchor bottom2 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                    if (top2 != null) {
                        top2.reset();
                    }
                    if (bottom2 != null) {
                        bottom2.reset();
                    }
                    margin = 0;
                } else if (constraintFrom == ConstraintAnchor.Type.TOP || constraintFrom == ConstraintAnchor.Type.BOTTOM) {
                    ConstraintAnchor baseline = getAnchor(ConstraintAnchor.Type.BASELINE);
                    if (baseline != null) {
                        baseline.reset();
                    }
                    ConstraintAnchor center = getAnchor(ConstraintAnchor.Type.CENTER);
                    if (center.getTarget() != toAnchor) {
                        center.reset();
                    }
                    ConstraintAnchor opposite = getAnchor(constraintFrom).getOpposite();
                    ConstraintAnchor centerY2 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
                    if (centerY2.isConnected()) {
                        opposite.reset();
                        centerY2.reset();
                    }
                } else if (constraintFrom == ConstraintAnchor.Type.LEFT || constraintFrom == ConstraintAnchor.Type.RIGHT) {
                    ConstraintAnchor center2 = getAnchor(ConstraintAnchor.Type.CENTER);
                    if (center2.getTarget() != toAnchor) {
                        center2.reset();
                    }
                    ConstraintAnchor opposite2 = getAnchor(constraintFrom).getOpposite();
                    ConstraintAnchor centerX2 = getAnchor(ConstraintAnchor.Type.CENTER_X);
                    if (centerX2.isConnected()) {
                        opposite2.reset();
                        centerX2.reset();
                    }
                }
                fromAnchor.connect(toAnchor, margin);
            }
        }
    }

    public void resetAllConstraints() {
        resetAnchors();
        setVerticalBiasPercent(DEFAULT_BIAS);
        setHorizontalBiasPercent(DEFAULT_BIAS);
    }

    public void resetAnchor(ConstraintAnchor anchor) {
        if (getParent() == null || !(getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            ConstraintAnchor left = getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor right = getAnchor(ConstraintAnchor.Type.RIGHT);
            ConstraintAnchor top = getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor bottom = getAnchor(ConstraintAnchor.Type.BOTTOM);
            ConstraintAnchor center = getAnchor(ConstraintAnchor.Type.CENTER);
            ConstraintAnchor centerX = getAnchor(ConstraintAnchor.Type.CENTER_X);
            ConstraintAnchor centerY = getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (anchor == center) {
                if (left.isConnected() && right.isConnected() && left.getTarget() == right.getTarget()) {
                    left.reset();
                    right.reset();
                }
                if (top.isConnected() && bottom.isConnected() && top.getTarget() == bottom.getTarget()) {
                    top.reset();
                    bottom.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == centerX) {
                if (left.isConnected() && right.isConnected() && left.getTarget().getOwner() == right.getTarget().getOwner()) {
                    left.reset();
                    right.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
            } else if (anchor == centerY) {
                if (top.isConnected() && bottom.isConnected() && top.getTarget().getOwner() == bottom.getTarget().getOwner()) {
                    top.reset();
                    bottom.reset();
                }
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == left || anchor == right) {
                if (left.isConnected() && left.getTarget() == right.getTarget()) {
                    center.reset();
                }
            } else if ((anchor == top || anchor == bottom) && top.isConnected() && top.getTarget() == bottom.getTarget()) {
                center.reset();
            }
            anchor.reset();
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int mAnchorsSize = this.mAnchors.size();
            for (int i = 0; i < mAnchorsSize; i++) {
                this.mAnchors.get(i).reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type anchorType) {
        switch (C02141.f79x4c44d048[anchorType.ordinal()]) {
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case 6:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            case 9:
                return null;
            default:
                throw new AssertionError(anchorType.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int orientation) {
        if (orientation == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (orientation == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[0] = behaviour;
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[1] = behaviour;
    }

    public boolean isInHorizontalChain() {
        if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) {
            return true;
        }
        if (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != this.mRight) {
            return false;
        }
        return true;
    }

    public ConstraintWidget getPreviousChainMember(int orientation) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (orientation == 0) {
            if (this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != (constraintAnchor2 = this.mLeft)) {
                return null;
            }
            return constraintAnchor2.mTarget.mOwner;
        } else if (orientation == 1 && this.mTop.mTarget != null && this.mTop.mTarget.mTarget == (constraintAnchor = this.mTop)) {
            return constraintAnchor.mTarget.mOwner;
        } else {
            return null;
        }
    }

    public ConstraintWidget getNextChainMember(int orientation) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (orientation == 0) {
            if (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != (constraintAnchor2 = this.mRight)) {
                return null;
            }
            return constraintAnchor2.mTarget.mOwner;
        } else if (orientation == 1 && this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == (constraintAnchor = this.mBottom)) {
            return constraintAnchor.mTarget.mOwner;
        } else {
            return null;
        }
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintWidget found = null;
        if (!isInHorizontalChain()) {
            return null;
        }
        ConstraintWidget tmp = this;
        while (found == null && tmp != null) {
            ConstraintAnchor anchor = tmp.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor targetAnchor = null;
            ConstraintAnchor targetOwner = anchor == null ? null : anchor.getTarget();
            ConstraintWidget target = targetOwner == null ? null : targetOwner.getOwner();
            if (target == getParent()) {
                return tmp;
            }
            if (target != null) {
                targetAnchor = target.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
            }
            if (targetAnchor == null || targetAnchor.getOwner() == tmp) {
                tmp = target;
            } else {
                found = tmp;
            }
        }
        return found;
    }

    public boolean isInVerticalChain() {
        if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) {
            return true;
        }
        if (this.mBottom.mTarget == null || this.mBottom.mTarget.mTarget != this.mBottom) {
            return false;
        }
        return true;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintWidget found = null;
        if (!isInVerticalChain()) {
            return null;
        }
        ConstraintWidget tmp = this;
        while (found == null && tmp != null) {
            ConstraintAnchor anchor = tmp.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor targetAnchor = null;
            ConstraintAnchor targetOwner = anchor == null ? null : anchor.getTarget();
            ConstraintWidget target = targetOwner == null ? null : targetOwner.getOwner();
            if (target == getParent()) {
                return tmp;
            }
            if (target != null) {
                targetAnchor = target.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
            }
            if (targetAnchor == null || targetAnchor.getOwner() == tmp) {
                tmp = target;
            } else {
                found = tmp;
            }
        }
        return found;
    }

    private boolean isChainHead(int orientation) {
        int offset = orientation * 2;
        if (this.mListAnchors[offset].mTarget != null) {
            ConstraintAnchor constraintAnchor = this.mListAnchors[offset].mTarget.mTarget;
            ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
            return (constraintAnchor == constraintAnchorArr[offset] || constraintAnchorArr[offset + 1].mTarget == null || this.mListAnchors[offset + 1].mTarget.mTarget != this.mListAnchors[offset + 1]) ? false : true;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v31, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r37v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v32, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v34, resolved type: int} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x033a  */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x033d  */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x034c  */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x035b  */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x035d  */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x0362  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x0366  */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x0371  */
    /* JADX WARNING: Removed duplicated region for block: B:208:0x0375  */
    /* JADX WARNING: Removed duplicated region for block: B:213:0x038a  */
    /* JADX WARNING: Removed duplicated region for block: B:247:0x04da  */
    /* JADX WARNING: Removed duplicated region for block: B:250:0x04f1  */
    /* JADX WARNING: Removed duplicated region for block: B:268:0x0552  */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x0565  */
    /* JADX WARNING: Removed duplicated region for block: B:273:0x0568  */
    /* JADX WARNING: Removed duplicated region for block: B:277:0x056f  */
    /* JADX WARNING: Removed duplicated region for block: B:311:0x0608  */
    /* JADX WARNING: Removed duplicated region for block: B:312:0x060b  */
    /* JADX WARNING: Removed duplicated region for block: B:314:0x0647  */
    /* JADX WARNING: Removed duplicated region for block: B:316:0x064b  */
    /* JADX WARNING: Removed duplicated region for block: B:322:0x0677  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(androidx.constraintlayout.solver.LinearSystem r74, boolean r75) {
        /*
            r73 = this;
            r15 = r73
            r14 = r74
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mLeft
            androidx.constraintlayout.solver.SolverVariable r13 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mRight
            androidx.constraintlayout.solver.SolverVariable r12 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mTop
            androidx.constraintlayout.solver.SolverVariable r11 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBottom
            androidx.constraintlayout.solver.SolverVariable r10 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.SolverVariable r9 = r14.createObjectVariable(r0)
            r0 = 0
            r1 = 0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            r8 = 1
            r5 = 0
            if (r2 == 0) goto L_0x004b
            if (r2 == 0) goto L_0x0036
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r2.mListDimensionBehaviors
            r2 = r2[r5]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r2 != r3) goto L_0x0036
            r2 = r8
            goto L_0x0037
        L_0x0036:
            r2 = r5
        L_0x0037:
            r0 = r2
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            if (r2 == 0) goto L_0x0046
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r2.mListDimensionBehaviors
            r2 = r2[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r2 != r3) goto L_0x0046
            r2 = r8
            goto L_0x0047
        L_0x0046:
            r2 = r5
        L_0x0047:
            r1 = r2
            r4 = r0
            r3 = r1
            goto L_0x004d
        L_0x004b:
            r4 = r0
            r3 = r1
        L_0x004d:
            int r0 = r15.mVisibility
            r2 = 8
            if (r0 != r2) goto L_0x0064
            boolean r0 = r73.hasDependencies()
            if (r0 != 0) goto L_0x0064
            boolean[] r0 = r15.mIsInBarrier
            boolean r1 = r0[r5]
            if (r1 != 0) goto L_0x0064
            boolean r0 = r0[r8]
            if (r0 != 0) goto L_0x0064
            return
        L_0x0064:
            boolean r0 = r15.resolvedHorizontal
            if (r0 != 0) goto L_0x006c
            boolean r1 = r15.resolvedVertical
            if (r1 == 0) goto L_0x00eb
        L_0x006c:
            if (r0 == 0) goto L_0x009c
            int r0 = r15.f77mX
            r14.addEquality(r13, r0)
            int r0 = r15.f77mX
            int r1 = r15.mWidth
            int r0 = r0 + r1
            r14.addEquality(r12, r0)
            if (r4 == 0) goto L_0x009c
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x009c
            boolean r1 = r15.OPTIMIZE_WRAP_ON_RESOLVED
            if (r1 == 0) goto L_0x0092
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mLeft
            r0.addVerticalWrapMinVariable(r1)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mRight
            r0.addHorizontalWrapMaxVariable(r1)
            goto L_0x009c
        L_0x0092:
            r1 = 5
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addGreaterThan(r0, r12, r5, r1)
        L_0x009c:
            boolean r0 = r15.resolvedVertical
            if (r0 == 0) goto L_0x00de
            int r0 = r15.f78mY
            r14.addEquality(r11, r0)
            int r0 = r15.f78mY
            int r1 = r15.mHeight
            int r0 = r0 + r1
            r14.addEquality(r10, r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            boolean r0 = r0.hasDependents()
            if (r0 == 0) goto L_0x00bd
            int r0 = r15.f78mY
            int r1 = r15.mBaselineDistance
            int r0 = r0 + r1
            r14.addEquality(r9, r0)
        L_0x00bd:
            if (r3 == 0) goto L_0x00de
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x00de
            boolean r1 = r15.OPTIMIZE_WRAP_ON_RESOLVED
            if (r1 == 0) goto L_0x00d4
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mTop
            r0.addVerticalWrapMinVariable(r1)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mBottom
            r0.addVerticalWrapMaxVariable(r1)
            goto L_0x00de
        L_0x00d4:
            r1 = 5
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addGreaterThan(r0, r10, r5, r1)
        L_0x00de:
            boolean r0 = r15.resolvedHorizontal
            if (r0 == 0) goto L_0x00eb
            boolean r0 = r15.resolvedVertical
            if (r0 == 0) goto L_0x00eb
            r15.resolvedHorizontal = r5
            r15.resolvedVertical = r5
            return
        L_0x00eb:
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            r6 = 1
            if (r0 == 0) goto L_0x00fb
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            r16 = r3
            long r2 = r0.widgets
            long r2 = r2 + r6
            r0.widgets = r2
            goto L_0x00fd
        L_0x00fb:
            r16 = r3
        L_0x00fd:
            if (r75 == 0) goto L_0x019c
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            if (r0 == 0) goto L_0x019c
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r15.verticalRun
            if (r1 == 0) goto L_0x019c
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x019c
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x019c
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x019c
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x019c
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            if (r0 == 0) goto L_0x0130
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            long r1 = r0.graphSolved
            long r1 = r1 + r6
            r0.graphSolved = r1
        L_0x0130:
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r13, r0)
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r12, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r11, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r10, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.baseline
            int r0 = r0.value
            r14.addEquality(r9, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0197
            if (r4 == 0) goto L_0x017c
            boolean[] r0 = r15.isTerminalWidget
            boolean r0 = r0[r5]
            if (r0 == 0) goto L_0x017c
            boolean r0 = r73.isInHorizontalChain()
            if (r0 != 0) goto L_0x017c
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 8
            r14.addGreaterThan(r0, r12, r5, r1)
        L_0x017c:
            if (r16 == 0) goto L_0x0197
            boolean[] r0 = r15.isTerminalWidget
            boolean r0 = r0[r8]
            if (r0 == 0) goto L_0x0197
            boolean r0 = r73.isInVerticalChain()
            if (r0 != 0) goto L_0x0197
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 8
            r14.addGreaterThan(r0, r10, r5, r1)
        L_0x0197:
            r15.resolvedHorizontal = r5
            r15.resolvedVertical = r5
            return
        L_0x019c:
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            if (r0 == 0) goto L_0x01a7
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            long r1 = r0.linearSolved
            long r1 = r1 + r6
            r0.linearSolved = r1
        L_0x01a7:
            r0 = 0
            r1 = 0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            if (r2 == 0) goto L_0x021e
            boolean r2 = r15.isChainHead(r5)
            if (r2 == 0) goto L_0x01bc
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r2 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r2
            r2.addChain(r15, r5)
            r0 = 1
            goto L_0x01c0
        L_0x01bc:
            boolean r0 = r73.isInHorizontalChain()
        L_0x01c0:
            boolean r2 = r15.isChainHead(r8)
            if (r2 == 0) goto L_0x01cf
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r2 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r2
            r2.addChain(r15, r8)
            r1 = 1
            goto L_0x01d3
        L_0x01cf:
            boolean r1 = r73.isInVerticalChain()
        L_0x01d3:
            if (r0 != 0) goto L_0x01f4
            if (r4 == 0) goto L_0x01f4
            int r2 = r15.mVisibility
            r3 = 8
            if (r2 == r3) goto L_0x01f4
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x01f4
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x01f4
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mRight
            androidx.constraintlayout.solver.SolverVariable r2 = r14.createObjectVariable(r2)
            r14.addGreaterThan(r2, r12, r5, r8)
        L_0x01f4:
            if (r1 != 0) goto L_0x0219
            if (r16 == 0) goto L_0x0219
            int r2 = r15.mVisibility
            r3 = 8
            if (r2 == r3) goto L_0x0219
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x0219
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x0219
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mBaseline
            if (r2 != 0) goto L_0x0219
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mBottom
            androidx.constraintlayout.solver.SolverVariable r2 = r14.createObjectVariable(r2)
            r14.addGreaterThan(r2, r10, r5, r8)
        L_0x0219:
            r56 = r0
            r57 = r1
            goto L_0x0222
        L_0x021e:
            r56 = r0
            r57 = r1
        L_0x0222:
            int r0 = r15.mWidth
            int r1 = r15.mMinWidth
            if (r0 >= r1) goto L_0x022a
            int r0 = r15.mMinWidth
        L_0x022a:
            int r1 = r15.mHeight
            int r2 = r15.mMinHeight
            if (r1 >= r2) goto L_0x0232
            int r1 = r15.mMinHeight
        L_0x0232:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r15.mListDimensionBehaviors
            r2 = r2[r5]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r2 == r3) goto L_0x023c
            r2 = r8
            goto L_0x023d
        L_0x023c:
            r2 = r5
        L_0x023d:
            r3 = r2
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r15.mListDimensionBehaviors
            r2 = r2[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r2 == r6) goto L_0x0248
            r2 = r8
            goto L_0x0249
        L_0x0248:
            r2 = r5
        L_0x0249:
            r6 = 0
            int r7 = r15.mDimensionRatioSide
            r15.mResolvedDimensionRatioSide = r7
            float r7 = r15.mDimensionRatio
            r15.mResolvedDimensionRatio = r7
            int r8 = r15.mMatchConstraintDefaultWidth
            int r5 = r15.mMatchConstraintDefaultHeight
            r17 = 0
            int r7 = (r7 > r17 ? 1 : (r7 == r17 ? 0 : -1))
            r17 = r0
            if (r7 <= 0) goto L_0x031a
            int r7 = r15.mVisibility
            r0 = 8
            if (r7 == r0) goto L_0x031a
            r6 = 1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 0
            r0 = r0[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x0271
            if (r8 != 0) goto L_0x0271
            r8 = 3
        L_0x0271:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 1
            r0 = r0[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x027d
            if (r5 != 0) goto L_0x027d
            r5 = 3
        L_0x027d:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 0
            r0 = r0[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r22 = r1
            r1 = 3
            if (r0 != r7) goto L_0x029f
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 1
            r0 = r0[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x029f
            if (r8 != r1) goto L_0x029f
            if (r5 != r1) goto L_0x029f
            r0 = r16
            r15.setupDimensionRatio(r4, r0, r3, r2)
            r23 = r0
            goto L_0x031e
        L_0x029f:
            r0 = r16
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r7 = r15.mListDimensionBehaviors
            r1 = 0
            r7 = r7[r1]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r7 != r1) goto L_0x02d8
            r1 = 3
            if (r8 != r1) goto L_0x02d8
            r1 = 0
            r15.mResolvedDimensionRatioSide = r1
            float r1 = r15.mResolvedDimensionRatio
            int r7 = r15.mHeight
            float r7 = (float) r7
            float r1 = r1 * r7
            int r1 = (int) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r7 = r15.mListDimensionBehaviors
            r16 = 1
            r7 = r7[r16]
            r23 = r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r7 == r0) goto L_0x02cf
            r8 = 4
            r6 = 0
            r0 = r1
            r58 = r5
            r59 = r8
            r28 = r22
            r8 = r6
            goto L_0x0327
        L_0x02cf:
            r0 = r1
            r58 = r5
            r59 = r8
            r28 = r22
            r8 = r6
            goto L_0x0327
        L_0x02d8:
            r23 = r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r1 = 1
            r0 = r0[r1]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x031e
            r0 = 3
            if (r5 != r0) goto L_0x031e
            r15.mResolvedDimensionRatioSide = r1
            int r0 = r15.mDimensionRatioSide
            r1 = -1
            if (r0 != r1) goto L_0x02f4
            r0 = 1065353216(0x3f800000, float:1.0)
            float r1 = r15.mResolvedDimensionRatio
            float r0 = r0 / r1
            r15.mResolvedDimensionRatio = r0
        L_0x02f4:
            float r0 = r15.mResolvedDimensionRatio
            int r1 = r15.mWidth
            float r1 = (float) r1
            float r0 = r0 * r1
            int r1 = (int) r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r7 = 0
            r0 = r0[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 == r7) goto L_0x0310
            r5 = 4
            r6 = 0
            r28 = r1
            r58 = r5
            r59 = r8
            r0 = r17
            r8 = r6
            goto L_0x0327
        L_0x0310:
            r28 = r1
            r58 = r5
            r59 = r8
            r0 = r17
            r8 = r6
            goto L_0x0327
        L_0x031a:
            r22 = r1
            r23 = r16
        L_0x031e:
            r58 = r5
            r59 = r8
            r0 = r17
            r28 = r22
            r8 = r6
        L_0x0327:
            int[] r1 = r15.mResolvedMatchConstraintDefault
            r5 = 0
            r1[r5] = r59
            r5 = 1
            r1[r5] = r58
            r15.mResolvedHasRatio = r8
            if (r8 == 0) goto L_0x033d
            int r1 = r15.mResolvedDimensionRatioSide
            if (r1 == 0) goto L_0x033a
            r5 = -1
            if (r1 != r5) goto L_0x033d
        L_0x033a:
            r17 = 1
            goto L_0x033f
        L_0x033d:
            r17 = 0
        L_0x033f:
            if (r8 == 0) goto L_0x034c
            int r1 = r15.mResolvedDimensionRatioSide
            r5 = 1
            if (r1 == r5) goto L_0x0349
            r5 = -1
            if (r1 != r5) goto L_0x034c
        L_0x0349:
            r45 = 1
            goto L_0x034e
        L_0x034c:
            r45 = 0
        L_0x034e:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r5 = 0
            r1 = r1[r5]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r5) goto L_0x035d
            boolean r1 = r15 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r1 == 0) goto L_0x035d
            r1 = 1
            goto L_0x035e
        L_0x035d:
            r1 = 0
        L_0x035e:
            r29 = r1
            if (r29 == 0) goto L_0x0366
            r0 = 0
            r60 = r0
            goto L_0x0368
        L_0x0366:
            r60 = r0
        L_0x0368:
            r0 = 1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mCenter
            boolean r1 = r1.isConnected()
            if (r1 == 0) goto L_0x0375
            r0 = 0
            r30 = r0
            goto L_0x0377
        L_0x0375:
            r30 = r0
        L_0x0377:
            boolean[] r0 = r15.mIsInBarrier
            r1 = 0
            boolean r61 = r0[r1]
            r1 = 1
            boolean r62 = r0[r1]
            int r0 = r15.mHorizontalResolution
            r5 = 2
            r31 = 0
            if (r0 == r5) goto L_0x04da
            boolean r0 = r15.resolvedHorizontal
            if (r0 != 0) goto L_0x04da
            if (r75 == 0) goto L_0x0434
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            if (r0 == 0) goto L_0x0434
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x0434
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 != 0) goto L_0x03a2
            r6 = 8
            goto L_0x0436
        L_0x03a2:
            if (r75 == 0) goto L_0x041c
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r13, r0)
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r12, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0404
            if (r4 == 0) goto L_0x03ec
            boolean[] r0 = r15.isTerminalWidget
            r1 = 0
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x03ec
            boolean r0 = r73.isInHorizontalChain()
            if (r0 != 0) goto L_0x03ec
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r6 = 8
            r14.addGreaterThan(r0, r12, r1, r6)
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x04ee
        L_0x03ec:
            r6 = 8
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x04ee
        L_0x0404:
            r6 = 8
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x04ee
        L_0x041c:
            r6 = 8
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
            goto L_0x04ee
        L_0x0434:
            r6 = 8
        L_0x0436:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0442
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r7 = r0
            goto L_0x0444
        L_0x0442:
            r7 = r31
        L_0x0444:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x044f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mLeft
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            goto L_0x0451
        L_0x044f:
            r0 = r31
        L_0x0451:
            r16 = r6
            r6 = r0
            r18 = 1
            boolean[] r0 = r15.isTerminalWidget
            r20 = 0
            boolean r21 = r0[r20]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r22 = r0[r20]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mLeft
            r27 = r2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mRight
            r32 = r2
            int r2 = r15.f77mX
            r33 = r2
            int r2 = r15.mMinWidth
            int[] r5 = r15.mMaxDimension
            r35 = r5[r20]
            float r5 = r15.mHorizontalBiasPercent
            r19 = 1
            r0 = r0[r19]
            r36 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r1) goto L_0x0481
            r37 = r19
            goto L_0x0483
        L_0x0481:
            r37 = r20
        L_0x0483:
            int r0 = r15.mMatchConstraintMinWidth
            r24 = r0
            int r0 = r15.mMatchConstraintMaxWidth
            r25 = r0
            float r0 = r15.mMatchConstraintPercentWidth
            r26 = r0
            r0 = r73
            r1 = r74
            r63 = r27
            r16 = r32
            r27 = r33
            r32 = r2
            r2 = r18
            r65 = r3
            r64 = r23
            r3 = r4
            r66 = r4
            r4 = r64
            r18 = r5
            r5 = r21
            r67 = r8
            r8 = r22
            r68 = r9
            r9 = r29
            r69 = r10
            r10 = r36
            r70 = r11
            r11 = r16
            r71 = r12
            r12 = r27
            r72 = r13
            r13 = r60
            r14 = r32
            r15 = r35
            r16 = r18
            r18 = r37
            r19 = r56
            r20 = r57
            r21 = r61
            r22 = r59
            r23 = r58
            r27 = r30
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27)
            goto L_0x04ee
        L_0x04da:
            r63 = r2
            r65 = r3
            r66 = r4
            r67 = r8
            r68 = r9
            r69 = r10
            r70 = r11
            r71 = r12
            r72 = r13
            r64 = r23
        L_0x04ee:
            r0 = 1
            if (r75 == 0) goto L_0x0552
            r7 = r73
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            if (r1 == 0) goto L_0x0554
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.start
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x0554
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.end
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x0554
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.start
            int r1 = r1.value
            r8 = r74
            r9 = r70
            r8.addEquality(r9, r1)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.end
            int r1 = r1.value
            r10 = r69
            r8.addEquality(r10, r1)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r7.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.baseline
            int r1 = r1.value
            r11 = r68
            r8.addEquality(r11, r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r7.mParent
            if (r1 == 0) goto L_0x054c
            if (r57 != 0) goto L_0x0547
            if (r64 == 0) goto L_0x0547
            boolean[] r2 = r7.isTerminalWidget
            r3 = 1
            boolean r2 = r2[r3]
            if (r2 == 0) goto L_0x0544
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mBottom
            androidx.constraintlayout.solver.SolverVariable r1 = r8.createObjectVariable(r1)
            r2 = 8
            r12 = 0
            r8.addGreaterThan(r1, r10, r12, r2)
            goto L_0x0550
        L_0x0544:
            r2 = 8
            goto L_0x054a
        L_0x0547:
            r2 = 8
            r3 = 1
        L_0x054a:
            r12 = 0
            goto L_0x0550
        L_0x054c:
            r2 = 8
            r3 = 1
            r12 = 0
        L_0x0550:
            r0 = 0
            goto L_0x0560
        L_0x0552:
            r7 = r73
        L_0x0554:
            r8 = r74
            r11 = r68
            r10 = r69
            r9 = r70
            r2 = 8
            r3 = 1
            r12 = 0
        L_0x0560:
            int r1 = r7.mVerticalResolution
            r4 = 2
            if (r1 != r4) goto L_0x0568
            r0 = 0
            r13 = r0
            goto L_0x0569
        L_0x0568:
            r13 = r0
        L_0x0569:
            if (r13 == 0) goto L_0x0647
            boolean r0 = r7.resolvedVertical
            if (r0 != 0) goto L_0x0647
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r7.mListDimensionBehaviors
            r0 = r0[r3]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r1) goto L_0x057d
            boolean r0 = r7 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r0 == 0) goto L_0x057d
            r0 = r3
            goto L_0x057e
        L_0x057d:
            r0 = r12
        L_0x057e:
            if (r0 == 0) goto L_0x0585
            r28 = 0
            r1 = r28
            goto L_0x0587
        L_0x0585:
            r1 = r28
        L_0x0587:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r7.mParent
            if (r4 == 0) goto L_0x0592
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mBottom
            androidx.constraintlayout.solver.SolverVariable r4 = r8.createObjectVariable(r4)
            goto L_0x0594
        L_0x0592:
            r4 = r31
        L_0x0594:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r7.mParent
            if (r5 == 0) goto L_0x05a1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTop
            androidx.constraintlayout.solver.SolverVariable r5 = r8.createObjectVariable(r5)
            r34 = r5
            goto L_0x05a3
        L_0x05a1:
            r34 = r31
        L_0x05a3:
            int r5 = r7.mBaselineDistance
            if (r5 > 0) goto L_0x05ab
            int r5 = r7.mVisibility
            if (r5 != r2) goto L_0x05e4
        L_0x05ab:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r7.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTarget
            if (r5 == 0) goto L_0x05d5
            int r5 = r73.getBaselineDistance()
            r8.addEquality(r11, r9, r5, r2)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r7.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTarget
            androidx.constraintlayout.solver.SolverVariable r5 = r8.createObjectVariable(r5)
            r6 = 0
            r8.addEquality(r11, r5, r6, r2)
            r30 = 0
            if (r64 == 0) goto L_0x05d2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r7.mBottom
            androidx.constraintlayout.solver.SolverVariable r2 = r8.createObjectVariable(r2)
            r14 = 5
            r8.addGreaterThan(r4, r2, r12, r14)
        L_0x05d2:
            r2 = r30
            goto L_0x05e6
        L_0x05d5:
            int r5 = r7.mVisibility
            if (r5 != r2) goto L_0x05dd
            r8.addEquality(r11, r9, r12, r2)
            goto L_0x05e4
        L_0x05dd:
            int r5 = r73.getBaselineDistance()
            r8.addEquality(r11, r9, r5, r2)
        L_0x05e4:
            r2 = r30
        L_0x05e6:
            r30 = 0
            boolean[] r5 = r7.isTerminalWidget
            boolean r33 = r5[r3]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r5 = r7.mListDimensionBehaviors
            r36 = r5[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r7.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r7.mBottom
            int r15 = r7.f78mY
            int r12 = r7.mMinHeight
            r68 = r11
            int[] r11 = r7.mMaxDimension
            r43 = r11[r3]
            float r11 = r7.mVerticalBiasPercent
            r16 = 0
            r5 = r5[r16]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r5 != r3) goto L_0x060b
            r46 = 1
            goto L_0x060d
        L_0x060b:
            r46 = 0
        L_0x060d:
            int r3 = r7.mMatchConstraintMinHeight
            r52 = r3
            int r3 = r7.mMatchConstraintMaxHeight
            r53 = r3
            float r3 = r7.mMatchConstraintPercentHeight
            r54 = r3
            r28 = r73
            r29 = r74
            r31 = r64
            r32 = r66
            r35 = r4
            r37 = r0
            r38 = r6
            r39 = r14
            r40 = r15
            r41 = r1
            r42 = r12
            r44 = r11
            r47 = r57
            r48 = r56
            r49 = r62
            r50 = r58
            r51 = r59
            r55 = r2
            r28.applyConstraints(r29, r30, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40, r41, r42, r43, r44, r45, r46, r47, r48, r49, r50, r51, r52, r53, r54, r55)
            r29 = r0
            r28 = r1
            r30 = r2
            goto L_0x0649
        L_0x0647:
            r68 = r11
        L_0x0649:
            if (r67 == 0) goto L_0x066f
            r11 = 8
            int r0 = r7.mResolvedDimensionRatioSide
            r1 = 1
            if (r0 != r1) goto L_0x0661
            float r5 = r7.mResolvedDimensionRatio
            r0 = r74
            r1 = r10
            r2 = r9
            r3 = r71
            r4 = r72
            r6 = r11
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x066f
        L_0x0661:
            float r5 = r7.mResolvedDimensionRatio
            r0 = r74
            r1 = r71
            r2 = r72
            r3 = r10
            r4 = r9
            r6 = r11
            r0.addRatio(r1, r2, r3, r4, r5, r6)
        L_0x066f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            boolean r0 = r0.isConnected()
            if (r0 == 0) goto L_0x0695
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.getTarget()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.getOwner()
            float r1 = r7.mCircleConstraintAngle
            r2 = 1119092736(0x42b40000, float:90.0)
            float r1 = r1 + r2
            double r1 = (double) r1
            double r1 = java.lang.Math.toRadians(r1)
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r7.mCenter
            int r2 = r2.getMargin()
            r8.addCenterPoint(r7, r0, r1, r2)
        L_0x0695:
            r0 = 0
            r7.resolvedHorizontal = r0
            r7.resolvedVertical = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.addToSolver(androidx.constraintlayout.solver.LinearSystem, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public boolean addFirst() {
        return (this instanceof VirtualLayout) || (this instanceof Guideline);
    }

    public void setupDimensionRatio(boolean hparentWrapContent, boolean vparentWrapContent, boolean horizontalDimensionFixed, boolean verticalDimensionFixed) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (horizontalDimensionFixed && !verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!horizontalDimensionFixed && verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int i = this.mMatchConstraintMinWidth;
            if (i > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (i == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:243:0x05df, code lost:
        if ((r3 instanceof androidx.constraintlayout.solver.widgets.Barrier) != false) goto L_0x05e4;
     */
    /* JADX WARNING: Removed duplicated region for block: B:220:0x056b  */
    /* JADX WARNING: Removed duplicated region for block: B:230:0x05b4  */
    /* JADX WARNING: Removed duplicated region for block: B:235:0x05ce A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:236:0x05cf  */
    /* JADX WARNING: Removed duplicated region for block: B:312:0x06b2 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyConstraints(androidx.constraintlayout.solver.LinearSystem r38, boolean r39, boolean r40, boolean r41, boolean r42, androidx.constraintlayout.solver.SolverVariable r43, androidx.constraintlayout.solver.SolverVariable r44, androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour r45, boolean r46, androidx.constraintlayout.solver.widgets.ConstraintAnchor r47, androidx.constraintlayout.solver.widgets.ConstraintAnchor r48, int r49, int r50, int r51, int r52, float r53, boolean r54, boolean r55, boolean r56, boolean r57, boolean r58, int r59, int r60, int r61, int r62, float r63, boolean r64) {
        /*
            r37 = this;
            r0 = r37
            r10 = r38
            r11 = r43
            r12 = r44
            r13 = r47
            r14 = r48
            r15 = r51
            r9 = r52
            r8 = r60
            r1 = r61
            r2 = r62
            androidx.constraintlayout.solver.SolverVariable r7 = r10.createObjectVariable(r13)
            androidx.constraintlayout.solver.SolverVariable r6 = r10.createObjectVariable(r14)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r47.getTarget()
            androidx.constraintlayout.solver.SolverVariable r5 = r10.createObjectVariable(r3)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r48.getTarget()
            androidx.constraintlayout.solver.SolverVariable r4 = r10.createObjectVariable(r3)
            androidx.constraintlayout.solver.Metrics r3 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            if (r3 == 0) goto L_0x0040
            androidx.constraintlayout.solver.Metrics r3 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            long r11 = r3.nonresolvedWidgets
            r16 = 1
            long r11 = r11 + r16
            r3.nonresolvedWidgets = r11
        L_0x0040:
            boolean r11 = r47.isConnected()
            boolean r12 = r48.isConnected()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r0.mCenter
            boolean r16 = r3.isConnected()
            r3 = 0
            r17 = 0
            if (r11 == 0) goto L_0x0055
            int r17 = r17 + 1
        L_0x0055:
            if (r12 == 0) goto L_0x0059
            int r17 = r17 + 1
        L_0x0059:
            if (r16 == 0) goto L_0x0060
            int r17 = r17 + 1
            r8 = r17
            goto L_0x0062
        L_0x0060:
            r8 = r17
        L_0x0062:
            if (r54 == 0) goto L_0x0069
            r17 = 3
            r14 = r17
            goto L_0x006b
        L_0x0069:
            r14 = r59
        L_0x006b:
            int[] r17 = androidx.constraintlayout.solver.widgets.ConstraintWidget.C02141.f80xdde91696
            int r18 = r45.ordinal()
            r19 = r3
            r3 = r17[r18]
            r13 = 1
            if (r3 == r13) goto L_0x008e
            r13 = 2
            if (r3 == r13) goto L_0x008c
            r13 = 3
            if (r3 == r13) goto L_0x008a
            r13 = 4
            if (r3 == r13) goto L_0x0084
            r3 = r19
            goto L_0x0090
        L_0x0084:
            if (r14 == r13) goto L_0x0088
            r3 = 1
            goto L_0x0089
        L_0x0088:
            r3 = 0
        L_0x0089:
            goto L_0x0090
        L_0x008a:
            r3 = 0
            goto L_0x0090
        L_0x008c:
            r3 = 0
            goto L_0x0090
        L_0x008e:
            r3 = 0
        L_0x0090:
            int r13 = r0.mVisibility
            r19 = r4
            r4 = 8
            if (r13 != r4) goto L_0x009d
            r13 = 0
            r3 = 0
            r21 = r3
            goto L_0x00a1
        L_0x009d:
            r13 = r50
            r21 = r3
        L_0x00a1:
            if (r64 == 0) goto L_0x00bc
            if (r11 != 0) goto L_0x00af
            if (r12 != 0) goto L_0x00af
            if (r16 != 0) goto L_0x00af
            r3 = r49
            r10.addEquality(r7, r3)
            goto L_0x00bc
        L_0x00af:
            r3 = r49
            if (r11 == 0) goto L_0x00bc
            if (r12 != 0) goto L_0x00bc
            int r3 = r47.getMargin()
            r10.addEquality(r7, r5, r3, r4)
        L_0x00bc:
            if (r21 != 0) goto L_0x00f0
            if (r46 == 0) goto L_0x00d8
            r3 = 3
            r4 = 0
            r10.addEquality(r6, r7, r4, r3)
            if (r15 <= 0) goto L_0x00cd
            r4 = 8
            r10.addGreaterThan(r6, r7, r15, r4)
            goto L_0x00cf
        L_0x00cd:
            r4 = 8
        L_0x00cf:
            r3 = 2147483647(0x7fffffff, float:NaN)
            if (r9 >= r3) goto L_0x00db
            r10.addLowerThan(r6, r7, r9, r4)
            goto L_0x00db
        L_0x00d8:
            r10.addEquality(r6, r7, r13, r4)
        L_0x00db:
            r22 = r2
            r28 = r5
            r25 = r8
            r9 = r19
            r23 = r21
            r27 = 3
            r19 = r1
            r8 = r6
            r21 = r13
            r13 = r42
            goto L_0x0226
        L_0x00f0:
            r3 = 2
            if (r8 == r3) goto L_0x0120
            if (r54 != 0) goto L_0x0120
            r3 = 1
            if (r14 == r3) goto L_0x00fa
            if (r14 != 0) goto L_0x0120
        L_0x00fa:
            r21 = 0
            int r3 = java.lang.Math.max(r1, r13)
            if (r2 <= 0) goto L_0x0106
            int r3 = java.lang.Math.min(r2, r3)
        L_0x0106:
            r4 = 8
            r10.addEquality(r6, r7, r3, r4)
            r22 = r2
            r28 = r5
            r25 = r8
            r9 = r19
            r23 = r21
            r27 = 3
            r19 = r1
            r8 = r6
            r21 = r13
            r13 = r42
            goto L_0x0226
        L_0x0120:
            r3 = -2
            if (r1 != r3) goto L_0x0126
            r1 = r13
            r4 = r1
            goto L_0x0127
        L_0x0126:
            r4 = r1
        L_0x0127:
            if (r2 != r3) goto L_0x012c
            r1 = r13
            r3 = r1
            goto L_0x012d
        L_0x012c:
            r3 = r2
        L_0x012d:
            if (r13 <= 0) goto L_0x0133
            r1 = 1
            if (r14 == r1) goto L_0x0133
            r13 = 0
        L_0x0133:
            if (r4 <= 0) goto L_0x013e
            r1 = 8
            r10.addGreaterThan(r6, r7, r4, r1)
            int r13 = java.lang.Math.max(r13, r4)
        L_0x013e:
            if (r3 <= 0) goto L_0x0153
            r1 = 1
            if (r40 == 0) goto L_0x0147
            r2 = 1
            if (r14 != r2) goto L_0x0147
            r1 = 0
        L_0x0147:
            if (r1 == 0) goto L_0x014e
            r2 = 8
            r10.addLowerThan(r6, r7, r3, r2)
        L_0x014e:
            int r2 = java.lang.Math.min(r13, r3)
            r13 = r2
        L_0x0153:
            r1 = 1
            if (r14 != r1) goto L_0x0187
            if (r40 == 0) goto L_0x015f
            r2 = 8
            r10.addEquality(r6, r7, r13, r2)
            r1 = 5
            goto L_0x0172
        L_0x015f:
            r2 = 8
            if (r56 == 0) goto L_0x016b
            r1 = 5
            r10.addEquality(r6, r7, r13, r1)
            r10.addLowerThan(r6, r7, r13, r2)
            goto L_0x0172
        L_0x016b:
            r1 = 5
            r10.addEquality(r6, r7, r13, r1)
            r10.addLowerThan(r6, r7, r13, r2)
        L_0x0172:
            r22 = r3
            r28 = r5
            r25 = r8
            r9 = r19
            r23 = r21
            r27 = 3
            r19 = r4
            r8 = r6
            r21 = r13
            r13 = r42
            goto L_0x0226
        L_0x0187:
            r1 = 5
            r2 = 8
            r1 = 2
            if (r14 != r1) goto L_0x0213
            r1 = 0
            r23 = 0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = r47.getType()
            r61 = r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            if (r2 == r1) goto L_0x01c2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = r47.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r1 != r2) goto L_0x01a3
            goto L_0x01c2
        L_0x01a3:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.getAnchor(r2)
            androidx.constraintlayout.solver.SolverVariable r1 = r10.createObjectVariable(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r0.mParent
            r61 = r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r2.getAnchor(r1)
            androidx.constraintlayout.solver.SolverVariable r1 = r10.createObjectVariable(r1)
            r24 = r61
            r23 = r1
            goto L_0x01e0
        L_0x01c2:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.getAnchor(r2)
            androidx.constraintlayout.solver.SolverVariable r1 = r10.createObjectVariable(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r0.mParent
            r61 = r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r2.getAnchor(r1)
            androidx.constraintlayout.solver.SolverVariable r1 = r10.createObjectVariable(r1)
            r24 = r61
            r23 = r1
        L_0x01e0:
            androidx.constraintlayout.solver.ArrayRow r1 = r38.createRow()
            r25 = 5
            r26 = 8
            r2 = r6
            r22 = r3
            r27 = 3
            r36 = r25
            r25 = r8
            r8 = r36
            r3 = r7
            r9 = r19
            r8 = r26
            r19 = r4
            r4 = r23
            r28 = r5
            r5 = r24
            r8 = r6
            r6 = r63
            androidx.constraintlayout.solver.ArrayRow r1 = r1.createRowDimensionRatio(r2, r3, r4, r5, r6)
            r10.addConstraint(r1)
            r21 = 0
            r23 = r21
            r21 = r13
            r13 = r42
            goto L_0x0226
        L_0x0213:
            r22 = r3
            r28 = r5
            r25 = r8
            r9 = r19
            r27 = 3
            r19 = r4
            r8 = r6
            r1 = 1
            r23 = r21
            r21 = r13
            r13 = r1
        L_0x0226:
            if (r64 == 0) goto L_0x06e4
            if (r56 == 0) goto L_0x0243
            r2 = r43
            r4 = r44
            r3 = r48
            r1 = r0
            r6 = r7
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r7 = r28
            r0 = 8
            r11 = r8
            r12 = r9
            r9 = r14
            goto L_0x06fb
        L_0x0243:
            r6 = 5
            if (r11 != 0) goto L_0x025e
            if (r12 != 0) goto L_0x025e
            if (r16 != 0) goto L_0x025e
            r2 = r43
            r1 = r0
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r14
            r8 = r6
            r6 = r7
            r7 = r28
            goto L_0x06ae
        L_0x025e:
            if (r11 == 0) goto L_0x0276
            if (r12 != 0) goto L_0x0276
            r2 = r43
            r1 = r0
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r14
            r8 = r6
            r6 = r7
            r7 = r28
            goto L_0x06ae
        L_0x0276:
            if (r11 != 0) goto L_0x02e8
            if (r12 == 0) goto L_0x02e8
            int r1 = r48.getMargin()
            int r1 = -r1
            r2 = 8
            r10.addEquality(r8, r9, r1, r2)
            if (r40 == 0) goto L_0x02d1
            boolean r1 = r0.OPTIMIZE_WRAP
            if (r1 == 0) goto L_0x02b5
            boolean r1 = r7.isFinalValue
            if (r1 == 0) goto L_0x02b5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r0.mParent
            if (r1 == 0) goto L_0x02b5
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r1 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r1
            if (r39 == 0) goto L_0x029c
            r5 = r47
            r1.addHorizontalWrapMinVariable(r5)
            goto L_0x02a1
        L_0x029c:
            r5 = r47
            r1.addVerticalWrapMinVariable(r5)
        L_0x02a1:
            r2 = r43
            r1 = r0
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r14
            r8 = r6
            r6 = r7
            r7 = r28
            goto L_0x06ae
        L_0x02b5:
            r5 = r47
            r4 = r43
            r1 = 5
            r2 = 0
            r10.addGreaterThan(r7, r4, r2, r1)
            r1 = r0
            r2 = r4
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r14
            r8 = r6
            r6 = r7
            r7 = r28
            goto L_0x06ae
        L_0x02d1:
            r4 = r43
            r5 = r47
            r1 = r0
            r2 = r4
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r14
            r8 = r6
            r6 = r7
            r7 = r28
            goto L_0x06ae
        L_0x02e8:
            r4 = r43
            r5 = r47
            r3 = r27
            if (r11 == 0) goto L_0x069d
            if (r12 == 0) goto L_0x069d
            r24 = 1
            r1 = 0
            r26 = 0
            r2 = 0
            r27 = 5
            r29 = 4
            r30 = 6
            if (r40 == 0) goto L_0x0302
            r27 = 5
        L_0x0302:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r5.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r3.mOwner
            r50 = r1
            r31 = r11
            r11 = r14
            r14 = r48
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r14.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r1.mOwner
            r32 = r12
            androidx.constraintlayout.solver.widgets.ConstraintWidget r12 = r37.getParent()
            if (r23 == 0) goto L_0x04e9
            if (r11 != 0) goto L_0x03a6
            if (r22 != 0) goto L_0x0350
            if (r19 != 0) goto L_0x0350
            r26 = 1
            r17 = 8
            r20 = 8
            r61 = r2
            r15 = r28
            boolean r2 = r15.isFinalValue
            if (r2 == 0) goto L_0x0345
            boolean r2 = r9.isFinalValue
            if (r2 == 0) goto L_0x0345
            int r2 = r47.getMargin()
            r62 = r6
            r6 = 8
            r10.addEquality(r7, r15, r2, r6)
            int r2 = r48.getMargin()
            int r2 = -r2
            r10.addEquality(r8, r9, r2, r6)
            return
        L_0x0345:
            r62 = r6
            r6 = 8
            r2 = r50
            r27 = r61
            r29 = r20
            goto L_0x0363
        L_0x0350:
            r61 = r2
            r62 = r6
            r15 = r28
            r6 = 8
            r2 = 1
            r17 = 5
            r20 = 5
            r24 = 1
            r27 = 1
            r29 = r20
        L_0x0363:
            boolean r6 = r3 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r6 != 0) goto L_0x0388
            boolean r6 = r1 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r6 == 0) goto L_0x036c
            goto L_0x0388
        L_0x036c:
            r0 = r62
            r50 = r1
            r62 = r3
            r61 = r7
            r28 = r11
            r34 = r12
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r44
            r36 = r17
            r17 = r2
            r2 = r27
            r27 = r36
            goto L_0x0553
        L_0x0388:
            r29 = 4
            r0 = r62
            r50 = r1
            r62 = r3
            r61 = r7
            r28 = r11
            r34 = r12
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r44
            r36 = r17
            r17 = r2
            r2 = r27
            r27 = r36
            goto L_0x0553
        L_0x03a6:
            r61 = r2
            r62 = r6
            r15 = r28
            r2 = 1
            if (r11 != r2) goto L_0x03ca
            r2 = 1
            r6 = 1
            r27 = 8
            r0 = r62
            r50 = r1
            r17 = r2
            r62 = r3
            r2 = r6
            r61 = r7
            r28 = r11
            r34 = r12
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x03ca:
            r6 = 3
            if (r11 != r6) goto L_0x04cf
            int r2 = r0.mResolvedDimensionRatioSide
            r6 = -1
            if (r2 != r6) goto L_0x042b
            r2 = 1
            r6 = 1
            r26 = 1
            r27 = 8
            r29 = 5
            if (r57 == 0) goto L_0x0412
            r29 = 5
            r30 = 4
            if (r40 == 0) goto L_0x03fb
            r30 = 5
            r0 = r62
            r50 = r1
            r17 = r2
            r62 = r3
            r2 = r6
            r61 = r7
            r28 = r11
            r34 = r12
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x03fb:
            r0 = r62
            r50 = r1
            r17 = r2
            r62 = r3
            r2 = r6
            r61 = r7
            r28 = r11
            r34 = r12
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x0412:
            r30 = 8
            r0 = r62
            r50 = r1
            r17 = r2
            r62 = r3
            r2 = r6
            r61 = r7
            r28 = r11
            r34 = r12
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x042b:
            r2 = 1
            r6 = 1
            r26 = 1
            if (r54 == 0) goto L_0x0460
            r50 = r2
            r28 = r11
            r33 = r25
            r2 = 2
            r11 = r60
            if (r11 == r2) goto L_0x0443
            r2 = 1
            if (r11 != r2) goto L_0x0440
            goto L_0x0443
        L_0x0440:
            r20 = 0
            goto L_0x0445
        L_0x0443:
            r20 = 1
        L_0x0445:
            r2 = r20
            if (r2 != 0) goto L_0x044d
            r27 = 8
            r29 = 5
        L_0x044d:
            r17 = r50
            r0 = r62
            r50 = r1
            r62 = r3
            r2 = r6
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x0460:
            r50 = r2
            r28 = r11
            r33 = r25
            r11 = r60
            r27 = 5
            if (r22 <= 0) goto L_0x0481
            r29 = 5
            r17 = r50
            r0 = r62
            r50 = r1
            r62 = r3
            r2 = r6
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x0481:
            if (r22 != 0) goto L_0x04bc
            if (r19 != 0) goto L_0x04bc
            if (r57 != 0) goto L_0x049c
            r29 = 8
            r17 = r50
            r0 = r62
            r50 = r1
            r62 = r3
            r2 = r6
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x049c:
            if (r3 == r12) goto L_0x04a4
            if (r1 == r12) goto L_0x04a4
            r2 = 4
            r27 = r2
            goto L_0x04a7
        L_0x04a4:
            r2 = 5
            r27 = r2
        L_0x04a7:
            r29 = 4
            r17 = r50
            r0 = r62
            r50 = r1
            r62 = r3
            r2 = r6
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x04bc:
            r17 = r50
            r0 = r62
            r50 = r1
            r62 = r3
            r2 = r6
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x04cf:
            r28 = r11
            r33 = r25
            r11 = r60
            r17 = r50
            r2 = r61
            r0 = r62
            r50 = r1
            r62 = r3
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            goto L_0x0553
        L_0x04e9:
            r61 = r2
            r62 = r6
            r33 = r25
            r15 = r28
            r28 = r11
            r11 = r60
            r17 = 1
            r20 = 1
            boolean r2 = r15.isFinalValue
            if (r2 == 0) goto L_0x0543
            boolean r2 = r9.isFinalValue
            if (r2 == 0) goto L_0x0543
            int r6 = r47.getMargin()
            int r18 = r48.getMargin()
            r25 = 8
            r2 = r1
            r1 = r38
            r11 = r2
            r2 = r7
            r50 = r11
            r11 = r3
            r3 = r15
            r0 = r4
            r4 = r6
            r5 = r53
            r0 = r62
            r6 = r9
            r61 = r7
            r7 = r8
            r62 = r11
            r11 = r8
            r8 = r18
            r34 = r12
            r12 = r9
            r9 = r25
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            if (r40 == 0) goto L_0x0540
            if (r13 == 0) goto L_0x0540
            r1 = 0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r14.mTarget
            if (r2 == 0) goto L_0x0538
            int r1 = r48.getMargin()
        L_0x0538:
            r9 = r44
            if (r12 == r9) goto L_0x0542
            r10.addGreaterThan(r9, r11, r1, r0)
            goto L_0x0542
        L_0x0540:
            r9 = r44
        L_0x0542:
            return
        L_0x0543:
            r0 = r62
            r50 = r1
            r62 = r3
            r61 = r7
            r11 = r8
            r34 = r12
            r12 = r9
            r9 = r44
            r2 = r20
        L_0x0553:
            if (r2 == 0) goto L_0x0563
            if (r15 != r12) goto L_0x0563
            r8 = r62
            r7 = r34
            if (r8 == r7) goto L_0x0567
            r2 = 0
            r24 = 0
            r20 = r2
            goto L_0x0569
        L_0x0563:
            r8 = r62
            r7 = r34
        L_0x0567:
            r20 = r2
        L_0x0569:
            if (r17 == 0) goto L_0x05b4
            if (r23 != 0) goto L_0x0587
            if (r55 != 0) goto L_0x0587
            if (r57 != 0) goto L_0x0587
            r6 = r0
            r0 = r43
            if (r15 != r0) goto L_0x058a
            if (r12 != r9) goto L_0x058a
            r1 = 8
            r2 = 8
            r3 = 0
            r4 = 0
            r30 = r1
            r27 = r2
            r25 = r3
            r24 = r4
            goto L_0x058e
        L_0x0587:
            r6 = r0
            r0 = r43
        L_0x058a:
            r25 = r24
            r24 = r40
        L_0x058e:
            int r4 = r47.getMargin()
            int r34 = r48.getMargin()
            r1 = r38
            r2 = r61
            r5 = 3
            r3 = r15
            r0 = r5
            r5 = r53
            r0 = r6
            r35 = 8
            r6 = r12
            r14 = r7
            r7 = r11
            r62 = r0
            r59 = r13
            r0 = r35
            r13 = r8
            r8 = r34
            r9 = r30
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x05c0
        L_0x05b4:
            r62 = r0
            r14 = r7
            r59 = r13
            r0 = 8
            r13 = r8
            r25 = r24
            r24 = r40
        L_0x05c0:
            r1 = r37
            r2 = r43
            int r3 = r1.mVisibility
            if (r3 != r0) goto L_0x05cf
            boolean r3 = r48.hasDependents()
            if (r3 != 0) goto L_0x05cf
            return
        L_0x05cf:
            if (r20 == 0) goto L_0x05fe
            if (r24 == 0) goto L_0x05e6
            if (r15 == r12) goto L_0x05e6
            if (r23 != 0) goto L_0x05e6
            boolean r3 = r13 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r3 != 0) goto L_0x05e2
            r3 = r50
            boolean r4 = r3 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r4 == 0) goto L_0x05e8
            goto L_0x05e4
        L_0x05e2:
            r3 = r50
        L_0x05e4:
            r4 = 6
            goto L_0x05ea
        L_0x05e6:
            r3 = r50
        L_0x05e8:
            r4 = r27
        L_0x05ea:
            int r5 = r47.getMargin()
            r6 = r61
            r10.addGreaterThan(r6, r15, r5, r4)
            int r5 = r48.getMargin()
            int r5 = -r5
            r10.addLowerThan(r11, r12, r5, r4)
            r27 = r4
            goto L_0x0602
        L_0x05fe:
            r3 = r50
            r6 = r61
        L_0x0602:
            if (r24 == 0) goto L_0x0619
            if (r58 == 0) goto L_0x0619
            boolean r4 = r13 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r4 != 0) goto L_0x0619
            boolean r4 = r3 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r4 != 0) goto L_0x0619
            r29 = 6
            r27 = 6
            r25 = 1
            r4 = r27
            r5 = r29
            goto L_0x061d
        L_0x0619:
            r4 = r27
            r5 = r29
        L_0x061d:
            if (r25 == 0) goto L_0x0662
            if (r26 == 0) goto L_0x0644
            if (r57 == 0) goto L_0x0625
            if (r41 == 0) goto L_0x0644
        L_0x0625:
            r7 = r5
            if (r13 == r14) goto L_0x062a
            if (r3 != r14) goto L_0x062b
        L_0x062a:
            r7 = 6
        L_0x062b:
            boolean r8 = r13 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r8 != 0) goto L_0x0633
            boolean r8 = r3 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r8 == 0) goto L_0x0634
        L_0x0633:
            r7 = 5
        L_0x0634:
            boolean r8 = r13 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r8 != 0) goto L_0x063c
            boolean r8 = r3 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r8 == 0) goto L_0x063d
        L_0x063c:
            r7 = 5
        L_0x063d:
            if (r57 == 0) goto L_0x0640
            r7 = 5
        L_0x0640:
            int r5 = java.lang.Math.max(r7, r5)
        L_0x0644:
            if (r24 == 0) goto L_0x0653
            int r5 = java.lang.Math.min(r4, r5)
            if (r54 == 0) goto L_0x0653
            if (r57 != 0) goto L_0x0653
            if (r13 == r14) goto L_0x0652
            if (r3 != r14) goto L_0x0653
        L_0x0652:
            r5 = 4
        L_0x0653:
            int r7 = r47.getMargin()
            r10.addEquality(r6, r15, r7, r5)
            int r7 = r48.getMargin()
            int r7 = -r7
            r10.addEquality(r11, r12, r7, r5)
        L_0x0662:
            if (r24 == 0) goto L_0x0676
            r7 = 0
            if (r2 != r15) goto L_0x066b
            int r7 = r47.getMargin()
        L_0x066b:
            if (r15 == r2) goto L_0x0673
            r8 = r62
            r10.addGreaterThan(r6, r2, r7, r8)
            goto L_0x0678
        L_0x0673:
            r8 = r62
            goto L_0x0678
        L_0x0676:
            r8 = r62
        L_0x0678:
            if (r24 == 0) goto L_0x0699
            if (r23 == 0) goto L_0x0699
            r7 = r15
            if (r51 != 0) goto L_0x0696
            if (r19 != 0) goto L_0x0696
            if (r23 == 0) goto L_0x068f
            r9 = r28
            r15 = 3
            if (r9 != r15) goto L_0x068d
            r15 = 0
            r10.addGreaterThan(r11, r6, r15, r0)
            goto L_0x06b0
        L_0x068d:
            r15 = 0
            goto L_0x0692
        L_0x068f:
            r9 = r28
            r15 = 0
        L_0x0692:
            r10.addGreaterThan(r11, r6, r15, r8)
            goto L_0x06b0
        L_0x0696:
            r9 = r28
            goto L_0x06b0
        L_0x0699:
            r7 = r15
            r9 = r28
            goto L_0x06b0
        L_0x069d:
            r1 = r0
            r2 = r4
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r11 = r8
            r12 = r9
            r9 = r14
            r8 = r6
            r6 = r7
            r7 = r28
        L_0x06ae:
            r24 = r40
        L_0x06b0:
            if (r24 == 0) goto L_0x06df
            if (r59 == 0) goto L_0x06df
            r0 = 0
            r3 = r48
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r3.mTarget
            if (r4 == 0) goto L_0x06bf
            int r0 = r48.getMargin()
        L_0x06bf:
            r4 = r44
            if (r12 == r4) goto L_0x06e3
            boolean r5 = r1.OPTIMIZE_WRAP
            if (r5 == 0) goto L_0x06db
            boolean r5 = r11.isFinalValue
            if (r5 == 0) goto L_0x06db
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r1.mParent
            if (r5 == 0) goto L_0x06db
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r5
            if (r39 == 0) goto L_0x06d7
            r5.addHorizontalWrapMaxVariable(r3)
            goto L_0x06da
        L_0x06d7:
            r5.addVerticalWrapMaxVariable(r3)
        L_0x06da:
            return
        L_0x06db:
            r10.addGreaterThan(r4, r11, r0, r8)
            goto L_0x06e3
        L_0x06df:
            r4 = r44
            r3 = r48
        L_0x06e3:
            return
        L_0x06e4:
            r2 = r43
            r4 = r44
            r3 = r48
            r1 = r0
            r6 = r7
            r31 = r11
            r32 = r12
            r59 = r13
            r33 = r25
            r7 = r28
            r0 = 8
            r11 = r8
            r12 = r9
            r9 = r14
        L_0x06fb:
            r5 = r33
            r8 = 2
            if (r5 >= r8) goto L_0x0744
            if (r40 == 0) goto L_0x0744
            if (r59 == 0) goto L_0x0744
            r8 = 0
            r10.addGreaterThan(r6, r2, r8, r0)
            if (r39 != 0) goto L_0x0713
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r1.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r8.mTarget
            if (r8 != 0) goto L_0x0711
            goto L_0x0713
        L_0x0711:
            r8 = 0
            goto L_0x0714
        L_0x0713:
            r8 = 1
        L_0x0714:
            if (r39 != 0) goto L_0x073e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r13 = r1.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r13 = r13.mTarget
            if (r13 == 0) goto L_0x073e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r13 = r1.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r13 = r13.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r13 = r13.mOwner
            float r14 = r13.mDimensionRatio
            r15 = 0
            int r14 = (r14 > r15 ? 1 : (r14 == r15 ? 0 : -1))
            if (r14 == 0) goto L_0x073d
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r14 = r13.mListDimensionBehaviors
            r15 = 0
            r14 = r14[r15]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r14 != r15) goto L_0x073d
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r14 = r13.mListDimensionBehaviors
            r15 = 1
            r14 = r14[r15]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r14 != r15) goto L_0x073d
            r8 = 1
            goto L_0x073e
        L_0x073d:
            r8 = 0
        L_0x073e:
            if (r8 == 0) goto L_0x0744
            r13 = 0
            r10.addGreaterThan(r4, r11, r13, r0)
        L_0x0744:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.applyConstraints(androidx.constraintlayout.solver.LinearSystem, boolean, boolean, boolean, boolean, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.solver.widgets.ConstraintAnchor, androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, boolean, boolean, boolean, int, int, int, int, float, boolean):void");
    }

    /* renamed from: androidx.constraintlayout.solver.widgets.ConstraintWidget$1 */
    static /* synthetic */ class C02141 {

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type */
        static final /* synthetic */ int[] f79x4c44d048;

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour */
        static final /* synthetic */ int[] f80xdde91696;

        static {
            int[] iArr = new int[DimensionBehaviour.values().length];
            f80xdde91696 = iArr;
            try {
                iArr[DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f80xdde91696[DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f80xdde91696[DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f80xdde91696[DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            int[] iArr2 = new int[ConstraintAnchor.Type.values().length];
            f79x4c44d048 = iArr2;
            try {
                iArr2[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                f79x4c44d048[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError e13) {
            }
        }
    }

    public void updateFromSolver(LinearSystem system, boolean optimize) {
        VerticalWidgetRun verticalWidgetRun;
        HorizontalWidgetRun horizontalWidgetRun;
        int left = system.getObjectVariableValue(this.mLeft);
        int top = system.getObjectVariableValue(this.mTop);
        int right = system.getObjectVariableValue(this.mRight);
        int bottom = system.getObjectVariableValue(this.mBottom);
        if (optimize && (horizontalWidgetRun = this.horizontalRun) != null && horizontalWidgetRun.start.resolved && this.horizontalRun.end.resolved) {
            left = this.horizontalRun.start.value;
            right = this.horizontalRun.end.value;
        }
        if (optimize && (verticalWidgetRun = this.verticalRun) != null && verticalWidgetRun.start.resolved && this.verticalRun.end.resolved) {
            top = this.verticalRun.start.value;
            bottom = this.verticalRun.end.value;
        }
        int h = bottom - top;
        if (right - left < 0 || h < 0 || left == Integer.MIN_VALUE || left == Integer.MAX_VALUE || top == Integer.MIN_VALUE || top == Integer.MAX_VALUE || right == Integer.MIN_VALUE || right == Integer.MAX_VALUE || bottom == Integer.MIN_VALUE || bottom == Integer.MAX_VALUE) {
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
        }
        setFrame(left, top, right, bottom);
    }

    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> map) {
        this.mHorizontalResolution = src.mHorizontalResolution;
        this.mVerticalResolution = src.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = src.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = src.mMatchConstraintDefaultHeight;
        int[] iArr = this.mResolvedMatchConstraintDefault;
        int[] iArr2 = src.mResolvedMatchConstraintDefault;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.mMatchConstraintMinWidth = src.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = src.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = src.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = src.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = src.mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = src.mIsWidthWrapContent;
        this.mIsHeightWrapContent = src.mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = src.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = src.mResolvedDimensionRatio;
        int[] iArr3 = src.mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(iArr3, iArr3.length);
        this.mCircleConstraintAngle = src.mCircleConstraintAngle;
        this.hasBaseline = src.hasBaseline;
        this.inPlaceholder = src.inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = (DimensionBehaviour[]) Arrays.copyOf(this.mListDimensionBehaviors, 2);
        ConstraintWidget constraintWidget = null;
        this.mParent = this.mParent == null ? null : map.get(src.mParent);
        this.mWidth = src.mWidth;
        this.mHeight = src.mHeight;
        this.mDimensionRatio = src.mDimensionRatio;
        this.mDimensionRatioSide = src.mDimensionRatioSide;
        this.f77mX = src.f77mX;
        this.f78mY = src.f78mY;
        this.mRelX = src.mRelX;
        this.mRelY = src.mRelY;
        this.mOffsetX = src.mOffsetX;
        this.mOffsetY = src.mOffsetY;
        this.mBaselineDistance = src.mBaselineDistance;
        this.mMinWidth = src.mMinWidth;
        this.mMinHeight = src.mMinHeight;
        this.mHorizontalBiasPercent = src.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = src.mVerticalBiasPercent;
        this.mCompanionWidget = src.mCompanionWidget;
        this.mContainerItemSkip = src.mContainerItemSkip;
        this.mVisibility = src.mVisibility;
        this.mDebugName = src.mDebugName;
        this.mType = src.mType;
        this.mDistToTop = src.mDistToTop;
        this.mDistToLeft = src.mDistToLeft;
        this.mDistToRight = src.mDistToRight;
        this.mDistToBottom = src.mDistToBottom;
        this.mLeftHasCentered = src.mLeftHasCentered;
        this.mRightHasCentered = src.mRightHasCentered;
        this.mTopHasCentered = src.mTopHasCentered;
        this.mBottomHasCentered = src.mBottomHasCentered;
        this.mHorizontalWrapVisited = src.mHorizontalWrapVisited;
        this.mVerticalWrapVisited = src.mVerticalWrapVisited;
        this.mHorizontalChainStyle = src.mHorizontalChainStyle;
        this.mVerticalChainStyle = src.mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = src.mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = src.mVerticalChainFixedPosition;
        float[] fArr = this.mWeight;
        float[] fArr2 = src.mWeight;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.mListNextMatchConstraintsWidget;
        ConstraintWidget[] constraintWidgetArr2 = src.mListNextMatchConstraintsWidget;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.mNextChainWidget;
        ConstraintWidget[] constraintWidgetArr4 = src.mNextChainWidget;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget2 = src.mHorizontalNextWidget;
        this.mHorizontalNextWidget = constraintWidget2 == null ? null : map.get(constraintWidget2);
        ConstraintWidget constraintWidget3 = src.mVerticalNextWidget;
        if (constraintWidget3 != null) {
            constraintWidget = map.get(constraintWidget3);
        }
        this.mVerticalNextWidget = constraintWidget;
    }

    public void updateFromRuns(boolean updateHorizontal, boolean updateVertical) {
        boolean updateHorizontal2 = updateHorizontal & this.horizontalRun.isResolved();
        boolean updateVertical2 = updateVertical & this.verticalRun.isResolved();
        int left = this.horizontalRun.start.value;
        int top = this.verticalRun.start.value;
        int right = this.horizontalRun.end.value;
        int bottom = this.verticalRun.end.value;
        int h = bottom - top;
        if (right - left < 0 || h < 0 || left == Integer.MIN_VALUE || left == Integer.MAX_VALUE || top == Integer.MIN_VALUE || top == Integer.MAX_VALUE || right == Integer.MIN_VALUE || right == Integer.MAX_VALUE || bottom == Integer.MIN_VALUE || bottom == Integer.MAX_VALUE) {
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
        }
        int w = right - left;
        int h2 = bottom - top;
        if (updateHorizontal2) {
            this.f77mX = left;
        }
        if (updateVertical2) {
            this.f78mY = top;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (updateHorizontal2) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && w < this.mWidth) {
                w = this.mWidth;
            }
            this.mWidth = w;
            int i = this.mMinWidth;
            if (w < i) {
                this.mWidth = i;
            }
        }
        if (updateVertical2) {
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && h2 < this.mHeight) {
                h2 = this.mHeight;
            }
            this.mHeight = h2;
            int i2 = this.mMinHeight;
            if (h2 < i2) {
                this.mHeight = i2;
            }
        }
    }

    public void addChildrenToSolverByDependency(ConstraintWidgetContainer container, LinearSystem system, HashSet<ConstraintWidget> widgets, int orientation, boolean addSelf) {
        if (addSelf) {
            if (widgets.contains(this)) {
                Optimizer.checkMatchParent(container, system, this);
                widgets.remove(this);
                addToSolver(system, container.optimizeFor(64));
            } else {
                return;
            }
        }
        if (orientation == 0) {
            HashSet<ConstraintAnchor> dependents = this.mLeft.getDependents();
            if (dependents != null) {
                Iterator<ConstraintAnchor> it = dependents.iterator();
                while (it.hasNext()) {
                    it.next().mOwner.addChildrenToSolverByDependency(container, system, widgets, orientation, true);
                }
            }
            HashSet<ConstraintAnchor> dependents2 = this.mRight.getDependents();
            if (dependents2 != null) {
                Iterator<ConstraintAnchor> it2 = dependents2.iterator();
                while (it2.hasNext()) {
                    it2.next().mOwner.addChildrenToSolverByDependency(container, system, widgets, orientation, true);
                }
                return;
            }
            return;
        }
        HashSet<ConstraintAnchor> dependents3 = this.mTop.getDependents();
        if (dependents3 != null) {
            Iterator<ConstraintAnchor> it3 = dependents3.iterator();
            while (it3.hasNext()) {
                it3.next().mOwner.addChildrenToSolverByDependency(container, system, widgets, orientation, true);
            }
        }
        HashSet<ConstraintAnchor> dependents4 = this.mBottom.getDependents();
        if (dependents4 != null) {
            Iterator<ConstraintAnchor> it4 = dependents4.iterator();
            while (it4.hasNext()) {
                it4.next().mOwner.addChildrenToSolverByDependency(container, system, widgets, orientation, true);
            }
        }
        HashSet<ConstraintAnchor> dependents5 = this.mBaseline.getDependents();
        if (dependents5 != null) {
            Iterator<ConstraintAnchor> it5 = dependents5.iterator();
            while (it5.hasNext()) {
                it5.next().mOwner.addChildrenToSolverByDependency(container, system, widgets, orientation, true);
            }
        }
    }
}
