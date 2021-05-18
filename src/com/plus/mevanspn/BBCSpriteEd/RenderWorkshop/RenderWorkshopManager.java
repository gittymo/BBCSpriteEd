/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author win10
 */
final public class RenderWorkshopManager extends AbstractRenderJobProvider {
    public RenderWorkshopManager(BufferedImage sourceImage) throws InvalidSourceImageException {
        this.rawImageSamples = new RawImageSamples(sourceImage);
        this.sourceImage = sourceImage;
    }

    @Override
    public void RenderWorkMethod() {
        while (!killed) {
            for (AbstractRenderJobProvider abstractRenderJobProvider : jobs) {
                if (!abstractRenderJobProvider.IsDone()) abstractRenderJobProvider.StartJob();
                while (!abstractRenderJobProvider.IsDone()) { }
            }
        }
    }

    public void AddJob(AbstractRenderJobProvider abstractRenderJobProvider) {
        if (abstractRenderJobProvider != null && !jobs.contains(abstractRenderJobProvider)) {
            if (abstractRenderJobProvider.MustWait()) {
                abstractRenderJobProvider.waitForJob = jobs.size() > 0 ? jobs.getLast() : null;
                jobs.add(abstractRenderJobProvider);
            }
        }
    }

    private LinkedList<AbstractRenderJobProvider> jobs;
    private final BufferedImage sourceImage;
    RawImageSamples rawImageSamples;
}
