package dev.motdcommand;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minecraft.command.permission.PermissionLevel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

record MotdConfig(String successPrefix, PermissionLevel permissionLevel) {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create(); // everything in java has a fuckass name like this I do not understand

    static MotdConfig load(Path path) {
        try {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                JsonObject defaults = new JsonObject();
                defaults.addProperty("success_prefix", "Server MOTD set to: ");
                defaults.addProperty("permission_level", PermissionLevel.GAMEMASTERS.name());
                Files.writeString(path, GSON.toJson(defaults), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            }

            JsonElement parsed = JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8));
            if (!parsed.isJsonObject()) {
                throw new IllegalArgumentException("Expected a JSON object");
            }
            JsonObject object = parsed.getAsJsonObject();
            String prefix = requiredString(object, "success_prefix");
            String level = requiredString(object, "permission_level");
            return new MotdConfig(prefix, PermissionLevel.valueOf(level));
        } catch (IOException | JsonParseException | IllegalArgumentException e) {
            throw new IllegalStateException("Failed to load Simple MOTD Command config at " + path + ": " + e.getMessage(), e);
        }
    }

    private static String requiredString(JsonObject object, String key) {
        JsonElement element = object.get(key);
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("'" + key + "' must be a string");
        }
        return element.getAsString();
    }
}
