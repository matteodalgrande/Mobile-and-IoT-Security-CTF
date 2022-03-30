package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import java.util.ArrayList;

public class BasicMeasure {
    public static final int AT_MOST = Integer.MIN_VALUE;
    private static final boolean DEBUG = false;
    public static final int EXACTLY = 1073741824;
    public static final int FIXED = -3;
    public static final int MATCH_PARENT = -1;
    private static final int MODE_SHIFT = 30;
    public static final int UNSPECIFIED = 0;
    public static final int WRAP_CONTENT = -2;
    private ConstraintWidgetContainer constraintWidgetContainer;
    private Measure mMeasure = new Measure();
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>();

    public static class Measure {
        public static int SELF_DIMENSIONS = 0;
        public static int TRY_GIVEN_DIMENSIONS = 1;
        public static int USE_GIVEN_DIMENSIONS = 2;
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public int horizontalDimension;
        public int measureStrategy;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public int measuredHeight;
        public boolean measuredNeedsSolverPass;
        public int measuredWidth;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int verticalDimension;
    }

    public interface Measurer {
        void didMeasures();

        void measure(ConstraintWidget constraintWidget, Measure measure);
    }

    public void updateHierarchy(ConstraintWidgetContainer layout) {
        this.mVariableDimensionsWidgets.clear();
        int childCount = layout.mChildren.size();
        for (int i = 0; i < childCount; i++) {
            ConstraintWidget widget = (ConstraintWidget) layout.mChildren.get(i);
            if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                this.mVariableDimensionsWidgets.add(widget);
            }
        }
        layout.invalidateGraph();
    }

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer2) {
        this.constraintWidgetContainer = constraintWidgetContainer2;
    }

    private void measureChildren(ConstraintWidgetContainer layout) {
        int childCount = layout.mChildren.size();
        boolean optimize = layout.optimizeFor(64);
        Measurer measurer = layout.getMeasurer();
        for (int i = 0; i < childCount; i++) {
            ConstraintWidget child = (ConstraintWidget) layout.mChildren.get(i);
            if (!(child instanceof Guideline) && !(child instanceof Barrier) && !child.isInVirtualLayout() && (!optimize || child.horizontalRun == null || child.verticalRun == null || !child.horizontalRun.dimension.resolved || !child.verticalRun.dimension.resolved)) {
                boolean skip = false;
                ConstraintWidget.DimensionBehaviour widthBehavior = child.getDimensionBehaviour(0);
                ConstraintWidget.DimensionBehaviour heightBehavior = child.getDimensionBehaviour(1);
                if (widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultWidth != 1 && heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultHeight != 1) {
                    skip = true;
                }
                if (!skip && layout.optimizeFor(1) && !(child instanceof VirtualLayout)) {
                    if (widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultWidth == 0 && heightBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !child.isInHorizontalChain()) {
                        skip = true;
                    }
                    if (heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultHeight == 0 && widthBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !child.isInHorizontalChain()) {
                        skip = true;
                    }
                    if ((widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && child.mDimensionRatio > 0.0f) {
                        skip = true;
                    }
                }
                if (!skip) {
                    measure(measurer, child, Measure.SELF_DIMENSIONS);
                    if (layout.mMetrics != null) {
                        layout.mMetrics.measuredWidgets++;
                    }
                }
            }
        }
        measurer.didMeasures();
    }

    private void solveLinearSystem(ConstraintWidgetContainer layout, String reason, int w, int h) {
        int minWidth = layout.getMinWidth();
        int minHeight = layout.getMinHeight();
        layout.setMinWidth(0);
        layout.setMinHeight(0);
        layout.setWidth(w);
        layout.setHeight(h);
        layout.setMinWidth(minWidth);
        layout.setMinHeight(minHeight);
        this.constraintWidgetContainer.layout();
    }

    public long solverMeasure(ConstraintWidgetContainer layout, int optimizationLevel, int paddingX, int paddingY, int widthMode, int widthSize, int heightMode, int heightSize, int lastMeasureWidth, int lastMeasureHeight) {
        boolean ratio;
        long layoutTime;
        int heightSize2;
        int widthSize2;
        int optimizations;
        int startingWidth;
        boolean containerWrapWidth;
        Measurer measurer;
        int j;
        int sizeDependentWidgetsCount;
        int measureStrategy;
        boolean optimize;
        int measureStrategy2;
        Measurer measurer2;
        int startingWidth2;
        int optimizations2;
        boolean needSolverPass;
        boolean needSolverPass2;
        boolean z;
        boolean optimize2;
        ConstraintWidgetContainer constraintWidgetContainer2 = layout;
        int i = optimizationLevel;
        int i2 = widthMode;
        int i3 = heightMode;
        Measurer measurer3 = layout.getMeasurer();
        int childCount = constraintWidgetContainer2.mChildren.size();
        int startingWidth3 = layout.getWidth();
        int startingHeight = layout.getHeight();
        boolean optimizeWrap = Optimizer.enabled(i, 128);
        boolean optimize3 = optimizeWrap || Optimizer.enabled(i, 64);
        if (optimize3) {
            int i4 = 0;
            while (i4 < childCount) {
                ConstraintWidget child = (ConstraintWidget) constraintWidgetContainer2.mChildren.get(i4);
                boolean matchWidth = child.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                boolean optimize4 = optimize3;
                boolean ratio2 = matchWidth && (child.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && child.getDimensionRatio() > 0.0f;
                if (!child.isInHorizontalChain() || !ratio2) {
                    if (child.isInVerticalChain() && ratio2) {
                        ratio = false;
                        break;
                    }
                    boolean z2 = matchWidth;
                    if (child instanceof VirtualLayout) {
                        ratio = false;
                        break;
                    } else if (child.isInHorizontalChain() || child.isInVerticalChain()) {
                        ratio = false;
                        break;
                    } else {
                        i4++;
                        int i5 = optimizationLevel;
                        optimize3 = optimize4;
                    }
                } else {
                    ratio = false;
                    break;
                }
            }
            optimize2 = optimize3;
        } else {
            optimize2 = optimize3;
        }
        ratio = optimize2;
        if (!ratio || LinearSystem.sMetrics == null) {
            layoutTime = 0;
        } else {
            layoutTime = 0;
            LinearSystem.sMetrics.measures++;
        }
        boolean allSolved = false;
        boolean optimize5 = ((i2 == 1073741824 && i3 == 1073741824) || optimizeWrap) & ratio;
        int computations = 0;
        if (optimize5) {
            widthSize2 = Math.min(layout.getMaxWidth(), widthSize);
            heightSize2 = Math.min(layout.getMaxHeight(), heightSize);
            if (i2 == 1073741824 && layout.getWidth() != widthSize2) {
                constraintWidgetContainer2.setWidth(widthSize2);
                layout.invalidateGraph();
            }
            if (i3 == 1073741824 && layout.getHeight() != heightSize2) {
                constraintWidgetContainer2.setHeight(heightSize2);
                layout.invalidateGraph();
            }
            if (i2 == 1073741824 && i3 == 1073741824) {
                allSolved = constraintWidgetContainer2.directMeasure(optimizeWrap);
                computations = 2;
                z = true;
            } else {
                allSolved = constraintWidgetContainer2.directMeasureSetup(optimizeWrap);
                if (i2 == 1073741824) {
                    allSolved &= constraintWidgetContainer2.directMeasureWithOrientation(optimizeWrap, 0);
                    computations = 0 + 1;
                }
                if (i3 == 1073741824) {
                    z = true;
                    allSolved &= constraintWidgetContainer2.directMeasureWithOrientation(optimizeWrap, 1);
                    computations++;
                } else {
                    z = true;
                }
            }
            if (allSolved) {
                if (i2 != 1073741824) {
                    z = false;
                }
                constraintWidgetContainer2.updateFromRuns(z, i3 == 1073741824);
            }
        } else {
            widthSize2 = widthSize;
            heightSize2 = heightSize;
        }
        if (!allSolved || computations != 2) {
            int optimizations3 = layout.getOptimizationLevel();
            if (childCount > 0) {
                measureChildren(layout);
            }
            updateHierarchy(layout);
            int sizeDependentWidgetsCount2 = this.mVariableDimensionsWidgets.size();
            if (childCount > 0) {
                boolean z3 = allSolved;
                solveLinearSystem(constraintWidgetContainer2, "First pass", startingWidth3, startingHeight);
            }
            if (sizeDependentWidgetsCount2 > 0) {
                boolean containerWrapWidth2 = layout.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                boolean containerWrapHeight = layout.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                int i6 = childCount;
                boolean needSolverPass3 = false;
                int i7 = widthSize2;
                int i8 = 0;
                int minWidth = Math.max(layout.getWidth(), this.constraintWidgetContainer.getMinWidth());
                int minWidth2 = optimizeWrap;
                int minHeight = Math.max(layout.getHeight(), this.constraintWidgetContainer.getMinHeight());
                while (i8 < sizeDependentWidgetsCount2) {
                    int heightSize3 = heightSize2;
                    ConstraintWidget widget = this.mVariableDimensionsWidgets.get(i8);
                    int computations2 = computations;
                    if ((widget instanceof VirtualLayout) == 0) {
                        measurer2 = measurer3;
                        optimizations2 = optimizations3;
                        startingWidth2 = startingWidth3;
                    } else {
                        int preWidth = widget.getWidth();
                        optimizations2 = optimizations3;
                        int preHeight = widget.getHeight();
                        startingWidth2 = startingWidth3;
                        boolean needSolverPass4 = needSolverPass3 | measure(measurer3, widget, Measure.TRY_GIVEN_DIMENSIONS);
                        if (constraintWidgetContainer2.mMetrics != null) {
                            needSolverPass = needSolverPass4;
                            measurer2 = measurer3;
                            constraintWidgetContainer2.mMetrics.measuredMatchWidgets++;
                        } else {
                            needSolverPass = needSolverPass4;
                            measurer2 = measurer3;
                        }
                        int measuredWidth = widget.getWidth();
                        int measuredHeight = widget.getHeight();
                        if (measuredWidth != preWidth) {
                            widget.setWidth(measuredWidth);
                            if (!containerWrapWidth2 || widget.getRight() <= minWidth) {
                            } else {
                                int i9 = measuredWidth;
                                minWidth = Math.max(minWidth, widget.getRight() + widget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                            }
                            needSolverPass2 = true;
                        } else {
                            needSolverPass2 = needSolverPass;
                        }
                        if (measuredHeight != preHeight) {
                            widget.setHeight(measuredHeight);
                            if (!containerWrapHeight || widget.getBottom() <= minHeight) {
                            } else {
                                boolean z4 = needSolverPass2;
                                minHeight = Math.max(minHeight, widget.getBottom() + widget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                            }
                            needSolverPass2 = true;
                        } else {
                            boolean z5 = needSolverPass2;
                        }
                        needSolverPass3 = needSolverPass2 | ((VirtualLayout) widget).needSolverPass();
                    }
                    i8++;
                    heightSize2 = heightSize3;
                    computations = computations2;
                    optimizations3 = optimizations2;
                    startingWidth3 = startingWidth2;
                    measurer3 = measurer2;
                }
                Measurer measurer4 = measurer3;
                optimizations = optimizations3;
                int startingWidth4 = startingWidth3;
                int i10 = heightSize2;
                int i11 = computations;
                int maxIterations = 2;
                int j2 = 0;
                while (true) {
                    if (j2 >= maxIterations) {
                        int i12 = maxIterations;
                        int i13 = j2;
                        boolean z6 = optimize5;
                        int i14 = sizeDependentWidgetsCount2;
                        startingWidth = startingWidth4;
                        Measurer measurer5 = measurer4;
                        break;
                    }
                    int i15 = 0;
                    while (i15 < sizeDependentWidgetsCount2) {
                        ConstraintWidget widget2 = this.mVariableDimensionsWidgets.get(i15);
                        if ((!(widget2 instanceof Helper) || (widget2 instanceof VirtualLayout)) && !(widget2 instanceof Guideline) && widget2.getVisibility() != 8 && ((!optimize5 || !widget2.horizontalRun.dimension.resolved || !widget2.verticalRun.dimension.resolved) && !(widget2 instanceof VirtualLayout))) {
                            int preWidth2 = widget2.getWidth();
                            int preHeight2 = widget2.getHeight();
                            optimize = optimize5;
                            int preBaselineDistance = widget2.getBaselineDistance();
                            int measureStrategy3 = Measure.TRY_GIVEN_DIMENSIONS;
                            sizeDependentWidgetsCount = sizeDependentWidgetsCount2;
                            if (j2 == maxIterations - 1) {
                                measureStrategy2 = Measure.USE_GIVEN_DIMENSIONS;
                            } else {
                                measureStrategy2 = measureStrategy3;
                            }
                            measureStrategy = maxIterations;
                            Measurer measurer6 = measurer4;
                            boolean needSolverPass5 = needSolverPass3 | measure(measurer6, widget2, measureStrategy2);
                            if (constraintWidgetContainer2.mMetrics != null) {
                                measurer = measurer6;
                                j = j2;
                                constraintWidgetContainer2.mMetrics.measuredMatchWidgets++;
                            } else {
                                measurer = measurer6;
                                j = j2;
                            }
                            int measuredWidth2 = widget2.getWidth();
                            int measuredHeight2 = widget2.getHeight();
                            if (measuredWidth2 != preWidth2) {
                                widget2.setWidth(measuredWidth2);
                                if (!containerWrapWidth2 || widget2.getRight() <= minWidth) {
                                    containerWrapWidth = containerWrapWidth2;
                                } else {
                                    containerWrapWidth = containerWrapWidth2;
                                    minWidth = Math.max(minWidth, widget2.getRight() + widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                }
                                needSolverPass5 = true;
                            } else {
                                containerWrapWidth = containerWrapWidth2;
                            }
                            if (measuredHeight2 != preHeight2) {
                                widget2.setHeight(measuredHeight2);
                                if (containerWrapHeight && widget2.getBottom() > minHeight) {
                                    minHeight = Math.max(minHeight, widget2.getBottom() + widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                }
                                needSolverPass5 = true;
                            }
                            if (!widget2.hasBaseline() || preBaselineDistance == widget2.getBaselineDistance()) {
                                needSolverPass3 = needSolverPass5;
                            } else {
                                needSolverPass3 = true;
                            }
                        } else {
                            containerWrapWidth = containerWrapWidth2;
                            measureStrategy = maxIterations;
                            j = j2;
                            optimize = optimize5;
                            sizeDependentWidgetsCount = sizeDependentWidgetsCount2;
                            measurer = measurer4;
                        }
                        i15++;
                        optimize5 = optimize;
                        maxIterations = measureStrategy;
                        sizeDependentWidgetsCount2 = sizeDependentWidgetsCount;
                        j2 = j;
                        measurer4 = measurer;
                        containerWrapWidth2 = containerWrapWidth;
                    }
                    boolean containerWrapWidth3 = containerWrapWidth2;
                    int maxIterations2 = maxIterations;
                    int j3 = j2;
                    boolean optimize6 = optimize5;
                    int sizeDependentWidgetsCount3 = sizeDependentWidgetsCount2;
                    Measurer measurer7 = measurer4;
                    if (!needSolverPass3) {
                        startingWidth = startingWidth4;
                        break;
                    }
                    solveLinearSystem(constraintWidgetContainer2, "intermediate pass", startingWidth4, startingHeight);
                    needSolverPass3 = false;
                    j2 = j3 + 1;
                    optimize5 = optimize6;
                    maxIterations = maxIterations2;
                    sizeDependentWidgetsCount2 = sizeDependentWidgetsCount3;
                    measurer4 = measurer7;
                    containerWrapWidth2 = containerWrapWidth3;
                }
                if (needSolverPass3) {
                    solveLinearSystem(constraintWidgetContainer2, "2nd pass", startingWidth, startingHeight);
                    boolean needSolverPass6 = false;
                    if (layout.getWidth() < minWidth) {
                        constraintWidgetContainer2.setWidth(minWidth);
                        needSolverPass6 = true;
                    }
                    if (layout.getHeight() < minHeight) {
                        constraintWidgetContainer2.setHeight(minHeight);
                        needSolverPass6 = true;
                    }
                    if (needSolverPass6) {
                        solveLinearSystem(constraintWidgetContainer2, "3rd pass", startingWidth, startingHeight);
                    }
                }
            } else {
                optimizations = optimizations3;
                boolean z7 = optimize5;
                int i16 = childCount;
                int i17 = startingWidth3;
                boolean z8 = optimizeWrap;
                int i18 = widthSize2;
                int i19 = heightSize2;
                int i20 = computations;
                int i21 = sizeDependentWidgetsCount2;
            }
            constraintWidgetContainer2.setOptimizationLevel(optimizations);
        } else {
            boolean z9 = allSolved;
            Measurer measurer8 = measurer3;
            boolean z10 = optimize5;
            int i22 = childCount;
            int i23 = startingWidth3;
            boolean z11 = optimizeWrap;
            int i24 = widthSize2;
            int i25 = heightSize2;
            int i26 = computations;
        }
        return layoutTime;
    }

    private boolean measure(Measurer measurer, ConstraintWidget widget, int measureStrategy) {
        this.mMeasure.horizontalBehavior = widget.getHorizontalDimensionBehaviour();
        this.mMeasure.verticalBehavior = widget.getVerticalDimensionBehaviour();
        this.mMeasure.horizontalDimension = widget.getWidth();
        this.mMeasure.verticalDimension = widget.getHeight();
        this.mMeasure.measuredNeedsSolverPass = false;
        this.mMeasure.measureStrategy = measureStrategy;
        boolean horizontalMatchConstraints = this.mMeasure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean verticalMatchConstraints = this.mMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean horizontalUseRatio = horizontalMatchConstraints && widget.mDimensionRatio > 0.0f;
        boolean verticalUseRatio = verticalMatchConstraints && widget.mDimensionRatio > 0.0f;
        if (horizontalUseRatio && widget.mResolvedMatchConstraintDefault[0] == 4) {
            this.mMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (verticalUseRatio && widget.mResolvedMatchConstraintDefault[1] == 4) {
            this.mMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        measurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
        this.mMeasure.measureStrategy = Measure.SELF_DIMENSIONS;
        return this.mMeasure.measuredNeedsSolverPass;
    }
}
