package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.google.android.material.color.MaterialColors;

final class CircularDrawingDelegate extends DrawingDelegate<CircularProgressIndicatorSpec> {
    private float adjustedRadius;
    private int arcDirectionFactor = 1;
    private float displayedCornerRadius;
    private float displayedTrackThickness;

    public CircularDrawingDelegate(CircularProgressIndicatorSpec spec) {
        super(spec);
    }

    public int getPreferredWidth() {
        return getSize();
    }

    public int getPreferredHeight() {
        return getSize();
    }

    public void adjustCanvas(Canvas canvas, float trackThicknessFraction) {
        float outerRadiusWithInset = (((float) ((CircularProgressIndicatorSpec) this.spec).indicatorSize) / 2.0f) + ((float) ((CircularProgressIndicatorSpec) this.spec).indicatorInset);
        canvas.translate(outerRadiusWithInset, outerRadiusWithInset);
        canvas.rotate(-90.0f);
        canvas.clipRect(-outerRadiusWithInset, -outerRadiusWithInset, outerRadiusWithInset, outerRadiusWithInset);
        this.arcDirectionFactor = ((CircularProgressIndicatorSpec) this.spec).indicatorDirection == 0 ? 1 : -1;
        this.displayedTrackThickness = ((float) ((CircularProgressIndicatorSpec) this.spec).trackThickness) * trackThicknessFraction;
        this.displayedCornerRadius = ((float) ((CircularProgressIndicatorSpec) this.spec).trackCornerRadius) * trackThicknessFraction;
        this.adjustedRadius = ((float) (((CircularProgressIndicatorSpec) this.spec).indicatorSize - ((CircularProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f;
        if ((this.drawable.isShowing() && ((CircularProgressIndicatorSpec) this.spec).showAnimationBehavior == 2) || (this.drawable.isHiding() && ((CircularProgressIndicatorSpec) this.spec).hideAnimationBehavior == 1)) {
            this.adjustedRadius += ((1.0f - trackThicknessFraction) * ((float) ((CircularProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f;
        } else if ((this.drawable.isShowing() && ((CircularProgressIndicatorSpec) this.spec).showAnimationBehavior == 1) || (this.drawable.isHiding() && ((CircularProgressIndicatorSpec) this.spec).hideAnimationBehavior == 2)) {
            this.adjustedRadius -= ((1.0f - trackThicknessFraction) * ((float) ((CircularProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f;
        }
    }

    /* access modifiers changed from: package-private */
    public void fillIndicator(Canvas canvas, Paint paint, float startFraction, float endFraction, int color) {
        Paint paint2 = paint;
        if (startFraction != endFraction) {
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeCap(Paint.Cap.BUTT);
            paint2.setAntiAlias(true);
            paint2.setColor(color);
            paint2.setStrokeWidth(this.displayedTrackThickness);
            int i = this.arcDirectionFactor;
            float startDegree = startFraction * 360.0f * ((float) i);
            float arcDegree = (endFraction >= startFraction ? endFraction - startFraction : (endFraction + 1.0f) - startFraction) * 360.0f * ((float) i);
            float f = this.adjustedRadius;
            canvas.drawArc(new RectF(-f, -f, f, f), startDegree, arcDegree, false, paint);
            if (this.displayedCornerRadius > 0.0f && Math.abs(arcDegree) < 360.0f) {
                paint2.setStyle(Paint.Style.FILL);
                float f2 = this.displayedCornerRadius;
                RectF cornerPatternRectBound = new RectF(-f2, -f2, f2, f2);
                drawRoundedEnd(canvas, paint, this.displayedTrackThickness, this.displayedCornerRadius, startDegree, true, cornerPatternRectBound);
                drawRoundedEnd(canvas, paint, this.displayedTrackThickness, this.displayedCornerRadius, startDegree + arcDegree, false, cornerPatternRectBound);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void fillTrack(Canvas canvas, Paint paint) {
        int trackColor = MaterialColors.compositeARGBWithAlpha(((CircularProgressIndicatorSpec) this.spec).trackColor, this.drawable.getAlpha());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(trackColor);
        paint.setStrokeWidth(this.displayedTrackThickness);
        float f = this.adjustedRadius;
        canvas.drawArc(new RectF(-f, -f, f, f), 0.0f, 360.0f, false, paint);
    }

    private int getSize() {
        return ((CircularProgressIndicatorSpec) this.spec).indicatorSize + (((CircularProgressIndicatorSpec) this.spec).indicatorInset * 2);
    }

    private void drawRoundedEnd(Canvas canvas, Paint paint, float trackSize, float cornerRadius, float positionInDeg, boolean isStartPosition, RectF cornerPatternRectBound) {
        Canvas canvas2 = canvas;
        float startOrEndFactor = isStartPosition ? -1.0f : 1.0f;
        canvas.save();
        canvas.rotate(positionInDeg);
        Paint paint2 = paint;
        canvas.drawRect((this.adjustedRadius - (trackSize / 2.0f)) + cornerRadius, Math.min(0.0f, startOrEndFactor * cornerRadius * ((float) this.arcDirectionFactor)), (this.adjustedRadius + (trackSize / 2.0f)) - cornerRadius, Math.max(0.0f, startOrEndFactor * cornerRadius * ((float) this.arcDirectionFactor)), paint2);
        canvas.translate((this.adjustedRadius - (trackSize / 2.0f)) + cornerRadius, 0.0f);
        RectF rectF = cornerPatternRectBound;
        canvas.drawArc(rectF, 180.0f, (-startOrEndFactor) * 90.0f * ((float) this.arcDirectionFactor), true, paint2);
        canvas.translate(trackSize - (cornerRadius * 2.0f), 0.0f);
        canvas.drawArc(rectF, 0.0f, 90.0f * startOrEndFactor * ((float) this.arcDirectionFactor), true, paint2);
        canvas.restore();
    }
}
