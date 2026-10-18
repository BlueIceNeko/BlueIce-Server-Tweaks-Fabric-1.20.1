package top.blueicemiaow.blueiceservertweaks.commands.lvl0;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import top.blueicemiaow.blueiceservertweaks.BlueIceServerTweaks;
import top.blueicemiaow.blueiceservertweaks.tools.CommandHelper;
import top.blueicemiaow.blueiceservertweaks.tools.PermissionHelper;
import top.blueicemiaow.blueiceservertweaks.tools.TranslationHelper;

public class Compass {
    public static void run(ServerPlayer player, BlockPos location, String name) {
        if (player.getMainHandItem().is(Items.COMPASS)) {
            String locationString = CommandHelper.getLocationString(location);
            String snbt = "{LodestoneDimension:\"" + player.level().dimension().location() + "\",LodestonePos:{X:" + location.getX() + ",Y:" + location.getY() + ",Z:" + location.getZ() + "},LodestoneTracked:0b,display:{Lore:[\"{\\\"text\\\":\\\"" + TranslationHelper.get("location") + locationString + "\\\"}\"]" + (name == null ? "" : ",Name:\"{\\\"text\\\":\\\"" + name + "\\\"}\"") + "}}";
            CompoundTag nbt;
            try {
                nbt = TagParser.parseTag(snbt);
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
                return;
            }
            player.getMainHandItem().setTag(nbt);
            player.getInventory().setChanged();
            BlueIceServerTweaks.WRAPPER.info(CommandHelper.getEntityInfos(player) + TranslationHelper.get("get_compass") + locationString);
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("compass").then(Commands.argument("location", BlockPosArgument.blockPos()).then(Commands.argument("name", StringArgumentType.greedyString()).executes(arguments -> {
            if (arguments.getSource().getEntity() instanceof ServerPlayer player && PermissionHelper.check(player, "compass")) {
                run(player, BlockPosArgument.getLoadedBlockPos(arguments, "location"), StringArgumentType.getString(arguments, "name"));
            }
            return 0;
        })).executes(arguments -> {
            if (arguments.getSource().getEntity() instanceof ServerPlayer player && PermissionHelper.check(player, "compass")) {
                run(player, BlockPosArgument.getLoadedBlockPos(arguments, "location"), null);
            }
            return 0;
        })));
    }
}
