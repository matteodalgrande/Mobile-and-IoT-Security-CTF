package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.C0017R;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.StateSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class MotionScene {
    static final int ANTICIPATE = 4;
    static final int BOUNCE = 5;
    private static final boolean DEBUG = false;
    static final int EASE_IN = 1;
    static final int EASE_IN_OUT = 0;
    static final int EASE_OUT = 2;
    private static final int INTERPOLATOR_REFRENCE_ID = -2;
    public static final int LAYOUT_HONOR_REQUEST = 1;
    public static final int LAYOUT_IGNORE_REQUEST = 0;
    static final int LINEAR = 3;
    private static final int SPLINE_STRING = -1;
    public static final String TAG = "MotionScene";
    static final int TRANSITION_BACKWARD = 0;
    static final int TRANSITION_FORWARD = 1;
    public static final int UNSET = -1;
    private boolean DEBUG_DESKTOP = false;
    private ArrayList<Transition> mAbstractTransitionList = new ArrayList<>();
    private HashMap<String, Integer> mConstraintSetIdMap = new HashMap<>();
    /* access modifiers changed from: private */
    public SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    Transition mCurrentTransition = null;
    /* access modifiers changed from: private */
    public int mDefaultDuration = 400;
    private Transition mDefaultTransition = null;
    private SparseIntArray mDeriveMap = new SparseIntArray();
    private boolean mDisableAutoTransition = false;
    private boolean mIgnoreTouch = false;
    private MotionEvent mLastTouchDown;
    float mLastTouchX;
    float mLastTouchY;
    /* access modifiers changed from: private */
    public int mLayoutDuringTransition = 0;
    /* access modifiers changed from: private */
    public final MotionLayout mMotionLayout;
    private boolean mMotionOutsideRegion = false;
    private boolean mRtl;
    StateSet mStateSet = null;
    private ArrayList<Transition> mTransitionList = new ArrayList<>();
    private MotionLayout.MotionTracker mVelocityTracker;

    /* access modifiers changed from: package-private */
    public void setTransition(int beginId, int endId) {
        int start = beginId;
        int end = endId;
        StateSet stateSet = this.mStateSet;
        if (stateSet != null) {
            int tmp = stateSet.stateGetConstraintID(beginId, -1, -1);
            if (tmp != -1) {
                start = tmp;
            }
            int tmp2 = this.mStateSet.stateGetConstraintID(endId, -1, -1);
            if (tmp2 != -1) {
                end = tmp2;
            }
        }
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition transition = it.next();
            if ((transition.mConstraintSetEnd == end && transition.mConstraintSetStart == start) || (transition.mConstraintSetEnd == endId && transition.mConstraintSetStart == beginId)) {
                this.mCurrentTransition = transition;
                if (transition != null && transition.mTouchResponse != null) {
                    this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
                    return;
                }
                return;
            }
        }
        Transition matchTransiton = this.mDefaultTransition;
        Iterator<Transition> it2 = this.mAbstractTransitionList.iterator();
        while (it2.hasNext()) {
            Transition transition2 = it2.next();
            if (transition2.mConstraintSetEnd == endId) {
                matchTransiton = transition2;
            }
        }
        Transition t = new Transition(this, matchTransiton);
        int unused = t.mConstraintSetStart = start;
        int unused2 = t.mConstraintSetEnd = end;
        if (start != -1) {
            this.mTransitionList.add(t);
        }
        this.mCurrentTransition = t;
    }

    public void addTransition(Transition transition) {
        int index = getIndex(transition);
        if (index == -1) {
            this.mTransitionList.add(transition);
        } else {
            this.mTransitionList.set(index, transition);
        }
    }

    public void removeTransition(Transition transition) {
        int index = getIndex(transition);
        if (index != -1) {
            this.mTransitionList.remove(index);
        }
    }

    private int getIndex(Transition transition) {
        int id = transition.mId;
        if (id != -1) {
            for (int index = 0; index < this.mTransitionList.size(); index++) {
                if (this.mTransitionList.get(index).mId == id) {
                    return index;
                }
            }
            return -1;
        }
        throw new IllegalArgumentException("The transition must have an id");
    }

    public boolean validateLayout(MotionLayout layout) {
        return layout == this.mMotionLayout && layout.mScene == this;
    }

    public void setTransition(Transition transition) {
        this.mCurrentTransition = transition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    private int getRealID(int stateid) {
        int tmp;
        StateSet stateSet = this.mStateSet;
        if (stateSet == null || (tmp = stateSet.stateGetConstraintID(stateid, -1, -1)) == -1) {
            return stateid;
        }
        return tmp;
    }

    public List<Transition> getTransitionsWithState(int stateid) {
        int stateid2 = getRealID(stateid);
        ArrayList<Transition> ret = new ArrayList<>();
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition transition = it.next();
            if (transition.mConstraintSetStart == stateid2 || transition.mConstraintSetEnd == stateid2) {
                ret.add(transition);
            }
        }
        return ret;
    }

    public void addOnClickListeners(MotionLayout motionLayout, int currentState) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition transition = it.next();
            if (transition.mOnClicks.size() > 0) {
                Iterator it2 = transition.mOnClicks.iterator();
                while (it2.hasNext()) {
                    ((Transition.TransitionOnClick) it2.next()).removeOnClickListeners(motionLayout);
                }
            }
        }
        Iterator<Transition> it3 = this.mAbstractTransitionList.iterator();
        while (it3.hasNext()) {
            Transition transition2 = it3.next();
            if (transition2.mOnClicks.size() > 0) {
                Iterator it4 = transition2.mOnClicks.iterator();
                while (it4.hasNext()) {
                    ((Transition.TransitionOnClick) it4.next()).removeOnClickListeners(motionLayout);
                }
            }
        }
        Iterator<Transition> it5 = this.mTransitionList.iterator();
        while (it5.hasNext()) {
            Transition transition3 = it5.next();
            if (transition3.mOnClicks.size() > 0) {
                Iterator it6 = transition3.mOnClicks.iterator();
                while (it6.hasNext()) {
                    ((Transition.TransitionOnClick) it6.next()).addOnClickListeners(motionLayout, currentState, transition3);
                }
            }
        }
        Iterator<Transition> it7 = this.mAbstractTransitionList.iterator();
        while (it7.hasNext()) {
            Transition transition4 = it7.next();
            if (transition4.mOnClicks.size() > 0) {
                Iterator it8 = transition4.mOnClicks.iterator();
                while (it8.hasNext()) {
                    ((Transition.TransitionOnClick) it8.next()).addOnClickListeners(motionLayout, currentState, transition4);
                }
            }
        }
    }

    public Transition bestTransitionFor(int currentState, float dx, float dy, MotionEvent mLastTouchDown2) {
        RectF region;
        float val;
        if (currentState == -1) {
            return this.mCurrentTransition;
        }
        List<Transition> candidates = getTransitionsWithState(currentState);
        float max = 0.0f;
        Transition best = null;
        RectF cache = new RectF();
        for (Transition transition : candidates) {
            if (!transition.mDisable && transition.mTouchResponse != null) {
                transition.mTouchResponse.setRTL(this.mRtl);
                RectF region2 = transition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache);
                if ((region2 == null || mLastTouchDown2 == null || region2.contains(mLastTouchDown2.getX(), mLastTouchDown2.getY())) && ((region = transition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache)) == null || mLastTouchDown2 == null || region.contains(mLastTouchDown2.getX(), mLastTouchDown2.getY()))) {
                    float val2 = transition.mTouchResponse.dot(dx, dy);
                    if (transition.mConstraintSetEnd == currentState) {
                        val = val2 * -1.0f;
                    } else {
                        val = val2 * 1.1f;
                    }
                    if (val > max) {
                        max = val;
                        best = transition;
                    }
                }
            }
        }
        return best;
    }

    public ArrayList<Transition> getDefinedTransitions() {
        return this.mTransitionList;
    }

    public Transition getTransitionById(int id) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition transition = it.next();
            if (transition.mId == id) {
                return transition;
            }
        }
        return null;
    }

    public int[] getConstraintSetIds() {
        int[] ids = new int[this.mConstraintSetMap.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = this.mConstraintSetMap.keyAt(i);
        }
        return ids;
    }

    /* access modifiers changed from: package-private */
    public boolean autoTransition(MotionLayout motionLayout, int currentState) {
        if (isProcessingTouch() || this.mDisableAutoTransition) {
            return false;
        }
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition transition = it.next();
            if (!(transition.mAutoTransition == 0 || this.mCurrentTransition == transition)) {
                if (currentState == transition.mConstraintSetStart && (transition.mAutoTransition == 4 || transition.mAutoTransition == 2)) {
                    motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    motionLayout.setTransition(transition);
                    if (transition.mAutoTransition == 4) {
                        motionLayout.transitionToEnd();
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                    } else {
                        motionLayout.setProgress(1.0f);
                        motionLayout.evaluate(true);
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                        motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                        motionLayout.onNewStateAttachHandlers();
                    }
                    return true;
                } else if (currentState == transition.mConstraintSetEnd && (transition.mAutoTransition == 3 || transition.mAutoTransition == 1)) {
                    motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    motionLayout.setTransition(transition);
                    if (transition.mAutoTransition == 3) {
                        motionLayout.transitionToStart();
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                    } else {
                        motionLayout.setProgress(0.0f);
                        motionLayout.evaluate(true);
                        motionLayout.setState(MotionLayout.TransitionState.SETUP);
                        motionLayout.setState(MotionLayout.TransitionState.MOVING);
                        motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                        motionLayout.onNewStateAttachHandlers();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isProcessingTouch() {
        return this.mVelocityTracker != null;
    }

    public void setRtl(boolean rtl) {
        this.mRtl = rtl;
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    public static class Transition {
        public static final int AUTO_ANIMATE_TO_END = 4;
        public static final int AUTO_ANIMATE_TO_START = 3;
        public static final int AUTO_JUMP_TO_END = 2;
        public static final int AUTO_JUMP_TO_START = 1;
        public static final int AUTO_NONE = 0;
        static final int TRANSITION_FLAG_FIRST_DRAW = 1;
        /* access modifiers changed from: private */
        public int mAutoTransition = 0;
        /* access modifiers changed from: private */
        public int mConstraintSetEnd = -1;
        /* access modifiers changed from: private */
        public int mConstraintSetStart = -1;
        /* access modifiers changed from: private */
        public int mDefaultInterpolator = 0;
        /* access modifiers changed from: private */
        public int mDefaultInterpolatorID = -1;
        /* access modifiers changed from: private */
        public String mDefaultInterpolatorString = null;
        /* access modifiers changed from: private */
        public boolean mDisable = false;
        /* access modifiers changed from: private */
        public int mDuration = 400;
        /* access modifiers changed from: private */
        public int mId = -1;
        /* access modifiers changed from: private */
        public boolean mIsAbstract = false;
        /* access modifiers changed from: private */
        public ArrayList<KeyFrames> mKeyFramesList = new ArrayList<>();
        private int mLayoutDuringTransition = 0;
        /* access modifiers changed from: private */
        public final MotionScene mMotionScene;
        /* access modifiers changed from: private */
        public ArrayList<TransitionOnClick> mOnClicks = new ArrayList<>();
        /* access modifiers changed from: private */
        public int mPathMotionArc = -1;
        /* access modifiers changed from: private */
        public float mStagger = 0.0f;
        /* access modifiers changed from: private */
        public TouchResponse mTouchResponse = null;
        private int mTransitionFlags = 0;

        public int getLayoutDuringTransition() {
            return this.mLayoutDuringTransition;
        }

        public void addOnClick(Context context, XmlPullParser parser) {
            this.mOnClicks.add(new TransitionOnClick(context, this, parser));
        }

        public void setAutoTransition(int type) {
            this.mAutoTransition = type;
        }

        public int getAutoTransition() {
            return this.mAutoTransition;
        }

        public int getId() {
            return this.mId;
        }

        public int getEndConstraintSetId() {
            return this.mConstraintSetEnd;
        }

        public int getStartConstraintSetId() {
            return this.mConstraintSetStart;
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public float getStagger() {
            return this.mStagger;
        }

        public List<KeyFrames> getKeyFrameList() {
            return this.mKeyFramesList;
        }

        public List<TransitionOnClick> getOnClickList() {
            return this.mOnClicks;
        }

        public TouchResponse getTouchResponse() {
            return this.mTouchResponse;
        }

        public void setStagger(float stagger) {
            this.mStagger = stagger;
        }

        public void setPathMotionArc(int arcMode) {
            this.mPathMotionArc = arcMode;
        }

        public int getPathMotionArc() {
            return this.mPathMotionArc;
        }

        public boolean isEnabled() {
            return !this.mDisable;
        }

        public void setEnable(boolean enable) {
            this.mDisable = !enable;
        }

        public String debugString(Context context) {
            String ret;
            if (this.mConstraintSetStart == -1) {
                ret = "null";
            } else {
                ret = context.getResources().getResourceEntryName(this.mConstraintSetStart);
            }
            if (this.mConstraintSetEnd == -1) {
                return ret + " -> null";
            }
            return ret + " -> " + context.getResources().getResourceEntryName(this.mConstraintSetEnd);
        }

        public boolean isTransitionFlag(int flag) {
            return (this.mTransitionFlags & flag) != 0;
        }

        static class TransitionOnClick implements View.OnClickListener {
            public static final int ANIM_TOGGLE = 17;
            public static final int ANIM_TO_END = 1;
            public static final int ANIM_TO_START = 16;
            public static final int JUMP_TO_END = 256;
            public static final int JUMP_TO_START = 4096;
            int mMode = 17;
            int mTargetId = -1;
            private final Transition mTransition;

            public TransitionOnClick(Context context, Transition transition, XmlPullParser parser) {
                this.mTransition = transition;
                TypedArray a = context.obtainStyledAttributes(Xml.asAttributeSet(parser), C0017R.styleable.OnClick);
                int N = a.getIndexCount();
                for (int i = 0; i < N; i++) {
                    int attr = a.getIndex(i);
                    if (attr == C0017R.styleable.OnClick_targetId) {
                        this.mTargetId = a.getResourceId(attr, this.mTargetId);
                    } else if (attr == C0017R.styleable.OnClick_clickAction) {
                        this.mMode = a.getInt(attr, this.mMode);
                    }
                }
                a.recycle();
            }

            public void addOnClickListeners(MotionLayout motionLayout, int currentState, Transition transition) {
                int i = this.mTargetId;
                View v = i == -1 ? motionLayout : motionLayout.findViewById(i);
                if (v == null) {
                    Log.e(MotionScene.TAG, "OnClick could not find id " + this.mTargetId);
                    return;
                }
                int start = transition.mConstraintSetStart;
                int end = transition.mConstraintSetEnd;
                if (start == -1) {
                    v.setOnClickListener(this);
                    return;
                }
                int i2 = this.mMode;
                boolean z = false;
                boolean listen = ((i2 & 1) != 0 && currentState == start) | ((i2 & 256) != 0 && currentState == start) | ((i2 & 1) != 0 && currentState == start) | ((i2 & 16) != 0 && currentState == end);
                if ((i2 & 4096) != 0 && currentState == end) {
                    z = true;
                }
                if (listen || z) {
                    v.setOnClickListener(this);
                }
            }

            public void removeOnClickListeners(MotionLayout motionLayout) {
                int i = this.mTargetId;
                if (i != -1) {
                    View v = motionLayout.findViewById(i);
                    if (v == null) {
                        Log.e(MotionScene.TAG, " (*)  could not find id " + this.mTargetId);
                    } else {
                        v.setOnClickListener((View.OnClickListener) null);
                    }
                }
            }

            /* access modifiers changed from: package-private */
            public boolean isTransitionViable(Transition current, MotionLayout tl) {
                Transition transition = this.mTransition;
                if (transition == current) {
                    return true;
                }
                int dest = transition.mConstraintSetEnd;
                int from = this.mTransition.mConstraintSetStart;
                if (from == -1) {
                    if (tl.mCurrentState != dest) {
                        return true;
                    }
                    return false;
                } else if (tl.mCurrentState == from || tl.mCurrentState == dest) {
                    return true;
                } else {
                    return false;
                }
            }

            public void onClick(View view) {
                MotionLayout tl = this.mTransition.mMotionScene.mMotionLayout;
                if (tl.isInteractionEnabled()) {
                    if (this.mTransition.mConstraintSetStart == -1) {
                        int currentState = tl.getCurrentState();
                        if (currentState == -1) {
                            tl.transitionToState(this.mTransition.mConstraintSetEnd);
                            return;
                        }
                        Transition t = new Transition(this.mTransition.mMotionScene, this.mTransition);
                        int unused = t.mConstraintSetStart = currentState;
                        int unused2 = t.mConstraintSetEnd = this.mTransition.mConstraintSetEnd;
                        tl.setTransition(t);
                        tl.transitionToEnd();
                        return;
                    }
                    Transition current = this.mTransition.mMotionScene.mCurrentTransition;
                    int i = this.mMode;
                    boolean bidirectional = false;
                    boolean forward = ((i & 1) == 0 && (i & 256) == 0) ? false : true;
                    boolean backward = ((i & 16) == 0 && (i & 4096) == 0) ? false : true;
                    if (forward && backward) {
                        bidirectional = true;
                    }
                    if (bidirectional) {
                        Transition transition = this.mTransition.mMotionScene.mCurrentTransition;
                        Transition transition2 = this.mTransition;
                        if (transition != transition2) {
                            tl.setTransition(transition2);
                        }
                        if (tl.getCurrentState() == tl.getEndState() || tl.getProgress() > 0.5f) {
                            forward = false;
                        } else {
                            backward = false;
                        }
                    }
                    if (!isTransitionViable(current, tl)) {
                        return;
                    }
                    if (forward && (1 & this.mMode) != 0) {
                        tl.setTransition(this.mTransition);
                        tl.transitionToEnd();
                    } else if (backward && (this.mMode & 16) != 0) {
                        tl.setTransition(this.mTransition);
                        tl.transitionToStart();
                    } else if (forward && (this.mMode & 256) != 0) {
                        tl.setTransition(this.mTransition);
                        tl.setProgress(1.0f);
                    } else if (backward && (this.mMode & 4096) != 0) {
                        tl.setTransition(this.mTransition);
                        tl.setProgress(0.0f);
                    }
                }
            }
        }

        Transition(MotionScene motionScene, Transition global) {
            this.mMotionScene = motionScene;
            if (global != null) {
                this.mPathMotionArc = global.mPathMotionArc;
                this.mDefaultInterpolator = global.mDefaultInterpolator;
                this.mDefaultInterpolatorString = global.mDefaultInterpolatorString;
                this.mDefaultInterpolatorID = global.mDefaultInterpolatorID;
                this.mDuration = global.mDuration;
                this.mKeyFramesList = global.mKeyFramesList;
                this.mStagger = global.mStagger;
                this.mLayoutDuringTransition = global.mLayoutDuringTransition;
            }
        }

        public Transition(int id, MotionScene motionScene, int constraintSetStartId, int constraintSetEndId) {
            this.mId = id;
            this.mMotionScene = motionScene;
            this.mConstraintSetStart = constraintSetStartId;
            this.mConstraintSetEnd = constraintSetEndId;
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
        }

        Transition(MotionScene motionScene, Context context, XmlPullParser parser) {
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
            this.mMotionScene = motionScene;
            fillFromAttributeList(motionScene, context, Xml.asAttributeSet(parser));
        }

        private void fillFromAttributeList(MotionScene motionScene, Context context, AttributeSet attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, C0017R.styleable.Transition);
            fill(motionScene, context, a);
            a.recycle();
        }

        private void fill(MotionScene motionScene, Context context, TypedArray a) {
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == C0017R.styleable.Transition_constraintSetEnd) {
                    this.mConstraintSetEnd = a.getResourceId(attr, this.mConstraintSetEnd);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.mConstraintSetEnd))) {
                        ConstraintSet cSet = new ConstraintSet();
                        cSet.load(context, this.mConstraintSetEnd);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetEnd, cSet);
                    }
                } else if (attr == C0017R.styleable.Transition_constraintSetStart) {
                    this.mConstraintSetStart = a.getResourceId(attr, this.mConstraintSetStart);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.mConstraintSetStart))) {
                        ConstraintSet cSet2 = new ConstraintSet();
                        cSet2.load(context, this.mConstraintSetStart);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetStart, cSet2);
                    }
                } else if (attr == C0017R.styleable.Transition_motionInterpolator) {
                    TypedValue type = a.peekValue(attr);
                    if (type.type == 1) {
                        int resourceId = a.getResourceId(attr, -1);
                        this.mDefaultInterpolatorID = resourceId;
                        if (resourceId != -1) {
                            this.mDefaultInterpolator = -2;
                        }
                    } else if (type.type == 3) {
                        String string = a.getString(attr);
                        this.mDefaultInterpolatorString = string;
                        if (string.indexOf("/") > 0) {
                            this.mDefaultInterpolatorID = a.getResourceId(attr, -1);
                            this.mDefaultInterpolator = -2;
                        } else {
                            this.mDefaultInterpolator = -1;
                        }
                    } else {
                        this.mDefaultInterpolator = a.getInteger(attr, this.mDefaultInterpolator);
                    }
                } else if (attr == C0017R.styleable.Transition_duration) {
                    this.mDuration = a.getInt(attr, this.mDuration);
                } else if (attr == C0017R.styleable.Transition_staggered) {
                    this.mStagger = a.getFloat(attr, this.mStagger);
                } else if (attr == C0017R.styleable.Transition_autoTransition) {
                    this.mAutoTransition = a.getInteger(attr, this.mAutoTransition);
                } else if (attr == C0017R.styleable.Transition_android_id) {
                    this.mId = a.getResourceId(attr, this.mId);
                } else if (attr == C0017R.styleable.Transition_transitionDisable) {
                    this.mDisable = a.getBoolean(attr, this.mDisable);
                } else if (attr == C0017R.styleable.Transition_pathMotionArc) {
                    this.mPathMotionArc = a.getInteger(attr, -1);
                } else if (attr == C0017R.styleable.Transition_layoutDuringTransition) {
                    this.mLayoutDuringTransition = a.getInteger(attr, 0);
                } else if (attr == C0017R.styleable.Transition_transitionFlags) {
                    this.mTransitionFlags = a.getInteger(attr, 0);
                }
            }
            if (this.mConstraintSetStart == -1) {
                this.mIsAbstract = true;
            }
        }
    }

    public MotionScene(MotionLayout layout) {
        this.mMotionLayout = layout;
    }

    MotionScene(Context context, MotionLayout layout, int resourceID) {
        this.mMotionLayout = layout;
        load(context, resourceID);
        this.mConstraintSetMap.put(C0017R.C0020id.motion_base, new ConstraintSet());
        this.mConstraintSetIdMap.put("motion_base", Integer.valueOf(C0017R.C0020id.motion_base));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008f, code lost:
        switch(r6) {
            case 0: goto L_0x0136;
            case 1: goto L_0x00f4;
            case 2: goto L_0x00b5;
            case 3: goto L_0x00b0;
            case 4: goto L_0x00a7;
            case 5: goto L_0x00a2;
            case 6: goto L_0x0094;
            default: goto L_0x0092;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0094, code lost:
        androidx.constraintlayout.motion.widget.MotionScene.Transition.access$1300(r4).add(new androidx.constraintlayout.motion.widget.KeyFrames(r13, r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a2, code lost:
        parseConstraintSet(r13, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00a7, code lost:
        r12.mStateSet = new androidx.constraintlayout.widget.StateSet(r13, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00b0, code lost:
        r4.addOnClick(r13, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00b5, code lost:
        if (r4 != null) goto L_0x00e9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b7, code lost:
        android.util.Log.v(TAG, " OnSwipe (" + r13.getResources().getResourceEntryName(r14) + ".xml:" + r1.getLineNumber() + ")");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00e9, code lost:
        androidx.constraintlayout.motion.widget.MotionScene.Transition.access$202(r4, new androidx.constraintlayout.motion.widget.TouchResponse(r13, r12.mMotionLayout, r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f4, code lost:
        r6 = r12.mTransitionList;
        r7 = new androidx.constraintlayout.motion.widget.MotionScene.Transition(r12, r13, r1);
        r4 = r7;
        r6.add(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0101, code lost:
        if (r12.mCurrentTransition != null) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0107, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$1200(r4) != false) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0109, code lost:
        r12.mCurrentTransition = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x010f, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$200(r4) == null) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0111, code lost:
        androidx.constraintlayout.motion.widget.MotionScene.Transition.access$200(r12.mCurrentTransition).setRTL(r12.mRtl);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0120, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$1200(r4) == false) goto L_0x0150;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0126, code lost:
        if (androidx.constraintlayout.motion.widget.MotionScene.Transition.access$000(r4) != -1) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0128, code lost:
        r12.mDefaultTransition = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x012b, code lost:
        r12.mAbstractTransitionList.add(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0130, code lost:
        r12.mTransitionList.remove(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0136, code lost:
        parseMotionSceneTags(r13, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x013a, code lost:
        android.util.Log.v(TAG, "WARNING UNKNOWN ATTRIBUTE " + r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void load(android.content.Context r13, int r14) {
        /*
            r12 = this;
            android.content.res.Resources r0 = r13.getResources()
            android.content.res.XmlResourceParser r1 = r0.getXml(r14)
            r2 = 0
            r3 = 0
            r4 = 0
            int r5 = r1.getEventType()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x000f:
            r6 = 1
            if (r5 == r6) goto L_0x015e
            if (r5 == 0) goto L_0x0151
            r7 = 3
            r8 = 2
            if (r5 == r8) goto L_0x001f
            if (r5 == r7) goto L_0x001c
            goto L_0x0157
        L_0x001c:
            r3 = 0
            goto L_0x0157
        L_0x001f:
            java.lang.String r9 = r1.getName()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r3 = r9
            boolean r9 = r12.DEBUG_DESKTOP     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r9 == 0) goto L_0x0040
            java.io.PrintStream r9 = java.lang.System.out     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r10.<init>()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r11 = "parsing = "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.StringBuilder r10 = r10.append(r3)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r10 = r10.toString()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r9.println(r10)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x0040:
            int r9 = r3.hashCode()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r10 = -1
            java.lang.String r11 = "MotionScene"
            switch(r9) {
                case -1349929691: goto L_0x0084;
                case -1239391468: goto L_0x007a;
                case 269306229: goto L_0x0071;
                case 312750793: goto L_0x0067;
                case 327855227: goto L_0x005d;
                case 793277014: goto L_0x0055;
                case 1382829617: goto L_0x004b;
                default: goto L_0x004a;
            }
        L_0x004a:
            goto L_0x008e
        L_0x004b:
            java.lang.String r6 = "StateSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x004a
            r6 = 4
            goto L_0x008f
        L_0x0055:
            boolean r6 = r3.equals(r11)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x004a
            r6 = 0
            goto L_0x008f
        L_0x005d:
            java.lang.String r6 = "OnSwipe"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x004a
            r6 = r8
            goto L_0x008f
        L_0x0067:
            java.lang.String r6 = "OnClick"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x004a
            r6 = r7
            goto L_0x008f
        L_0x0071:
            java.lang.String r7 = "Transition"
            boolean r7 = r3.equals(r7)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r7 == 0) goto L_0x004a
            goto L_0x008f
        L_0x007a:
            java.lang.String r6 = "KeyFrameSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x004a
            r6 = 6
            goto L_0x008f
        L_0x0084:
            java.lang.String r6 = "ConstraintSet"
            boolean r6 = r3.equals(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x004a
            r6 = 5
            goto L_0x008f
        L_0x008e:
            r6 = r10
        L_0x008f:
            switch(r6) {
                case 0: goto L_0x0136;
                case 1: goto L_0x00f4;
                case 2: goto L_0x00b5;
                case 3: goto L_0x00b0;
                case 4: goto L_0x00a7;
                case 5: goto L_0x00a2;
                case 6: goto L_0x0094;
                default: goto L_0x0092;
            }     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x0092:
            goto L_0x013a
        L_0x0094:
            androidx.constraintlayout.motion.widget.KeyFrames r6 = new androidx.constraintlayout.motion.widget.KeyFrames     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.<init>(r13, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.util.ArrayList r7 = r4.mKeyFramesList     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r7.add(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x00a2:
            r12.parseConstraintSet(r13, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x00a7:
            androidx.constraintlayout.widget.StateSet r6 = new androidx.constraintlayout.widget.StateSet     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.<init>(r13, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r12.mStateSet = r6     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x00b0:
            r4.addOnClick(r13, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x00b5:
            if (r4 != 0) goto L_0x00e9
            android.content.res.Resources r6 = r13.getResources()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r6 = r6.getResourceEntryName(r14)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            int r7 = r1.getLineNumber()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r8.<init>()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r9 = " OnSwipe ("
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.StringBuilder r8 = r8.append(r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r9 = ".xml:"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.StringBuilder r8 = r8.append(r7)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r9 = ")"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r8 = r8.toString()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            android.util.Log.v(r11, r8)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x00e9:
            androidx.constraintlayout.motion.widget.TouchResponse r6 = new androidx.constraintlayout.motion.widget.TouchResponse     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            androidx.constraintlayout.motion.widget.MotionLayout r7 = r12.mMotionLayout     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.<init>(r13, r7, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            androidx.constraintlayout.motion.widget.TouchResponse unused = r4.mTouchResponse = r6     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x00f4:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r6 = r12.mTransitionList     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = new androidx.constraintlayout.motion.widget.MotionScene$Transition     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r7.<init>(r12, r13, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r4 = r7
            r6.add(r7)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            androidx.constraintlayout.motion.widget.MotionScene$Transition r6 = r12.mCurrentTransition     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 != 0) goto L_0x011c
            boolean r6 = r4.mIsAbstract     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 != 0) goto L_0x011c
            r12.mCurrentTransition = r4     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            androidx.constraintlayout.motion.widget.TouchResponse r6 = r4.mTouchResponse     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x011c
            androidx.constraintlayout.motion.widget.MotionScene$Transition r6 = r12.mCurrentTransition     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            androidx.constraintlayout.motion.widget.TouchResponse r6 = r6.mTouchResponse     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            boolean r7 = r12.mRtl     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.setRTL(r7)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x011c:
            boolean r6 = r4.mIsAbstract     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 == 0) goto L_0x0150
            int r6 = r4.mConstraintSetEnd     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            if (r6 != r10) goto L_0x012b
            r12.mDefaultTransition = r4     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0130
        L_0x012b:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r6 = r12.mAbstractTransitionList     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.add(r4)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x0130:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r6 = r12.mTransitionList     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.remove(r4)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x0136:
            r12.parseMotionSceneTags(r13, r1)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            goto L_0x0150
        L_0x013a:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r6.<init>()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r7 = "WARNING UNKNOWN ATTRIBUTE "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.StringBuilder r6 = r6.append(r3)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            java.lang.String r6 = r6.toString()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            android.util.Log.v(r11, r6)     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
        L_0x0150:
            goto L_0x0157
        L_0x0151:
            java.lang.String r6 = r1.getName()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r2 = r6
        L_0x0157:
            int r6 = r1.next()     // Catch:{ XmlPullParserException -> 0x0164, IOException -> 0x015f }
            r5 = r6
            goto L_0x000f
        L_0x015e:
            goto L_0x0168
        L_0x015f:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0169
        L_0x0164:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0168:
        L_0x0169:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.load(android.content.Context, int):void");
    }

    private void parseMotionSceneTags(Context context, XmlPullParser parser) {
        TypedArray a = context.obtainStyledAttributes(Xml.asAttributeSet(parser), C0017R.styleable.MotionScene);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == C0017R.styleable.MotionScene_defaultDuration) {
                this.mDefaultDuration = a.getInt(attr, this.mDefaultDuration);
            } else if (attr == C0017R.styleable.MotionScene_layoutDuringTransition) {
                this.mLayoutDuringTransition = a.getInteger(attr, 0);
            }
        }
        a.recycle();
    }

    private int getId(Context context, String idString) {
        int id = -1;
        if (idString.contains("/")) {
            id = context.getResources().getIdentifier(idString.substring(idString.indexOf(47) + 1), "id", context.getPackageName());
            if (this.DEBUG_DESKTOP) {
                System.out.println("id getMap res = " + id);
            }
        }
        if (id != -1) {
            return id;
        }
        if (idString != null && idString.length() > 1) {
            return Integer.parseInt(idString.substring(1));
        }
        Log.e(TAG, "error in parsing id");
        return id;
    }

    private void parseConstraintSet(Context context, XmlPullParser parser) {
        ConstraintSet set = new ConstraintSet();
        set.setForceId(false);
        int count = parser.getAttributeCount();
        int id = -1;
        int derivedId = -1;
        int i = 0;
        while (true) {
            char c = 65535;
            if (i >= count) {
                break;
            }
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            if (this.DEBUG_DESKTOP) {
                System.out.println("id string = " + value);
            }
            int hashCode = name.hashCode();
            if (hashCode != -1496482599) {
                if (hashCode == 3355 && name.equals("id")) {
                    c = 0;
                }
            } else if (name.equals("deriveConstraintsFrom")) {
                c = 1;
            }
            if (c == 0) {
                id = getId(context, value);
                this.mConstraintSetIdMap.put(stripID(value), Integer.valueOf(id));
            } else if (c == 1) {
                derivedId = getId(context, value);
            }
            i++;
        }
        if (id != -1) {
            if (this.mMotionLayout.mDebugPath != 0) {
                set.setValidateOnParse(true);
            }
            set.load(context, parser);
            if (derivedId != -1) {
                this.mDeriveMap.put(id, derivedId);
            }
            this.mConstraintSetMap.put(id, set);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    public ConstraintSet getConstraintSet(Context context, String id) {
        if (this.DEBUG_DESKTOP) {
            System.out.println("id " + id);
            System.out.println("size " + this.mConstraintSetMap.size());
        }
        for (int i = 0; i < this.mConstraintSetMap.size(); i++) {
            int key = this.mConstraintSetMap.keyAt(i);
            String IdAsString = context.getResources().getResourceName(key);
            if (this.DEBUG_DESKTOP) {
                System.out.println("Id for <" + i + "> is <" + IdAsString + "> looking for <" + id + ">");
            }
            if (id.equals(IdAsString)) {
                return this.mConstraintSetMap.get(key);
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public ConstraintSet getConstraintSet(int id) {
        return getConstraintSet(id, -1, -1);
    }

    /* access modifiers changed from: package-private */
    public ConstraintSet getConstraintSet(int id, int width, int height) {
        int cid;
        if (this.DEBUG_DESKTOP) {
            System.out.println("id " + id);
            System.out.println("size " + this.mConstraintSetMap.size());
        }
        StateSet stateSet = this.mStateSet;
        if (!(stateSet == null || (cid = stateSet.stateGetConstraintID(id, width, height)) == -1)) {
            id = cid;
        }
        if (this.mConstraintSetMap.get(id) != null) {
            return this.mConstraintSetMap.get(id);
        }
        Log.e(TAG, "Warning could not find ConstraintSet id/" + Debug.getName(this.mMotionLayout.getContext(), id) + " In MotionScene");
        SparseArray<ConstraintSet> sparseArray = this.mConstraintSetMap;
        return sparseArray.get(sparseArray.keyAt(0));
    }

    public void setConstraintSet(int id, ConstraintSet set) {
        this.mConstraintSetMap.put(id, set);
    }

    public void getKeyFrames(MotionController motionController) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            Transition transition2 = this.mDefaultTransition;
            if (transition2 != null) {
                Iterator it = transition2.mKeyFramesList.iterator();
                while (it.hasNext()) {
                    ((KeyFrames) it.next()).addFrames(motionController);
                }
                return;
            }
            return;
        }
        Iterator it2 = transition.mKeyFramesList.iterator();
        while (it2.hasNext()) {
            ((KeyFrames) it2.next()).addFrames(motionController);
        }
    }

    /* access modifiers changed from: package-private */
    public Key getKeyFrame(Context context, int type, int target, int position) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return null;
        }
        Iterator it = transition.mKeyFramesList.iterator();
        while (it.hasNext()) {
            KeyFrames keyFrames = (KeyFrames) it.next();
            Iterator<Integer> it2 = keyFrames.getKeys().iterator();
            while (true) {
                if (it2.hasNext()) {
                    Integer integer = it2.next();
                    if (target == integer.intValue()) {
                        Iterator<Key> it3 = keyFrames.getKeyFramesForView(integer.intValue()).iterator();
                        while (it3.hasNext()) {
                            Key key = it3.next();
                            if (key.mFramePosition == position && key.mType == type) {
                                return key;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int getTransitionDirection(int stateId) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            if (it.next().mConstraintSetStart == stateId) {
                return 0;
            }
        }
        return 1;
    }

    /* access modifiers changed from: package-private */
    public boolean hasKeyFramePosition(View view, int position) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return false;
        }
        Iterator it = transition.mKeyFramesList.iterator();
        while (it.hasNext()) {
            Iterator<Key> it2 = ((KeyFrames) it.next()).getKeyFramesForView(view.getId()).iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (it2.next().mFramePosition == position) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setKeyframe(View view, int position, String name, Object value) {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            Iterator it = transition.mKeyFramesList.iterator();
            while (it.hasNext()) {
                Iterator<Key> it2 = ((KeyFrames) it.next()).getKeyFramesForView(view.getId()).iterator();
                while (it2.hasNext()) {
                    if (it2.next().mFramePosition == position) {
                        float v = 0.0f;
                        if (value != null) {
                            v = ((Float) value).floatValue();
                        }
                        if (v == 0.0f) {
                        }
                        name.equalsIgnoreCase("app:PerpendicularPath_percent");
                    }
                }
            }
        }
    }

    public float getPathPercent(View view, int position) {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public boolean supportTouch() {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            if (it.next().mTouchResponse != null) {
                return true;
            }
        }
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void processTouchEvent(MotionEvent event, int currentState, MotionLayout motionLayout) {
        MotionLayout.MotionTracker motionTracker;
        MotionEvent motionEvent;
        RectF cache = new RectF();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = this.mMotionLayout.obtainVelocityTracker();
        }
        this.mVelocityTracker.addMovement(event);
        if (currentState != -1) {
            int action = event.getAction();
            boolean z = false;
            if (action == 0) {
                this.mLastTouchX = event.getRawX();
                this.mLastTouchY = event.getRawY();
                this.mLastTouchDown = event;
                this.mIgnoreTouch = false;
                if (this.mCurrentTransition.mTouchResponse != null) {
                    RectF region = this.mCurrentTransition.mTouchResponse.getLimitBoundsTo(this.mMotionLayout, cache);
                    if (region == null || region.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                        RectF region2 = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache);
                        if (region2 == null || region2.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            this.mMotionOutsideRegion = false;
                        } else {
                            this.mMotionOutsideRegion = true;
                        }
                        this.mCurrentTransition.mTouchResponse.setDown(this.mLastTouchX, this.mLastTouchY);
                        return;
                    }
                    this.mLastTouchDown = null;
                    this.mIgnoreTouch = true;
                    return;
                }
                return;
            } else if (action == 2 && !this.mIgnoreTouch) {
                float dy = event.getRawY() - this.mLastTouchY;
                float dx = event.getRawX() - this.mLastTouchX;
                if ((((double) dx) != 0.0d || ((double) dy) != 0.0d) && (motionEvent = this.mLastTouchDown) != null) {
                    Transition transition = bestTransitionFor(currentState, dx, dy, motionEvent);
                    if (transition != null) {
                        motionLayout.setTransition(transition);
                        RectF region3 = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache);
                        if (region3 != null && !region3.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            z = true;
                        }
                        this.mMotionOutsideRegion = z;
                        this.mCurrentTransition.mTouchResponse.setUpTouchEvent(this.mLastTouchX, this.mLastTouchY);
                    }
                } else {
                    return;
                }
            }
        }
        if (!this.mIgnoreTouch) {
            Transition transition2 = this.mCurrentTransition;
            if (!(transition2 == null || transition2.mTouchResponse == null || this.mMotionOutsideRegion)) {
                this.mCurrentTransition.mTouchResponse.processTouchEvent(event, this.mVelocityTracker, currentState, this);
            }
            this.mLastTouchX = event.getRawX();
            this.mLastTouchY = event.getRawY();
            if (event.getAction() == 1 && (motionTracker = this.mVelocityTracker) != null) {
                motionTracker.recycle();
                this.mVelocityTracker = null;
                if (motionLayout.mCurrentState != -1) {
                    autoTransition(motionLayout, motionLayout.mCurrentState);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void processScrollMove(float dx, float dy) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollMove(dx, dy);
        }
    }

    /* access modifiers changed from: package-private */
    public void processScrollUp(float dx, float dy) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollUp(dx, dy);
        }
    }

    /* access modifiers changed from: package-private */
    public float getProgressDirection(float dx, float dy) {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getProgressDirection(dx, dy);
    }

    /* access modifiers changed from: package-private */
    public int getStartId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetStart;
    }

    /* access modifiers changed from: package-private */
    public int getEndId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetEnd;
    }

    public Interpolator getInterpolator() {
        int access$1400 = this.mCurrentTransition.mDefaultInterpolator;
        if (access$1400 == -2) {
            return AnimationUtils.loadInterpolator(this.mMotionLayout.getContext(), this.mCurrentTransition.mDefaultInterpolatorID);
        }
        if (access$1400 == -1) {
            final Easing easing = Easing.getInterpolator(this.mCurrentTransition.mDefaultInterpolatorString);
            return new Interpolator() {
                public float getInterpolation(float v) {
                    return (float) easing.get((double) v);
                }
            };
        } else if (access$1400 == 0) {
            return new AccelerateDecelerateInterpolator();
        } else {
            if (access$1400 == 1) {
                return new AccelerateInterpolator();
            }
            if (access$1400 == 2) {
                return new DecelerateInterpolator();
            }
            if (access$1400 == 4) {
                return new AnticipateInterpolator();
            }
            if (access$1400 != 5) {
                return null;
            }
            return new BounceInterpolator();
        }
    }

    public int getDuration() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mDuration;
        }
        return this.mDefaultDuration;
    }

    public void setDuration(int duration) {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            transition.setDuration(duration);
        } else {
            this.mDefaultDuration = duration;
        }
    }

    public int gatPathMotionArc() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mPathMotionArc;
        }
        return -1;
    }

    public float getStaggered() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mStagger;
        }
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public float getMaxAcceleration() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getMaxAcceleration();
    }

    /* access modifiers changed from: package-private */
    public float getMaxVelocity() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getMaxVelocity();
    }

    /* access modifiers changed from: package-private */
    public void setupTouch() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setupTouch();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean getMoveWhenScrollAtTop() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return false;
        }
        return this.mCurrentTransition.mTouchResponse.getMoveWhenScrollAtTop();
    }

    /* access modifiers changed from: package-private */
    public void readFallback(MotionLayout motionLayout) {
        int i = 0;
        while (i < this.mConstraintSetMap.size()) {
            int key = this.mConstraintSetMap.keyAt(i);
            if (hasCycleDependency(key)) {
                Log.e(TAG, "Cannot be derived from yourself");
                return;
            } else {
                readConstraintChain(key);
                i++;
            }
        }
        for (int i2 = 0; i2 < this.mConstraintSetMap.size(); i2++) {
            this.mConstraintSetMap.valueAt(i2).readFallback((ConstraintLayout) motionLayout);
        }
    }

    private boolean hasCycleDependency(int key) {
        int derived = this.mDeriveMap.get(key);
        int len = this.mDeriveMap.size();
        while (derived > 0) {
            if (derived == key) {
                return true;
            }
            int len2 = len - 1;
            if (len < 0) {
                return true;
            }
            derived = this.mDeriveMap.get(derived);
            len = len2;
        }
        return false;
    }

    private void readConstraintChain(int key) {
        int derivedFromId = this.mDeriveMap.get(key);
        if (derivedFromId > 0) {
            readConstraintChain(this.mDeriveMap.get(key));
            ConstraintSet cs = this.mConstraintSetMap.get(key);
            ConstraintSet derivedFrom = this.mConstraintSetMap.get(derivedFromId);
            if (derivedFrom == null) {
                Log.e(TAG, "ERROR! invalid deriveConstraintsFrom: @id/" + Debug.getName(this.mMotionLayout.getContext(), derivedFromId));
                return;
            }
            cs.readFallback(derivedFrom);
            this.mDeriveMap.put(key, -1);
        }
    }

    public static String stripID(String id) {
        if (id == null) {
            return "";
        }
        int index = id.indexOf(47);
        if (index < 0) {
            return id;
        }
        return id.substring(index + 1);
    }

    public int lookUpConstraintId(String id) {
        return this.mConstraintSetIdMap.get(id).intValue();
    }

    public String lookUpConstraintName(int id) {
        for (Map.Entry<String, Integer> entry : this.mConstraintSetIdMap.entrySet()) {
            if (entry.getValue().intValue() == id) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void disableAutoTransition(boolean disable) {
        this.mDisableAutoTransition = disable;
    }
}
