package com.plus.mevanspn.BBCSpriteEd.ui.panels.config;

import com.plus.mevanspn.BBCSpriteEd.ui.components.NumberSpinner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OnionSkinConfigPanel extends JPanel implements ConfigPanel {
    public OnionSkinConfigPanel() {
        super();
        setBorder(new EmptyBorder(4,4,4,4));
        setLayout(new GridLayout(1,5));

        JLabel enableOnionSkinningLabel = new JLabel("Enable Onion Skinning");
        enableOnionSkinningCheckbox = new JCheckBox();
        JLabel pastFramesCountLabel = new JLabel("Past Frames");
        pastFramesCountSpinner = new NumberSpinner(0, 0, 5, 1);
        JLabel futureFramesCountLabel = new JLabel("Future Frames");
        futureFramesCountSpinner = new NumberSpinner(0, 0, 5, 1);
        linkFrameCountsToggleButton = new JCheckBox();

        enableOnionSkinningCheckbox.addChangeListener(e -> {
            pastFramesCountSpinner.setEnabled(enableOnionSkinningCheckbox.isSelected());
            futureFramesCountSpinner.setEnabled(enableOnionSkinningCheckbox.isSelected());
            linkFrameCountsToggleButton.setEnabled(enableOnionSkinningCheckbox.isSelected());
        });

        pastFramesCountSpinner.addChangeListener(e -> {
            final int futureValue = (Integer) futureFramesCountSpinner.getValue();
            final int pastValue = (Integer) pastFramesCountSpinner.getValue();
            if (linkFrameCountsToggleButton.isSelected() && futureValue != pastValue) {
                futureFramesCountSpinner.setValue(pastFramesCountSpinner.getValue());
            }
        });

        futureFramesCountSpinner.addChangeListener(e -> {
            final int futureValue = (Integer) futureFramesCountSpinner.getValue();
            final int pastValue = (Integer) pastFramesCountSpinner.getValue();
            if (linkFrameCountsToggleButton.isSelected() && pastValue != futureValue) {
                pastFramesCountSpinner.setValue(futureFramesCountSpinner.getValue());
            }
        });

        linkFrameCountsToggleButton.addActionListener( e -> {
            final int futureValue = (Integer) futureFramesCountSpinner.getValue();
            final int pastValue = (Integer) pastFramesCountSpinner.getValue();
            if (linkFrameCountsToggleButton.isSelected() && futureValue != pastValue) {
                futureFramesCountSpinner.setValue(pastValue);
            }
        });
        linkFrameCountsToggleButton.setSelected(true);

        add(enableOnionSkinningLabel);
        add(enableOnionSkinningCheckbox);
        add(pastFramesCountLabel);
        add(pastFramesCountSpinner);
        add(linkFrameCountsToggleButton);
        add(futureFramesCountSpinner);
        add(futureFramesCountLabel);
    }

    public boolean OnionSkinningEnabled() {
        return enableOnionSkinningCheckbox.isSelected();
    }

    public int GetPastFramesCount() {
        return (Integer) pastFramesCountSpinner.getValue();
    }

    public int GetFutureFramesCount() {
        return (Integer) futureFramesCountSpinner.getValue();
    }

    public NumberSpinner GetPastFramesCountSpinner() {
        return pastFramesCountSpinner;
    }

    public NumberSpinner GetFutureFramesCountSpinner() {
        return futureFramesCountSpinner;
    }

    @Override
    public String GetTitle() {
        return "Onion Skinning";
    }

    @Override
    public String GetId() {
        return "onionskinning";
    }

    @Override
    public void BackupCurrentValues() {
        oldEnabledValue = enableOnionSkinningCheckbox.isSelected();
        oldPastValue = (Integer) pastFramesCountSpinner.getValue();
        oldFutureValue = (Integer) futureFramesCountSpinner.getValue();
    }

    @Override
    public void RestoreOriginalValues() {
        enableOnionSkinningCheckbox.setSelected(oldEnabledValue);
        pastFramesCountSpinner.setValue(oldPastValue);
        futureFramesCountSpinner.setValue(oldFutureValue);
    }

    @Override
    public void SetUpComponents() {
        RestoreOriginalValues();
    }

    private final JCheckBox enableOnionSkinningCheckbox, linkFrameCountsToggleButton;
    private final NumberSpinner pastFramesCountSpinner, futureFramesCountSpinner;
    private boolean oldEnabledValue = true;
    private int oldPastValue = 1, oldFutureValue = 1;
}
