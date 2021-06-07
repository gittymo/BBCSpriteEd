package com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu;

import com.plus.mevanspn.BBCSpriteEd.image.convert.NoDither;
import com.plus.mevanspn.BBCSpriteEd.image.convert.OrderedDither;
import com.plus.mevanspn.BBCSpriteEd.image.convert.RandomDither;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
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
            if (mainFrame.GetSprite().GetFilePath() != null) filePicker.setCurrentDirectory(mainFrame.GetSprite().GetFilePath().getParentFile());
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
            if (mainFrame.GetSprite().GetFilePath() != null) filePicker.setSelectedFile(mainFrame.GetSprite().GetFilePath());
            int rv = filePicker.showSaveDialog(mainFrame);
            if (rv == JFileChooser.APPROVE_OPTION) {
                try {
                    String filePath = filePicker.getSelectedFile().getAbsolutePath();
                    DataOutputStream compressedFile = new DataOutputStream(new FileOutputStream(filePath + ".dat"));
                    compressedFile.write(mainFrame.GetSprite().GetCompressedData());
                    compressedFile.flush();
                    compressedFile.close();
                    if (!filePath.toLowerCase(Locale.ROOT).endsWith(".bsf")) filePath += ".bsf";
                    mainFrame.GetSprite().WriteToFile( filePath);
                    mainFrame.GetSprite().SetFilePath(new File(filePath));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.add(saveAsFileMenuItem);

        this.add(new JSeparator());
        JMenuItem importImageMenuItem = new JMenuItem(("Import image..."));
        importImageMenuItem.addActionListener(e-> {
            JFileChooser importFileChooser = new JFileChooser();
            importFileChooser.setFileFilter(new FileNameExtensionFilter("Image files","png","jpeg","jpg","bmp","pbm"));
            int rv = importFileChooser.showOpenDialog(mainFrame);
            if (rv == JFileChooser.APPROVE_OPTION) {
                mainFrame.LoadSprite(OrderedDither.GetConvertedSprite(importFileChooser.getSelectedFile().getAbsolutePath(), BBCSprite.DisplayMode.ModeTwo, mainFrame));
            }
        });
        this.add(importImageMenuItem);

        this.add(new JSeparator());
        JMenuItem optionsMenuItem = new JMenuItem("Options...");
        optionsMenuItem.addActionListener(e -> mainFrame.GetOptionsDialog().setVisible(true));
        this.add(optionsMenuItem);
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
