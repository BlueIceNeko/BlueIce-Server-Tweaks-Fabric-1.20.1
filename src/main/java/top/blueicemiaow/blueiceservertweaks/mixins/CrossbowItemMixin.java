package top.blueicemiaow.blueiceservertweaks.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.blueicemiaow.blueiceservertweaks.api.Config;
import top.blueicemiaow.blueiceservertweaks.tools.ConfigHelper;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @WrapOperation(
        method = "use",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FF)V"
        )
    )
    private void use(Level level, LivingEntity entity, InteractionHand interactionHand, ItemStack itemStack, float f, float g, Operation<Void> original) {
        if (ConfigHelper.get(Config.fletching_table_assemble_barrel) && itemStack.getOrCreateTag().getBoolean("with_barrel")) {
            original.call(level, entity, interactionHand, itemStack, (float) (f * 1.5), 0.5f);
        } else {
            original.call(level, entity, interactionHand, itemStack, f, g);
        }
    }
}
