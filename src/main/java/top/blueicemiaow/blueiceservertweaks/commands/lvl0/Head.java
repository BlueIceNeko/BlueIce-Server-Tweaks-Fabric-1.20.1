package top.blueicemiaow.blueiceservertweaks.commands.lvl0;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import top.blueicemiaow.blueiceservertweaks.BlueIceServerTweaks;
import top.blueicemiaow.blueiceservertweaks.tools.CommandHelper;
import top.blueicemiaow.blueiceservertweaks.tools.PermissionHelper;
import top.blueicemiaow.blueiceservertweaks.tools.TranslationHelper;

public class Head {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("head").then(Commands.argument("name", StringArgumentType.greedyString()).executes(arguments -> {
            if (arguments.getSource().getEntity() instanceof ServerPlayer player && PermissionHelper.check(player, "head")) {
                String name = StringArgumentType.getString(arguments, "name");
                String snbt = "{SkullOwner:" + name + "}";
                CompoundTag nbt;
                try {
                    nbt = TagParser.parseTag(snbt);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                    return 0;
                }
                ItemStack head = new ItemStack(Blocks.PLAYER_HEAD);
                head.setTag(nbt);
                head.setCount(1);
                player.getInventory().add(head);
                BlueIceServerTweaks.WRAPPER.info(CommandHelper.getEntityInfos(player) + TranslationHelper.get("get_head") + name + TranslationHelper.get("of_head"));
            }
            return 0;
        })));
    }
}
