package top.blueicemiaow.blueiceservertweaks.gameplay;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import top.blueicemiaow.blueiceservertweaks.api.Config;
import top.blueicemiaow.blueiceservertweaks.api.References;
import top.blueicemiaow.blueiceservertweaks.tools.ConfigHelper;

public class FletchingTable {
    public record Stew(String id, int duration) {}

    public static InteractionResult setStew(Player entity, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (ConfigHelper.get(Config.fletching_table_set_stew) && check(entity, level, pos)) {
            ServerPlayer player = (ServerPlayer) entity;
            ItemStack mainHand = player.getMainHandItem().copy();
            if (mainHand.is(Items.MUSHROOM_STEW)) {
                ItemStack offHand = player.getOffhandItem().copy();
                if (offHand.is(Items.POTION)) {
                    CompoundTag potionNbt = offHand.getTag();
                    if (potionNbt != null) {
                        Stew stewType = References.stews.get(potionNbt.getString("Potion"));
                        if (stewType != null) {
                            CompoundTag stewNbt;
                            try {
                                stewNbt = TagParser.parseTag("{Effects:[{EffectId:" + stewType.id() + ",EffectDuration:" + stewType.duration() + "}]}");
                            } catch (CommandSyntaxException e) {
                                e.printStackTrace();
                                return InteractionResult.PASS;
                            }
                            CompoundTag mushroomNbt = offHand.getTag();
                            if (mushroomNbt != null) {
                                stewNbt.merge(mushroomNbt);
                            }
                            ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
                            stew.setTag(stewNbt);
                            stew.setCount(mainHand.getCount());
                            player.setItemInHand(InteractionHand.MAIN_HAND, stew);
                            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                            bottle.setCount(1);
                            player.setItemInHand(InteractionHand.OFF_HAND, bottle);
                            player.getInventory().setChanged();
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult tipArrow(Player entity, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (ConfigHelper.get(Config.fletching_table_tip_arrow) && check(entity, level, pos)) {
            ServerPlayer player = (ServerPlayer) entity;
            ItemStack mainHand = player.getMainHandItem().copy();
            if (mainHand.is(Items.ARROW)) {
                ItemStack offHand = player.getOffhandItem().copy();
                if (offHand.is(Items.POTION)) {
                    CompoundTag potionNbt = offHand.getTag();
                    if (potionNbt != null) {
                        ItemStack arrow = new ItemStack(Items.TIPPED_ARROW);
                        arrow.setTag(potionNbt.copy());
                        arrow.setCount(mainHand.getCount());
                        player.setItemInHand(InteractionHand.MAIN_HAND, arrow);
                        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                        bottle.setCount(1);
                        player.setItemInHand(InteractionHand.OFF_HAND, bottle);
                        player.getInventory().setChanged();
                        return InteractionResult.SUCCESS;
                    }
                } else if (offHand.is(Items.GLOWSTONE_DUST)) {
                    ItemStack arrow = new ItemStack(Items.SPECTRAL_ARROW);
                    arrow.setCount(mainHand.getCount());
                    player.setItemInHand(InteractionHand.MAIN_HAND, arrow);
                    player.getOffhandItem().shrink(1);
                    player.getInventory().setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult modifyCrossbow(Player entity, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (check(entity, level, pos)) {
            ServerPlayer player = (ServerPlayer) entity;
            if (player.getMainHandItem().is(Items.CROSSBOW)) {
                CompoundTag barrel = player.getMainHandItem().getOrCreateTag();
                if (player.isShiftKeyDown() && barrel.getBoolean("with_barrel")) {
                    barrel.putBoolean("with_barrel", false);
                    player.getMainHandItem().setTag(barrel);
                    ItemStack rod = new ItemStack(Items.LIGHTNING_ROD);
                    rod.setCount(1);
                    player.addItem(rod);
                    player.getInventory().setChanged();
                    return InteractionResult.SUCCESS;
                }
                if (ConfigHelper.get(Config.fletching_table_assemble_barrel) && player.getOffhandItem().is(Items.LIGHTNING_ROD) && !player.isShiftKeyDown() && !barrel.getBoolean("with_barrel")) {
                    barrel.putBoolean("with_barrel", true);
                    player.getMainHandItem().setTag(barrel);
                    player.getOffhandItem().shrink(1);
                    player.getInventory().setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static boolean check(Player entity, Level level, BlockPos pos) {
        return entity instanceof ServerPlayer && level instanceof ServerLevel && level.getBlockState(pos).is(Blocks.FLETCHING_TABLE);
    }
}
