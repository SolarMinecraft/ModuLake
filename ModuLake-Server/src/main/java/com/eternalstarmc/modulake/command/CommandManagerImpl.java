package com.eternalstarmc.modulake.command;

import com.eternalstarmc.modulake.api.Impl;
import com.eternalstarmc.modulake.api.commands.Command;
import com.eternalstarmc.modulake.api.commands.CommandManager;
import com.eternalstarmc.modulake.api.plugin.PluginBase;
import com.eternalstarmc.modulake.api.utils.NameSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.eternalstarmc.modulake.Main.CONSOLE;


@Impl("COMMAND_SYSTEM, CommandManagerImpl")
public class CommandManagerImpl implements CommandManager {
    private final Map<NameSpace, Command> commands = new ConcurrentHashMap<>();
    private final Map<String, Command> stringCommands = new ConcurrentHashMap<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private Thread commandThread;

    private static final Logger logger = LoggerFactory.getLogger(CommandManagerImpl.class);

    @Override
    public Command getCommand(NameSpace nameSpace) {
        return commands.get(nameSpace);
    }

    @Override
    public Command newCommand(PluginBase plugin, String name, String description, String... aliases) {
        return newCommand(plugin, name, aliases, description);
    }

    public Map<NameSpace, Command> getCommands () {
        return commands;
    }

    private void registerCommand(Command command) {
        commands.put(command.getNameSpace(), command);
        stringCommands.put(command.getName(), command);
        stringCommands.put(command.getNameSpace().getNamespace(), command);
        for (String alias : command.getAliases()) {
            stringCommands.put(alias, command);
            stringCommands.put(command.getNameSpace().getKey() + ":" + alias, command);
        }
    }

    @Override
    public Command newCommand(PluginBase plugin, String name, String[] aliases, String description) {
        Command command = new CommandImpl(plugin, name, aliases, description);
        registerCommand(command);
        return command;
    }

    public void startListening () {
        if (isRunning.get()) return;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        commandThread = new Thread(() -> {
            while (true) {
                Command[] c = { null };
                try {
                    System.out.print("> ");
                    String input = reader.readLine();
                    String[] args0 = input.split(" ");
                    stringCommands.forEach((name, command) -> {
                        if (!(args0.length < 1)) {
                            if (name.equals(args0[0])) {
                                c[0] = command;
                                String[] args = new String[args0.length - 1];
                                System.arraycopy(args0, 1, args, 0, args.length);
                                command.execute(CONSOLE, args0[0], args);
                            }
                        }
                    });
                } catch (Exception e) {
                    if (c[0] == null) {
                        logger.error("在处理命令时发生异常，", e);
                    } else {
                        logger.error("在命令监听器 {} 中有一个未被处理的异常！", c[0].getClass().getName());
                    }
                }
            }
        }, "COMMAND-THREAD");
        commandThread.start();
        isRunning.set(true);
    }

    public void cleanup() {
        commands.clear();
        stringCommands.clear();
    }

    public Thread getCommandThread () {
        return commandThread;
    }
}
