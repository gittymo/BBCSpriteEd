package com.plus.mevanspn.BBCSpriteEd.ui.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.DerivableComponent;

import javax.swing.*;

/**
 * NumberSpinner objects are descendants of the JSpinner class which have a SpinnerNumberModel already associated to
 * them.
 */
public class NumberSpinner extends JSpinner implements DerivableComponent {
    /**
     * Creates an instance of NumberSpinner with the given initial, minimum, maximum and step size values.
     * @param initialValue Initial value of the component.
     * @param minValue Minimum value of the component.
     * @param maxValue Maximum value of the component.
     * @param stepSize Amount the value will change (increment or decrement) when the up/down arrows are used.
     */
    public NumberSpinner(int initialValue, int minValue, int maxValue, int stepSize) {
        super(new SpinnerNumberModel(initialValue, minValue, maxValue, stepSize));
    }

    /**
     * Creates an exact copy of the NumberSpinner object, but whose values are independant of the original.
     * @return Reference to the copy of the NumberSpinner object.
     */
    public NumberSpinner CreateDerivedComponent() {
        final SpinnerNumberModel spinnerNumberModel = (SpinnerNumberModel) this.getModel();
        return new NumberSpinner((Integer) this.getValue(), (Integer) spinnerNumberModel.getMinimum(),
                (Integer) spinnerNumberModel.getMaximum(), (Integer) spinnerNumberModel.getStepSize());
    }
}
