package com.plus.mevanspn.BBCSpriteEd.ui.panels.config;

public interface ConfigPanel {
    String GetTitle();
    String GetId();
    void SetUpComponents();
    void BackupCurrentValues();
    void RestoreOriginalValues();
}
