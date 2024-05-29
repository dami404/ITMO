package levit104dami404.tpo.lab1.task3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Person {
    private final String name;

    public void tryToLockDoor(Door door) {
        System.out.printf("%s пытается запереть дверь...\n", name);

        if (!door.isFit()) {
            System.out.printf("%s не запирает дверь. Дверь плохо подогнана!\n", name);
        } else {
            door.setLocked(true);
            System.out.printf("%s запирает дверь!\n", name);
        }
    }
}
