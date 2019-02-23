package fok.stream.action;

public interface Action<Input, Output> {
    Output action(Input input);
}