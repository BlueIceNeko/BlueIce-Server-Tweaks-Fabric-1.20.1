package top.blueicemiaow.blueiceservertweaks.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.blueicemiaow.blueiceservertweaks.api.Config;
import top.blueicemiaow.blueiceservertweaks.tools.ConfigHelper;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {
    @WrapOperation(
            method = "destroy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getDropItem()Lnet/minecraft/world/item/Item;"
            )
    )
    private Item destroy(AbstractMinecart instance, Operation<Item> original) {
        if (ConfigHelper.get(Config.minecart_drop_fix)) {
            ItemStack itemStack = new ItemStack(original.call(instance));
            if (itemStack.is(Items.CHEST_MINECART)) {
                instance.spawnAtLocation(new ItemStack(Items.MINECART));
                return Items.CHEST;
            }
            if (itemStack.is(Items.FURNACE_MINECART)) {
                instance.spawnAtLocation(new ItemStack(Items.MINECART));
                return Items.FURNACE;
            }
            if (itemStack.is(Items.HOPPER_MINECART)) {
                instance.spawnAtLocation(new ItemStack(Items.MINECART));
                return Items.HOPPER;
            }
            if (itemStack.is(Items.TNT_MINECART)) {
                instance.spawnAtLocation(new ItemStack(Items.MINECART));
                return Items.TNT;
            }
            if (itemStack.is(Items.COMMAND_BLOCK_MINECART)) {
                instance.spawnAtLocation(new ItemStack(Items.MINECART));
                return Items.COMMAND_BLOCK;
            }
        }
        return original.call(instance);
    }
}
