package fok.stream.action;

public interface AsyncAction<Input, Output> {
    void action(Input input, Result<Output> result);

    interface Result<Output> {
        void result(Output output);
    }
}