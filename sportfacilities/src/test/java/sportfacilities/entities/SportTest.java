package sportfacilities.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class SportTest {

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

    private final transient Equipment hockeyStick;
    private final transient Equipment soccerBall;

    public SportTest() {
        soccer = new Sport("soccer", 6, 11);
        hockey = new Sport("hockey", 7, 14);
        volleyball = new Sport("volleyball", 4, 12);
        tennis = new Sport("tennis", 4, 13);
        yoga = new Sport("yoga");
        zumba = new Sport("zumba");
        kickboxing = new Sport("kickbox");

        hallX1 = new SportRoom("X1", List.of(soccer, hockey), 10, 50, true);
        hallX2 = new SportRoom("X2", List.of(hockey, volleyball, tennis, zumba), 15, 60, true);
        hallX3 = new SportRoom("X3", List.of(yoga, zumba, kickboxing), 1, 55, true);
        hockeyField = new SportRoom("hockeyfieldA", List.of(hockey), 10, 200, false);

        hockeyStick = new Equipment(6L, "hockey stick", hockey, true);
        soccerBall = new Equipment("soccer ball", soccer, false);

    }

    @Test
    void setSportNameTest() {
        soccer.setSportName("soccer1");
        assertThat(soccer.getSportName()).isEqualTo("soccer1");
    }

    @Test
    void addSportToSportLocationTest() {
        soccer.setSportLocations(new ArrayList<>());
        soccer.addSportToSportLocation(hallX3);
        assertThat(soccer.getSportLocations().size()).isEqualTo(1);
        assertThat(soccer.getSportLocations().get(0)).isEqualTo(hallX3);
    }

    @Test
    void addSportToSportLocationThrowsExceptionTest() {
        soccer.setSportLocations(new ArrayList<>());
        soccer.addSportToSportLocation(hallX3);
        IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> soccer.addSportToSportLocation(hallX3),
            "soccer already exists for this sport location"
        );
    }

    @Test
    void setTeamSportTest() {
        yoga.setTeamSport(true);
        assertThat(yoga.isTeamSport()).isTrue();
    }

    @Test
    void setMinTeamSizeTest() {
        hockey.setMinTeamSize(10);
        assertThat(hockey.getMinTeamSize()).isEqualTo(10);
    }

    @Test
    void setMaxTeamSizeTest() {
        volleyball.setMaxTeamSize(15);
        System.out.println(volleyball.toString());
        assertThat(volleyball.getMaxTeamSize()).isEqualTo(15);
    }

    @Test
    void notEqualsTest() {
        Sport soccer1 = null;
        assertFalse(soccer.equals(soccer1));
    }

    @Test
    void setSportLocationsTest() {
        soccer.setSportLocations(Arrays.asList(hallX1, hallX2));
        assertThat(soccer.getSportLocations().size()).isEqualTo(2);
        assertThat(soccer.getSportLocations().get(0)).isEqualTo(hallX1);
        assertThat(soccer.getSportLocations().get(1)).isEqualTo(hallX2);
    }

    @Test
    void setEquipmentListTest() {
        soccer.setEquipmentList(Arrays.asList(soccerBall, hockeyStick));
        assertThat(soccer.getEquipmentList().size()).isEqualTo(2);
        assertThat(soccer.getEquipmentList().get(0)).isEqualTo(soccerBall);
        assertThat(soccer.getEquipmentList().get(1)).isEqualTo(hockeyStick);
    }

    @Test
    void toStringTest() {
        String res = "Sport{sportName='soccer', teamSport=true, minTeamSize=6, maxTeamSize=11}";
        assertThat(soccer.toString()).isEqualTo(res);
    }
}