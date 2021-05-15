package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import com.plus.mevanspn.BBCSpriteEd.ui.menus.AnimationMenu;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.EditMenu;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu.FileMenu;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.ZoomMenu.ZoomMenu;

import javax.swing.JMenuBar;

final public class MainFrameMenuBar extends JMenuBar {
    public MainFrameMenuBar(MainFrame parent) {
        super();

        FileMenu fileMenu = new FileMenu(parent);
        ZoomMenu zoomMenu = new ZoomMenu(parent);
        this.animationMenu = new AnimationMenu(parent);
        EditMenu editMenu = new EditMenu(parent);

        this.add(fileMenu);
        this.add(zoomMenu);
        this.add(this.animationMenu);
        this.add(editMenu);
    }

    public AnimationMenu GetAnimationMenu() {
        return animationMenu;
    }

    private final AnimationMenu animationMenu;
}
