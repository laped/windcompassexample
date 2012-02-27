package dk.laped.windcompass;

public enum WindDirectionType {
    North (1),
    NorthEast (2),
    East (4),
    SouthEast (8),
    South (16),
    SouthWest (32),
    West (64),
    NorthWest (128);
 
    private final int id;
 
    WindDirectionType(final int id) {
        this.id = id;
    }
 
    public int getId() {
        return id;
    }
 
    public static WindDirectionType fromId(final int id) {
        for (WindDirectionType e : WindDirectionType.values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}