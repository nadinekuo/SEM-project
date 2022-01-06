package sportfacilities.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EquipmentTest {

    private final transient Equipment hockeyStick;
    private final transient Sport hockey;
    private final transient Sport football;

    public EquipmentTest() {
        hockey = new Sport("hockey", 8, 12);
        football = new Sport("football", 6, 10);

        hockeyStick = new Equipment(6L, "hockey stick", hockey, true);
    }

    @Test
    void setInUse() {
        hockeyStick.setInUse(false);
        assertThat(hockeyStick.isInUse()).isFalse();
    }

    @Test
    void setEquipmentId() {
        hockeyStick.setEquipmentId(4L);
        assertThat(hockeyStick.getEquipmentId()).isEqualTo(4L);
    }

    @Test
    void setName() {
        hockeyStick.setName("hockeyStick1");
        assertThat(hockeyStick.getName()).isEqualTo("hockeyStick1");
    }

    @Test
    void setRelatedSport() {
        hockeyStick.setRelatedSport(football);
        assertThat(hockeyStick.getRelatedSport()).isEqualTo(football);
    }
}