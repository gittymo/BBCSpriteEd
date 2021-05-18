package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

public interface RenderJobProvider {
    void AddListener(RenderJobRequester renderJobRequester);
    void RemoveListener(RenderJobRequester renderJobRequester);
    void RemoveAllListeners();
    void NotifyAllListeners();
    boolean IsDone();
    void KillJob();
    void StartJob();
    boolean MustWait();
}
