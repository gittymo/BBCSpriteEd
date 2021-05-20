package com.plus.mevanspn.BBCSpriteEd.components;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.LinkedList;

public class NumberSpinner extends JSpinner {
    public NumberSpinner(int initialValue, int minValue, int maxValue, int stepSize) {
        super(new SpinnerNumberModel(initialValue, minValue, maxValue, stepSize));
    }

    public NumberSpinner CreateDerivedComponent() {
        final SpinnerNumberModel spinnerNumberModel = (SpinnerNumberModel)  this.getModel();
        NumberSpinner numberSpinner = new NumberSpinner((Integer) this.getValue(), (Integer) spinnerNumberModel.getMinimum(),
                (Integer) spinnerNumberModel.getMaximum(), (Integer) spinnerNumberModel.getStepSize());
        return numberSpinner;
    }
}
