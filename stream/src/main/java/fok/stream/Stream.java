package fok.stream;

public class Stream {
    boolean mScheduled;

    public static <Input> Spray<Input, Input> from(Input input) {
        Stream stream = new Stream();

        Spray<Input, Input> spray = new Spray<>(stream);
        spray.mInput = input;
        return spray;
    }
}
