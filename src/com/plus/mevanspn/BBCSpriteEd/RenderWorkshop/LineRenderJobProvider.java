package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.awt.Point;

final public class LineRenderJobProvider extends AbstractRenderJobProvider {
    public LineRenderJobProvider(RenderWorkshopManager renderWorkshopManager, Point pointA, Point pointB, RawSampleColour colour) throws RenderJobConfigurationException {
        if (renderWorkshopManager != null && pointA != null && pointB != null) {
            this.renderWorkshopManager = renderWorkshopManager;
            int x = pointA.x < pointB.x ? pointA.x : pointB.x;
            int y = pointA.y < pointB.y ? pointA.y : pointB.y;
            int width = pointA.x < pointB.x ? pointB.x - pointA.x : pointA.x - pointB.x;
            int height = pointA.y < pointB.y ? pointB.y - pointA.y : pointA.y - pointB.y;
            this.startX = (pointA.y < pointB.y ? pointA.x : pointB.x) - x;
            this.horizontalGradient = (pointA.y > pointB.y ? pointA.x - pointB.x : pointB.x - pointA.x) / (float) height;
            this.samples = renderWorkshopManager.rawImageSamples.GetSampleArea(x, y, width, height);
            this.colour = colour;
        } else throw new RenderJobConfigurationException();
    }

    @Override
    public void RenderWorkMethod() {
        if (samples == null) MarkAsDone();
        if (!IsDone()) {

        }
    }

    private final RenderWorkshopManager renderWorkshopManager;
    private int[] samples = null;
    private final int startX;
    private final float horizontalGradient;
    private final RawSampleColour colour;
}
