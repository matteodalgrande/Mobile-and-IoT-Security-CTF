package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;

public class HorizontalWidgetRun extends WidgetRun {
    private static int[] tempDimensions = new int[2];

    public HorizontalWidgetRun(ConstraintWidget widget) {
        super(widget);
        this.start.type = DependencyNode.Type.LEFT;
        this.end.type = DependencyNode.Type.RIGHT;
        this.orientation = 0;
    }

    public String toString() {
        return "HorizontalRun " + this.widget.getDebugName();
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.dimension.clear();
        this.resolved = false;
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        this.resolved = false;
        this.start.clear();
        this.start.resolved = false;
        this.end.clear();
        this.end.resolved = false;
        this.dimension.resolved = false;
    }

    /* access modifiers changed from: package-private */
    public boolean supportsWrapComputation() {
        if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.mMatchConstraintDefaultWidth == 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void apply() {
        ConstraintWidget parent;
        ConstraintWidget parent2;
        if (this.widget.measured) {
            this.dimension.resolve(this.widget.getWidth());
        }
        if (!this.dimension.resolved) {
            this.dimensionBehavior = this.widget.getHorizontalDimensionBehaviour();
            if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && (((parent2 = this.widget.getParent()) != null && parent2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) || parent2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT)) {
                    int resolvedDimension = (parent2.getWidth() - this.widget.mLeft.getMargin()) - this.widget.mRight.getMargin();
                    addTarget(this.start, parent2.horizontalRun.start, this.widget.mLeft.getMargin());
                    addTarget(this.end, parent2.horizontalRun.end, -this.widget.mRight.getMargin());
                    this.dimension.resolve(resolvedDimension);
                    return;
                } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.dimension.resolve(this.widget.getWidth());
                }
            }
        } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && (((parent = this.widget.getParent()) != null && parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) || parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT)) {
            addTarget(this.start, parent.horizontalRun.start, this.widget.mLeft.getMargin());
            addTarget(this.end, parent.horizontalRun.end, -this.widget.mRight.getMargin());
            return;
        }
        if (!this.dimension.resolved || !this.widget.measured) {
            if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                int i = this.widget.mMatchConstraintDefaultWidth;
                if (i == 2) {
                    ConstraintWidget parent3 = this.widget.getParent();
                    if (parent3 != null) {
                        DependencyNode targetDimension = parent3.verticalRun.dimension;
                        this.dimension.targets.add(targetDimension);
                        targetDimension.dependencies.add(this.dimension);
                        this.dimension.delegateToWidgetRun = true;
                        this.dimension.dependencies.add(this.start);
                        this.dimension.dependencies.add(this.end);
                    }
                } else if (i == 3) {
                    if (this.widget.mMatchConstraintDefaultHeight == 3) {
                        this.start.updateDelegate = this;
                        this.end.updateDelegate = this;
                        this.widget.verticalRun.start.updateDelegate = this;
                        this.widget.verticalRun.end.updateDelegate = this;
                        this.dimension.updateDelegate = this;
                        if (this.widget.isInVerticalChain()) {
                            this.dimension.targets.add(this.widget.verticalRun.dimension);
                            this.widget.verticalRun.dimension.dependencies.add(this.dimension);
                            this.widget.verticalRun.dimension.updateDelegate = this;
                            this.dimension.targets.add(this.widget.verticalRun.start);
                            this.dimension.targets.add(this.widget.verticalRun.end);
                            this.widget.verticalRun.start.dependencies.add(this.dimension);
                            this.widget.verticalRun.end.dependencies.add(this.dimension);
                        } else if (this.widget.isInHorizontalChain()) {
                            this.widget.verticalRun.dimension.targets.add(this.dimension);
                            this.dimension.dependencies.add(this.widget.verticalRun.dimension);
                        } else {
                            this.widget.verticalRun.dimension.targets.add(this.dimension);
                        }
                    } else {
                        DependencyNode targetDimension2 = this.widget.verticalRun.dimension;
                        this.dimension.targets.add(targetDimension2);
                        targetDimension2.dependencies.add(this.dimension);
                        this.widget.verticalRun.start.dependencies.add(this.dimension);
                        this.widget.verticalRun.end.dependencies.add(this.dimension);
                        this.dimension.delegateToWidgetRun = true;
                        this.dimension.dependencies.add(this.start);
                        this.dimension.dependencies.add(this.end);
                        this.start.targets.add(this.dimension);
                        this.end.targets.add(this.dimension);
                    }
                }
            }
            if (this.widget.mListAnchors[0].mTarget == null || this.widget.mListAnchors[1].mTarget == null) {
                if (this.widget.mListAnchors[0].mTarget != null) {
                    DependencyNode target = getTarget(this.widget.mListAnchors[0]);
                    if (target != null) {
                        addTarget(this.start, target, this.widget.mListAnchors[0].getMargin());
                        addTarget(this.end, this.start, 1, this.dimension);
                    }
                } else if (this.widget.mListAnchors[1].mTarget != null) {
                    DependencyNode target2 = getTarget(this.widget.mListAnchors[1]);
                    if (target2 != null) {
                        addTarget(this.end, target2, -this.widget.mListAnchors[1].getMargin());
                        addTarget(this.start, this.end, -1, this.dimension);
                    }
                } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null) {
                    addTarget(this.start, this.widget.getParent().horizontalRun.start, this.widget.getX());
                    addTarget(this.end, this.start, 1, this.dimension);
                }
            } else if (this.widget.isInHorizontalChain()) {
                this.start.margin = this.widget.mListAnchors[0].getMargin();
                this.end.margin = -this.widget.mListAnchors[1].getMargin();
            } else {
                DependencyNode startTarget = getTarget(this.widget.mListAnchors[0]);
                DependencyNode endTarget = getTarget(this.widget.mListAnchors[1]);
                startTarget.addDependency(this);
                endTarget.addDependency(this);
                this.mRunType = WidgetRun.RunType.CENTER;
            }
        } else if (this.widget.mListAnchors[0].mTarget == null || this.widget.mListAnchors[1].mTarget == null) {
            if (this.widget.mListAnchors[0].mTarget != null) {
                DependencyNode target3 = getTarget(this.widget.mListAnchors[0]);
                if (target3 != null) {
                    addTarget(this.start, target3, this.widget.mListAnchors[0].getMargin());
                    addTarget(this.end, this.start, this.dimension.value);
                }
            } else if (this.widget.mListAnchors[1].mTarget != null) {
                DependencyNode target4 = getTarget(this.widget.mListAnchors[1]);
                if (target4 != null) {
                    addTarget(this.end, target4, -this.widget.mListAnchors[1].getMargin());
                    addTarget(this.start, this.end, -this.dimension.value);
                }
            } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null && this.widget.getAnchor(ConstraintAnchor.Type.CENTER).mTarget == null) {
                addTarget(this.start, this.widget.getParent().horizontalRun.start, this.widget.getX());
                addTarget(this.end, this.start, this.dimension.value);
            }
        } else if (this.widget.isInHorizontalChain()) {
            this.start.margin = this.widget.mListAnchors[0].getMargin();
            this.end.margin = -this.widget.mListAnchors[1].getMargin();
        } else {
            DependencyNode startTarget2 = getTarget(this.widget.mListAnchors[0]);
            if (startTarget2 != null) {
                addTarget(this.start, startTarget2, this.widget.mListAnchors[0].getMargin());
            }
            DependencyNode endTarget2 = getTarget(this.widget.mListAnchors[1]);
            if (endTarget2 != null) {
                addTarget(this.end, endTarget2, -this.widget.mListAnchors[1].getMargin());
            }
            this.start.delegateToWidgetRun = true;
            this.end.delegateToWidgetRun = true;
        }
    }

    private void computeInsetRatio(int[] dimensions, int x1, int x2, int y1, int y2, float ratio, int side) {
        int i = side;
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (i == -1) {
            int candidateX1 = (int) ((((float) dy) * ratio) + 0.5f);
            int candidateY1 = dy;
            int candidateX2 = dx;
            int candidateY2 = (int) ((((float) dx) / ratio) + 0.5f);
            if (candidateX1 <= dx && candidateY1 <= dy) {
                dimensions[0] = candidateX1;
                dimensions[1] = candidateY1;
            } else if (candidateX2 <= dx && candidateY2 <= dy) {
                dimensions[0] = candidateX2;
                dimensions[1] = candidateY2;
            }
        } else if (i == 0) {
            dimensions[0] = (int) ((((float) dy) * ratio) + 0.5f);
            dimensions[1] = dy;
        } else if (i == 1) {
            dimensions[0] = dx;
            dimensions[1] = (int) ((((float) dx) * ratio) + 0.5f);
        }
    }

    /* renamed from: androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun$1 */
    static /* synthetic */ class C02161 {

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType */
        static final /* synthetic */ int[] f84xbf6f0c8e;

        static {
            int[] iArr = new int[WidgetRun.RunType.values().length];
            f84xbf6f0c8e = iArr;
            try {
                iArr[WidgetRun.RunType.START.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f84xbf6f0c8e[WidgetRun.RunType.END.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f84xbf6f0c8e[WidgetRun.RunType.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0327, code lost:
        if (r4 != 1) goto L_0x0398;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void update(androidx.constraintlayout.solver.widgets.analyzer.Dependency r26) {
        /*
            r25 = this;
            r8 = r25
            int[] r0 = androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun.C02161.f84xbf6f0c8e
            androidx.constraintlayout.solver.widgets.analyzer.WidgetRun$RunType r1 = r8.mRunType
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 2
            r2 = 3
            r9 = 1
            r10 = 0
            if (r0 == r9) goto L_0x002d
            if (r0 == r1) goto L_0x0027
            if (r0 == r2) goto L_0x0019
            r11 = r26
            goto L_0x0033
        L_0x0019:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mRight
            r11 = r26
            r8.updateRunCenter(r11, r0, r1, r10)
            return
        L_0x0027:
            r11 = r26
            r25.updateRunEnd(r26)
            goto L_0x0033
        L_0x002d:
            r11 = r26
            r25.updateRunStart(r26)
        L_0x0033:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            boolean r0 = r0.resolved
            r12 = 1056964608(0x3f000000, float:0.5)
            if (r0 != 0) goto L_0x0398
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = r8.dimensionBehavior
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r3) goto L_0x0398
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            int r0 = r0.mMatchConstraintDefaultWidth
            if (r0 == r1) goto L_0x0375
            if (r0 == r2) goto L_0x004b
            goto L_0x0398
        L_0x004b:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            int r0 = r0.mMatchConstraintDefaultHeight
            r1 = -1
            if (r0 == 0) goto L_0x00a6
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            int r0 = r0.mMatchConstraintDefaultHeight
            if (r0 != r2) goto L_0x0059
            goto L_0x00a6
        L_0x0059:
            r0 = 0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r8.widget
            int r2 = r2.getDimensionRatioSide()
            if (r2 == r1) goto L_0x008d
            if (r2 == 0) goto L_0x007a
            if (r2 == r9) goto L_0x0067
            goto L_0x009f
        L_0x0067:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r1.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r1 = r1.dimension
            int r1 = r1.value
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r8.widget
            float r3 = r3.getDimensionRatio()
            float r1 = r1 * r3
            float r1 = r1 + r12
            int r0 = (int) r1
            goto L_0x009f
        L_0x007a:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r1.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r1 = r1.dimension
            int r1 = r1.value
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r8.widget
            float r3 = r3.getDimensionRatio()
            float r1 = r1 / r3
            float r1 = r1 + r12
            int r0 = (int) r1
            goto L_0x009f
        L_0x008d:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r1 = r1.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r1 = r1.dimension
            int r1 = r1.value
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r8.widget
            float r3 = r3.getDimensionRatio()
            float r1 = r1 * r3
            float r1 = r1 + r12
            int r0 = (int) r1
        L_0x009f:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r1 = r8.dimension
            r1.resolve(r0)
            goto L_0x0398
        L_0x00a6:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r0.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r13 = r0.start
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r0.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r14 = r0.end
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x00bc
            r0 = r9
            goto L_0x00bd
        L_0x00bc:
            r0 = r10
        L_0x00bd:
            r15 = r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x00c8
            r0 = r9
            goto L_0x00c9
        L_0x00c8:
            r0 = r10
        L_0x00c9:
            r16 = r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x00d5
            r0 = r9
            goto L_0x00d6
        L_0x00d5:
            r0 = r10
        L_0x00d6:
            r17 = r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x00e2
            r0 = r9
            goto L_0x00e3
        L_0x00e2:
            r0 = r10
        L_0x00e3:
            r18 = r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            int r7 = r0.getDimensionRatioSide()
            if (r15 == 0) goto L_0x025f
            if (r16 == 0) goto L_0x025f
            if (r17 == 0) goto L_0x025f
            if (r18 == 0) goto L_0x025f
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            float r19 = r0.getDimensionRatio()
            boolean r0 = r13.resolved
            if (r0 == 0) goto L_0x016c
            boolean r0 = r14.resolved
            if (r0 == 0) goto L_0x016c
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            boolean r0 = r0.readyToSolve
            if (r0 == 0) goto L_0x0169
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            boolean r0 = r0.readyToSolve
            if (r0 != 0) goto L_0x0110
            r23 = r7
            goto L_0x016b
        L_0x0110:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.start
            int r1 = r1.margin
            int r12 = r0 + r1
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.end
            int r1 = r1.margin
            int r20 = r0 - r1
            int r0 = r13.value
            int r1 = r13.margin
            int r21 = r0 + r1
            int r0 = r14.value
            int r1 = r14.margin
            int r22 = r0 - r1
            int[] r1 = tempDimensions
            r0 = r25
            r2 = r12
            r3 = r20
            r4 = r21
            r5 = r22
            r6 = r19
            r23 = r7
            r0.computeInsetRatio(r1, r2, r3, r4, r5, r6, r7)
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            int[] r1 = tempDimensions
            r1 = r1[r10]
            r0.resolve(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r0.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r0.dimension
            int[] r1 = tempDimensions
            r1 = r1[r9]
            r0.resolve(r1)
            return
        L_0x0169:
            r23 = r7
        L_0x016b:
            return
        L_0x016c:
            r23 = r7
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x01de
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x01de
            boolean r0 = r13.readyToSolve
            if (r0 == 0) goto L_0x01dd
            boolean r0 = r14.readyToSolve
            if (r0 != 0) goto L_0x0183
            goto L_0x01dd
        L_0x0183:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            int r0 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.start
            int r1 = r1.margin
            int r20 = r0 + r1
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            int r0 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.end
            int r1 = r1.margin
            int r21 = r0 - r1
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r13.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            int r1 = r13.margin
            int r22 = r0 + r1
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r14.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            int r1 = r14.margin
            int r24 = r0 - r1
            int[] r1 = tempDimensions
            r0 = r25
            r2 = r20
            r3 = r21
            r4 = r22
            r5 = r24
            r6 = r19
            r7 = r23
            r0.computeInsetRatio(r1, r2, r3, r4, r5, r6, r7)
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            int[] r1 = tempDimensions
            r1 = r1[r10]
            r0.resolve(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r0.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r0.dimension
            int[] r1 = tempDimensions
            r1 = r1[r9]
            r0.resolve(r1)
            goto L_0x01de
        L_0x01dd:
            return
        L_0x01de:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            boolean r0 = r0.readyToSolve
            if (r0 == 0) goto L_0x025e
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            boolean r0 = r0.readyToSolve
            if (r0 == 0) goto L_0x025e
            boolean r0 = r13.readyToSolve
            if (r0 == 0) goto L_0x025e
            boolean r0 = r14.readyToSolve
            if (r0 != 0) goto L_0x01f3
            goto L_0x025e
        L_0x01f3:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.start
            int r1 = r1.margin
            int r20 = r0 + r1
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.end
            int r1 = r1.margin
            int r21 = r0 - r1
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r13.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            int r1 = r13.margin
            int r22 = r0 + r1
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r14.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            int r0 = r0.value
            int r1 = r14.margin
            int r24 = r0 - r1
            int[] r1 = tempDimensions
            r0 = r25
            r2 = r20
            r3 = r21
            r4 = r22
            r5 = r24
            r6 = r19
            r7 = r23
            r0.computeInsetRatio(r1, r2, r3, r4, r5, r6, r7)
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            int[] r1 = tempDimensions
            r1 = r1[r10]
            r0.resolve(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r0.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r0.dimension
            int[] r1 = tempDimensions
            r1 = r1[r9]
            r0.resolve(r1)
            goto L_0x0374
        L_0x025e:
            return
        L_0x025f:
            r23 = r7
            if (r15 == 0) goto L_0x02f3
            if (r17 == 0) goto L_0x02f3
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            boolean r0 = r0.readyToSolve
            if (r0 == 0) goto L_0x02f0
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            boolean r0 = r0.readyToSolve
            if (r0 != 0) goto L_0x0275
            r4 = r23
            goto L_0x02f2
        L_0x0275:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            float r0 = r0.getDimensionRatio()
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r2 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r2 = r2.targets
            java.lang.Object r2 = r2.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r2 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r2
            int r2 = r2.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = r8.start
            int r3 = r3.margin
            int r2 = r2 + r3
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r3 = r3.targets
            java.lang.Object r3 = r3.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r3
            int r3 = r3.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r4 = r8.end
            int r4 = r4.margin
            int r3 = r3 - r4
            r4 = r23
            if (r4 == r1) goto L_0x02cb
            if (r4 == 0) goto L_0x02cb
            if (r4 == r9) goto L_0x02a7
            goto L_0x0373
        L_0x02a7:
            int r1 = r3 - r2
            int r5 = r8.getLimitedDimension(r1, r10)
            float r6 = (float) r5
            float r6 = r6 / r0
            float r6 = r6 + r12
            int r6 = (int) r6
            int r7 = r8.getLimitedDimension(r6, r9)
            if (r6 == r7) goto L_0x02bb
            float r9 = (float) r7
            float r9 = r9 * r0
            float r9 = r9 + r12
            int r5 = (int) r9
        L_0x02bb:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r8.dimension
            r9.resolve(r5)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r9 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r9 = r9.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r9.dimension
            r9.resolve(r7)
            goto L_0x0373
        L_0x02cb:
            int r1 = r3 - r2
            int r5 = r8.getLimitedDimension(r1, r10)
            float r6 = (float) r5
            float r6 = r6 * r0
            float r6 = r6 + r12
            int r6 = (int) r6
            r7 = 1
            int r9 = r8.getLimitedDimension(r6, r7)
            if (r6 == r9) goto L_0x02e0
            float r7 = (float) r9
            float r7 = r7 / r0
            float r7 = r7 + r12
            int r5 = (int) r7
        L_0x02e0:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r7 = r8.dimension
            r7.resolve(r5)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r7 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r7 = r7.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r7 = r7.dimension
            r7.resolve(r9)
            goto L_0x0373
        L_0x02f0:
            r4 = r23
        L_0x02f2:
            return
        L_0x02f3:
            r4 = r23
            if (r16 == 0) goto L_0x0373
            if (r18 == 0) goto L_0x0373
            boolean r0 = r13.readyToSolve
            if (r0 == 0) goto L_0x0372
            boolean r0 = r14.readyToSolve
            if (r0 != 0) goto L_0x0302
            goto L_0x0372
        L_0x0302:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            float r0 = r0.getDimensionRatio()
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r2 = r13.targets
            java.lang.Object r2 = r2.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r2 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r2
            int r2 = r2.value
            int r3 = r13.margin
            int r2 = r2 + r3
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r3 = r14.targets
            java.lang.Object r3 = r3.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r3
            int r3 = r3.value
            int r5 = r14.margin
            int r3 = r3 - r5
            if (r4 == r1) goto L_0x034e
            if (r4 == 0) goto L_0x032a
            r1 = 1
            if (r4 == r1) goto L_0x034e
            goto L_0x0374
        L_0x032a:
            r1 = 1
            int r5 = r3 - r2
            int r6 = r8.getLimitedDimension(r5, r1)
            float r1 = (float) r6
            float r1 = r1 * r0
            float r1 = r1 + r12
            int r1 = (int) r1
            int r7 = r8.getLimitedDimension(r1, r10)
            if (r1 == r7) goto L_0x033f
            float r9 = (float) r7
            float r9 = r9 / r0
            float r9 = r9 + r12
            int r6 = (int) r9
        L_0x033f:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r8.dimension
            r9.resolve(r7)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r9 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r9 = r9.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r9.dimension
            r9.resolve(r6)
            goto L_0x0374
        L_0x034e:
            int r1 = r3 - r2
            r5 = 1
            int r6 = r8.getLimitedDimension(r1, r5)
            float r5 = (float) r6
            float r5 = r5 / r0
            float r5 = r5 + r12
            int r5 = (int) r5
            int r7 = r8.getLimitedDimension(r5, r10)
            if (r5 == r7) goto L_0x0363
            float r9 = (float) r7
            float r9 = r9 * r0
            float r9 = r9 + r12
            int r6 = (int) r9
        L_0x0363:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r8.dimension
            r9.resolve(r7)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r9 = r8.widget
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r9 = r9.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r9.dimension
            r9.resolve(r6)
            goto L_0x0374
        L_0x0372:
            return
        L_0x0373:
        L_0x0374:
            goto L_0x0398
        L_0x0375:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.getParent()
            if (r0 == 0) goto L_0x0398
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r1 = r0.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r1 = r1.dimension
            boolean r1 = r1.resolved
            if (r1 == 0) goto L_0x0398
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r8.widget
            float r1 = r1.mMatchConstraintPercentWidth
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r2 = r0.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r2 = r2.dimension
            int r2 = r2.value
            float r3 = (float) r2
            float r3 = r3 * r1
            float r3 = r3 + r12
            int r3 = (int) r3
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r4 = r8.dimension
            r4.resolve(r3)
        L_0x0398:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            boolean r0 = r0.readyToSolve
            if (r0 == 0) goto L_0x04c7
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            boolean r0 = r0.readyToSolve
            if (r0 != 0) goto L_0x03a6
            goto L_0x04c7
        L_0x03a6:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x03b9
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x03b9
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x03b9
            return
        L_0x03b9:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            boolean r0 = r0.resolved
            if (r0 != 0) goto L_0x0407
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = r8.dimensionBehavior
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r1) goto L_0x0407
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            int r0 = r0.mMatchConstraintDefaultWidth
            if (r0 != 0) goto L_0x0407
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r8.widget
            boolean r0 = r0.isInHorizontalChain()
            if (r0 != 0) goto L_0x0407
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r1 = r1.targets
            java.lang.Object r1 = r1.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r1
            int r2 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = r8.start
            int r3 = r3.margin
            int r2 = r2 + r3
            int r3 = r1.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r4 = r8.end
            int r4 = r4.margin
            int r3 = r3 + r4
            int r4 = r3 - r2
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r5 = r8.start
            r5.resolve(r2)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r5 = r8.end
            r5.resolve(r3)
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r5 = r8.dimension
            r5.resolve(r4)
            return
        L_0x0407:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            boolean r0 = r0.resolved
            if (r0 != 0) goto L_0x046f
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = r8.dimensionBehavior
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r1) goto L_0x046f
            int r0 = r8.matchConstraintsType
            r1 = 1
            if (r0 != r1) goto L_0x046f
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x046f
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x046f
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r1 = r1.targets
            java.lang.Object r1 = r1.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r1
            int r2 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = r8.start
            int r3 = r3.margin
            int r2 = r2 + r3
            int r3 = r1.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r4 = r8.end
            int r4 = r4.margin
            int r3 = r3 + r4
            int r4 = r3 - r2
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r5 = r8.dimension
            int r5 = r5.wrapValue
            int r5 = java.lang.Math.min(r4, r5)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r8.widget
            int r6 = r6.mMatchConstraintMaxWidth
            androidx.constraintlayout.solver.widgets.ConstraintWidget r7 = r8.widget
            int r7 = r7.mMatchConstraintMinWidth
            int r5 = java.lang.Math.max(r7, r5)
            if (r6 <= 0) goto L_0x046a
            int r5 = java.lang.Math.min(r6, r5)
        L_0x046a:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r8.dimension
            r9.resolve(r5)
        L_0x046f:
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r0 = r8.dimension
            boolean r0 = r0.resolved
            if (r0 != 0) goto L_0x0476
            return
        L_0x0476:
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r8.start
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r0 = r0.targets
            java.lang.Object r0 = r0.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r0
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r8.end
            java.util.List<androidx.constraintlayout.solver.widgets.analyzer.DependencyNode> r1 = r1.targets
            java.lang.Object r1 = r1.get(r10)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = (androidx.constraintlayout.solver.widgets.analyzer.DependencyNode) r1
            int r2 = r0.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r3 = r8.start
            int r3 = r3.margin
            int r2 = r2 + r3
            int r3 = r1.value
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r4 = r8.end
            int r4 = r4.margin
            int r3 = r3 + r4
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r8.widget
            float r4 = r4.getHorizontalBiasPercent()
            if (r0 != r1) goto L_0x04a6
            int r2 = r0.value
            int r3 = r1.value
            r4 = 1056964608(0x3f000000, float:0.5)
        L_0x04a6:
            int r5 = r3 - r2
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r6 = r8.dimension
            int r6 = r6.value
            int r5 = r5 - r6
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r6 = r8.start
            float r7 = (float) r2
            float r7 = r7 + r12
            float r9 = (float) r5
            float r9 = r9 * r4
            float r7 = r7 + r9
            int r7 = (int) r7
            r6.resolve(r7)
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r6 = r8.end
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r7 = r8.start
            int r7 = r7.value
            androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency r9 = r8.dimension
            int r9 = r9.value
            int r7 = r7 + r9
            r6.resolve(r7)
            return
        L_0x04c7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun.update(androidx.constraintlayout.solver.widgets.analyzer.Dependency):void");
    }

    public void applyToWidget() {
        if (this.start.resolved) {
            this.widget.setX(this.start.value);
        }
    }
}
