package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.util.LinkedList;

public abstract class AbstractRenderJobProvider extends Thread implements RenderJobProvider {
    @Override
    public void run() {
        if (waitForJob != null) {
            while (!waitForJob.IsDone() && !killed) {}
        }

        while (!renderDone && !killed) {
            RenderWorkMethod();
        }

        if (!killed) NotifyAllListeners();
    }

    public boolean IsDone() {
        return renderDone;
    }

    void MarkAsDone() {
        renderDone = true;
    }

    public void StartJob() {
        start();
    }

    public void KillJob() {
        killed = true;
    }

    public boolean MustWait() {
        return true;
    }

    public void AddListener(RenderJobRequester renderJobRequester) {
        if (!renderJobRequesters.contains(renderJobRequester)) renderJobRequesters.add(renderJobRequester);
    }

    public void RemoveListener(RenderJobRequester renderJobRequester) {
        renderJobRequesters.remove(renderJobRequester);
    }

    public void RemoveAllListeners() {
        renderJobRequesters.clear();
    }

    public void NotifyAllListeners() {
        for (RenderJobRequester renderJobRequester : renderJobRequesters) renderJobRequester.JobDone();
    }

    public abstract void RenderWorkMethod();

    boolean renderDone = false;
    boolean killed = false;
    LinkedList<RenderJobRequester> renderJobRequesters = new LinkedList<>();
    AbstractRenderJobProvider waitForJob = null;
}
