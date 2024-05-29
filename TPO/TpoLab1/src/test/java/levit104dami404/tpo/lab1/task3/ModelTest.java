package levit104dami404.tpo.lab1.task3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    private static Person person;
    private static Door door;
    private static Voice voice;
    private static Creature creature;

    @BeforeAll
    static void prepare() {
        boolean doorFits = false; // главная переменная
        boolean dirtyHands = true;
        boolean furryCreature = true;

        VoiceType voiceType = VoiceType.THIN;
        List<Hand> hands = createHands(dirtyHands);

        person = new Person("Артур");
        door = new Door(doorFits);
        voice = new Voice(voiceType);
        creature = new Creature(furryCreature, hands, voice);
    }

    private static List<Finger> createFingers(boolean dirty) {
        List<Finger> fingers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fingers.add(new Finger(dirty));
        }
        return fingers;
    }

    private static List<Hand> createHands(boolean dirty) {
        List<Hand> hands = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            hands.add(new Hand(createFingers(dirty)));
        }
        return hands;
    }

    @Test
    @DisplayName("Дверь не заперта")
    void checkDoorLock() {
        person.tryToLockDoor(door);
        assertFalse(door.isLocked());
    }

    @Test
    @DisplayName("Существо протянуло руки и и безумно верещало")
    void checkCreature() {
        assertTrue(creature.putHandsThroughGapAndMakeSound(door, true));
    }

    @Test
    @DisplayName("Человека зовут Артур")
    void checkPersonName() {
        assertEquals(person.getName(), "Артур");
    }

    @Test
    @DisplayName("Дверь плохо подогнана")
    void checkDoorFit() {
        assertFalse(door.isFit());
    }

    @Test
    @DisplayName("Руки испачканы")
    void checkHands() {
        assertTrue(creature.hasDirtyHands());
    }

    @Test
    @DisplayName("Мохнатое существо")
    void checkFur() {
        assertTrue(creature.isFurry());
    }

    @Test
    @DisplayName("Тихий голосок")
    void checkVoice() {
        assertEquals(voice.getType(), VoiceType.THIN);
    }

    @Test
    @DisplayName("Существо без рук")
    void checkNoHands() {
        Creature creatureWithNoHands = new Creature(true, new ArrayList<>(), voice);
        assertThrows(IllegalStateException.class, creatureWithNoHands::hasDirtyHands);
    }

    @Test
    @DisplayName("Палец грязный")
    void checkFinger() {
        Finger finger = new Finger(true);
        assertTrue(finger.isDirtyWithInk());
    }

}
