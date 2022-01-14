package nl.tudelft.sem.sportfacilities.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    void equipmentConstructorTest() {
        Equipment soccerBall = new Equipment("soccer ball", football, false);
        assertThat(soccerBall.getName()).isEqualTo("soccer ball");
        assertThat(soccerBall.getRelatedSport()).isEqualTo(football);
        assertFalse(soccerBall.isInUse());
    }

    @Test
    void setInUseTest() {
        hockeyStick.setInUse(false);
        assertThat(hockeyStick.isInUse()).isFalse();
    }

    @Test
    void setEquipmentIdTest() {
        hockeyStick.setEquipmentId(4L);
        assertThat(hockeyStick.getEquipmentId()).isEqualTo(4L);
    }

    @Test
    void setNameTest() {
        hockeyStick.setName("hockeyStick1");
        assertThat(hockeyStick.getName()).isEqualTo("hockeyStick1");
    }

    @Test
    void setRelatedSportTest() {
        hockeyStick.setRelatedSport(football);
        assertThat(hockeyStick.getRelatedSport()).isEqualTo(football);
    }

    @Test
    void equalsTest() {
        Equipment hockeyStick1 = hockeyStick;
        assertTrue(hockeyStick.equals(hockeyStick1));
    }

    @Test
    void notEqualsTest() {
        Equipment basketBall = null;
        assertFalse(hockeyStick.equals(basketBall));
    }

    @Test
    void toStringTest() {
        String res = "Equipment{equipmentId=6, name='hockey stick', relatedSport=hockey}";
        assertThat(hockeyStick.toString()).isEqualTo(res);
    }
}