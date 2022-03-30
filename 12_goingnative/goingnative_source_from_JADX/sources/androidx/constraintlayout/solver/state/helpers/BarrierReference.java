package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.state.HelperReference;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.HelperWidget;

public class BarrierReference extends HelperReference {
    private Barrier mBarrierWidget;
    private State.Direction mDirection;
    private int mMargin;

    public BarrierReference(State state) {
        super(state, State.Helper.BARRIER);
    }

    public void setBarrierDirection(State.Direction barrierDirection) {
        this.mDirection = barrierDirection;
    }

    public void margin(Object value) {
        margin(this.mState.convertDimension(value));
    }

    public void margin(int value) {
        this.mMargin = value;
    }

    public HelperWidget getHelperWidget() {
        if (this.mBarrierWidget == null) {
            this.mBarrierWidget = new Barrier();
        }
        return this.mBarrierWidget;
    }

    public void apply() {
        getHelperWidget();
        int direction = 0;
        switch (C02101.f75xf452c4aa[this.mDirection.ordinal()]) {
            case 1:
            case 2:
                direction = 0;
                break;
            case 3:
            case 4:
                direction = 1;
                break;
            case 5:
                direction = 2;
                break;
            case 6:
                direction = 3;
                break;
        }
        this.mBarrierWidget.setBarrierType(direction);
        this.mBarrierWidget.setMargin(this.mMargin);
    }

    /* renamed from: androidx.constraintlayout.solver.state.helpers.BarrierReference$1 */
    static /* synthetic */ class C02101 {

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$state$State$Direction */
        static final /* synthetic */ int[] f75xf452c4aa;

        static {
            int[] iArr = new int[State.Direction.values().length];
            f75xf452c4aa = iArr;
            try {
                iArr[State.Direction.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f75xf452c4aa[State.Direction.START.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f75xf452c4aa[State.Direction.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f75xf452c4aa[State.Direction.END.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f75xf452c4aa[State.Direction.TOP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f75xf452c4aa[State.Direction.BOTTOM.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }
}
