package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.util.LinkedList;

final public class BBCSprite {
    public BBCSprite(int width, int height, DisplayMode displayMode, MainFrame parent) {
        this.width = width;
        this.height = height;
        this.displayMode = displayMode;
        this.parent = parent;
        this.activeFrame = null;
        this.frames = new LinkedList<>();
        this.colours = new Color[displayMode.colours.length];
        for (int i = 0; i < this.colours.length; i++) {
            this.colours[i] = displayMode.colours[i];
        }
        AddFrame();
    }

    public BBCSpriteFrame AddFrame() {
        BBCSpriteFrame bsf = new BBCSpriteFrame(this);
        frames.add(bsf);
        SetActiveFrame(bsf);
        return bsf;
    }

    public BBCSpriteFrame AddFrame(int atIndex) {
        if (atIndex >= 0 && atIndex < frames.size()) {
            BBCSpriteFrame bsf = new BBCSpriteFrame(this);
            frames.add(atIndex, bsf);
            SetActiveFrame(bsf);
            return bsf;
        } else return null;
    }

    public BBCSpriteFrame GetFrame(int frameIndex) {
        return frameIndex >= 0 && frameIndex < frames.size() ? frames.get(frameIndex) : null;
    }

    public int GetFrameIndex(BBCSpriteFrame bsf) {
        return frames.indexOf(bsf);
    }

    public int GetFrameCount() {
        return frames.size();
    }

    public int GetCurrentFrameIndex() {
        return GetFrameIndex(activeFrame);
    }

    public void SetFrame(int frameIndex, BBCSpriteFrame bsf) {
        if (frameIndex >=0 && frameIndex < frames.size()) frames.set(frameIndex, bsf);
    }

    public void SwapFrames(int frameOneIndex, int frameTwoIndex) {
        if (frameOneIndex >= 0 && frameTwoIndex >=0 && frameOneIndex < frames.size() && frameTwoIndex < frames.size() &&
            frameOneIndex != frameTwoIndex) {
            BBCSpriteFrame tempFrame = frames.get(frameOneIndex);
            frames.set(frameOneIndex, frames.get(frameTwoIndex));
            frames.set(frameTwoIndex, tempFrame);
        }
    }

    public void MoveFrame(int currentIndex, int newIndex) {
        if (currentIndex != newIndex && currentIndex >= 0 && newIndex >=0 && currentIndex < frames.size() && newIndex < frames.size()) {
            frames.add(newIndex, frames.get(currentIndex));
            frames.remove(currentIndex);
        }
    }

    public void DuplicateFrame(BBCSpriteFrame bsf) {
        if (bsf != null) {
            final int frameIndex = frames.indexOf(bsf);
            if (frameIndex >= 0) {
                BBCSpriteFrame newBsf = new BBCSpriteFrame(this);
                final byte[] sourceData = bsf.GetData();
                byte[] destData = newBsf.GetData();
                for (int i = 0; i < sourceData.length; i++) destData[i] = sourceData[i];
                frames.add(frameIndex + 1, newBsf);
            }
        }
    }

    public void SetActiveFrame(BBCSpriteFrame bsf) {
        if (activeFrame != bsf) {
            activeFrame = bsf;
            parent.RefreshImagePane();
        }
    }

    public BBCSpriteFrame GetActiveFrame() {
        return activeFrame;
    }

    public void DeleteFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < frames.size()) frames.remove(frameIndex);
    }

    public MainFrame GetParent() {
        return parent;
    }

    public Color[] GetColours() {
        return colours;
    }

    public int GetWidth() {
        return width;
    }

    public int GetHeight() {
        return height;
    }

    public float GetHorizontalPixelRatio() {
        return displayMode.pixelRatio;
    }

    public DisplayMode GetDisplayMode() {
        return displayMode;
    }

    public void SetColourAt(int index, Color newColour) {
        if (index >= 0 && index < colours.length) colours[index] = newColour;
    }

    private LinkedList<BBCSpriteFrame> frames;
    private int width, height;
    private DisplayMode displayMode;
    private Color[] colours;
    private MainFrame parent;
    private BBCSpriteFrame activeFrame;

    enum DisplayMode {
        ModeZero(0,0.5f, new Color[] { Color.BLACK, Color.WHITE}, 640, 256),
        ModeOne(1,1, new Color[] { Color.BLACK, Color.RED, Color.YELLOW, Color.WHITE}, 320, 256),
        ModeTwo(2,2, new Color[] { Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE,
                Color.MAGENTA, Color.CYAN, Color.WHITE }, 160, 256),
        ModeFour(4,1, new Color[] { Color.BLACK, Color.WHITE}, 320 ,256),
        ModeFive(5,2, new Color[] { Color.BLACK, Color.RED, Color.YELLOW, Color.WHITE}, 160, 256);

        DisplayMode(int number, float pixelRatio, Color[] colours, int width, int height) {
            this.number = number;
            this.colours = colours;
            this.pixelRatio = pixelRatio;
            this.width = width;
            this.height = height;
        }

        public Color[] GetColours() {
            return colours;
        }

        private int findColourIndex(Color colour) {
            int i = 0;
            boolean found = false;
            while (i < allColours.length && !found) {
                if (colour.getRGB() == allColours[i].getRGB()) {
                    found = true;
                    break;
                }
                i++;
            }
            return found ? i : -1;
        }

        public Color GetNextColour(Color colour) {
            int colourPos = findColourIndex(colour);
            return colourPos >= 0 ? allColours[colourPos < allColours.length - 1 ? colourPos + 1 : 0] : null;
        }

        public Color GetPreviousColour(Color colour) {
            int colourPos = findColourIndex(colour);
            return colourPos >= 0 ? allColours[colourPos > 0 ? colourPos - 1 : allColours.length - 1] : null;
        }

        public int GetWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getNumber() {
            return number;
        }

        public float GetPixelAspectRatio() {
            return pixelRatio;
        }

        final float pixelRatio;
        final Color[] colours;
        final int width, height;
        final int number;
        public static Color[] allColours = new Color[] { Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE,
                Color.MAGENTA, Color.CYAN, Color.WHITE };
    }
}
