package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintWidgetContainer extends WidgetContainer {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    static int mycounter = 0;
    private WeakReference<ConstraintAnchor> horizontalWrapMax = null;
    private WeakReference<ConstraintAnchor> horizontalWrapMin = null;
    BasicMeasure mBasicMeasureSolver = new BasicMeasure(this);
    int mDebugSolverPassCount = 0;
    public DependencyGraph mDependencyGraph = new DependencyGraph(this);
    public boolean mGroupsWrapOptimized = false;
    private boolean mHeightMeasuredTooSmall = false;
    ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    public int mHorizontalChainsSize = 0;
    public boolean mHorizontalWrapOptimized = false;
    private boolean mIsRtl = false;
    public BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
    protected BasicMeasure.Measurer mMeasurer = null;
    public Metrics mMetrics;
    private int mOptimizationLevel = 257;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver = false;
    protected LinearSystem mSystem = new LinearSystem();
    ChainHead[] mVerticalChainsArray = new ChainHead[4];
    public int mVerticalChainsSize = 0;
    public boolean mVerticalWrapOptimized = false;
    private boolean mWidthMeasuredTooSmall = false;
    public int mWrapFixedHeight = 0;
    public int mWrapFixedWidth = 0;
    private WeakReference<ConstraintAnchor> verticalWrapMax = null;
    private WeakReference<ConstraintAnchor> verticalWrapMin = null;

    public void invalidateGraph() {
        this.mDependencyGraph.invalidateGraph();
    }

    public void invalidateMeasures() {
        this.mDependencyGraph.invalidateMeasures();
    }

    public boolean directMeasure(boolean optimizeWrap) {
        return this.mDependencyGraph.directMeasure(optimizeWrap);
    }

    public boolean directMeasureSetup(boolean optimizeWrap) {
        return this.mDependencyGraph.directMeasureSetup(optimizeWrap);
    }

    public boolean directMeasureWithOrientation(boolean optimizeWrap, int orientation) {
        return this.mDependencyGraph.directMeasureWithOrientation(optimizeWrap, orientation);
    }

    public void defineTerminalWidgets() {
        this.mDependencyGraph.defineTerminalWidgets(getHorizontalDimensionBehaviour(), getVerticalDimensionBehaviour());
    }

    public long measure(int optimizationLevel, int widthMode, int widthSize, int heightMode, int heightSize, int lastMeasureWidth, int lastMeasureHeight, int paddingX, int paddingY) {
        this.mPaddingLeft = paddingX;
        this.mPaddingTop = paddingY;
        return this.mBasicMeasureSolver.solverMeasure(this, optimizationLevel, paddingX, paddingY, widthMode, widthSize, heightMode, heightSize, lastMeasureWidth, lastMeasureHeight);
    }

    public void updateHierarchy() {
        this.mBasicMeasureSolver.updateHierarchy(this);
    }

    public void setMeasurer(BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
        this.mDependencyGraph.setMeasurer(measurer);
    }

    public BasicMeasure.Measurer getMeasurer() {
        return this.mMeasurer;
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mSystem.fillMetrics(metrics);
    }

    public ConstraintWidgetContainer() {
    }

    public ConstraintWidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ConstraintWidgetContainer(int width, int height) {
        super(width, height);
    }

    public ConstraintWidgetContainer(String debugName, int width, int height) {
        super(width, height);
        setDebugName(debugName);
    }

    public void setOptimizationLevel(int value) {
        this.mOptimizationLevel = value;
        LinearSystem.USE_DEPENDENCY_ORDERING = optimizeFor(512);
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public boolean optimizeFor(int feature) {
        return (this.mOptimizationLevel & feature) == feature;
    }

    public String getType() {
        return "ConstraintLayout";
    }

    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mSkipSolver = false;
        super.reset();
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    /* access modifiers changed from: package-private */
    public void addVerticalWrapMinVariable(ConstraintAnchor top) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMin;
        if (weakReference == null || weakReference.get() == null || top.getFinalValue() > ((ConstraintAnchor) this.verticalWrapMin.get()).getFinalValue()) {
            this.verticalWrapMin = new WeakReference<>(top);
        }
    }

    public void addHorizontalWrapMinVariable(ConstraintAnchor left) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMin;
        if (weakReference == null || weakReference.get() == null || left.getFinalValue() > ((ConstraintAnchor) this.horizontalWrapMin.get()).getFinalValue()) {
            this.horizontalWrapMin = new WeakReference<>(left);
        }
    }

    /* access modifiers changed from: package-private */
    public void addVerticalWrapMaxVariable(ConstraintAnchor bottom) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMax;
        if (weakReference == null || weakReference.get() == null || bottom.getFinalValue() > ((ConstraintAnchor) this.verticalWrapMax.get()).getFinalValue()) {
            this.verticalWrapMax = new WeakReference<>(bottom);
        }
    }

    public void addHorizontalWrapMaxVariable(ConstraintAnchor right) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMax;
        if (weakReference == null || weakReference.get() == null || right.getFinalValue() > ((ConstraintAnchor) this.horizontalWrapMax.get()).getFinalValue()) {
            this.horizontalWrapMax = new WeakReference<>(right);
        }
    }

    private void addMinWrap(ConstraintAnchor constraintAnchor, SolverVariable parentMin) {
        this.mSystem.addGreaterThan(this.mSystem.createObjectVariable(constraintAnchor), parentMin, 0, 5);
    }

    private void addMaxWrap(ConstraintAnchor constraintAnchor, SolverVariable parentMax) {
        this.mSystem.addGreaterThan(parentMax, this.mSystem.createObjectVariable(constraintAnchor), 0, 5);
    }

    public boolean addChildrenToSolver(LinearSystem system) {
        int orientation;
        boolean optimize = optimizeFor(64);
        addToSolver(system, optimize);
        int count = this.mChildren.size();
        boolean hasBarriers = false;
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            widget.setInBarrier(0, false);
            widget.setInBarrier(1, false);
            if (widget instanceof Barrier) {
                hasBarriers = true;
            }
        }
        if (hasBarriers) {
            for (int i2 = 0; i2 < count; i2++) {
                ConstraintWidget widget2 = (ConstraintWidget) this.mChildren.get(i2);
                if (widget2 instanceof Barrier) {
                    ((Barrier) widget2).markWidgets();
                }
            }
        }
        for (int i3 = 0; i3 < count; i3++) {
            ConstraintWidget widget3 = (ConstraintWidget) this.mChildren.get(i3);
            if (widget3.addFirst()) {
                widget3.addToSolver(system, optimize);
            }
        }
        if (LinearSystem.USE_DEPENDENCY_ORDERING != 0) {
            HashSet<ConstraintWidget> widgetsToAdd = new HashSet<>();
            for (int i4 = 0; i4 < count; i4++) {
                ConstraintWidget widget4 = (ConstraintWidget) this.mChildren.get(i4);
                if (!widget4.addFirst()) {
                    widgetsToAdd.add(widget4);
                }
            }
            if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                orientation = 0;
            } else {
                orientation = 1;
            }
            addChildrenToSolverByDependency(this, system, widgetsToAdd, orientation, false);
            Iterator<ConstraintWidget> it = widgetsToAdd.iterator();
            while (it.hasNext()) {
                ConstraintWidget widget5 = it.next();
                Optimizer.checkMatchParent(this, system, widget5);
                widget5.addToSolver(system, optimize);
            }
        } else {
            for (int i5 = 0; i5 < count; i5++) {
                ConstraintWidget widget6 = (ConstraintWidget) this.mChildren.get(i5);
                if (widget6 instanceof ConstraintWidgetContainer) {
                    ConstraintWidget.DimensionBehaviour horizontalBehaviour = widget6.mListDimensionBehaviors[0];
                    ConstraintWidget.DimensionBehaviour verticalBehaviour = widget6.mListDimensionBehaviors[1];
                    if (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    if (verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    widget6.addToSolver(system, optimize);
                    if (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setHorizontalDimensionBehaviour(horizontalBehaviour);
                    }
                    if (verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setVerticalDimensionBehaviour(verticalBehaviour);
                    }
                } else {
                    Optimizer.checkMatchParent(this, system, widget6);
                    if (!widget6.addFirst()) {
                        widget6.addToSolver(system, optimize);
                    }
                }
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, system, (ArrayList<ConstraintWidget>) null, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, system, (ArrayList<ConstraintWidget>) null, 1);
        }
        return true;
    }

    public void updateChildrenFromSolver(LinearSystem system, boolean[] flags) {
        flags[2] = false;
        boolean optimize = optimizeFor(64);
        updateFromSolver(system, optimize);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromSolver(system, optimize);
        }
    }

    public void updateFromRuns(boolean updateHorizontal, boolean updateVertical) {
        super.updateFromRuns(updateHorizontal, updateVertical);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromRuns(updateHorizontal, updateVertical);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    public void setRtl(boolean isRtl) {
        this.mIsRtl = isRtl;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public static boolean measure(ConstraintWidget widget, BasicMeasure.Measurer measurer, BasicMeasure.Measure measure, int measureStrategy) {
        int measuredWidth;
        int measuredHeight;
        if (measurer == null) {
            return false;
        }
        measure.horizontalBehavior = widget.getHorizontalDimensionBehaviour();
        measure.verticalBehavior = widget.getVerticalDimensionBehaviour();
        measure.horizontalDimension = widget.getWidth();
        measure.verticalDimension = widget.getHeight();
        measure.measuredNeedsSolverPass = false;
        measure.measureStrategy = measureStrategy;
        boolean horizontalMatchConstraints = measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean verticalMatchConstraints = measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean horizontalUseRatio = horizontalMatchConstraints && widget.mDimensionRatio > 0.0f;
        boolean verticalUseRatio = verticalMatchConstraints && widget.mDimensionRatio > 0.0f;
        if (horizontalMatchConstraints && widget.hasDanglingDimension(0) && widget.mMatchConstraintDefaultWidth == 0 && !horizontalUseRatio) {
            horizontalMatchConstraints = false;
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (verticalMatchConstraints && widget.mMatchConstraintDefaultHeight == 0) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            }
        }
        if (verticalMatchConstraints && widget.hasDanglingDimension(1) && widget.mMatchConstraintDefaultHeight == 0 && !verticalUseRatio) {
            verticalMatchConstraints = false;
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (horizontalMatchConstraints && widget.mMatchConstraintDefaultWidth == 0) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            }
        }
        if (widget.isResolvedHorizontally()) {
            horizontalMatchConstraints = false;
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (widget.isResolvedVertically()) {
            verticalMatchConstraints = false;
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (horizontalUseRatio) {
            if (widget.mResolvedMatchConstraintDefault[0] == 4) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (!verticalMatchConstraints) {
                if (measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    measuredHeight = measure.verticalDimension;
                } else {
                    measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(widget, measure);
                    measuredHeight = measure.measuredHeight;
                }
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                if (widget.mDimensionRatioSide == 0 || widget.mDimensionRatioSide == -1) {
                    measure.horizontalDimension = (int) (widget.getDimensionRatio() * ((float) measuredHeight));
                } else {
                    measure.horizontalDimension = (int) (widget.getDimensionRatio() / ((float) measuredHeight));
                }
            }
        }
        if (verticalUseRatio) {
            if (widget.mResolvedMatchConstraintDefault[1] == 4) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (!horizontalMatchConstraints) {
                if (measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    measuredWidth = measure.horizontalDimension;
                } else {
                    measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(widget, measure);
                    measuredWidth = measure.measuredWidth;
                }
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                if (widget.mDimensionRatioSide == 0 || widget.mDimensionRatioSide == -1) {
                    measure.verticalDimension = (int) (((float) measuredWidth) / widget.getDimensionRatio());
                } else {
                    measure.verticalDimension = (int) (((float) measuredWidth) * widget.getDimensionRatio());
                }
            }
        }
        measurer.measure(widget, measure);
        widget.setWidth(measure.measuredWidth);
        widget.setHeight(measure.measuredHeight);
        widget.setHasBaseline(measure.measuredHasBaseline);
        widget.setBaselineDistance(measure.measuredBaseline);
        measure.measureStrategy = BasicMeasure.Measure.SELF_DIMENSIONS;
        return measure.measuredNeedsSolverPass;
    }

    /* JADX WARNING: Removed duplicated region for block: B:138:0x02d7  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x02f3  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0302  */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x0347 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layout() {
        /*
            r22 = this;
            r1 = r22
            r2 = 0
            r1.f77mX = r2
            r1.f78mY = r2
            r1.mWidthMeasuredTooSmall = r2
            r1.mHeightMeasuredTooSmall = r2
            java.util.ArrayList r0 = r1.mChildren
            int r3 = r0.size()
            int r0 = r22.getWidth()
            int r0 = java.lang.Math.max(r2, r0)
            int r4 = r22.getHeight()
            int r4 = java.lang.Math.max(r2, r4)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r5 = r1.mListDimensionBehaviors
            r6 = 1
            r5 = r5[r6]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r7 = r1.mListDimensionBehaviors
            r7 = r7[r2]
            androidx.constraintlayout.solver.Metrics r8 = r1.mMetrics
            if (r8 == 0) goto L_0x0035
            long r9 = r8.layouts
            r11 = 1
            long r9 = r9 + r11
            r8.layouts = r9
        L_0x0035:
            r8 = 0
            int r9 = r1.mOptimizationLevel
            boolean r9 = androidx.constraintlayout.solver.widgets.Optimizer.enabled(r9, r6)
            if (r9 == 0) goto L_0x0095
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r9 = r22.getMeasurer()
            androidx.constraintlayout.solver.widgets.analyzer.Direct.solvingPass(r1, r9)
            r9 = 0
        L_0x0046:
            if (r9 >= r3) goto L_0x0095
            java.util.ArrayList r10 = r1.mChildren
            java.lang.Object r10 = r10.get(r9)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r10
            boolean r11 = r10.isMeasureRequested()
            if (r11 == 0) goto L_0x0091
            boolean r11 = r10 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r11 != 0) goto L_0x0091
            boolean r11 = r10 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r11 != 0) goto L_0x0091
            boolean r11 = r10 instanceof androidx.constraintlayout.solver.widgets.VirtualLayout
            if (r11 != 0) goto L_0x0091
            boolean r11 = r10.isInVirtualLayout()
            if (r11 != 0) goto L_0x0091
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = r10.getDimensionBehaviour(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = r10.getDimensionBehaviour(r6)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r13 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r11 != r13) goto L_0x0082
            int r13 = r10.mMatchConstraintDefaultWidth
            if (r13 == r6) goto L_0x0082
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r13 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r12 != r13) goto L_0x0082
            int r13 = r10.mMatchConstraintDefaultHeight
            if (r13 == r6) goto L_0x0082
            r13 = r6
            goto L_0x0083
        L_0x0082:
            r13 = r2
        L_0x0083:
            if (r13 != 0) goto L_0x0091
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r14 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r14.<init>()
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r15 = r1.mMeasurer
            int r2 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            measure(r10, r15, r14, r2)
        L_0x0091:
            int r9 = r9 + 1
            r2 = 0
            goto L_0x0046
        L_0x0095:
            r2 = 2
            if (r3 <= r2) goto L_0x00e8
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r7 == r9) goto L_0x00a0
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r5 != r9) goto L_0x00e8
        L_0x00a0:
            int r9 = r1.mOptimizationLevel
            r10 = 1024(0x400, float:1.435E-42)
            boolean r9 = androidx.constraintlayout.solver.widgets.Optimizer.enabled(r9, r10)
            if (r9 == 0) goto L_0x00e8
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r9 = r22.getMeasurer()
            boolean r9 = androidx.constraintlayout.solver.widgets.analyzer.Grouping.simpleSolvingPass(r1, r9)
            if (r9 == 0) goto L_0x00e8
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r7 != r9) goto L_0x00ca
            int r9 = r22.getWidth()
            if (r0 >= r9) goto L_0x00c6
            if (r0 <= 0) goto L_0x00c6
            r1.setWidth(r0)
            r1.mWidthMeasuredTooSmall = r6
            goto L_0x00ca
        L_0x00c6:
            int r0 = r22.getWidth()
        L_0x00ca:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r5 != r9) goto L_0x00e0
            int r9 = r22.getHeight()
            if (r4 >= r9) goto L_0x00dc
            if (r4 <= 0) goto L_0x00dc
            r1.setHeight(r4)
            r1.mHeightMeasuredTooSmall = r6
            goto L_0x00e0
        L_0x00dc:
            int r4 = r22.getHeight()
        L_0x00e0:
            r8 = 1
            r21 = r4
            r4 = r0
            r0 = r8
            r8 = r21
            goto L_0x00ee
        L_0x00e8:
            r21 = r4
            r4 = r0
            r0 = r8
            r8 = r21
        L_0x00ee:
            r9 = 64
            boolean r10 = r1.optimizeFor(r9)
            if (r10 != 0) goto L_0x0101
            r10 = 128(0x80, float:1.794E-43)
            boolean r10 = r1.optimizeFor(r10)
            if (r10 == 0) goto L_0x00ff
            goto L_0x0101
        L_0x00ff:
            r10 = 0
            goto L_0x0102
        L_0x0101:
            r10 = r6
        L_0x0102:
            androidx.constraintlayout.solver.LinearSystem r11 = r1.mSystem
            r12 = 0
            r11.graphOptimizer = r12
            androidx.constraintlayout.solver.LinearSystem r11 = r1.mSystem
            r11.newgraphOptimizer = r12
            int r11 = r1.mOptimizationLevel
            if (r11 == 0) goto L_0x0115
            if (r10 == 0) goto L_0x0115
            androidx.constraintlayout.solver.LinearSystem r11 = r1.mSystem
            r11.newgraphOptimizer = r6
        L_0x0115:
            r11 = 0
            java.util.ArrayList r12 = r1.mChildren
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r13 = r22.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r14 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r13 == r14) goto L_0x012b
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r13 = r22.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r14 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r13 != r14) goto L_0x0129
            goto L_0x012b
        L_0x0129:
            r13 = 0
            goto L_0x012c
        L_0x012b:
            r13 = r6
        L_0x012c:
            r22.resetChains()
            r11 = 0
            r14 = 0
        L_0x0131:
            if (r14 >= r3) goto L_0x0149
            java.util.ArrayList r15 = r1.mChildren
            java.lang.Object r15 = r15.get(r14)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r15 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r15
            boolean r6 = r15 instanceof androidx.constraintlayout.solver.widgets.WidgetContainer
            if (r6 == 0) goto L_0x0145
            r6 = r15
            androidx.constraintlayout.solver.widgets.WidgetContainer r6 = (androidx.constraintlayout.solver.widgets.WidgetContainer) r6
            r6.layout()
        L_0x0145:
            int r14 = r14 + 1
            r6 = 1
            goto L_0x0131
        L_0x0149:
            boolean r6 = r1.optimizeFor(r9)
            r9 = 1
            r21 = r9
            r9 = r0
            r0 = r11
            r11 = r21
        L_0x0154:
            if (r11 == 0) goto L_0x034d
            int r14 = r0 + 1
            androidx.constraintlayout.solver.LinearSystem r0 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            r0.reset()     // Catch:{ Exception -> 0x020a }
            r22.resetChains()     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.LinearSystem r0 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            r1.createObjectVariables(r0)     // Catch:{ Exception -> 0x020a }
            r0 = 0
        L_0x0166:
            if (r0 >= r3) goto L_0x0179
            java.util.ArrayList r15 = r1.mChildren     // Catch:{ Exception -> 0x020a }
            java.lang.Object r15 = r15.get(r0)     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintWidget r15 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r15     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.LinearSystem r2 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            r15.createObjectVariables(r2)     // Catch:{ Exception -> 0x020a }
            int r0 = r0 + 1
            r2 = 2
            goto L_0x0166
        L_0x0179:
            androidx.constraintlayout.solver.LinearSystem r0 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            boolean r0 = r1.addChildrenToSolver(r0)     // Catch:{ Exception -> 0x020a }
            r11 = r0
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.verticalWrapMin     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x01a0
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x01a0
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.verticalWrapMin     // Catch:{ Exception -> 0x020a }
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r0     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.LinearSystem r15 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTop     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.SolverVariable r2 = r15.createObjectVariable(r2)     // Catch:{ Exception -> 0x020a }
            r1.addMinWrap(r0, r2)     // Catch:{ Exception -> 0x020a }
            r0 = 0
            r1.verticalWrapMin = r0     // Catch:{ Exception -> 0x020a }
        L_0x01a0:
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.verticalWrapMax     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x01c0
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x01c0
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.verticalWrapMax     // Catch:{ Exception -> 0x020a }
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r0     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.LinearSystem r2 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r1.mBottom     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.SolverVariable r2 = r2.createObjectVariable(r15)     // Catch:{ Exception -> 0x020a }
            r1.addMaxWrap(r0, r2)     // Catch:{ Exception -> 0x020a }
            r0 = 0
            r1.verticalWrapMax = r0     // Catch:{ Exception -> 0x020a }
        L_0x01c0:
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.horizontalWrapMin     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x01e0
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x01e0
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.horizontalWrapMin     // Catch:{ Exception -> 0x020a }
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r0     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.LinearSystem r2 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r1.mLeft     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.SolverVariable r2 = r2.createObjectVariable(r15)     // Catch:{ Exception -> 0x020a }
            r1.addMinWrap(r0, r2)     // Catch:{ Exception -> 0x020a }
            r0 = 0
            r1.horizontalWrapMin = r0     // Catch:{ Exception -> 0x020a }
        L_0x01e0:
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.horizontalWrapMax     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x0200
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            if (r0 == 0) goto L_0x0200
            java.lang.ref.WeakReference<androidx.constraintlayout.solver.widgets.ConstraintAnchor> r0 = r1.horizontalWrapMax     // Catch:{ Exception -> 0x020a }
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r0     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.LinearSystem r2 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r1.mRight     // Catch:{ Exception -> 0x020a }
            androidx.constraintlayout.solver.SolverVariable r2 = r2.createObjectVariable(r15)     // Catch:{ Exception -> 0x020a }
            r1.addMaxWrap(r0, r2)     // Catch:{ Exception -> 0x020a }
            r0 = 0
            r1.horizontalWrapMax = r0     // Catch:{ Exception -> 0x020a }
        L_0x0200:
            if (r11 == 0) goto L_0x0207
            androidx.constraintlayout.solver.LinearSystem r0 = r1.mSystem     // Catch:{ Exception -> 0x020a }
            r0.minimize()     // Catch:{ Exception -> 0x020a }
        L_0x0207:
            r18 = r10
            goto L_0x0228
        L_0x020a:
            r0 = move-exception
            r0.printStackTrace()
            java.io.PrintStream r2 = java.lang.System.out
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r18 = r10
            java.lang.String r10 = "EXCEPTION : "
            java.lang.StringBuilder r10 = r15.append(r10)
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r10 = r10.toString()
            r2.println(r10)
        L_0x0228:
            if (r11 == 0) goto L_0x0232
            androidx.constraintlayout.solver.LinearSystem r0 = r1.mSystem
            boolean[] r2 = androidx.constraintlayout.solver.widgets.Optimizer.flags
            r1.updateChildrenFromSolver(r0, r2)
            goto L_0x024a
        L_0x0232:
            androidx.constraintlayout.solver.LinearSystem r0 = r1.mSystem
            r1.updateFromSolver(r0, r6)
            r0 = 0
        L_0x0238:
            if (r0 >= r3) goto L_0x024a
            java.util.ArrayList r2 = r1.mChildren
            java.lang.Object r2 = r2.get(r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r2
            androidx.constraintlayout.solver.LinearSystem r10 = r1.mSystem
            r2.updateFromSolver(r10, r6)
            int r0 = r0 + 1
            goto L_0x0238
        L_0x024a:
            r0 = 0
            if (r13 == 0) goto L_0x02c3
            r2 = 8
            if (r14 >= r2) goto L_0x02c3
            boolean[] r2 = androidx.constraintlayout.solver.widgets.Optimizer.flags
            r10 = 2
            boolean r2 = r2[r10]
            if (r2 == 0) goto L_0x02c3
            r2 = 0
            r11 = 0
            r15 = 0
        L_0x025b:
            if (r15 >= r3) goto L_0x0285
            java.util.ArrayList r10 = r1.mChildren
            java.lang.Object r10 = r10.get(r15)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r10
            r19 = r0
            int r0 = r10.f77mX
            int r20 = r10.getWidth()
            int r0 = r0 + r20
            int r2 = java.lang.Math.max(r2, r0)
            int r0 = r10.f78mY
            int r20 = r10.getHeight()
            int r0 = r0 + r20
            int r11 = java.lang.Math.max(r11, r0)
            int r15 = r15 + 1
            r0 = r19
            r10 = 2
            goto L_0x025b
        L_0x0285:
            r19 = r0
            int r0 = r1.mMinWidth
            int r0 = java.lang.Math.max(r0, r2)
            int r2 = r1.mMinHeight
            int r2 = java.lang.Math.max(r2, r11)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r7 != r10) goto L_0x02ab
            int r10 = r22.getWidth()
            if (r10 >= r0) goto L_0x02ab
            r1.setWidth(r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r10 = r1.mListDimensionBehaviors
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r15 = 0
            r10[r15] = r11
            r9 = 1
            r10 = 1
            r19 = r10
        L_0x02ab:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r5 != r10) goto L_0x02c5
            int r10 = r22.getHeight()
            if (r10 >= r2) goto L_0x02c5
            r1.setHeight(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r10 = r1.mListDimensionBehaviors
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r15 = 1
            r10[r15] = r11
            r9 = 1
            r10 = 1
            r0 = r10
            goto L_0x02c7
        L_0x02c3:
            r19 = r0
        L_0x02c5:
            r0 = r19
        L_0x02c7:
            int r2 = r1.mMinWidth
            int r10 = r22.getWidth()
            int r2 = java.lang.Math.max(r2, r10)
            int r10 = r22.getWidth()
            if (r2 <= r10) goto L_0x02e3
            r1.setWidth(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r10 = r1.mListDimensionBehaviors
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r15 = 0
            r10[r15] = r11
            r9 = 1
            r0 = 1
        L_0x02e3:
            int r10 = r1.mMinHeight
            int r11 = r22.getHeight()
            int r10 = java.lang.Math.max(r10, r11)
            int r11 = r22.getHeight()
            if (r10 <= r11) goto L_0x0300
            r1.setHeight(r10)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r11 = r1.mListDimensionBehaviors
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r17 = 1
            r11[r17] = r15
            r9 = 1
            r0 = 1
        L_0x0300:
            if (r9 != 0) goto L_0x0346
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r11 = r1.mListDimensionBehaviors
            r15 = 0
            r11 = r11[r15]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r11 != r15) goto L_0x0323
            if (r4 <= 0) goto L_0x0323
            int r11 = r22.getWidth()
            if (r11 <= r4) goto L_0x0323
            r11 = 1
            r1.mWidthMeasuredTooSmall = r11
            r9 = 1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r11 = r1.mListDimensionBehaviors
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r16 = 0
            r11[r16] = r15
            r1.setWidth(r4)
            r0 = 1
        L_0x0323:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r11 = r1.mListDimensionBehaviors
            r15 = 1
            r11 = r11[r15]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r11 != r15) goto L_0x0344
            if (r8 <= 0) goto L_0x0344
            int r11 = r22.getHeight()
            if (r11 <= r8) goto L_0x0344
            r11 = 1
            r1.mHeightMeasuredTooSmall = r11
            r9 = 1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r15 = r1.mListDimensionBehaviors
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r17 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r15[r11] = r17
            r1.setHeight(r8)
            r0 = 1
            r11 = r0
            goto L_0x0347
        L_0x0344:
            r11 = r0
            goto L_0x0347
        L_0x0346:
            r11 = r0
        L_0x0347:
            r0 = r14
            r10 = r18
            r2 = 2
            goto L_0x0154
        L_0x034d:
            r18 = r10
            r2 = r12
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            r1.mChildren = r2
            if (r9 == 0) goto L_0x0360
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r1.mListDimensionBehaviors
            r10 = 0
            r2[r10] = r7
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r1.mListDimensionBehaviors
            r10 = 1
            r2[r10] = r5
        L_0x0360:
            androidx.constraintlayout.solver.LinearSystem r2 = r1.mSystem
            androidx.constraintlayout.solver.Cache r2 = r2.getCache()
            r1.resetSolverVariables(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.layout():void");
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList<>();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 1) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList<>();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 0) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    /* access modifiers changed from: package-private */
    public void addChain(ConstraintWidget constraintWidget, int type) {
        ConstraintWidget widget = constraintWidget;
        if (type == 0) {
            addHorizontalChain(widget);
        } else if (type == 1) {
            addVerticalChain(widget);
        }
    }

    private void addHorizontalChain(ConstraintWidget widget) {
        int i = this.mHorizontalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mHorizontalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(widget, 0, isRtl());
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget widget) {
        int i = this.mVerticalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mVerticalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(widget, 1, isRtl());
        this.mVerticalChainsSize++;
    }
}
