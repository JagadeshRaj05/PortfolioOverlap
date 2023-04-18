package com.fabric.portfoliooverlap.service;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommandFactory {

  private final List<Command> commands;

  public Command getCommand(Class commandClassType) {
    return commands.stream()
        .filter(command -> command.getClass().equals(commandClassType))
        .findFirst()
        .orElseThrow(
            () ->
                new InvalidCommandException(
                    String.format("Invalid command class type %s given", commandClassType)));
  }
}
