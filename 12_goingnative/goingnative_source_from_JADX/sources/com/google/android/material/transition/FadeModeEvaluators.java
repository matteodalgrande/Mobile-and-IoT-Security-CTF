package com.google.android.material.transition;

class FadeModeEvaluators {
    private static final FadeModeEvaluator CROSS = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction) {
            return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, fadeStartFraction, fadeEndFraction, progress), TransitionUtils.lerp(0, 255, fadeStartFraction, fadeEndFraction, progress));
        }
    };

    /* renamed from: IN */
    private static final FadeModeEvaluator f117IN = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction) {
            return FadeModeResult.endOnTop(255, TransitionUtils.lerp(0, 255, fadeStartFraction, fadeEndFraction, progress));
        }
    };
    private static final FadeModeEvaluator OUT = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction) {
            return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, fadeStartFraction, fadeEndFraction, progress), 255);
        }
    };
    private static final FadeModeEvaluator THROUGH = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float progress, float fadeStartFraction, float fadeEndFraction) {
            float fadeFractionThreshold = (0.35f * (fadeEndFraction - fadeStartFraction)) + fadeStartFraction;
            return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, fadeStartFraction, fadeFractionThreshold, progress), TransitionUtils.lerp(0, 255, fadeFractionThreshold, fadeEndFraction, progress));
        }
    };

    static FadeModeEvaluator get(int fadeMode, boolean entering) {
        if (fadeMode == 0) {
            return entering ? f117IN : OUT;
        }
        if (fadeMode == 1) {
            return entering ? OUT : f117IN;
        }
        if (fadeMode == 2) {
            return CROSS;
        }
        if (fadeMode == 3) {
            return THROUGH;
        }
        throw new IllegalArgumentException("Invalid fade mode: " + fadeMode);
    }

    private FadeModeEvaluators() {
    }
}
