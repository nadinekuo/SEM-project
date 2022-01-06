package sportfacilities.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class SportRoomTest {

    private final transient Sport soccer;
    private final transient Sport hockey;
    private final transient Sport volleyball;
    private final transient Sport tennis;
    private final transient Sport yoga;
    private final transient Sport zumba;
    private final transient Sport kickboxing;

    private final transient SportRoom hallX1;
    private final transient SportRoom hallX2;
    private final transient SportRoom hallX3;
    private final transient SportRoom hockeyField;


    public SportRoomTest() {
        soccer = new Sport("soccer", 6, 11);
        hockey = new Sport("hockey",  7, 14);
        volleyball = new Sport("volleyball", 4, 12);
        tennis = new Sport("tennis", 4, 13);
        yoga = new Sport("yoga");
        zumba = new Sport("zumba");
        kickboxing = new Sport("kickbox");

        hallX1 = new SportRoom("X1", List.of(soccer, hockey), 10, 50, true);
        hallX2 = new SportRoom("X2", List.of(hockey, volleyball, tennis, zumba), 15, 60, true);
        hallX3 = new SportRoom("X3", List.of(yoga, zumba, kickboxing), 1, 55, true);
        hockeyField = new SportRoom("hockeyfieldA", List.of(hockey), 10, 200, false);
    }

    @Test
    void getSportRoomId() {
        hallX1.setSportRoomId(1L);
        assertThat(hallX1.getSportRoomId()).isEqualTo(1L);
    }

    @Test
    void getIsSportsHall() {
        assertThat(hallX1.getIsSportsHall()).isTrue();
        assertThat(hockeyField.getIsSportsHall()).isFalse();
    }

    @Test
    void setSportRoomName() {
        hallX1.setSportRoomName("hallX5");
        assertThat(hallX1.getSportRoomName()).isEqualTo("hallX5");
    }

    @Test
    void setMinCapacity() {
        hallX3.setMinCapacity(8);
        assertThat(hallX3.getMinCapacity()).isEqualTo(8);
    }

    @Test
    void setMaxCapacity() {
        hallX3.setMaxCapacity(50);
        assertThat(hallX3.getMaxCapacity()).isEqualTo(50);
    }

//    @Test
//    void setRelatedSport() {
//    }
}