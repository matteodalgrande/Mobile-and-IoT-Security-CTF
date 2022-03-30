package com.google.android.material.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.C0089R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.timepicker.TimePickerView;
import java.util.LinkedHashSet;
import java.util.Set;

public final class MaterialTimePicker extends DialogFragment {
    public static final int INPUT_MODE_CLOCK = 0;
    static final String INPUT_MODE_EXTRA = "TIME_PICKER_INPUT_MODE";
    public static final int INPUT_MODE_KEYBOARD = 1;
    static final String TIME_MODEL_EXTRA = "TIME_PICKER_TIME_MODEL";
    static final String TITLE_RES_EXTRA = "TIME_PICKER_TITLE_RES";
    static final String TITLE_TEXT_EXTRA = "TIME_PICKER_TITLE_TEXT";
    private TimePickerPresenter activePresenter;
    private final Set<DialogInterface.OnCancelListener> cancelListeners = new LinkedHashSet();
    private int clockIcon;
    private final Set<DialogInterface.OnDismissListener> dismissListeners = new LinkedHashSet();
    /* access modifiers changed from: private */
    public int inputMode = 0;
    private int keyboardIcon;
    /* access modifiers changed from: private */
    public MaterialButton modeButton;
    /* access modifiers changed from: private */
    public final Set<View.OnClickListener> negativeButtonListeners = new LinkedHashSet();
    /* access modifiers changed from: private */
    public final Set<View.OnClickListener> positiveButtonListeners = new LinkedHashSet();
    private ViewStub textInputStub;
    private LinearLayout textInputView;
    private TimeModel time;
    private TimePickerClockPresenter timePickerClockPresenter;
    /* access modifiers changed from: private */
    public TimePickerTextInputPresenter timePickerTextInputPresenter;
    private TimePickerView timePickerView;
    private int titleResId = 0;
    private String titleText;

