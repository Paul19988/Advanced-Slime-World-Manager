package com.grinderwolf.swm.plugin.upgrade;

import com.grinderwolf.swm.nms.CraftSlimeWorld;
import com.grinderwolf.swm.plugin.SWMPlugin;
import com.grinderwolf.swm.plugin.log.Logging;
import com.grinderwolf.swm.plugin.upgrade.v1_14.v1_14WorldUpgrade;
import com.grinderwolf.swm.plugin.upgrade.v1_16.v1_16WorldUpgrade;
import java.util.HashMap;
import java.util.Map;

public final class WorldUpgrader {

  private static final Map<Byte, Upgrade> upgrades = new HashMap<>();

  static {
    WorldUpgrader.upgrades.put((byte) 0x05, new v1_14WorldUpgrade());
    WorldUpgrader.upgrades.put((byte) 0x07, new v1_16WorldUpgrade());
    // Todo we need a 1_14_WorldUpgrade class as well for 0x06
  }

  public static void downgradeWorld(final CraftSlimeWorld world) {
    final byte serverVersion = SWMPlugin.getInstance().getNms().getWorldVersion();
    for (byte ver = world.getVersion(); ver > serverVersion; ver--) {
      final Upgrade upgrade = WorldUpgrader.upgrades.get(ver);
      if (upgrade == null) {
        Logging.warning("Missing world upgrader for version " + ver + ". World will not be downgraded.");
        continue;
      }
      upgrade.downgrade(world);
    }
    world.setVersion(serverVersion);
  }

  public static void upgradeWorld(final CraftSlimeWorld world) {
    final byte serverVersion = SWMPlugin.getInstance().getNms().getWorldVersion();
    for (byte ver = (byte) (world.getVersion() + 1); ver <= serverVersion; ver++) {
      final Upgrade upgrade = WorldUpgrader.upgrades.get(ver);
      if (upgrade == null) {
        Logging.warning("Missing world upgrader for version " + ver + ". World will not be upgraded.");
        continue;
      }
      upgrade.upgrade(world);
    }
    world.setVersion(serverVersion);
  }
}
