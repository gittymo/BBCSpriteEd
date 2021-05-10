package com.plus.mevanspn.BBCSpriteEd.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

final public class BBCSpriteFilePicker extends JFileChooser {
    public BBCSpriteFilePicker() {
        super();
        setFileFilter(new FileNameExtensionFilter("BBC Sprite Files", "bsf"));
    }
}
