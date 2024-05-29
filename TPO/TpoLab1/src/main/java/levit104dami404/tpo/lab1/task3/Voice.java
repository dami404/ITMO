package levit104dami404.tpo.lab1.task3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Voice {
    private final VoiceType type;

    public void makeSound(boolean crazy) {
        System.out.println(type + " голос " + (crazy ? "безумно верещал" : "спокойно шептал"));
    }
}
