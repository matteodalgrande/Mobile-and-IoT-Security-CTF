package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class StateSet {
    private static final boolean DEBUG = false;
    public static final String TAG = "ConstraintLayoutStates";
    private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    private ConstraintsChangedListener mConstraintsChangedListener = null;
    int mCurrentConstraintNumber = -1;
    int mCurrentStateId = -1;
    ConstraintSet mDefaultConstraintSet;
    int mDefaultState = -1;
    private SparseArray<State> mStateList = new SparseArray<>();

    public StateSet(Context context, XmlPullParser parser) {
        load(context, parser);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void load(android.content.Context r14, org.xmlpull.v1.XmlPullParser r15) {
        /*
            r13 = this;
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r15)
            int[] r1 = androidx.constraintlayout.widget.C0017R.styleable.StateSet
            android.content.res.TypedArray r1 = r14.obtainStyledAttributes(r0, r1)
            int r2 = r1.getIndexCount()
            r3 = 0
        L_0x000f:
            if (r3 >= r2) goto L_0x0024
            int r4 = r1.getIndex(r3)
            int r5 = androidx.constraintlayout.widget.C0017R.styleable.StateSet_defaultState
            if (r4 != r5) goto L_0x0021
            int r5 = r13.mDefaultState
            int r5 = r1.getResourceId(r4, r5)
            r13.mDefaultState = r5
        L_0x0021:
            int r3 = r3 + 1
            goto L_0x000f
        L_0x0024:
            r1.recycle()
            r3 = 0
            r4 = 0
            r5 = 0
            int r6 = r15.getEventType()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
        L_0x002e:
            r7 = 1
            if (r6 == r7) goto L_0x00c8
            if (r6 == 0) goto L_0x00bb
            java.lang.String r8 = "StateSet"
            r9 = 3
            r10 = 2
            if (r6 == r10) goto L_0x004b
            if (r6 == r9) goto L_0x003d
            goto L_0x00c1
        L_0x003d:
            java.lang.String r7 = r15.getName()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            boolean r7 = r8.equals(r7)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            if (r7 == 0) goto L_0x0048
            return
        L_0x0048:
            r3 = 0
            goto L_0x00c1
        L_0x004b:
            java.lang.String r11 = r15.getName()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r3 = r11
            r11 = -1
            int r12 = r3.hashCode()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            switch(r12) {
                case 80204913: goto L_0x0075;
                case 1301459538: goto L_0x006b;
                case 1382829617: goto L_0x0063;
                case 1901439077: goto L_0x0059;
                default: goto L_0x0058;
            }     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
        L_0x0058:
            goto L_0x007e
        L_0x0059:
            java.lang.String r8 = "Variant"
            boolean r8 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            if (r8 == 0) goto L_0x0058
            r11 = r9
            goto L_0x007e
        L_0x0063:
            boolean r8 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            if (r8 == 0) goto L_0x0058
            r11 = r7
            goto L_0x007e
        L_0x006b:
            java.lang.String r8 = "LayoutDescription"
            boolean r8 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            if (r8 == 0) goto L_0x0058
            r11 = 0
            goto L_0x007e
        L_0x0075:
            java.lang.String r8 = "State"
            boolean r8 = r3.equals(r8)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            if (r8 == 0) goto L_0x0058
            r11 = r10
        L_0x007e:
            if (r11 == 0) goto L_0x00b9
            if (r11 == r7) goto L_0x00b8
            if (r11 == r10) goto L_0x00aa
            if (r11 == r9) goto L_0x009f
            java.lang.String r7 = "ConstraintLayoutStates"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r8.<init>()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            java.lang.String r9 = "unknown tag "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            java.lang.StringBuilder r8 = r8.append(r3)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            java.lang.String r8 = r8.toString()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            android.util.Log.v(r7, r8)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            goto L_0x00ba
        L_0x009f:
            androidx.constraintlayout.widget.StateSet$Variant r7 = new androidx.constraintlayout.widget.StateSet$Variant     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r7.<init>(r14, r15)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            if (r5 == 0) goto L_0x00ba
            r5.add(r7)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            goto L_0x00ba
        L_0x00aa:
            androidx.constraintlayout.widget.StateSet$State r7 = new androidx.constraintlayout.widget.StateSet$State     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r7.<init>(r14, r15)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r5 = r7
            android.util.SparseArray<androidx.constraintlayout.widget.StateSet$State> r7 = r13.mStateList     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            int r8 = r5.mId     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r7.put(r8, r5)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            goto L_0x00ba
        L_0x00b8:
            goto L_0x00ba
        L_0x00b9:
        L_0x00ba:
            goto L_0x00c1
        L_0x00bb:
            java.lang.String r7 = r15.getName()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r4 = r7
        L_0x00c1:
            int r7 = r15.next()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c9 }
            r6 = r7
            goto L_0x002e
        L_0x00c8:
            goto L_0x00d2
        L_0x00c9:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x00d3
        L_0x00ce:
            r4 = move-exception
            r4.printStackTrace()
        L_0x00d2:
        L_0x00d3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.StateSet.load(android.content.Context, org.xmlpull.v1.XmlPullParser):void");
    }

    public boolean needsToChange(int id, float width, float height) {
        int i = this.mCurrentStateId;
        if (i != id) {
            return true;
        }
        State state = (State) (id == -1 ? this.mStateList.valueAt(0) : this.mStateList.get(i));
        if ((this.mCurrentConstraintNumber == -1 || !state.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) && this.mCurrentConstraintNumber != state.findMatch(width, height)) {
            return true;
        }
        return false;
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
    }

    public int stateGetConstraintID(int id, int width, int height) {
        return updateConstraints(-1, id, (float) width, (float) height);
    }

    public int convertToConstraintSet(int currentConstrainSettId, int stateId, float width, float height) {
        State state = this.mStateList.get(stateId);
        if (state == null) {
            return stateId;
        }
        if (width != -1.0f && height != -1.0f) {
            Variant match = null;
            Iterator<Variant> it = state.mVariants.iterator();
            while (it.hasNext()) {
                Variant mVariant = it.next();
                if (mVariant.match(width, height)) {
                    if (currentConstrainSettId == mVariant.mConstraintID) {
                        return currentConstrainSettId;
                    }
                    match = mVariant;
                }
            }
            if (match != null) {
                return match.mConstraintID;
            }
            return state.mConstraintID;
        } else if (state.mConstraintID == currentConstrainSettId) {
            return currentConstrainSettId;
        } else {
            Iterator<Variant> it2 = state.mVariants.iterator();
            while (it2.hasNext()) {
                if (currentConstrainSettId == it2.next().mConstraintID) {
                    return currentConstrainSettId;
                }
            }
            return state.mConstraintID;
        }
    }

    public int updateConstraints(int currentid, int id, float width, float height) {
        State state;
        int match;
        if (currentid == id) {
            if (id == -1) {
                state = this.mStateList.valueAt(0);
            } else {
                state = this.mStateList.get(this.mCurrentStateId);
            }
            if (state == null) {
                return -1;
            }
            if ((this.mCurrentConstraintNumber == -1 || !state.mVariants.get(currentid).match(width, height)) && currentid != (match = state.findMatch(width, height))) {
                return match == -1 ? state.mConstraintID : state.mVariants.get(match).mConstraintID;
            }
            return currentid;
        }
        State state2 = this.mStateList.get(id);
        if (state2 == null) {
            return -1;
        }
        int match2 = state2.findMatch(width, height);
        return match2 == -1 ? state2.mConstraintID : state2.mVariants.get(match2).mConstraintID;
    }

    static class State {
        int mConstraintID = -1;
        int mId;
        boolean mIsLayout = false;
        ArrayList<Variant> mVariants = new ArrayList<>();

        public State(Context context, XmlPullParser parser) {
            TypedArray a = context.obtainStyledAttributes(Xml.asAttributeSet(parser), C0017R.styleable.State);
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == C0017R.styleable.State_android_id) {
                    this.mId = a.getResourceId(attr, this.mId);
                } else if (attr == C0017R.styleable.State_constraints) {
                    this.mConstraintID = a.getResourceId(attr, this.mConstraintID);
                    String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    String resourceName = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        this.mIsLayout = true;
                    }
                }
            }
            a.recycle();
        }

        /* access modifiers changed from: package-private */
        public void add(Variant size) {
            this.mVariants.add(size);
        }

        public int findMatch(float width, float height) {
            for (int i = 0; i < this.mVariants.size(); i++) {
                if (this.mVariants.get(i).match(width, height)) {
                    return i;
                }
            }
            return -1;
        }
    }

    static class Variant {
        int mConstraintID = -1;
        int mId;
        boolean mIsLayout = false;
        float mMaxHeight = Float.NaN;
        float mMaxWidth = Float.NaN;
        float mMinHeight = Float.NaN;
        float mMinWidth = Float.NaN;

        public Variant(Context context, XmlPullParser parser) {
            TypedArray a = context.obtainStyledAttributes(Xml.asAttributeSet(parser), C0017R.styleable.Variant);
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == C0017R.styleable.Variant_constraints) {
                    this.mConstraintID = a.getResourceId(attr, this.mConstraintID);
                    String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    String resourceName = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        this.mIsLayout = true;
                    }
                } else if (attr == C0017R.styleable.Variant_region_heightLessThan) {
                    this.mMaxHeight = a.getDimension(attr, this.mMaxHeight);
                } else if (attr == C0017R.styleable.Variant_region_heightMoreThan) {
                    this.mMinHeight = a.getDimension(attr, this.mMinHeight);
                } else if (attr == C0017R.styleable.Variant_region_widthLessThan) {
                    this.mMaxWidth = a.getDimension(attr, this.mMaxWidth);
                } else if (attr == C0017R.styleable.Variant_region_widthMoreThan) {
                    this.mMinWidth = a.getDimension(attr, this.mMinWidth);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            a.recycle();
        }

        /* access modifiers changed from: package-private */
        public boolean match(float widthDp, float heightDp) {
            if (!Float.isNaN(this.mMinWidth) && widthDp < this.mMinWidth) {
                return false;
            }
            if (!Float.isNaN(this.mMinHeight) && heightDp < this.mMinHeight) {
                return false;
            }
            if (!Float.isNaN(this.mMaxWidth) && widthDp > this.mMaxWidth) {
                return false;
            }
            if (Float.isNaN(this.mMaxHeight) || heightDp <= this.mMaxHeight) {
                return true;
            }
            return false;
        }
    }
}
