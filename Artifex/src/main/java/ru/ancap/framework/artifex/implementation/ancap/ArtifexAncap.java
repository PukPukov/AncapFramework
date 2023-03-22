package ru.ancap.framework.artifex.implementation.ancap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.artifex.implementation.plugin.ServerTPSCounter;
import ru.ancap.framework.plugin.api.Ancap;

@RequiredArgsConstructor @Getter
@ToString @EqualsAndHashCode
public class ArtifexAncap implements Ancap {
    
    private final ServerTPSCounter tpsCounter;

    @Override
    public double getServerTPS() {
        return this.tpsCounter.get();
    }

}
