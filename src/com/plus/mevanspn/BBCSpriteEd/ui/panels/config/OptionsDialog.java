package com.plus.mevanspn.BBCSpriteEd.ui.panels.config;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class OptionsDialog extends JDialog implements ComponentListener {
    public OptionsDialog(MainFrame mainFrame) {
        super(mainFrame, "BBC Sprite Editor Options", true);
        this.mainFrame = mainFrame;
        this.addComponentListener(this);
        this.setLayout(new BorderLayout());
        this.optionsTabManager = new OptionsTabManager();

        JButton buttonOK = new JButton("OK");
        buttonOK.addActionListener(e -> setVisible(false));
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(e -> {
            cancelled = true;
            setVisible(false);
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(0,4,4,4));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(buttonOK);
        buttonsPanel.add(buttonCancel);

        this.add(this.optionsTabManager, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.SOUTH);
        this.pack();
    }

    public void AddConfigPanel(ConfigPanel configPanel) {
        if (optionsTabManager != null) optionsTabManager.AddConfigPanel(configPanel);
    }

    public ConfigPanel GetConfigPanel(String id) {
        return optionsTabManager != null ? optionsTabManager.GetConfigPanel(id) : null;
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        optionsTabManager.BackupValues();
        cancelled = false;
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        if (cancelled) optionsTabManager.RestoreValues();
        else mainFrame.RefreshPanels();
    }

    private final OptionsTabManager optionsTabManager;
    private boolean cancelled = false;
    private final MainFrame mainFrame;

    final class OptionsTabManager extends JTabbedPane {
        OptionsTabManager() {
            super();
            this.setBorder(new EmptyBorder(4,4,4,4));
            this.setPreferredSize(new Dimension(320,240));
            configPanels = new HashMap<>();
        }

        void AddConfigPanel(ConfigPanel configPanel) {
            if (configPanel != null && !configPanels.containsKey(configPanel.GetId())) {
                configPanels.put(configPanel.GetId(), configPanel);
                configPanel.SetUpComponents();
                addTab(configPanel.GetTitle(), (JPanel) configPanel);
            }
        }

        ConfigPanel GetConfigPanel(String id) {
            return configPanels != null && configPanels.containsKey(id) ? configPanels.get(id) : null;
        }

        void BackupValues() {
            for (Map.Entry<String, ConfigPanel> configPanelEntry : configPanels.entrySet()) {
                configPanelEntry.getValue().BackupCurrentValues();
            }
        }

        void RestoreValues() {
            for (Map.Entry<String, ConfigPanel> configPanelEntry : configPanels.entrySet()) {
                configPanelEntry.getValue().RestoreOriginalValues();
            }
        }
        private final HashMap<String, ConfigPanel> configPanels;
    }
}
