package com.plus.mevanspn.BBCSpriteEd.image;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.*;
import java.util.LinkedList;
import java.util.Stack;

final public class BBCSprite {
    public BBCSprite(int width, int height, DisplayMode displayMode, MainFrame parent) {
        this.width = width;
        this.height = height;
        this.displayMode = displayMode;
        this.parent = parent;
        this.activeFrame = null;
        this.frames = new LinkedList<>();
        this.colours = BBCColour.GetCopy(displayMode.colours);
        AddFrame();
        this.rollForwardHistory = new Stack<>();
        this.rollBackHistory = new Stack<>();
        this.ResetHistory();
    }

    public BBCSprite(String filename, MainFrame parent) throws InvalidSpriteFileException, InvalidDisplayModeException, IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filename));
        this.parent = parent;
        this.width = dataInputStream.readInt();
        this.height = dataInputStream.readInt();
        this.displayMode = DisplayMode.GetFromNumber(dataInputStream.readInt());
        this.colours = new BBCColour[dataInputStream.readInt()];
        for (int i = 0; i < this.colours.length; i++) {
            this.colours[i] = new BBCColour(dataInputStream.readInt());
        }
        int frameCount = dataInputStream.readInt();
        this.frames = new LinkedList<>();
        for (int i = 0; i < frameCount; i++) this.frames.add(new BBCSpriteFrame(this, dataInputStream));
        this.activeFrame = this.frames.getFirst();
        dataInputStream.close();
        this.rollForwardHistory = new Stack<>();
        this.rollBackHistory = new Stack<>();
        this.ResetHistory();
    }

    private BBCSprite(BBCSprite originalSprite) {
        this(originalSprite.width, originalSprite.height, originalSprite.displayMode, originalSprite.parent);
        this.colours = BBCColour.GetCopy(originalSprite.colours);
        this.frames.clear();
        for (BBCSpriteFrame frame : originalSprite.frames) {
            this.frames.add(new BBCSpriteFrame(frame));
        }
        this.activeFrame = this.GetFrame(originalSprite.GetFrameIndex(originalSprite.activeFrame));
        this.rollBackHistory = originalSprite.rollBackHistory;
        this.rollForwardHistory = originalSprite.rollForwardHistory;
    }

    public BBCSpriteFrame GetFrame(int frameIndex) {
        return frameIndex >= 0 && frameIndex < frames.size() ? frames.get(frameIndex) : null;
    }

    public int GetFrameIndex(BBCSpriteFrame bsf) {
        return frames.indexOf(bsf);
    }

    public BBCSpriteFrame GetActiveFrame() {
        return activeFrame;
    }

    public int GetFrameCount() {
        return frames.size();
    }

    public int GetCurrentFrameIndex() {
        return GetFrameIndex(activeFrame);
    }

    public BBCColour[] GetColours() {
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

    public void AddFrame() {
        BBCSpriteFrame bsf = new BBCSpriteFrame(this);
        frames.add(bsf);
        SetActiveFrame(bsf);
    }

    public void AddFrame(int atIndex) {
        if (atIndex >= 0 && atIndex < frames.size()) {
            BBCSpriteFrame bsf = new BBCSpriteFrame(this);
            frames.add(atIndex, bsf);
            SetActiveFrame(bsf);
        }
    }

    public void SetFrame(int frameIndex, BBCSpriteFrame bsf) {
        if (frameIndex >=0 && frameIndex < frames.size()) frames.set(frameIndex, bsf);
        SetActiveFrame(bsf);
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

    public void DuplicateFrame(BBCSpriteFrame bsf, boolean atEnd) {
        if (bsf != null) {
            final int frameIndex = frames.indexOf(bsf);
            if (frameIndex >= 0) {
                BBCSpriteFrame newBsf = new BBCSpriteFrame(this);
                BufferedImage newBSFImage = newBsf.GetRenderedImage();
                BufferedImage bsfImage = bsf.GetRenderedImage();
                newBSFImage.getGraphics().drawImage(bsfImage, 0, 0, null);
                if (atEnd) frames.add(newBsf);
                else frames.add(frameIndex + 1, newBsf);
                SetActiveFrame(newBsf);
            }
        }
    }

    public void SetActiveFrame(BBCSpriteFrame bsf) {
        if (activeFrame != bsf) {
            activeFrame = bsf;
            parent.RefreshPanels();
            parent.UpdateTimeline();
            if (parent.GetOnionSkinManager() != null) parent.GetOnionSkinManager().Update();
            if (parent.GetPreviewPanel() != null) parent.GetPreviewPanel().SetFrame(GetFrameIndex(bsf));
        }
    }

    public void DeleteFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < frames.size()) {
            final int activeFrameIndex = GetCurrentFrameIndex();
            BBCSpriteFrame newActiveFrame = frameIndex == activeFrameIndex ? frameIndex > 0 ?
                    GetFrame(frameIndex - 1) : frameIndex < frames.size() - 1 ? GetFrame(frameIndex + 1) : null : activeFrame;
            frames.remove(frameIndex);
            SetActiveFrame(newActiveFrame);
        }
    }

    public void Resize(int newWidth, int newHeight) {
        if (newWidth < 1 || newWidth > GetDisplayMode().width) return;
        if (newHeight < 1 || newHeight > GetDisplayMode().height) return;
        if (newWidth == width && newHeight == height) return;
        for (BBCSpriteFrame bbcSpriteFrame : frames) {
            BBCImage newRenderedImage = new BBCImage(newWidth, newHeight, bbcSpriteFrame.GetSprite());
            final int xPos = (newWidth - width) / 2;
            final int yPos = (newHeight - height) / 2;
            newRenderedImage.getGraphics().drawImage(bbcSpriteFrame.GetRenderedImage(), xPos, yPos, null);
            bbcSpriteFrame.SetRenderedImage(newRenderedImage);
        }
        width = newWidth;
        height = newHeight;
        parent.RefreshPanels();
    }

    public void WriteToFile(String filename) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filename));
        dataOutputStream.writeInt(width);
        dataOutputStream.writeInt(height);
        displayMode.WriteToStream(dataOutputStream);
        dataOutputStream.writeInt(colours.length);
        for (BBCColour colour : colours) {
            dataOutputStream.writeInt(colour.getRGB());
        }
        dataOutputStream.writeInt(frames.size());
        for (BBCSpriteFrame frame : frames) frame.WriteToStream(dataOutputStream);
        dataOutputStream.close();
    }

    public void UpdateColourModel() {
        for (BBCSpriteFrame frame : frames) {
            frame.UpdateColourModel();
        }
    }

    public IndexColorModel GetColourModel() {
        return BBCColour.GenerateIndexColourModel(colours);
    }

    public void RollBack() {
        if (rollBackHistory.size() > 0) {
            rollForwardHistory.push(new BBCSprite(this));
            this.setToSprite(rollBackHistory.pop());
            this.parent.RefreshPanels();
        }
    }

    public void RollForward() {
        if (rollForwardHistory.size() > 0) {
            rollBackHistory.push(new BBCSprite(this));
            this.setToSprite(rollForwardHistory.pop());
            this.parent.RefreshPanels();
        }
    }

    public void RecordHistory() {
        rollBackHistory.push(new BBCSprite(this));
        rollForwardHistory.clear();
    }

    public void ResetHistory() {
        rollForwardHistory.clear();
        rollBackHistory.clear();
    }

    private void setToSprite(BBCSprite otherSprite) {
        this.width = otherSprite.width;
        this.height = otherSprite.height;
        this.colours = BBCColour.GetCopy(otherSprite.colours);
        this.frames.clear();
        for (BBCSpriteFrame frame : otherSprite.frames) this.frames.add(new BBCSpriteFrame(frame));
        this.activeFrame = this.frames.get(otherSprite.GetFrameIndex(otherSprite.activeFrame));
    }

    private final LinkedList<BBCSpriteFrame> frames;
    private int width, height;
    private final DisplayMode displayMode;
    private BBCColour[] colours;
    private final MainFrame parent;
    private BBCSpriteFrame activeFrame;
    private Stack<BBCSprite> rollBackHistory, rollForwardHistory;

    public enum DisplayMode {
        ModeZero(0,0.5f, BBCColour.TWO_COLOUR_MODE_DEFAULT, 640, 256),
        ModeOne(1,1, BBCColour.FOUR_COLOUR_MODE_DEFAULT, 320, 256),
        ModeTwo(2,2, BBCColour.EIGHT_COLOUR_MODE_DEFAULT, 160, 260),
        ModeFour(4,1, BBCColour.TWO_COLOUR_MODE_DEFAULT, 320 ,256),
        ModeFive(5,2, BBCColour.FOUR_COLOUR_MODE_DEFAULT, 160, 256);

        DisplayMode(int number, float pixelRatio, BBCColour[] colours, int width, int height) {
            this.number = number;
            this.colours = colours;
            this.pixelRatio = pixelRatio;
            this.width = width;
            this.height = height;
        }

        public static int GetColourIndex(BBCColour colour) {
            int i = 0;
            while (i < allColours.length) {
                if (colour.getRGB() == allColours[i].getRGB()) break;
                i++;
            }
            return i < allColours.length ? i : -1;
        }

        public BBCColour GetNextColour(BBCColour colour) {
            int colourPos = GetColourIndex(colour);
            return colourPos >= 0 ? allColours[colourPos < allColours.length - 1 ? colourPos + 1 : 0] : null;
        }

        public BBCColour GetPreviousColour(BBCColour colour) {
            int colourPos = GetColourIndex(colour);
            return colourPos >= 0 ? allColours[colourPos > 0 ? colourPos - 1 : allColours.length - 1] : null;
        }

        public void WriteToStream(DataOutputStream dataOutputStream) throws IOException {
            if (dataOutputStream != null) {
                dataOutputStream.writeInt(number);
            }
        }

        public static DisplayMode GetFromNumber(int modeNumber) throws InvalidDisplayModeException {
            if (modeNumber < 0 || modeNumber > 5) throw new InvalidDisplayModeException(modeNumber);
            DisplayMode foundMode = null;
            for (DisplayMode displayMode : DisplayMode.values()) {
                if (displayMode.number == modeNumber) {
                    foundMode = displayMode;
                    break;
                }
            }
            if (foundMode == null) throw new InvalidDisplayModeException(modeNumber);
            return foundMode;
        }

        public final float pixelRatio;
        public final BBCColour[] colours;
        public final int width, height;
        public final int number;
        public static BBCColour[] allColours = BBCColour.PHYSICAL_COLOURS;
    }
}
