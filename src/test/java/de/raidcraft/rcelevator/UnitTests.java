package de.raidcraft.rcelevator;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.raidcraft.rcelevator.utils.BlockDataUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTests {

    private ServerMock server;
    private RCElevator plugin;

    @BeforeEach
    void setUp() {

        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(RCElevator.class);
    }

    @AfterEach
    void tearDown() {

        MockBukkit.unmock();
    }

    @Nested
    @DisplayName("ElevatorSign")
    class ElevatorSignTest {

        @Test
        @DisplayName("place-elevator-sign")
        void placeElevatorSign() {

            PlayerMock player = server.addPlayer();

            Block block = player.getWorld().getBlockAt(player.getLocation());
            if(!(block.getState() instanceof Sign)) {
                block.setType(Material.BIRCH_SIGN);
            }

            Sign sign = (Sign) block.getState();

            sign.setLine(0, "Etage EG");
            sign.setLine(1, RCElevator.SIGN_TAG);
            sign.setLine(2, "UG1 - OG1");
            sign.setLine(3, "OG1");

            assertThat(ElevatorSign.isElevatorSign(sign)).isTrue();

            sign.setLine(1, "[NotAnElevator]");
            assertThat(ElevatorSign.isElevatorSign(sign)).isFalse();
        }

        @Test
        @DisplayName("block-facing")
        void blockFacing() {

            BlockData wallSignBlockData = new FacingBlockData(Material.BIRCH_WALL_SIGN, BlockFace.WEST);
            BlockFace wallSignFacing = BlockDataUtils.getFacing(wallSignBlockData);
            assertThat(wallSignFacing == BlockFace.WEST).isTrue();

            BlockData postSignBlockData = new FacingBlockData(Material.BIRCH_SIGN, BlockFace.WEST);
            BlockFace postSignFacing = BlockDataUtils.getFacing(postSignBlockData);
            assertThat(postSignFacing == BlockFace.WEST).isTrue();
        }
    }
}
