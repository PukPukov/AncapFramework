package ru.ancap.framework.artifex.implementation.ancap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ancap.framework.artifex.implementation.plugin.ServerTPSCounter;
import ru.ancap.framework.plugin.api.Ancap;

@RequiredArgsConstructor
@Getter
public class ArtifexAncap implements Ancap {
    
    private final ServerTPSCounter tpsCounter;

    @Override
    public double getServerTPS() {
        return this.tpsCounter.get();
    }

}
