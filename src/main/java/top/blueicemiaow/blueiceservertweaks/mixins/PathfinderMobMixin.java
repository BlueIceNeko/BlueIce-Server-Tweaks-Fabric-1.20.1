package top.blueicemiaow.blueiceservertweaks.mixins;

import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import top.blueicemiaow.blueiceservertweaks.api.Config;
import top.blueicemiaow.blueiceservertweaks.tools.ConfigHelper;

@Mixin(PathfinderMob.class)
public abstract class PathfinderMobMixin {
    @ModifyConstant(method = "tickLeash", constant = @Constant(floatValue = 10))
    private static float tickLeash(float constant) {
        return ConfigHelper.get(Config.remove_lead_length_limit) ? Float.MAX_VALUE : constant;
    }
}
