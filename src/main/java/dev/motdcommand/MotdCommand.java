package dev.motdcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.permission.Permission;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public final class MotdCommand implements ModInitializer {
    @Override
    public void onInitialize() {
        MotdConfig config = MotdConfig.load(FabricLoader.getInstance().getConfigDir().resolve("simple_motd_command.json"));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment != CommandManager.RegistrationEnvironment.DEDICATED) { return; }

            dispatcher.register(CommandManager.literal("motd")
                .requires(source -> source.getPermissions().hasPermission( new Permission.Level(config.permissionLevel())))
                .then(CommandManager.argument("string", StringArgumentType.greedyString())
                    .executes(context -> {
                        String motd = formatMotd(StringArgumentType.getString(context, "string"));
                        context.getSource().getServer().setMotd(motd);
                        context.getSource().sendFeedback(() -> Text.literal(config.successPrefix() + motd), true);
                        return Command.SINGLE_SUCCESS;
                    })
                )
            );
        });
    }

    // it refuses to do a normal \n if I don't do this??
    // also the regular text formatting system isn't a thing in motds so we have to use the formatting symbol
    static String formatMotd(String input) {
        return input.replace("\\n", "\n").replace('&', '\u00a7');
    }
}
