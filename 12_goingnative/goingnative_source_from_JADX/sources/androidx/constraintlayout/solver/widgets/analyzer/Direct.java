package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ChainHead;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;

public class Direct {
    private static final boolean APPLY_MATCH_PARENT = false;
    private static final boolean DEBUG = false;
    private static BasicMeasure.Measure measure = new BasicMeasure.Measure();

    public static void solvingPass(ConstraintWidgetContainer layout, BasicMeasure.Measurer measurer) {
        int i;
        ConstraintWidgetContainer constraintWidgetContainer = layout;
        BasicMeasure.Measurer measurer2 = measurer;
        ConstraintWidget.DimensionBehaviour horizontal = layout.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour vertical = layout.getVerticalDimensionBehaviour();
        layout.resetFinalResolution();
        ArrayList<ConstraintWidget> children = layout.getChildren();
        int count = children.size();
        for (int i2 = 0; i2 < count; i2++) {
            children.get(i2).resetFinalResolution();
        }
        boolean isRtl = layout.isRtl();
        if (horizontal == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidgetContainer.setFinalHorizontal(0, layout.getWidth());
        } else {
            constraintWidgetContainer.setFinalLeft(0);
        }
        boolean hasGuideline = false;
        boolean hasBarrier = false;
        int i3 = 0;
        while (true) {
            i = -1;
            if (i3 >= count) {
                break;
            }
            ConstraintWidget child = children.get(i3);
            if (child instanceof Guideline) {
                Guideline guideline = (Guideline) child;
                if (guideline.getOrientation() == 1) {
                    if (guideline.getRelativeBegin() != -1) {
                        guideline.setFinalValue(guideline.getRelativeBegin());
                    } else if (guideline.getRelativeEnd() != -1 && layout.isResolvedHorizontally()) {
                        guideline.setFinalValue(layout.getWidth() - guideline.getRelativeEnd());
                    } else if (layout.isResolvedHorizontally()) {
                        guideline.setFinalValue((int) ((guideline.getRelativePercent() * ((float) layout.getWidth())) + 0.5f));
                    }
                    hasGuideline = true;
                }
            } else if ((child instanceof Barrier) && ((Barrier) child).getOrientation() == 0) {
                hasBarrier = true;
            }
            i3++;
        }
        if (hasGuideline) {
            for (int i4 = 0; i4 < count; i4++) {
                ConstraintWidget child2 = children.get(i4);
                if (child2 instanceof Guideline) {
                    Guideline guideline2 = (Guideline) child2;
                    if (guideline2.getOrientation() == 1) {
                        horizontalSolvingPass(guideline2, measurer2, isRtl);
                    }
                }
            }
        }
        horizontalSolvingPass(constraintWidgetContainer, measurer2, isRtl);
        if (hasBarrier) {
            for (int i5 = 0; i5 < count; i5++) {
                ConstraintWidget child3 = children.get(i5);
                if (child3 instanceof Barrier) {
                    Barrier barrier = (Barrier) child3;
                    if (barrier.getOrientation() == 0) {
                        solveBarrier(barrier, measurer2, 0, isRtl);
                    }
                }
            }
        }
        if (vertical == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidgetContainer.setFinalVertical(0, layout.getHeight());
        } else {
            constraintWidgetContainer.setFinalTop(0);
        }
        boolean hasGuideline2 = false;
        boolean hasBarrier2 = false;
        int i6 = 0;
        while (i6 < count) {
            ConstraintWidget child4 = children.get(i6);
            if (child4 instanceof Guideline) {
                Guideline guideline3 = (Guideline) child4;
                if (guideline3.getOrientation() == 0) {
                    if (guideline3.getRelativeBegin() != i) {
                        guideline3.setFinalValue(guideline3.getRelativeBegin());
                    } else if (guideline3.getRelativeEnd() != i && layout.isResolvedVertically()) {
                        guideline3.setFinalValue(layout.getHeight() - guideline3.getRelativeEnd());
                    } else if (layout.isResolvedVertically()) {
                        guideline3.setFinalValue((int) ((guideline3.getRelativePercent() * ((float) layout.getHeight())) + 0.5f));
                    }
                    hasGuideline2 = true;
                }
            } else if ((child4 instanceof Barrier) && ((Barrier) child4).getOrientation() == 1) {
                hasBarrier2 = true;
            }
            i6++;
            i = -1;
        }
        if (hasGuideline2) {
            for (int i7 = 0; i7 < count; i7++) {
                ConstraintWidget child5 = children.get(i7);
                if (child5 instanceof Guideline) {
                    Guideline guideline4 = (Guideline) child5;
                    if (guideline4.getOrientation() == 0) {
                        verticalSolvingPass(guideline4, measurer2);
                    }
                }
            }
        }
        verticalSolvingPass(layout, measurer);
        if (hasBarrier2) {
            for (int i8 = 0; i8 < count; i8++) {
                ConstraintWidget child6 = children.get(i8);
                if (child6 instanceof Barrier) {
                    Barrier barrier2 = (Barrier) child6;
                    if (barrier2.getOrientation() == 1) {
                        solveBarrier(barrier2, measurer2, 1, isRtl);
                    }
                }
            }
        }
        for (int i9 = 0; i9 < count; i9++) {
            ConstraintWidget child7 = children.get(i9);
            if (child7.isMeasureRequested() && canMeasure(child7)) {
                ConstraintWidgetContainer.measure(child7, measurer2, measure, BasicMeasure.Measure.SELF_DIMENSIONS);
                horizontalSolvingPass(child7, measurer2, isRtl);
                verticalSolvingPass(child7, measurer2);
            }
        }
    }

