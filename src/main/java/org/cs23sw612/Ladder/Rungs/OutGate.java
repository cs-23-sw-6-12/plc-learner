package org.cs23sw612.Ladder.Rungs;

import java.util.Optional;

public record OutGate(String label, Optional<String> actualState) {

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}
