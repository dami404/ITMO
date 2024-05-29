package levit104dami404.tpo.lab1.task3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Hand {
    private final List<Finger> fingers;

    public boolean hasDirtyFingers() {
        if (fingers == null || fingers.isEmpty())
            throw new IllegalStateException("Нет пальцев на руке!");
        if (fingers.stream().anyMatch(Objects::isNull))
            throw new IllegalStateException("Нет пальца!");
        return fingers.stream().anyMatch(Finger::isDirtyWithInk);
    }
}
