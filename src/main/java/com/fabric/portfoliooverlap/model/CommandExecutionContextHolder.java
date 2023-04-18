package com.fabric.portfoliooverlap.model;

public class CommandExecutionContextHolder {
  public static final ThreadLocal<CommandExecutionContext> commandExecutionContextHolder =
      new ThreadLocal<>();

  public static CommandExecutionContext getCommandExecutionContext() {
    return commandExecutionContextHolder.get();
  }

  public static void setCommandExecutionContext(CommandExecutionContext commandExecutionContext) {
    commandExecutionContextHolder.set(commandExecutionContext);
  }

  public static void clearCommandExecutionContext() {
    commandExecutionContextHolder.remove();
  }
}
