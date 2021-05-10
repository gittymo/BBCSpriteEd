package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

public interface RenderJobProvider {
    public void AddListener(RenderJobRequester renderJobRequester);
    public void RemoveListener(RenderJobRequester renderJobRequester);
    public void RemoveAllListeners();
    public void NotifyAllListeners();
    public boolean IsDone();
    public void KillJob();
    public void StartJob();
    public boolean MustWait();
}
