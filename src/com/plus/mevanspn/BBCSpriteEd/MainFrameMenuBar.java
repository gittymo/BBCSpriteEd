package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.JMenuBar;

final public class MainFrameMenuBar extends JMenuBar {
    public MainFrameMenuBar(MainFrame parent) {
        super();
        this.parent = parent;

        this.fileMenu = new FileMenu(this.parent);
        this.zoomMenu = new ZoomMenu(this.parent);
        this.animationMenu = new AnimationMenu(this.parent);

        this.add(this.fileMenu);
        this.add(this.zoomMenu);
        this.add(this.animationMenu);
    }

    AnimationMenu GetAnimationMenu() {
        return animationMenu;
    }

    private final MainFrame parent;
    private final FileMenu fileMenu;
    private final ZoomMenu zoomMenu;
    private final AnimationMenu animationMenu;
}
