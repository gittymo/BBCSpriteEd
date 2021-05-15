package com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;
import java.util.Locale;

final public class FileMenu extends JMenu {
    public FileMenu(MainFrame mainFrame) {
        super("File");
        this.mainFrame = mainFrame;
        NewFileMenu newFileMenu = new NewFileMenu(this);
        this.add(newFileMenu);
        JMenuItem openFileMenuItem = new JMenuItem("Open...");
        openFileMenuItem.addActionListener(e -> {
            BBCSpriteFilePickerDialog filePicker = new BBCSpriteFilePickerDialog();
            int rv = filePicker.showOpenDialog(mainFrame);
            if (rv == JFileChooser.APPROVE_OPTION) {
                try {
                    String filePath = filePicker.getSelectedFile().getAbsolutePath();
                    mainFrame.LoadSprite(new BBCSprite(filePath, mainFrame));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.add(openFileMenuItem);
        JMenuItem saveAsFileMenuItem = new JMenuItem("Save As...");
        saveAsFileMenuItem.addActionListener(e -> {
            BBCSpriteFilePickerDialog filePicker = new BBCSpriteFilePickerDialog();
            int rv = filePicker.showSaveDialog(mainFrame);
            if (rv == JFileChooser.APPROVE_OPTION) {
                try {
                    String filePath = filePicker.getSelectedFile().getAbsolutePath();
                    if (!filePath.toLowerCase(Locale.ROOT).endsWith(".bsf")) filePath += ".bsf";
                    mainFrame.GetSprite().WriteToFile( filePath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.add(saveAsFileMenuItem);
        this.add(new JSeparator());
        JMenuItem exitApp = new JMenuItem("Quit");
        exitApp.addActionListener(e -> mainFrame.Quit());
        this.add(exitApp);
    }

    MainFrame GetMainFrame() {
        return mainFrame;
    }

    private final MainFrame mainFrame;


}
