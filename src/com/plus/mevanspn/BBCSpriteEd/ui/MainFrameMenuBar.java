package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.JMenuBar;

final public class MainFrameMenuBar extends JMenuBar {
    public MainFrameMenuBar(MainFrame parent) {
        super();
        this.parent = parent;

        this.fileMenu = new FileMenu(this.parent);
        this.zoomMenu = new ZoomMenu(this.parent);
        this.animationMenu = new AnimationMenu(this.parent);
        this.editMenu = new EditMenu(this.parent);

        this.add(this.fileMenu);
        this.add(this.zoomMenu);
        this.add(this.animationMenu);
        this.add(this.editMenu);
    }

    public AnimationMenu GetAnimationMenu() {
        return animationMenu;
    }

    private final MainFrame parent;
    private final FileMenu fileMenu;
    private final ZoomMenu zoomMenu;
    private final EditMenu editMenu;

    private final AnimationMenu animationMenu;
}
