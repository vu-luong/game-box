package com.tvd12.gamebox.testing;

import com.tvd12.gamebox.entity.MMOGridRoom;
import com.tvd12.gamebox.entity.MMOPlayer;
import com.tvd12.gamebox.math.Vec3;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.performance.Performance;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MMOGridRoomTest {
    
    public static void main(String[] args) {
        final MMOGridRoom room = (MMOGridRoom) MMOGridRoom.builder()
            .maxX(50)
            .maxY(50)
            .maxZ(50)
            .cellSize(5)
            .maxPlayer(800)
            .distanceOfInterest(18)
            .build();
        
        for (int i = 0; i < room.getMaxPlayer(); ++i) {
            final MMOPlayer player = MMOPlayer.builder()
                .name("player" + i)
                .build();
            room.setPlayerPosition(
                player,
                new Vec3(
                    RandomUtil.randomFloat(0, room.getMaxX()),
                    RandomUtil.randomFloat(0, room.getMaxY()),
                    RandomUtil.randomFloat(0, room.getMaxZ())
                )
            );
            room.addPlayer(player);
        }
        
        final long elapsedTime = Performance
            .create()
            .loop(1)
            .test(room::update)
            .getTime();
        System.out.println("elapsed time: " + elapsedTime);
    }
    
    @Test
    public void createMMOGridRoomTest() {
        // given
        int maxX = RandomUtil.randomInt(50, 100);
        int maxY = RandomUtil.randomInt(50, 100);
        int maxZ = RandomUtil.randomInt(50, 100);
        int cellSize = RandomUtil.randomSmallInt();
        int maxPlayer = RandomUtil.randomSmallInt();
        
        // when
        final MMOGridRoom room = (MMOGridRoom) MMOGridRoom.builder()
            .maxX(maxX)
            .maxY(maxY)
            .maxZ(maxZ)
            .cellSize(cellSize)
            .maxPlayer(maxPlayer)
            .distanceOfInterest(RandomUtil.randomSmallDouble())
            .build();
        
        // then
        Asserts.assertEquals(room.getMaxX(), maxX);
        Asserts.assertEquals(room.getMaxY(), maxY);
        Asserts.assertEquals(room.getMaxZ(), maxZ);
        Asserts.assertEquals(room.getCellSize(), cellSize);
    }
    
    @Test
    public void setPlayerPositionTest() {
        // given
        final MMOGridRoom room = (MMOGridRoom) MMOGridRoom.builder()
            .maxX(50)
            .maxY(50)
            .maxZ(50)
            .cellSize(5)
            .maxPlayer(3)
            .distanceOfInterest(RandomUtil.randomSmallDouble())
            .build();

        Vec3 expectedPosition = new Vec3(40, 40, 40);
        MMOPlayer player = new MMOPlayer("player");
        
        // when
        room.setPlayerPosition(player, expectedPosition);
        
        // then
        Asserts.assertEquals(player.getPosition(), expectedPosition);
    }

    @Test
    public void updateMMOGridRoomTest() {
        // given
        final MMOGridRoom room = (MMOGridRoom) MMOGridRoom.builder()
            .maxX(50)
            .maxY(50)
            .maxZ(50)
            .cellSize(5)
            .maxPlayer(3)
            .distanceOfInterest(RandomUtil.randomSmallDouble())
            .build();

        MMOPlayer player1 = new MMOPlayer("player1");
        player1.setPosition(new Vec3(0, 0, 0));
        room.addPlayer(player1);

        MMOPlayer player2 = new MMOPlayer("player2");
        player2.setPosition(new Vec3(0, 0, 0));
        room.addPlayer(player2);

        MMOPlayer player3 = new MMOPlayer("player3");
        player3.setPosition(new Vec3(0, 0, 0));
        room.addPlayer(player3);
        
        // when
        room.setPlayerPosition(player1, new Vec3(0, 0, 0));
        room.setPlayerPosition(player2, new Vec3(1, 1, 1));
        room.setPlayerPosition(player3, new Vec3(25, 25, 25));
        room.update();

        // then
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();
        List<String> buffer3 = new ArrayList<>();

        player1.getNearbyPlayerNames(buffer1);
        player2.getNearbyPlayerNames(buffer2);
        player3.getNearbyPlayerNames(buffer3);

        List<String> expectedNearbyPlayerNames1 =
            new ArrayList<>(((Map<String, MMOPlayer>) FieldUtil.getFieldValue(player1, "nearbyPlayers"))
                                .keySet());
        List<String> expectedNearbyPlayerNames2 =
            new ArrayList<>(((Map<String, MMOPlayer>) FieldUtil.getFieldValue(player2, "nearbyPlayers"))
                                .keySet());
        List<String> expectedNearbyPlayerNames3 =
            new ArrayList<>(((Map<String, MMOPlayer>) FieldUtil.getFieldValue(player3, "nearbyPlayers"))
                                .keySet());

        Asserts.assertEquals(buffer1, expectedNearbyPlayerNames1);
        Asserts.assertEquals(buffer2, expectedNearbyPlayerNames2);
        Asserts.assertEquals(buffer3, expectedNearbyPlayerNames3);
    }
}