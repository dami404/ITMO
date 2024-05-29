package levit104dami404.tpo.lab1.task3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Creature {
    private final boolean isFurry;
    private final List<Hand> hands;
    private final Voice voice;

    public boolean putHandsThroughGapAndMakeSound(Door door, boolean crazySound) {
        if (door.isLocked()) {
            System.out.println("Дверь заперта! Протянуть руки нельзя");
            return false;
        }

        System.out.println((isFurry ? "Мохнатые ручки" : "Ручки") + " просовывались в щель");
        System.out.println(hasDirtyHands() ? "Пальцы на них были перепачканы чернилами" : "Пальцы на них чистые");
        voice.makeSound(crazySound);
        return true;
    }

    public boolean hasDirtyHands() {
        if (hands == null || hands.isEmpty())
            throw new IllegalStateException("Нет рук!");
        if (hands.stream().anyMatch(Objects::isNull))
            throw new IllegalStateException("Нет руки!");
        return hands.stream().anyMatch(Hand::hasDirtyFingers);
    }
}
