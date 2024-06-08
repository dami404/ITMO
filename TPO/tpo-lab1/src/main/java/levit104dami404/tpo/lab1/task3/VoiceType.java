package levit104dami404.tpo.lab1.task3;

public enum VoiceType {
    THIN("Тонкий"),
    ROUGH("Грубый");

    private final String description;

    VoiceType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
