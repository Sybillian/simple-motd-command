# Simple MOTD Command

An extremely simple mod to let server admins and datapack creators set the server MOTD with a command.

Syntax is `/motd <string>`. It does not currently support the text component format, so use formatting codes. It will replace `&` with `§` to make that a bit easier, but you can also use the actual `§` character.

The config file will be created at config/simple_motd_command.json. Options are as follows:

`success_prefix` - This can be any string and is sent as command feedback when running /motd.
`permission_level` - Sets the command permission level required to run the command. Legal options are `ALL`, `MODERATORS`, `GAMEMASTERS`, `ADMINS`, and `OWNERS`. Defaults to GAMEMASTERS, which allows it to be run by opped players and (by default) functions.

Please feel free to do literally anything you want with the extremely complex and expert-level code.