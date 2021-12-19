package sportfacilities.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EquipmentTest {

    private final transient Equipment hockeyStick;
    private final transient Sport hockey;
    private final transient Sport football;

    public EquipmentTest() {
        hockey = new Sport("hockey", true, 8, 12);
        football = new Sport("football", true, 6, 10);

        hockeyStick = new Equipment(6L, "hockey stick", hockey, true);
    }

    @Test
    void isInUse() {
        assertThat(hockeyStick.isInUse()).isTrue();
    }

    @Test
    void setInUse() {
        hockeyStick.setInUse(false);
        assertThat(hockeyStick.isInUse()).isFalse();
    }

    @Test
    void getEquipmentId() {
        assertThat(hockeyStick.getEquipmentId()).isEqualTo(6L);
    }

    @Test
    void setEquipmentId() {
        hockeyStick.setEquipmentId(4L);
        assertThat(hockeyStick.getEquipmentId()).isEqualTo(4L);
    }

    @Test
    void getName() {
        assertThat(hockeyStick.getName()).isEqualTo("hockey stick");
    }

    @Test
    void setName() {
        hockeyStick.setName("hockeyStick1");
        assertThat(hockeyStick.getName()).isEqualTo("hockeyStick1");
    }

    @Test
    void getRelatedSport() {
        assertThat(hockeyStick.getRelatedSport()).isEqualTo(hockey);
    }

    @Test
    void setRelatedSport() {
        hockeyStick.setRelatedSport(football);
        assertThat(hockeyStick.getRelatedSport()).isEqualTo(football);
    }
}