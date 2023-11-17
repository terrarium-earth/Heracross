package earth.terrarium.heracross.forge;

import earth.terrarium.heracross.client.HeracrossClient;
import earth.terrarium.heracross.common.Heracross;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Heracross.MOD_ID)
public class HeracrossForge {
    public HeracrossForge() {
        Heracross.init();
        if (FMLLoader.getDist().isClient()) {
            HeracrossClient.init();
        }
    }
}