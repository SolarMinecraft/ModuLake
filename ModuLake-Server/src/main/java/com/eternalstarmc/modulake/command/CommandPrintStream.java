package com.eternalstarmc.modulake.command;

import java.io.OutputStream;
import java.io.PrintStream;

@Deprecated
public class CommandPrintStream extends PrintStream {
    @Deprecated
    private final String prompt;

    @Deprecated
    public CommandPrintStream(OutputStream out, String prompt) {
        super(out);
        this.prompt = prompt;
    }

    @Deprecated
    @Override
    public void println(String x) {
        super.println(x);
        showPrompt();
    }

    @Deprecated
    private void showPrompt() {
        super.printf(prompt);
    }
}
