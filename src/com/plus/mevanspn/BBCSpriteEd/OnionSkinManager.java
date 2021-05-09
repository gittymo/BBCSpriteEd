package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

final public class OnionSkinManager extends Thread {
    public OnionSkinManager(MainFrame parent) {
        this.parent = parent;
        this.frameOffset = -1;
        this.onionSkinFrame = 0;
        this.enabled = true;
        this.maxFrameOffset = 5;
        this.maxWaitTime = 15;
        this.onionSkinManagerToolbar = new OnionSkinManagerToolbar(this);
    }

    public BufferedImage GetOnionSkin() {
        if (enabled && parent.GetSprite() != null) {
            final float zoom = parent.GetZoom();
            final BBCSpriteFrame onionSkinSourceFrame = parent.GetSprite().GetFrame(onionSkinFrame);
            if (onionSkinSourceFrame != null) {
                final int onionSkinImageWidth = (int) (onionSkinSourceFrame.GetWidth() * zoom);
                final int onionSkinImageHeight = (int) (onionSkinSourceFrame.GetHeight() * zoom);

                BufferedImage onionSkinImage = new BufferedImage(onionSkinImageWidth, onionSkinImageHeight, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2 = (Graphics2D) onionSkinImage.getGraphics();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2.drawImage(onionSkinSourceFrame.GetRenderedImage(), 0, 0, onionSkinImageWidth, onionSkinImageHeight, null);

                if (zoom > 4) {
                    byte[] samples = new byte[4 * onionSkinImageWidth * onionSkinImageHeight];
                    onionSkinImage.getRaster().getDataElements(0, 0, onionSkinImageWidth, onionSkinImageHeight, samples);
                    final int iZoom = (int) zoom;
                    final int halfIZoom = Math.max((iZoom / 2), 1);
                    final int rowStride = onionSkinImageWidth * 4;
                    int offsetInc = 0, zoomCount = 0;
                    for (int offset = 0; offset < samples.length; offset += 4) {
                        if (offset % rowStride == 0) {
                            offset += (4 * offsetInc);
                            offsetInc = (offsetInc + 1) % halfIZoom;
                            zoomCount = 0;
                        }
                        if (zoomCount < halfIZoom) samples[offset + 3] = 0;
                        zoomCount = (zoomCount + 1) % iZoom;
                    }
                    onionSkinImage.getRaster().setDataElements(0, 0, onionSkinImageWidth, onionSkinImageHeight, samples);
                }
                return onionSkinImage;
            } return null;
        } else return null;
    }

    public boolean IsEnabled() {
        return enabled;
    }

    public void SetEnabled(boolean enabled) {
        this.enabled = enabled;
        parent.RefreshPanels();
    }

    public void SetFrameOffset(int frameOffset) {
        if (enabled && parent.GetSprite() != null) {
            this.onionSkinFrame = parent.GetSprite().GetCurrentFrameIndex() + frameOffset;
            this.frameOffset = frameOffset > 0 ? 1 : frameOffset < 0 ? -1 : 0;
            this.wait = this.maxWaitTime;
            if (this.onionSkinFrame < 0) this.onionSkinFrame = 0;
            if (this.onionSkinFrame >= parent.GetSprite().GetFrameCount())
                this.onionSkinFrame = parent.GetSprite().GetFrameCount() - 1;
            parent.RefreshPanels();
        }
    }

    public int GetMinimumAllowedFrameOffset() {
        if (enabled && parent.GetSprite() != null) {
            int currentLessMaxOffset = parent.GetSprite().GetCurrentFrameIndex() - maxFrameOffset;
            if (currentLessMaxOffset < 0) return -(maxFrameOffset + currentLessMaxOffset);
            else return -maxFrameOffset;
        } else return 0;
    }

    public int GetMaximumAllowedFrameOffset() {
        if (enabled && parent.GetSprite() != null) {
            int currentPlusMaxOffset = parent.GetSprite().GetCurrentFrameIndex() + maxFrameOffset;
            if (currentPlusMaxOffset > parent.GetSprite().GetFrameCount())
                return (parent.GetSprite().GetFrameCount() - parent.GetSprite().GetCurrentFrameIndex()) - 1;
            else return maxFrameOffset;
        } else return 0;
    }

    public void SetMaxWaitTime(int givenWaitTime) {
        if (enabled && parent.GetSprite() != null && givenWaitTime >= 0) {
            maxWaitTime = givenWaitTime;
            wait = maxWaitTime;
        }
    }

    public void UserRollForward() {
        if (enabled && parent.GetSprite() != null) {
            if (onionSkinFrame < parent.GetSprite().GetFrameCount() - 1) {
                frameOffset = 1;
                onionSkinFrame += maxFrameOffset > 0 ?
                        onionSkinFrame - parent.GetSprite().GetCurrentFrameIndex() < maxFrameOffset ?
                                frameOffset : 0 : frameOffset;
                wait = maxWaitTime;
                parent.RefreshPanels();
            }
        }
    }

    public void UserRollBack() {
        if (enabled && parent.GetSprite() != null) {
            if (onionSkinFrame > 0) {
                frameOffset = -1;
                onionSkinFrame += maxFrameOffset > 0 ?
                        onionSkinFrame - parent.GetSprite().GetCurrentFrameIndex() > -maxFrameOffset ?
                                frameOffset : 0 : frameOffset;
                wait = maxWaitTime;
                parent.RefreshPanels();
            }
        }
    }

    public void Update() {
        if (enabled && parent.GetSprite() != null) {
            onionSkinFrame = parent.GetSprite().GetCurrentFrameIndex();
            onionSkinManagerToolbar.UpdateControls();
            parent.RefreshPanels();
        }
    }

    public void Animate() {
        if (enabled && parent.GetSprite() != null && frameOffset != 0) {
            if (maxWaitTime > 0) {
                if (wait > 0) {
                    System.out.println("Animating");
                    if (onionSkinFrame > parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame--;
                    else if (onionSkinFrame < parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame++;
                    parent.RefreshPanels();
                    onionSkinManagerToolbar.UpdateControls();
                } else wait--;
            }
        }
    }

    public void Quit() {
        quit = true;
    }

    @Override
    public void run() {
        quit = false;
        while (!quit) {
            try {
                Animate();
                sleep(100);
            } catch (Exception ex) {}
        }
    }

    public OnionSkinManagerToolbar GetToolbar() {
        return onionSkinManagerToolbar;
    }

    private final MainFrame parent;
    private int frameOffset, onionSkinFrame, wait = 0;
    private boolean enabled;
    private int maxFrameOffset;
    private int maxWaitTime;
    private boolean quit;
    private OnionSkinManagerToolbar onionSkinManagerToolbar;

    final class OnionSkinManagerConfigTab extends JPanel implements ConfigPanel {
        public OnionSkinManagerConfigTab(OnionSkinManager onionSkinManager) {
            super();
        }

        public String GetTitle() { return "Onion Skinning"; }

        private OnionSkinManager onionSkinManager;
    }

    final class OnionSkinManagerToolbar extends JToolBar {
        public OnionSkinManagerToolbar(OnionSkinManager onionSkinManager) {
            super(JToolBar.HORIZONTAL);
            this.onionSkinManager = onionSkinManager;
            this.animationWaitTime = onionSkinManager.maxWaitTime;
            this.onionSkinSlider = new JSlider(JSlider.HORIZONTAL);
            this.onionSkinSlider.setAlignmentX(JSlider.CENTER_ALIGNMENT);
            this.onionSkinSlider.setMinorTickSpacing(1);
            this.onionSkinSlider.setPaintTicks(true);
            this.onionSkinSlider.setPaintLabels(true);
            this.onionSkinSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    onionSkinSlider.setToolTipText("Set onion skinning offset (current " + onionSkinSlider.getValue() + " frames)");
                    onionSkinManager.SetFrameOffset(onionSkinSlider.getValue());
                }
            });
            this.onionSkinToggle = new JCheckBox();
            this.onionSkinToggle.setToolTipText("Toggle onion skinning on/off");
            this.onionSkinToggle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onionSkinManager.SetEnabled(onionSkinToggle.isSelected());
                    onionSkinSlider.setEnabled(onionSkinToggle.isSelected());
                    onionSkinAnimateToggle.setEnabled(onionSkinToggle.isEnabled());
                }
            });
            this.onionSkinAnimateToggle = new JCheckBox();
            this.onionSkinAnimateToggle.setToolTipText("Enable onion skin rollback.");
            this.onionSkinAnimateToggle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onionSkinManager.SetMaxWaitTime(onionSkinAnimateToggle.isSelected() ? animationWaitTime : 0);
                }
            });
            this.add(onionSkinSlider);
            this.add(onionSkinToggle);
            this.add(onionSkinAnimateToggle);
            UpdateControls();
        }

        public void UpdateControls() {
            this.onionSkinToggle.setSelected(onionSkinManager.IsEnabled());
            this.onionSkinSlider.setMinimum(onionSkinManager.GetMinimumAllowedFrameOffset());
            this.onionSkinSlider.setMaximum(onionSkinManager.GetMaximumAllowedFrameOffset());
            this.onionSkinSlider.setValue(onionSkinManager.frameOffset);
            this.onionSkinAnimateToggle.setSelected(onionSkinManager.maxWaitTime > 0);
        }

        private OnionSkinManager onionSkinManager;
        private JSlider onionSkinSlider;
        private JCheckBox onionSkinToggle;
        private JCheckBox onionSkinAnimateToggle;
        private int animationWaitTime;
    }
}
