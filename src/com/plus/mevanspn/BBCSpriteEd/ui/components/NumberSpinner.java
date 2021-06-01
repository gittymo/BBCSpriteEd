package com.plus.mevanspn.BBCSpriteEd.ui.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.DerivableComponent;

import javax.swing.*;

public class NumberSpinner extends JSpinner implements DerivableComponent {
    public NumberSpinner(int initialValue, int minValue, int maxValue, int stepSize) {
        super(new SpinnerNumberModel(initialValue, minValue, maxValue, stepSize));
    }

    public NumberSpinner CreateDerivedComponent() {
        final SpinnerNumberModel spinnerNumberModel = (SpinnerNumberModel) this.getModel();
        return new NumberSpinner((Integer) this.getValue(), (Integer) spinnerNumberModel.getMinimum(),
                (Integer) spinnerNumberModel.getMaximum(), (Integer) spinnerNumberModel.getStepSize());
    }
}