    private static void solveBarrier(Barrier barrier, BasicMeasure.Measurer measurer, int orientation, boolean isRtl) {
        if (!barrier.allSolved()) {
            return;
        }
        if (orientation == 0) {
            horizontalSolvingPass(barrier, measurer, isRtl);
        } else {
            verticalSolvingPass(barrier, measurer);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:119:0x01e5, code lost:
        if (r11.getDimensionRatio() == 0.0f) goto L_0x01ee;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void horizontalSolvingPass(androidx.constraintlayout.solver.widgets.ConstraintWidget r19, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measurer r20, boolean r21) {
        /*
            r0 = r19
            r1 = r20
            r2 = r21
            boolean r3 = r0 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r3 != 0) goto L_0x0020
            boolean r3 = r19.isMeasureRequested()
            if (r3 == 0) goto L_0x0020
            boolean r3 = canMeasure(r19)
            if (r3 == 0) goto L_0x0020
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r3 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r3.<init>()
            int r4 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r0, r1, r3, r4)
        L_0x0020:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r0.getAnchor(r3)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            int r5 = r3.getFinalValue()
            int r6 = r4.getFinalValue()
            java.util.HashSet r7 = r3.getDependents()
            r8 = 0
            r9 = 8
            if (r7 == 0) goto L_0x0148
            boolean r7 = r3.hasFinalValue()
            if (r7 == 0) goto L_0x0148
            java.util.HashSet r7 = r3.getDependents()
            java.util.Iterator r7 = r7.iterator()
        L_0x004b:
            boolean r12 = r7.hasNext()
            if (r12 == 0) goto L_0x0148
            java.lang.Object r12 = r7.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r12 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r12
            androidx.constraintlayout.solver.widgets.ConstraintWidget r13 = r12.mOwner
            r14 = 0
            r15 = 0
            boolean r16 = canMeasure(r13)
            boolean r17 = r13.isMeasureRequested()
            if (r17 == 0) goto L_0x0075
            if (r16 == 0) goto L_0x0075
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r17 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r17.<init>()
            r18 = r17
            int r10 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            r11 = r18
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r13, r1, r11, r10)
        L_0x0075:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = r13.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r10 != r11) goto L_0x00e5
            if (r16 == 0) goto L_0x0080
            goto L_0x00e5
        L_0x0080:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = r13.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r10 != r11) goto L_0x0146
            int r10 = r13.mMatchConstraintMaxWidth
            if (r10 < 0) goto L_0x0146
            int r10 = r13.mMatchConstraintMinWidth
            if (r10 < 0) goto L_0x0146
            int r10 = r13.getVisibility()
            if (r10 == r9) goto L_0x00a2
            int r10 = r13.mMatchConstraintDefaultWidth
            if (r10 != 0) goto L_0x0146
            float r10 = r13.getDimensionRatio()
            int r10 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r10 != 0) goto L_0x0146
        L_0x00a2:
            boolean r10 = r13.isInHorizontalChain()
            if (r10 != 0) goto L_0x0146
            boolean r10 = r13.isInVirtualLayout()
            if (r10 != 0) goto L_0x0146
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            if (r12 != r10) goto L_0x00c2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 == 0) goto L_0x00c2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            boolean r10 = r10.hasFinalValue()
            if (r10 != 0) goto L_0x00d6
        L_0x00c2:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            if (r12 != r10) goto L_0x00d8
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 == 0) goto L_0x00d8
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            boolean r10 = r10.hasFinalValue()
            if (r10 == 0) goto L_0x00d8
        L_0x00d6:
            r10 = 1
            goto L_0x00d9
        L_0x00d8:
            r10 = 0
        L_0x00d9:
            if (r10 == 0) goto L_0x0146
            boolean r11 = r13.isInHorizontalChain()
            if (r11 != 0) goto L_0x0146
            solveHorizontalMatchConstraint(r0, r1, r13, r2)
            goto L_0x0146
        L_0x00e5:
            boolean r10 = r13.isMeasureRequested()
            if (r10 == 0) goto L_0x00ed
            goto L_0x004b
        L_0x00ed:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            if (r12 != r10) goto L_0x010a
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 != 0) goto L_0x010a
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            int r10 = r10.getMargin()
            int r10 = r10 + r5
            int r11 = r13.getWidth()
            int r11 = r11 + r10
            r13.setFinalHorizontal(r10, r11)
            horizontalSolvingPass(r13, r1, r2)
            goto L_0x0146
        L_0x010a:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            if (r12 != r10) goto L_0x0129
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 != 0) goto L_0x0129
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            int r10 = r10.getMargin()
            int r10 = r5 - r10
            int r11 = r13.getWidth()
            int r11 = r10 - r11
            r13.setFinalHorizontal(r11, r10)
            horizontalSolvingPass(r13, r1, r2)
            goto L_0x0146
        L_0x0129:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            if (r12 != r10) goto L_0x0146
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 == 0) goto L_0x0146
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            boolean r10 = r10.hasFinalValue()
            if (r10 == 0) goto L_0x0146
            boolean r10 = r13.isInHorizontalChain()
            if (r10 != 0) goto L_0x0146
            solveHorizontalCenterConstraints(r1, r13, r2)
        L_0x0146:
            goto L_0x004b
        L_0x0148:
            boolean r7 = r0 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r7 == 0) goto L_0x014d
            return
        L_0x014d:
            java.util.HashSet r7 = r4.getDependents()
            if (r7 == 0) goto L_0x0264
            boolean r7 = r4.hasFinalValue()
            if (r7 == 0) goto L_0x0264
            java.util.HashSet r7 = r4.getDependents()
            java.util.Iterator r7 = r7.iterator()
        L_0x0161:
            boolean r10 = r7.hasNext()
            if (r10 == 0) goto L_0x0264
            java.lang.Object r10 = r7.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r10
            androidx.constraintlayout.solver.widgets.ConstraintWidget r11 = r10.mOwner
            boolean r12 = canMeasure(r11)
            boolean r13 = r11.isMeasureRequested()
            if (r13 == 0) goto L_0x0185
            if (r12 == 0) goto L_0x0185
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r13 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r13.<init>()
            int r14 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r11, r1, r13, r14)
        L_0x0185:
            r13 = 0
            r14 = 0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r11.mLeft
            if (r10 != r15) goto L_0x019b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r11.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r15.mTarget
            if (r15 == 0) goto L_0x019b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r11.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r15.mTarget
            boolean r15 = r15.hasFinalValue()
            if (r15 != 0) goto L_0x01af
        L_0x019b:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r11.mRight
            if (r10 != r15) goto L_0x01b1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r11.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r15.mTarget
            if (r15 == 0) goto L_0x01b1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r11.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r15.mTarget
            boolean r15 = r15.hasFinalValue()
            if (r15 == 0) goto L_0x01b1
        L_0x01af:
            r15 = 1
            goto L_0x01b2
        L_0x01b1:
            r15 = 0
        L_0x01b2:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r8 = r11.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r8 != r9) goto L_0x020b
            if (r12 == 0) goto L_0x01c1
            r9 = 8
            r16 = 0
            goto L_0x020f
        L_0x01c1:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r8 = r11.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r8 != r9) goto L_0x0206
            int r8 = r11.mMatchConstraintMaxWidth
            if (r8 < 0) goto L_0x0206
            int r8 = r11.mMatchConstraintMinWidth
            if (r8 < 0) goto L_0x0206
            int r8 = r11.getVisibility()
            r9 = 8
            if (r8 == r9) goto L_0x01ec
            int r8 = r11.mMatchConstraintDefaultWidth
            if (r8 != 0) goto L_0x01e8
            float r8 = r11.getDimensionRatio()
            r16 = 0
            int r8 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1))
            if (r8 != 0) goto L_0x0260
            goto L_0x01ee
        L_0x01e8:
            r16 = 0
            goto L_0x0260
        L_0x01ec:
            r16 = 0
        L_0x01ee:
            boolean r8 = r11.isInHorizontalChain()
            if (r8 != 0) goto L_0x0260
            boolean r8 = r11.isInVirtualLayout()
            if (r8 != 0) goto L_0x0260
            if (r15 == 0) goto L_0x0260
            boolean r8 = r11.isInHorizontalChain()
            if (r8 != 0) goto L_0x0260
            solveHorizontalMatchConstraint(r0, r1, r11, r2)
            goto L_0x0260
        L_0x0206:
            r9 = 8
            r16 = 0
            goto L_0x0260
        L_0x020b:
            r9 = 8
            r16 = 0
        L_0x020f:
            boolean r8 = r11.isMeasureRequested()
            if (r8 == 0) goto L_0x0219
            r8 = r16
            goto L_0x0161
        L_0x0219:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r11.mLeft
            if (r10 != r8) goto L_0x0236
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r11.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r8.mTarget
            if (r8 != 0) goto L_0x0236
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r11.mLeft
            int r8 = r8.getMargin()
            int r8 = r8 + r6
            int r13 = r11.getWidth()
            int r13 = r13 + r8
            r11.setFinalHorizontal(r8, r13)
            horizontalSolvingPass(r11, r1, r2)
            goto L_0x0260
        L_0x0236:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r11.mRight
            if (r10 != r8) goto L_0x0255
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r11.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r8.mTarget
            if (r8 != 0) goto L_0x0255
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r11.mRight
            int r8 = r8.getMargin()
            int r8 = r6 - r8
            int r14 = r11.getWidth()
            int r13 = r8 - r14
            r11.setFinalHorizontal(r13, r8)
            horizontalSolvingPass(r11, r1, r2)
            goto L_0x0260
        L_0x0255:
            if (r15 == 0) goto L_0x0260
            boolean r8 = r11.isInHorizontalChain()
            if (r8 != 0) goto L_0x0260
            solveHorizontalCenterConstraints(r1, r11, r2)
        L_0x0260:
            r8 = r16
            goto L_0x0161
        L_0x0264:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.Direct.horizontalSolvingPass(androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer, boolean):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:117:0x01d7, code lost:
        if (r10.getDimensionRatio() == 0.0f) goto L_0x01de;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void verticalSolvingPass(androidx.constraintlayout.solver.widgets.ConstraintWidget r18, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measurer r19) {
        /*
            r0 = r18
            r1 = r19
            boolean r2 = r0 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r2 != 0) goto L_0x001e
            boolean r2 = r18.isMeasureRequested()
            if (r2 == 0) goto L_0x001e
            boolean r2 = canMeasure(r18)
            if (r2 == 0) goto L_0x001e
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r2 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r2.<init>()
            int r3 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r0, r1, r2, r3)
        L_0x001e:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r0.getAnchor(r2)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r0.getAnchor(r3)
            int r4 = r2.getFinalValue()
            int r5 = r3.getFinalValue()
            java.util.HashSet r6 = r2.getDependents()
            r7 = 0
            r8 = 8
            if (r6 == 0) goto L_0x0140
            boolean r6 = r2.hasFinalValue()
            if (r6 == 0) goto L_0x0140
            java.util.HashSet r6 = r2.getDependents()
            java.util.Iterator r6 = r6.iterator()
        L_0x0049:
            boolean r11 = r6.hasNext()
            if (r11 == 0) goto L_0x0140
            java.lang.Object r11 = r6.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r11 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r11
            androidx.constraintlayout.solver.widgets.ConstraintWidget r12 = r11.mOwner
            r13 = 0
            r14 = 0
            boolean r15 = canMeasure(r12)
            boolean r16 = r12.isMeasureRequested()
            if (r16 == 0) goto L_0x0073
            if (r15 == 0) goto L_0x0073
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r16 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r16.<init>()
            r17 = r16
            int r9 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            r10 = r17
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r12, r1, r10, r9)
        L_0x0073:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = r12.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r9 != r10) goto L_0x00e3
            if (r15 == 0) goto L_0x007e
            goto L_0x00e3
        L_0x007e:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = r12.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r9 != r10) goto L_0x013e
            int r9 = r12.mMatchConstraintMaxHeight
            if (r9 < 0) goto L_0x013e
            int r9 = r12.mMatchConstraintMinHeight
            if (r9 < 0) goto L_0x013e
            int r9 = r12.getVisibility()
            if (r9 == r8) goto L_0x00a0
            int r9 = r12.mMatchConstraintDefaultHeight
            if (r9 != 0) goto L_0x013e
            float r9 = r12.getDimensionRatio()
            int r9 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x013e
        L_0x00a0:
            boolean r9 = r12.isInVerticalChain()
            if (r9 != 0) goto L_0x013e
            boolean r9 = r12.isInVirtualLayout()
            if (r9 != 0) goto L_0x013e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mTop
            if (r11 != r9) goto L_0x00c0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 == 0) goto L_0x00c0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            boolean r9 = r9.hasFinalValue()
            if (r9 != 0) goto L_0x00d4
        L_0x00c0:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            if (r11 != r9) goto L_0x00d6
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 == 0) goto L_0x00d6
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            boolean r9 = r9.hasFinalValue()
            if (r9 == 0) goto L_0x00d6
        L_0x00d4:
            r9 = 1
            goto L_0x00d7
        L_0x00d6:
            r9 = 0
        L_0x00d7:
            if (r9 == 0) goto L_0x013e
            boolean r10 = r12.isInVerticalChain()
            if (r10 != 0) goto L_0x013e
            solveVerticalMatchConstraint(r0, r1, r12)
            goto L_0x013e
        L_0x00e3:
            boolean r9 = r12.isMeasureRequested()
            if (r9 == 0) goto L_0x00eb
            goto L_0x0049
        L_0x00eb:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mTop
            if (r11 != r9) goto L_0x0108
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x0108
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mTop
            int r9 = r9.getMargin()
            int r9 = r9 + r4
            int r10 = r12.getHeight()
            int r10 = r10 + r9
            r12.setFinalVertical(r9, r10)
            verticalSolvingPass(r12, r1)
            goto L_0x013e
        L_0x0108:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            if (r11 != r9) goto L_0x0127
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x0127
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            int r9 = r9.getMargin()
            int r9 = r4 - r9
            int r10 = r12.getHeight()
            int r10 = r9 - r10
            r12.setFinalVertical(r10, r9)
            verticalSolvingPass(r12, r1)
            goto L_0x013e
        L_0x0127:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mTop
            if (r11 != r9) goto L_0x013e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 == 0) goto L_0x013e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r12.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            boolean r9 = r9.hasFinalValue()
            if (r9 == 0) goto L_0x013e
            solveVerticalCenterConstraints(r1, r12)
        L_0x013e:
            goto L_0x0049
        L_0x0140:
            boolean r6 = r0 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r6 == 0) goto L_0x0145
            return
        L_0x0145:
            java.util.HashSet r6 = r3.getDependents()
            if (r6 == 0) goto L_0x024c
            boolean r6 = r3.hasFinalValue()
            if (r6 == 0) goto L_0x024c
            java.util.HashSet r6 = r3.getDependents()
            java.util.Iterator r6 = r6.iterator()
        L_0x0159:
            boolean r9 = r6.hasNext()
            if (r9 == 0) goto L_0x024c
            java.lang.Object r9 = r6.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r9
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r9.mOwner
            boolean r11 = canMeasure(r10)
            boolean r12 = r10.isMeasureRequested()
            if (r12 == 0) goto L_0x017d
            if (r11 == 0) goto L_0x017d
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r12 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r12.<init>()
            int r13 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r10, r1, r12, r13)
        L_0x017d:
            r12 = 0
            r13 = 0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r10.mTop
            if (r9 != r14) goto L_0x0193
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r10.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r14.mTarget
            if (r14 == 0) goto L_0x0193
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r10.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r14.mTarget
            boolean r14 = r14.hasFinalValue()
            if (r14 != 0) goto L_0x01a7
        L_0x0193:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r10.mBottom
            if (r9 != r14) goto L_0x01a9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r10.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r14.mTarget
            if (r14 == 0) goto L_0x01a9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r10.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r14.mTarget
            boolean r14 = r14.hasFinalValue()
            if (r14 == 0) goto L_0x01a9
        L_0x01a7:
            r14 = 1
            goto L_0x01aa
        L_0x01a9:
            r14 = 0
        L_0x01aa:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = r10.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r15 != r7) goto L_0x01f8
            if (r11 == 0) goto L_0x01b6
            r15 = 0
            goto L_0x01f9
        L_0x01b6:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = r10.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r7 != r15) goto L_0x01f6
            int r7 = r10.mMatchConstraintMaxHeight
            if (r7 < 0) goto L_0x01f6
            int r7 = r10.mMatchConstraintMinHeight
            if (r7 < 0) goto L_0x01f6
            int r7 = r10.getVisibility()
            if (r7 == r8) goto L_0x01dd
            int r7 = r10.mMatchConstraintDefaultHeight
            if (r7 != 0) goto L_0x01da
            float r7 = r10.getDimensionRatio()
            r15 = 0
            int r7 = (r7 > r15 ? 1 : (r7 == r15 ? 0 : -1))
            if (r7 != 0) goto L_0x0249
            goto L_0x01de
        L_0x01da:
            r15 = 0
            goto L_0x0249
        L_0x01dd:
            r15 = 0
        L_0x01de:
            boolean r7 = r10.isInVerticalChain()
            if (r7 != 0) goto L_0x0249
            boolean r7 = r10.isInVirtualLayout()
            if (r7 != 0) goto L_0x0249
            if (r14 == 0) goto L_0x0249
            boolean r7 = r10.isInVerticalChain()
            if (r7 != 0) goto L_0x0249
            solveVerticalMatchConstraint(r0, r1, r10)
            goto L_0x0249
        L_0x01f6:
            r15 = 0
            goto L_0x0249
        L_0x01f8:
            r15 = 0
        L_0x01f9:
            boolean r7 = r10.isMeasureRequested()
            if (r7 == 0) goto L_0x0202
            r7 = r15
            goto L_0x0159
        L_0x0202:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r10.mTop
            if (r9 != r7) goto L_0x021f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r10.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r7.mTarget
            if (r7 != 0) goto L_0x021f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r10.mTop
            int r7 = r7.getMargin()
            int r7 = r7 + r5
            int r12 = r10.getHeight()
            int r12 = r12 + r7
            r10.setFinalVertical(r7, r12)
            verticalSolvingPass(r10, r1)
            goto L_0x0249
        L_0x021f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r10.mBottom
            if (r9 != r7) goto L_0x023e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r10.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r7.mTarget
            if (r7 != 0) goto L_0x023e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r10.mBottom
            int r7 = r7.getMargin()
            int r7 = r5 - r7
            int r13 = r10.getHeight()
            int r12 = r7 - r13
            r10.setFinalVertical(r12, r7)
            verticalSolvingPass(r10, r1)
            goto L_0x0249
        L_0x023e:
            if (r14 == 0) goto L_0x0249
            boolean r7 = r10.isInVerticalChain()
            if (r7 != 0) goto L_0x0249
            solveVerticalCenterConstraints(r1, r10)
        L_0x0249:
            r7 = r15
            goto L_0x0159
        L_0x024c:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BASELINE
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r0.getAnchor(r6)
            java.util.HashSet r7 = r6.getDependents()
            if (r7 == 0) goto L_0x02aa
            boolean r7 = r6.hasFinalValue()
            if (r7 == 0) goto L_0x02aa
            int r7 = r6.getFinalValue()
            java.util.HashSet r8 = r6.getDependents()
            java.util.Iterator r8 = r8.iterator()
        L_0x026a:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x02aa
            java.lang.Object r9 = r8.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r9
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r9.mOwner
            boolean r11 = canMeasure(r10)
            boolean r12 = r10.isMeasureRequested()
            if (r12 == 0) goto L_0x028e
            if (r11 == 0) goto L_0x028e
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r12 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r12.<init>()
            int r13 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r10, r1, r12, r13)
        L_0x028e:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = r10.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r13 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r12 != r13) goto L_0x0298
            if (r11 == 0) goto L_0x02a9
        L_0x0298:
            boolean r12 = r10.isMeasureRequested()
            if (r12 == 0) goto L_0x029f
            goto L_0x026a
        L_0x029f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r12 = r10.mBaseline
            if (r9 != r12) goto L_0x02a9
            r10.setFinalBaseline(r7)
            verticalSolvingPass(r10, r1)
        L_0x02a9:
            goto L_0x026a
        L_0x02aa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.Direct.verticalSolvingPass(androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer):void");
    }

    private static void solveHorizontalCenterConstraints(BasicMeasure.Measurer measurer, ConstraintWidget widget, boolean isRtl) {
        float bias = widget.getHorizontalBiasPercent();
        int start = widget.mLeft.mTarget.getFinalValue();
        int end = widget.mRight.mTarget.getFinalValue();
        int s1 = widget.mLeft.getMargin() + start;
        int s2 = end - widget.mRight.getMargin();
        if (start == end) {
            bias = 0.5f;
            s1 = start;
            s2 = end;
        }
        int width = widget.getWidth();
        int distance = (s2 - s1) - width;
        if (s1 > s2) {
            distance = (s1 - s2) - width;
        }
        int d1 = (int) ((((float) distance) * bias) + 0.5f);
        int x1 = s1 + d1;
        int x2 = x1 + width;
        if (s1 > s2) {
            x1 = s1 + d1;
            x2 = x1 - width;
        }
        widget.setFinalHorizontal(x1, x2);
        horizontalSolvingPass(widget, measurer, isRtl);
    }

    private static void solveVerticalCenterConstraints(BasicMeasure.Measurer measurer, ConstraintWidget widget) {
        float bias = widget.getVerticalBiasPercent();
        int start = widget.mTop.mTarget.getFinalValue();
        int end = widget.mBottom.mTarget.getFinalValue();
        int s1 = widget.mTop.getMargin() + start;
        int s2 = end - widget.mBottom.getMargin();
        if (start == end) {
            bias = 0.5f;
            s1 = start;
            s2 = end;
        }
        int height = widget.getHeight();
        int distance = (s2 - s1) - height;
        if (s1 > s2) {
            distance = (s1 - s2) - height;
        }
        int d1 = (int) ((((float) distance) * bias) + 0.5f);
        int y1 = s1 + d1;
        int y2 = y1 + height;
        if (s1 > s2) {
            y1 = s1 - d1;
            y2 = y1 - height;
        }
        widget.setFinalVertical(y1, y2);
        verticalSolvingPass(widget, measurer);
    }

    private static void solveHorizontalMatchConstraint(ConstraintWidget layout, BasicMeasure.Measurer measurer, ConstraintWidget widget, boolean isRtl) {
        int parentWidth;
        float bias = widget.getHorizontalBiasPercent();
        int s1 = widget.mLeft.mTarget.getFinalValue() + widget.mLeft.getMargin();
        int s2 = widget.mRight.mTarget.getFinalValue() - widget.mRight.getMargin();
        if (s2 >= s1) {
            int width = widget.getWidth();
            if (widget.getVisibility() != 8) {
                if (widget.mMatchConstraintDefaultWidth == 2) {
                    if (layout instanceof ConstraintWidgetContainer) {
                        parentWidth = layout.getWidth();
                    } else {
                        parentWidth = layout.getParent().getWidth();
                    }
                    width = (int) (widget.getHorizontalBiasPercent() * 0.5f * ((float) parentWidth));
                } else if (widget.mMatchConstraintDefaultWidth == 0) {
                    width = s2 - s1;
                }
                width = Math.max(widget.mMatchConstraintMinWidth, width);
                if (widget.mMatchConstraintMaxWidth > 0) {
                    width = Math.min(widget.mMatchConstraintMaxWidth, width);
                }
            }
            int x1 = s1 + ((int) ((((float) ((s2 - s1) - width)) * bias) + 0.5f));
            widget.setFinalHorizontal(x1, x1 + width);
            horizontalSolvingPass(widget, measurer, isRtl);
        }
    }

    private static void solveVerticalMatchConstraint(ConstraintWidget layout, BasicMeasure.Measurer measurer, ConstraintWidget widget) {
        int parentHeight;
        float bias = widget.getVerticalBiasPercent();
        int s1 = widget.mTop.mTarget.getFinalValue() + widget.mTop.getMargin();
        int s2 = widget.mBottom.mTarget.getFinalValue() - widget.mBottom.getMargin();
        if (s2 >= s1) {
            int height = widget.getHeight();
            if (widget.getVisibility() != 8) {
                if (widget.mMatchConstraintDefaultHeight == 2) {
                    if (layout instanceof ConstraintWidgetContainer) {
                        parentHeight = layout.getHeight();
                    } else {
                        parentHeight = layout.getParent().getHeight();
                    }
                    height = (int) (bias * 0.5f * ((float) parentHeight));
                } else if (widget.mMatchConstraintDefaultHeight == 0) {
                    height = s2 - s1;
                }
                height = Math.max(widget.mMatchConstraintMinHeight, height);
                if (widget.mMatchConstraintMaxHeight > 0) {
                    height = Math.min(widget.mMatchConstraintMaxHeight, height);
                }
            }
            int y1 = s1 + ((int) ((((float) ((s2 - s1) - height)) * bias) + 0.5f));
            widget.setFinalVertical(y1, y1 + height);
            verticalSolvingPass(widget, measurer);
        }
    }

    private static boolean canMeasure(ConstraintWidget layout) {
        ConstraintWidget.DimensionBehaviour horizontalBehaviour = layout.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour verticalBehaviour = layout.getVerticalDimensionBehaviour();
        ConstraintWidgetContainer parent = layout.getParent() != null ? (ConstraintWidgetContainer) layout.getParent() : null;
        if (parent == null || parent.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
        }
        if (parent == null || parent.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
        }
        boolean isHorizontalFixed = horizontalBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && layout.mMatchConstraintDefaultWidth == 0 && layout.mDimensionRatio == 0.0f && layout.hasDanglingDimension(0)) || layout.isResolvedHorizontally();
        boolean isVerticalFixed = verticalBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (verticalBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && layout.mMatchConstraintDefaultHeight == 0 && layout.mDimensionRatio == 0.0f && layout.hasDanglingDimension(1)) || layout.isResolvedVertically();
        if (layout.mDimensionRatio > 0.0f && (isHorizontalFixed || isVerticalFixed)) {
            return true;
        }
        if (!isHorizontalFixed || !isVerticalFixed) {
            return false;
        }
        return true;
    }

    public static boolean solveChain(ConstraintWidgetContainer container, LinearSystem system, int orientation, int offset, ChainHead chainHead, boolean isChainSpread, boolean isChainSpreadInside, boolean isChainPacked) {
        ConstraintWidget widget;
        int i;
        int gap;
        int gap2;
        int current;
        float bias;
        BasicMeasure.Measure measure2;
        int totalSize;
        ConstraintWidget next;
        if (isChainPacked) {
            return false;
        }
        if (orientation == 0) {
            if (!container.isResolvedHorizontally()) {
                return false;
            }
        } else if (!container.isResolvedVertically()) {
            return false;
        }
        boolean isRtl = container.isRtl();
        ConstraintWidget first = chainHead.getFirst();
        ConstraintWidget last = chainHead.getLast();
        ConstraintWidget firstVisibleWidget = chainHead.getFirstVisibleWidget();
        ConstraintWidget lastVisibleWidget = chainHead.getLastVisibleWidget();
        ConstraintWidget head = chainHead.getHead();
        ConstraintWidget widget2 = first;
        ConstraintWidget next2 = null;
        boolean done = false;
        ConstraintAnchor begin = first.mListAnchors[offset];
        ConstraintAnchor end = last.mListAnchors[offset + 1];
        if (begin.mTarget == null) {
            ConstraintWidget constraintWidget = last;
            ConstraintWidget constraintWidget2 = head;
            LinearSystem linearSystem = system;
        } else if (end.mTarget == null) {
            ConstraintWidget constraintWidget3 = first;
            ConstraintWidget constraintWidget4 = last;
            ConstraintWidget constraintWidget5 = head;
            LinearSystem linearSystem2 = system;
        } else {
            if (!begin.mTarget.hasFinalValue()) {
                ConstraintWidget constraintWidget6 = last;
                ConstraintWidget constraintWidget7 = head;
                LinearSystem linearSystem3 = system;
            } else if (!end.mTarget.hasFinalValue()) {
                ConstraintWidget constraintWidget8 = first;
                ConstraintWidget constraintWidget9 = last;
                ConstraintWidget constraintWidget10 = head;
                LinearSystem linearSystem4 = system;
            } else {
                if (firstVisibleWidget == null) {
                    ConstraintWidget constraintWidget11 = last;
                    ConstraintWidget constraintWidget12 = head;
                    LinearSystem linearSystem5 = system;
                } else if (lastVisibleWidget == null) {
                    ConstraintWidget constraintWidget13 = first;
                    ConstraintWidget constraintWidget14 = last;
                    ConstraintWidget constraintWidget15 = head;
                    LinearSystem linearSystem6 = system;
                } else {
                    int startPoint = begin.mTarget.getFinalValue() + firstVisibleWidget.mListAnchors[offset].getMargin();
                    int endPoint = end.mTarget.getFinalValue() - lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                    int distance = endPoint - startPoint;
                    if (distance <= 0) {
                        return false;
                    }
                    int totalSize2 = 0;
                    BasicMeasure.Measure measure3 = new BasicMeasure.Measure();
                    int numWidgets = 0;
                    int numVisibleWidgets = 0;
                    while (!done) {
                        ConstraintAnchor begin2 = widget2.mListAnchors[offset];
                        boolean canMeasure = canMeasure(widget2);
                        if (!canMeasure) {
                            return false;
                        }
                        boolean z = canMeasure;
                        ConstraintWidget last2 = last;
                        if (widget2.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            return false;
                        }
                        if (widget2.isMeasureRequested()) {
                            ConstraintWidget constraintWidget16 = next2;
                            measure2 = measure3;
                            ConstraintWidgetContainer.measure(widget2, container.getMeasurer(), measure2, BasicMeasure.Measure.SELF_DIMENSIONS);
                        } else {
                            measure2 = measure3;
                        }
                        int totalSize3 = totalSize2 + widget2.mListAnchors[offset].getMargin();
                        if (orientation == 0) {
                            totalSize = totalSize3 + widget2.getWidth();
                        } else {
                            totalSize = totalSize3 + widget2.getHeight();
                        }
                        totalSize2 = totalSize + widget2.mListAnchors[offset + 1].getMargin();
                        numWidgets++;
                        if (widget2.getVisibility() != 8) {
                            numVisibleWidgets++;
                        } else {
                            int i2 = numVisibleWidgets;
                        }
                        ConstraintAnchor nextAnchor = widget2.mListAnchors[offset + 1].mTarget;
                        if (nextAnchor != null) {
                            ConstraintWidget next3 = nextAnchor.mOwner;
                            ConstraintAnchor constraintAnchor = nextAnchor;
                            if (next3.mListAnchors[offset].mTarget == null || next3.mListAnchors[offset].mTarget.mOwner != widget2) {
                                next = null;
                            } else {
                                next = next3;
                            }
                        } else {
                            next = null;
                        }
                        if (next != null) {
                            widget2 = next;
                        } else {
                            done = true;
                        }
                        measure3 = measure2;
                        last = last2;
                        next2 = next;
                    }
                    ConstraintWidget next4 = next2;
                    BasicMeasure.Measure measure4 = measure3;
                    int numWidgets2 = numWidgets;
                    int numVisibleWidgets2 = numVisibleWidgets;
                    if (numVisibleWidgets2 == 0 || numVisibleWidgets2 != numWidgets2 || distance < totalSize2) {
                        return false;
                    }
                    int gap3 = distance - totalSize2;
                    int i3 = numWidgets2;
                    if (isChainSpread) {
                        widget = widget2;
                        gap = gap3 / (numVisibleWidgets2 + 1);
                        i = 1;
                    } else {
                        if (!isChainSpreadInside) {
                            widget = widget2;
                            i = 1;
                        } else if (numVisibleWidgets2 > 2) {
                            widget = widget2;
                            i = 1;
                            gap = (gap3 / numVisibleWidgets2) - 1;
                        } else {
                            widget = widget2;
                            i = 1;
                        }
                        gap = gap3;
                    }
                    if (numVisibleWidgets2 == i) {
                        if (orientation == 0) {
                            bias = head.getHorizontalBiasPercent();
                        } else {
                            bias = head.getVerticalBiasPercent();
                        }
                        ConstraintWidget constraintWidget17 = head;
                        BasicMeasure.Measure measure5 = measure4;
                        int p1 = (int) (((float) startPoint) + 0.5f + (((float) gap) * bias));
                        if (orientation == 0) {
                            firstVisibleWidget.setFinalHorizontal(p1, firstVisibleWidget.getWidth() + p1);
                        } else {
                            firstVisibleWidget.setFinalVertical(p1, firstVisibleWidget.getHeight() + p1);
                        }
                        horizontalSolvingPass(firstVisibleWidget, container.getMeasurer(), isRtl);
                        return true;
                    }
                    BasicMeasure.Measure measure6 = measure4;
                    if (isChainSpread) {
                        int current2 = startPoint + gap;
                        ConstraintWidget widget3 = first;
                        boolean done2 = false;
                        while (!done2) {
                            ConstraintAnchor begin3 = widget3.mListAnchors[offset];
                            ConstraintWidget first2 = first;
                            if (widget3.getVisibility() != 8) {
                                int current3 = current2 + widget3.mListAnchors[offset].getMargin();
                                if (orientation == 0) {
                                    widget3.setFinalHorizontal(current3, widget3.getWidth() + current3);
                                    horizontalSolvingPass(widget3, container.getMeasurer(), isRtl);
                                    current = current3 + widget3.getWidth();
                                } else {
                                    widget3.setFinalVertical(current3, widget3.getHeight() + current3);
                                    verticalSolvingPass(widget3, container.getMeasurer());
                                    current = current3 + widget3.getHeight();
                                }
                                current2 = current + widget3.mListAnchors[offset + 1].getMargin() + gap;
                            } else if (orientation == 0) {
                                widget3.setFinalHorizontal(current2, current2);
                                horizontalSolvingPass(widget3, container.getMeasurer(), isRtl);
                            } else {
                                widget3.setFinalVertical(current2, current2);
                                verticalSolvingPass(widget3, container.getMeasurer());
                            }
                            widget3.addToSolver(system, false);
                            ConstraintAnchor nextAnchor2 = widget3.mListAnchors[offset + 1].mTarget;
                            if (nextAnchor2 != null) {
                                gap2 = gap;
                                ConstraintWidget next5 = nextAnchor2.mOwner;
                                ConstraintAnchor constraintAnchor2 = nextAnchor2;
                                if (next5.mListAnchors[offset].mTarget == null || next5.mListAnchors[offset].mTarget.mOwner != widget3) {
                                    next4 = null;
                                } else {
                                    next4 = next5;
                                }
                            } else {
                                gap2 = gap;
                                ConstraintAnchor constraintAnchor3 = nextAnchor2;
                                next4 = null;
                            }
                            if (next4 != null) {
                                widget3 = next4;
                            } else {
                                done2 = true;
                            }
                            first = first2;
                            gap = gap2;
                        }
                        LinearSystem linearSystem7 = system;
                        int i4 = gap;
                        ConstraintWidget constraintWidget18 = first;
                        ConstraintWidget constraintWidget19 = widget3;
                        ConstraintWidget widget4 = next4;
                        return true;
                    }
                    LinearSystem linearSystem8 = system;
                    int i5 = gap;
                    ConstraintWidget constraintWidget20 = first;
                    if (!isChainSpreadInside) {
                        ConstraintWidget constraintWidget21 = next4;
                        ConstraintWidget constraintWidget22 = widget;
                        return true;
                    } else if (numVisibleWidgets2 != 2) {
                        return false;
                    } else {
                        if (orientation == 0) {
                            firstVisibleWidget.setFinalHorizontal(startPoint, firstVisibleWidget.getWidth() + startPoint);
                            lastVisibleWidget.setFinalHorizontal(endPoint - lastVisibleWidget.getWidth(), endPoint);
                            horizontalSolvingPass(firstVisibleWidget, container.getMeasurer(), isRtl);
                            horizontalSolvingPass(lastVisibleWidget, container.getMeasurer(), isRtl);
                            return true;
                        }
                        firstVisibleWidget.setFinalVertical(startPoint, firstVisibleWidget.getHeight() + startPoint);
                        lastVisibleWidget.setFinalVertical(endPoint - lastVisibleWidget.getHeight(), endPoint);
                        verticalSolvingPass(firstVisibleWidget, container.getMeasurer());
                        verticalSolvingPass(lastVisibleWidget, container.getMeasurer());
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
