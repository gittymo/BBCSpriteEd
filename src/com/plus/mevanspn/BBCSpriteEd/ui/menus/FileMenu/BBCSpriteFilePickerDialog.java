package com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

final public class BBCSpriteFilePickerDialog extends JFileChooser {
    public BBCSpriteFilePickerDialog() {
        super();
        setFileFilter(new FileNameExtensionFilter("BBC Sprite Files", "bsf"));
    }
}