    /* access modifiers changed from: private */
    public static MaterialTimePicker newInstance(Builder options) {
        MaterialTimePicker fragment = new MaterialTimePicker();
        Bundle args = new Bundle();
        args.putParcelable(TIME_MODEL_EXTRA, options.time);
        args.putInt(INPUT_MODE_EXTRA, options.inputMode);
        args.putInt(TITLE_RES_EXTRA, options.titleTextResId);
        if (options.titleText != null) {
            args.putString(TITLE_TEXT_EXTRA, options.titleText.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    public int getMinute() {
        return this.time.minute;
    }

    public int getHour() {
        return this.time.hour % 24;
    }

    public int getInputMode() {
        return this.inputMode;
    }

    public final Dialog onCreateDialog(Bundle bundle) {
        TypedValue value = MaterialAttributes.resolve(requireContext(), C0089R.attr.materialTimePickerTheme);
        Dialog dialog = new Dialog(requireContext(), value == null ? 0 : value.data);
        Context context = dialog.getContext();
        int surfaceColor = MaterialAttributes.resolveOrThrow(context, C0089R.attr.colorSurface, MaterialTimePicker.class.getCanonicalName());
        MaterialShapeDrawable background = new MaterialShapeDrawable(context, (AttributeSet) null, C0089R.attr.materialTimePickerStyle, C0089R.style.Widget_MaterialComponents_TimePicker);
        TypedArray a = context.obtainStyledAttributes((AttributeSet) null, C0089R.styleable.MaterialTimePicker, C0089R.attr.materialTimePickerStyle, C0089R.style.Widget_MaterialComponents_TimePicker);
        this.clockIcon = a.getResourceId(C0089R.styleable.MaterialTimePicker_clockIcon, 0);
        this.keyboardIcon = a.getResourceId(C0089R.styleable.MaterialTimePicker_keyboardIcon, 0);
        a.recycle();
        background.initializeElevationOverlay(context);
        background.setFillColor(ColorStateList.valueOf(surfaceColor));
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(background);
        window.requestFeature(1);
        window.setLayout(-2, -2);
        return dialog;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        restoreState(bundle == null ? getArguments() : bundle);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(TIME_MODEL_EXTRA, this.time);
        bundle.putInt(INPUT_MODE_EXTRA, this.inputMode);
        bundle.putInt(TITLE_RES_EXTRA, this.titleResId);
        bundle.putString(TITLE_TEXT_EXTRA, this.titleText);
    }

    private void restoreState(Bundle bundle) {
        if (bundle != null) {
            TimeModel timeModel = (TimeModel) bundle.getParcelable(TIME_MODEL_EXTRA);
            this.time = timeModel;
            if (timeModel == null) {
                this.time = new TimeModel();
            }
            this.inputMode = bundle.getInt(INPUT_MODE_EXTRA, 0);
            this.titleResId = bundle.getInt(TITLE_RES_EXTRA, 0);
            this.titleText = bundle.getString(TITLE_TEXT_EXTRA);
        }
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ViewGroup root = (ViewGroup) layoutInflater.inflate(C0089R.layout.material_timepicker_dialog, viewGroup);
        TimePickerView timePickerView2 = (TimePickerView) root.findViewById(C0089R.C0092id.material_timepicker_view);
        this.timePickerView = timePickerView2;
        timePickerView2.setOnDoubleTapListener(new TimePickerView.OnDoubleTapListener() {
            public void onDoubleTap() {
                int unused = MaterialTimePicker.this.inputMode = 1;
                MaterialTimePicker materialTimePicker = MaterialTimePicker.this;
                materialTimePicker.updateInputMode(materialTimePicker.modeButton);
                MaterialTimePicker.this.timePickerTextInputPresenter.resetChecked();
            }
        });
        this.textInputStub = (ViewStub) root.findViewById(C0089R.C0092id.material_textinput_timepicker);
        this.modeButton = (MaterialButton) root.findViewById(C0089R.C0092id.material_timepicker_mode_button);
        TextView headerTitle = (TextView) root.findViewById(C0089R.C0092id.header_title);
        if (!TextUtils.isEmpty(this.titleText)) {
            headerTitle.setText(this.titleText);
        }
        int i = this.titleResId;
        if (i != 0) {
            headerTitle.setText(i);
        }
        updateInputMode(this.modeButton);
        ((Button) root.findViewById(C0089R.C0092id.material_timepicker_ok_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (View.OnClickListener listener : MaterialTimePicker.this.positiveButtonListeners) {
                    listener.onClick(v);
                }
                MaterialTimePicker.this.dismiss();
            }
        });
        ((Button) root.findViewById(C0089R.C0092id.material_timepicker_cancel_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (View.OnClickListener listener : MaterialTimePicker.this.negativeButtonListeners) {
                    listener.onClick(v);
                }
                MaterialTimePicker.this.dismiss();
            }
        });
        this.modeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MaterialTimePicker materialTimePicker = MaterialTimePicker.this;
                int unused = materialTimePicker.inputMode = materialTimePicker.inputMode == 0 ? 1 : 0;
                MaterialTimePicker materialTimePicker2 = MaterialTimePicker.this;
                materialTimePicker2.updateInputMode(materialTimePicker2.modeButton);
            }
        });
        return root;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        for (DialogInterface.OnCancelListener listener : this.cancelListeners) {
            listener.onCancel(dialogInterface);
        }
        super.onCancel(dialogInterface);
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        for (DialogInterface.OnDismissListener listener : this.dismissListeners) {
            listener.onDismiss(dialogInterface);
        }
        ViewGroup viewGroup = (ViewGroup) getView();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        super.onDismiss(dialogInterface);
    }

    /* access modifiers changed from: private */
    public void updateInputMode(MaterialButton modeButton2) {
        TimePickerPresenter timePickerPresenter = this.activePresenter;
        if (timePickerPresenter != null) {
            timePickerPresenter.hide();
        }
        TimePickerPresenter initializeOrRetrieveActivePresenterForMode = initializeOrRetrieveActivePresenterForMode(this.inputMode);
        this.activePresenter = initializeOrRetrieveActivePresenterForMode;
        initializeOrRetrieveActivePresenterForMode.show();
        this.activePresenter.invalidate();
        Pair<Integer, Integer> buttonData = dataForMode(this.inputMode);
        modeButton2.setIconResource(((Integer) buttonData.first).intValue());
        modeButton2.setContentDescription(getResources().getString(((Integer) buttonData.second).intValue()));
    }

    private TimePickerPresenter initializeOrRetrieveActivePresenterForMode(int mode) {
        if (mode == 0) {
            TimePickerClockPresenter timePickerClockPresenter2 = this.timePickerClockPresenter;
            if (timePickerClockPresenter2 == null) {
                timePickerClockPresenter2 = new TimePickerClockPresenter(this.timePickerView, this.time);
            }
            this.timePickerClockPresenter = timePickerClockPresenter2;
            return timePickerClockPresenter2;
        }
        if (this.timePickerTextInputPresenter == null) {
            this.textInputView = (LinearLayout) this.textInputStub.inflate();
            this.timePickerTextInputPresenter = new TimePickerTextInputPresenter(this.textInputView, this.time);
        }
        this.timePickerTextInputPresenter.clearCheck();
        return this.timePickerTextInputPresenter;
    }

    private Pair<Integer, Integer> dataForMode(int mode) {
        if (mode == 0) {
            return new Pair<>(Integer.valueOf(this.keyboardIcon), Integer.valueOf(C0089R.string.material_timepicker_text_input_mode_description));
        }
        if (mode == 1) {
            return new Pair<>(Integer.valueOf(this.clockIcon), Integer.valueOf(C0089R.string.material_timepicker_clock_mode_description));
        }
        throw new IllegalArgumentException("no icon for mode: " + mode);
    }

    /* access modifiers changed from: package-private */
    public TimePickerClockPresenter getTimePickerClockPresenter() {
        return this.timePickerClockPresenter;
    }

    public boolean addOnPositiveButtonClickListener(View.OnClickListener listener) {
        return this.positiveButtonListeners.add(listener);
    }

    public boolean removeOnPositiveButtonClickListener(View.OnClickListener listener) {
        return this.positiveButtonListeners.remove(listener);
    }

    public void clearOnPositiveButtonClickListeners() {
        this.positiveButtonListeners.clear();
    }

    public boolean addOnNegativeButtonClickListener(View.OnClickListener listener) {
        return this.negativeButtonListeners.add(listener);
    }

    public boolean removeOnNegativeButtonClickListener(View.OnClickListener listener) {
        return this.negativeButtonListeners.remove(listener);
    }

    public void clearOnNegativeButtonClickListeners() {
        this.negativeButtonListeners.clear();
    }

    public boolean addOnCancelListener(DialogInterface.OnCancelListener listener) {
        return this.cancelListeners.add(listener);
    }

    public boolean removeOnCancelListener(DialogInterface.OnCancelListener listener) {
        return this.cancelListeners.remove(listener);
    }

    public void clearOnCancelListeners() {
        this.cancelListeners.clear();
    }

    public boolean addOnDismissListener(DialogInterface.OnDismissListener listener) {
        return this.dismissListeners.add(listener);
    }

    public boolean removeOnDismissListener(DialogInterface.OnDismissListener listener) {
        return this.dismissListeners.remove(listener);
    }

    public void clearOnDismissListeners() {
        this.dismissListeners.clear();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int inputMode;
        /* access modifiers changed from: private */
        public TimeModel time = new TimeModel();
        /* access modifiers changed from: private */
        public CharSequence titleText;
        /* access modifiers changed from: private */
        public int titleTextResId = 0;

        public Builder setInputMode(int inputMode2) {
            this.inputMode = inputMode2;
            return this;
        }

        public Builder setHour(int hour) {
            this.time.setHourOfDay(hour);
            return this;
        }

        public Builder setMinute(int minute) {
            this.time.setMinute(minute);
            return this;
        }

        public Builder setTimeFormat(int format) {
            int hour = this.time.hour;
            int minute = this.time.minute;
            TimeModel timeModel = new TimeModel(format);
            this.time = timeModel;
            timeModel.setMinute(minute);
            this.time.setHourOfDay(hour);
            return this;
        }

        public Builder setTitleText(int titleTextResId2) {
            this.titleTextResId = titleTextResId2;
            return this;
        }

        public Builder setTitleText(CharSequence charSequence) {
            this.titleText = charSequence;
            return this;
        }

        public MaterialTimePicker build() {
            return MaterialTimePicker.newInstance(this);
        }
    }
}
